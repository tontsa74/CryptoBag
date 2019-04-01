package fi.tuni.cryptobag;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private static final int REQUEST_CODE_ADD_BAG = 10;
    private static final int REQUEST_CODE_EDIT = 11;

    ArrayAdapter bagArrayAdapter;
    List<Bag> bags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Debug.loadDebug(this);
        Debug.print(TAG, "onCreate()", "Logging my application", 1);

        bags = new ArrayList<Bag>();
        Currency buy = new Currency("nem", "NEM", "xem");
        bags.add(new Bag(buy, new BigDecimal("1000"), new BigDecimal("0.1"), new BigDecimal("0.001"), new BigDecimal("0.01")));

        final ListView bagsListView = (ListView) findViewById(R.id.bagsListView);
        bagsListView.setOnItemClickListener((parent, view, position, id) -> {
            Debug.print(TAG, "setOnItemClickListener",  "position_id: " + position + "_" + id, 2);
            Bag bag = (Bag) bagsListView.getItemAtPosition(position);
            editBag(bag, position);
        });
        

        bagArrayAdapter = new ArrayAdapter(this, R.layout.bag_item, R.id.bagItemTextView, bags);
        bagsListView.setAdapter(bagArrayAdapter);
    }

    public void editBag(Bag bag, int position) {
        Debug.print(TAG, "editBag()", "edit bag: " + bag + ", pos: " + position, 1);
        Intent intent = new Intent(this, BagActivity.class);
        intent.putExtra("bag", bag);
        intent.putExtra("position", position);
        startActivityForResult(intent, REQUEST_CODE_EDIT);
    }

    public void addBag(View view) {
        Debug.print(TAG, "addBag()", "add new bag", 1);
        Intent intent = new Intent(this, BagActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADD_BAG);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Debug.print(TAG, "onActivityResult()",
                "requestCode: " + requestCode
                + ", resultCode: " + resultCode
                + ", data: " + data, 1);
        if (requestCode == REQUEST_CODE_ADD_BAG) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                Debug.print(TAG,"onActivityResult","buy: " + bundle.get("bag"), 3);
                bags.add((Bag) bundle.get("bag"));
            }
        }
        if (requestCode == REQUEST_CODE_EDIT) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                Debug.print(TAG,"onActivityResult","buy: " + bundle.get("bag") + bundle.get("position"), 3);
                Bag bag = (Bag) bundle.get("bag");
                int position = bundle.getInt("position");
                bags.set(position, bag);
            }
        }
        Debug.print(TAG,"onActivityResult","bagArrayAdapter.notifyDataSetChanged", 1);
        bagArrayAdapter.notifyDataSetChanged();
    }
}
