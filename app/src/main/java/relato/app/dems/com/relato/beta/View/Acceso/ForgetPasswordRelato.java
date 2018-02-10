package relato.app.dems.com.relato.beta.View.Acceso;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import relato.app.dems.com.relato.beta.R;
import relato.app.dems.com.relato.beta.View.Util.ValidarEmail;

public class ForgetPasswordRelato extends AppCompatActivity {
    private FirebaseAuth mAuth;
    public TextView title;
    private FrameLayout mForgetPasswordCuenta;
    private TextInputEditText mForget_user_email;
    private String display_email;

    private ProgressDialog mProgress;
    private static Typeface Pacifico,Nightmare,Double,BloodLust;


    /*Animación*/
    private Animation mUp_to_down,mhide_to_bottom;
    private LinearLayout mRelato_forget_body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_relato);

        mRelato_forget_body = (LinearLayout) findViewById(R.id.relato_forget_body);

        title = (TextView) findViewById(R.id.forget_title_sangrienta_lectura);
        String text = "<font color='#da152c'>Reiniciar</font> Password";
        Log.v("textq1",text);
        title.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

         mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(this);

        String bloodLustFuente= "fuentes/BloodLust.ttf";
        String nightmareFuente= "fuentes/Nigh.ttf";
        this.BloodLust = Typeface.createFromAsset(getAssets(),bloodLustFuente);
        this.Nightmare = Typeface.createFromAsset(getAssets(),nightmareFuente);
        title.setTypeface(BloodLust);

        mForget_user_email = (TextInputEditText) findViewById(R.id.forget_user_email);

        mForgetPasswordCuenta = (FrameLayout) findViewById(R.id.forgetPasswordCuenta);

        mUp_to_down = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.up_to_down);
        mhide_to_bottom = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.down_to_up);

        title.setAnimation(mUp_to_down);
        mRelato_forget_body.setAnimation(mUp_to_down);
        mForgetPasswordCuenta.setAnimation(mhide_to_bottom);


        mForgetPasswordCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validarEmail(display_email)){
                    showSnackBar("Email no válido.");
                    mForget_user_email.setError("Email no válido.");
                }else{
                    mProgress.setTitle("Reiniciando Contraseña");
                    mProgress.setMessage("reiniciando su contraseña" );
                    mProgress.setCancelable(false);
                    mProgress.show();
                    display_email = mForget_user_email.getText().toString();

                    mAuth.sendPasswordResetEmail(display_email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        mProgress.hide();
                                        Log.d("estado", "Email sent."+display_email);
                                        showSnackBar("Hemos enviado un enlace a su correo "+display_email);
                                        finish();
                                    }
                                }
                            });
                }

            }
        });


    }

    private boolean validarEmail(String display_email) {
        ValidarEmail check = new ValidarEmail(getApplicationContext());
        boolean booleamCheckEmail = check.checkEmail(display_email);
        Log.v("checkEmail",""+booleamCheckEmail);
        return booleamCheckEmail;
    }


    public void showSnackBar(String msg) {
        Snackbar
                .make(findViewById(R.id.forgetRelato), msg, Snackbar.LENGTH_SHORT)
                .show();
    }
}
