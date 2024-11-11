package tn.esprit.restaurantmanagerapp.repository;

import android.content.Context;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tn.esprit.restaurantmanagerapp.dao.ReservationDao;
import tn.esprit.restaurantmanagerapp.database.AppDatabase;
import tn.esprit.restaurantmanagerapp.entity.Reservation;

public class ReservationRepository {
    private final ReservationDao reservationDao;
    private final ExecutorService executorService;

    public ReservationRepository(Context context) {
        AppDatabase db = AppDatabase.getAppDatabase(context);
        reservationDao = db.reservationDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insertReservation(Reservation reservation) {
        executorService.execute(() -> reservationDao.insertReservation(reservation));
    }

    public void updateReservation(Reservation reservation) {
        executorService.execute(() -> reservationDao.updateReservation(reservation));
    }

    public void deleteReservation(Reservation reservation) {
        executorService.execute(() -> reservationDao.deleteReservation(reservation));
    }

    public void getAllReservations(ReservationsCallback callback) {
        executorService.execute(() -> {
            List<Reservation> reservations = reservationDao.getAllReservations();
            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                callback.onReservationsLoaded(reservations);
            });
        });
    }

    public void getReservationById(int id, ReservationCallback callback) {
        executorService.execute(() -> {
            Reservation reservation = reservationDao.getReservationById(id);
            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                callback.onReservationLoaded(reservation);
            });
        });
    }

    public interface ReservationsCallback {
        void onReservationsLoaded(List<Reservation> reservations);
    }

    public interface ReservationCallback {
        void onReservationLoaded(Reservation reservation);
    }
}
