package co.edu.unipiloto.mascotas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView; // Importación necesaria
import java.util.List;
import android.widget.Button;

public class MascotaAdapter extends RecyclerView.Adapter<MascotaAdapter.ViewHolder> {

    private List<Mascota> mascotas;
    private SQLiteHelper dbHelper;
    private OnMascotaClickListener listener;

    // Constructor con listener incluido
    public MascotaAdapter(List<Mascota> mascotas, SQLiteHelper dbHelper, OnMascotaClickListener listener) {
        this.mascotas = mascotas;
        this.dbHelper = dbHelper;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mascota, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mascota mascota = mascotas.get(position);
        holder.tvNombre.setText(mascota.getNombre());
        holder.tvTipo.setText(mascota.getTipo());

        // Configurar el botón eliminar
        holder.btnEliminar.setOnClickListener(v -> {
            dbHelper.eliminarMascota(mascota.getId());
            mascotas.remove(position);
            notifyItemRemoved(position);

            if (mascotas.isEmpty()) {
                // Acción opcional si la lista queda vacía
            }
        });

        // Manejar clic en el item (selección de mascota)
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMascotaClick(mascota);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mascotas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvTipo;
        Button btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvTipo = itemView.findViewById(R.id.tvTipo);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }

    // Actualizar la lista de mascotas desde la actividad
    public void actualizarLista(List<Mascota> nuevaLista) {
        mascotas.clear();
        mascotas.addAll(nuevaLista);
        notifyDataSetChanged();
    }

    // Interfaz para notificar selección de mascota
    public interface OnMascotaClickListener {
        void onMascotaClick(Mascota mascota);
    }
}



