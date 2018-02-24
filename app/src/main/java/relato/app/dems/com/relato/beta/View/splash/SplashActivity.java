package relato.app.dems.com.relato.beta.View.splash;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import relato.app.dems.com.relato.beta.MenuCustomizeNow;
import relato.app.dems.com.relato.beta.R;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.splash_text_spook)
    TextView mSplash_text_spook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        String Fuente_Rutas = "fuentes/Feast.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), Fuente_Rutas);

        mSplash_text_spook.setTypeface(tf);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                   /* startActivity(new Intent(Splash.this, Main_Activity_Fragment.class));*/
                startActivity(new Intent(SplashActivity.this, MenuCustomizeNow.class));
                finish();
            }
        }, 100);




    }
}
