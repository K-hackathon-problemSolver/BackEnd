package pnu.problemsolver.myorder.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import pnu.problemsolver.myorder.domain.Customer;
import pnu.problemsolver.myorder.domain.Demand;
import pnu.problemsolver.myorder.domain.Store;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Commit
class DemandRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    DemandRepository demandRepository;

    @Test
    public void save생성_수정_시간차이확인() {
        Customer customer = Customer.builder().build();
//        System.out.println("유유아이디"+customer.getUuid());
        customerRepository.save(customer);

        Store store = Store.builder().build();
        storeRepository.save(store);


        Demand demand = Demand.builder()
                .customer(customer)
                .store(store)
                .build();

        demandRepository.save(demand);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        demand.setStatus(1);
        demandRepository.save(demand);
    }


}