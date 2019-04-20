package fi.tuni.cryptobag;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Currency.
 */
public class Currency implements Serializable {
    /**
     * The Id.
     */
    private String id;

    /**
     * The Name.
     */
    private String name;

    /**
     * The Symbol.
     */
    private String symbol;

    /**
     * The Bags.
     */
    private List<Bag> bags;

    private BigDecimal price = BigDecimal.ZERO;

    private String imageUrl;
    /**
     * The Icon.
     */
    private byte[] icon;

    /**
     * Instantiates a new Currency.
     *
     * @param id     the id
     * @param name   the name
     * @param symbol the symbol
     */
    Currency(String id, String name, String symbol) {
        setId(id);
        setName(name);
        setSymbol(symbol);

        bags = new ArrayList<>();
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    private void setId(String id) {
        this.id = id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets symbol.
     *
     * @return the symbol
     */
    String getSymbol() {
        return symbol;
    }

    /**
     * Sets symbol.
     *
     * @param symbol the symbol
     */
    private void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Gets price.
     *
     * @return the price
     */
    BigDecimal getPrice() {
        return price;
    }

    /**
     * Sets price.
     *
     * @param price the price
     */
    void setPrice(BigDecimal price) {
        if (price.doubleValue() >= 0 ) {
            this.price = price;
        }
    }

    /**
     * Gets bags.
     *
     * @return the bags
     */
    List<Bag> getBags() {
        return bags;
    }

    /**
     * Sets image url.
     *
     * @param imageUrl the image url
     */
    void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Gets bitmap.
     *
     * @return the bitmap
     */
    Bitmap getBitmap() {
        Bitmap bmp;

        byte[] byteArray = this.icon;
        bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return bmp;
    }

    /**
     * Sets icon.
     *
     * @param icon the icon
     */
    void setIcon(byte[] icon) {
        this.icon = icon;
    }

    /**
     * Get icon byte [ ].
     *
     * @return the byte [ ]
     */
    byte[] getIcon() {
        return icon;
    }

}
