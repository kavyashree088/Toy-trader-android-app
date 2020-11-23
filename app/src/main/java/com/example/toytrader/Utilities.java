package com.example.toytrader;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Utilities {
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isValidPassword(CharSequence target) {
        return (!TextUtils.isEmpty(target) && target.length() > 6);
    }

    public static String getCategory(String category) {
        if(category.equalsIgnoreCase(Constants.SOFT_TOYS)) {
            return "Soft Toys";
        }else if(category.equalsIgnoreCase(Constants.ELECTRONICS)){
            return "Electronics";
        }else if(category.equalsIgnoreCase(Constants.DOLLS)){
            return "Dolls";
        }else if(category.equalsIgnoreCase(Constants.VEHICLE)){
            return "Vehicle";
        }else if(category.equalsIgnoreCase(Constants.PARTY_TOYS)){
            return "Party Toys";
        }else {
            return "";
        }
    }

    public static ArrayList<Toy> getToys(Set<String> s){
        Gson gson = new Gson();
        ArrayList <Toy> tlist = new ArrayList<>();
        for (String temp : s) {
            Toy t = gson.fromJson(temp, Toy.class);
            tlist.add(t);
        }
        return tlist;
    }

    public static Toy generateToyFromJSON(Map m, String toyID){
        Toy t = new Toy(toyID, (String) m.get("name"), (String) m.get("description"),
                (Double) m.get("cost"), (String) m.get("image"),
                (String) m.get("location"), (String) m.get("userid"), (String) m.get("category"));
        return t;
    }

    public static Order generateOrderFromJSON(Map m, String orderID){
        Order o = new Order();
        o.cost = (Double)m.get("cost");
        o.ownerID =  (String) m.get("ownerid");
        o.toyID = (String) m.get("toyid");
        o.userID = (String) m.get("userid");
        o.saleType = (String) m.get("type_of_sale");
        o.toyName = (String) m.get("toyname");
        o.endDate = (String) m.get("end_date");
        o.orderID = orderID;
        return o;
    }

    public static Set<String> getToysWithout(Set<String> s, String toyID ){
        Gson gson = new Gson();
        Set <String> tlist = new HashSet<>();
        for (String temp : s) {
            Toy t = gson.fromJson(temp, Toy.class);
            String json=gson.toJson(t);
            if(!t.getToyID().equalsIgnoreCase(toyID)){
                tlist.add(json);
            }
        }
        return tlist;
    }

    public static Boolean handleNavigationDrawerClick(MenuItem menuItem, Activity c, DrawerLayout drawer){
        switch (menuItem.getItemId()){
            case R.id.home:
                Intent home = new Intent(c, UserHomeActivity.class);
                c.startActivity(home);
                break;
            case R.id.add_toys:
                Intent intent;
                intent = new Intent(c, UploadToy.class);
                c.startActivity(intent);
                break;
            case R.id.nav_profile:
                Intent intent2 = new Intent(c, ProfileActivity.class);
                c.startActivity(intent2);
                break;

            case R.id.nav_cart:
                intent = new Intent(c, CartActivity.class);
                c.startActivity(intent);
                break;

            case R.id.nav_orders:
                intent = new Intent(c, MyOrders.class);
                c.startActivity(intent);
                break;

            case R.id.nav_logout:
                FirebaseHelper.getInstance().cleanUpForLogout();
                intent = new Intent(c, MainActivity.class);
                c.startActivity(intent);
                c.finish();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
