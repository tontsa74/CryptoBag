package fi.tuni.cryptobag;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BaseActivity extends AppCompatActivity {
    protected final String TAG = "tsilve." + this.getClass().getName();

    ServiceConnection connectionToService;
    static boolean isBounded = false;
    static APIService apiService;

    static final String CURRENCIES_FILE = "currencies";
    static final String SELECTED_CURRENCIES_FILE = "selected_currencies";
    static List<Currency> currencies;
    static List<Currency> selectedCurrencies;
    static ArrayAdapter selectedCurrencyArrayAdapter;
    static ArrayAdapter currenciesAdapter;


    static final int HIGH_PRIORITY = 1;
    static final int MEDIUM_PRIORITY = 2;
    static final int LOW_PRIORITY = 3;
    static Set<Currency> lowToFetch;
    static Set<Currency> mediumToFetch;
    static Set<Currency> highToFetch;

    static int fetchCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionToService = new ApiServiceConnection();
    }

    @Override
    protected void onStart() {
        Debug.print(TAG, "onStart()", "onStart", 1);
        super.onStart();
        Intent intent = new Intent(this, APIService.class);
        bindService(intent, connectionToService, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        Debug.print(TAG, "onStop()", "onStop", 1);
        super.onStop();
        if (isBounded) {
            unbindService(connectionToService);
            isBounded = false;
        }
    }

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

    void loadSelectedCurrenciesFile() {

        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream = null;
        try {
            fileInputStream = openFileInput(SELECTED_CURRENCIES_FILE);
            objectInputStream = new ObjectInputStream(fileInputStream);
            selectedCurrencies = (ArrayList<Currency>) objectInputStream.readObject();
            Debug.print(TAG, "BaseActivity", "loadSelectedCurrencies: " + selectedCurrencies.size(), 2);
        } catch (ClassNotFoundException e) {
            Debug.print(TAG, "add SelectedCurrencies", "serialization problem: " + e, 2);
        } catch (IOException e) {
            Debug.print(TAG, "add SelectedCurrencies", "No bags file: " + e, 2);
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

    void saveSelectedCurrenciesFile() {
        Debug.print(TAG, "BaseActivity", "saveSelectedCurrenciesFile: " + selectedCurrencies.size(), 1);
        FileOutputStream fileOutputStream;
        ObjectOutputStream objectOutputStream = null;

        try {
            fileOutputStream = openFileOutput(SELECTED_CURRENCIES_FILE, Activity.MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(selectedCurrencies);
        } catch (IOException e) {
            Debug.print(TAG,"save selectedCurrencies", "error: " + e,2);
        } finally {
            try {
                objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    class ApiServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Debug.print(TAG, "ApiServiceConnection", "onServiceConnected", 2);
            LocalBinder binder = (LocalBinder) service;
            apiService = binder.getApiService();
            isBounded = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Debug.print(TAG, "ApiServiceConnection", "onServiceDisconnected", 2);
            isBounded = false;
        }
    }
}
