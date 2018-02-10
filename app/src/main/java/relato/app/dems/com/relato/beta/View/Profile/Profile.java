package relato.app.dems.com.relato.beta.View.Profile;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.squareup.picasso.Picasso;

import relato.app.dems.com.relato.beta.MenuCustomizeNow;

import relato.app.dems.com.relato.beta.Model.ItemFeed;
import relato.app.dems.com.relato.beta.R;
import relato.app.dems.com.relato.beta.View.Acceso.AccessRelato;
import relato.app.dems.com.relato.beta.View.Details.DetailsRelato;
import relato.app.dems.com.relato.beta.View.Util.ViewHolder.RelatoViewHolderStructure;

public class Profile extends AppCompatActivity  {

    private TextView mNameProfile;
    private TextView mEmailProfile;
    private ImageView mPhotoProfile;

    private ProgressDialog mProgress;


    private RecyclerView mRecyclerMisLecturas;
    private DatabaseReference mDatabaseMisLecturas;



    /*Anucnios*/
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_details_uno);


        mDatabaseMisLecturas = FirebaseDatabase.getInstance().getReference().child("Historias");
        mDatabaseMisLecturas.keepSynced(true);

        LinearLayoutManager layoutManagerMisLecturas
                = new LinearLayoutManager(Profile.this, LinearLayoutManager.VERTICAL, false);

        layoutManagerMisLecturas.setReverseLayout(true);
        layoutManagerMisLecturas.setStackFromEnd(true);

        mRecyclerMisLecturas = (RecyclerView) findViewById(R.id.fragmento_mis_lecturas);
        mRecyclerMisLecturas.setHasFixedSize(true);

        mRecyclerMisLecturas.setLayoutManager(layoutManagerMisLecturas);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        Query queryRef = mDatabaseMisLecturas.orderByChild("IdMiLectura").equalTo(userId);


        FirebaseRecyclerAdapter<ItemFeed, RelatoViewHolderStructure>
                firebaseRecyclerAdapterMyLecturas = new FirebaseRecyclerAdapter<ItemFeed, RelatoViewHolderStructure>(
                ItemFeed.class,
                R.layout.design_structure_relato_menu,
                RelatoViewHolderStructure.class,queryRef) {
            @Override
            protected void populateViewHolder(RelatoViewHolderStructure viewHolder, ItemFeed model, final int position) {
                final String post_key = getRef(position).getKey();
                viewHolder.setTitle(model.getTitle());
                viewHolder.setCatergory(model.getCategory());
                viewHolder.setAuthor(model.getAuthor());

                viewHolder.setImage(Profile.this, model.getImage());


                viewHolder.mViewStructure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mProgress.setMessage("Accediendo...");
                        // mProgress.show();
                        //Toast.makeText(getContext(),"dale "+post_key,Toast.LENGTH_SHORT).show();
                        Intent singleBlogIntent = new Intent(Profile.this, DetailsRelato.class);
                        singleBlogIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        singleBlogIntent.putExtra("blog_id", post_key);
                        startActivity(singleBlogIntent);

                        Log.v("id","id"+post_key);
                    }
                });
            }
        };

        mRecyclerMisLecturas.setAdapter(firebaseRecyclerAdapterMyLecturas);



        mPhotoProfile = (ImageView) findViewById(R.id.photoProfile);

        mNameProfile = (TextView) findViewById(R.id.nameProfile);
        mEmailProfile = (TextView) findViewById(R.id.emailProfile);

        mProgress = new ProgressDialog(this);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-2031757066481790/6047399845");
        //mInterstitialAd.setAdUnitId("ca-app-pub-2031757066481790/5186336245");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            Log.v("Anuncio","click");
        }


        showToolbar("asd",true);
        String mNameProfileSet = user.getDisplayName();
        String mEmailProfileSet = user.getUid();
        Uri mPhotoUrlProfileSet = user.getPhotoUrl();

        if(user != null) {


            mNameProfile.setText(mNameProfileSet);
            mEmailProfile.setText(mEmailProfileSet);

            Glide.with(Profile.this)
                    .load(mPhotoUrlProfileSet)
                    .thumbnail(Glide.with(Profile.this).load(R.drawable.item_placeholder))
                    .into(mPhotoProfile);
        }


        if(mNameProfileSet==null){

            DatabaseReference mDatabaseUser;
            mDatabaseUser = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("Users");
            mDatabaseUser.keepSynced(true);

            mDatabaseUser.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String post_image = (String) dataSnapshot.child("image").getValue();
                    String name = (String) dataSnapshot.child("name").getValue();

                    Log.v("estado","estado"+post_image+name);

                    Glide.with(Profile.this)
                            .load(post_image)
                            .thumbnail(Glide.with(Profile.this).load(R.drawable.item_placeholder))
                            .into(mPhotoProfile);

                    mNameProfile.setText(name);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

        }

    }

    public void cerrarSesion(View v){
        LoginManager.getInstance().logOut();
        FirebaseAuth.getInstance().signOut();

        mProgress.setTitle("Cerrando Sessión...");
        mProgress.setMessage("Eliminando Credenciales.");
        mProgress.setCancelable(false);
        mProgress.show();

        needAccess();


    }

    @Override
    protected void onPause() {
        mProgress.dismiss();
        super.onPause();
    }

    public void needAccess(){
        Intent i = new Intent(Profile.this,AccessRelato.class);
        finish();
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }

    public void showToolbar(String tittle, boolean upButton) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }



}
