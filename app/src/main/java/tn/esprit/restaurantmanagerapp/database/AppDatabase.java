package tn.esprit.restaurantmanagerapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import tn.esprit.restaurantmanagerapp.dao.CustomerDao;
import tn.esprit.restaurantmanagerapp.dao.DeliveryDao;
import tn.esprit.restaurantmanagerapp.dao.DeliveryPersonDao;
import tn.esprit.restaurantmanagerapp.dao.MenuDao;
import tn.esprit.restaurantmanagerapp.dao.OrderDao;
import tn.esprit.restaurantmanagerapp.dao.ReservationDao;
import tn.esprit.restaurantmanagerapp.entity.Customer;
import tn.esprit.restaurantmanagerapp.entity.Delivery;
import tn.esprit.restaurantmanagerapp.entity.DeliveryPerson;
import tn.esprit.restaurantmanagerapp.entity.MenuItem;
import tn.esprit.restaurantmanagerapp.entity.Order;
import tn.esprit.restaurantmanagerapp.entity.Reservation;

@Database(entities = {Order.class, MenuItem.class, Customer.class, Reservation.class, Delivery.class , DeliveryPerson.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // Abstract methods for DAOs
    public abstract OrderDao orderDao();
    public abstract MenuDao menuDao();
    public abstract CustomerDao customerDao();
    public abstract ReservationDao reservationDao();
    public abstract DeliveryPersonDao DeliveryPersonDao();
    public abstract DeliveryDao DeliveryDao();
    // Singleton instance
    private static volatile AppDatabase instance;

    // Get instance method with synchronized block for thread-safety
    public static AppDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "restaurant_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }
}
