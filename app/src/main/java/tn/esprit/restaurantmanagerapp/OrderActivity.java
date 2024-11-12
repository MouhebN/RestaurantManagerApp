package tn.esprit.restaurantmanagerapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tn.esprit.restaurantmanagerapp.adapter.OrderAdapter;
import tn.esprit.restaurantmanagerapp.entity.Order;
import tn.esprit.restaurantmanagerapp.repository.OrderRepository;

public class OrderActivity extends AppCompatActivity {

    private EditText editTextOrderType, editTextOrderStatus, editTextTotalPrice, editTextCustomerId;
    private Button buttonAddUpdateOrder;
    private RecyclerView recyclerViewOrders;
    private OrderAdapter orderAdapter;
    private List<Order> orderList = new ArrayList<>();
    private OrderRepository orderRepository;
    private boolean isUpdating = false;
    private Order orderToUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // Initialize views
        editTextOrderType = findViewById(R.id.editTextOrderType);
        editTextOrderStatus = findViewById(R.id.editTextOrderStatus);
        editTextTotalPrice = findViewById(R.id.editTextTotalPrice);
        editTextCustomerId = findViewById(R.id.editTextCustomerId);
        buttonAddUpdateOrder = findViewById(R.id.buttonAddUpdateOrder);
        recyclerViewOrders = findViewById(R.id.recyclerViewOrders);

        // Initialize repository and load orders asynchronously
        orderRepository = new OrderRepository(getApplicationContext());
        loadOrders();

        // Set up RecyclerView
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));
        orderAdapter = new OrderAdapter(orderList, orderRepository, this);
        recyclerViewOrders.setAdapter(orderAdapter);

        // Set button click listener for adding/updating orders
        buttonAddUpdateOrder.setOnClickListener(v -> {
            if (isUpdating) {
                updateOrder();
            } else {
                addOrder();
            }
        });
    }

    private void loadOrders() {
        // Load orders asynchronously using the callback
        orderRepository.getAllOrders(new OrderRepository.OrdersCallback() {
            @Override
            public void onOrdersLoaded(List<Order> orders) {
                // Run on the main thread to update UI
                runOnUiThread(() -> {
                    orderList.clear();
                    orderList.addAll(orders);
                    orderAdapter.notifyDataSetChanged();
                });
            }
        });
    }

    private void addOrder() {
        String orderType = editTextOrderType.getText().toString();
        String orderStatus = editTextOrderStatus.getText().toString();
        String totalPriceText = editTextTotalPrice.getText().toString();
        String customerIdText = editTextCustomerId.getText().toString();

        if (TextUtils.isEmpty(orderType) || TextUtils.isEmpty(orderStatus) ||
                TextUtils.isEmpty(totalPriceText) || TextUtils.isEmpty(customerIdText)) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double totalPrice = Double.parseDouble(totalPriceText);
        int customerId = Integer.parseInt(customerIdText);

        Order newOrder = new Order(customerId, orderStatus, orderType, totalPrice);
        orderRepository.insertOrder(newOrder);

        orderList.add(newOrder);
        orderAdapter.notifyItemInserted(orderList.size() - 1);
        clearFields();
    }

    private void updateOrder() {
        String orderType = editTextOrderType.getText().toString();
        String orderStatus = editTextOrderStatus.getText().toString();
        String totalPriceText = editTextTotalPrice.getText().toString();
        String customerIdText = editTextCustomerId.getText().toString();

        if (TextUtils.isEmpty(orderType) || TextUtils.isEmpty(orderStatus) ||
                TextUtils.isEmpty(totalPriceText) || TextUtils.isEmpty(customerIdText)) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double totalPrice = Double.parseDouble(totalPriceText);
        int customerId = Integer.parseInt(customerIdText);

        orderToUpdate.setOrderType(orderType);
        orderToUpdate.setOrderStatus(orderStatus);
        orderToUpdate.setTotalPrice(totalPrice);
        orderToUpdate.setCustomerId(customerId);

        orderRepository.updateOrder(orderToUpdate);
        int position = orderList.indexOf(orderToUpdate);
        orderAdapter.notifyItemChanged(position);

        isUpdating = false;
        orderToUpdate = null;
        buttonAddUpdateOrder.setText("Add Order");
        clearFields();
    }

    public void editOrder(Order order) {
        editTextOrderType.setText(order.getOrderType());
        editTextOrderStatus.setText(order.getOrderStatus());
        editTextTotalPrice.setText(String.valueOf(order.getTotalPrice()));
        editTextCustomerId.setText(String.valueOf(order.getCustomerId()));

        isUpdating = true;
        orderToUpdate = order;
        buttonAddUpdateOrder.setText("Update Order");
    }

    private void clearFields() {
        editTextOrderType.setText("");
        editTextOrderStatus.setText("");
        editTextTotalPrice.setText("");
        editTextCustomerId.setText("");
    }
}
