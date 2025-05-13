package Group2.example.UserCasePoint.controller;

import Group2.example.UserCasePoint.model.*;
import Group2.example.UserCasePoint.repository.EnvironmentalFactorRepository;
import Group2.example.UserCasePoint.repository.TechnicalFactorRepository;
import Group2.example.UserCasePoint.service.ProjectService;
import Group2.example.UserCasePoint.service.UcpCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/projects/{projectId}/calculations")
public class UcpCalculationController {

    private final UcpCalculationService ucpCalculationService;
    private final ProjectService projectService;
    private final TechnicalFactorRepository technicalFactorRepository;
    private final EnvironmentalFactorRepository environmentalFactorRepository;

    @Autowired
    public UcpCalculationController(
            UcpCalculationService ucpCalculationService,
            ProjectService projectService,
            TechnicalFactorRepository technicalFactorRepository,
            EnvironmentalFactorRepository environmentalFactorRepository) {
        this.ucpCalculationService = ucpCalculationService;
        this.projectService = projectService;
        this.technicalFactorRepository = technicalFactorRepository;
        this.environmentalFactorRepository = environmentalFactorRepository;
    }

    @GetMapping
    public String listCalculations(@PathVariable Long projectId, Model model) {
        Project project = projectService.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid project ID: " + projectId));
        
        List<UcpCalculation> calculations = ucpCalculationService.findByProjectId(projectId);
        
        model.addAttribute("project", project);
        model.addAttribute("calculations", calculations);
        return "calculations/list";
    }

    @GetMapping("/create")
    public String createCalculationForm(@PathVariable Long projectId, Model model) {
        Project project = projectService.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid project ID: " + projectId));
        
        model.addAttribute("project", project);
        model.addAttribute("calculation", new UcpCalculation());
        return "calculations/create-form";
    }

    @PostMapping("/create")
    public String createCalculation(@PathVariable Long projectId,
                                  @RequestParam String calculationName,
                                  RedirectAttributes redirectAttributes) {
        UcpCalculation calculation = ucpCalculationService.initialize(projectId, calculationName);
        redirectAttributes.addFlashAttribute("successMessage", "Calculation initialized successfully!");
        return "redirect:/projects/" + projectId + "/calculations/" + calculation.getId() + "/technical-factors";
    }

    @GetMapping("/{id}")
    public String viewCalculation(@PathVariable Long projectId,
                                @PathVariable Long id,
                                Model model) {
        Project project = projectService.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid project ID: " + projectId));
        
        UcpCalculation calculation = ucpCalculationService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid calculation ID: " + id));
        
        List<TechnicalFactor> technicalFactors = technicalFactorRepository.findByCalculationId(id);
        List<EnvironmentalFactor> environmentalFactors = environmentalFactorRepository.findByCalculationId(id);
        
        model.addAttribute("project", project);
        model.addAttribute("calculation", calculation);
        model.addAttribute("technicalFactors", technicalFactors);
        model.addAttribute("environmentalFactors", environmentalFactors);
        return "calculations/view";
    }

    @GetMapping("/{id}/technical-factors")
    public String editTechnicalFactorsForm(@PathVariable Long projectId,
                                         @PathVariable Long id,
                                         Model model) {
        Project project = projectService.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid project ID: " + projectId));
        
        UcpCalculation calculation = ucpCalculationService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid calculation ID: " + id));
        
        List<TechnicalFactor> technicalFactors = technicalFactorRepository.findByCalculationId(id);
        
        model.addAttribute("project", project);
        model.addAttribute("calculation", calculation);
        model.addAttribute("technicalFactors", technicalFactors);
        return "calculations/technical-factors";
    }    @PostMapping("/{id}/technical-factors")
    public String updateTechnicalFactors(@PathVariable Long projectId,
                                       @PathVariable Long id,
                                       @ModelAttribute TechnicalFactorWrapper wrapper,
                                       RedirectAttributes redirectAttributes) {
        // Ensure the calculation exists before proceeding
        ucpCalculationService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid calculation ID: " + id));
        
        if (wrapper != null && wrapper.getTechnicalFactors() != null) {
            ucpCalculationService.updateTechnicalFactors(id, wrapper.getTechnicalFactors());
            redirectAttributes.addFlashAttribute("successMessage", "Technical factors updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "No technical factors were submitted. Please try again.");
        }
        
        // Chuyển thẳng đến tính toán thay vì qua environmental factors
        return "redirect:/projects/" + projectId + "/calculations/" + id + "/calculate";
    }

    /**
     * @deprecated Environmental factors không còn được sử dụng trong công thức UCP mới 
     */
    @Deprecated
    @GetMapping("/{id}/environmental-factors")
    public String editEnvironmentalFactorsForm(@PathVariable Long projectId,
                                             @PathVariable Long id,
                                             RedirectAttributes redirectAttributes) {
        // Chuyển thẳng đến tính toán
        redirectAttributes.addFlashAttribute("infoMessage", "Environmental factors are not used in the new UCP formula.");
        return "redirect:/projects/" + projectId + "/calculations/" + id + "/calculate";
    }

    /**
     * @deprecated Environmental factors không còn được sử dụng trong công thức UCP mới
     */
    @Deprecated
    @PostMapping("/{id}/environmental-factors")
    public String updateEnvironmentalFactors(@PathVariable Long projectId,
                                           @PathVariable Long id,
                                           @RequestParam(value = "score", required = false) int[] scores,
                                           RedirectAttributes redirectAttributes) {
        // Chuyển thẳng đến tính toán
        redirectAttributes.addFlashAttribute("infoMessage", "Environmental factors are not used in the new UCP formula.");
        return "redirect:/projects/" + projectId + "/calculations/" + id + "/calculate";
    }

    @GetMapping("/{id}/calculate")
    public String calculate(@PathVariable Long projectId,
                          @PathVariable Long id,
                          RedirectAttributes redirectAttributes) {
        ucpCalculationService.calculate(id);
        redirectAttributes.addFlashAttribute("successMessage", "UCP calculation completed successfully!");
        return "redirect:/projects/" + projectId + "/calculations/" + id;
    }

    @GetMapping("/{id}/actual-effort")
    public String editActualEffortForm(@PathVariable Long projectId,
                                     @PathVariable Long id,
                                     Model model) {
        Project project = projectService.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid project ID: " + projectId));
        
        UcpCalculation calculation = ucpCalculationService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid calculation ID: " + id));
        
        model.addAttribute("project", project);
        model.addAttribute("calculation", calculation);
        return "calculations/actual-effort";
    }

    @PostMapping("/{id}/actual-effort")
    public String updateActualEffort(@PathVariable Long projectId,
                                   @PathVariable Long id,
                                   @RequestParam Double actualEffort,
                                   RedirectAttributes redirectAttributes) {
        ucpCalculationService.updateActualEffort(id, actualEffort);
        redirectAttributes.addFlashAttribute("successMessage", "Actual effort updated successfully!");
        return "redirect:/projects/" + projectId + "/calculations/" + id;
    }

    @GetMapping("/{id}/delete")
    public String deleteCalculation(@PathVariable Long projectId,
                                  @PathVariable Long id,
                                  RedirectAttributes redirectAttributes) {
        ucpCalculationService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Calculation deleted successfully!");
        return "redirect:/projects/" + projectId + "/calculations";
    }
}
