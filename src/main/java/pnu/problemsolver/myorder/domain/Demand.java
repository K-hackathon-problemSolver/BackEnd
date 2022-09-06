package pnu.problemsolver.myorder.domain;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import pnu.problemsolver.myorder.domain.constant.DemandStatus;
import pnu.problemsolver.myorder.dto.DemandDTO;
import pnu.problemsolver.myorder.dto.DemandSaveDTO;

import javax.persistence.*;
import java.nio.file.Path;
import java.util.UUID;

@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Getter
//@Setter
@Entity
public class Demand extends BaseTimeEntitiy {
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID uuid;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@ToString.Exclude
	private Customer customer;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@ToString.Exclude
	private Cake cake;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@ToString.Exclude
	private Store store;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private DemandStatus status;
	
	
	@Column(columnDefinition = "JSON")
	private String option;

//    @Column(nullable = false)
//    private String name; //케이크이름
//
//    private String description; //json에 포함된다.
	
	@Column(nullable = false)
	private int price;
	
	private String filePath;

	
	
	public static Demand toEntity(DemandDTO dto) {
		UUID storeUUID = dto.getStoreUUID();
		UUID customerUUID = dto.getCustomerUUID();
		UUID cakeUUID = dto.getCakeUUID();
		if (storeUUID == null) {
			throw new NullPointerException("store must not be null!");
			
		}
		if (customerUUID == null) {
			throw new NullPointerException("customer must not be null!");
			
		}
		if (cakeUUID == null) {
			throw new NullPointerException("cake must not be null!");
			
		}
		
		Demand demand = Demand.builder()
				.uuid(dto.getUuid())
				.cake(cakeUUID == null ? null : Cake.builder().uuid(cakeUUID).build())
				.customer(customerUUID == null ? null : Customer.builder().uuid(customerUUID).build())
				.store(storeUUID == null ? null : Store.builder().uuid(storeUUID).build())
				.status(dto.getStatus())
				.option(dto.getOption())
				.price(dto.getPrice())
				.filePath(dto.getFilePath())
				.build();
		return demand;
	}

	
	
	//저장까지 여기서 하면 안된다. 너무 많은 기능을 함. 하나의 함수 = 하나의 기능.
	public static Demand toEntity(DemandSaveDTO d, Path filePath, Store s, Customer c, Cake cake) {
		
		Demand demand = Demand.builder()
				.customer(c)
				.cake(cake)
				.store(s)
				.option(d.getOption())
				.price(d.getPrice())
				.status(DemandStatus.WAITING)//처음에는 waiting이다.
				.filePath(filePath == null ? null : filePath.toString())
				.build();
		
		return demand;
		
	}
	
	public static Demand toEntity(DemandSaveDTO d, Path filePath) {
		
		
		UUID customerUUID = d.getCustomerUUID();
		UUID cakeUUID = d.getCakeUUID();
		UUID storeUUID = d.getStoreUUID();
//		List<UUID> li = new ArrayList<>();
//		li.add(customerUUID);
//		li.add(cakeUUID);
//		li.add(storeUUID);
//		for (UUID i : li) {//리스트로 작성하는 것이 확장성에 더 좋다.
//			if (i == null) {
//				throw new NullPointerException("entity must not be null!");//optional=false 이다. 어짜피 자동으로 Exception 발생할 텐데 내가 하는게 맞나 싶기도 하고...
//			}
//		}
		Demand demand = Demand.builder()
				.customer(Customer.builder().uuid(customerUUID).build())
				.cake(Cake.builder().uuid(cakeUUID).build())
				.store(Store.builder().uuid(storeUUID).build())
				.option(d.getOption())
				.price(d.getPrice())
				.status(DemandStatus.WAITING)//처음에는 waiting이다.
				.filePath(filePath == null ? null : filePath.toString())
				.build();
		return demand;
	}
	public void setFilePath(Path path) {
		filePath = path.toString();
	}
	/**
	 * 이렇게 용도에 따라 함수를 다르게 사용하는게 복잡성을 줄일 수 있다.
	 * changeStatus(DemandStatus status) 이런 함수를 만들면 어디서 사용될지 모르고 잘못된 동작 유발 가능
	 *
	 * @return
	 */
	public boolean changeToAccepted() {
		if (status == DemandStatus.WAITING) {
			status = DemandStatus.ACCEPTED;
			return true;
		}
		return false;
	}
	
	public boolean changeToRejected() {
		if (status == DemandStatus.WAITING) {
			status = DemandStatus.REJECTED;
			return true;
		}
		return false;
	}
	
	public boolean changeToCompleted() {
		if (status == DemandStatus.ACCEPTED) {
			status = DemandStatus.COMPLETED;
			return true;
		}
		return false;
	}
	
	
}
