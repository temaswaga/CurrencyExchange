package Mappers;

import Model.Currency;
import Model.ExchangeRate;
import DTO.ModelDTO.ExchangeRateDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public class ExchangeRateMapper {

    public static ExchangeRate toEntity (ExchangeRateDTO exchangeRateDTO) {
        return new ExchangeRate(exchangeRateDTO.exchangeRateId(), exchangeRateDTO.baseCurrency().getId(), exchangeRateDTO.targetCurrency().getId(), exchangeRateDTO.rate());
    }

    public static ExchangeRateDTO toDTO (ExchangeRate exchangeRate) throws SQLException {
        Currency baseCurrency = DAO.CurrenciesDAO.getCurrencyById(exchangeRate.getBaseCurrencieID());
        Currency targetCurrency = DAO.CurrenciesDAO.getCurrencyById(exchangeRate.getTargetCurrencyID());

        return new ExchangeRateDTO(exchangeRate.getExchangeRateId(), baseCurrency, targetCurrency, exchangeRate.getRate());
    }

    public static ArrayList<ExchangeRateDTO> getAllExchangeRatesDTOs(ArrayList<ExchangeRate> exchangeRates) throws SQLException {
        ArrayList<ExchangeRateDTO> exchangeRateDTOs = new ArrayList<>();

        for (ExchangeRate exchangeRate : exchangeRates) {
            ExchangeRateDTO exchangeRateDTO = toDTO(exchangeRate);
            exchangeRateDTOs.add(exchangeRateDTO);
        }

        return exchangeRateDTOs;
    }

}
