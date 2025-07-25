package Servlets.ExchangeRate;


import DTO.ModelDTO.ExchangeRateDTO;
import Messages.Message;
import Model.ExchangeRate;
import Service.ExchangeRateService;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static Mappers.ExchangeRateMapper.toDTO;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();

        String allExchangeRates = gson.toJson(ExchangeRateService.allExchangeRates);
        out.println(allExchangeRates);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();

        try {
            String baseCurrencyCode = req.getParameter("baseCurrencyCode");
            String targetCurrencyCode = req.getParameter("targetCurrencyCode");
            float rate = Float.parseFloat(req.getParameter("rate"));

            if (baseCurrencyCode.length() != 3 || targetCurrencyCode.length() != 3) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                Message message = new Message("Cmon bro, gimme normal currencies codes");
                out.println(gson.toJson(message));
                return;
            }

            int baseCurrencyId = DAO.CurrenciesDAO.getCurrencyIdByCode(baseCurrencyCode);
            int targetCurrencyId = DAO.CurrenciesDAO.getCurrencyIdByCode(targetCurrencyCode);

            ExchangeRate postedExchangeRate = new ExchangeRate(baseCurrencyId, targetCurrencyId, rate);

//            if (baseCurrency == null || targetCurrency == null || Rate == null) {
//                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                out.println("Missing required fields (code, fullName, sign)");
//                return;
//            }

            if (DAO.ExchangeRatesDAO.isExchangeRateExists(postedExchangeRate)) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("Currency already exists");
                return;
            }

            ExchangeRateDTO responseExchangeRateDTO = ExchangeRateService.postExchangeRate(toDTO(postedExchangeRate));

            out.println(gson.toJson(responseExchangeRateDTO));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
