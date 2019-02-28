package com.zipit;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Pegasus on 11/15/16.
 */

/**
 * Cart class that maintains cart items across application
 */
public class Cart extends Application {

    public void addItemToCart(ItemInfo newItem) {
        cart.add(newItem);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Cart.context = getApplicationContext();
        total = (float) 0.0;
    }


    public void voidDeleteItem(ItemInfo item) {
        cart.remove(item);
    }

    public ArrayList<ItemInfo> getCart() {
        return cart;
    }

    public static Context getContext() {
        return Cart.context;
    }

    private static Context context;
    private ArrayList<ItemInfo> cart = new ArrayList<>();
    private float total;

    public void updateTotal(float newPrice, int operation) {
        if (operation == 0)
            total = total - newPrice;
        else
            total = total + newPrice;

    }

    public float getTotal() {
        return total;
    }
}
