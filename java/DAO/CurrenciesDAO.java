package DAO;

import ConnectionPool.HikariCPConfig;
import Model.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrenciesDAO {

    public static Currency postCurrency(Currency currency) throws SQLException {

        String currencyInsert = "INSERT INTO Currencies (Code, FullName, Sign) " +
                "SELECT '" + currency.getCode() + "', '" + currency.getFullName() + "', '" + currency.getSign() + "'; ";


        try (Connection connection = HikariCPConfig.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(currencyInsert);) {

            statement.executeUpdate();
            currency.setId(getCurrencyId(currency));

            return currency;
        }
    }

    public static ArrayList<Currency> getAllCurrencies() throws SQLException {

        try (Connection connection = HikariCPConfig.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM currencies");
             ResultSet rs = statement.executeQuery()) {

            ArrayList<Currency> currencies = new ArrayList<>();
            while (rs.next()) {
                Currency currency = new Currency(
                        rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("fullName"),
                        rs.getString("sign")
                );
                currencies.add(currency);
            }

            return currencies;
        }
    }

    public static Currency getCurrencyByCode(String code) throws SQLException {

        String currencySelect = "SELECT * FROM currencies WHERE code = '" + code + "'";

        try (Connection connection = HikariCPConfig.getDataSource().getConnection();
        PreparedStatement statement = connection.prepareStatement(currencySelect);
        ResultSet rs = statement.executeQuery()) {

            rs.next();
            Currency currency = new Currency(rs.getInt("ID"), rs.getString("Code"), rs.getString("FullName"), rs.getString("Sign"));

            return currency;
        }
    }

    public static Currency getCurrencyById(int id) throws SQLException {

        String currencySelect = "SELECT * FROM currencies WHERE id = '" + id + "'";

        try (Connection connection = HikariCPConfig.getDataSource().getConnection();
        PreparedStatement statement = connection.prepareStatement(currencySelect);
        ResultSet rs = statement.executeQuery()) {
            rs.next();
            Currency currency = new Currency(rs.getInt("ID"), rs.getString("Code"), rs.getString("FullName"), rs.getString("Sign"));;

            return currency;
        }
    }

    public static int getCurrencyIdByCode(String code) throws SQLException {

        String currencySelect = "SELECT * FROM currencies WHERE code = '" + code + "'";

        try (Connection connection = HikariCPConfig.getDataSource().getConnection();
        PreparedStatement statement = connection.prepareStatement(currencySelect);
            ResultSet rs = statement.executeQuery();) {

            rs.next();
            Currency currency = new Currency(rs.getInt("ID"), rs.getString("Code"), rs.getString("FullName"), rs.getString("Sign"));;
            int currencyId = currency.getId();

            return currencyId;
        }
    }

    public static int getCurrencyId(Currency currency) throws SQLException {

        String currencySelect = "SELECT * FROM currencies WHERE code = '" + currency.getCode() + "'";

        try (Connection connection = HikariCPConfig.getDataSource().getConnection();
        PreparedStatement statement = connection.prepareStatement(currencySelect);
        ResultSet rs = statement.executeQuery()) {

            rs.next();
            int currencyId = rs.getInt("ID");

            return currencyId;
            }
    }

    public static boolean isCurrencyExists(Currency currency) throws SQLException {

        String currencySelect = "SELECT * FROM Currencies WHERE code = '" + currency.getCode() + "' OR fullName = '" + currency.getFullName() + "'";

        try (Connection connection = HikariCPConfig.getDataSource().getConnection();
        PreparedStatement statement = connection.prepareStatement(currencySelect);
        ResultSet rs = statement.executeQuery()) {
            return rs.next();
        }
    }

    public static boolean isCurrencyExistsByCode(String currencysCode) throws SQLException {
        String currencySelect = "SELECT * FROM Currencies WHERE code = '" + currencysCode + "'";

        try (Connection connection = HikariCPConfig.getDataSource().getConnection();
        PreparedStatement statement = connection.prepareStatement(currencySelect);
        ResultSet rs = statement.executeQuery()) {
            return rs.next();
        }
    }
}
