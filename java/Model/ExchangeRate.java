package Model;

public class ExchangeRate {
    private int exchangeRateId;
    private int baseCurrencieID;
    private int targetCurrencyID;
    private float rate;

    public ExchangeRate(int baseCurrencieID, int targetCurrencyID, float rate) {
        this.rate = rate;
        this.targetCurrencyID = targetCurrencyID;
        this.baseCurrencieID = baseCurrencieID;
    }

    public ExchangeRate(int baseCurrencieID, int targetCurrencyID) {
        this.baseCurrencieID = baseCurrencieID;
        this.targetCurrencyID = targetCurrencyID;
    }

    public int getExchangeRateId() {
        return exchangeRateId;
    }

    public int getBaseCurrencieID() {
        return baseCurrencieID;
    }

    public void setExchangeRateId(int exchangeRateId) {
        this.exchangeRateId = exchangeRateId;
    }

    public ExchangeRate(int exchangeRateId, int baseCurrencieID, int targetCurrencyID, float rate) {
        this.exchangeRateId = exchangeRateId;
        this.baseCurrencieID = baseCurrencieID;
        this.targetCurrencyID = targetCurrencyID;
        this.rate = rate;
    }

    public int getTargetCurrencyID() {
        return targetCurrencyID;
    }

    public float getRate() {
        return rate;
    }
}
