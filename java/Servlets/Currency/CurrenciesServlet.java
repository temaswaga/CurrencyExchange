package Servlets.Currency;

import Messages.Message;
import Model.Currency;
import DTO.ModelDTO.CurrencyDTO;
import Service.CurrencyService;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import static DAO.CurrenciesDAO.getAllCurrencies;
import static Mappers.CurrencyMapper.toDTO;
import static Mappers.CurrencyMapper.toEntity;
//import static Mappers.CurrencyMapper.getAllCurrenciesDTO;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();

        try {
            Gson gson = new Gson();
            String jsonCurrencies = gson.toJson(getAllCurrencies());

            out.println(jsonCurrencies);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();

        String code = req.getParameter("code");
        String name = req.getParameter("fullName");
        String sign = req.getParameter("sign");

        Currency postedCurrency = new Currency(code, name, sign);

        if (code == null || name == null || sign == null || name.isEmpty() || sign.isEmpty() || code.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Message message = new Message("Missing required fields (code, fullName, sign)");
            out.print(gson.toJson(message));
            return;

        } else if (code.length() != 3 ) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Message message = new Message("Code supposed to be a 3-digit");
            out.println(gson.toJson(message));
            return;

        } else if (code.equalsIgnoreCase("GAY") || code.equalsIgnoreCase("SEX")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Message message = new Message("Naaah homie, there is no place for GAYSEX in my app");
            out.println(gson.toJson(message));
            return;
        }

        try {
            if (DAO.CurrenciesDAO.isCurrencyExists(postedCurrency)) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                Message message = new Message("Currency already exists");
                out.println(gson.toJson(message));
                return;
            }

            CurrencyDTO responseCurrencyDTO = CurrencyService.postCurrency(toDTO(postedCurrency));

            out.print(gson.toJson(toEntity(responseCurrencyDTO)));

    } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}