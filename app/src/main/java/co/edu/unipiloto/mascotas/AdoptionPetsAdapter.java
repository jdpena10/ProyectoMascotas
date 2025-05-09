package co.edu.unipiloto.mascotas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AdoptionPetsAdapter extends RecyclerView.Adapter<AdoptionPetsAdapter.ViewHolder> {

    private List<AdoptionPet> petsList;

    public AdoptionPetsAdapter(List<AdoptionPet> petsList) {
        this.petsList = petsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdoptionPet pet = petsList.get(position);
        holder.tvPetName.setText(pet.getName());
        holder.tvPetType.setText("Tipo de mascota: " + pet.getType());
        holder.tvPetBreed.setText("Raza: " + pet.getBreed());
        holder.tvPetAge.setText("Edad: " + pet.getAge() + " años");
        holder.tvPetLocation.setText("Ubicación: " + pet.getLocation());
        holder.tvAdoptionStatus.setText("Estado: " + pet.getAdoptionStatus());
        holder.tvUserName.setText("Correo electronico: " + pet.getUserName());
        holder.tvUserPhone.setText("Celular: " + pet.getUserPhone());
    }

    @Override
    public int getItemCount() {
        return petsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPetName, tvPetType, tvPetBreed, tvPetAge, tvPetLocation, tvAdoptionStatus, tvUserName, tvUserPhone;

        public ViewHolder(View itemView) {
            super(itemView);
            tvPetName = itemView.findViewById(R.id.tvPetName);
            tvPetType = itemView.findViewById(R.id.tvPetType);
            tvPetBreed = itemView.findViewById(R.id.tvPetBreed);
            tvPetAge = itemView.findViewById(R.id.tvPetAge);
            tvPetLocation = itemView.findViewById(R.id.tvPetLocation);
            tvAdoptionStatus = itemView.findViewById(R.id.tvAdoptionStatus);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserPhone = itemView.findViewById(R.id.tvUserPhone);
        }
    }
}

