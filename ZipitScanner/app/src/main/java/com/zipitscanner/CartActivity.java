package com.zipitscanner;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * CartActivity that shows the items that have been scanned
 */
public class CartActivity extends AppCompatActivity {

    ArrayList<String> cartItems = new ArrayList<>();
    ArrayAdapter<String> adapter = null;
    String total;

    /**
     * Cart's oncreate Function that gets executed once the activity is called
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this, ScanActivity.class);
                startActivity(intent);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }

        Intent beforeIntent = getIntent();
        String cartString = beforeIntent.getStringExtra("cartString");

        if(cartString.length()!=0){
            int start=0;
            for(int i=0;i<cartString.length();i++){
                if(cartString.charAt(i)=='$'){
                    String name = cartString.substring(start,i);
                    String price = cartString.substring(i, i+5);
                    cartItems.add(name);
                    start = i+6;
                    i = i+5;
                }
                if(i==cartString.length()-1){
                    total = cartString.substring(start, cartString.length());
                    break;
                }
            }
        }

        ListView cart = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, cartItems);
        cart.setAdapter(adapter);

        TextView totalDisplay = (TextView) findViewById(R.id.totalView);
        totalDisplay.setText("Total amount is $" + total);
    }

    /**
     * Handles the back button press
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CartActivity.this, ScanActivity.class);
        startActivity(intent);
    }
}
