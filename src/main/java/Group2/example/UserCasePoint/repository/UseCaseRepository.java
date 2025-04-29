package Group2.example.UserCasePoint.repository;

import Group2.example.UserCasePoint.model.UseCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UseCaseRepository extends JpaRepository<UseCase, Long> {
    List<UseCase> findByProjectId(Long projectId);
}
