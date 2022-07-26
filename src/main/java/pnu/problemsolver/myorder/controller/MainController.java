package pnu.problemsolver.myorder.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pnu.problemsolver.myorder.domain.Cake;
import pnu.problemsolver.myorder.domain.GENDER;
import pnu.problemsolver.myorder.domain.SNSType;
import pnu.problemsolver.myorder.dto.CakeDTO;
import pnu.problemsolver.myorder.dto.CustomerDTO;
import pnu.problemsolver.myorder.dto.StoreDTO;
import pnu.problemsolver.myorder.service.CakeService;
import pnu.problemsolver.myorder.service.CustomerService;
import pnu.problemsolver.myorder.service.DemandService;
import pnu.problemsolver.myorder.service.StoreService;

@Controller
@RestController
@RequiredArgsConstructor
public class MainController {

    private final StoreService storeService;
    private final CakeService cakeService;
    private final CustomerService customerService;

    private final DemandService demandService;


    @GetMapping("/")
    public void setting() {
        //TODO : 더미데이터 넣기!

        insertStoreNCake();
        insertCustomer();
//        insertDemand();
    }

    private void insertDemand() {

    }

    private void insertCustomer() {
        for (int i = 0; i < 3; ++i) {
            CustomerDTO cus = CustomerDTO.builder()
                    .email("email"+i)
                    .name("custmer"+i)
                    .phone_num("010-1234-7890")
                    .birthYear(1999+i)
                    .snsIdentifyKey("snsid~")
                    .snsType(SNSType.NAVER)
                    .gender(GENDER.MAN)
                    .build();

            CustomerDTO saved = customerService.save(cus);
        }
        for (int i = 0; i < 3; ++i) {
            CustomerDTO cus = CustomerDTO.builder()
                    .email("email"+i)
                    .name("custmer"+i)
                    .phone_num("010-1234-7890")
                    .birthYear(1999+i)
                    .snsIdentifyKey("snsid~")
                    .snsType(SNSType.KAKAO)
                    .gender(GENDER.MAN)
                    .build();

            CustomerDTO saved = customerService.save(cus);
        }



    }

    public void insertStoreNCake() {
        for (int i = 1; i <= 11; ++i) {
            StoreDTO st = StoreDTO.builder()
                    .snsIdentifyKey("snskey")
                    .birthYear(1999)
                    .description("맛있는 가게!")
                    .location("부산시 금정구 부산대학로~")
                    .email("zhdhfhd33@zsdf")
                    .snsType(SNSType.KAKAO)
                    .owner_phone_num("010-3391-6486")
                    .filePath("src/main/resources/static/" + i + ".jpg")
                    .build();
            StoreDTO savedStore = storeService.save(st);

            CakeDTO cakeDTO = CakeDTO.builder()
                    .min_price(3000 * i)
                    .name("케이크" + i)
                    .option("{\"color\" : [\"red\", \"blue\", \"green\"], \"size\" : [\"1\", \"2\", \"3\"]}")
                    .storeUUID(savedStore.getUuid()) //연관관계
                    .description("생일 선물로 딱맞음!!")
                    .filePath("src/main/resources/static/" + i +".jpg")
                    .build();
            cakeService.save(cakeDTO);

            //

        }
    }
}
