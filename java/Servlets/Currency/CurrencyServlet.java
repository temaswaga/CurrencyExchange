package Servlets.Currency;

import DAO.CurrenciesDAO;
import DTO.ModelDTO.CurrencyDTO;
import Mappers.CurrencyMapper;
import Messages.Message;
import Model.Currency;
import Service.CurrencyService;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        String pathInfo = req.getPathInfo();
        Gson gson = new Gson();

        PrintWriter out = resp.getWriter();

        try {
            CurrencyDTO currencyDTO = CurrencyService.getCurrencyByCode(pathInfo);

            out.println(gson.toJson(currencyDTO));

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            Message message = new Message("Missing currency");
            out.println(gson.toJson(message));
        }

    }
}
