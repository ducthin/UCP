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
public class EnvironmentalFactor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String description;
    private double weight;
    private int score; // 0-5
      @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calculation_id")
    @JsonBackReference(value = "calculation-environmental-factors")
    private UcpCalculation calculation;
    
    public double getWeightedScore() {
        return weight * score;
    }
    
    // Added getValue method to simplify UcpCalculationService
    public int getValue() {
        return score;
    }
    
    // Environmental factors with standard weights as defined in UCP methodology
    public static EnvironmentalFactor[] getDefaultFactors() {
        return new EnvironmentalFactor[] {
            new EnvironmentalFactor(null, "E1", "Familiarity with Project", 1.5, 0, null),
            new EnvironmentalFactor(null, "E2", "Application Experience", 0.5, 0, null),
            new EnvironmentalFactor(null, "E3", "Object-Oriented Experience", 1.0, 0, null),
            new EnvironmentalFactor(null, "E4", "Lead Analyst Capability", 0.5, 0, null),
            new EnvironmentalFactor(null, "E5", "Motivation", 1.0, 0, null),
            new EnvironmentalFactor(null, "E6", "Stable Requirements", 2.0, 0, null),
            new EnvironmentalFactor(null, "E7", "Part-Time Staff", -1.0, 0, null),
            new EnvironmentalFactor(null, "E8", "Difficult Programming Language", -1.0, 0, null)
        };
    }
    
    // Count factors with score < 3 for E1-E6
    // and factors with score > 3 for E7-E8
    public static int countSignificantFactors(EnvironmentalFactor[] factors) {
        int count = 0;
        
        for (int i = 0; i < factors.length; i++) {
            if (i < 6 && factors[i].getScore() < 3) {
                count++;
            } else if (i >= 6 && factors[i].getScore() > 3) {
                count++;
            }
        }
        
        return count;
    }
}
