package Mappers;

import Model.Currency;
import DTO.ModelDTO.CurrencyDTO;

public class CurrencyMapper {

    public static Currency toEntity (CurrencyDTO currencyDTO) {
        return new Currency(currencyDTO.id(), currencyDTO.code(), currencyDTO.fullName(), currencyDTO.sign());
    }

    public static CurrencyDTO toDTO (Currency currency) {
        return new CurrencyDTO(currency.getId(), currency.getCode(), currency.getFullName(), currency.getSign());
    }


//    static public ArrayList<CurrencyDTO> getAllCurrenciesDTO(ArrayList<Currency> currencies) {
//        ArrayList<CurrencyDTO> currenciesDTO = new ArrayList<>();
//
//        for (int i = 0; i < currencies.size(); i++) {
//            Currency currency = currencies.get(i);
//            currenciesDTO.add(new CurrencyDTO(currency.getId(), currency.getCode(), currency.getFullName(), currency.getSign()));
//        }
//
//        return currenciesDTO;
//    }


    static public CurrencyDTO getCurrencyDTO(Currency currency) {
        CurrencyDTO currencyDTO = new CurrencyDTO(currency.getId(), currency.getCode(), currency.getFullName(), currency.getSign());
        return currencyDTO;
    }
}
