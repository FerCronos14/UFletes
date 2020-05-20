import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.ufletes.R;

public class cuadro_dialogo_busqfletero {

    private RadioButton mr1Norm, mr2AZ, mr3ZA;

    public cuadro_dialogo_busqfletero(Context con) {
        final Dialog ventana = new Dialog(con);
        ventana.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ventana.setCancelable(true);
        ventana.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ventana.setContentView(R.layout.cuadro_dialogo_busquedafletero);


         if (mr1Norm.isChecked()) {
            //Toast.makeText(, "de A a Z", Toast.LENGTH_SHORT).show();
            //mBusqueda = "";
            ventana.dismiss();
        }
        if (mr2AZ.isChecked()) {
            //Toast.makeText(getContext(), "de A a Z", Toast.LENGTH_SHORT).show();
            //mBusqueda = "Acendente";
            ventana.dismiss();
        }
        if (mr2AZ.isChecked()) {
            //Toast.makeText(getContext(), "al revez, campeon", Toast.LENGTH_SHORT).show();
            //mBusqueda = "Descendente";
            ventana.dismiss();
        }

        ventana.show();
    }


}
