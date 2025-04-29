package Group2.example.UserCasePoint.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UcpCalculation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private LocalDateTime calculationDate;
    
    // Unadjusted Actor Weight
    private double uaw;
    
    // Unadjusted Use Case Weight
    private double uucw;
    
    // Unadjusted Use Case Points (UUCW + UAW)
    private double uucp;
    
    // Technical Factor Total
    private double tf;
    
    // Technical Complexity Factor (0.6 + (0.01 × TF))
    private double tcf;
    
    // Environmental Factor Total
    private double ef;
    
    // Environmental Complexity Factor (1.4 + (-0.03 × EF))
    private double ecf;
    
    // Use Case Points (UUCP × TCF × ECF)
    private double ucp;
    
    // Productivity Factor (hours/UCP)
    private double productivityFactor;
    
    // Estimated Effort in hours (UCP * productivityFactor)
    private double estimatedEffort;
    
    // Actual Effort in hours
    private Double actualEffort;
      @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonBackReference(value = "project-calculations")
    private Project project;
    
    @OneToMany(mappedBy = "calculation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "calculation-technical-factors")
    private List<TechnicalFactor> technicalFactors = new ArrayList<>();
    
    @OneToMany(mappedBy = "calculation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "calculation-environmental-factors")
    private List<EnvironmentalFactor> environmentalFactors = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        calculationDate = LocalDateTime.now();
    }
    
    // Calculate productivity factor based on Environmental Significance (ES)
    public int calculateProductivityFactor(int environmentalSignificance) {
        if (environmentalSignificance < 1) {
            return 48;  // hours/UCP
        } else if (environmentalSignificance < 3) {
            return 32;  // hours/UCP
        } else {
            return 20;  // hours/UCP
        }
    }
}
