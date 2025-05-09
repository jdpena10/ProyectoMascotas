package co.edu.unipiloto.mascotas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class VeterinarioAdapter extends RecyclerView.Adapter<VeterinarioAdapter.ViewHolder> {
    private List<Veterinario> listaVeterinarios;
    private OnVeterinarioClickListener listener;

    // Constructor que recibe la lista de veterinarios y el listener
    public VeterinarioAdapter(List<Veterinario> listaVeterinarios, OnVeterinarioClickListener listener) {
        this.listaVeterinarios = listaVeterinarios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout del item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_veterinario, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Obtener el veterinario correspondiente a la posición
        Veterinario veterinario = listaVeterinarios.get(position);

        // Establecer los datos del veterinario en las vistas
        holder.tvNombre.setText(veterinario.getNombre());
        holder.tvDireccion.setText(veterinario.getDireccion());
        holder.tvRating.setText("⭐ " + veterinario.getRating());

        // Establecer el clic en el item
        holder.itemView.setOnClickListener(v -> listener.onVeterinarioClick(veterinario));
    }

    @Override
    public int getItemCount() {
        return listaVeterinarios.size();
    }

    // ViewHolder para los elementos de la lista
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDireccion, tvRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            tvRating = itemView.findViewById(R.id.tvRating);
        }
    }

    // Interfaz para manejar los clics
    public interface OnVeterinarioClickListener {
        void onVeterinarioClick(Veterinario veterinario);
    }
}
