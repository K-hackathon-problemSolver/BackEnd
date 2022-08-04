package pnu.problemsolver.myorder;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional//테스트별로 독립적이기 위해서는 이게 필수임.
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MyDemandApplicationTests {//여기서 시나리오 테스트 하면 되겠다.

//	@Test
//	public void

}
