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

    ArrayAdapter<Currency> selectedBagsArrayAdapter;

    TextView totalProfit, totalHoldValue, totalInvest, totalTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalProfit = (TextView) findViewById(R.id.totalProfit);
        totalHoldValue = (TextView) findViewById(R.id.totalHoldValue);
        totalInvest = (TextView) findViewById(R.id.totalInvest);
        totalTotal = (TextView) findViewById(R.id.totalTotal);

        Debug.loadDebug(this);
        Debug.print(TAG, "onCreate()", "Logging my application", 1);

        loadCurrenciesFile();
        loadSelectedFile();

        if (currencies == null) {
            currencies = new ArrayList<Currency>();
        }
        if (selectedBags == null) {
            selectedBags = new ArrayList<Bag>();
        }
        if (initCurrencies == null) {
            initCurrencies = new ArrayList<Currency>();
        }

        for (int i=0; i < currencies.size(); i++) {
            if(currencies.get(i).getIcon() != null) {

            } else {
                initCurrencies.add(currencies.get(i));
            }
            for (Bag bag : selectedBags) {
                Currency selCur = bag.getCurrency();

                if (selCur.getId().equals(currencies.get(i).getId())) {
                    currencies.set(i, selCur);
                }
            }
        }

        connectionToService = new ApiServiceConnection();


        final ListView selectedBagsListView = (ListView) findViewById(R.id.selectedBagsListView);

        selectedBagsListView.setOnItemClickListener((parent, view, position, id) -> {
            Debug.print(TAG, "setOnItemClickListener",  "position_id: " + position + "_" + id, 2);

            //Currency currency = (Currency) selectedBagsListView.getItemAtPosition(position);
            Bag bag = (Bag) selectedBagsListView.getItemAtPosition(position);

            //editCurrency(currency, position);
            editBag(bag, position);
        });

        selectedBagsListView.setOnItemLongClickListener((parent, view, position, id) -> {
            Debug.print(TAG, "setOnItemClickListener",  "position_id: " + position + "_" + id, 2);
            //Currency currency = (Currency) selectedBagsListView.getItemAtPosition(position);
            //deleteCurrency(currency, position);
            Bag bag = (Bag) selectedBagsListView.getItemAtPosition(position);
            deleteBag(bag, position);
            return true;
        });

        selectedBagsArrayAdapter = new ArrayAdapter(this, R.layout.currency_item, R.id.currencyItemTextView, selectedBags) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = inflater.inflate(R.layout.currency_item, parent, false);

                //Currency currency = (Currency) getItem(position);
                //Bag bag = currency.getBag();
                Bag bag = (Bag) getItem(position);
                Currency currency = bag.getCurrency();

                TextView currencyItemTextView = (TextView) rowView.findViewById(R.id.currencyItemTextView);
                TextView profitItemTextView = (TextView) rowView.findViewById(R.id.profitItemTextView);
                TextView holdValueTextView = (TextView) rowView.findViewById(R.id.holdValueTextView);
                TextView investTextView = (TextView) rowView.findViewById(R.id.investTextView);
                TextView totalTotalTextView = (TextView) rowView.findViewById(R.id.totalTotalTextView);

                ImageView imageView = (ImageView) rowView.findViewById(R.id.iconImageView);
                if(currency.getIcon() != null) {
                    imageView.setImageBitmap(currency.getBitmap());
                }
                currencyItemTextView.setText(currency.getName());


                try {
                    BigDecimal profit = bag.getProfit().setScale(0, RoundingMode.HALF_UP);
                    if(profit.doubleValue() > 0) {
                        profitItemTextView.setTextColor(Color.rgb(29, 196, 112));
                    } else if(profit.doubleValue() < 0) {
                        profitItemTextView.setTextColor(Color.RED);
                    }
                    profitItemTextView.setText(profit.toString());
                } catch (Exception e) {
                    Debug.print(TAG, "selectedBagsArrayAdapter", "profit e: " + e, 4);
                }


                try {
                    BigDecimal holdValue = bag.getHoldValue().setScale(0, RoundingMode.HALF_UP);
                    if (holdValue.doubleValue() > 0) {
                        holdValueTextView.setTextColor(Color.rgb(29, 196, 112));
                    } else if (holdValue.doubleValue() < 0) {
                        holdValueTextView.setTextColor(Color.RED);
                    }
                    holdValueTextView.setText(holdValue.toString());
                } catch (Exception e) {
                    Debug.print(TAG, "selectedBagsArrayAdapter", "estimated e: " + e, 4);
                }

                try {
                    BigDecimal invest = bag.getInvestedValue().setScale(0, RoundingMode.HALF_UP);
//                    if (invest.doubleValue() > 0) {
//                        investTextView.setTextColor(Color.rgb(29, 196, 112));
//                    } else if (invest.doubleValue() < 0) {
//                        investTextView.setTextColor(Color.RED);
//                    }
                    investTextView.setText(invest.toString());
                } catch (Exception e) {
                    Debug.print(TAG, "selectedBagsArrayAdapter", "invest e: " + e, 4);
                }

                try {
                    BigDecimal total = bag.getTotalValue().setScale(0, RoundingMode.HALF_UP);
                    if (total.doubleValue() > 0) {
                        totalTotalTextView.setTextColor(Color.rgb(29, 196, 112));
                    } else if (total.doubleValue() < 0) {
                        totalTotalTextView.setTextColor(Color.RED);
                    }
                    totalTotalTextView.setText(total.toString());
                } catch (Exception e) {
                    Debug.print(TAG, "selectedBagsArrayAdapter", "total e: " + e, 4);
                }

                // DEBUG
                TextView debug = (TextView) rowView.findViewById(R.id.mainDebug);
                debug.setText(currency.toString() + " -- " + bag.toString());
                // DEBUG

                return rowView;
            }
        };
        selectedBagsListView.setAdapter(selectedBagsArrayAdapter);

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
        saveSelectedFile();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

