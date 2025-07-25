package Filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class JsonContentTypeFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        httpResponse.setContentType("application/json");
        httpResponse.setCharacterEncoding("UTF-8");

        try {
            chain.doFilter(request, response); // Пробрасываем запрос дальше
        } catch (Exception e) {
            // Обрабатываем исключение и возвращаем JSON-ошибку
            httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            httpResponse.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }

    }

}
