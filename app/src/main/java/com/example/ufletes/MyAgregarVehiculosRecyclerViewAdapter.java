package com.example.ufletes;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ufletes.AgregarVehiculosFragment.OnListFragmentInteractionListener;
import com.example.ufletes.ui.Vehiculos_Lista;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import java.util.List;

public class MyAgregarVehiculosRecyclerViewAdapter extends FirestoreRecyclerAdapter<Vehiculos_Lista, MyAgregarVehiculosRecyclerViewAdapter.ViewHolder> {

    List<Vehiculos_Lista> mValues;
    OnListFragmentInteractionListener mListener;

    private Context ctx;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public MyAgregarVehiculosRecyclerViewAdapter(Context context,@NonNull FirestoreRecyclerOptions<Vehiculos_Lista> options) {
        super(options);
        ctx=context;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAgregarVehiculosRecyclerViewAdapter.ViewHolder holder, int position, @NonNull Vehiculos_Lista model) {
        {
            holder.mtextViewMarcaV.setText((model.getMarca_v()));
            holder.mtextViewTipoV.setText((model.getTipo_v()));
            holder.mtextViewVolv.setText((model.getVol_v()));
            Glide.with(ctx)
                    .load(Registro_AgregarAuto.sPathFoto_Vehiculo)
                    .fitCenter()
                    .centerCrop()
            .into(holder.imgFoto_Vehiculo)
            ;
//holder.itemView.getContext()
        }
    }

        @NonNull
        @Override
        public MyAgregarVehiculosRecyclerViewAdapter.ViewHolder onCreateViewHolder
        (@NonNull ViewGroup parent,int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_agregar_vehiculos, parent, false);
            return new MyAgregarVehiculosRecyclerViewAdapter.ViewHolder(view);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mtextViewTipoV;
            public final TextView mtextViewMarcaV;
            public final TextView mtextViewVolv;
            public final ImageView imgFoto_Vehiculo;

            public Vehiculos_Lista mItem;

            public ViewHolder(@NonNull View view) {
                super(view);
                mView = view;
                mtextViewMarcaV = view.findViewById(R.id.textViewMarca_V);
                mtextViewTipoV = view.findViewById(R.id.textViewTipo_V);
                mtextViewVolv = view.findViewById(R.id.textViewVol_V);
                imgFoto_Vehiculo = view.findViewById(R.id.imageViewVehiculoFletero);
            }


            @Override
            public String toString() {
                return super.toString() + " '" + mtextViewMarcaV.getText() + "'";
            }

        }
    }

