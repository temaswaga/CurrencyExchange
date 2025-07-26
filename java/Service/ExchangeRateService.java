package Service;

import DAO.CurrenciesDAO;
import DAO.ExchangeRatesDAO;
import DTO.ExchangeDTO.ExchangeRequestDTO;
import DTO.ExchangeDTO.ExchangeResponseDTO;
import Model.Currency;
import Model.ExchangeRate;
import DTO.ModelDTO.ExchangeRateDTO;
import Mappers.ExchangeRateMapper;

import java.sql.SQLException;
import java.util.ArrayList;

import static DAO.CurrenciesDAO.getCurrencyById;
import static DAO.ExchangeRatesDAO.*;
import static Mappers.ExchangeRateMapper.*;

public class ExchangeRateService {

    public static final int ID_OF_USD = 1;

    public static ArrayList<ExchangeRateDTO> allExchangeRates;

    static {
        try {
            allExchangeRates = ExchangeRateMapper.getAllExchangeRatesDTOs(getAllExchangeRates());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static String[] connectedCurrencyCodesSplitter(String connectedCodes) {
        String upperCaseConnectedCurrenciesCode = connectedCodes.substring(1).toUpperCase();

        String baseCurrencyCode = upperCaseConnectedCurrenciesCode.substring(0, 3);
        String targetCurrencyCode = upperCaseConnectedCurrenciesCode.substring(3);
        return new String[]{baseCurrencyCode, targetCurrencyCode};
    }


    public static ExchangeRateDTO getExchangeRateByCodes(String connectedCodes) throws SQLException {
        String[] codes = connectedCurrencyCodesSplitter(connectedCodes);

        int baseCurrencyId = DAO.CurrenciesDAO.getCurrencyIdByCode(codes[0]);
        int targetCurrencyId = DAO.CurrenciesDAO.getCurrencyIdByCode(codes[1]);

        if(!DAO.ExchangeRatesDAO.isExchangeRateExistsByCurrencyCodes(codes[0], codes[1])) {
            return null;
        }

        ExchangeRate exchangeRate = DAO.ExchangeRatesDAO.getExchangeRateByIds(baseCurrencyId, targetCurrencyId);

        return toDTO(exchangeRate);
    }


    public static ExchangeRateDTO postExchangeRate (ExchangeRateDTO exchangeRateDTO) throws SQLException {
        ExchangeRate postedExchangeRate = toEntity(exchangeRateDTO);

        return ExchangeRateMapper.toDTO(ExchangeRatesDAO.postExchangeRate(postedExchangeRate));
    }


    public static ExchangeRateDTO updateExchangeRate(String exchangeRateCurrenciesConnectedCodes, float rate) throws SQLException {
        String[] codes = connectedCurrencyCodesSplitter(exchangeRateCurrenciesConnectedCodes);

        int baseCurrencyId = CurrenciesDAO.getCurrencyIdByCode(codes[0]);
        int targetCurrencyId = CurrenciesDAO.getCurrencyIdByCode(codes[1]);

        if (!DAO.ExchangeRatesDAO.isExchangeRateExistsByCurrencyCodes(codes[0], codes[1])) {
            return null;
        } else {

            DAO.ExchangeRatesDAO.patchExchangeRate(baseCurrencyId, targetCurrencyId, rate);

            ExchangeRate patchedExchangeRate =  DAO.ExchangeRatesDAO.getExchangeRateByIds(baseCurrencyId, targetCurrencyId);

            return toDTO(patchedExchangeRate);
        }

    }


    public static ExchangeResponseDTO exchange(ExchangeRequestDTO exchangeRequestDTO) throws SQLException {
        int basicCurrencyId = DAO.CurrenciesDAO.getCurrencyIdByCode(exchangeRequestDTO.baseCurrencyCode());
        int targetCurrencyId = DAO.CurrenciesDAO.getCurrencyIdByCode(exchangeRequestDTO.targetCurrencyCode());


        ExchangeRate exchangeRate_AB = new ExchangeRate(basicCurrencyId, targetCurrencyId);
        ExchangeRate exchangeRate_BA = new ExchangeRate(targetCurrencyId, basicCurrencyId);
        ExchangeRate exchangeRate_USDA = new ExchangeRate(ID_OF_USD, basicCurrencyId);
        ExchangeRate exchangeRate_USDB = new ExchangeRate(ID_OF_USD, targetCurrencyId);

        if (isExchangeRateExists(exchangeRate_AB)) {
            Currency baseCurrency = getCurrencyById(basicCurrencyId);
            Currency targetCurrency = getCurrencyById(targetCurrencyId);
            float rate = getRate(exchangeRate_AB);
            float amount = exchangeRequestDTO.amount();
            float convertedAmount = amount * rate;

            String aproximatedAmount = String.format("%.2f", convertedAmount);

            return new ExchangeResponseDTO(baseCurrency, targetCurrency, rate, amount, aproximatedAmount);

        } else if (isExchangeRateExists(exchangeRate_BA)) {
            Currency baseCurrency = getCurrencyById(basicCurrencyId);
            Currency targetCurrency = getCurrencyById(targetCurrencyId);
            float rate = getRate(exchangeRate_BA);
            float amount = exchangeRequestDTO.amount();
            float convertedAmount = amount / rate;

            String aproximatedAmount = String.format("%.2f", convertedAmount);

            return new ExchangeResponseDTO(baseCurrency, targetCurrency, rate, amount, aproximatedAmount);

        } else if (isExchangeRateExists(exchangeRate_USDA) && isExchangeRateExists(exchangeRate_USDB)) {
            Currency baseCurrency = getCurrencyById(basicCurrencyId);
            Currency targetCurrency = getCurrencyById(targetCurrencyId);
            float rate = getRate(exchangeRate_USDB) / getRate(exchangeRate_USDA);
            float amount = exchangeRequestDTO.amount();
            float convertedAmount = amount * rate;

            String aproximatedAmount = String.format("%.2f", convertedAmount);

            return new ExchangeResponseDTO(baseCurrency, targetCurrency, rate, amount, aproximatedAmount);
        } else {
            return null;
        }
    }


}
