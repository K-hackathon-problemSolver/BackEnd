package pnu.problemsolver.myorder.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import pnu.problemsolver.myorder.domain.constant.SNSType;
import pnu.problemsolver.myorder.dto.CustomerDTO;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Commit
class CustomerServiceTest {

    @Autowired
    CustomerService customerService;


    @Test
    void findBySnsTypeAndSnsIdentifyKeTest() {
        CustomerDTO dto = CustomerDTO.builder()
                .snsType(SNSType.NAVER)
                .snsIdentifyKey("1")
                .build();

        customerService.save(dto);
//        customerService.save(dto); 에러 뜨는 것 까지 다 확인함!
        CustomerDTO res = customerService.findBySnsTypeAndSnsIdentifyKey(dto);
        assertEquals(res.getSnsIdentifyKey(), "1");
        assertEquals(res.getSnsType(), SNSType.NAVER);

    }
    @Test
    void findBySnsTypeAndSnsIdentifyKey() {
        CustomerDTO dto = CustomerDTO.builder()
                .snsType(SNSType.NAVER)
                .snsIdentifyKey("1")
                .build();

//        customerService.save(dto);
        CustomerDTO res = customerService.findBySnsTypeAndSnsIdentifyKey(dto);
        System.out.println(res);

//        assertEquals(res.getSnsIdentifyKey(), "1");
//        assertEquals(res.getSnsType(), SNSType.NAVER);

    }
}