package tn.esprit.restaurantmanagerapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tn.esprit.restaurantmanagerapp.R;
import tn.esprit.restaurantmanagerapp.entity.Order;
import tn.esprit.restaurantmanagerapp.repository.OrderRepository;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orders;
    private OrderRepository orderRepository;
    private Context context;

    public OrderAdapter(List<Order> orders, OrderRepository orderRepository, Context context) {
        this.orders = orders;
        this.orderRepository = orderRepository;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.textViewOrderId.setText("Order ID: " + order.getOrderId());
        holder.textViewCustomerId.setText("Customer ID: " + order.getCustomerId());
        holder.textViewOrderType.setText("Type: " + order.getOrderType());
        holder.textViewOrderStatus.setText("Status: " + order.getOrderStatus());
        holder.textViewTotalPrice.setText(String.format("Price: $%.2f", order.getTotalPrice()));

        // Delete functionality
        holder.buttonDelete.setOnClickListener(v -> {
            orderRepository.deleteOrder(order);
            orders.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, orders.size());
        });

        // Update functionality
        holder.buttonUpdate.setOnClickListener(v -> showUpdateDialog(order, position));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    private void showUpdateDialog(Order order, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_update_order, null);

        EditText editTextOrderType = dialogView.findViewById(R.id.editTextOrderType);
        EditText editTextOrderStatus = dialogView.findViewById(R.id.editTextOrderStatus);
        EditText editTextTotalPrice = dialogView.findViewById(R.id.editTextTotalPrice);

        editTextOrderType.setText(order.getOrderType());
        editTextOrderStatus.setText(order.getOrderStatus());
        editTextTotalPrice.setText(String.valueOf(order.getTotalPrice()));

        builder.setView(dialogView)
                .setTitle("Update Order")
                .setPositiveButton("Update", (dialog, which) -> {
                    order.setOrderType(editTextOrderType.getText().toString());
                    order.setOrderStatus(editTextOrderStatus.getText().toString());
                    order.setTotalPrice(Double.parseDouble(editTextTotalPrice.getText().toString()));

                    orderRepository.updateOrder(order);
                    notifyItemChanged(position);
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView textViewOrderId, textViewCustomerId, textViewOrderType, textViewOrderStatus, textViewTotalPrice;
        ImageButton buttonDelete, buttonUpdate;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewOrderId = itemView.findViewById(R.id.textViewOrderId);
            textViewCustomerId = itemView.findViewById(R.id.textViewCustomerId);
            textViewOrderType = itemView.findViewById(R.id.textViewOrderType);
            textViewOrderStatus = itemView.findViewById(R.id.textViewOrderStatus);
            textViewTotalPrice = itemView.findViewById(R.id.textViewTotalPrice);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonUpdate = itemView.findViewById(R.id.buttonUpdate);
        }
    }
}
