package com.example.ufletes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ufletes.ui.Vehiculos_Lista;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
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

public class Registro_AgregarAuto extends AppCompatActivity implements AgregarVehiculosFragment.OnListFragmentInteractionListener, View.OnClickListener {

    private EditText mtxtMarcaVehiculo;
    private EditText mtxtMedidasVehiculo;
    private EditText mtxtVolVehiculo;

    private String marcaVehiculo = "";
    private String tipoVehiculo = "";
    private String volVehiculo = "";
    private String medidasVehiculo = "";
    public static String sPathFoto_Vehiculo = " ";

    private Button mbtnAgregarVehiculo;
    private Button mbtnFotoVehivulo;
    //Agregar botones que falten
    private ProgressDialog mPDialog;

    private Spinner mspinnerCubicaje;

    private Uri photoURI;
    Uri picUri;

    private static final String CARPETA_PRINCIPAL = "ImagenesUFletes/"; //directorio principal
    private static final String CARPETA_IMAGEN = "Vehiculos"; //carpeta donde se guaardan las fotos
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN; //ruta carpeta de directorio
    private String pathCamara;
    File fileImagenVehiculo;
    Bitmap bitmap;
    private static final int COS_SELECCIONA = 10;
    private static final int COS_FOTO = 20;



    private FirebaseFirestore mFireStore;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseAuth mAuth;
    private StorageReference mStorage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro__agregar_auto);

        mtxtMarcaVehiculo = findViewById(R.id.editTextMarcaVehiculo);
        mtxtVolVehiculo = findViewById(R.id.editTextCapacidadVehiculo);
        mtxtMedidasVehiculo = findViewById(R.id.editMedidasVehiculo);

        mbtnAgregarVehiculo = findViewById(R.id.btnAñadirVehiculo);
        mbtnAgregarVehiculo.setOnClickListener(this);

        mbtnFotoVehivulo = findViewById(R.id.btnAgregarFotoVehiculo);
        mbtnFotoVehivulo.setOnClickListener(this);

        mPDialog = new ProgressDialog(this, R.style.CustomAlertDialog);
        mspinnerCubicaje = findViewById(R.id.spinnerTipo_Vehiculo);
        final ArrayAdapter<CharSequence> adapterTipo = ArrayAdapter.createFromResource(this, R.array.SpinnerTipoCaja, android.R.layout.simple_spinner_item);
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mspinnerCubicaje.setAdapter(adapterTipo);
        mspinnerCubicaje.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String textSpinner = adapterView.getItemAtPosition(i).toString();
                if (i >= 0) {
                    tipoVehiculo = textSpinner;
                }
                Toast.makeText(Registro_AgregarAuto.this, tipoVehiculo, Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mFireStore = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        getSupportActionBar().hide();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    public void manejoEventosAgregaVehiculos (int opcA) {
        switch (opcA) {
            case R.id.btnAñadirVehiculo:
                if (marcaVehiculo.isEmpty()) {
                    Toast.makeText(Registro_AgregarAuto.this, "Favor de llenar nombre del articulo", Toast.LENGTH_SHORT).show();
                }else if (tipoVehiculo.isEmpty() ){
                    Toast.makeText(Registro_AgregarAuto.this, "Favor de seleccionar el tipo de caja", Toast.LENGTH_SHORT).show();
                }
                else {
                    final Map<String, Object> map = new HashMap<>();

                    map.put("marca_v", marcaVehiculo);
                    map.put("tipo_v", tipoVehiculo);
                    map.put("vol_v", volVehiculo + " tonelada(s)");
                    map.put("medida_v", medidasVehiculo + " mts³");
                    map.put("pathFoto_v", sPathFoto_Vehiculo);

                    mPDialog.setTitle("Subiendo...");
                    mPDialog.setMessage("Registrando datos");
                    mPDialog.setCancelable(false);
                    mPDialog.show();

                    Map<String, Object> mapPathFoto = new HashMap<>();
                    mapPathFoto.put("pathFoto_v", sPathFoto_Vehiculo);

                    DocumentReference newPathFotoFletero = mFireStore.collection("Fletero")
                            .document(MainActivity.idDoc_Fletero);
                    newPathFotoFletero.update(mapPathFoto)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Registro_AgregarAuto.this, "Foto actualizada" , Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Registro_AgregarAuto.this, "Error al actualizar foto" , Toast.LENGTH_SHORT).show();

                                }
                            });;


                    mFireStore.collection("Fletero")
                            .document(MainActivity.idDoc_Fletero)
                            .collection("Vehiculos")
                            .add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            mPDialog.dismiss();
                            Toast.makeText(Registro_AgregarAuto.this, "Vehiculo guardado", Toast.LENGTH_SHORT).show();
                            mtxtMarcaVehiculo.setText("");
                            mtxtMedidasVehiculo.setText("");
                            mtxtVolVehiculo.setText("");
                            sPathFoto_Vehiculo = "";
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Registro_AgregarAuto.this, "No se pudo guardar el vehiculo", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                }
            case R.id.btnAgregarFotoVehiculo:
                subirFoto();
                break;
             default:
                break;
        }
    }

    private void subirFoto() {
        final CharSequence[] opciones = {"Tomar foto", "Cargar imagen", "Cancelar"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder((Registro_AgregarAuto.this), R.style.CustomAlertDialog);
        alertOpciones.setTitle("Seleccione una opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar foto")) {
                    tomarFotoM2();
                } else {
                    if (opciones[i].equals("Cargar imagen")) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent.createChooser(intent, "Seleccione la aplicación"), GALLERY_INTENT );
                    } else {
                        dialogInterface.dismiss();
                    }
                }

            }
        });
        alertOpciones.show();
    }

    //Metodo para crear un nombre unico de cada fotografia --------------------------------------------------------------------------
    String mCurrentPhotoPath;
    private File createImageFile () throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Backup_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private static final int GALLERY_INTENT = 10;
    static final int REQUEST_TAKE_PHOTO = 1;
    private void tomarFotoM2 () {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.ufletes.provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI.toString());
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    //----------------------------------------------------------------------------------------------------------------------------------
    static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GALLERY_INTENT) {
            mPDialog.setTitle("Subiendo imagen, espere por favor.");
            mPDialog.setMessage("Espere un momento...");
            mPDialog.setCancelable(false);
            mPDialog.show();
            Uri uriFotoVehiculo = data.getData();

            final StorageReference DBVehiculoPath = mStorage.child("fotos_vehiculos").child(MainActivity.idDoc_Fletero).child("galeria/" + uriFotoVehiculo.getLastPathSegment());
            DBVehiculoPath.putFile(uriFotoVehiculo)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(Registro_AgregarAuto.this, "Se subio exitosamente", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    //Toast.makeText(Registro_AgregarAuto.this, "Subiendo imagen, espere por favor", Toast.LENGTH_SHORT).show();

                    System.out.println("Upload is " + progress + "% done");
                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("Upload is paused");
                    Toast.makeText(Registro_AgregarAuto.this, "Ha sido pausado...", Toast.LENGTH_SHORT).show();
                    mPDialog.dismiss();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Registro_AgregarAuto.this,"Sorry", Toast.LENGTH_SHORT).show();
                }
            }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return DBVehiculoPath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        sPathFoto_Vehiculo = downloadUri.toString();
                        mPDialog.dismiss();
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
            //sPathFoto_Vehiculo = linkFoto_Vehiculo.toString();

        }else {

            if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
                mPDialog.setTitle("Subiendo imagen, espere por favor.");
                mPDialog.setMessage("Espere un momento...");
                mPDialog.setCancelable(false);
                mPDialog.show();

                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                byte[] b = stream.toByteArray();
                Uri uri = data.getData();
                final StorageReference filepath = mStorage.child("fotos_vehiculos").child(MainActivity.idDoc_Fletero).child("camara/foto " + new Date());
                filepath.putBytes(b).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(Registro_AgregarAuto.this, "Foto súbida", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Registro_AgregarAuto.this,"¡Falló!",Toast.LENGTH_LONG).show();


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
                            sPathFoto_Vehiculo = downloadUri.toString();
                            mPDialog.dismiss();
                        } else {
                        }
                    }
                });
            }
        }

    }


    @Override
    public void onListFragmentInteraction(Vehiculos_Lista item) {

    }

    @Override
    public void onClick(View view) {
        marcaVehiculo = mtxtMarcaVehiculo.getText().toString();
        medidasVehiculo =  mtxtMedidasVehiculo.getText().toString();
        volVehiculo = mtxtVolVehiculo.getText().toString();
        manejoEventosAgregaVehiculos(view.getId());
    }

}
