package fi.tuni.cryptobag;

import java.io.Serializable;
import java.math.BigDecimal;

public class Bag implements Serializable {
    private Currency currency;
    private BigDecimal buyAmount = BigDecimal.ZERO;
    private BigDecimal sellAmount = BigDecimal.ZERO;
    private BigDecimal coinBuyPrice = BigDecimal.ZERO;
    private BigDecimal coinSellPrice = BigDecimal.ZERO;

    public Bag(Currency currency) {
        setCurrency(currency);
    }

    public Bag(Currency currency, BigDecimal buyAmount, BigDecimal sellAmount, BigDecimal coinBuyPrice, BigDecimal coinSellPrice) {
        setCurrency(currency);
        setBuyAmount(buyAmount);
        setSellAmount(sellAmount);
        setCoinBuyPrice(coinBuyPrice);
        setCoinSellPrice(coinSellPrice);
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

    public BigDecimal getCoinSellPrice() {
        return coinSellPrice;
    }

    public void setCoinSellPrice(BigDecimal coinSellPrice) {
        if(coinSellPrice.doubleValue() >= 0) {
            this.coinSellPrice = coinSellPrice;
        }
    }

    public String getProfit() {
        return (getCoinSellPrice().subtract(getCoinBuyPrice())).multiply(getSellAmount()).toString();
    }

    public String getEstimated() {
        BigDecimal result = (getCurrency().getPrice().subtract(getCoinBuyPrice())).multiply(getBuyAmount().subtract(getSellAmount()));
        return result.toString();
    }

    @Override
    public String toString() {
        return "Amount: " + getBuyAmount() + " Profit: " + getProfit() + " Price: " + getCoinSellPrice()
//                + "\n"
                + super.toString()
                ;
    }
}
