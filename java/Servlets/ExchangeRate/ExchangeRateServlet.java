package Servlets.ExchangeRate;

import DTO.ModelDTO.ExchangeRateDTO;
import Messages.Message;
import Service.ExchangeRateService;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.stream.Collectors;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();

        String pathInfo = req.getPathInfo();
        Gson gson = new Gson();

        if (pathInfo.length() != 7) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Message message = new Message("Code's length should be 6");
            out.println(gson.toJson(message));
        }

        try {
            ExchangeRateDTO exchangeRateDTO = ExchangeRateService.getExchangeRateByCodes(pathInfo);
            if (exchangeRateDTO == null) {

                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                Message message = new Message("Exchange rate's not found");
                out.println(gson.toJson(message));
            } else {
                String json = gson.toJson(exchangeRateDTO);
                out.println(json);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        String pathInfo = req.getPathInfo();
        Gson gson = new Gson();


        String body = req.getReader().lines().collect(Collectors.joining());
        String rateStr = body.split("rate=")[1].split("&")[0];
        rateStr = URLDecoder.decode(rateStr, StandardCharsets.UTF_8);

        float rate = Float.parseFloat(rateStr);

        try {
            ExchangeRateDTO patchedRate = ExchangeRateService.updateExchangeRate(pathInfo, rate);

            if (patchedRate == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                Message message = new Message("Exchange rate's not found");
                out.println(gson.toJson(message));
            } else out.println(gson.toJson(patchedRate));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

//    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        resp.setCharacterEncoding("UTF-8");
//        resp.setContentType("application/json");
//        PrintWriter out = resp.getWriter();
//        String pathInfo = req.getPathInfo();
//        Gson gson = new Gson();
//
//        try {
//            String rawBody = req.getReader().lines().collect(Collectors.joining());
//            System.out.println("Received raw body: " + rawBody);
//
//            JsonElement jsonElement = gson.fromJson(rawBody, JsonElement.class);
//
//            float rate;
//            if (jsonElement.isJsonObject()) {
//                // Вариант 1: {"rate": 0.83}
//                JsonObject jsonObject = jsonElement.getAsJsonObject();
//                rate = jsonObject.get("rate").getAsFloat();
//            } else if (jsonElement.isJsonPrimitive()) {
//                // Вариант 2: Просто число (0.83)
//                rate = jsonElement.getAsFloat();
//            } else {
//                throw new JsonSyntaxException("Unsupported JSON format");
//            }
//
//            ExchangeRateDTO patchedRate = ExchangeRateService.updateExchangeRate(pathInfo, rate);
//
//            if (patchedRate == null) {
//                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
//                out.println(gson.toJson(new Message("Exchange rate not found")));
//            } else {
//                out.println(gson.toJson(patchedRate));
//            }
//
//        } catch (JsonSyntaxException e) {
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            out.println(gson.toJson(new Message("Invalid data format. Send JSON: {\"rate\": number} or just number")));
//        } catch (NumberFormatException e) {
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            out.println(gson.toJson(new Message("Rate must be a number")));
//        } catch (SQLException e) {
//            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            out.println(gson.toJson(new Message("Database error: " + e.getMessage())));
//        }
//    }

}
