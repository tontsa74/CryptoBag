package fi.tuni.cryptobag;

import java.io.Serializable;
import java.math.BigDecimal;

public class Bag implements Serializable {
    private Currency currency;
    private BigDecimal buyAmount;
    private BigDecimal sellAmount;
    private BigDecimal coinBuyPrice;
    private BigDecimal coinCurrentPrice;

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

    public BigDecimal validateDouble(BigDecimal oldValue, String newValue) {
        BigDecimal result = oldValue;
        try {
            BigDecimal newBDValue = new BigDecimal(newValue);
            if (newBDValue.doubleValue() >= 0) {
                result = newBDValue;
            }
        } catch (Exception e) {
            Debug.print("tsilve", "Bag", "validateDouble.error: " + e, 1);
        }
        return result;
    }

    public BigDecimal getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(String buyAmount) {
        Debug.print("tsilve", "Bag", "setBuyAmount: " + buyAmount, 1);
        this.buyAmount = validateDouble(getBuyAmount(), buyAmount);
    }

    public BigDecimal getSellAmount() {
        return sellAmount;
    }

    public void setSellAmount(String sellAmount) {
        this.sellAmount = validateDouble(getSellAmount(), sellAmount);
    }

    public BigDecimal getCoinBuyPrice() {
        return coinBuyPrice;
    }

    public void setCoinBuyPrice(String coinBuyPrice) {

        this.coinBuyPrice = validateDouble(getCoinBuyPrice(), coinBuyPrice);
    }

    public BigDecimal getCoinCurrentPrice() {
        return coinCurrentPrice;
    }

    public void setCoinCurrentPrice(String coinCurrentPrice) {

        this.coinCurrentPrice = validateDouble(getCoinCurrentPrice(), coinCurrentPrice);

    }

    public String getProfit() {
//        BigDecimal currentPrice = new BigDecimal(getCoinCurrentPrice());
//        BigDecimal buyPrice = new BigDecimal(getCoinBuyPrice());
//        BigDecimal buyAmount = new BigDecimal(getBuyAmount());
//        BigDecimal sellAmount = new BigDecimal(getSellAmount());
        return (getCoinCurrentPrice().subtract(getCoinBuyPrice())).multiply(getSellAmount()).toString();
    }
    @Override
    public String toString() {
        return getCurrency().getName() + " - " + getBuyAmount() + " Profit: " + getProfit()
//                + super.toString()
                ;
    }
}
