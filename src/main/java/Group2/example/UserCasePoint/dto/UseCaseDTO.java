package Group2.example.UserCasePoint.dto;

import Group2.example.UserCasePoint.model.UseCaseType;
import lombok.Data;

@Data
public class UseCaseDTO {
    private Long id;
    private String name;
    private UseCaseType type;
    private int transactionCount;
    private int weight;
    private Long projectId;
}
