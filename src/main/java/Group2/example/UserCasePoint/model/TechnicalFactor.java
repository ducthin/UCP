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
public class TechnicalFactor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String description;
    private double weight;
    private int score; // 0-5
      @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calculation_id")
    @JsonBackReference(value = "calculation-technical-factors")
    private UcpCalculation calculation;
    
    public double getWeightedScore() {
        return weight * score;
    }
    
    // Added getValue method to simplify UcpCalculationService
    public int getValue() {
        return score;
    }
    
    // Technical factors with standard weights as defined in UCP methodology
    public static TechnicalFactor[] getDefaultFactors() {
        return new TechnicalFactor[] {
            new TechnicalFactor(null, "T1", "Distributed System", 2.0, 0, null),
            new TechnicalFactor(null, "T2", "Response Time/Performance Objectives", 1.0, 0, null),
            new TechnicalFactor(null, "T3", "End-User Efficiency", 1.0, 0, null),
            new TechnicalFactor(null, "T4", "Complex Internal Processing", 1.0, 0, null),
            new TechnicalFactor(null, "T5", "Reusability", 1.0, 0, null),
            new TechnicalFactor(null, "T6", "Easy to Install", 0.5, 0, null),
            new TechnicalFactor(null, "T7", "Easy to Use", 0.5, 0, null),
            new TechnicalFactor(null, "T8", "Portability", 2.0, 0, null),
            new TechnicalFactor(null, "T9", "Easy to Change", 1.0, 0, null),
            new TechnicalFactor(null, "T10", "Concurrency", 1.0, 0, null),
            new TechnicalFactor(null, "T11", "Special Security Features", 1.0, 0, null),
            new TechnicalFactor(null, "T12", "Direct Access for Third Parties", 1.0, 0, null),
            new TechnicalFactor(null, "T13", "Special User Training Facilities", 1.0, 0, null)
        };
    }
}
