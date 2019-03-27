package fi.tuni.cryptobag;

import java.io.Serializable;
import java.math.BigDecimal;

public class Transaction implements Serializable {
    private Currency buyCurrency;
    private String buyAmount;
    private Currency sellCurrency;
    private String sellAmount;
    private String coinBuyPrice;
    private String coinCurrentPrice;

    public Transaction(Currency buyCurrency, String buyAmount) {
        setBuyCurrency(buyCurrency);
        setBuyAmount(buyAmount);
    }

    public Transaction(Currency buyCurrency, String buyAmount, Currency sellCurrency, String sellAmount, String coinBuyPrice, String coinCurrentPrice) {
        setBuyCurrency(buyCurrency);
        setBuyAmount(buyAmount);
        setSellCurrency(sellCurrency);
        setSellAmount(sellAmount);
        setCoinBuyPrice(coinBuyPrice);
        setCoinCurrentPrice(coinCurrentPrice);
    }

    public Currency getBuyCurrency() {
        return buyCurrency;
    }

    public void setBuyCurrency(Currency buyCurrency) {
        this.buyCurrency = buyCurrency;
    }

    public String getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(String buyAmount) {
        if (Double.parseDouble(buyAmount) > 0) {
            this.buyAmount = buyAmount;
        }
    }

    public Currency getSellCurrency() {
        return sellCurrency;
    }

    public void setSellCurrency(Currency sellCurrency) {
        this.sellCurrency = sellCurrency;
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
        return buyCurrency + " - " + buyAmount + " Profit: " + getProfit()
//                + super.toString()
                ;
    }
}
