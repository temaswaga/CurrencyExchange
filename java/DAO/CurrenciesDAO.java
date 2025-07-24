package DAO;

import Model.Currency;

import java.sql.*;
import java.util.ArrayList;

public class CurrenciesDAO {

    static String jdbcURL2 = "jdbc:sqlite:C:/Users/temas/OneDrive/Desktop/java/CurrencyExchangeV0.1/src/main/webapp/WEB-INF/identifier.sqlite";


    public static Currency postCurrency(Currency currency) throws SQLException {

        Connection connection = DriverManager.getConnection(jdbcURL2);
        Statement statement = connection.createStatement();
        String currencyInsert = "INSERT INTO Currencies (Code, FullName, Sign) " +
                "SELECT '" + currency.getCode() + "', '" + currency.getFullName() + "', '" + currency.getSign() + "' ";

        statement.executeUpdate(currencyInsert);
        currency.setId(getCurrencyId(currency));

        connection.close();
        return currency;
    }

    public static ArrayList<Currency> getAllCurrencies() throws SQLException {
            Connection connection = DriverManager.getConnection(jdbcURL2);
            Statement statement = connection.createStatement();

            String currencySelect = "SELECT * FROM currencies";

            ArrayList<Currency> currencies = new ArrayList<>();
            ResultSet rs = statement.executeQuery(currencySelect);

            while (rs.next()) {
                Currency currency = new Currency(
                        rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("fullName"),
                        rs.getString("sign")
                );
                currencies.add(currency);
            }
            connection.close();
            return currencies;
    }

    public static Currency getCurrencyByCode(String code) throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcURL2);
        Statement statement = connection.createStatement();

        String currencySelect = "SELECT * FROM currencies WHERE code = '" + code + "'";
        ResultSet rs = statement.executeQuery(currencySelect);

        rs.next();
        Currency currency = new Currency(rs.getInt("ID"), rs.getString("Code"), rs.getString("FullName"), rs.getString("Sign"));;
        connection.close();
        return currency;
    }

    public static Currency getCurrencyById(int id) throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcURL2);
        Statement statement = connection.createStatement();
        String currencySelect = "SELECT * FROM currencies WHERE id = '" + id + "'";
        ResultSet rs = statement.executeQuery(currencySelect);
        rs.next();
        Currency currency = new Currency(rs.getInt("ID"), rs.getString("Code"), rs.getString("FullName"), rs.getString("Sign"));;
        connection.close();
        return currency;
    }

    public static int getCurrencyIdByCode(String code) throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcURL2);
        Statement statement = connection.createStatement();

        String currencySelect = "SELECT * FROM currencies WHERE code = '" + code + "'";
        ResultSet rs = statement.executeQuery(currencySelect);

        rs.next();
        Currency currency = new Currency(rs.getInt("ID"), rs.getString("Code"), rs.getString("FullName"), rs.getString("Sign"));;
        int currencyId = currency.getId();

        connection.close();
        return currencyId;
    }

    public static int getCurrencyId(Currency currency) throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcURL2);
        Statement statement = connection.createStatement();

        String currencySelect = "SELECT * FROM currencies WHERE code = '" + currency.getCode() + "'";
        ResultSet rs = statement.executeQuery(currencySelect);

        rs.next();
        int currencyId = rs.getInt("ID");
        connection.close();
        return currencyId;
    }

    public static boolean isCurrencyExists(Currency currency) throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcURL2);
        Statement statement = connection.createStatement();
        String currencySelect = "SELECT * FROM Currencies WHERE code = '" + currency.getCode() + "' OR fullName = '" + currency.getFullName() + "' OR sign = '" + currency.getSign() + "'";

        ResultSet rs = statement.executeQuery(currencySelect);

        connection.close();
        return rs.next();
    }

    public static boolean isCurrencyExistsByCode(String currencysCode) throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcURL2);
        Statement statement = connection.createStatement();
        String currencySelect = "SELECT * FROM Currencies WHERE code = '" + currencysCode + "'";

        ResultSet rs = statement.executeQuery(currencySelect);


        connection.close();
        return rs.next();
    }




}