//    public void editCurrency(Currency currency, int position) {
//        Debug.print(TAG, "editCurrency()", "edit currency: " + currency + ", pos: " + position, 1);
//        Intent intent = new Intent(this, BagActivity.class);
//        intent.putExtra("position", position);
//        startActivity(intent);
//        apiService.fetch(currency, HIGH_PRIORITY);
//    }
    public void editBag(Bag bag, int position) {
        Debug.print(TAG, "editCurrency()", "edit currency: " + bag + ", pos: " + position, 1);
        Intent intent = new Intent(this, BagActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
        //apiService.fetch(currency, HIGH_PRIORITY);
        apiService.fetch(bag.getCurrency(), HIGH_PRIORITY);
    }

//    public void addCurrency(View view) {
//        Debug.print(TAG, "MainActivity()", "addCurrency", 1);
//        Intent intent = new Intent(this, BagActivity.class);
//        intent.putExtra("position", -1);
//        startActivityForResult(intent, REQUEST_CODE_ADD_BAG);
//        //apiService.fetch(selectedBags, MEDIUM_PRIORITY);
//    }

    public void addBag(View view) {
        Debug.print(TAG, "MainActivity()", "addBag", 1);
        Intent intent = new Intent(this, BagActivity.class);
        intent.putExtra("position", -1);
        startActivityForResult(intent, REQUEST_CODE_ADD_BAG);
        //apiService.fetch(selectedBags, MEDIUM_PRIORITY);
        fetchBags();
    }

//    public void deleteCurrency(Currency currency, int position) {
//        Debug.print(TAG, "deleteCurrency()", "edit currency: " + currency + ", pos: " + position, 1);
//
//        AlertDialog.Builder alert = new AlertDialog.Builder(
//                MainActivity.this);
//
//        alert.setTitle("Delete");
//        alert.setMessage("Do you want delete " + currency.getName());
//        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                selectedBags.remove(position);
//                selectedBagsArrayAdapter.notifyDataSetChanged();
//
//            }
//        });
//        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//        alert.show();
//
//    }
public void deleteBag(Bag bag, int position) {
    Debug.print(TAG, "deleteBag()", "edit bag: " + bag + ", pos: " + position, 1);

    AlertDialog.Builder alert = new AlertDialog.Builder(
            MainActivity.this);

    alert.setTitle("Delete");
    alert.setMessage("Do you want delete " + bag.getCurrency().getName());
    alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            selectedBags.remove(position);
            selectedBagsArrayAdapter.notifyDataSetChanged();

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
        BigDecimal invest = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;


        for (Bag bag : selectedBags) {
            //Bag bag = bag.getBag();
            try {
                profit = profit.add(bag.getProfit());
                hold = hold.add(bag.getHoldValue());
                invest = invest.add(bag.getInvestedValue());
                total = total.add(bag.getTotalValue());
            } catch(Exception e) {
                Debug.print(TAG, "updateActivity",  "total ex: " + bag + "_" + e, 3);
            }

        }
        totalProfit.setText(profit.setScale(0, BigDecimal.ROUND_HALF_UP).toString());
        totalHoldValue.setText(hold.setScale(0,BigDecimal.ROUND_HALF_UP).toString());
        totalInvest.setText(invest.setScale(0, BigDecimal.ROUND_HALF_UP).toString());
        totalTotal.setText(total.setScale(0, BigDecimal.ROUND_HALF_UP).toString());


        selectedBagsArrayAdapter.notifyDataSetChanged();
        fetchBags();

        saveSelectedFile();
        saveCurrenciesFile();
    }

    private void fetchBags() {
        fetchSelected = new ArrayList<>();
        for(Bag bag : selectedBags) {
            fetchSelected.add(bag.getCurrency());
        }
        //apiService.fetch(fetchSelected, MEDIUM_PRIORITY);
    }
}
