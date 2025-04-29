package Group2.example.UserCasePoint.repository;

import Group2.example.UserCasePoint.model.TechnicalFactor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TechnicalFactorRepository extends JpaRepository<TechnicalFactor, Long> {
    List<TechnicalFactor> findByCalculationId(Long calculationId);
}
