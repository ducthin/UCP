package Group2.example.UserCasePoint.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @Enumerated(EnumType.STRING)
    private ActorType type;
    
    private int weight;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonBackReference(value = "project-actors")
    private Project project;
    
    @PrePersist
    @PreUpdate
    private void calculateWeight() {
        this.weight = type.getWeight();
    }
}
