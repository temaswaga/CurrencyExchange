package Servlets.ExchangeRate;

import DTO.ExchangeDTO.ExchangeRequestDTO;
import DTO.ExchangeDTO.ExchangeResponseDTO;
import Messages.Message;
import Service.ExchangeRateService;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Gson gson = new Gson();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        String baseCurrencyCode = req.getParameter("from");
        String targetCurrencyCode = req.getParameter("to");
        float amount = Float.parseFloat(req.getParameter("amount"));

        ExchangeRequestDTO requestDTO = new ExchangeRequestDTO(baseCurrencyCode, targetCurrencyCode, amount);

        try {
            ExchangeResponseDTO exchangeResponseDTO = ExchangeRateService.exchange(requestDTO);
            if (exchangeResponseDTO == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                Message message = new Message("Missing echange rate");
                out.println(gson.toJson(message));
            } else
                out.println(gson.toJson(exchangeResponseDTO));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }
}
