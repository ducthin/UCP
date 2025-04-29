package Group2.example.UserCasePoint.repository;

import Group2.example.UserCasePoint.model.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {
    List<Actor> findByProjectId(Long projectId);
}
