package Group2.example.UserCasePoint.controller;

import Group2.example.UserCasePoint.dto.UseCaseDTO;
import Group2.example.UserCasePoint.model.Project;
import Group2.example.UserCasePoint.model.UseCase;
import Group2.example.UserCasePoint.model.UseCaseType;
import Group2.example.UserCasePoint.service.ProjectService;
import Group2.example.UserCasePoint.service.UseCaseService;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/projects/{projectId}/usecases")
public class UseCaseController {

    private final UseCaseService useCaseService;
    private final ProjectService projectService;

    @Autowired
    public UseCaseController(UseCaseService useCaseService, ProjectService projectService) {
        this.useCaseService = useCaseService;
        this.projectService = projectService;
    }    @GetMapping
    public String listUseCases(@PathVariable Long projectId, Model model) {
        try {
            Project project = projectService.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project ID: " + projectId));
            
            List<UseCase> useCases = useCaseService.findByProjectId(projectId);
            
            // Add count by type to model
            Map<UseCaseType, Long> useCaseCounts = new HashMap<>();
            for (UseCaseType type : UseCaseType.values()) {
                long count = useCases.stream()
                    .filter(useCase -> useCase.getType() == type)
                    .count();
                useCaseCounts.put(type, count);
            }
            
            model.addAttribute("project", project);
            model.addAttribute("useCases", useCases);
            model.addAttribute("useCaseCounts", useCaseCounts);
            return "usecases/list";
        } catch (Exception e) {
            // Log the error
            e.printStackTrace();
            // Redirect to projects list with error message
            return "redirect:/projects";
        }
    }

    @GetMapping("/create")
    public String createUseCaseForm(@PathVariable Long projectId, Model model) {
        Project project = projectService.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid project ID: " + projectId));
        
        UseCaseDTO useCaseDTO = new UseCaseDTO();
        useCaseDTO.setProjectId(projectId);
        
        model.addAttribute("useCaseDTO", useCaseDTO);
        model.addAttribute("project", project);
        model.addAttribute("useCaseTypes", UseCaseType.values());
        return "usecases/form";
    }

    @PostMapping("/create")
    public String createUseCase(@PathVariable Long projectId,
                               @Valid @ModelAttribute("useCaseDTO") UseCaseDTO useCaseDTO,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("useCaseTypes", UseCaseType.values());
            return "usecases/form";
        }        
        UseCase useCase = new UseCase();
        useCase.setName(useCaseDTO.getName());
        useCase.setTransactionCount(useCaseDTO.getTransactionCount());
        useCase.setType(useCaseDTO.getType());
        
        useCaseService.save(useCase, projectId);
        redirectAttributes.addFlashAttribute("successMessage", "Use Case created successfully!");
        return "redirect:/projects/" + projectId + "/usecases";
    }

    @GetMapping("/{id}/edit")
    public String editUseCaseForm(@PathVariable Long projectId, 
                                 @PathVariable Long id, 
                                 Model model) {
        Project project = projectService.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid project ID: " + projectId));
        
        UseCase useCase = useCaseService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid use case ID: " + id));
        
        UseCaseDTO useCaseDTO = new UseCaseDTO();
        useCaseDTO.setId(useCase.getId());
        useCaseDTO.setName(useCase.getName());
        useCaseDTO.setType(useCase.getType());
        useCaseDTO.setTransactionCount(useCase.getTransactionCount());
        useCaseDTO.setWeight(useCase.getWeight());
        useCaseDTO.setProjectId(projectId);
        
        model.addAttribute("useCaseDTO", useCaseDTO);
        model.addAttribute("project", project);
        model.addAttribute("useCaseTypes", UseCaseType.values());
        return "usecases/form";
    }

    @PostMapping("/{id}/edit")
    public String updateUseCase(@PathVariable Long projectId,
                               @PathVariable Long id,
                               @Valid @ModelAttribute("useCaseDTO") UseCaseDTO useCaseDTO,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("useCaseTypes", UseCaseType.values());
            return "usecases/form";
        }        
        UseCase useCase = useCaseService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid use case ID: " + id));
        
        useCase.setName(useCaseDTO.getName());
        useCase.setTransactionCount(useCaseDTO.getTransactionCount());
        useCase.setType(useCaseDTO.getType());
        
        useCaseService.save(useCase, projectId);
        redirectAttributes.addFlashAttribute("successMessage", "Use Case updated successfully!");
        return "redirect:/projects/" + projectId + "/usecases";
    }

    @GetMapping("/{id}/delete")
    public String deleteUseCase(@PathVariable Long projectId,
                               @PathVariable Long id,
                               RedirectAttributes redirectAttributes) {
        useCaseService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Use Case deleted successfully!");
        return "redirect:/projects/" + projectId + "/usecases";
    }
}
