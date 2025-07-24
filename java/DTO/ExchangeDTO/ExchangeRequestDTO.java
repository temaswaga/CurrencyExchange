package DTO.ExchangeDTO;

public record ExchangeRequestDTO(
        String baseCurrencyCode,
        String targetCurrencyCode,
        float amount
) {}
