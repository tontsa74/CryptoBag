package fi.tuni.cryptobag;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private static final int REQUEST_CODE_ADD = 10;
    private static final int REQUEST_CODE_EDIT = 11;

    List transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Debug.loadDebug(this);
        Debug.print(TAG, "onCreate()", "Logging my application", 1);

        transactions = new ArrayList();

        final ListView bagsListView = (ListView) findViewById(R.id.bagsListView);
        bagsListView.setOnItemClickListener((parent, view, position, id) -> {
            Transaction transaction = (Transaction) bagsListView.getItemAtPosition(position);
            editTransaction(transaction);
        });
        

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.bag_item, R.id.bagItemTextView, transactions);
        bagsListView.setAdapter(arrayAdapter);
    }

    public void editTransaction(Transaction transaction) {
        Debug.print(TAG, "editTransaction()", "edit transaction: " + transaction, 1);
        Intent intent = new Intent(this, TransactionActivity.class);
        intent.putExtra("transaction", transaction);
        startActivityForResult(intent, REQUEST_CODE_EDIT);
    }

    public void addTransaction(View view) {
        Debug.print(TAG, "addTransaction()", "add new transaction", 1);
        Intent intent = new Intent(this, TransactionActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADD);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Debug.print(TAG, "onActivityResult()",
                "requestCode: " + requestCode
                + ", resultCode: " + resultCode
                + ", data: " + data, 1);
        if (requestCode == REQUEST_CODE_ADD) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                String buyCurrency = bundle.getString("buyCurrency");
                Double buyAmount = bundle.getDouble("buyAmount");
                Debug.print(TAG,"onActivityResult","buy: " + buyCurrency + " - " + buyAmount, 3);
                Transaction transaction = new Transaction(buyCurrency, buyAmount);
                transactions.add(transaction);
            }
        }
        if (requestCode == REQUEST_CODE_EDIT) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                String buyCurrency = bundle.getString("buyCurrency");
                Double buyAmount = bundle.getDouble("buyAmount");
                Debug.print(TAG,"onActivityResult","buy: " + buyCurrency + " - " + buyAmount, 3);
                //TODO handle edited transaction
                //Transaction transaction = new Transaction(buyCurrency, buyAmount);
                //transactions.add(transaction);
            }
        }
    }
}
