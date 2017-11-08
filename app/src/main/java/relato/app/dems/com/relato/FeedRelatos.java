package relato.app.dems.com.relato;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FeedRelatos extends AppCompatActivity {

    private RecyclerView mBlogList;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseHeader;
    private SwipeRefreshLayout swipeRefreshLayout;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_relatos);

        mInterstitialAd = new InterstitialAd(this);
        //mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.setAdUnitId("ca-app-pub-2031757066481790/6993553573");

        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        setToolbar();




        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        mDatabase.keepSynced(true);

        mDatabaseHeader = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("Blog");
        mDatabaseHeader.keepSynced(true);

        mBlogList = (RecyclerView) findViewById(R.id.feed_relatos_list);
        mBlogList.setHasFixedSize(true);

        mDatabaseHeader.child("header").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String post_image = (String) dataSnapshot.child("image").getValue();

                ImageView image_paralax_header_relato = (ImageView) findViewById(R.id.image_paralax_header_relato);

                image_paralax_header_relato.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                Glide.with(FeedRelatos.this)
                        .load(post_image)
                        .into(image_paralax_header_relato);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

      //  mBlogList.setLayoutManager(new LinearLayoutManager(this));

        //GridLayoutManager mLayout = new GridLayoutManager(this,3);
        //GridLayoutManager mLayout = new GridLayoutManager(FeedRelatos.this,3);
        //mLayout.setReverseLayout(true);
        //mLayout.setStackFromEnd(true);
        mBlogList.setLayoutManager(new GridLayoutManager(this,3,LinearLayoutManager.VERTICAL, true));




        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setSize(8);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                FirebaseRecyclerAdapter<ItemFeed,FeedRelatos.BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ItemFeed, FeedRelatos.BlogViewHolder>(
                        ItemFeed.class,
                        R.layout.item_recycler_design,
                        FeedRelatos.BlogViewHolder.class,
                        mDatabase

                ) {
                    @Override
                    protected void populateViewHolder(BlogViewHolder viewHolder, ItemFeed model, int position) {
                        final String post_key = getRef(position).getKey();

                        viewHolder.setTitle(model.getTitle());
                        viewHolder.setDesc(model.getDesc());
                        viewHolder.setImage(getApplicationContext(),model.getImage());
                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (mInterstitialAd.isLoaded()) {
                                    mInterstitialAd.show();
                                } else {
                                    Intent singleBlogIntent = new Intent(FeedRelatos.this,BlogSingleActicity.class);
                                    singleBlogIntent.putExtra("blog_id", post_key);
                                    startActivity(singleBlogIntent);
                                }


                            }
                        });

                    }

                };

                mBlogList.setAdapter(firebaseRecyclerAdapter);
            }



        });



    }

    @Override
    protected void onStart() {
        FirebaseRecyclerAdapter<ItemFeed,FeedRelatos.BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ItemFeed, FeedRelatos.BlogViewHolder>(
                ItemFeed.class,
                R.layout.item_recycler_design,
                FeedRelatos.BlogViewHolder.class,
                mDatabase

        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, ItemFeed model, int position) {
                final String post_key = getRef(position).getKey();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext(),model.getImage());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent singleBlogIntent = new Intent(FeedRelatos.this,BlogSingleActicity.class);
                        singleBlogIntent.putExtra("blog_id", post_key);
                        startActivity(singleBlogIntent);
                    }
                });

            }

        };

        mBlogList.setAdapter(firebaseRecyclerAdapter);

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
        super.onStart();
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder{

        View mView;
        TextView mItem_recycler_design_title;


        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView ;

        }
        public void setTitle(String title){
            //mItem_recycler_design_title.setTypeface(Pacifico);
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
                    .into(post_image);

        }
    }

    private void setToolbar() {
        // Añadir la Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Relatos");
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
