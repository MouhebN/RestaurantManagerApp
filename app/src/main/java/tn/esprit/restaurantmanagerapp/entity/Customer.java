package tn.esprit.restaurantmanagerapp.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "customers")
public class Customer {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "customer_id")
    private int customerId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "phone_number")
    private String phoneNumber = null;

    @ColumnInfo(name = "address")
    private String address = null;

    @ColumnInfo(name = "date_of_birth")
    private String dateOfBirth = null;

    @ColumnInfo(name = "password")
    private String password;

    // New fields
    @ColumnInfo(name = "profile_image_uri")
    private String profileImageUri = null;

    @ColumnInfo(name = "location")
    private String location = null;

    // Default constructor
    public Customer() {}

    // Full parameterized constructor
    @Ignore
    public Customer(String name, String phoneNumber, String email, String address, String dateOfBirth, String password, String profileImageUri, String location) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.password = password;
        this.profileImageUri = profileImageUri;
        this.location = location;
    }

    // Constructor with essential fields
    @Ignore
    public Customer(String username, String email, String hashedPassword) {
        this.name = username;
        this.email = email;
        this.password = hashedPassword;
    }

    // Getters and Setters for all fields
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImageUri() {
        return profileImageUri;
    }

    public void setProfileImageUri(String profileImageUri) {
        this.profileImageUri = profileImageUri;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
