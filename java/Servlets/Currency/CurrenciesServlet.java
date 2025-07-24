package Servlets.Currency;

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

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

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
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");
        Gson gson = new Gson();

        try {
//            String code = req.getParameter("code");
//            String name = req.getParameter("fullName");
//            String sign = req.getParameter("sign");

            String code = req.getParameter("code");
            String name = req.getParameter("fullName");
            String sign = req.getParameter("sign");

            Currency postedCurrency = new Currency(code, name, sign);

            if (code == null || name == null || sign == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("Missing required fields (code, fullName, sign)");
                return;
            }

            if (DAO.CurrenciesDAO.isCurrencyExists(postedCurrency)) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("Currency already exists");
                return;
            }

            CurrencyDTO responseCurrencyDTO = CurrencyService.postCurrency(toDTO(postedCurrency));

            out.println(gson.toJson(toEntity(responseCurrencyDTO)));

    } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}