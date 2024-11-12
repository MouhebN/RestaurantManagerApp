package tn.esprit.restaurantmanagerapp.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "deliveries")
public class Delivery {

    @PrimaryKey(autoGenerate = true)
    private int deliveryId;

    @ColumnInfo(name = "order_id")
    private int orderId;

    @ColumnInfo(name = "customer_id")
    private int customerId;

    private String address;

    @ColumnInfo(name = "delivery_status")
    private String deliveryStatus;

    // No-arg constructor (Room required constructor)
    public Delivery() {
    }

    // Custom constructor with parameters (marked with @Ignore so Room doesn't use it)
    @Ignore
    public Delivery(int orderId, int customerId, String address, String deliveryStatus) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.address = address;
        this.deliveryStatus = deliveryStatus;
    }

    // Getters and Setters for all fields

    public int getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}
