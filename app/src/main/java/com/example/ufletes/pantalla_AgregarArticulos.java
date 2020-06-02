package com.example.ufletes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.multidex.BuildConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ufletes.holders.articulosClienteHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.google.firebase.firestore.FirebaseFirestore.getInstance;

public class pantalla_AgregarArticulos extends AppCompatActivity implements  View.OnClickListener {

    private EditText mtxtNombreArticulo;
    private TextView mtxtCantidadArticulo;
    private EditText mtxtDescripcionArticulo;

    private String nombreArticulo = "";
    private Integer cantidadArticulo = 1;
    private String descripcionArticulo ="";
    static String sPathFoto_Articulo = " ";
    static String idDocArticulo = "";

    private Button mbtnAgregarArticulo;
    private ImageButton mbtnSumar;
    private ImageButton mbtnRestar;
    private Button mbtnFotoArticulo;

    private ProgressDialog mPDialog;

    private Uri photoURIArticulo;

    private FirebaseFirestore mFireStore;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private StorageReference mStorage;
    private RecyclerView RVListaArticulos;
    private FirestoreRecyclerAdapter<Articulos_Lista, articulosClienteHolder> Adapter_ListArticulos;
    private FirestoreRecyclerOptions<Articulos_Lista> FirestoreRecyclerOptionsList;
    private int expandedPosition = -1;
    Query query;
    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    private static final int GALLERY_INTENT = 10;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla__agregar_articulos);
        getSupportActionBar().hide();
        createInstances();
        loadData();
    }

    private void createInstances() {
        mtxtNombreArticulo = findViewById(R.id.editTextNombreArticulo);
        mtxtCantidadArticulo = findViewById(R.id.txtCantidadArticulo);
        mtxtDescripcionArticulo = findViewById(R.id.editTextDescripcionArticulo);

        mbtnAgregarArticulo = findViewById(R.id.btnAgregarArticulo);
        mbtnSumar = findViewById(R.id.imageButtonMas);
        mbtnRestar = findViewById(R.id.imageButtonMenos);
        mbtnFotoArticulo = findViewById(R.id.btnAgregarFoto_Articulo);
        mbtnAgregarArticulo.setOnClickListener(pantalla_AgregarArticulos.this);
        mbtnRestar.setOnClickListener(pantalla_AgregarArticulos.this);
        mbtnSumar.setOnClickListener(pantalla_AgregarArticulos.this);
        mbtnFotoArticulo.setOnClickListener(pantalla_AgregarArticulos.this);

        mPDialog = new ProgressDialog(this);

        mFireStore = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();

        RVListaArticulos = findViewById(R.id.listArticulosRVAct);
    }

    private void loadData() {
        RVListaArticulos.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        query = getInstance()
                .collection("Cliente")
                .document(MainActivity.idDoc_Cliente)
                .collection("Articulos");

        FirestoreRecyclerOptionsList = new FirestoreRecyclerOptions.Builder<Articulos_Lista>()
                .setQuery(query, Articulos_Lista.class)
                .setLifecycleOwner(this)
                .build();


        Adapter_ListArticulos = new FirestoreRecyclerAdapter<Articulos_Lista, articulosClienteHolder>(FirestoreRecyclerOptionsList) {
            @Override
            protected void onBindViewHolder(@NonNull articulosClienteHolder holder, int position, @NonNull Articulos_Lista model) {
                holder.textViewNombreArticuloListado.setText((model.getNombre_a()));
                holder.textViewDescripcionArticuloListado.setText((model.getDescri_a()));
                holder.textViewCantidadArticuloListado.setText((model.getCant_a()));
                Glide.with(getApplicationContext())
                        .load(model.getPathFoto_a()) // seleccionar path correcto de articulo
                        .fitCenter()
                        .centerCrop()
                        .placeholder(R.drawable.ic_noimg)
                        .into(holder.imageViewArticulo);
            }

            @NonNull
            @Override
            public articulosClienteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Toast.makeText(getApplication(), "ocreate holder" , Toast.LENGTH_SHORT).show();

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_listaarticulos, parent, false);
                return new articulosClienteHolder(view);
            }
        };

        RVListaArticulos.setLayoutManager(linearLayoutManager);
        RVListaArticulos.setAdapter(Adapter_ListArticulos);
        Adapter_ListArticulos.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Adapter_ListArticulos.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        Adapter_ListArticulos.stopListening();
    }

    public void manejoEventosAgregaArticulos (int opcA) {
        switch (opcA) {
            case R.id.btnAgregarArticulo:
                if (nombreArticulo.isEmpty()) {
                    Toast.makeText(pantalla_AgregarArticulos.this, "Favor de llenar nombre del articulo", Toast.LENGTH_SHORT).show();
                } else {
                    String cantidad = Integer.toString(cantidadArticulo);
                    Map<String, Object> map = new HashMap<>();
                    map.put("nombre_a", nombreArticulo);
                    map.put("descri_a", descripcionArticulo);
                    map.put("cant_a", cantidad );
                    map.put("pathFoto_a", sPathFoto_Articulo);

                    mPDialog.setTitle("Subiendo...");
                    mPDialog.setMessage("Registrando datos");
                    mPDialog.setCancelable(false);
                    mPDialog.show();

                    mFireStore.collection("Cliente")
                            .document(MainActivity.idDoc_Cliente)
                            .collection("Articulos")
                            .add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            mPDialog.dismiss();
                            //idDocArticulo = documentReference.getId();
                            Toast.makeText(pantalla_AgregarArticulos.this, "Articulo guardado" + idDocArticulo, Toast.LENGTH_SHORT).show();
                            mtxtNombreArticulo.setText("");
                            mtxtDescripcionArticulo.setText("");
                            mtxtCantidadArticulo.setText("1");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(pantalla_AgregarArticulos.this, "No se pudo guardar el articulo", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
                break;
            case R.id.imageButtonMas:
                cantidadArticulo += 1;
                mtxtCantidadArticulo.setText(cantidadArticulo.toString());

                break;
            case  R.id.imageButtonMenos:
                cantidadArticulo -= 1;
                        if (cantidadArticulo <= 1) {
                            cantidadArticulo = 1;
                        }
                mtxtCantidadArticulo.setText(cantidadArticulo.toString());
                break;
            case R.id.btnAgregarFoto_Articulo:
                subirFotoArticulo();
                break;
                default:
                break;
        }
    }

    private void subirFotoArticulo() {
        final CharSequence[] opciones = {"Tomar foto", "Cargar imagen", "Cancelar"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder((pantalla_AgregarArticulos.this));
        alertOpciones.setTitle("Seleccione una opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar foto")) {
                    tomarFoto_Articulo();
                } else {
                    if (opciones[i].equals("Cargar imagen")) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent, "Seleccione la aplicación"), 10);

                    } else {
                        dialogInterface.dismiss();
                    }
                }

            }
        });
        alertOpciones.show();
    }


    private File createImageFile () throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Backup_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void tomarFoto_Articulo () {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFileA = null;
            try {
                photoFileA = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFileA != null) {
                photoURIArticulo = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFileA);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURIArticulo.toString());
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GALLERY_INTENT) {
            Uri uriFotoArticulo = data.getData();

            final StorageReference DBArticuloPath = mStorage.child("fotos_articulos").child(MainActivity.idDoc_Cliente).child("galeria/" + uriFotoArticulo.getLastPathSegment());
            DBArticuloPath.putFile(uriFotoArticulo)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(pantalla_AgregarArticulos.this, "Se subio exitosamente", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    Toast.makeText(pantalla_AgregarArticulos.this, "Subiendo imagen, espere por favor.", Toast.LENGTH_SHORT).show();

                    System.out.println("Upload is " + progress + "% done");
                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("Upload is paused");
                    Toast.makeText(pantalla_AgregarArticulos.this, "Ha sido pausado...", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(pantalla_AgregarArticulos.this,"Sorry", Toast.LENGTH_SHORT).show();
                }
            }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return DBArticuloPath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        sPathFoto_Articulo = downloadUri.toString();
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });


        } else {
            if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {

                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                byte[] b = stream.toByteArray();
                Uri uri = data.getData();
                final StorageReference filepath = mStorage.child("fotos_articulos").child(MainActivity.idDoc_Cliente).child("camara/foto " + new Date());
                filepath.putBytes(b).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(pantalla_AgregarArticulos.this, "Foto súbida", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(pantalla_AgregarArticulos.this,"¡Falló!",Toast.LENGTH_LONG).show();


                    }
                }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            sPathFoto_Articulo = downloadUri.toString();
                        } else {
                        }
                    }
                });
            }
        }
    }



    @Override
    public void onClick(View view) {
        nombreArticulo = mtxtNombreArticulo.getText().toString();
        descripcionArticulo =  mtxtDescripcionArticulo.getText().toString();
        manejoEventosAgregaArticulos(view.getId());
    }


}
