package com.example.toytrader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Calendar;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    ArrayList<Toy> s;
    private Context context;

    public CartAdapter(ArrayList<Toy> temp, Context context) {
        s = temp;
        this.context = context;
    }


    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartrow, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(CartAdapter.ViewHolder holder, int position) {
        Toy toy = s.get(position);
        holder.t = toy;
        holder.name.setText(toy.getName());
        holder.price.setText("Cost :"+toy.getCost().toString());
        holder.location.setText(toy.getLocation());
        Double rentalValue = (toy.getCost() * 0.65);
        holder.rent.setText("On Rent: "+rentalValue.toString() + "/Day");
    }

    @Override
    public int getItemCount() {
        return s.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, FirebaseListener {

        public TextView name;
        public TextView price;
        public TextView location;
        public TextView rent;
        public Button removeButton;
        public Button buyButton;
        public Button rentButton;
        public Context context;
        public Toy t;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            name = itemView.findViewById(R.id.cart_toyname);
            price = itemView.findViewById(R.id.cart_toyprice);
            rent = itemView.findViewById(R.id.cart_toy_rental_price);
            location = itemView.findViewById(R.id.cart_toylocation);
            removeButton = itemView.findViewById(R.id.aboutbutton);
            buyButton = itemView.findViewById(R.id.cart_checkout);
            rentButton = itemView.findViewById(R.id.cart_rent);
            this.context = context;
            final Context c = context;
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeButtonAction();
                }
            });

            buyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkoutButtonAction("Buy");
                }
            });

            rentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkoutButtonAction("Rent");
                }
            });
        }

        @Override
        public void onClick(View view) {

        }

        public void removeButtonAction(){
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
            SharedPreferences.Editor editor = preferences.edit();
            Set stringSet = preferences.getStringSet("toys", new HashSet<String>());
            stringSet = Utilities.getToysWithout(stringSet, t.getToyID());
            editor.putStringSet("toys", stringSet);
            editor.apply();
            editor.commit();
            Intent intent;
            intent = new Intent(this.context, CartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.context.startActivity(intent);
        }

        public void checkoutButtonAction(String saleType){
            Map m = new HashMap();
            FirebaseHelper f = FirebaseHelper.getInstance();

            m.put("userid", f.getFirebaseUser().getUid());
            m.put("type_of_sale", saleType);
            m.put("toyid", t.getToyID());
            m.put("start_date", Calendar.getInstance().getTime().toString());
            m.put("ownerid", t.getUserID());
            m.put("cost", t.getCost());
            m.put("toyname", t.getName());
            m.put("end_date", "");
            f.tradeToyWithDetails(m, this);
        }

        @Override
        public <T> void getFBData(T event) {
            if(event instanceof Boolean) {
                Intent intent = new Intent(this.context, MyOrders.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.context.startActivity(intent);
            }else if(event instanceof Exception) {
                SnackbarHelper.showMessage(((Exception)event).getLocalizedMessage(), this.itemView);
            }
        }

        @Override
        public <T> void updateFBResult(T event) {

        }
    }


}
