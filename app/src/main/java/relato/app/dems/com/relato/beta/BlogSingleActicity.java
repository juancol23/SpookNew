package relato.app.dems.com.relato.beta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import relato.app.dems.com.relato.beta.Front.FrontDetails;

public class BlogSingleActicity extends AppCompatActivity implements
        TextToSpeech.OnInitListener{
    private String mPost_key = null;
    private DatabaseReference mDatabase,mMensajeShare;
    private ImageView mImage_paralax;
    private TextView mPostTitleDetails,mPostDescDetails;
    private Button mPostRemoveDetails;
    private ProgressDialog mProgresDialog;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_single_acticity);

        tts = new TextToSpeech(this, this);




        mPostTitleDetails = (TextView) findViewById(R.id.postTitleDetails);
        mPostDescDetails = (TextView) findViewById(R.id.postDescDetails);
        mImage_paralax = (ImageView) findViewById(R.id.image_paralax);
        mPostRemoveDetails = (Button) findViewById(R.id.postRemoveDetails);
        mProgresDialog= new ProgressDialog(this);





        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        mPost_key = getIntent().getExtras().getString("blog_id");



         mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_desc = (String) dataSnapshot.child("desc").getValue();
                String post_image = (String) dataSnapshot.child("image").getValue();
                //String post_uid = (String) dataSnapshot.child("uid").getValue();
               // Toast.makeText(BlogSingleActicity.this,""+post_title+post_desc+post_image,Toast.LENGTH_SHORT).show();

                mPostTitleDetails.setText(post_title);
                mPostDescDetails.setText(post_desc);
                Glide.with(BlogSingleActicity.this)
                        .load(post_image)
                        .into(mImage_paralax);


                setToolbar(post_title);
                if (getSupportActionBar() != null) // Habilitar up button
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                startActivity(new Intent(BlogSingleActicity.this,Feed.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                mProgresDialog.dismiss();
                finish();

            }
        });

        /*
        Toast.makeText(BlogSingleActicity.this,""+mPost_key,Toast.LENGTH_SHORT).show();
        Log.v("clicks","click"+mPost_key);*/


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

    }


    public void validarAudio(View v){

        mPostDescDetails = (TextView) findViewById(R.id.postDescDetails);

        String text = mPostDescDetails.getText().toString();
        int numero = text.length();

        if(numero < 4000){

            showSnackBar("El Audio Comenzará en Breve");

            speakOut();

        }else{

            showSnackBar("Audio no disponible");

        }

    }

    private void showSnackBar(String msg) {
        Snackbar
                .make(findViewById(R.id.coordinator), msg, Snackbar.LENGTH_LONG)
                .show();
    }

    public void share(View v) {

        mMensajeShare = (DatabaseReference) FirebaseDatabase.getInstance().getReference();
        mMensajeShare.keepSynced(true);

        mMensajeShare.child("Mensaje").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String share = (String) dataSnapshot.child("share").getValue();

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        ""+share);
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
/*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //this.moveTaskToBack(true);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/

    private void setToolbar(String title) {
        // Añadir la Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
    }
}
