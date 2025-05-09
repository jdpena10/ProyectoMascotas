package co.edu.unipiloto.mascotas;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VaccinesAdapter extends RecyclerView.Adapter<VaccinesAdapter.VaccinesViewHolder> {

    private List<String> listaVacunas;
    private Context context;

    public VaccinesAdapter(List<String> listaVacunas, Context context) {
        this.listaVacunas = listaVacunas;
        this.context = context;
    }

    @NonNull
    @Override
    public VaccinesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_vaccine, parent, false);
        return new VaccinesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VaccinesViewHolder holder, int position) {
        String vacuna = listaVacunas.get(position);
        holder.vaccineTextView.setText(vacuna);
    }

    @Override
    public int getItemCount() {
        return listaVacunas.size();
    }

    // Método para actualizar la lista de vacunas
    public void addVacuna(String vacuna) {
        listaVacunas.add(vacuna);
        notifyItemInserted(listaVacunas.size() - 1); // Notificar al adaptador sobre el nuevo ítem
    }

    public static class VaccinesViewHolder extends RecyclerView.ViewHolder {

        TextView vaccineTextView;

        public VaccinesViewHolder(View itemView) {
            super(itemView);
            vaccineTextView = itemView.findViewById(R.id.textViewVaccine); // Asegúrate de que el id esté correcto
        }
    }
}

