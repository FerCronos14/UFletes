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
import com.example.ufletes.ListaArticulosFragment.OnListFragmentInteractionListener;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.List;


public class MyListaArticulosRecyclerViewAdapter extends FirestoreRecyclerAdapter<Articulos_Lista, MyListaArticulosRecyclerViewAdapter.ViewHolder> {
    //RecyclerView.Adapter<MyListaArticulosRecyclerViewAdapter.ViewHolder>

    //private final
    List<Articulos_Lista> mValues;
    private Context ctx;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MyListaArticulosRecyclerViewAdapter(Context context, @NonNull FirestoreRecyclerOptions<Articulos_Lista> options) {
        super(options);
        ctx=context;
    }

    @Override
    public void onBindViewHolder(@NonNull MyListaArticulosRecyclerViewAdapter.ViewHolder holder, int position, @NonNull Articulos_Lista model) {
        {
            holder.textViewNombreArticuloListado.setText((model.getNombre_a()));
            holder.textViewDescripcionArticuloListado.setText((model.getDescri_a()));
            holder.textViewCantidadArticuloListado.setText((model.getCant_a()));
            Glide.with(ctx)
                    .load(model.getPathFoto_a()) // seleccionar path correcto de articulo
                    .fitCenter()
                    .centerCrop()
                    .placeholder(R.drawable.ic_noimg)
                    .into(holder.imageViewArticulo)
            ;
        }
    }

        @NonNull
        @Override
        public MyListaArticulosRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_listaarticulos, parent, false);
            return new MyListaArticulosRecyclerViewAdapter.ViewHolder(view);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView textViewNombreArticuloListado;
            public final TextView textViewDescripcionArticuloListado;
            public final TextView textViewCantidadArticuloListado;
            //public final ImageView imageViewArticulo;
            public final ImageView imageViewArticulo;

            public Articulos_Lista mItem;

            public ViewHolder(@NonNull View view) {
                super(view);
                mView = view;
                textViewNombreArticuloListado = view.findViewById(R.id.textViewListado_Nombre);
                textViewDescripcionArticuloListado = view.findViewById(R.id.textViewListado_Descripcion);
                textViewCantidadArticuloListado = view.findViewById(R.id.textViewListado_Cantidad);
                imageViewArticulo = view.findViewById(R.id.imageViewListado_Foto_Articulo);

            }

        @Override
        public String toString() {
            return super.toString() + " '" + textViewNombreArticuloListado.getText() + "'";
        }

    }
}
