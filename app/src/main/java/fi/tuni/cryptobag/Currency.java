package fi.tuni.cryptobag;

import java.io.Serializable;

public class Currency implements Serializable {
    private String id;
    private String name;
    private String symbol;

    private String type;
    private String price;
    private Bag[] bags;

    public Currency(String id, String name, String symbol) {
        setId(id);
        setName(name);
        setSymbol(symbol);

//        this.type = type;
//        this.price = price;
//        this.bags = bags;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Bag[] getBags() {
        return bags;
    }

    public void setBags(Bag[] bags) {
        this.bags = bags;
    }

    @Override
    public String toString() {
        return name + ": ";
    }
}
