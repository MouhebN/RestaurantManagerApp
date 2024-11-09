package tn.esprit.restaurantmanagerapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import tn.esprit.restaurantmanagerapp.dao.CustomerDao;
import tn.esprit.restaurantmanagerapp.dao.DeliveryDao;
import tn.esprit.restaurantmanagerapp.dao.MenuDao;
import tn.esprit.restaurantmanagerapp.dao.OrderDao;
import tn.esprit.restaurantmanagerapp.dao.ReservationDao;
import tn.esprit.restaurantmanagerapp.entity.Customer;
import tn.esprit.restaurantmanagerapp.entity.Delivery;
import tn.esprit.restaurantmanagerapp.entity.MenuItem;
import tn.esprit.restaurantmanagerapp.entity.Order;
import tn.esprit.restaurantmanagerapp.entity.Reservation;

@Database(entities = {Order.class, MenuItem.class, Customer.class, Reservation.class, Delivery.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract OrderDao orderDao();
    public abstract MenuDao menuDao();
    public abstract CustomerDao customerDao();
    public abstract ReservationDao reservationDao();
    public abstract DeliveryDao deliveryDao();


    private static AppDatabase instance;

    // Synchronized method to get the singleton instance
    public static synchronized AppDatabase getAppDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "restaurant_database")
                    .build();
        }
        return instance;
    }

}
