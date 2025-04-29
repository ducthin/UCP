package Group2.example.UserCasePoint.service;

import Group2.example.UserCasePoint.model.Actor;
import Group2.example.UserCasePoint.model.Project;
import Group2.example.UserCasePoint.repository.ActorRepository;
import Group2.example.UserCasePoint.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ActorService {

    private final ActorRepository actorRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public ActorService(ActorRepository actorRepository, ProjectRepository projectRepository) {
        this.actorRepository = actorRepository;
        this.projectRepository = projectRepository;
    }

    public List<Actor> findAll() {
        return actorRepository.findAll();
    }

    public List<Actor> findByProjectId(Long projectId) {
        return actorRepository.findByProjectId(projectId);
    }

    public Optional<Actor> findById(Long id) {
        return actorRepository.findById(id);
    }

    @Transactional
    public Actor save(Actor actor, Long projectId) {
        Optional<Project> project = projectRepository.findById(projectId);
        if (project.isPresent()) {
            actor.setProject(project.get());
            return actorRepository.save(actor);
        }
        throw new IllegalArgumentException("Project with ID " + projectId + " not found");
    }

    @Transactional
    public void deleteById(Long id) {
        actorRepository.deleteById(id);
    }

    // Calculate total Unadjusted Actor Weight for a project
    public double calculateUAW(Long projectId) {
        List<Actor> actors = findByProjectId(projectId);
        return actors.stream().mapToInt(Actor::getWeight).sum();
    }
    
    // Method for dashboard
    public List<Actor> getAllActors() {
        return actorRepository.findAll();
    }
}
