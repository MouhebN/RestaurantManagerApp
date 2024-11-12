package tn.esprit.restaurantmanagerapp.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import java.util.List;

import tn.esprit.restaurantmanagerapp.entity.Reservation;

@Dao
public interface ReservationDao {

    @Insert
    void insertReservation(Reservation reservation);

    @Query("SELECT * FROM reservations WHERE reservationId = :id")
    Reservation getReservationById(int id);

    @Query("SELECT * FROM reservations")
    List<Reservation> getAllReservations();

    @Update
    void updateReservation(Reservation reservation);

    @Delete
    void deleteReservation(Reservation reservation);
}
