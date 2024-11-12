package tn.esprit.restaurantmanagerapp.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "delivery_persons")
public class DeliveryPerson {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "delivery_person_id")
    private int deliveryPersonId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "phone_number")
    private String phoneNumber;

    @ColumnInfo(name = "car_info")
    private String carInfo;

    @ColumnInfo(name = "rating")
    private float rating;

    @ColumnInfo(name = "profile_image_uri")
    private String profileImageUri;

    // Default constructor required by Room
    public DeliveryPerson() {}

    // Full parameterized constructor
    @Ignore
    public DeliveryPerson(String name, String email, String phoneNumber, String carInfo, float rating) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.carInfo = carInfo;
        this.rating = rating;
    }

    // Getters and Setters
    public int getDeliveryPersonId() {
        return deliveryPersonId;
    }

    public void setDeliveryPersonId(int deliveryPersonId) {
        this.deliveryPersonId = deliveryPersonId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(String carInfo) {
        this.carInfo = carInfo;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getProfileImageUri() {
        return profileImageUri;
    }

    public void setProfileImageUri(String profileImageUri) {
        this.profileImageUri = profileImageUri;
    }
}
