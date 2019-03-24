package fi.tuni.cryptobag;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends BaseActivity {
    private static final int REQUEST_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Debug.loadDebug(this);
        Debug.print(TAG, "onCreate()", "Logging my application", 1);
    }

    public void addTransaction(View view) {
        Intent intent = new Intent(this, TransactionActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Debug.print(TAG, "onActivityResult()",
                "requestCode: " + requestCode
                + ", resultCode: " + resultCode
                + ", data: " + data, 1);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                String buyCurrency = bundle.getString("buyCurrency");
                Double buyAmount = bundle.getDouble("buyAmount");
                Debug.print(TAG,"onActivityResult","buy: " + buyCurrency + " - " + buyAmount, 3);
                Transaction transaction = new Transaction(buyCurrency, buyAmount);
            }
        }
    }
}
