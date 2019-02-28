package com.zipit;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The Activity that display that food items that can from there then be added to the cart
 */
public class ItemMenuActivity extends AppCompatActivity {

    SharedPreferences loginPreferences;
    ArrayAdapter<String> adapter = null;
    List<String> mcDonaldsItems = new ArrayList<>();
    static ArrayList<ItemInfo> allItems = new ArrayList<>();

    /**
     * Creates and sets the UI and database initializations for the activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left);
            }
        });

        Firebase.setAndroidContext(this);
        loginPreferences = this.getSharedPreferences("Login", 0);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.item_menu_button);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color
                .parseColor("#53AFA9")));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartActivityIntent = new Intent(ItemMenuActivity.this, CartActivity.class);
                startActivity(cartActivityIntent);
            }
        });

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.argb(255, 74, 162, 153)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }


        Intent beforeIntent = getIntent();
        int position = beforeIntent.getIntExtra("position", 0);
        final String collegeCode = beforeIntent.getStringExtra("selectedCategory");
        final Firebase items;
        Query queryItems = null;
        switch (position) {
            case 0:
                items = new Firebase("https://zip-it-eb9f0.firebaseio.com/restaurants/mcdonalds/breakfast");
                queryItems = items.orderByKey();
                break;
            case 1:
                items = new Firebase("https://zip-it-eb9f0.firebaseio.com/restaurants/mcdonalds/burgers");
                queryItems = items.orderByKey();
                break;
            case 2:
                items = new Firebase("https://zip-it-eb9f0.firebaseio.com/restaurants/mcdonalds/chicken&sandwiches");
                queryItems = items.orderByKey();
                break;
            case 3:
                items = new Firebase("https://zip-it-eb9f0.firebaseio.com/restaurants/mcdonalds/drinks&shakes");
                queryItems = items.orderByKey();
                break;
            case 4:
                items = new Firebase("https://zip-it-eb9f0.firebaseio.com/restaurants/mcdonalds/sides");
                queryItems = items.orderByKey();
                break;
        }


        queryItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allItems.clear();
                mcDonaldsItems.clear();
                Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                while (it.hasNext()) {
                    DataSnapshot itemId = (DataSnapshot) it.next();
                    ItemInfo food = itemId.getValue(ItemInfo.class);
                    allItems.add(food);
                    mcDonaldsItems.add(food.getName() + " (" + food.getPrice() + ")");
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        adapter = null;
        ListView foods = (ListView) findViewById(R.id.foodItems);
        adapter = new ItemMenuAdapter(this, R.layout.item_info_adapter_layout, mcDonaldsItems, this.getApplication(), allItems);
        foods.setAdapter(adapter);
    }

    /**
     * Inflates the menu
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    /**
     * Goes through the menu options to creates a dialog box and then ultimately sign out the user
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_button:
//                showDialog();
//
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Are you sure you wanted to Sign out?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                FirebaseAuth.getInstance().signOut();
                                SharedPreferences.Editor editor = loginPreferences.edit();
                                editor.remove("email");
                                editor.remove("password");
                                editor.commit();
                                Intent loginIntent = new Intent(ItemMenuActivity.this, LoginActivity.class);
                                startActivity(loginIntent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            }
                        });

                alertDialogBuilder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;

        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left);
    }

    public static ItemInfo addItemToCart(int position) {
        ItemInfo newItem = allItems.get(position);
        return newItem;
    }

}
