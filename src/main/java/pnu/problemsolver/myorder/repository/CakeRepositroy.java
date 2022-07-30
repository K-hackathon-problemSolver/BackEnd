package pnu.problemsolver.myorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pnu.problemsolver.myorder.domain.Cake;
import pnu.problemsolver.myorder.domain.Store;

import java.util.List;
import java.util.UUID;

public interface CakeRepositroy extends JpaRepository<Cake, UUID> {
	
	List<Cake> findByStore(Store store);
}
