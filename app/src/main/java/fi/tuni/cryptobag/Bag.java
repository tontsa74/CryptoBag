package fi.tuni.cryptobag;

import java.io.Serializable;
import java.math.BigDecimal;

public class Bag implements Serializable {
    private Currency currency;
    private BigDecimal buyAmount = BigDecimal.ZERO;
    private BigDecimal sellAmount = BigDecimal.ZERO;
    private BigDecimal coinBuyPrice = BigDecimal.ZERO;
    private BigDecimal coinCurrentPrice = BigDecimal.ZERO;

    public Bag(Currency currency) {
        setCurrency(currency);
    }

    public Bag(Currency currency, BigDecimal buyAmount, BigDecimal sellAmount, BigDecimal coinBuyPrice, BigDecimal coinCurrentPrice) {
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
        currency.setBag(this);
    }

    public BigDecimal getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(BigDecimal buyAmount) {
        if(buyAmount.doubleValue() >= 0) {
            this.buyAmount = buyAmount;
        }
    }

    public BigDecimal getSellAmount() {
        return sellAmount;
    }

    public void setSellAmount(BigDecimal sellAmount) {
        if(sellAmount.doubleValue() >= 0) {
            this.sellAmount = sellAmount;
        }
    }

    public BigDecimal getCoinBuyPrice() {
        return coinBuyPrice;
    }

    public void setCoinBuyPrice(BigDecimal coinBuyPrice) {
        if(coinBuyPrice.doubleValue() >= 0) {
            this.coinBuyPrice = coinBuyPrice;
        }
    }

    public BigDecimal getCoinCurrentPrice() {
        return coinCurrentPrice;
    }

    public void setCoinCurrentPrice(BigDecimal coinCurrentPrice) {
        if(coinCurrentPrice.doubleValue() >= 0) {
            this.coinCurrentPrice = coinCurrentPrice;
        }
    }

    public String getProfit() {
        return (getCoinCurrentPrice().subtract(getCoinBuyPrice())).multiply(getSellAmount()).toString();
    }
    @Override
    public String toString() {
        return "Amount: " + getBuyAmount() + " Profit: " + getProfit() + " Price: " + getCoinCurrentPrice()
//                + "\n"
//                + super.toString()
                ;
    }
}
