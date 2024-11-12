package tn.esprit.restaurantmanagerapp.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tn.esprit.restaurantmanagerapp.R;
import tn.esprit.restaurantmanagerapp.entity.Delivery;
import tn.esprit.restaurantmanagerapp.repository.DeliveryRepository;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder> {
    private final Context context;
    private final List<Delivery> deliveryList;
    private DeliveryRepository deliveryRepository;

    public DeliveryAdapter(Context context, List<Delivery> deliveryList) {
        this.context = context;
        this.deliveryList = deliveryList;
    }

    public DeliveryAdapter(Context context, List<Delivery> deliveryList, DeliveryRepository deliveryRepository) {
        this.context = context;
        this.deliveryList = deliveryList;
        this.deliveryRepository = deliveryRepository;
    }

    // Ajouter une livraison
    public void addDelivery(Delivery delivery) {
        deliveryList.add(delivery);
        notifyItemInserted(deliveryList.size() - 1);
    }

    // Mettre à jour la liste des livraisons
    public void updateData(List<Delivery> newDeliveryList) {
        deliveryList.clear();
        deliveryList.addAll(newDeliveryList);
        notifyDataSetChanged();
    }

    // Supprimer une livraison
    public void removeDelivery(int position) {
        if (position >= 0 && position < deliveryList.size()) {
            Delivery deliveryToRemove = deliveryList.get(position);

            if (deliveryRepository != null) {
                deliveryRepository.deleteDelivery(deliveryToRemove);
                deliveryList.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(context, "Livraison supprimée", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Erreur : repository non initialisé", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Erreur : position invalide", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public DeliveryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_delivery_new, parent, false);
        return new DeliveryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryViewHolder holder, int position) {
        Delivery delivery = deliveryList.get(position);
        holder.orderIdTextView.setText("Order ID: " + delivery.getOrderId());
        holder.customerIdTextView.setText("Customer ID: " + delivery.getCustomerId());
        holder.addressTextView.setText("Address: " + delivery.getAddress());
        holder.statusTextView.setText("Status: " + delivery.getDeliveryStatus());

        // Clic sur un élément pour afficher des détails
        holder.itemView.setOnClickListener(v ->
                Toast.makeText(context, "Détails : " + delivery.toString(), Toast.LENGTH_SHORT).show()
        );

        // Long clic pour supprimer l'élément
        holder.itemView.setOnLongClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                removeDelivery(currentPosition);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return deliveryList.size();
    }

    public static class DeliveryViewHolder extends RecyclerView.ViewHolder {

        ImageView ivDeliveryImage;
        TextView orderIdTextView;
        TextView customerIdTextView;
        TextView addressTextView;
        TextView statusTextView;

        public DeliveryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivDeliveryImage = itemView.findViewById(R.id.ivDeliveryImage);
            orderIdTextView = itemView.findViewById(R.id.tvOrderId);
            customerIdTextView = itemView.findViewById(R.id.tvCustomerId);
            addressTextView = itemView.findViewById(R.id.tvAddress);
            statusTextView = itemView.findViewById(R.id.tvStatus);
        }
    }
}