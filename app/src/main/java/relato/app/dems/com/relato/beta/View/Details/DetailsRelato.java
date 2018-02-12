package relato.app.dems.com.relato.beta.View.Details;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import relato.app.dems.com.relato.beta.MenuCustomizeNow;
import relato.app.dems.com.relato.beta.R;
import relato.app.dems.com.relato.beta.Service.Sounds.BackgroundSoundService;
import relato.app.dems.com.relato.beta.View.FeedRelatos;

/**
 * Created by CORAIMA on 21/11/2017.
 */

public class DetailsRelato extends AppCompatActivity implements
        TextToSpeech.OnInitListener{
    private String mPost_key = null;
    private DatabaseReference mDatabase,mMensajeShare;
    private ImageView mImage_paralax;
    private TextView mPostTitleDetails,mPostDescDetails;
    private ProgressDialog mProgresDialog;
    private TextToSpeech tts;
    private SharedPreferences prefs = null;
    private InterstitialAd mInterstitialAd;


    private AdView mAdView;


    @BindView(R.id.postRemoveDetails) Button mPostRemoveDetails;

    private FirebaseAuth mAuth;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_single_activity2);
        ButterKnife.bind(this);

        MobileAds.initialize(this,"ca-app-pub-2031757066481790/4821023989");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-2031757066481790/2003288956");
        // mInterstitialAd.setAdUnitId("ca-app-pub-2031757066481790/5186336245");

        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            Log.v("Anuncio","click");
        }

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        /*Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.WHITE); }*/


        final FloatingActionMenu fab = (FloatingActionMenu) findViewById(R.id.menu_floatingAction);
       // fab.setVisibility(View.VISIBLE);

        NestedScrollView nsv = (NestedScrollView) findViewById(R.id.scroll);
        nsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    fab.setVisibility(View.GONE);

                } else {
                    fab.setVisibility(View.VISIBLE);

                }
            }
        });



        tts = new TextToSpeech(this, this);



        prefs = getSharedPreferences("relato.app.dems.com.relato.beta", MODE_PRIVATE);


        validadVisibilidadSonido();
        mPostTitleDetails = (TextView) findViewById(R.id.postTitleDetails);
        mPostDescDetails = (TextView) findViewById(R.id.postDescDetails);
        mImage_paralax = (ImageView) findViewById(R.id.image_paralax);
        mProgresDialog= new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Historias");
        mPost_key = getIntent().getExtras().getString("blog_id");


        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_desc = (String) dataSnapshot.child("desc").getValue();
                String post_image = (String) dataSnapshot.child("image").getValue();
                String post_IdMiLectura = (String) dataSnapshot.child("IdMiLectura").getValue();
                // Toast.makeText(BlogSingleActicity.this,""+post_title+post_desc+post_image,Toast.LENGTH_SHORT).show();

                String textoCentradoDesc = post_desc;

                String text_string_center = "<html><body style='text-align:justify;'>"+textoCentradoDesc+"<body><html>";

                Log.v("asdasvtvrt",text_string_center);

                mPostDescDetails.setText(Html.fromHtml(text_string_center), TextView.BufferType.SPANNABLE);

                mPostTitleDetails.setText(post_title);
                //mPostDescDetails.setText(post_desc);

                //validarVisibilidadAudio();
                Glide.with(getApplicationContext())
                        .load(post_image)
                        .into(mImage_paralax);

                Log.v("post_all",""+post_image+post_title+post_desc);
                showToolbar(post_title,true);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                String userId = user.getUid();

                 if(user != null){
                     Log.v("okey","Logueado");
                     Log.v("okey",""+mAuth.getCurrentUser().getUid());
                     Log.v("okey",""+post_IdMiLectura);
                     Log.v("okey",""+userId);
                     if(mAuth.getCurrentUser().getUid().equals(post_IdMiLectura)){
                         Log.v("okey","asd"+mAuth.getCurrentUser().getUid());
                        mPostRemoveDetails.setVisibility(View.VISIBLE);
                    }

                 }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mPostRemoveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgresDialog.setMessage("Removiendo Historia");
                mProgresDialog.show();
                mDatabase.child(mPost_key).removeValue();
                startActivity(new Intent(getApplicationContext(),MenuCustomizeNow.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                mProgresDialog.dismiss();
                finish();

            }
        });

        /*
        Toast.makeText(BlogSingleActicity.this,""+mPost_key,Toast.LENGTH_SHORT).show();
        Log.v("clicks","click"+mPost_key);*/


    }

    @Override
    protected void onStart() {
        if (prefs.getBoolean("firstrun", true)) {
            Intent svc = new Intent(getApplicationContext(), BackgroundSoundService.class);
            startService(svc);

        }else{
            Intent svc = new Intent(getApplicationContext(), BackgroundSoundService.class);
            stopService(svc);
        }
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            Log.v("Anuncio","click");
        }
        super.onStart();
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
      //  stopService(new Intent(this, BackgroundSoundService.class));

    }

    @Override
    protected void onResume() {
        android.support.design.widget.FloatingActionButton mFabPlay =(android.support.design.widget.FloatingActionButton)  findViewById(R.id.fabPlay);
        android.support.design.widget.FloatingActionButton mFabPause =(android.support.design.widget.FloatingActionButton)  findViewById(R.id.fabPause);
        //mFabPlay.setVisibility(View.VISIBLE);
        mFabPause.setVisibility(View.GONE);
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            Log.v("Anuncio","click");
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            Log.v("Anuncio","click");
        }
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
                        + getApplicationContext().getPackageName()));
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void showToolbar(String tittle, boolean upButton) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onclickFabStatusMusic(View v) {

        validadDisponibilidadSonido();
    }




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void validadDisponibilidadSonido(){
        if (prefs.getBoolean("firstrun", true)) {

            prefs.edit().putBoolean("firstrun", false).commit();

            FloatingActionButton mFabStatusMusic = (FloatingActionButton) findViewById(R.id.fabStatusMusic);

            mFabStatusMusic.setVisibility(View.VISIBLE);
            mFabStatusMusic.setImageDrawable(getDrawable(R.mipmap.ic_play));

            Intent svc = new Intent(getApplicationContext(), BackgroundSoundService.class);
            stopService(svc);


        }else{

            prefs.edit().putBoolean("firstrun", true).commit();

            FloatingActionButton mFabStatusMusic = (FloatingActionButton) findViewById(R.id.fabStatusMusic);

            mFabStatusMusic.setVisibility(View.VISIBLE);

            mFabStatusMusic.setImageDrawable(getDrawable(R.mipmap.ic_stop));

            Intent svc = new Intent(getApplicationContext(), BackgroundSoundService.class);
            startService(svc);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void validadVisibilidadSonido(){
        if (prefs.getBoolean("firstrun", true)) {


            FloatingActionButton mFabStatusMusic = (FloatingActionButton) findViewById(R.id.fabStatusMusic);

            mFabStatusMusic.setVisibility(View.VISIBLE);
            mFabStatusMusic.setImageDrawable(getDrawable(R.mipmap.ic_stop));


        }else{


            FloatingActionButton mFabStatusMusic = (FloatingActionButton) findViewById(R.id.fabStatusMusic);

            mFabStatusMusic.setVisibility(View.VISIBLE);

            mFabStatusMusic.setImageDrawable(getDrawable(R.mipmap.ic_play));

        }
    }



}
