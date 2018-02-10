package relato.app.dems.com.relato.beta.View.Details;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.Locale;

import relato.app.dems.com.relato.beta.R;
import relato.app.dems.com.relato.beta.Service.Sounds.BackgroundSoundService;
import relato.app.dems.com.relato.beta.View.FeedRelatos;

import static com.facebook.FacebookSdk.getApplicationContext;

public class BlogSingleActicity extends AppCompatActivity implements
        TextToSpeech.OnInitListener{
    private String mPost_key = null;
    private DatabaseReference mDatabase,mMensajeShare;
    private ImageView mImage_paralax;
    private TextView mPostTitleDetails,mPostDescDetails;
    private Button mPostRemoveDetails;
    private ProgressDialog mProgresDialog;
    private TextToSpeech tts;
    private MediaPlayer player;
    private InterstitialAd mInterstitialAd;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_single_acticity);

        tts = new TextToSpeech(this, this);

        MobileAds.initialize(this,"ca-app-pub-2031757066481790/4821023989");


        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-2031757066481790/2003288956");
        // mInterstitialAd.setAdUnitId("ca-app-pub-2031757066481790/5186336245");

        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            Log.v("Anuncio","click");
        }

        mPostTitleDetails = (TextView) findViewById(R.id.postTitleDetails);
        mPostDescDetails = (TextView) findViewById(R.id.postDescDetails);
        mImage_paralax = (ImageView) findViewById(R.id.image_paralax);
        mPostRemoveDetails = (Button) findViewById(R.id.postRemoveDetails);
        mProgresDialog= new ProgressDialog(this);




        mDatabase = FirebaseDatabase.getInstance().getReference().child("Audios");
        mPost_key = getIntent().getExtras().getString("blog_id");



         mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_desc = (String) dataSnapshot.child("desc").getValue();
                String post_image = (String) dataSnapshot.child("image").getValue();


                final String name_audio = (String) dataSnapshot.child("name_audio").getValue();
                final String name_extension= (String) dataSnapshot.child("name_extension").getValue();


                 FloatingActionButton mFabPlay = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fabPlay);
                 FloatingActionButton mFabPause = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fabPause);
                 mFabPlay.setVisibility(View.VISIBLE);
                 mFabPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        FloatingActionButton mFabPlay = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fabPlay);
                        FloatingActionButton mFabPause = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fabPause);

                        mFabPlay.setVisibility(View.GONE);
                        mFabPause.setVisibility(View.VISIBLE);

                        loadSourcePlay(name_audio,name_extension);

                        Log.v("fileasd","asdasd"+name_audio+name_extension);
                        //Log.v("fileasd","asdasd"+nameSpace+nameExtension);

                    }
                });

                mFabPause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        FloatingActionButton mFabPlay = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fabPlay);
                        FloatingActionButton mFabPause = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fabPause);

                        mFabPlay.setVisibility(View.VISIBLE);
                        mFabPause.setVisibility(View.GONE);

                        loadSourcePause(name_audio,name_extension);



                        Log.v("fileasd","asdasd"+name_audio+name_extension);
                        //Log.v("fileasd","asdasd"+nameSpace+nameExtension);

                    }
                });



                Log.v("oeoeoe","oeoe"+name_audio+name_extension);


                mPostTitleDetails.setText(post_title);
                mPostDescDetails.setText(post_desc);

                //validarVisibilidadAudio();
                Glide.with(getApplicationContext())
                        .load(post_image)
                        .into(mImage_paralax);


                setToolbar(post_title);
                if (getSupportActionBar() != null) // Habilitar up button
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mPostRemoveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgresDialog.setMessage("Posteando al Blog");
                mProgresDialog.show();
                mDatabase.child(mPost_key).removeValue();
                startActivity(new Intent(getApplicationContext(),FeedRelatos.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                mProgresDialog.dismiss();
                finish();

            }
        });

        /*
        Toast.makeText(BlogSingleActicity.this,""+mPost_key,Toast.LENGTH_SHORT).show();
        Log.v("clicks","click"+mPost_key);*/


    }

    public void loadSourcePlay(String name_audio, String name_extension) {

        File folder = new  File( Environment
                .getExternalStorageDirectory()
                .getPath()+"/Android/data/"
                +getApplicationContext()
                .getPackageName()
                +"/files/sdcard/DirName/"+name_audio+"/");

        File output_file = new File(folder, name_extension);
        String path = output_file.toString();
        //imageView.setImageDrawable(Drawable.createFromPath(path));
        MediaPlayer player;
        player = MediaPlayer.create(getApplicationContext(), Uri.parse(path));
        Log.i("playy", "Path: " + path);


        player.start();
    }

    public void loadSourcePause(String name_audio, String name_extension) {



        File folder = new  File( Environment
                .getExternalStorageDirectory()
                .getPath()+"/Android/data/"
                +getApplicationContext()
                .getPackageName()
                +"/files/sdcard/DirName/"+name_audio+"/");

        File output_file = new File(folder, name_extension);
        String path = output_file.toString();
        MediaPlayer player;
        player = MediaPlayer.create(getApplicationContext(), Uri.parse(path));
        Log.i("playy", "Path: " + path);


        player.stop();
        player.release();
    }


    @Override
    public void onDestroy(){
        // Don't forget to shutdown!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            Log.v("Anuncio","click");
        }

        super.onDestroy();
    }

    @Override
    protected void onStop() {
        tts.playSilence(100, TextToSpeech.QUEUE_FLUSH,null);
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            Log.v("Anuncio","click");
        }
        super.onStop();
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

        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        Log.e("TTS", ""+text);
        stopService(new Intent(this, BackgroundSoundService.class));

    }

    @Override
    protected void onResume() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            Log.v("Anuncio","click");
        }
        android.support.design.widget.FloatingActionButton mFabPlay =(android.support.design.widget.FloatingActionButton)  findViewById(R.id.fabPlay);
        android.support.design.widget.FloatingActionButton mFabPause =(android.support.design.widget.FloatingActionButton)  findViewById(R.id.fabPause);
        //mFabPlay.setVisibility(View.VISIBLE);
        mFabPause.setVisibility(View.GONE);
        super.onResume();
    }

    @Override
    protected void onPause() {
        finish();
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
            //mFabPause.setVisibility(View.VISIBLE);


        }else{

            showSnackBar("Audio no disponible para este cuento");

        }

    }

    public void onClickPause(View v) {
        android.support.design.widget.FloatingActionButton mFabPlay = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fabPlay);
        android.support.design.widget.FloatingActionButton mFabPause =(android.support.design.widget.FloatingActionButton)  findViewById(R.id.fabPause);
       // mFabPlay.setVisibility(View.VISIBLE);
        mFabPause.setVisibility(View.GONE);

        tts.playSilence(100, TextToSpeech.QUEUE_FLUSH,null);
    }

    private void showSnackBar(String msg) {
        Snackbar
                .make(findViewById(R.id.coordinator), msg, Snackbar.LENGTH_LONG)
                .show();
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
               // sendIntent.putExtra(Intent.EXTRA_TEXT,texto+" "+link);
                sendIntent.putExtra(Intent.EXTRA_TEXT,"Esta app te hará sufrir un infarto con sus Sangrientas Lecturas, Descargala YA!! https://play.google.com/store/apps/details?id=relato.app.dems.com.relato.beta");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }




    public void scoreBlogDetails(View v) {
        Intent intent1 = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id="
                        + BlogSingleActicity.this.getPackageName()));
        startActivity(intent1);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //this.moveTaskToBack(true);
          /*  Intent i = new Intent(BlogSingleActicity.this,FeedRelatos.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(i);*/
            finish();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setToolbar(String title) {
        // Añadir la Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
    }

    public void showToolbar(String tittle, boolean upButton) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
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

            //mFabPlay.setVisibility(View.VISIBLE);

        }

    }
}
