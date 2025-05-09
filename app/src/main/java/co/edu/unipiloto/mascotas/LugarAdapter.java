package co.edu.unipiloto.mascotas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class LugarAdapter extends RecyclerView.Adapter<LugarAdapter.LugarViewHolder> {

    private List<Lugar> listaLugares;

    // Constructor
    public LugarAdapter(List<Lugar> listaLugares) {
        this.listaLugares = listaLugares;
    }

    @NonNull
    @Override
    public LugarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflamos el layout del item de la lista
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lugar, parent, false);
        return new LugarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LugarViewHolder holder, int position) {
        Lugar lugar = listaLugares.get(position);
        holder.bind(lugar);
    }

    @Override
    public int getItemCount() {
        return listaLugares.size();
    }

    // Clase ViewHolder que maneja las vistas de cada item
    public class LugarViewHolder extends RecyclerView.ViewHolder {

        private TextView nombreTextView;
        private TextView direccionTextView;

        public LugarViewHolder(View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.textViewNombreLugar);
            direccionTextView = itemView.findViewById(R.id.textViewDireccionLugar);
        }

        // Metodo para asociar los datos del lugar al item del RecyclerView
        public void bind(Lugar lugar) {
            nombreTextView.setText(lugar.getNombre());
            direccionTextView.setText(lugar.getDireccion());
        }
    }
}

