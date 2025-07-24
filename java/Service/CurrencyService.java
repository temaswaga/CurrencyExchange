package Service;

import Model.Currency;
import DAO.CurrenciesDAO;
import DTO.ModelDTO.CurrencyDTO;
import Mappers.CurrencyMapper;

import java.sql.SQLException;

public class CurrencyService {

    public static CurrencyDTO getCurrencyByCode(String code) throws SQLException {

        String currenciesCode = code.substring(1).toUpperCase();

//        if (code == null || currenciesCode.length() != 3) {
//            throw new IllegalArgumentException("Код валюты должен содержать ровно 3 символа");
//        }

        if(!DAO.CurrenciesDAO.isCurrencyExistsByCode(currenciesCode)) {
            return null;
        } else {
            Currency currency = CurrenciesDAO.getCurrencyByCode(currenciesCode);
            return CurrencyMapper.toDTO(currency);
        }
    }


    public static CurrencyDTO postCurrency (CurrencyDTO postedCurrencyDTO) throws SQLException {
        Currency currency = CurrencyMapper.toEntity(postedCurrencyDTO);


        return CurrencyMapper.toDTO(CurrenciesDAO.postCurrency(currency));
    }

}
