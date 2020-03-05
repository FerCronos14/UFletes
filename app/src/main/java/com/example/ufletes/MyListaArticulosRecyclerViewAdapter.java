package com.example.ufletes;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ufletes.ListaArticulosFragment.OnListFragmentInteractionListener;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MyListaArticulosRecyclerViewAdapter extends RecyclerView.Adapter<MyListaArticulosRecyclerViewAdapter.ViewHolder> {

    private final List<Articulos_Lista> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyListaArticulosRecyclerViewAdapter(List<Articulos_Lista> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_listaarticulos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.textViewNombreArticuloListado.setText(holder.mItem.getNombre_Articulo());
        holder.textViewDescripcionArticuloListado.setText(holder.mItem.getDescripcion_Articulo());
        holder.textViewCantidadArticuloListado.setText(holder.mItem.getCantidad_Articulo());

        Picasso.get().load(holder.mItem.getPath_Articulo()).into(holder.imageViewArticulo);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textViewNombreArticuloListado;
        public final TextView textViewDescripcionArticuloListado;
        public final TextView textViewCantidadArticuloListado;
        public final ImageView imageViewArticulo;

        public Articulos_Lista mItem;

        public ViewHolder(View view) {
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
