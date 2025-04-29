package Group2.example.UserCasePoint.controller;

import Group2.example.UserCasePoint.model.Project;
import Group2.example.UserCasePoint.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public String listProjects(Model model) {
        List<Project> projects = projectService.findAll();
        model.addAttribute("projects", projects);
        return "projects/list";
    }

    @GetMapping("/create")
    public String createProjectForm(Model model) {
        model.addAttribute("project", new Project());
        return "projects/form";
    }

    @PostMapping("/create")
    public String createProject(@Valid @ModelAttribute("project") Project project,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "projects/form";
        }
        
        projectService.save(project);
        redirectAttributes.addFlashAttribute("successMessage", "Project created successfully!");
        return "redirect:/projects";
    }

    @GetMapping("/{id}")
    public String viewProject(@PathVariable Long id, Model model) {
        Project project = projectService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid project ID: " + id));
        
        model.addAttribute("project", project);
        return "projects/view";
    }

    @GetMapping("/{id}/edit")
    public String editProjectForm(@PathVariable Long id, Model model) {
        Project project = projectService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid project ID: " + id));
        
        model.addAttribute("project", project);
        return "projects/form";
    }

    @PostMapping("/{id}/edit")
    public String updateProject(@PathVariable Long id,
                               @Valid @ModelAttribute("project") Project project,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "projects/form";
        }
        
        project.setId(id);
        projectService.save(project);
        redirectAttributes.addFlashAttribute("successMessage", "Project updated successfully!");
        return "redirect:/projects";
    }

    @GetMapping("/{id}/delete")
    public String deleteProject(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        projectService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Project deleted successfully!");
        return "redirect:/projects";
    }
}
