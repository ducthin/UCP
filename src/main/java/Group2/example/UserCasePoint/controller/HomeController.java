package Group2.example.UserCasePoint.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import Group2.example.UserCasePoint.service.ProjectService;
import Group2.example.UserCasePoint.service.ActorService;
import Group2.example.UserCasePoint.service.UseCaseService;
import Group2.example.UserCasePoint.service.UcpCalculationService;

@Controller
@RequestMapping("/")
public class HomeController {
    
    private final ProjectService projectService;
    private final ActorService actorService;
    private final UseCaseService useCaseService;
    private final UcpCalculationService ucpCalculationService;
    
    public HomeController(
        ProjectService projectService, 
        ActorService actorService, 
        UseCaseService useCaseService,
        UcpCalculationService ucpCalculationService) {
        this.projectService = projectService;
        this.actorService = actorService;
        this.useCaseService = useCaseService;
        this.ucpCalculationService = ucpCalculationService;
    }
    
    @GetMapping
    public String home(Model model) {
        model.addAttribute("projectCount", projectService.getAllProjects().size());
        model.addAttribute("actorCount", actorService.getAllActors().size());
        model.addAttribute("useCaseCount", useCaseService.getAllUseCases().size());
        model.addAttribute("calculationCount", ucpCalculationService.getAllCalculations().size());
        model.addAttribute("recentProjects", projectService.getRecentProjects(5));
        model.addAttribute("recentCalculations", ucpCalculationService.getRecentCalculations(5));
        
        return "index";
    }
}
