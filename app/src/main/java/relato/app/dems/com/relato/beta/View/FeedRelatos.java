package relato.app.dems.com.relato.beta.View;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import relato.app.dems.com.relato.beta.View.Details.BlogSingleActicity;
import relato.app.dems.com.relato.beta.Model.ItemFeed;
import relato.app.dems.com.relato.beta.R;
import relato.app.dems.com.relato.beta.Service.Sounds.BackgroundSoundService;

public class FeedRelatos extends AppCompatActivity {

    private RecyclerView mBlogList;
    private RecyclerView mRecyclerTreding;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseHeader, mMensajeShare,mDatabaseTreding;
    private SwipeRefreshLayout swipeRefreshLayout;

    private InterstitialAd mInterstitialAd;
    private static Typeface Pacifico;


    private Dialog MyDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_relatos);

        Intent svc = new Intent(this, BackgroundSoundService.class);
        startService(svc);

        FloatingActionMenu a = (FloatingActionMenu) findViewById(R.id.menu_floatingAction);
        a.setVisibility(View.VISIBLE);

        String pacificoFuente= "fuentes/Pacifico.ttf";
        this.Pacifico = Typeface.createFromAsset(getAssets(),pacificoFuente);


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-2031757066481790/5186336245");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        setToolbar();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        mDatabase.keepSynced(true);

        mDatabaseHeader = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("Portada");
        mDatabaseHeader.keepSynced(true);

        mBlogList = (RecyclerView) findViewById(R.id.feed_relatos_list);
        mBlogList.setHasFixedSize(true);

        mDatabaseTreding = FirebaseDatabase.getInstance().getReference().child("Blog");
        mDatabaseTreding.keepSynced(true);

        mRecyclerTreding = (RecyclerView) findViewById(R.id.feed_relatos_list_treding);

        /*mDatabaseHeader.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String post_image = (String) dataSnapshot.child("images").getValue();

                ImageView image_paralax_header_relato = (ImageView) findViewById(R.id.image_paralax_header_relato);

                image_paralax_header_relato.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        } else {

                            Intent singleBlogIntent = new Intent(FeedRelatos.this, FrontDetails.class);
                            singleBlogIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            //finish();
                            startActivity(singleBlogIntent);

                        }
                    }
                });

                Glide.with(FeedRelatos.this)
                        .load(post_image)
                        .into(image_paralax_header_relato);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);


        LinearLayoutManager layoutManagerTrending
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        layoutManagerTrending.setReverseLayout(true);
        layoutManagerTrending.setStackFromEnd(true);

        mBlogList.setLayoutManager(layoutManager);

        mRecyclerTreding.setLayoutManager(layoutManagerTrending);

        FirebaseRecyclerAdapter<ItemFeed, FeedRelatos.BlogViewHolderTrending> firebaseRecyclerAdapterTreding = new FirebaseRecyclerAdapter<ItemFeed, FeedRelatos.BlogViewHolderTrending>(
                ItemFeed.class,
                R.layout.item_recycler_trending,
                FeedRelatos.BlogViewHolderTrending.class,
                mDatabase.limitToFirst(10)

        ) {
            @Override
            protected void populateViewHolder(BlogViewHolderTrending viewHolder, ItemFeed model, int position) {
                final String post_key = getRef(position).getKey();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext(), model.getImage());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*Intent singleBlogIntent = new Intent(FeedRelatos.this, BlogSingleActicity.class);
                        singleBlogIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        singleBlogIntent.putExtra("blog_id", post_key);
                        startActivity(singleBlogIntent);*/

                        MyCustomAlertDialog(post_key);
                    }
                });
            }
        };

        mRecyclerTreding.setAdapter(firebaseRecyclerAdapterTreding);

        FirebaseRecyclerAdapter<ItemFeed, FeedRelatos.BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ItemFeed, FeedRelatos.BlogViewHolder>(
                ItemFeed.class,
                R.layout.item_recycler_design,
                FeedRelatos.BlogViewHolder.class,
                mDatabase.limitToLast(10)

        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, ItemFeed model, int position) {
                final String post_key = getRef(position).getKey();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        } else {
                            /*Intent singleBlogIntent = new Intent(FeedRelatos.this, BlogSingleActicity.class);
                            singleBlogIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            singleBlogIntent.putExtra("blog_id", post_key);
                            //finish();
                            startActivity(singleBlogIntent);*/

                            MyCustomAlertDialog(post_key);
                        }
                    }
                });
            }
        };

        mBlogList.setAdapter(firebaseRecyclerAdapter);
        }


    //agregamos una alerta tipo bootstrap o materialize
    public void MyCustomAlertDialog(final String post_key){

        MyDialog = new Dialog(this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.activity_alert_popup);
        mDatabaseTreding = FirebaseDatabase.getInstance().getReference().child("Blog");
        mDatabaseTreding.keepSynced(true);

        Button mPopup_read = MyDialog.findViewById(R.id.popup_read);

        mDatabaseTreding.child(post_key).addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {

                 String popup_title = (String) dataSnapshot.child("title").getValue();
                 String popup_image = (String) dataSnapshot.child("image").getValue();
                 String popup_resumen = (String) dataSnapshot.child("desc").getValue();

                 ImageView mImage_popup = MyDialog.findViewById(R.id.popup_image);
                 TextView mTitle_popup = MyDialog.findViewById(R.id.popup_title);
                 TextView mResumen_popup = MyDialog.findViewById(R.id.popup_resumen);

                 mTitle_popup.setTypeface(Pacifico);

                 mTitle_popup.setText(popup_title);

                 //mResumen_popup.setText(popup_resumen.substring(0,100));
                 mResumen_popup.setText("asd");

                 Glide.with(FeedRelatos.this)
                         .load(popup_image)
                         .thumbnail(Glide.with(FeedRelatos.this).load(R.drawable.item_placeholder))
                         .into(mImage_popup);
             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }

        });

        mPopup_read.setEnabled(true);


        mPopup_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        MyDialog.dismiss();
                        Intent singleBlogIntent = new Intent(FeedRelatos.this, BlogSingleActicity.class);
                        singleBlogIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        singleBlogIntent.putExtra("blog_id", post_key);
                        startActivity(singleBlogIntent);
            }
        });



        MyDialog.show();

    }

    public void onClickCerrarPopup(View v){
        //v.setEnabled(true);
        MyDialog.dismiss();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //this.moveTaskToBack(true);
            finish();


            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void showSnackBar(String msg) {
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

    public void score(View v) {
        Intent intent1 = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id="
                        + FeedRelatos.this.getPackageName()));
        startActivity(intent1);
    }
    public void onClickOffMusic(View v) {
        FloatingActionButton mFabOffMusic = (FloatingActionButton) findViewById(R.id.fabOffMusic);
        FloatingActionButton mFabOnMusic = (FloatingActionButton) findViewById(R.id.fabOnMusic);
        stopService(new Intent(this, BackgroundSoundService.class));
        mFabOffMusic.setVisibility(View.GONE);
        mFabOnMusic.setVisibility(View.VISIBLE);

    }
    public void onClickOnMusic(View v) {
        FloatingActionButton mFabOffMusic = (FloatingActionButton) findViewById(R.id.fabOffMusic);
        FloatingActionButton mFabOnMusic = (FloatingActionButton) findViewById(R.id.fabOnMusic);
        startService(new Intent(this, BackgroundSoundService.class));
        mFabOffMusic.setVisibility(View.VISIBLE);
        mFabOnMusic.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        //stopService(new Intent(this, BackgroundSoundService.class));

        super.onPause();
    }

    @Override
    protected void onResume() {

        startService(new Intent(this, BackgroundSoundService.class));




        FirebaseRecyclerAdapter<ItemFeed, FeedRelatos.BlogViewHolderTrending> firebaseRecyclerAdapterTreding = new FirebaseRecyclerAdapter<ItemFeed, FeedRelatos.BlogViewHolderTrending>(
                ItemFeed.class,
                R.layout.item_recycler_trending,
                FeedRelatos.BlogViewHolderTrending.class,
                mDatabase.limitToFirst(17)

        ) {
            @Override
            protected void populateViewHolder(BlogViewHolderTrending viewHolder, ItemFeed model, int position) {
                final String post_key = getRef(position).getKey();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*Intent singleBlogIntent = new Intent(FeedRelatos.this, BlogSingleActicity.class);
                        singleBlogIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        singleBlogIntent.putExtra("blog_id", post_key);
                        startActivity(singleBlogIntent);*/
                        MyCustomAlertDialog(post_key);
                    }
                });
            }
        };

        mRecyclerTreding.setAdapter(firebaseRecyclerAdapterTreding);





        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }

        super.onResume();
    }

    @Override
    protected void onStart() {
        startService(new Intent(this, BackgroundSoundService.class));

        super.onStart();
    }

    @Override
    protected void onStop() {
        stopService(new Intent(this, BackgroundSoundService.class));
        super.onStop();
    }


    public static class BlogViewHolder extends RecyclerView.ViewHolder{

        View mView;
        TextView mItem_recycler_design_title;


        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView ;

        }
        public void setTitle(String title){

            mItem_recycler_design_title = mView.findViewById(R.id.item_recycler_design_title);

            mItem_recycler_design_title.setText(title);
        }
        public void setDesc(String desc){
            TextView mItem_recycler_design_desc = mView.findViewById(R.id.item_recycler_design_desc);
            mItem_recycler_design_desc.setText(desc);
        }
        public void setImage(Context context, String image){
            ImageView post_image = mView.findViewById(R.id.item_recycler_design_imagen);

            Glide.with(context)
                    .load(image)
                    .thumbnail(Glide.with(context).load(R.drawable.item_placeholder))
                    .into(post_image);

        }
    }


    public static class BlogViewHolderTrending extends RecyclerView.ViewHolder{

        View mView;
        TextView mItem_recycler_treding_title;


        public BlogViewHolderTrending(View itemView) {
            super(itemView);
            mView = itemView ;

        }
        public void setTitle(String title){
            //mItem_recycler_treding_title.setTypeface(Pacifico);
            mItem_recycler_treding_title = mView.findViewById(R.id.item_recycler_treding_title);

            mItem_recycler_treding_title.setText(title);
        }
        public void setDesc(String desc){
            TextView mItem_recycler_treding_desc = mView.findViewById(R.id.item_recycler_treding_desc);
            mItem_recycler_treding_desc.setText(desc);
        }
        public void setImage(Context context, String image){
            ImageView post_image = mView.findViewById(R.id.item_recycler_treding_imagen);

            Glide.with(context)
                    .load(image)
                    .thumbnail(Glide.with(context).load(R.drawable.item_placeholder))
                    .into(post_image);

        }
    }


    private void setToolbar() {
        // Añadir la Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Sangrienta Lectura");
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_add){
           // startActivity(new Intent(FeedRelatos.this,PostFeed.class));
        }
        return super.onOptionsItemSelected(item);
    }

}
