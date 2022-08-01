package pnu.problemsolver.myorder.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import pnu.problemsolver.myorder.domain.Customer;
import pnu.problemsolver.myorder.domain.Demand;
import pnu.problemsolver.myorder.domain.Store;
import pnu.problemsolver.myorder.domain.constant.DemandStatus;

import java.util.List;
import java.util.UUID;

public interface DemandRepository extends JpaRepository<Demand, UUID> {
	List<Demand> findByCustomerAndStatus(Customer customer, DemandStatus status, Pageable pageable);
	
	
	
	List<Demand> findByStoreAndStatus(Store store, DemandStatus status, Pageable pageable);
}
