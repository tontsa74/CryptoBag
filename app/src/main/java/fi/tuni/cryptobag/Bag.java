package fi.tuni.cryptobag;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The type Bag.
 */
class Bag implements Serializable {
    /**
     * The Currency.
     */
    private Currency currency;
    /**
     * The Buy amount.
     */
    private BigDecimal buyAmount = BigDecimal.ZERO;
    /**
     * The Sell amount.
     */
    private BigDecimal sellAmount = BigDecimal.ZERO;
    /**
     * The Coin buy price.
     */
    private BigDecimal coinBuyPrice = BigDecimal.ZERO;
    /**
     * The Coin sell price.
     */
    private BigDecimal coinSellPrice = BigDecimal.ZERO;

    /**
     * Instantiates a new Bag.
     *
     * @param currency the currency
     */
    Bag(Currency currency) {
        setCurrency(currency);
    }


    /**
     * Gets currency.
     *
     * @return the currency
     */
    Currency getCurrency() {
        return currency;
    }

    /**
     * Sets currency.
     *
     * @param currency the currency
     */
    void setCurrency(Currency currency) {
        this.currency = currency;
        currency.getBags().add(this);
    }

    /**
     * Gets buy amount.
     *
     * @return the buy amount
     */
    BigDecimal getBuyAmount() {
        return buyAmount;
    }

    /**
     * Sets buy amount.
     *
     * @param buyAmount the buy amount
     */
    void setBuyAmount(BigDecimal buyAmount) {
        if(buyAmount.doubleValue() >= 0) {
            this.buyAmount = buyAmount;
        }
    }

    /**
     * Gets sell amount.
     *
     * @return the sell amount
     */
    BigDecimal getSellAmount() {
        return sellAmount;
    }

    /**
     * Sets sell amount.
     *
     * @param sellAmount the sell amount
     */
    void setSellAmount(BigDecimal sellAmount) {
        if(sellAmount.doubleValue() >= 0) {
            this.sellAmount = sellAmount;
        }
    }

    /**
     * Gets coin buy price.
     *
     * @return the coin buy price
     */
    BigDecimal getCoinBuyPrice() {
        return coinBuyPrice;
    }

    /**
     * Sets coin buy price.
     *
     * @param coinBuyPrice the coin buy price
     */
    void setCoinBuyPrice(BigDecimal coinBuyPrice) {
        if(coinBuyPrice.doubleValue() >= 0) {
            this.coinBuyPrice = coinBuyPrice;
        }
    }

    /**
     * Gets coin sell price.
     *
     * @return the coin sell price
     */
    BigDecimal getCoinSellPrice() {
        return coinSellPrice;
    }

    /**
     * Sets coin sell price.
     *
     * @param coinSellPrice the coin sell price
     */
    void setCoinSellPrice(BigDecimal coinSellPrice) {
        if(coinSellPrice.doubleValue() >= 0) {
            this.coinSellPrice = coinSellPrice;
        }
    }

    /**
     * Gets profit.
     *
     * @return the profit
     */
    BigDecimal getProfit() {
        return (getCoinSellPrice().subtract(getCoinBuyPrice())).multiply(getSellAmount());
    }

    /**
     * Gets hold value.
     *
     * @return the hold value
     */
    BigDecimal getHoldValue() {
        return getHold().multiply(getCurrency().getPrice());
    }

    /**
     * Gets sold value.
     *
     * @return the sold value
     */
    private BigDecimal getSoldValue() {
        return getSellAmount().multiply(getCoinSellPrice());
    }

    /**
     * Gets hold.
     *
     * @return the hold
     */
    private BigDecimal getHold() {
        return getBuyAmount().subtract(getSellAmount());
    }

    /**
     * Gets invested value.
     *
     * @return the invested value
     */
    BigDecimal getInvestedValue() {
        return getHold().multiply(getCoinBuyPrice());
    }

    /**
     * Gets total value.
     *
     * @return the total value
     */
    BigDecimal getTotalValue() {
        return (getSoldValue().add(getHoldValue())).subtract(getBuyValue());
    }

    /**
     * Gets buy value.
     *
     * @return the buy value
     */
    private BigDecimal getBuyValue() {
        return getBuyAmount().multiply(getCoinBuyPrice());
    }
}
