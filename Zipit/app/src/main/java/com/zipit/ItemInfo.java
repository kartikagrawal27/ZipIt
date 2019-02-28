package com.zipit;

/**
 * Created by Pegasus on 11/10/16.
 */

/**
 * Data structure for the Extraction of data from database
 */
public class ItemInfo {

    private String name;
    private String price;


    public ItemInfo() {
    }

    public String getName() {
        return name;
    }

    public ItemInfo(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public String getPrice() {
        return price;

    }
}
