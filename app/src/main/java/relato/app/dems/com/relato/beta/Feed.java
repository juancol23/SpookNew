package relato.app.dems.com.relato.beta;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.nio.file.Files;
import java.util.Collections;

public class Feed extends AppCompatActivity {


    private RecyclerView mBlogList;
    private DatabaseReference mDatabase;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static Typeface Pacifico;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        setToolbar();

        String pacificoFuente= "fuentes/Pacifico.ttf";

        this.Pacifico = Typeface.createFromAsset(getAssets(),pacificoFuente);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        mDatabase.keepSynced(true);
        mBlogList = (RecyclerView) findViewById(R.id.blog_list);
        mBlogList.setHasFixedSize(true);
       // mBlogList.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager mLayout = new GridLayoutManager(Feed.this,3);
        mLayout.setReverseLayout(true);
        mLayout.setStackFromEnd(true);
        mBlogList.setLayoutManager(mLayout);




        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setSize(8);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                FirebaseRecyclerAdapter<ItemFeed,BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ItemFeed, BlogViewHolder>(
                        ItemFeed.class,
                        R.layout.item_recycler_design,
                        BlogViewHolder.class,
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
                                Intent singleBlogIntent = new Intent(Feed.this,BlogSingleActicity.class);
                                singleBlogIntent.putExtra("blog_id", post_key);
                                startActivity(singleBlogIntent);
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
        super.onStart();

        FirebaseRecyclerAdapter<ItemFeed,BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ItemFeed, BlogViewHolder>(
                ItemFeed.class,
                R.layout.item_recycler_design,
                BlogViewHolder.class,
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
                        Intent singleBlogIntent = new Intent(Feed.this,BlogSingleActicity.class);
                        singleBlogIntent.putExtra("blog_id", post_key);
                        startActivity(singleBlogIntent);
                    }
                });
            }
        };

        mBlogList.setAdapter(firebaseRecyclerAdapter);

    }

    private void setToolbar() {
        // Añadir la Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Relatos");
        setSupportActionBar(toolbar);
    }
    public static class BlogViewHolder extends RecyclerView.ViewHolder{

        View mView;
        TextView mItem_recycler_design_title;


        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView ;
            mItem_recycler_design_title = mView.findViewById(R.id.item_recycler_design_title);

        }
        public void setTitle(String title){
            //mItem_recycler_design_title.setTypeface(Pacifico);
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_add){
            startActivity(new Intent(Feed.this,PostFeed.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
