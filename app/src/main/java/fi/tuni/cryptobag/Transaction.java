package fi.tuni.cryptobag;

import java.io.Serializable;

public class Transaction implements Serializable {
    private Currency buyCurrency;
    private double buyAmount;
    private Currency sellCurrency;
    private double sellAmount;
    private double coinBuyPrice;
    private double coinCurrentPrice;

    public Transaction(Currency buyCurrency, double buyAmount) {
        setBuyCurrency(buyCurrency);
        setBuyAmount(buyAmount);
    }

    public Transaction(Currency buyCurrency, double buyAmount, Currency sellCurrency, double sellAmount, double coinBuyPrice, double coinCurrentPrice) {
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

    public double getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(double buyAmount) {
        if (buyAmount > 0) {
            this.buyAmount = buyAmount;
        }
    }

    public Currency getSellCurrency() {
        return sellCurrency;
    }

    public void setSellCurrency(Currency sellCurrency) {
        this.sellCurrency = sellCurrency;
    }

    public double getSellAmount() {
        return sellAmount;
    }

    public void setSellAmount(double sellAmount) {
        this.sellAmount = sellAmount;
    }

    public double getCoinBuyPrice() {
        return coinBuyPrice;
    }

    public void setCoinBuyPrice(double coinBuyPrice) {
        this.coinBuyPrice = coinBuyPrice;
    }

    public double getCoinCurrentPrice() {
        return coinCurrentPrice;
    }

    public void setCoinCurrentPrice(double coinCurrentPrice) {
        this.coinCurrentPrice = coinCurrentPrice;
    }

    public double getProfit() {
        return (getCoinCurrentPrice() - getCoinBuyPrice()) * getBuyAmount();
    }
    @Override
    public String toString() {
        return buyCurrency + " - " + buyAmount + " Profit: " + getProfit()
//                + super.toString()
                ;
    }
}
