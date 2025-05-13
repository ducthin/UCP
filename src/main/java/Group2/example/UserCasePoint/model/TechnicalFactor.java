package Group2.example.UserCasePoint.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Data
@NoArgsConstructor
public class TechnicalFactor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String description;
    private int score; // 0-5
    
    // Keeping this for database compatibility but not using it in new calculations
    private double weight = 0.0;  // Default value to fix database schema
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calculation_id")
    @JsonBackReference(value = "calculation-technical-factors")
    private UcpCalculation calculation;
    
    // Custom constructor
    public TechnicalFactor(Long id, String name, String description, int score, UcpCalculation calculation) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.score = score;
        this.weight = 0.0;  // Set default value
        this.calculation = calculation;
    }
    
    // Technical factors list - weight is no longer needed according to the new formula
    public static TechnicalFactor[] getDefaultFactors() {
        return new TechnicalFactor[] {
            new TechnicalFactor(null, "T1", "Distributed System", 0, null),
            new TechnicalFactor(null, "T2", "Response Time/Performance Objectives", 0, null),
            new TechnicalFactor(null, "T3", "End-User Efficiency", 0, null),
            new TechnicalFactor(null, "T4", "Complex Internal Processing", 0, null),
            new TechnicalFactor(null, "T5", "Reusability", 0, null),
            new TechnicalFactor(null, "T6", "Easy to Install", 0, null),
            new TechnicalFactor(null, "T7", "Easy to Use", 0, null),
            new TechnicalFactor(null, "T8", "Portability", 0, null),
            new TechnicalFactor(null, "T9", "Easy to Change", 0, null),
            new TechnicalFactor(null, "T10", "Concurrency", 0, null),
            new TechnicalFactor(null, "T11", "Special Security Features", 0, null),
            new TechnicalFactor(null, "T12", "Direct Access for Third Parties", 0, null),
            new TechnicalFactor(null, "T13", "Special User Training Facilities", 0, null),
            new TechnicalFactor(null, "T14", "Facilitate Change", 0, null)
        };
    }
}
