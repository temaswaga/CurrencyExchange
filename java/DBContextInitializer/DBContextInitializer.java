package DBContextInitializer;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebListener
public class DBContextInitializer implements ServletContextListener {

        @Override
        public void contextInitialized(ServletContextEvent sce) {

            try {
                Class.forName("org.sqlite.JDBC");

//                String dbPath = sce.getServletContext().getRealPath("/WEB-INF/database.db"); // пока не очень понял
//                String jdbcURL1 = "jdbc:sqlite:" + dbPath;

                String jdbcURL2 = "jdbc:sqlite:C:/Users/temas/OneDrive/Desktop/java/CurrencyExchangeV0.1/src/main/webapp/WEB-INF/identifier.sqlite";

                try (Connection connection = DriverManager.getConnection(jdbcURL2)) {
                    System.out.println("Connected to database");

                    Statement statement = connection.createStatement();

                    String sqlCurrenciesTableCreate = "CREATE TABLE IF NOT EXISTS Currencies (\n" +
                            " ID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                            " Code VARCHAR(3) NOT NULL,\n" +
                            " FullName VARCHAR(50) NOT NULL,\n" +
                            " Sign VARCHAR(10) NOT NULL\n" +
                            ");;";

                    String sqlExchangeRatesTableCreate = "CREATE TABLE IF NOT EXISTS ExchangeRates (\n" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                        "BaseCurrencyId INT NOT NULL,\n" +
                        "TargetCurrencyId INT NOT NULL,\n" +
                        "Rate DECIMAL(18, 6) NOT NULL,\n" +

                        "FOREIGN KEY (BaseCurrencyId) REFERENCES Currencies(ID),\n" +
                        "FOREIGN KEY (TargetCurrencyId) REFERENCES Currencies(ID),\n" +
                        "CONSTRAINT UQ_BaseTarget UNIQUE (BaseCurrencyId, TargetCurrencyId),\n" +
                        "CONSTRAINT CHK_DifferentCurrencies CHECK (BaseCurrencyId <> TargetCurrencyId)\n" +
                        ");;";

                    statement.executeUpdate(sqlCurrenciesTableCreate);
                    statement.executeUpdate(sqlExchangeRatesTableCreate);

                    String[] currencies = {
                            "'USD', 'US Dollar', '$'",
                            "'AMD', 'Armenian dram', '֏'",
                            "'EUR', 'Euro', '€'",
                            "'JPA', 'Japanese Yen', '¥'",
                            "'AUD', 'Australian Dollar', 'A$'",
                            "'RUB', 'Russian Ruble', '₽'"
                    };

                    List<String> inserts = new ArrayList<>();
                    for (String currency : currencies) {
                        String[] parts = currency.split(", ");
                        String code = parts[0].replace("'", "");
                        String insert = "INSERT INTO Currencies (Code, FullName, Sign) " +
                                "SELECT " + currency + " " +
                                "WHERE NOT EXISTS (SELECT 1 FROM Currencies WHERE Code = '" + code + "');";
                        inserts.add(insert);
                    }

                    statement.executeUpdate(inserts.get(0));
                    statement.executeUpdate(inserts.get(1));
                    statement.executeUpdate(inserts.get(2));
                    statement.executeUpdate(inserts.get(3));
                    statement.executeUpdate(inserts.get(4));
                    statement.executeUpdate(inserts.get(5));

                    double[][] exchangeRates = {
                            {3, 1, 0.9},
                            {1, 2, 383.75},
                            {1, 6, 90.50},
                            {1, 4, 150.25},
                    };

                    for (double[] rate : exchangeRates) {
                        String sql = String.format(
                                "INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) " +
                                        "SELECT %f, %f, %.2f " +
                                        "WHERE NOT EXISTS (SELECT 1 FROM ExchangeRates WHERE BaseCurrencyId = %f AND TargetCurrencyId = %f);",
                                rate[0], rate[1], rate[2], rate[0], rate[1]
                        );
                        statement.executeUpdate(sql);
                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }


    }

