package Group2.example.UserCasePoint.repository;

import Group2.example.UserCasePoint.model.UcpCalculation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UcpCalculationRepository extends JpaRepository<UcpCalculation, Long> {
    List<UcpCalculation> findByProjectId(Long projectId);
}
