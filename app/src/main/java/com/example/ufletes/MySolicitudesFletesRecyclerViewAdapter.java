package com.example.ufletes;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ufletes.SolicitudesFletesFragment.OnListFragmentInteractionListener;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import java.util.List;

public class MySolicitudesFletesRecyclerViewAdapter extends FirestoreRecyclerAdapter<Solicitudes_Lista, MySolicitudesFletesRecyclerViewAdapter.ViewHolder> {


    //private final OnListFragmentInteractionListener mListener;


    List<Solicitudes_Lista> mValues;
    private Context ctx;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public MySolicitudesFletesRecyclerViewAdapter(Context context, @NonNull FirestoreRecyclerOptions<Solicitudes_Lista> options) {
        super(options);
        ctx=context;
    }


    @Override
    public void onBindViewHolder(@NonNull MySolicitudesFletesRecyclerViewAdapter.ViewHolder holder, int position, @NonNull Solicitudes_Lista model)
    {
        holder.mtextViewNombreSolicitud.setText((model.getNombre_s() + " " + model.getApellidop_s()));
        holder.mtextViewTelefonoSolicitud.setText((model.getTelefono_s()));
        holder.mtextViewDirOrigenSolicitud.setText((model.getDirOrigen_s()));
        holder.mtextViewDirDestinoSolicitud.setText(model.getDirDestino_s());

    }


    @NonNull
    @Override
    public MySolicitudesFletesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_solicitudes_fletes, parent, false);
        return new MySolicitudesFletesRecyclerViewAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
       // public final TextView mIdView;
        //public final TextView mContentView;
        public final TextView mtextViewNombreSolicitud;
        public final TextView mtextViewTelefonoSolicitud;
        //public final TextView mtextViewStatusSolicitud;
        public final TextView mtextViewDirOrigenSolicitud;
        public final TextView mtextViewDirDestinoSolicitud;
        //public final ImageView imageViewArticulo;

        public ViewHolder(View view) {
            super(view);
            mView = view;
           mtextViewNombreSolicitud = view.findViewById(R.id.textViewNombreCliente_Solicitud);
           mtextViewTelefonoSolicitud = view.findViewById(R.id.textViewTelCliente_Solicitud);
           mtextViewDirOrigenSolicitud = view.findViewById(R.id.textViewDirOrigen_Solicitud);
           mtextViewDirDestinoSolicitud = view.findViewById(R.id.textViewDirDestino_Solicitud);
           //mtextViewStatusSolicitud = view.findViewById(R.id.estatus);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mtextViewNombreSolicitud.getText() + "'";
        }
    }
}
