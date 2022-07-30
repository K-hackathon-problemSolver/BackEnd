package pnu.problemsolver.myorder.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import pnu.problemsolver.myorder.domain.*;
import pnu.problemsolver.myorder.domain.constant.DemandStatus;

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

    @Autowired
    CakeRepositroy cakeRepositroy;

    @Test
    public void save생성_수정_시간차이확인() {
        Customer customer = Customer.builder().build();
//        System.out.println("유유아이디"+customer.getUuid());
        customerRepository.save(customer);
        System.out.println(customer);

        Cake cake = Cake.builder()
                .name("cakeName")
                .minPrice(10000)
                .description("설명")
                .build();
        cakeRepositroy.save(cake);
        cake.setMinPrice(2);

        Cake newCake = Cake.builder()
                .uuid(cake.getUuid())
                .minPrice(3)
                .name("새로만듦")
                .build();

        cakeRepositroy.save(newCake);

        System.out.println(cake);


        Demand demand = Demand.builder()
                .customer(customer)
                .cake(cake)
                .status(DemandStatus.WAITING)
                .build();

        demandRepository.save(demand);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        demand.acceptDemand();//주문수락
        demandRepository.save(demand);
    }




}