package com.zipit;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Pegasus on 11/15/16.
 */

/**
 * Adapter class for listview Adapter
 */
public class CartAdapter extends ArrayAdapter {

    ArrayList<ItemInfo> itemsInCart = new ArrayList<>();
    TextView myTotal;

    public CartAdapter(Context context, int resource, ArrayList<ItemInfo> objects, TextView myTotal) {
        super(context, resource, objects);
        this.myTotal = myTotal;
        itemsInCart = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cart_item_layout, parent, false);
        }

        if (itemsInCart.size() == 0) {

            TextView itemName = (TextView) convertView.findViewById(R.id.item_info);
            itemName.setText("Your cart is empty :(");
            return convertView;
        }


        ItemInfo myItem = itemsInCart.get(position);
        TextView itemName = (TextView) convertView.findViewById(R.id.item_info);
        itemName.setText(myItem.getName() + "(" + myItem.getPrice() + ")");

        ImageButton deleteItemButton = (ImageButton) convertView.findViewById(R.id.deleteItem);
        deleteItemButton.setTag(position);
        deleteItemButton.setOnClickListener(myButtonClickListener);

        return convertView;
    }

    private View.OnClickListener myButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final int position = (Integer) v.getTag();
            ItemInfo item = itemsInCart.get(position);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setMessage("Are you sure you wanted to delete " + item.getName() + " from the cart?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            ItemInfo newItem = itemsInCart.get(position);

                            ((Cart) Cart.getContext()).voidDeleteItem(newItem);
                            String salePrice = newItem.getPrice().substring(newItem.getPrice().indexOf("$") + 1, newItem.getPrice().length());

                            float finalPrice = Float.parseFloat(salePrice);

                            ((Cart) Cart.getContext()).updateTotal(finalPrice, 0); //0 is for subtraction

                            notifyDataSetChanged();
                            float total = ((Cart) Cart.getContext()).getTotal();
                            float roundOff = (float) (Math.round(total*100.0) /100.0);
                            myTotal.setText("Your total is $" + roundOff);

                            Snackbar snack = Snackbar.make(v, "Item deleted from cart!", Snackbar.LENGTH_LONG);
                            View view = snack.getView();
                            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                            tv.setTextColor(Color.rgb(206, 201, 73));
                            snack.show();

                            arg0.cancel();
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
        }
    };

    public void refreshEvents(ArrayList<ItemInfo> itemsInCart) {
        this.itemsInCart.clear();
        this.itemsInCart.addAll(itemsInCart);
        notifyDataSetChanged();
    }

}
