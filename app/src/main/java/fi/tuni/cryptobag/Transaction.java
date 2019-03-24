package fi.tuni.cryptobag;

public class Transaction {
    private String buyCurrency;
    private double buyAmount;

    public Transaction(String buyCurrency, double buyAmount) {
        setBuyCurrency(buyCurrency);
        setBuyAmount(buyAmount);
    }

    public String getBuyCurrency() {
        return buyCurrency;
    }

    public void setBuyCurrency(String buyCurrency) {
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

    @Override
    public String toString() {
        return buyCurrency + " - " + buyAmount
//                + super.toString()
                ;
    }
}
