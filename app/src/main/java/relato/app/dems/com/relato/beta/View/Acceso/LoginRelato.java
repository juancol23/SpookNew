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

import relato.app.dems.com.relato.beta.MenuCustomizeNow;
import relato.app.dems.com.relato.beta.R;
import relato.app.dems.com.relato.beta.View.Util.ValidarEmail;

public class LoginRelato extends AppCompatActivity {
    private TextView mTitleRegister;
    private TextInputEditText mLogin_user_email;
    private TextInputEditText mLogin_user_password;
    private FrameLayout mBtnCreateUserLogin;
    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;
    private static Typeface Pacifico,Nightmare,Double,BloodLust;

    /*Animación*/
    private Animation mUp_to_down,mhide_to_bottom;
    private LinearLayout mRelato_login_body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_relato);

        mProgress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();


        mRelato_login_body = (LinearLayout) findViewById(R.id.relato_login_body);


        mTitleRegister = (TextView) findViewById(R.id.login_title_sangrienta_lectura);
        String text = "<font color='#da152c'>Acc</font>eso";
        mTitleRegister.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

        String bloodLustFuente= "fuentes/BloodLust.ttf";
        String nightmareFuente= "fuentes/Nigh.ttf";
        this.BloodLust = Typeface.createFromAsset(getAssets(),bloodLustFuente);
        this.Nightmare = Typeface.createFromAsset(getAssets(),nightmareFuente);
        mTitleRegister.setTypeface(BloodLust);

        mLogin_user_email = (TextInputEditText) findViewById(R.id.login_user_email);
        mLogin_user_password = (TextInputEditText) findViewById(R.id.login_user_password);

        mBtnCreateUserLogin = (FrameLayout) findViewById(R.id.loginCuenta);


        mUp_to_down = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.up_to_down);
        mhide_to_bottom = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.down_to_up);

        mTitleRegister.setAnimation(mUp_to_down);
        mRelato_login_body.setAnimation(mUp_to_down);
        mBtnCreateUserLogin.setAnimation(mhide_to_bottom);



        mBtnCreateUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startLogin();

            }
        });


    }

    private void startLogin() {
        String display_email = mLogin_user_email.getText().toString();
        String display_password = mLogin_user_password.getText().toString();



        if (!validarEmail(display_email)){
            showSnackBar("Email no válido.");
            mLogin_user_email.setError("Email no válido.");
        }else if(display_password.length() < 6){
            showSnackBar("La contraseña es muy corta.");
            mLogin_user_password.setError("Mínimo 6 dígitos.");
        }else{
            showSnackBar("Accediendo");
            mProgress.setTitle("Iniciando Sessión...");
            mProgress.setMessage("Por espere mientras verificamos sus credenciales.");
            mProgress.setCancelable(false);
            mProgress.show();
            loginUser(display_email,display_password);
        }

    }

    private boolean validarEmail(String display_email) {
        ValidarEmail check = new ValidarEmail(getApplicationContext());
        boolean booleamCheckEmail = check.checkEmail(display_email);
        Log.v("checkEmail",""+booleamCheckEmail);
        return booleamCheckEmail;
    }

    private void loginUser(String display_email, String display_password) {

        mAuth.signInWithEmailAndPassword(display_email,display_password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    accesoPermitido();
                }else{
                    Log.v("task",""+task);
                    mProgress.hide();
                    showSnackBar("Acceso denegado, Intente nuevamente.");
                }
            }
        });

    }


    public void accesoPermitido(){
        Intent i = new Intent(LoginRelato.this,MenuCustomizeNow.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
        mProgress.dismiss();
    }

    public void showSnackBar(String msg) {
        Snackbar
                .make(findViewById(R.id.loginRelato), msg, Snackbar.LENGTH_SHORT)
                .show();
    }

}
