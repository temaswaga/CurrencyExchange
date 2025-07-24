//package Filters;
//
//import jakarta.servlet.*;
//import jakarta.servlet.annotation.WebFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletRequestWrapper;
//
//import java.io.IOException;
//
//@WebFilter("/*")
//public class PatchToPostFilter implements Filter {
//    @Override
//    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
//            throws IOException, ServletException {
//        HttpServletRequest httpReq = (HttpServletRequest) req;
//        if ("PATCH".equalsIgnoreCase(httpReq.getMethod())) {
//            HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(httpReq) {
//                @Override
//                public String getMethod() {
//                    return "POST"; // Обманываем контейнер, чтобы getParameter() работал
//                }
//            };
//            chain.doFilter(wrapper, resp);
//        } else {
//            chain.doFilter(req, resp);
//        }
//    }
//}