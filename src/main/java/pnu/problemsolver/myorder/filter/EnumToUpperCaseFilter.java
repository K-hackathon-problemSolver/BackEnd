//package pnu.problemsolver.myorder.filter;
//
//
//import org.springframework.web.filter.OncePerRequestFilter;
//import pnu.problemsolver.myorder.domain.constant.DemandStatus;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//
//public class EnumToUpperCaseFilter extends OncePerRequestFilter {
//
//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//		String requestURI = request.getRequestURI();
//		if (requestURI.startsWith("/demand/")) {
//			String s = requestURI.substring(8).toUpperCase();
//			if (isStatus(s)) {
//				System.out.println("enum필터에서 forwarding됨!");
//				request.getRequestDispatcher("/demand/" + s).forward(request, response);//소문자를 대문자로 만들어주는 필터
//			}
//
//		}
//		filterChain.doFilter(request, response);
//	}
//
//
//	public boolean isStatus(String status) {
//		if (status.equals(DemandStatus.WAITING.toString())
//				|| status.equals(DemandStatus.ACCEPTED.toString())
//				|| status.equals(DemandStatus.REJECTED.toString())
//				|| status.equals(DemandStatus.COMPLETED.toString())) {
//			return true;
//		}
//		return false;
//	}
//}
