package Group2.example.UserCasePoint.dto;

import Group2.example.UserCasePoint.model.ActorType;
import lombok.Data;

@Data
public class ActorDTO {
    private Long id;
    private String name;
    private ActorType type;
    private int weight;
    private Long projectId;
}
