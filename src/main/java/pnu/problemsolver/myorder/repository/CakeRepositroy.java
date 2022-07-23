package pnu.problemsolver.myorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pnu.problemsolver.myorder.domain.Cake;

import java.util.UUID;

public interface CakeRepositroy extends JpaRepository<Cake, UUID> {

}
