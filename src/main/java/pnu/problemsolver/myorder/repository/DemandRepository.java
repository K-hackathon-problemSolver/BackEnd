package pnu.problemsolver.myorder.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pnu.problemsolver.myorder.domain.Customer;
import pnu.problemsolver.myorder.domain.Demand;
import pnu.problemsolver.myorder.domain.Store;
import pnu.problemsolver.myorder.domain.constant.DemandStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface DemandRepository extends JpaRepository<Demand, UUID> {
	
//	@Query("select * from Demand d where d.customer.uuid = :#{#customer.uuid} and d.status = :#{#status}}")
	List<Demand> findByCustomerAndStatus(Customer customer, DemandStatus status, Pageable pageable);
	List<Demand> findByCustomer(Customer customer, Pageable pageable);
	List<Demand> findByStoreAndStatus(Store store, DemandStatus status, Pageable pageable);
	
	/**
	 * And와 between을 어떻게 사용하는지 봐놓아라
	 * @param a
	 * @param b
	 * @return
	 */
	List<Demand> findByStatusAndCreatedBetween(DemandStatus status, LocalDateTime a, LocalDateTime b);
	
}
