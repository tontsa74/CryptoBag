package fi.tuni.cryptobag;

import java.io.Serializable;

public class Currency implements Serializable {
    private String id;
    private String name;
    private String symbol;
    private Bag bag;

    private String price;

    public Currency(String id, String name, String symbol) {
        setId(id);
        setName(name);
        setSymbol(symbol);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Bag getBag() {
        return bag;
    }

    public String getBagString() {
        String result = "";
        try {
            result += bag.toString() + "\n";
        } catch (Exception e) {
            System.out.println("tsilve." + e);
        }

        return result;
    }

    public void setBag(Bag bag) {
        this.bag = bag;
    }

    @Override
    public String toString() {
        return name + " - " + getSymbol() + ": \n"
                + getBagString()
                + super.toString()
                ;
    }
}
