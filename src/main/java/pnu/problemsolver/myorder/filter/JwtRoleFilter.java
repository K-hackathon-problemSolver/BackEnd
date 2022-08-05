package pnu.problemsolver.myorder.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class JwtRoleFilter extends OncePerRequestFilter {


    public static List<String> loginEssentialURLs = Arrays.asList("");
    public static List<String> loginInEssentialURLs = Arrays.asList("");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //TODO : 로그인이 있어야 접근할 수 있는 URL은 다 걸러내야함.
        filterChain.doFilter(request, response); //다음 필터로 넘겨줘야함.
    }
}
