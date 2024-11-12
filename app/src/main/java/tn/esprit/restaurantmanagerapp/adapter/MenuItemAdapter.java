package tn.esprit.restaurantmanagerapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import tn.esprit.restaurantmanagerapp.R;
import tn.esprit.restaurantmanagerapp.entity.MenuItem;
import tn.esprit.restaurantmanagerapp.repository.MenuItemRepository;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MenuItemViewHolder> {

    private List<MenuItem> menuItems;
    private MenuItemRepository menuItemRepository;
    private Context context;

    public MenuItemAdapter(List<MenuItem> menuItems, MenuItemRepository menuItemRepository, Context context) {
        this.menuItems = menuItems;
        this.menuItemRepository = menuItemRepository;
        this.context = context;
    }

    @NonNull
    @Override
    public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_layout, parent, false);
        return new MenuItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemViewHolder holder, int position) {
        MenuItem menuItem = menuItems.get(position);
        holder.textViewName.setText(menuItem.getName());
        holder.textViewDescription.setText(menuItem.getDescription());
        holder.textViewPrice.setText(String.format("$%.2f", menuItem.getPrice()));
        holder.textViewCategory.setText(menuItem.getCategory());

        // Delete button functionality
        holder.buttonDelete.setOnClickListener(v -> {
            if (position >= 0 && position < menuItems.size()) { // Check if the position is valid
                menuItemRepository.deleteMenuItem(menuItem); // First delete from the repository
                menuItems.remove(position); // Then remove from the list
                notifyItemRemoved(position); // Notify that an item was removed
                notifyItemRangeChanged(position, menuItems.size()); // Notify about the change in the range
            } else {
                Log.e("MenuItemAdapter", "Invalid position: " + position);
            }
        });

        // Update button functionality
        holder.buttonUpdate.setOnClickListener(v -> {
            showUpdateDialog(menuItem, position);
        });
    }


    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    private void showUpdateDialog(MenuItem menuItem, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_update_menu_item, null);

        // Initialize dialog views
        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        EditText editTextDescription = dialogView.findViewById(R.id.editTextDescription);
        EditText editTextPrice = dialogView.findViewById(R.id.editTextPrice);
        Spinner spinnerCategory = dialogView.findViewById(R.id.spinnerCategory);

        // Pre-fill existing data
        editTextName.setText(menuItem.getName());
        editTextDescription.setText(menuItem.getDescription());
        editTextPrice.setText(String.valueOf(menuItem.getPrice()));

        // Set up the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.category_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
        int spinnerPosition = adapter.getPosition(menuItem.getCategory());
        spinnerCategory.setSelection(spinnerPosition);

        // Set up the dialog
        builder.setView(dialogView)
                .setTitle("Update Menu Item")
                .setPositiveButton("Update", (dialog, which) -> {
                    // Get updated values
                    String updatedName = editTextName.getText().toString();
                    String updatedDescription = editTextDescription.getText().toString();
                    double updatedPrice = Double.parseDouble(editTextPrice.getText().toString());
                    String updatedCategory = spinnerCategory.getSelectedItem().toString();

                    // Update the menu item in the repository and adapter
                    menuItem.setName(updatedName);
                    menuItem.setDescription(updatedDescription);
                    menuItem.setPrice(updatedPrice);
                    menuItem.setCategory(updatedCategory);

                    menuItemRepository.updateMenuItem(menuItem);
                    notifyItemChanged(position);
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public static class MenuItemViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewDescription, textViewPrice, textViewCategory;
        ImageButton buttonDelete;
        ImageButton buttonUpdate;

        public MenuItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonUpdate = itemView.findViewById(R.id.buttonUpdate);

        }
    }
    public void updateMenuItems(List<MenuItem> filteredMenuItems) {
        this.menuItems = filteredMenuItems;
        notifyDataSetChanged();
    }

}
