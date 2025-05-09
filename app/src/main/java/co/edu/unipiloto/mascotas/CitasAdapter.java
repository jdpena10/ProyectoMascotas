package co.edu.unipiloto.mascotas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CitasAdapter extends RecyclerView.Adapter<CitasAdapter.ViewHolder> {

    private List<Cita> lista;

    public CitasAdapter(List<Cita> lista) {
        this.lista = lista;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView texto;

        public ViewHolder(View itemView) {
            super(itemView);
            texto = itemView.findViewById(R.id.textoCita);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cita, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Cita cita = lista.get(position);
        holder.texto.setText(cita.fecha + " " + cita.hora + " - " + cita.nombreVeterinario + "\n" + cita.motivo);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}

