package relato.app.dems.com.relato.beta.View.Details.Front;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import relato.app.dems.com.relato.beta.Service.Sounds.BackgroundSoundService;
import relato.app.dems.com.relato.beta.View.FeedRelatos;
import relato.app.dems.com.relato.beta.R;


public class FrontDetails extends AppCompatActivity implements
        TextToSpeech.OnInitListener{
    private DatabaseReference mDatabaseHeader,mMensajeShare;
    private TextView mPostTitleDetails,mPostDescDetails;
    private FloatingActionButton m;
    private TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_details);
        tts = new TextToSpeech(this, this);

        FloatingActionMenu a = (FloatingActionMenu) findViewById(R.id.menu_floatingAction);
        a.setVisibility(View.VISIBLE);

        mPostTitleDetails = (TextView) findViewById(R.id.postTitleDetails);
        mPostDescDetails = (TextView) findViewById(R.id.postDescDetails);


        mDatabaseHeader = FirebaseDatabase.getInstance().getReference().child("Blog");
        mDatabaseHeader.keepSynced(true);

        mDatabaseHeader.child("portada").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_desc = (String) dataSnapshot.child("desc").getValue();
                String post_image = (String) dataSnapshot.child("images").getValue();

                ImageView image_paralax = (ImageView) findViewById(R.id.front_image_paralax);
                Log.v("imagen","imagen"+post_image);
                mPostTitleDetails.setText(post_title);
                mPostDescDetails.setText(post_desc);

                //validarVisibilidadAudio();
                Glide.with(FrontDetails.this)
                        .load(post_image)
                        .into(image_paralax);

                setToolbar(post_title);
                if (getSupportActionBar() != null) // Habilitar up button
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {

        android.support.design.widget.FloatingActionButton mFabPlay =(android.support.design.widget.FloatingActionButton)  findViewById(R.id.fabPlay);
        android.support.design.widget.FloatingActionButton mFabPause =(android.support.design.widget.FloatingActionButton)  findViewById(R.id.fabPause);
        //mFabPlay.setVisibility(View.VISIBLE);
        mFabPause.setVisibility(View.GONE);
        super.onResume();
    }

    @Override
    protected void onPause() {
        tts.playSilence(100, TextToSpeech.QUEUE_FLUSH,null);

        super.onPause();
    }

    public void validarAudio(View v){

        mPostDescDetails = (TextView) findViewById(R.id.postDescDetails);

        String text = mPostDescDetails.getText().toString();
        int numero = text.length();

        if(numero < 4000){

            showSnackBar("El Audio Comenzará en Breve");

            speakOut();

            android.support.design.widget.FloatingActionButton mFabPlay = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fabPlay);
            android.support.design.widget.FloatingActionButton mFabPause =(android.support.design.widget.FloatingActionButton)  findViewById(R.id.fabPause);
            mFabPlay.setVisibility(View.GONE);
           // mFabPause.setVisibility(View.VISIBLE);


        }else{

            showSnackBar("Audio no disponible para este cuento");

        }

    }

    public void onClickPause(View v) {
        android.support.design.widget.FloatingActionButton mFabPlay = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fabPlay);
        android.support.design.widget.FloatingActionButton mFabPause =(android.support.design.widget.FloatingActionButton)  findViewById(R.id.fabPause);
        //mFabPlay.setVisibility(View.VISIBLE);
        mFabPause.setVisibility(View.GONE);

        tts.playSilence(100, TextToSpeech.QUEUE_FLUSH,null);
    }

    private void showSnackBar(String msg) {
        Snackbar
                .make(findViewById(R.id.coordinator), msg, Snackbar.LENGTH_LONG)
                .show();
    }


    @Override
    public void onDestroy(){
        // Don't forget to shutdown!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        // TODO Auto-generated method stub

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(new Locale("es","US"));

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language is not supported");
            } else {
                Log.e("TTS", "Language is  supported");
                mPostDescDetails = (TextView) findViewById(R.id.postDescDetails);
                String text = mPostDescDetails.getText().toString();

                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                tts.playSilence(100, TextToSpeech.QUEUE_FLUSH,null);

            }

        } else {
            Log.e("TTS", "Initilization Failed");
        }
    }

    private void speakOut(){

        mPostDescDetails = (TextView) findViewById(R.id.postDescDetails);
        String text = mPostDescDetails.getText().toString();
        int numero = text.length();

        Log.v("TTSS",""+numero);
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        Log.e("TTS", ""+text);

        stopService(new Intent(this, BackgroundSoundService.class));


    }

    public void scoreFront(View v) {
        Intent intent1 = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id="
                        + FrontDetails.this.getPackageName()));
        startActivity(intent1);
    }

    public void share(View v) {

        mMensajeShare = (DatabaseReference) FirebaseDatabase.getInstance().getReference();
        mMensajeShare.keepSynced(true);

        mMensajeShare.child("Message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String texto = (String) dataSnapshot.child("text").getValue();
                String link = (String) dataSnapshot.child("link").getValue();

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                //sendIntent.putExtra(Intent.EXTRA_TEXT,texto+" "+link);
                sendIntent.putExtra(Intent.EXTRA_TEXT,"Esta app te hará sufrir un infarto con sus Sangrientas Lecturas, Descargala YA!! https://play.google.com/store/apps/details?id=relato.app.dems.com.relato.beta");

                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void letraMas(View v) {
        mPostTitleDetails = (TextView) findViewById(R.id.postTitleDetails);
        mPostDescDetails = (TextView) findViewById(R.id.postDescDetails);

        int valorLetraDes = (int) mPostDescDetails.getTextSize();

        if(valorLetraDes < 38){
            int aumentar = valorLetraDes +10;
            Log.v("tts","tt"+valorLetraDes);
            mPostTitleDetails.setTextSize(aumentar);
            mPostDescDetails.setTextSize(TypedValue.COMPLEX_UNIT_SP,mPostDescDetails.getTextSize()+1);
         }
    }

    public void letraMenos(View v){
       /* mPostTitleDetails = (TextView) findViewById(R.id.postTitleDetails);
        mPostDescDetails = (TextView) findViewById(R.id.postDescDetails);

        int valorLetra = (int) mPostDescDetails.getTextSize();

        if(valorLetra > 14) {

            Log.v("tts", "tt" + valorLetra);
            mPostDescDetails.setTextSize(TypedValue.COMPLEX_UNIT_SP,mPostDescDetails.getTextSize()-1);
        }*/
    }
    private void setToolbar(String titulo) {
        // Añadir la Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(titulo);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //this.moveTaskToBack(true);
            Intent i = new Intent(FrontDetails.this,FeedRelatos.class);
            finish();
            startActivity(i);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void validarVisibilidadAudio(){
        mPostDescDetails = (TextView) findViewById(R.id.postDescDetails);

        String text = mPostDescDetails.getText().toString();
        int numero = text.length();
        android.support.design.widget.FloatingActionButton mFabPlay = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fabPlay);

        if(numero > 4000){
            showSnackBar("Audio no disponible para este Relato");
            mFabPlay.setVisibility(View.GONE);

        }else{
            mFabPlay.setVisibility(View.VISIBLE);

        }

    }

}
