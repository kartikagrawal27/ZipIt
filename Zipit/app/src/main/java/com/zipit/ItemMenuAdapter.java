package com.zipit;

import android.app.Application;
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
import java.util.List;

/**
 * Created by Pegasus on 11/14/16.
 */
public class ItemMenuAdapter extends ArrayAdapter {
    List<String> menu_items = new ArrayList<>();
    Context thisContext;
    Application newContext;
    ArrayList<ItemInfo> myItems = new ArrayList<>();
    ArrayList<ItemInfo> myCart = new ArrayList<>();


    /**
     * constructor
     * @param context
     * @param resource
     * @param objects
     * @param newApplicationContent
     * @param allItems
     */
    public ItemMenuAdapter(Context context, int resource, List<String> objects, Application newApplicationContent, ArrayList<ItemInfo> allItems) {
        super(context, resource, objects);
        thisContext = context;
        newContext = newApplicationContent;
        menu_items = objects;
        myItems = allItems;
    }

    /**
     * Required function implemented for ListView Adapter
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_info_adapter_layout, parent, false);
        }

        if (menu_items.size() == 0) {
            return convertView;
        }

        TextView item_details = (TextView) convertView.findViewById(R.id.itemDetails);
        String item = menu_items.get(position);
        item_details.setText(item);

        ImageButton addItem = (ImageButton) convertView.findViewById(R.id.addItem);
        addItem.setTag(position);
        addItem.setOnClickListener(myButtonClickListener);
        return convertView;
    }


    /**
     * OnClickListener for the button
     */
    private View.OnClickListener myButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            notifyDataSetChanged();
            final int position = (Integer) v.getTag();
            String courseKey = menu_items.get(position);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setMessage("Are you sure you wanted to add " + courseKey + " to the cart?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            ItemInfo newItem = myItems.get(position);
                            ((Cart) Cart.getContext()).addItemToCart(newItem);

                            String salePrice = newItem.getPrice().substring(newItem.getPrice().indexOf("$") + 1, newItem.getPrice().length());

                            float finalPrice = Float.parseFloat(salePrice);

                            ((Cart) Cart.getContext()).updateTotal(finalPrice, 1); //1 is for subtraction

                            Snackbar snack = Snackbar.make(v, "Item added to cart!", Snackbar.LENGTH_LONG);
                            View view = snack.getView();
                            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                            tv.setTextColor(Color.rgb(206, 201, 73));
                            snack.show();
                            myCart = ((Cart) Cart.getContext()).getCart();
//                            for (ItemInfo myItem : myCart) {
//
//                                System.out.println(myItem);
//                            }
                            arg0.cancel();
//                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
}
