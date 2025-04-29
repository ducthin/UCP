package Group2.example.UserCasePoint.controller;

import Group2.example.UserCasePoint.dto.ActorDTO;
import Group2.example.UserCasePoint.model.Actor;
import Group2.example.UserCasePoint.model.ActorType;
import Group2.example.UserCasePoint.model.Project;
import Group2.example.UserCasePoint.service.ActorService;
import Group2.example.UserCasePoint.service.ProjectService;
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
@RequestMapping("/projects/{projectId}/actors")
public class ActorController {

    private final ActorService actorService;
    private final ProjectService projectService;

    @Autowired
    public ActorController(ActorService actorService, ProjectService projectService) {
        this.actorService = actorService;
        this.projectService = projectService;
    }    
    @GetMapping
    public String listActors(@PathVariable Long projectId, Model model) {
        try {
            Project project = projectService.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project ID: " + projectId));
            
            List<Actor> actors = actorService.findByProjectId(projectId);
            
            // Add count by type to model
            Map<ActorType, Long> actorCounts = new HashMap<>();
            for (ActorType type : ActorType.values()) {
                long count = actors.stream()
                    .filter(actor -> actor.getType() == type)
                    .count();
                actorCounts.put(type, count);
            }
            
            model.addAttribute("project", project);
            model.addAttribute("actors", actors);
            model.addAttribute("actorCounts", actorCounts);
            return "actors/list";
        } catch (Exception e) {
            // Log the error
            e.printStackTrace();
            // Redirect to projects list with error message
            return "redirect:/projects";
        }
    }

    @GetMapping("/create")
    public String createActorForm(@PathVariable Long projectId, Model model) {
        Project project = projectService.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid project ID: " + projectId));
        
        ActorDTO actorDTO = new ActorDTO();
        actorDTO.setProjectId(projectId);
        
        model.addAttribute("actorDTO", actorDTO);
        model.addAttribute("project", project);
        model.addAttribute("actorTypes", ActorType.values());
        return "actors/form";
    }

    @PostMapping("/create")
    public String createActor(@PathVariable Long projectId,
                             @Valid @ModelAttribute("actorDTO") ActorDTO actorDTO,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("actorTypes", ActorType.values());
            return "actors/form";
        }
        
        Actor actor = new Actor();
        actor.setName(actorDTO.getName());
        actor.setType(actorDTO.getType());
        
        actorService.save(actor, projectId);
        redirectAttributes.addFlashAttribute("successMessage", "Actor created successfully!");
        return "redirect:/projects/" + projectId + "/actors";
    }

    @GetMapping("/{id}/edit")
    public String editActorForm(@PathVariable Long projectId, 
                               @PathVariable Long id, 
                               Model model) {
        Project project = projectService.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid project ID: " + projectId));
        
        Actor actor = actorService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid actor ID: " + id));
        
        ActorDTO actorDTO = new ActorDTO();
        actorDTO.setId(actor.getId());
        actorDTO.setName(actor.getName());
        actorDTO.setType(actor.getType());
        actorDTO.setWeight(actor.getWeight());
        actorDTO.setProjectId(projectId);
        
        model.addAttribute("actorDTO", actorDTO);
        model.addAttribute("project", project);
        model.addAttribute("actorTypes", ActorType.values());
        return "actors/form";
    }

    @PostMapping("/{id}/edit")
    public String updateActor(@PathVariable Long projectId,
                             @PathVariable Long id,
                             @Valid @ModelAttribute("actorDTO") ActorDTO actorDTO,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("actorTypes", ActorType.values());
            return "actors/form";
        }
        
        Actor actor = actorService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid actor ID: " + id));
        
        actor.setName(actorDTO.getName());
        actor.setType(actorDTO.getType());
        
        actorService.save(actor, projectId);
        redirectAttributes.addFlashAttribute("successMessage", "Actor updated successfully!");
        return "redirect:/projects/" + projectId + "/actors";
    }

    @GetMapping("/{id}/delete")
    public String deleteActor(@PathVariable Long projectId,
                             @PathVariable Long id,
                             RedirectAttributes redirectAttributes) {
        actorService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Actor deleted successfully!");
        return "redirect:/projects/" + projectId + "/actors";
    }
}
