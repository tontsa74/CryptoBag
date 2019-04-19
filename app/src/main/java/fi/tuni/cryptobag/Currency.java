package fi.tuni.cryptobag;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;

public class Currency implements Serializable {
    private String id;
    private String name;
    private String symbol;
    private Bag bag;

    private BigDecimal price = BigDecimal.ZERO;

    private String imageUrl;
    byte[] icon;

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        if (price.doubleValue() >= 0 ) {
            this.price = price;
        }
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Bitmap getBitmap() {
        Bitmap bmp;

        byte[] byteArray = this.icon;
        bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return bmp;
    }

    public void setIcon(Bitmap bitmap) {
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        this.icon = bStream.toByteArray();
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public byte[] getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        return getName() + "  -  " + getSymbol();
    }
}
