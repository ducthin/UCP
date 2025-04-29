package Group2.example.UserCasePoint.repository;

import Group2.example.UserCasePoint.model.EnvironmentalFactor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnvironmentalFactorRepository extends JpaRepository<EnvironmentalFactor, Long> {
    List<EnvironmentalFactor> findByCalculationId(Long calculationId);
}
