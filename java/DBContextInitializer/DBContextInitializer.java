package DBContextInitializer;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import javax.sql.DataSource;
import java.sql.*;

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
                    statement.executeUpdate(sqlCurrenciesTableCreate);

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

                    statement.executeUpdate(sqlExchangeRatesTableCreate);

                    String EuroInsert = "INSERT INTO Currencies (Code, FullName, Sign) " +
                            "SELECT 'EUR', 'Euro', '€' " +
                            "WHERE NOT EXISTS (SELECT 1 FROM Currencies WHERE Code = 'EUR');";

                    String DramInsert = "INSERT INTO Currencies (Code, FullName, Sign) " +
                            "SELECT 'AMD', 'Armenian dram', '֏' " +
                            "WHERE NOT EXISTS (SELECT 1 FROM Currencies WHERE Code = 'AMD');";

                     String USDInsert = "INSERT INTO Currencies (Code, FullName, Sign) " +
                            "SELECT 'USD', 'US dollar', '$' " +
                            "WHERE NOT EXISTS (SELECT 1 FROM Currencies WHERE Code = 'USD');";

                    statement.executeUpdate(EuroInsert);
                    statement.executeUpdate(DramInsert);
                    statement.executeUpdate(USDInsert);

                    String EURUSDExchangeRateInsert = "INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) " +
                            "SELECT '1', '3', '0.9'" +
                            "WHERE NOT EXISTS (SELECT 1 FROM ExchangeRates WHERE BaseCurrencyId = '1' and TargetCurrencyId = '3');";

                    String USDAMDExchangeRateInsert = "INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) " +
                            "SELECT '3', '2', '383.75'" +
                            "WHERE NOT EXISTS (SELECT 1 FROM ExchangeRates WHERE BaseCurrencyId = '3' and TargetCurrencyId = '2');";

                    statement.executeUpdate(EURUSDExchangeRateInsert);
                    statement.executeUpdate(USDAMDExchangeRateInsert);

                    statement.close();

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }



    }

