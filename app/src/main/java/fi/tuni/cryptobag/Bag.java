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


    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
        currency.getBags().add(this);
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

    public BigDecimal getProfit() {
        BigDecimal result = (getCoinSellPrice().subtract(getCoinBuyPrice()))
                .multiply(getSellAmount());
        return result;
    }

    public BigDecimal getHoldValue() {
        BigDecimal result = getHold().multiply(getCurrency().getPrice());
        return result;
    }

    public BigDecimal getSoldValue() {
        BigDecimal result = getSellAmount().multiply(getCoinSellPrice());
        return result;
    }

    public BigDecimal getHold() {
        BigDecimal result = getBuyAmount().subtract(getSellAmount());
        return result;
    }

    public BigDecimal getInvestedValue() {
        BigDecimal result = getHold().multiply(getCoinBuyPrice());
        return result;
    }

    public BigDecimal getTotalValue() {
        BigDecimal result = (getSoldValue().add(getHoldValue())).subtract(getBuyValue());
        return result;
    }

    public BigDecimal getBuyValue() {
        BigDecimal result = getBuyAmount().multiply(getCoinBuyPrice());
        return result;
    }

    @Override
    public String toString() {
        return "Bag: "
//                "Amount: " + getBuyAmount().toString() + " Profit: " + getProfit().toString() + " Price: " + getCoinSellPrice().toString()
//                + "\n"
                + super.toString()
                ;
    }
}
