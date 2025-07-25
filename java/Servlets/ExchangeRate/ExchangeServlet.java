package Servlets.ExchangeRate;

import DTO.ExchangeDTO.ExchangeRequestDTO;
import DTO.ExchangeDTO.ExchangeResponseDTO;
import Messages.Message;
import Service.ExchangeRateService;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
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
        PrintWriter out = resp.getWriter();

        String baseCurrencyCode = req.getParameter("from");
        String targetCurrencyCode = req.getParameter("to");
        float amount = Float.parseFloat(req.getParameter("amount"));

        if (baseCurrencyCode.length() != 3 || targetCurrencyCode.length() != 3) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Message message = new Message("Cmon bro, gimme normal currencies codes");
            out.println(gson.toJson(message));
            return;
        }

        ExchangeRequestDTO requestDTO = new ExchangeRequestDTO(baseCurrencyCode, targetCurrencyCode, amount);

        try {
            ExchangeResponseDTO exchangeResponseDTO = ExchangeRateService.exchange(requestDTO);
            if (exchangeResponseDTO == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                Message message = new Message("Missing exchange rate");
                out.println(gson.toJson(message));
            } else
                out.println(gson.toJson(exchangeResponseDTO));

        } catch (SQLException e) {
        resp.setStatus(500);
        out.println(gson.toJson(new Message("Database error: " + e.getMessage())));

        } catch (JsonIOException | JsonSyntaxException e) {
            resp.setStatus(500);
            out.println(gson.toJson(new Message("JSON serialization error")));
        }
    }

}
