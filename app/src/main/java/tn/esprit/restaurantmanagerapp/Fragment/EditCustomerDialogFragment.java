package tn.esprit.restaurantmanagerapp.Fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import tn.esprit.restaurantmanagerapp.R;
import tn.esprit.restaurantmanagerapp.entity.Customer;

public class EditCustomerDialogFragment extends DialogFragment {

    private EditText nameEditText, locationEditText, emailEditText;
    private Customer customer;
    private OnCustomerUpdatedListener listener;

    public interface OnCustomerUpdatedListener {
        void onCustomerUpdated(Customer updatedCustomer);
    }

    public EditCustomerDialogFragment(Customer customer, OnCustomerUpdatedListener listener) {
        this.customer = customer;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_edit_profile_dialog, null);
        nameEditText = view.findViewById(R.id.edit_name);
        locationEditText = view.findViewById(R.id.edit_location);
        emailEditText = view.findViewById(R.id.edit_email);

        // Populate fields with existing data
        nameEditText.setText(customer.getName());
        locationEditText.setText(customer.getLocation());
        emailEditText.setText(customer.getEmail());

        return new AlertDialog.Builder(requireContext())
                .setTitle("Edit Customer")
                .setView(view)
                .setPositiveButton("Update", (dialog, which) -> updateCustomer())
                .setNegativeButton("Cancel", null)
                .create();
    }

    private void updateCustomer() {
        String name = nameEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), "Name and Email cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        customer.setName(name);
        customer.setLocation(location);
        customer.setEmail(email);

        if (listener != null) {
            listener.onCustomerUpdated(customer);
        }
        dismiss();
    }
}
