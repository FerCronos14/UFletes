package com.example.ufletes;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ufletes.ListaArticulosFragment.OnListFragmentInteractionListener;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class MyListaArticulosRecyclerViewAdapter extends FirestoreRecyclerAdapter<Articulos_Lista, MyListaArticulosRecyclerViewAdapter.ViewHolder> {
    //RecyclerView.Adapter<MyListaArticulosRecyclerViewAdapter.ViewHolder>

    List<Articulos_Lista> mValues;
    private Context ctx;
    private int expandedPosition = -1;
    CardView cvListArticulos;
    Button mbtnEliminarArticulo;

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
    public void onBindViewHolder(@NonNull final MyListaArticulosRecyclerViewAdapter.ViewHolder holder, final int position, @NonNull Articulos_Lista model) {
        {
            holder.textViewNombreArticuloListado.setText((model.getNombre_a()));
            holder.textViewDescripcionArticuloListado.setText((model.getDescri_a()));
            holder.textViewCantidadArticuloListado.setText((model.getCant_a()));

            ObservableSnapshotArray<Articulos_Lista> observableSnapshotArray = getSnapshots();
            final DocumentReference documentReference =
                    observableSnapshotArray.getSnapshot(position).getReference();

            Glide.with(ctx)
                    .load(model.getPathFoto_a()) // seleccionar path correcto de articulo
                    .fitCenter()
                    .centerCrop()
                    .placeholder(R.drawable.ic_noimg)
                    .into(holder.imageViewArticulo)
            ;
            final boolean isExpanded = position==expandedPosition;
            holder.mllExpandAreaListArticulos.setVisibility(isExpanded?View.VISIBLE:View.GONE);
            holder.mView.setActivated(isExpanded);
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    expandedPosition = isExpanded ? -1:position;
                    TransitionManager.beginDelayedTransition(cvListArticulos);
                    notifyDataSetChanged();
                    mbtnEliminarArticulo = view.findViewById(R.id.btnEliminar_listArticulos);
                    mbtnEliminarArticulo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            documentReference.delete();

                        }
                    });
                }
            });
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

            public LinearLayout mllExpandAreaListArticulos;

            public Articulos_Lista mItem;

            public ViewHolder(@NonNull View view) {
                super(view);
                mView = view;
                textViewNombreArticuloListado = view.findViewById(R.id.textViewListado_Nombre);
                textViewDescripcionArticuloListado = view.findViewById(R.id.textViewListado_Descripcion);
                textViewCantidadArticuloListado = view.findViewById(R.id.textViewListado_Cantidad);
                imageViewArticulo = view.findViewById(R.id.imageViewListado_Foto_Articulo);
                mllExpandAreaListArticulos= view.findViewById(R.id.llExpandAreaListaArticulosA);
                cvListArticulos= view.findViewById(R.id.cardviewListArticulos);

            }

        @Override
        public String toString() {
            return super.toString() + " '" + textViewNombreArticuloListado.getText() + "'";
        }

    }
}
