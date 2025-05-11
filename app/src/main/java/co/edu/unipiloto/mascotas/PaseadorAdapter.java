package co.edu.unipiloto.mascotas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class PaseadorAdapter extends RecyclerView.Adapter<PaseadorAdapter.PaseadorViewHolder> {
    private Context context;
    private List<Paseador> paseadores;

    public PaseadorAdapter(Context context, List<Paseador> paseadores) {
        this.context = context;
        this.paseadores = paseadores;
    }

    @NonNull
    @Override
    public PaseadorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_paseador, parent, false);
        return new PaseadorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaseadorViewHolder holder, int position) {
        Paseador paseador = paseadores.get(position);
        holder.nombreTextView.setText(paseador.getNombre());
        holder.direccionTextView.setText(paseador.getDireccion());  // Mostrar la dirección
        holder.celularTextView.setText(paseador.getCelular());      // Mostrar el celular
        holder.ratingBar.setRating(paseador.getCalificacion());
        Glide.with(context).load(paseador.getImagenUrl()).into(holder.imagen);
    }

    @Override
    public int getItemCount() {
        return paseadores.size();
    }

    static class PaseadorViewHolder extends RecyclerView.ViewHolder {
        ImageView imagen;
        TextView nombreTextView;
        TextView direccionTextView;  // Nueva vista para la dirección
        TextView celularTextView;    // Nueva vista para el celular
        RatingBar ratingBar;

        public PaseadorViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.imagenPaseador);
            nombreTextView = itemView.findViewById(R.id.nombrePaseador);
            direccionTextView = itemView.findViewById(R.id.direccionPaseador);  // Referencia a la dirección
            celularTextView = itemView.findViewById(R.id.celularPaseador);      // Referencia al celular
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}

