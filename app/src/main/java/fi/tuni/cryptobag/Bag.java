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
        setBuyAmount(new BigDecimal(0));
        setSellAmount(new BigDecimal(0));
        setCoinBuyPrice(new BigDecimal(0));
        setCoinCurrentPrice(new BigDecimal(0));

    }

    public Bag(Currency currency, BigDecimal buyAmount) {
        setCurrency(currency);
        setBuyAmount(buyAmount);
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
    }

    public BigDecimal getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(BigDecimal buyAmount) {
        Debug.print("tsilve", "Bag", "setBuyAmount: " + buyAmount, 1);
        this.buyAmount = buyAmount;
    }

    public BigDecimal getSellAmount() {
        return sellAmount;
    }

    public void setSellAmount(BigDecimal sellAmount) {
        this.sellAmount = sellAmount;
    }

    public BigDecimal getCoinBuyPrice() {
        return coinBuyPrice;
    }

    public void setCoinBuyPrice(BigDecimal coinBuyPrice) {

        this.coinBuyPrice = coinBuyPrice;
    }

    public BigDecimal getCoinCurrentPrice() {
        return coinCurrentPrice;
    }

    public void setCoinCurrentPrice(BigDecimal coinCurrentPrice) {

        this.coinCurrentPrice = coinCurrentPrice;

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
