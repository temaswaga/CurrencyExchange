package DTO.ModelDTO;

import Model.Currency;

public record ExchangeRateDTO (
    int exchangeRateId,
    Currency baseCurrency,
    Currency targetCurrency,
    float rate
) {}
