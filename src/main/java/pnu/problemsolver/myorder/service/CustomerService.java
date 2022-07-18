package pnu.problemsolver.myorder.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pnu.problemsolver.myorder.domain.Customer;
import pnu.problemsolver.myorder.domain.Store;
import pnu.problemsolver.myorder.dto.CustomerDTO;
import pnu.problemsolver.myorder.dto.StoreDTO;
import pnu.problemsolver.myorder.repository.CustomerRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    /**
     * 매개변수로 DTO보다는 email처럼 핵심만 전달하는 것이 좋은 것 같다.
     * @param customerDTO DTO를 사용하는게 확장, 수정에 유리하다.
     * @return 성공하면 CustomerDTO, 실패하면 null
     */
    public CustomerDTO findById(CustomerDTO customerDTO) {

        Optional<Customer> customerOptional = customerRepository.findById(customerDTO.getUuid());
        if (customerOptional.isPresent()) {
            CustomerDTO resDTO = modelMapper.map(customerOptional, CustomerDTO.class);
            return resDTO;
        }

        return null;
    }

}
