package fi.tuni.cryptobag;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

public class Bag implements Serializable {
    private Currency currency;
    private String buyAmount;
    private String sellAmount;
    private String coinBuyPrice;
    private String coinCurrentPrice;

    public Bag(Currency currency) {
        setCurrency(currency);
        setBuyAmount("0");
        setSellAmount("0");
        setCoinBuyPrice("0");
        setCoinCurrentPrice("0");

    }

    public Bag(Currency currency, String buyAmount) {
        setCurrency(currency);
        setBuyAmount(buyAmount);
    }

    public Bag(Currency currency, String buyAmount, String sellAmount, String coinBuyPrice, String coinCurrentPrice) {
        setCurrency(currency);
        setBuyAmount(buyAmount);
        setSellAmount(sellAmount);
        setCoinBuyPrice(coinBuyPrice);
        setCoinCurrentPrice(coinCurrentPrice);
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String validateDouble(String oldValue, String newValue) {
        String result = oldValue;
        try {
            if (Double.parseDouble(newValue) >= 0) {
                result = newValue;
            }
        } catch (Exception e) {
            Debug.print("tsilve", "Bag", "validateDouble.error: " + e, 1);
        }
        return result;
    }

    public String getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(String buyAmount) {
        Debug.print("tsilve", "Bag", "setBuyAmount: " + buyAmount, 1);
        this.buyAmount = validateDouble(getBuyAmount(), buyAmount);
    }

    public String getSellAmount() {
        return sellAmount;
    }

    public void setSellAmount(String sellAmount) {
        this.sellAmount = validateDouble(getSellAmount(), sellAmount);
    }

    public String getCoinBuyPrice() {
        return coinBuyPrice;
    }

    public void setCoinBuyPrice(String coinBuyPrice) {

        this.coinBuyPrice = validateDouble(getCoinBuyPrice(), coinBuyPrice);
    }

    public String getCoinCurrentPrice() {
        return coinCurrentPrice;
    }

    public void setCoinCurrentPrice(String coinCurrentPrice) {

        this.coinCurrentPrice = validateDouble(getCoinCurrentPrice(), coinCurrentPrice);

    }

    public String getProfit() {
        BigDecimal currentPrice = new BigDecimal(getCoinCurrentPrice());
        BigDecimal buyPrice = new BigDecimal(getCoinBuyPrice());
        BigDecimal buyAmount = new BigDecimal(getBuyAmount());
        BigDecimal sellAmount = new BigDecimal(getSellAmount());
        return (currentPrice.subtract(buyPrice)).multiply(sellAmount).toString();
    }
    @Override
    public String toString() {
        return getCurrency().getName() + " - " + getBuyAmount() + " Profit: " + getProfit()
//                + super.toString()
                ;
    }
}
