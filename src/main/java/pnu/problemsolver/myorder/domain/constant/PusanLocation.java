package pnu.problemsolver.myorder.domain.constant;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PusanLocation {
	
	
	SUYUNG(35.163761, 129.113288), GUMJUNG(35.249845, 129.090564), DONGLAE(35.208588, 129.079995);
	public double latitude;//위도 경도
	
	public double longitude;
}
