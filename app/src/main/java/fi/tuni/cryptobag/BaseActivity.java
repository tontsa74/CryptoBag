package fi.tuni.cryptobag;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity {
    protected final String TAG = "tsilve." + this.getClass().getName();

    static final String BAGS_FILE = "bags";
    static final String CURRENCIES_FILE = "currencies";
    static List<Currency> currencies;
    static List<Bag> bags;

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

    void loadBagsFile() {

        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream = null;
        try {
            fileInputStream = openFileInput(BAGS_FILE);
            objectInputStream = new ObjectInputStream(fileInputStream);
            bags = (ArrayList<Bag>) objectInputStream.readObject();
            Debug.print(TAG, "BaseActivity", "loadBagsFile: " + bags.size(), 2);
        } catch (ClassNotFoundException e) {
            Debug.print(TAG, "add bags", "serialization problem: " + e, 2);
        } catch (IOException e) {
            Debug.print(TAG, "add bags", "No bags file: " + e, 2);
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

    void saveBagsFile() {
        Debug.print(TAG, "BaseActivity", "saveBagsFile: " + bags.size(), 1);
        FileOutputStream fileOutputStream;
        ObjectOutputStream objectOutputStream = null;

        try {
            fileOutputStream = openFileOutput(BAGS_FILE, Activity.MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(bags);
        } catch (IOException e) {
            Debug.print(TAG,"save bags", "error: " + e,2);
        } finally {
            try {
                objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
