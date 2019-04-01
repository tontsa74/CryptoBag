package fi.tuni.cryptobag;

import java.io.Serializable;
import java.math.BigDecimal;

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

    public String getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(String buyAmount) {
        if (Double.parseDouble(buyAmount) >= 0) {
            this.buyAmount = buyAmount;
        }
    }

    public String getSellAmount() {
        return sellAmount;
    }

    public void setSellAmount(String sellAmount) {
        if (Double.parseDouble(sellAmount) >= 0) {
            this.sellAmount = sellAmount;
        }
    }

    public String getCoinBuyPrice() {
        return coinBuyPrice;
    }

    public void setCoinBuyPrice(String coinBuyPrice) {
        if (Double.parseDouble(coinBuyPrice) >= 0) {
            this.coinBuyPrice = coinBuyPrice;
        }
    }

    public String getCoinCurrentPrice() {
        return coinCurrentPrice;
    }

    public void setCoinCurrentPrice(String coinCurrentPrice) {
        if (Double.parseDouble(coinCurrentPrice) >= 0) {
            this.coinCurrentPrice = coinCurrentPrice;
        }

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
