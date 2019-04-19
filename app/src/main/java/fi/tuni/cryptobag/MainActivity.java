package fi.tuni.cryptobag;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    ServiceConnection connectionToService;
    private static final int REQUEST_CODE_ADD_BAG = 15;

    ArrayAdapter<Currency> selectedCurrencyArrayAdapter;

    TextView totalProfit, totalHoldValue, totalTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalProfit = (TextView) findViewById(R.id.totalProfit);
        totalHoldValue = (TextView) findViewById(R.id.totalHoldValue);
        totalTotal = (TextView) findViewById(R.id.totalTotal);

        Debug.loadDebug(this);
        Debug.print(TAG, "onCreate()", "Logging my application", 1);

        loadCurrenciesFile();
        loadSelectedCurrenciesFile();

        if (currencies == null) {
            currencies = new ArrayList<Currency>();
        }
        if (selectedCurrencies == null) {
            selectedCurrencies = new ArrayList<Currency>();
        }
        if (initCurrencies == null) {
            initCurrencies = new ArrayList<Currency>();
        }

        for (int i=0; i < currencies.size(); i++) {
            if(currencies.get(i).getIcon() != null) {

            } else {
                initCurrencies.add(currencies.get(i));
            }
            for (Currency selCur : selectedCurrencies) {

                if (selCur.getId().equals(currencies.get(i).getId())) {
                    currencies.set(i, selCur);
                }
            }
        }

        connectionToService = new ApiServiceConnection();


        final ListView selectedCurrenciesListView = (ListView) findViewById(R.id.selectedCurrenciesListView);

        selectedCurrenciesListView.setOnItemClickListener((parent, view, position, id) -> {
            Debug.print(TAG, "setOnItemClickListener",  "position_id: " + position + "_" + id, 2);
            Currency currency = (Currency) selectedCurrenciesListView.getItemAtPosition(position);
            editCurrency(currency, position);
        });

        selectedCurrenciesListView.setOnItemLongClickListener((parent, view, position, id) -> {
            Debug.print(TAG, "setOnItemClickListener",  "position_id: " + position + "_" + id, 2);
            Currency currency = (Currency) selectedCurrenciesListView.getItemAtPosition(position);
            deleteCurrency(currency, position);
            return true;
        });

        selectedCurrencyArrayAdapter = new ArrayAdapter(this, R.layout.currency_item, R.id.currencyItemTextView, selectedCurrencies) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = inflater.inflate(R.layout.currency_item, parent, false);
                Currency currency = (Currency) getItem(position);// selectedCurrenciesListView.getItemAtPosition(position);
                Bag bag = currency.getBag();

                TextView currencyItemTextView = (TextView) rowView.findViewById(R.id.currencyItemTextView);
                TextView profitItemTextView = (TextView) rowView.findViewById(R.id.profitItemTextView);
                //TextView priceTextView = (TextView) rowView.findViewById(R.id.priceTextView);
                TextView holdValueTextView = (TextView) rowView.findViewById(R.id.holdValueTextView);
                TextView totalTextView = (TextView) rowView.findViewById(R.id.totalTextView);
                //TextView investItemTextView = (TextView) rowView.findViewById(R.id.investItemTextView);

                ImageView imageView = (ImageView) rowView.findViewById(R.id.iconImageView);
                if(currency.getIcon() != null) {
                    //Bitmap bitmap = currency.getBitmap();
                    imageView.setImageBitmap(currency.getBitmap());
                }
                currencyItemTextView.setText(currency.getName());

                //investItemTextView.setText(bag.getInvested().toString());

                try {
                    BigDecimal profit = bag.getProfit().setScale(2, RoundingMode.HALF_UP);
                    if(profit.doubleValue() > 0) {
                        profitItemTextView.setTextColor(Color.rgb(29, 196, 112));
                    } else if(profit.doubleValue() < 0) {
                        profitItemTextView.setTextColor(Color.RED);
                    }
                    profitItemTextView.setText(profit.toString());
                } catch (Exception e) {
                    Debug.print(TAG, "selectedCurrencyArrayAdapter", "profit e: " + e, 4);
                }


                try {
                    BigDecimal holdValue = bag.getHoldValue().setScale(2, RoundingMode.HALF_UP);
                    if (holdValue.doubleValue() > 0) {
                        holdValueTextView.setTextColor(Color.rgb(29, 196, 112));
                    } else if (holdValue.doubleValue() < 0) {
                        holdValueTextView.setTextColor(Color.RED);
                    }
                    holdValueTextView.setText(holdValue.toString());
                } catch (Exception e) {
                    Debug.print(TAG, "selectedCurrencyArrayAdapter", "estimated e: " + e, 4);
                }

                try {
                    BigDecimal total = bag.getTotal().setScale(2, RoundingMode.HALF_UP);
                    if (total.doubleValue() > 0) {
                        totalTextView.setTextColor(Color.rgb(29, 196, 112));
                    } else if (total.doubleValue() < 0) {
                        totalTextView.setTextColor(Color.RED);
                    }
                    totalTextView.setText(total.toString());
                } catch (Exception e) {
                    Debug.print(TAG, "selectedCurrencyArrayAdapter", "total e: " + e, 4);
                }

                //priceTextView.setText("" + currency.getPrice());
                //Debug.print(TAG, "selectedCurrencyArrayAdapter",  "position: " + position + currencyItemTextView.getText(), 3);
                return rowView;//super.getView(position, convertView, parent);
            }
        };
        selectedCurrenciesListView.setAdapter(selectedCurrencyArrayAdapter);

        updateActivity();
    }

    @Override
    protected void onStart() {
        Debug.print(TAG, "onStart()", "onStart", 1);
        super.onStart();
        Intent intent = new Intent(this, APIService.class);
        if(!isBounded) {
            bindService(intent, connectionToService, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        Debug.print(TAG, "onStop()", "onStop", 1);
        super.onStop();
        if (isBounded) {
            Debug.print(TAG, "onStop()", "isBounded", 2);
            unbindService(connectionToService);
            isBounded = false;
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        Debug.print(TAG, "BroadcastReceiver()", "mMessageReceiver", 1);
        Currency currency = (Currency) intent.getSerializableExtra("currency");
        double price = intent.getDoubleExtra("price", 0);
        String imageUrl = intent.getStringExtra("imageUrl");
        byte[] icon = intent.getByteArrayExtra("icon");

        currency.setPrice(new BigDecimal(""+price));
        currency.setImageUrl(imageUrl);
        currency.setIcon(icon);

        updateActivity();
        }
    };

    @Override
    protected void onResume() {
        Debug.print(TAG, "onResume()", "onResume", 1);
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("tsilve.api"));

    }

    @Override
    protected void onPause() {
        Debug.print(TAG, "onPause()", "onPause", 1);
        super.onPause();
        saveCurrenciesFile();
        saveSelectedCurrenciesFile();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    public void editCurrency(Currency currency, int position) {
        Debug.print(TAG, "editCurrency()", "edit currency: " + currency + ", pos: " + position, 1);
        Intent intent = new Intent(this, BagActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
        apiService.fetch(currency, HIGH_PRIORITY);
    }

    public void addCurrency(View view) {
        Debug.print(TAG, "MainActivity()", "addCurrency", 1);
        Intent intent = new Intent(this, BagActivity.class);
        intent.putExtra("position", -1);
        startActivityForResult(intent, REQUEST_CODE_ADD_BAG);
        apiService.fetch(selectedCurrencies, MEDIUM_PRIORITY);
    }

    public void deleteCurrency(Currency currency, int position) {
        Debug.print(TAG, "deleteCurrency()", "edit currency: " + currency + ", pos: " + position, 1);

        AlertDialog.Builder alert = new AlertDialog.Builder(
                MainActivity.this);

        alert.setTitle("Delete");
        alert.setMessage("Do you want delete " + currency.getName());
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedCurrencies.remove(position);
                selectedCurrencyArrayAdapter.notifyDataSetChanged();

            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Debug.print(TAG, "MainActivity()", "onActivityResult", 1);

        if (requestCode == REQUEST_CODE_ADD_BAG) {
            Debug.print(TAG, "onActivityResult()", "REQUEST_CODE_ADD_BAG", 1);
        }

        updateActivity();
    }

    private void updateActivity() {
        BigDecimal profit = BigDecimal.ZERO;
        BigDecimal hold = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;


        for (Currency selCur : selectedCurrencies) {
            Bag bag = selCur.getBag();
            try {
                profit = profit.add(bag.getProfit());
                hold = hold.add(bag.getHoldValue());
                total = total.add(bag.getTotal());
            } catch(Exception e) {
                Debug.print(TAG, "updateActivity",  "total ex: " + selCur + "_" + e, 3);
            }

        }
        totalProfit.setText(profit.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        totalHoldValue.setText(hold.setScale(2,BigDecimal.ROUND_HALF_UP).toString());
        totalTotal.setText(total.setScale(2, BigDecimal.ROUND_HALF_UP).toString());


        selectedCurrencyArrayAdapter.notifyDataSetChanged();

        saveSelectedCurrenciesFile();
        saveCurrenciesFile();
    }
}
