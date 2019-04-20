package fi.tuni.cryptobag;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Currency.
 */
public class Currency implements Serializable {
    private String id;
    private String name;
    private String symbol;
    private List<Bag> bags;

    private BigDecimal price = BigDecimal.ZERO;

    private String imageUrl;
    /**
     * The Icon.
     */
    byte[] icon;

    /**
     * Instantiates a new Currency.
     *
     * @param id     the id
     * @param name   the name
     * @param symbol the symbol
     */
    public Currency(String id, String name, String symbol) {
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
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
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
    public String getSymbol() {
        return symbol;
    }

    /**
     * Sets symbol.
     *
     * @param symbol the symbol
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Gets price.
     *
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Sets price.
     *
     * @param price the price
     */
    public void setPrice(BigDecimal price) {
        if (price.doubleValue() >= 0 ) {
            this.price = price;
        }
    }

    /**
     * Gets bags.
     *
     * @return the bags
     */
    public List<Bag> getBags() {
        return bags;
    }

    /**
     * Sets image url.
     *
     * @param imageUrl the image url
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Gets bitmap.
     *
     * @return the bitmap
     */
    public Bitmap getBitmap() {
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
    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    /**
     * Get icon byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getIcon() {
        return icon;
    }

}
