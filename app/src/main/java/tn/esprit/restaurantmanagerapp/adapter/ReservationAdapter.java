package tn.esprit.restaurantmanagerapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tn.esprit.restaurantmanagerapp.R;
import tn.esprit.restaurantmanagerapp.entity.Reservation;
import tn.esprit.restaurantmanagerapp.repository.ReservationRepository;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private List<Reservation> reservations;
    private ReservationRepository reservationRepository;
    private Context context;

    public ReservationAdapter(List<Reservation> reservations, ReservationRepository reservationRepository, Context context) {
        this.reservations = reservations;
        this.reservationRepository = reservationRepository;
        this.context = context;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_item_layout, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservations.get(position);
        holder.textViewCustomerId.setText(String.valueOf(reservation.getCustomerId()));
        holder.textViewReservationDate.setText(reservation.getReservationDate());
        holder.textViewReservationTime.setText(reservation.getReservationTime());
        holder.textViewPartySize.setText(String.valueOf(reservation.getPartySize()));

        // Delete button functionality
        holder.buttonDeleteReservation.setOnClickListener(v -> {
            if (position >= 0 && position < reservations.size()) {
                reservationRepository.deleteReservation(reservation);
                reservations.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, reservations.size());
                Toast.makeText(context, "Reservation deleted", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("ReservationAdapter", "Invalid position: " + position);
            }
        });

        // Update button functionality
        holder.buttonUpdateReservation.setOnClickListener(v -> showUpdateDialog(reservation, position));
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    private void showUpdateDialog(Reservation reservation, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_update_reservation, null);

        // Initialize dialog views
        EditText editTextCustomerId = dialogView.findViewById(R.id.editTextCustomerId);
        EditText editTextReservationDate = dialogView.findViewById(R.id.editTextReservationDate);
        EditText editTextReservationTime = dialogView.findViewById(R.id.editTextReservationTime);
        EditText editTextPartySize = dialogView.findViewById(R.id.editTextPartySize);

        // Pre-fill existing data
        editTextCustomerId.setText(String.valueOf(reservation.getCustomerId()));
        editTextReservationDate.setText(reservation.getReservationDate());
        editTextReservationTime.setText(reservation.getReservationTime());
        editTextPartySize.setText(String.valueOf(reservation.getPartySize()));

        builder.setView(dialogView)
                .setTitle("Update Reservation")
                .setPositiveButton("Update", (dialog, which) -> {
                    // Get updated values
                    try {
                        int updatedCustomerId = Integer.parseInt(editTextCustomerId.getText().toString());
                        String updatedDate = editTextReservationDate.getText().toString();
                        String updatedTime = editTextReservationTime.getText().toString();
                        int updatedPartySize = Integer.parseInt(editTextPartySize.getText().toString());

                        // Update the reservation
                        reservation.setCustomerId(updatedCustomerId);
                        reservation.setReservationDate(updatedDate);
                        reservation.setReservationTime(updatedTime);
                        reservation.setPartySize(updatedPartySize);

                        reservationRepository.updateReservation(reservation);
                        notifyItemChanged(position);
                        Toast.makeText(context, "Reservation updated", Toast.LENGTH_SHORT).show();

                    } catch (NumberFormatException e) {
                        Toast.makeText(context, "Invalid input format", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCustomerId, textViewReservationDate, textViewReservationTime, textViewPartySize;
        ImageButton buttonDeleteReservation, buttonUpdateReservation;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCustomerId = itemView.findViewById(R.id.textViewCustomerId);
            textViewReservationDate = itemView.findViewById(R.id.textViewReservationDate);
            textViewReservationTime = itemView.findViewById(R.id.textViewReservationTime);
            textViewPartySize = itemView.findViewById(R.id.textViewPartySize);
            buttonDeleteReservation = itemView.findViewById(R.id.buttonDeleteReservation);
            buttonUpdateReservation = itemView.findViewById(R.id.buttonUpdateReservation);
        }
    }
}
