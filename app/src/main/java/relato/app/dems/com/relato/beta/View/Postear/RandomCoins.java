package relato.app.dems.com.relato.beta.View.Postear;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import relato.app.dems.com.relato.beta.R;
import relato.app.dems.com.relato.beta.Service.Sounds.BackgroundSoundService;
import relato.app.dems.com.relato.beta.Service.Sounds.Random_start_game;

public class RandomCoins extends AppCompatActivity {
    private SharedPreferences monedas = null;
    private TextInputEditText mRandom_apuesta;
    private TextView mRandom_ganaste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_coins);

        monedas = getSharedPreferences("relato.app.dems.com.relato.beta", MODE_PRIVATE);

        mRandom_apuesta = (TextInputEditText) findViewById(R.id.random_apuesta);
        mRandom_ganaste = (TextView) findViewById(R.id.random_ganaste);





        final ImageView img = (ImageView) findViewById(R.id.duplicarMoneda);
        final Animation an = AnimationUtils.loadAnimation(getBaseContext(),R.anim.rotar_rueda);

        final TextView saldo_all = (TextView) findViewById(R.id.saldo_all);


        final int allItem = monedas.getInt("valorGuardadoTest", -1);
        //int coinsDesc = allItem - 5;

       // monedas.edit().putInt("valorGuardadoTest", coinsDesc).apply();

        saldo_all.setText("Saldo Actual: "+allItem);


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img.startAnimation(an);
                 int mRandom_apuesta_val = Integer.parseInt(mRandom_apuesta.getText().toString());

                final int  numero = (int) (Math.random() *mRandom_apuesta_val) + 1;
                Log.v("aksdh",""+allItem+numero+mRandom_apuesta_val);

                an.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        Intent svc = new Intent(getApplicationContext(), Random_start_game.class);
                        startService(svc);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent svc = new Intent(getApplicationContext(), Random_start_game.class);
                        stopService(svc);
                        saldo_all.setText("Saldo Actual: "+allItem+" "+numero);
                        mRandom_ganaste.setText("Ganaste: "+numero);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });


    }
}
