package DAO;

import Model.ExchangeRate;

import java.sql.*;
import java.util.ArrayList;

public class ExchangeRatesDAO {

    static String jdbcURL2 = "jdbc:sqlite:C:/Users/temas/OneDrive/Desktop/java/CurrencyExchangeV0.1/src/main/webapp/WEB-INF/identifier.sqlite";

    public static ArrayList<ExchangeRate> getAllExchangeRates() throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcURL2);
        Statement statement = connection.createStatement();

        String exchangeRateSelect = "SELECT * FROM ExchangeRates";

        ArrayList<ExchangeRate> exchangeRates = new ArrayList<>();
        ResultSet rs = statement.executeQuery(exchangeRateSelect);

        while (rs.next()) {
            ExchangeRate exchangeRate = new ExchangeRate(
                    rs.getInt("ID"),
                    rs.getInt("BaseCurrencyId"),
                    rs.getInt("TargetCurrencyId"),
                    rs.getFloat("Rate")
            );
            exchangeRates.add(exchangeRate);
        }

        connection.close();
        return exchangeRates;
    }

    public static ExchangeRate getExchangeRateByIds(int baseCurrencyId, int targetCurrencyId) throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcURL2);

        Statement statement = connection.createStatement();
        String exchangeRateSelect = "SELECT * FROM ExchangeRates WHERE BaseCurrencyId = " + baseCurrencyId + " AND TargetCurrencyId = " + targetCurrencyId;

        ResultSet rs = statement.executeQuery(exchangeRateSelect);
        rs.next();
        ExchangeRate exchangeRate = new ExchangeRate(rs.getInt("ID"), rs.getInt("BaseCurrencyId"), rs.getInt("TargetCurrencyId"), rs.getFloat("Rate") );

        connection.close();
        return exchangeRate;
    }

    public static boolean isExchangeRateExists(ExchangeRate exchangeRate) throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcURL2);

        Statement statement = connection.createStatement();
        String rateSelect = "SELECT * FROM ExchangeRates WHERE BaseCurrencyId = '" + exchangeRate.getBaseCurrencieID() + "' AND TargetCurrencyId = " + exchangeRate.getTargetCurrencyID();

        ResultSet rs = statement.executeQuery(rateSelect);

        connection.close();
        return rs.next();
    }

    public static boolean isExchangeRateExistsByCurrencyCodes(String basicCurrencyCode, String targetCurrencyCode) throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcURL2);

        Statement statement = connection.createStatement();
        String rateSelect = "SELECT * FROM ExchangeRates WHERE BaseCurrencyId = " + DAO.CurrenciesDAO.getCurrencyIdByCode(basicCurrencyCode) + " AND TargetCurrencyId = " + DAO.CurrenciesDAO.getCurrencyIdByCode(targetCurrencyCode);

        ResultSet rs = statement.executeQuery(rateSelect);
        connection.close();
        return rs.next();
    }


    public static ExchangeRate postExchangeRate(ExchangeRate exchangeRate) throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcURL2);

        Statement statement = connection.createStatement();
        String currencyInsert = "INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) " +
                                "SELECT '" + exchangeRate.getBaseCurrencieID() + "', '" + exchangeRate.getTargetCurrencyID() + "', '" + exchangeRate.getRate() + "' ";

        statement.executeUpdate(currencyInsert);

        exchangeRate.setExchangeRateId(getExchangeRateId(exchangeRate));

        connection.close();
        return exchangeRate;
    }

    public static int getExchangeRateId(ExchangeRate exchangeRate) throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcURL2);

        Statement statement = connection.createStatement();

        String exchangeRateSelect = "SELECT * FROM ExchangeRates WHERE BaseCurrencyId = '" + exchangeRate.getBaseCurrencieID() + "' AND TargetCurrencyId = " + exchangeRate.getTargetCurrencyID();
        ResultSet rs = statement.executeQuery(exchangeRateSelect);

        rs.next();
        int exchangeRateId = rs.getInt("ID");
        connection.close();
        return exchangeRateId;

    }


    public static void patchExchangeRate(int baseCurrencyId, int targetCurrencyId, float rate) throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcURL2);
        Statement statement = connection.createStatement();

//        String exchangeRatePatch = """
//            UPDATE ExchangeRates er
//            SET er.Rate = + "'" + rate + "'
//            WHERE er.BaseCurrencyCode = ?
//            AND er.TargetCurrencyCode = ?""";

        String exchangeRatePatch = "UPDATE ExchangeRates " +
                "SET Rate = " + rate + " " +
                "WHERE BaseCurrencyId = '" + baseCurrencyId + "' " +
                "AND TargetCurrencyId = '" + targetCurrencyId + "'";

        statement.executeUpdate(exchangeRatePatch);
        connection.close();
    }

    public static float getRate(ExchangeRate exchangeRate) throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcURL2);
        Statement statement = connection.createStatement();

        String rateSellect = "SELECT * FROM ExchangeRates WHERE BaseCurrencyId = '" + exchangeRate.getBaseCurrencieID() + "' AND TargetCurrencyId = " + exchangeRate.getTargetCurrencyID();

        ResultSet rs = statement.executeQuery(rateSellect);
        rs.next();
        float rate = rs.getFloat("Rate");
        connection.close();

        return rate;
    }


}
