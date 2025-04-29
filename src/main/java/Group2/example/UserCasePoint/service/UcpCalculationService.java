package Group2.example.UserCasePoint.service;

import Group2.example.UserCasePoint.model.*;
import Group2.example.UserCasePoint.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UcpCalculationService {

    private final UcpCalculationRepository ucpCalculationRepository;
    private final ProjectRepository projectRepository;
    private final TechnicalFactorRepository technicalFactorRepository;
    private final EnvironmentalFactorRepository environmentalFactorRepository;
    private final ActorService actorService;
    private final UseCaseService useCaseService;

    @Autowired
    public UcpCalculationService(
            UcpCalculationRepository ucpCalculationRepository,
            ProjectRepository projectRepository,
            TechnicalFactorRepository technicalFactorRepository,
            EnvironmentalFactorRepository environmentalFactorRepository,
            ActorService actorService,
            UseCaseService useCaseService) {
        this.ucpCalculationRepository = ucpCalculationRepository;
        this.projectRepository = projectRepository;
        this.technicalFactorRepository = technicalFactorRepository;
        this.environmentalFactorRepository = environmentalFactorRepository;
        this.actorService = actorService;
        this.useCaseService = useCaseService;
    }

    public List<UcpCalculation> findAll() {
        return ucpCalculationRepository.findAll();
    }

    public List<UcpCalculation> findByProjectId(Long projectId) {
        return ucpCalculationRepository.findByProjectId(projectId);
    }

    public Optional<UcpCalculation> findById(Long id) {
        return ucpCalculationRepository.findById(id);
    }

    @Transactional
    public UcpCalculation initialize(Long projectId, String calculationName) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        // Create new calculation
        UcpCalculation calculation = new UcpCalculation();
        calculation.setProject(project);
        calculation.setName(calculationName);
        // Save the calculation first to get the persisted entity with an ID
        UcpCalculation savedCalculation = ucpCalculationRepository.save(calculation);
        // Use a final variable inside the lambdas
        final UcpCalculation finalCalculation = savedCalculation;

        // Initialize technical factors
        List<TechnicalFactor> technicalFactors = Arrays.stream(TechnicalFactor.getDefaultFactors())
                .map(tf -> {
                    tf.setCalculation(finalCalculation); // Use the final variable
                    return tf;
                })
                .collect(Collectors.toList());
        technicalFactorRepository.saveAll(technicalFactors);

        // Initialize environmental factors
        List<EnvironmentalFactor> environmentalFactors = Arrays.stream(EnvironmentalFactor.getDefaultFactors())
                .map(ef -> {
                    ef.setCalculation(finalCalculation); // Use the final variable
                    return ef;
                })
                .collect(Collectors.toList());
        environmentalFactorRepository.saveAll(environmentalFactors);

        return savedCalculation; // Return the saved entity
    }    @Transactional
    public UcpCalculation calculate(Long calculationId) {
        UcpCalculation calculation = ucpCalculationRepository.findById(calculationId)
                .orElseThrow(() -> new IllegalArgumentException("Calculation not found"));

        Project project = calculation.getProject();
        Long projectId = project.getId();

        // Get all factors
        List<TechnicalFactor> technicalFactors = technicalFactorRepository.findByCalculationId(calculationId);
        List<EnvironmentalFactor> environmentalFactors = environmentalFactorRepository.findByCalculationId(calculationId);

        // Calculate UAW (Unadjusted Actor Weight)
        double uaw = actorService.calculateUAW(projectId);
        calculation.setUaw(uaw);

        // Calculate UUCW (Unadjusted Use Case Weight)
        double uucw = useCaseService.calculateUUCW(projectId);
        calculation.setUucw(uucw);

        // Calculate UUCP (Unadjusted Use Case Points)
        double uucp = uaw + uucw;
        calculation.setUucp(uucp);

        // Calculate Total Technical Factor (TF)
        double tf = technicalFactors.stream()
                .mapToDouble(factor -> factor.getWeight() * factor.getScore())
                .sum();
        calculation.setTf(tf);
        
        // Calculate TCF (Technical Complexity Factor)
        double tcf = 0.6 + (0.01 * tf);
        calculation.setTcf(tcf);
        
        // Calculate Total Environmental Factor (EF)
        double ef = environmentalFactors.stream()
                .mapToDouble(factor -> factor.getWeight() * factor.getScore())
                .sum();
        calculation.setEf(ef);

        // Calculate ECF (Environmental Complexity Factor)
        double ecf = 1.4 + (-0.03 * ef);
        calculation.setEcf(ecf);

        // Calculate UCP (Use Case Points)
        double ucp = uucp * tcf * ecf;
        calculation.setUcp(ucp);
        
        // Count significant environmental factors
        EnvironmentalFactor[] efArray = environmentalFactors.toArray(new EnvironmentalFactor[0]);
        int significantFactors = EnvironmentalFactor.countSignificantFactors(efArray);
        
        // Calculate Productivity Factor based on Environmental Significance
        double productivityFactor = calculation.calculateProductivityFactor(significantFactors);
        calculation.setProductivityFactor(productivityFactor);
        
        // Calculate Estimated Effort in hours
        double estimatedEffort = ucp * productivityFactor;
        calculation.setEstimatedEffort(estimatedEffort);

        // Save and return the calculation
        return ucpCalculationRepository.save(calculation);
    }
      @Transactional
    public UcpCalculation updateTechnicalFactors(Long calculationId, List<TechnicalFactor> updatedFactors) {
        UcpCalculation calculation = ucpCalculationRepository.findById(calculationId)
                .orElseThrow(() -> new IllegalArgumentException("Calculation not found"));

        // Get existing factors
        List<TechnicalFactor> existingFactors = technicalFactorRepository.findByCalculationId(calculationId);

        // Update existing factors with new scores
        for (int i = 0; i < existingFactors.size() && i < updatedFactors.size(); i++) {
            TechnicalFactor existing = existingFactors.get(i);
            TechnicalFactor updated = updatedFactors.get(i);
            existing.setScore(updated.getScore());
        }

        // Calculate and set TF
        double tf = existingFactors.stream()
                .mapToDouble(factor -> factor.getWeight() * factor.getScore())
                .sum();
        calculation.setTf(tf);
        
        // Calculate and set TCF
        double tcf = 0.6 + (0.01 * tf);
        calculation.setTcf(tcf);

        technicalFactorRepository.saveAll(existingFactors);
        ucpCalculationRepository.save(calculation);
        
        return calculation;
    }    @Transactional
    public UcpCalculation updateEnvironmentalFactors(Long calculationId, List<EnvironmentalFactor> updatedFactors) {
        UcpCalculation calculation = ucpCalculationRepository.findById(calculationId)
                .orElseThrow(() -> new IllegalArgumentException("Calculation not found"));

        // Get existing factors
        List<EnvironmentalFactor> existingFactors = environmentalFactorRepository.findByCalculationId(calculationId);

        // Update existing factors with new scores
        for (int i = 0; i < existingFactors.size() && i < updatedFactors.size(); i++) {
            EnvironmentalFactor existing = existingFactors.get(i);
            EnvironmentalFactor updated = updatedFactors.get(i);
            existing.setScore(updated.getScore());
        }
        
        // Calculate and set EF
        double ef = existingFactors.stream()
                .mapToDouble(factor -> factor.getWeight() * factor.getScore())
                .sum();
        calculation.setEf(ef);
        
        // Calculate and set ECF
        double ecf = 1.4 + (-0.03 * ef);
        calculation.setEcf(ecf);

        environmentalFactorRepository.saveAll(existingFactors);
        ucpCalculationRepository.save(calculation);
        
        return calculation;
    }

    // Update actual effort
    @Transactional
    public UcpCalculation updateActualEffort(Long id, Double actualEffort) {
        UcpCalculation calculation = ucpCalculationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Calculation not found"));
        calculation.setActualEffort(actualEffort);
        return ucpCalculationRepository.save(calculation);
    }
    
    // New methods for dashboard
    public List<UcpCalculation> getAllCalculations() {
        return ucpCalculationRepository.findAll();
    }
    
    public List<UcpCalculation> getRecentCalculations(int limit) {
        return ucpCalculationRepository.findAll(
            PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "id"))
        ).getContent();
    }

    @Transactional
    public void deleteById(Long id) {
        ucpCalculationRepository.deleteById(id);
    }
}
