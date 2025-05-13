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

        // Initialize technical factors - only need these for the new formula
        List<TechnicalFactor> technicalFactors = Arrays.stream(TechnicalFactor.getDefaultFactors())
                .map(tf -> {
                    tf.setCalculation(finalCalculation); // Use the final variable
                    return tf;
                })
                .collect(Collectors.toList());
        technicalFactorRepository.saveAll(technicalFactors);

        // No need to initialize environmental factors as they aren't used in the new formula

        return savedCalculation; // Return the saved entity
    }    
    @Transactional
    public UcpCalculation calculate(Long calculationId) {
        UcpCalculation calculation = ucpCalculationRepository.findById(calculationId)
                .orElseThrow(() -> new IllegalArgumentException("Calculation not found"));

        Project project = calculation.getProject();
        Long projectId = project.getId();

        // Chỉ lấy technical factors
        List<TechnicalFactor> technicalFactors = technicalFactorRepository.findByCalculationId(calculationId);

        // Calculate UAW (Unadjusted Actor Weight)
        double uaw = actorService.calculateUAW(projectId);
        calculation.setUaw(uaw);

        // Calculate UUCW (Unadjusted Use Case Weight)
        double uucw = useCaseService.calculateUUCW(projectId);
        calculation.setUucw(uucw);

        // Calculate UUCP (Unadjusted Use Case Points)
        double uucp = uaw + uucw;
        calculation.setUucp(uucp);

        // Calculate TDI (Total Degree of Influence) - tổng của 14 yếu tố kỹ thuật (thang điểm 0-5)
        double tdi = technicalFactors.stream()
                .mapToDouble(TechnicalFactor::getScore)
                .sum();
        calculation.setTdi(tdi);
        
        // Calculate VAF (Value Adjustment Factor) theo công thức: VAF = 0.65 + (0.01 × TDI)
        double vaf = 0.65 + (0.01 * tdi);
        calculation.setVaf(vaf);

        // Calculate UCP theo công thức: UCP = (UUCW + UAW) × VAF
        double ucp = uucp * vaf;
        calculation.setUcp(ucp);
        
        // Ước tính nỗ lực
        double estimatedEffort = ucp * 20; // Sử dụng hệ số 20 giờ/UCP đơn giản
        calculation.setEstimatedEffort(estimatedEffort);

        // Các giá trị cũ không còn sử dụng - chỉ đặt giá trị mặc định để tránh lỗi
        calculation.setTf(0);
        calculation.setTcf(0.6);
        calculation.setEf(0);
        calculation.setEcf(1.4);
        calculation.setProductivityFactor(20);

        // Lưu và trả về kết quả
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

        // Calculate Total Degree of Influence (TDI)
        double tdi = existingFactors.stream()
                .mapToDouble(TechnicalFactor::getScore)
                .sum();
        calculation.setTdi(tdi);
        
        // Calculate VAF (Value Adjustment Factor)
        double vaf = 0.65 + (0.01 * tdi);
        calculation.setVaf(vaf);

        technicalFactorRepository.saveAll(existingFactors);
        ucpCalculationRepository.save(calculation);
        
        return calculation;
    }    @Transactional
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
    
    /**
     * @deprecated Environmental factors không còn được sử dụng trong công thức UCP mới, giữ lại để tương thích ngược
     */
    @Deprecated
    @Transactional
    public UcpCalculation updateEnvironmentalFactors(Long calculationId, List<EnvironmentalFactor> updatedFactors) {
        UcpCalculation calculation = ucpCalculationRepository.findById(calculationId)
                .orElseThrow(() -> new IllegalArgumentException("Calculation not found"));
        return calculation; // Không làm gì cả, chỉ trả về calculation
    }
}
