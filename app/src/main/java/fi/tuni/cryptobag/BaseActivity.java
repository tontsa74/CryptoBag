package fi.tuni.cryptobag;

import android.support.v7.app.AppCompatActivity;

import java.util.List;

public class BaseActivity extends AppCompatActivity {
    protected final String TAG = "tsilve." + this.getClass().getName();

    List<Currency> currencies;
    List<Bag> bags;
}
