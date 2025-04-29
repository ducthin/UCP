package Group2.example.UserCasePoint.dto;

import Group2.example.UserCasePoint.model.EnvironmentalFactor;
import Group2.example.UserCasePoint.model.TechnicalFactor;
import lombok.Data;

import java.util.List;

@Data
public class UcpCalculationDTO {
    private Long id;
    private String name;
    private Long projectId;
    private Double uaw;
    private Double uucw;
    private Double uucp;
    private Double tcf;
    private Double ecf;
    private Double ucp;
    private Double productivityFactor;
    private Double estimatedEffort;
    private Double actualEffort;
    private List<TechnicalFactor> technicalFactors;
    private List<EnvironmentalFactor> environmentalFactors;
}
