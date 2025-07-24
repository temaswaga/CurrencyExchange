package DTO.ExchangeDTO;

import Model.Currency;

public record ExchangeResponseDTO(
        Currency baseCurrency,
        Currency targetCurrency,
        float rate,
        float amount,
        String convertedAmount
) {
}
