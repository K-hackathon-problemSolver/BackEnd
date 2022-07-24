package pnu.problemsolver.myorder.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import pnu.problemsolver.myorder.domain.Customer;
import pnu.problemsolver.myorder.domain.SNSType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Commit
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    /**
     * 없을 때 null을 반환하지는 않는다. 빈 리스트를 반환함.
     */
    @Test
    public void findBySnsTypeAndSnsIdentifyKeyTest() {
        customerRepository.save(Customer.builder().build());
        List<Customer> li = customerRepository.findBySnsTypeAndSnsIdentifyKey(SNSType.NAVER, "1");
        System.out.println(li);
        if (li == null) {
            System.out.println("null");

        } else {
            System.out.println("not null");
        }
    }
}