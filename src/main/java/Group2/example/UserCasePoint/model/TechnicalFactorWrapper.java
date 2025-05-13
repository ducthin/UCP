package Group2.example.UserCasePoint.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TechnicalFactorWrapper {
    private List<TechnicalFactor> technicalFactors;
}
