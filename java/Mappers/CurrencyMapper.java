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
}
