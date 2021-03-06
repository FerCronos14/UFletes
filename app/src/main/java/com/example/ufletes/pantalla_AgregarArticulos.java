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

public class pantalla_AgregarArticulos extends AppCompatActivity implements ListaArticulosFragment.OnListFragmentInteractionListener, View.OnClickListener {

    private EditText mtxtNombreArticulo;
    private TextView mtxtCantidadArticulo;
    private EditText mtxtDescripcionArticulo;

    private String nombreArticulo = "";
    private Integer cantidadArticulo = 1;
    private String descripcionArticulo ="";
    static String sPathFoto_Articulo = " ";
    static String idDocArticulo = "";
    static String idDocArticulo2 = "";

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
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla__agregar_articulos);
        getSupportActionBar().hide();

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

        mPDialog = new ProgressDialog(this, R.style.CustomAlertDialog);

        mFireStore = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
    }

    public void manejoEventosAgregaArticulos (int opcA) {
        switch (opcA) {
            case R.id.btnAgregarArticulo:
                if (nombreArticulo.isEmpty()) {
                    Toast.makeText(pantalla_AgregarArticulos.this, R.string.llenarcampos_articulos, Toast.LENGTH_SHORT).show();
                } else {
                    String cantidad = Integer.toString(cantidadArticulo);
                    Map<String, Object> map = new HashMap<>();
                    map.put("nombre_a", nombreArticulo);
                    map.put("descri_a", descripcionArticulo);
                    map.put("cant_a", cantidad );
                    map.put("pathFoto_a", sPathFoto_Articulo);

                    mPDialog.setTitle(R.string.dialog_subiendo_datos);
                    mPDialog.setMessage(pantalla_AgregarArticulos.this.getResources().getString(R.string.dialog_espere));
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
                            Toast.makeText(pantalla_AgregarArticulos.this, R.string.articulo_guardado + idDocArticulo, Toast.LENGTH_SHORT).show();
                            mtxtNombreArticulo.setText("");
                            mtxtDescripcionArticulo.setText("");
                            mtxtCantidadArticulo.setText("1");
                            sPathFoto_Articulo = "";
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(pantalla_AgregarArticulos.this, R.string.no_guardo_articulo, Toast.LENGTH_SHORT).show();
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
        final CharSequence[] opciones = {
                pantalla_AgregarArticulos.this.getResources().getString(R.string.tomar_foto)
                ,pantalla_AgregarArticulos.this.getResources().getString(R.string.cargar_imagen)
                ,pantalla_AgregarArticulos.this.getResources().getString(R.string.cancelar)
        };
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder((pantalla_AgregarArticulos.this), R.style.CustomAlertDialog);
        alertOpciones.setTitle(R.string.seleccione_opcion);
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals(pantalla_AgregarArticulos.this.getResources().getString(R.string.tomar_foto))) {
                    tomarFoto_Articulo();
                } else {
                    if (opciones[i].equals(pantalla_AgregarArticulos.this.getResources().getString(R.string.cargar_imagen))) {

                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent, pantalla_AgregarArticulos.this.getResources().getString(R.string.seleccione_opcion)), 10);

                    } else {
                        dialogInterface.dismiss();
                    }
                }

            }
        });
        alertOpciones.show();
    }

    String mCurrentPhotoPath;
    private File createImageFile () throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Backup_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_TAKE_PHOTO = 1;
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
                photoURIArticulo = FileProvider.getUriForFile(pantalla_AgregarArticulos.this,
                         "com.example.ufletes.provider"
                        , photoFileA);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURIArticulo.toString());
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private static final int GALLERY_INTENT = 10;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GALLERY_INTENT) {
            Uri uriFotoArticulo = data.getData();
            mPDialog.setTitle(R.string.dialog_subiendo_img);
            mPDialog.setMessage(pantalla_AgregarArticulos.this.getResources().getString(R.string.dialog_espere));
            mPDialog.setCancelable(false);
            mPDialog.show();

            final StorageReference DBArticuloPath = mStorage.child("fotos_articulos").child(MainActivity.idDoc_Cliente).child("galeria/" + uriFotoArticulo.getLastPathSegment());
            DBArticuloPath.putFile(uriFotoArticulo)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(pantalla_AgregarArticulos.this, R.string.subido_exitosamente, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    //Toast.makeText(pantalla_AgregarArticulos.this, "Subiendo imagen, espere por favor.", Toast.LENGTH_SHORT).show();

                    System.out.println("Upload is " + progress + "% done");
                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("Upload is paused");
                    Toast.makeText(pantalla_AgregarArticulos.this, R.string.pausa, Toast.LENGTH_SHORT).show();
                    mPDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(pantalla_AgregarArticulos.this,R.string.error, Toast.LENGTH_SHORT).show();
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
                        mPDialog.dismiss();
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });


        } else {
            if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
                mPDialog.setTitle(R.string.dialog_subiendo_img);
                mPDialog.setMessage(pantalla_AgregarArticulos.this.getResources().getString(R.string.dialog_espere));
                mPDialog.setCancelable(false);
                mPDialog.show();

                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] b = stream.toByteArray();
                Uri uri = data.getData();
                final StorageReference filepath = mStorage.child("fotos_articulos").child(MainActivity.idDoc_Cliente).child("camara/foto " + new Date());
                filepath.putBytes(b).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(pantalla_AgregarArticulos.this, R.string.subir_imagen, Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(pantalla_AgregarArticulos.this,R.string.error,Toast.LENGTH_LONG).show();


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
                            mPDialog.dismiss();
                        } else {
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onListFragmentInteraction(Articulos_Lista item) {

    }

    @Override
    public void onClick(View view) {
        nombreArticulo = mtxtNombreArticulo.getText().toString();
        descripcionArticulo =  mtxtDescripcionArticulo.getText().toString();
        manejoEventosAgregaArticulos(view.getId());
    }


}
