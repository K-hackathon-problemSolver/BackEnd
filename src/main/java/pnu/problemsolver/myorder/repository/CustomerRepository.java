package pnu.problemsolver.myorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pnu.problemsolver.myorder.domain.Customer;
import pnu.problemsolver.myorder.domain.constant.SNSType;

import java.util.List;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    public List<Customer> findBySnsTypeAndSnsIdentifyKey(SNSType type, String key);
    
    public List<Customer> findByName(String name);
    

}
