package fi.tuni.cryptobag;

import java.io.Serializable;
import java.math.BigDecimal;

public class Bag implements Serializable {
    private Currency currency;
    private String buyAmount;
    private String sellAmount;
    private String coinBuyPrice;
    private String coinCurrentPrice;

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

    public String getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(String buyAmount) {
        if (Double.parseDouble(buyAmount) > 0) {
            this.buyAmount = buyAmount;
        }
    }

    public String getSellAmount() {
        return sellAmount;
    }

    public void setSellAmount(String sellAmount) {
        this.sellAmount = sellAmount;
    }

    public String getCoinBuyPrice() {
        return coinBuyPrice;
    }

    public void setCoinBuyPrice(String coinBuyPrice) {
        this.coinBuyPrice = coinBuyPrice;
    }

    public String getCoinCurrentPrice() {
        return coinCurrentPrice;
    }

    public void setCoinCurrentPrice(String coinCurrentPrice) {
        this.coinCurrentPrice = coinCurrentPrice;
    }

    public String getProfit() {
        BigDecimal currentPrice = new BigDecimal(getCoinCurrentPrice());
        BigDecimal buyPrice = new BigDecimal(getCoinBuyPrice());
        BigDecimal buyAmount = new BigDecimal(getBuyAmount());
        return (currentPrice.subtract(buyPrice)).multiply(buyAmount).toString();
    }
    @Override
    public String toString() {
        return currency + " - " + buyAmount + " Profit: " + getProfit()
//                + super.toString()
                ;
    }
}
