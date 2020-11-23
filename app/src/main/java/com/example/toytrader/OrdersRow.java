package com.example.toytrader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class OrdersRow extends Fragment {

    public OrdersRow() {

    }

    public static OrdersRow newInstance(String param1, String param2) {
        OrdersRow fragment = new OrdersRow();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders_row, container, false);
    }
}

class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    ArrayList<Order> s;
    private Context context;

    public OrdersAdapter(ArrayList<Order> temp, Context context) {
        s = temp;
        this.context = context;
    }


    @Override
    public OrdersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_orders_row, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(OrdersAdapter.ViewHolder holder, int position) {
        Order order = s.get(position);
        holder.o = order;
        holder.name.setText(order.toyName);
        holder.price.setText(order.cost.toString());
        holder.typeOfSale.setText(order.saleType);
        if(order != null && !order.endDate.isEmpty()) {
            holder.returnButton.setText("Returned");
            holder.returnButton.setEnabled(false);
        }else {
            holder.returnButton.setText("Return");
            holder.returnButton.setEnabled(true);
        }
    }

    @Override
    public int getItemCount() {
        return s.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, FirebaseListener {

        public TextView name;
        public TextView price;
        public TextView typeOfSale;
        public Button returnButton;
        public Context context;
        public Order o;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            name = itemView.findViewById(R.id.orders_toyname);
            price = itemView.findViewById(R.id.orders_toyprice);
            typeOfSale = itemView.findViewById(R.id.orders_toylocation);
            returnButton = itemView.findViewById(R.id.orders_return);

            this.context = context;
            final Context c = context;
            returnButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    returnButtonAction();
                }
            });
        }

        @Override
        public void onClick(View view) {

        }

        public void returnButtonAction(){
            Map m = new HashMap();
            FirebaseHelper f = FirebaseHelper.getInstance();
            m.put("end_date", Calendar.getInstance().getTime().toString());
            f.returnToyWithID(o.orderID, m, this);
        }

        @Override
        public <T> void getFBData(T event) {
            if(event instanceof Boolean) {
                Intent intent = new Intent(this.context, UserHomeActivity.class);
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
