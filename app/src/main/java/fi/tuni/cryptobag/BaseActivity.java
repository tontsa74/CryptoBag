package fi.tuni.cryptobag;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Base activity contains file operation methods for loading and saving data.
 */
public class BaseActivity extends AppCompatActivity {
    /**
     * The Tag.
     */
    protected final String TAG = "tsilve." + this.getClass().getName();


    /**
     * The Currencies file contains all currency objects.
     */
    static final String CURRENCIES_FILE = "currencies";

    /**
     * The Selected file contains all bag objects.
     */
    static final String SELECTED_FILE = "selected";

    /**
     * The Currencies contains all currency objects.
     */
    static List<Currency> currencies;

    /**
     * The Selected bags contains all bag objects.
     */
    static List<Bag> selectedBags;

    /**
     * The Init currencies contains currencies needed more data from API.
     */
    static List<Currency> initCurrencies;

    /**
     * The Fetch selected contains bags currencies to fetch.
     */
    static List<Currency> fetchSelected;

    /**
     * The High priority to fetch.
     */
    static final int HIGH_PRIORITY = 1;
    /**
     * The Medium priority to fetch.
     */
    static final int MEDIUM_PRIORITY = 2;
    /**
     * The Low priority to fetch.
     */
    static final int LOW_PRIORITY = 3;

    /**
     * Counts currencies queue for fetch.
     */
    static int fetchCount;

    /**
     * Load currencies file.
     */
    void loadCurrenciesFile() {

        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream = null;
        try {
            fileInputStream = openFileInput(CURRENCIES_FILE);
            objectInputStream = new ObjectInputStream(fileInputStream);
            currencies = (ArrayList<Currency>) objectInputStream.readObject();
            Debug.print(TAG, "BaseActivity", "loadCurrenciesFile: " + currencies.size(), 2);
        } catch (ClassNotFoundException e) {
            Debug.print(TAG, "add currencies", "serialization problem: " + e, 2);
        } catch (IOException e) {
            Debug.print(TAG, "add currencies", "No currencies file: " + e, 2);
        } finally {
            try {
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Save currencies file.
     */
    void saveCurrenciesFile() {
        Debug.print(TAG, "BaseActivity", "saveCurrenciesFile: " + currencies.size(), 1);
        FileOutputStream fileOutputStream;
        ObjectOutputStream objectOutputStream = null;

        try {
            fileOutputStream = openFileOutput(CURRENCIES_FILE, Activity.MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(currencies);
        } catch (IOException e) {
            Debug.print(TAG,"save currencies", "error: " + e,2);
        } finally {
            try {
                objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Load selected file.
     */
    void loadSelectedFile() {

        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream = null;
        try {
            fileInputStream = openFileInput(SELECTED_FILE);
            objectInputStream = new ObjectInputStream(fileInputStream);
            selectedBags = (ArrayList<Bag>) objectInputStream.readObject();
            Debug.print(TAG, "BaseActivity", "loadSelectedFile: " + selectedBags.size(), 2);
        } catch (ClassNotFoundException e) {
            Debug.print(TAG, "loadSelectedFile", "serialization problem: " + e, 2);
        } catch (IOException e) {
            Debug.print(TAG, "loadSelectedFile", "No bags file: " + e, 2);
        } finally {
            try {
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Save selected file.
     */
    void saveSelectedFile() {
        Debug.print(TAG, "BaseActivity", "saveSelectedFile: " + selectedBags.size(), 1);
        FileOutputStream fileOutputStream;
        ObjectOutputStream objectOutputStream = null;

        try {
            fileOutputStream = openFileOutput(SELECTED_FILE, Activity.MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(selectedBags);
        } catch (IOException e) {
            Debug.print(TAG,"saveSelectedFile", "error: " + e,2);
        } finally {
            try {
                objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
