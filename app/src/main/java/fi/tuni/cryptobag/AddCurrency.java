package fi.tuni.cryptobag;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddCurrency extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_currency);
        Debug.print(TAG, "onCreate()", "AddCurrencyActivity", 1);
    }
}
