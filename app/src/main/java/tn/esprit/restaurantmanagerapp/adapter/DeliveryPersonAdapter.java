package tn.esprit.restaurantmanagerapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import tn.esprit.restaurantmanagerapp.R;
import tn.esprit.restaurantmanagerapp.entity.DeliveryPerson;

public class DeliveryPersonAdapter extends RecyclerView.Adapter<DeliveryPersonAdapter.ViewHolder> {

    private List<DeliveryPerson> deliveryPersons;
    private Context context;

    public DeliveryPersonAdapter(Context context, List<DeliveryPerson> deliveryPersons) {
        this.context = context;
        this.deliveryPersons = deliveryPersons;
    }

    public void setDeliveryPersons(List<DeliveryPerson> deliveryPersons) {
        this.deliveryPersons = deliveryPersons;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DeliveryPersonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.delivery_person_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryPersonAdapter.ViewHolder holder, int position) {
        DeliveryPerson dp = deliveryPersons.get(position);
        holder.nameTextView.setText(dp.getName());
        holder.ratingTextView.setText("Rating: " + dp.getRating());
        if (dp.getProfileImageUri() != null && !dp.getProfileImageUri().isEmpty()) {
            holder.profileImageView.setImageURI(Uri.parse(dp.getProfileImageUri()));
        } else {
            holder.profileImageView.setImageResource(R.drawable.img);
        }
    }

    @Override
    public int getItemCount() {
        return deliveryPersons.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImageView;
        TextView nameTextView;
        TextView ratingTextView;

        ViewHolder(View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.delivery_person_image);
            nameTextView = itemView.findViewById(R.id.delivery_person_name);
            ratingTextView = itemView.findViewById(R.id.delivery_person_rating);
        }
    }
}
