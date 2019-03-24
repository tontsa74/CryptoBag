package fi.tuni.cryptobag;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class TransactionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        Debug.print(TAG, "onCreate()", "TransactionActivity", 1);
    }

    @Override
    public void onBackPressed() {
        Debug.print(TAG, "onBackPressed()", "TransactionActivity", 1);

        EditText buyAmount = (EditText) findViewById(R.id.buyAmountEditText);
        Debug.print(TAG, "onBackPressed()", "buyAmount: " + buyAmount.getText(), 1);

        super.onBackPressed();
    }
}
