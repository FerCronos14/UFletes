package com.example.ufletes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.List;

public class MyBusquedaListaFleterosRecyclerViewAdapter extends FirestoreRecyclerAdapter<Fleteros_Lista,MyBusquedaListaFleterosRecyclerViewAdapter.ViewHolder> {

    List<Fleteros_Lista> mValues;
    List<Fleteros_Lista> mValuesFiltrado;
    ListaArticulosFragment.OnListFragmentInteractionListener mListener;

    private Context ctx;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public MyBusquedaListaFleterosRecyclerViewAdapter(Context context,@NonNull FirestoreRecyclerOptions<Fleteros_Lista> options) {
        super(options);
        ctx=context;
        //mValues = items;
        //mListener = listener;
    }



    @Override
    public void onBindViewHolder(@NonNull MyBusquedaListaFleterosRecyclerViewAdapter.ViewHolder holder, int position, @NonNull Fleteros_Lista model) {
        holder.mNombreFletero.setText((model.getNombre() + " " + model.getApellidop() ));
        //holder.mNombreFletero.setText((model.getNombre()));
        holder.mTelefonoFletero.setText((model.getTelefono()));
        if (model.getPathFoto_v() != null) {
            Glide.with(ctx)
                    .load((model.getPathFoto_v()))
                    .fitCenter()
                    .centerCrop()
                    .into(holder.imgFoto_Vehiculo_Busqueda);
        }else {
            Glide.with(ctx)
                    .load(R.drawable.ic_vehiculo_na)
                    .fitCenter()
                    .centerCrop()
                    .into(holder.imgFoto_Vehiculo_Busqueda);
        }
    }
/*
    @Override
    public int getItemCount() {
        return mValues.size();
    }

 */


    @NonNull
    @Override
    public MyBusquedaListaFleterosRecyclerViewAdapter.ViewHolder onCreateViewHolder
    (@NonNull ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_busqueda_lista_fleteros, parent, false);
        return new MyBusquedaListaFleterosRecyclerViewAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        //public final TextView mIdView;
        //public final TextView mContentView;

        public final TextView mNombreFletero;
        public final TextView mTelefonoFletero;
        public final ImageView imgFoto_Vehiculo_Busqueda;

        public Fleteros_Lista mItem;

        public ViewHolder(@NonNull View view) {
            super(view);
            mView = view;
           // mIdView = (TextView) view.findViewById(R.id.item_number);
           // mContentView = (TextView) view.findViewById(R.id.content);
            mNombreFletero = view.findViewById(R.id.txtNombreFletero_ListaBusqueda);
            mTelefonoFletero = view.findViewById(R.id.txtTelefono_ListaBusqueda);
            imgFoto_Vehiculo_Busqueda = view.findViewById(R.id.imageViewVehiculoFletero_busqueda);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNombreFletero.getText() + "'";
        }


    }

}
