package Group2.example.UserCasePoint.service;

import Group2.example.UserCasePoint.model.Project;
import Group2.example.UserCasePoint.model.UseCase;
import Group2.example.UserCasePoint.repository.ProjectRepository;
import Group2.example.UserCasePoint.repository.UseCaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UseCaseService {

    private final UseCaseRepository useCaseRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public UseCaseService(UseCaseRepository useCaseRepository, ProjectRepository projectRepository) {
        this.useCaseRepository = useCaseRepository;
        this.projectRepository = projectRepository;
    }

    public List<UseCase> findAll() {
        return useCaseRepository.findAll();
    }

    public List<UseCase> findByProjectId(Long projectId) {
        return useCaseRepository.findByProjectId(projectId);
    }

    public Optional<UseCase> findById(Long id) {
        return useCaseRepository.findById(id);
    }

    @Transactional
    public UseCase save(UseCase useCase, Long projectId) {
        Optional<Project> project = projectRepository.findById(projectId);
        if (project.isPresent()) {
            useCase.setProject(project.get());
            return useCaseRepository.save(useCase);
        }
        throw new IllegalArgumentException("Project with ID " + projectId + " not found");
    }

    @Transactional
    public void deleteById(Long id) {
        useCaseRepository.deleteById(id);
    }

    // Calculate total Unadjusted Use Case Weight for a project
    public double calculateUUCW(Long projectId) {
        List<UseCase> useCases = findByProjectId(projectId);
        return useCases.stream().mapToInt(UseCase::getWeight).sum();
    }
    
    // Method for dashboard
    public List<UseCase> getAllUseCases() {
        return useCaseRepository.findAll();
    }
}
