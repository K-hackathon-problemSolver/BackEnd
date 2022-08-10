package pnu.problemsolver.myorder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pnu.problemsolver.myorder.domain.Customer;
import pnu.problemsolver.myorder.dto.CustomerDTO;
import pnu.problemsolver.myorder.repository.CustomerRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class CustomerService {
    private final CustomerRepository customerRepository;
    
    /**
     * 매개변수로 DTO보다는 email처럼 핵심만 전달하는 것이 좋은 것 같다.
     * @return 성공하면 CustomerDTO, 실패하면 null
     */
    public <T> T findById(Function<Customer, T> func, UUID id) {
        
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (customerOptional.isPresent()) {
            T resDTO = func.apply(customerOptional.get());
            return resDTO;
        }
        return null;
    }

    /**
     * service는 controller에서 사용되기 때문에 DTO를 입력, 출력 으로 받아야 한다.
     * @param customerDTO
     * @return
     */
    public void save(CustomerDTO customerDTO) {
        Customer customer = Customer.toEntity(customerDTO);
        customerRepository.save(customer);
        customerDTO.setUuid(customer.getUuid());
    }

    /**
     * @param customerDTO
     * @return DB에 sns 고유 ID가 중복되면 예외발생. 없으면 null.
     */
    public CustomerDTO findBySnsTypeAndSnsIdentifyKey(CustomerDTO customerDTO) {
        List<Customer> resList = customerRepository.findBySnsTypeAndSnsIdentifyKey(customerDTO.getSnsType(), customerDTO.getSnsIdentifyKey());
        if (resList.size() == 1) {
            return CustomerDTO.toDTO(resList.get(0));
        } else if (resList.isEmpty()) {
            return null;
        }

        throw new RuntimeException("snsType and snsIdentifyKey 중복!");
    }
    
    public <T> List<T> findAll(Function<Customer, T> func) {
        List<Customer> all = customerRepository.findAll();
        List<T> list = all.stream().map(i -> func.apply(i)).collect(Collectors.toList());
        return list;
    }
    
    public <T> List<T> findByName(Function<Customer, T> func, String name) {
        return customerRepository.findByName(name).stream().map(i -> func.apply(i)).collect(Collectors.toList());
    }
    
    public void setFcmToken(UUID uuid, String token) {
        Optional<Customer> byId = customerRepository.findById(uuid);
        if (!byId.isPresent()) {
            throw new NullPointerException("findById() return null");
        }
        Customer customer = byId.get();
        customer.setFcmToken(token);
        
    }






}
