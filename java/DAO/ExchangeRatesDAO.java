package DAO;

import ConnectionPool.HikariCPConfig;
import Model.ExchangeRate;

import java.sql.*;
import java.util.ArrayList;

public class ExchangeRatesDAO {

    static String jdbcURL2 = "jdbc:sqlite:C:/Users/temas/OneDrive/Desktop/java/CurrencyExchangeV0.1/src/main/webapp/WEB-INF/identifier.sqlite";

    public static ArrayList<ExchangeRate> getAllExchangeRates() throws SQLException {
        try (Connection connection = HikariCPConfig.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM ExchangeRates");
             ResultSet rs = statement.executeQuery()) {

            ArrayList<ExchangeRate> exchangeRates = new ArrayList<>();

            while (rs.next()) {
                ExchangeRate exchangeRate = new ExchangeRate(
                        rs.getInt("ID"),
                        rs.getInt("BaseCurrencyId"),
                        rs.getInt("TargetCurrencyId"),
                        rs.getFloat("Rate")
                );
                exchangeRates.add(exchangeRate);
            }

            return exchangeRates;
        }
    }

    public static ExchangeRate getExchangeRateByIds(int baseCurrencyId, int targetCurrencyId) throws SQLException {

        String exchangeRateSelect = "SELECT * FROM ExchangeRates WHERE BaseCurrencyId = " + baseCurrencyId + " AND TargetCurrencyId = " + targetCurrencyId;

        try (Connection connection = HikariCPConfig.getDataSource().getConnection();
        PreparedStatement statement = connection.prepareStatement(exchangeRateSelect);
        ResultSet rs = statement.executeQuery()) {

            rs.next();
            ExchangeRate exchangeRate = new ExchangeRate(rs.getInt("ID"), rs.getInt("BaseCurrencyId"), rs.getInt("TargetCurrencyId"), rs.getFloat("Rate"));

            return exchangeRate;
        }
    }

    public static boolean isExchangeRateExists(ExchangeRate exchangeRate) throws SQLException {

        String rateSelect = "SELECT * FROM ExchangeRates WHERE BaseCurrencyId = '" + exchangeRate.getBaseCurrencieID() + "' AND TargetCurrencyId = " + exchangeRate.getTargetCurrencyID();

        try (Connection connection = HikariCPConfig.getDataSource().getConnection();
        PreparedStatement statement = connection.prepareStatement(rateSelect);
        ResultSet rs = statement.executeQuery()) {
            return rs.next();
        }
    }

    public static boolean isExchangeRateExistsByCurrencyCodes(String basicCurrencyCode, String targetCurrencyCode) throws SQLException {

        String rateSelect = "SELECT * FROM ExchangeRates WHERE BaseCurrencyId = " + DAO.CurrenciesDAO.getCurrencyIdByCode(basicCurrencyCode) + " AND TargetCurrencyId = " + DAO.CurrenciesDAO.getCurrencyIdByCode(targetCurrencyCode);

        try (Connection connection = HikariCPConfig.getDataSource().getConnection();
        PreparedStatement statement = connection.prepareStatement(rateSelect);
        ResultSet rs = statement.executeQuery()) {
            return rs.next();
        }
    }


    public static ExchangeRate postExchangeRate(ExchangeRate exchangeRate) throws SQLException {

        String currencyInsert = "INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) " +
                "SELECT '" + exchangeRate.getBaseCurrencieID() + "', '" + exchangeRate.getTargetCurrencyID() + "', '" + exchangeRate.getRate() + "' ";

        try (Connection connection = HikariCPConfig.getDataSource().getConnection();
        PreparedStatement statement = connection.prepareStatement(currencyInsert);) {

            statement.executeUpdate();
            exchangeRate.setExchangeRateId(getExchangeRateId(exchangeRate));
            return exchangeRate;
        }
    }

    public static int getExchangeRateId(ExchangeRate exchangeRate) throws SQLException {

        String exchangeRateSelect = "SELECT * FROM ExchangeRates WHERE BaseCurrencyId = '" + exchangeRate.getBaseCurrencieID() + "' AND TargetCurrencyId = " + exchangeRate.getTargetCurrencyID();

       try (Connection connection = HikariCPConfig.getDataSource().getConnection();
       PreparedStatement statement = connection.prepareStatement(exchangeRateSelect);
       ResultSet rs = statement.executeQuery()) {

           rs.next();
           return rs.getInt("ID");
       }
    }

    public static void patchExchangeRate(int baseCurrencyId, int targetCurrencyId, float rate) throws SQLException {

        String exchangeRatePatch = "UPDATE ExchangeRates " +
                "SET Rate = " + rate + " " +
                "WHERE BaseCurrencyId = '" + baseCurrencyId + "' " +
                "AND TargetCurrencyId = '" + targetCurrencyId + "'";

        try (Connection connection = HikariCPConfig.getDataSource().getConnection();
        PreparedStatement statement = connection.prepareStatement(exchangeRatePatch);) {

            statement.executeUpdate();

        }

//        String exchangeRatePatch = """
//            UPDATE ExchangeRates er
//            SET er.Rate = + "'" + rate + "'
//            WHERE er.BaseCurrencyCode = ?
//            AND er.TargetCurrencyCode = ?""";

    }

    public static float getRate(ExchangeRate exchangeRate) throws SQLException {

        String rateSellect = "SELECT * FROM ExchangeRates WHERE BaseCurrencyId = '" + exchangeRate.getBaseCurrencieID() + "' AND TargetCurrencyId = " + exchangeRate.getTargetCurrencyID();

        try (Connection connection = HikariCPConfig.getDataSource().getConnection();
        PreparedStatement statement = connection.prepareStatement(rateSellect);
        ResultSet rs = statement.executeQuery()) {

            rs.next();
            return rs.getFloat("Rate");
        }
    }

}
