//package pnu.problemsolver.myorder.dto;
//
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import net.bytebuddy.utility.nullability.NeverNull;
//import pnu.problemsolver.myorder.domain.Store;
//
//import java.util.UUID;
//
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class StoreDTOForListPreflight {
//    private UUID uuid;
//    private double latitude;
//    private double longitude;
//
//
//    public static StoreDTOForListPreflight toDTO(Store store) {
//        return StoreDTOForListPreflight.builder()
//                .uuid(store.getUuid())
//                .latitude(store.getLatitude())
//                .longitude(store.getLongitude())
//                .build();
//    }
//}
