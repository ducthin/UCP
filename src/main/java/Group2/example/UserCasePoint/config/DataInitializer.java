package Group2.example.UserCasePoint.config;

import Group2.example.UserCasePoint.model.Project;
import Group2.example.UserCasePoint.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(@Autowired ProjectRepository projectRepository) {
        return args -> {
            // Check if we need to initialize data
            if (projectRepository.count() == 0) {
                // Create and save a default project
                Project defaultProject = new Project();
                defaultProject.setName("Sample Project");
                defaultProject.setDescription("This is a sample project created automatically to help you get started.");
                projectRepository.save(defaultProject);
                
                System.out.println("Data initialization completed: Created sample project with ID: " + defaultProject.getId());
            }
        };
    }
}
