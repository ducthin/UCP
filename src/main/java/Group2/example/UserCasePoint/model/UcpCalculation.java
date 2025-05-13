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
    private double uucw;    // Unadjusted Use Case Points (UUCW + UAW)
    private double uucp;
    
    // Total Degree of Influence (TDI) - sum of 14 technical factors
    private double tdi;
    
    // Value Adjustment Factor (VAF = 0.65 + (0.01 × TDI))
    private double vaf;
    
    // Use Case Points (UCP = (UUCW + UAW) × VAF)
    private double ucp;
    
    // For backward compatibility
    @Deprecated
    private double tf;
    
    @Deprecated
    private double tcf;
    
    @Deprecated
    private double ef;
    
    @Deprecated
    private double ecf;
    
    @Deprecated
    private double productivityFactor;
    
    // Estimated Effort in hours (can be calculated separately if needed)
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
    
    // Getter and setter for TDI
    public double getTdi() {
        return tdi;
    }
    
    public void setTdi(double tdi) {
        this.tdi = tdi;
    }
    
    // Getter and setter for VAF
    public double getVaf() {
        return vaf;
    }
    
    public void setVaf(double vaf) {
        this.vaf = vaf;
    }
    
    // Legacy getters and setters for backward compatibility
    @Deprecated
    public void setTf(double tf) {
        this.tf = tf;
    }
    
    @Deprecated
    public double getTf() {
        return tf;
    }
    
    @Deprecated
    public void setTcf(double tcf) {
        this.tcf = tcf;
    }
    
    @Deprecated
    public double getTcf() {
        return tcf;
    }
    
    @Deprecated
    public void setEf(double ef) {
        this.ef = ef;
    }
    
    @Deprecated
    public double getEf() {
        return ef;
    }
    
    @Deprecated
    public void setEcf(double ecf) {
        this.ecf = ecf;
    }
    
    @Deprecated
    public double getEcf() {
        return ecf;
    }
    
    @Deprecated
    public void setProductivityFactor(double productivityFactor) {
        this.productivityFactor = productivityFactor;
    }
    
    @Deprecated
    public double getProductivityFactor() {
        return productivityFactor;
    }
}
