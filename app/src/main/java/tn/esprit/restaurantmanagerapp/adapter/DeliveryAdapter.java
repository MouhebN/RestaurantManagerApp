package tn.esprit.restaurantmanagerapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tn.esprit.restaurantmanagerapp.R;
import tn.esprit.restaurantmanagerapp.entity.Delivery;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder> {

    private final Context context;
    private final List<Delivery> deliveryList;

    public DeliveryAdapter(Context context, List<Delivery> deliveryList) {
        this.context = context;
        this.deliveryList = deliveryList;
    }

    @Override
    public DeliveryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_delivery, parent, false);
        return new DeliveryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DeliveryViewHolder holder, int position) {
        Delivery delivery = deliveryList.get(position);
        holder.orderIdTextView.setText(String.valueOf(delivery.getOrderId()));
        holder.customerIdTextView.setText(String.valueOf(delivery.getCustomerId()));
        holder.addressTextView.setText(delivery.getAddress());
        holder.statusTextView.setText(delivery.getDeliveryStatus());
    }

    @Override
    public int getItemCount() {
        return deliveryList.size();
    }

    public void updateData(List<Delivery> newDeliveryList) {
        deliveryList.clear();
        deliveryList.addAll(newDeliveryList);
        notifyDataSetChanged();
    }

    public static class DeliveryViewHolder extends RecyclerView.ViewHolder {

        TextView orderIdTextView;
        TextView customerIdTextView;
        TextView addressTextView;
        TextView statusTextView;

        public DeliveryViewHolder(View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.textViewOrderId);
            customerIdTextView = itemView.findViewById(R.id.textViewCustomerId);
            addressTextView = itemView.findViewById(R.id.textViewAddress);
            statusTextView = itemView.findViewById(R.id.textViewDeliveryStatus);
        }
    }
}
