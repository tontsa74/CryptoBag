package fi.tuni.cryptobag;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

        super.onBackPressed();
    }

    public void saveTransaction(View view) {
        Debug.print(TAG, "saveTransaction()", "save", 1);

        EditText buyCurrencyEditText = (EditText) findViewById(R.id.buyCurrencyEditText);
        EditText buyAmountEditText = (EditText) findViewById(R.id.buyAmountEditText);

        Intent intent = new Intent();
        intent.putExtra("buyCurrency", buyCurrencyEditText.getText().toString());
        intent.putExtra("buyAmount", Double.parseDouble(buyAmountEditText.getText().toString()));
        setResult(RESULT_OK, intent);

        onBackPressed();
    }
}
