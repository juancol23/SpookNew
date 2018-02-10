package relato.app.dems.com.relato.beta.View.Acceso;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

import relato.app.dems.com.relato.beta.MenuCustomizeNow;
import relato.app.dems.com.relato.beta.R;
import relato.app.dems.com.relato.beta.View.Util.ValidarEmail;

public class RegisterRelato extends AppCompatActivity {
    private TextView mTitleRegister;
    private TextInputEditText mRegister_user_name;
    private TextInputEditText mRegister_user_email;
    private TextInputEditText mRegister_user_password;
    private FrameLayout mBtnCreateUserRegister;
    private ProgressDialog mProgress;
    private static Typeface Pacifico,Nightmare,Double,BloodLust;


    private DatabaseReference mDatabaseUser;
    private FirebaseAuth mAuth;

    /*Animación*/
    private Animation mUp_to_down,mhide_to_bottom;
    private LinearLayout mRelato_regis_body;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_relato);

        mProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users");

        mTitleRegister = (TextView) findViewById(R.id.register_title_sangrienta_lectura);
        String text = "<font color='#da152c'>Crear </font>Cuenta";
        mTitleRegister.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

        String bloodLustFuente= "fuentes/BloodLust.ttf";
        String nightmareFuente= "fuentes/Nigh.ttf";
        this.BloodLust = Typeface.createFromAsset(getAssets(),bloodLustFuente);
        this.Nightmare = Typeface.createFromAsset(getAssets(),nightmareFuente);
        mTitleRegister.setTypeface(BloodLust);

        mRelato_regis_body = (LinearLayout) findViewById(R.id.relato_regis_body);


        mRegister_user_name = (TextInputEditText) findViewById(R.id.register_user_name);
        mRegister_user_email = (TextInputEditText) findViewById(R.id.register_user_email);
        mRegister_user_password = (TextInputEditText) findViewById(R.id.register_user_password);

        mBtnCreateUserRegister = (FrameLayout) findViewById(R.id.RegistrarCuenta);


        mUp_to_down = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.up_to_down);
        mhide_to_bottom = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.down_to_up);

        mTitleRegister.setAnimation(mUp_to_down);
        mRelato_regis_body.setAnimation(mUp_to_down);
        mBtnCreateUserRegister.setAnimation(mhide_to_bottom);


        mBtnCreateUserRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegister();

            }
        });

    }

    private void startRegister() {
        String display_name = mRegister_user_name.getText().toString();
        String display_email = mRegister_user_email.getText().toString();
        String display_password = mRegister_user_password.getText().toString();

        if (display_name.length() < 4){
            showSnackBar("Verificar nombre.");
            mRegister_user_name.setError("El usuario debe ser mayor de 4 dígitos.");

        }else if (!validarEmail(display_email)){
            showSnackBar("Email no válido.");
            mRegister_user_email.setError("Email no válido.");

        }else if(display_password.length() < 6){
            showSnackBar("La contraseña es muy corta.");
            mRegister_user_password.setError("Mínimo 6 dígitos.");
        }else{
            showSnackBar("Creando Cuenta.");
            mProgress.setTitle("Registrando Datos.");
            mProgress.setMessage("Asignando Credenciales.");
            mProgress.show();
            register_user(display_name,display_email,display_password);
        }

    }

    private boolean validarEmail(String display_email) {
        ValidarEmail check = new ValidarEmail(getApplicationContext());
        boolean booleamCheckEmail = check.checkEmail(display_email);
        Log.v("checkEmail",""+booleamCheckEmail);
        return booleamCheckEmail;
    }


    private void register_user(final String display_name, String display_email, String display_password) {
        mAuth.createUserWithEmailAndPassword(display_email, display_password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        String user_id = mAuth.getCurrentUser().getUid();

                        DatabaseReference cureent_user_db = mDatabaseUser.child(user_id);
                        cureent_user_db.child("name").setValue(display_name);
                        cureent_user_db.child("image").setValue("https://scontent.flim5-4.fna.fbcdn.net/v/t1.0-1/p320x320/24176780_1618033478256581_9130265489800939037_n.jpg?oh=4c6ac8a55e08147c1c2c3179f661f31f&oe=5A917D80");
                        mProgress.dismiss();
                        createRegister();
                        showSnackBar("Creando Cuenta");

                    } else {
                        mProgress.hide();
                        showSnackBar("No se pudo crear la cuenta por favor intente nuevamente");
                    }

                }
            });
    }

    public void createRegister(){
        Intent i = new Intent(RegisterRelato.this,MenuCustomizeNow.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        finish();
        startActivity(i);

    }

    public void showSnackBar(String msg) {
        Snackbar
                .make(findViewById(R.id.registerRelato), msg, Snackbar.LENGTH_SHORT)
                .show();
    }
}
