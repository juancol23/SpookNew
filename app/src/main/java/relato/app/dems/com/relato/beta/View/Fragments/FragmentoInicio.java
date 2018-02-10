package relato.app.dems.com.relato.beta.View.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import relato.app.dems.com.relato.beta.View.Details.BlogSingleActicity;
import relato.app.dems.com.relato.beta.Model.ItemFeed;
import relato.app.dems.com.relato.beta.R;
import relato.app.dems.com.relato.beta.View.Details.DetailsRelato;
import relato.app.dems.com.relato.beta.View.Fragments.Details.DetailsBestHistory;
import relato.app.dems.com.relato.beta.View.Fragments.Details.DetalleHistoria;
import relato.app.dems.com.relato.beta.View.Fragments.Details.DetalleRelato;
import relato.app.dems.com.relato.beta.View.Util.ViewHolder.RelatoViewHolderStructure;
import relato.app.dems.com.relato.beta.View.Util.ViewHolder.RelatoViewHolderStructure_h;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentoInicio extends Fragment {

    private RecyclerView mRecyclerNewStories;
    private RecyclerView mRecyclerTrending;
    private RecyclerView mRecyclerLast;
    private RecyclerView mRecyclerEpisodios;
    private RecyclerView mRecyclerAsesinos;
    private DatabaseReference mDatabase,mDataHeader,mDatabaseA;
    private DatabaseReference mDatabaseHeader,mDatabaseTreding;

    private ProgressDialog mProgress;


    private RatingBar mRating;

    public FragmentoInicio() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View v =  inflater.inflate(R.layout.fragment_fragmento_inicio, container, false);
        mProgress = new ProgressDialog(getContext());

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Historias");
        mDatabase.keepSynced(true);

        mDatabaseHeader = (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("Portada");
        mDatabaseHeader.keepSynced(true);
        mRating = v.findViewById(R.id.rating);
        mRating.setRating(5);

        Query queryLenyendas = mDatabase.orderByChild("category").equalTo("LeyendasUrbanas");
        Query queryEpisodios = mDatabase.orderByChild("category").equalTo("EpisodiosPerdidos");
        Query queryAsesinos = mDatabase.orderByChild("category").equalTo("AsesinosSeriales");


        mDatabaseHeader.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String post_image = (String) dataSnapshot.child("images").getValue();

                ImageView image_paralax_header_relato = (ImageView) v.findViewById(R.id.inicio_header_image);

                image_paralax_header_relato.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                           Intent singleBlogIntent = new Intent(getActivity().getApplicationContext(),DetailsBestHistory.class);
                            startActivity(singleBlogIntent);

                    }
                });

                Glide.with(getActivity().getApplicationContext())
                        .load(post_image)
                        .into(image_paralax_header_relato);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mRecyclerNewStories = (RecyclerView) v.findViewById(R.id.recycler_view_relatos_new_stories);
        mRecyclerNewStories.setHasFixedSize(true);

        mRecyclerTrending = (RecyclerView) v.findViewById(R.id.recycler_view_relatos_trending);
        mRecyclerTrending.setHasFixedSize(true);

        mRecyclerLast = (RecyclerView) v.findViewById(R.id.recycler_view_relatos_last);
        mRecyclerLast.setHasFixedSize(true);

        mRecyclerEpisodios = (RecyclerView) v.findViewById(R.id.recycler_view_relatos_episodios);
        mRecyclerEpisodios.setHasFixedSize(true);

        mRecyclerAsesinos = (RecyclerView) v.findViewById(R.id.recycler_view_relatos_asesinos);
        mRecyclerAsesinos.setHasFixedSize(true);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        LinearLayoutManager layoutManagerTrending
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        layoutManagerTrending.setReverseLayout(true);
        layoutManagerTrending.setStackFromEnd(true);

        LinearLayoutManager layoutManagerLast
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        layoutManagerLast.setReverseLayout(true);
        layoutManagerLast.setStackFromEnd(true);

        LinearLayoutManager layoutManagerEpisodios
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        layoutManagerEpisodios.setReverseLayout(true);
        layoutManagerEpisodios.setStackFromEnd(true);

        LinearLayoutManager layoutManagerAsesinos
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        layoutManagerAsesinos.setReverseLayout(true);
        layoutManagerAsesinos.setStackFromEnd(true);


        mRecyclerNewStories.setLayoutManager(layoutManager);
        mRecyclerTrending.setLayoutManager(layoutManagerTrending);

        mRecyclerLast.setLayoutManager(layoutManagerLast);

        mRecyclerEpisodios.setLayoutManager(layoutManagerEpisodios);
        mRecyclerAsesinos.setLayoutManager(layoutManagerAsesinos);


        FirebaseRecyclerAdapter<ItemFeed, RelatoViewHolderStructure_h> firebaseRecyclerAdapterNewStories = new FirebaseRecyclerAdapter<ItemFeed, RelatoViewHolderStructure_h>(
                ItemFeed.class,
                R.layout.design_structure_relato_menu_horizontal,
                RelatoViewHolderStructure_h.class,
                mDatabase.limitToLast(15)

        ) {
            @Override
            protected void populateViewHolder(RelatoViewHolderStructure_h viewHolder, ItemFeed model, int position) {
                final String post_key = getRef(position).getKey();
                viewHolder.setTitle_h(model.getTitle());
                viewHolder.setCatergory_h(model.getCategory());
                viewHolder.setAuthor_h(model.getAuthor());

                viewHolder.setImage_h(getContext(), model.getImage());

                viewHolder.mViewStructure_h.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mProgress.setMessage("Accediendo...");
                        mProgress.show();

                        Intent singleBlogIntent = new Intent(getContext(), DetailsRelato.class);
                        singleBlogIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        singleBlogIntent.putExtra("blog_id", post_key);
                        startActivity(singleBlogIntent);

                        Log.v("id","id"+post_key);
                    }
                });
            }
        };

        mRecyclerNewStories.setAdapter(firebaseRecyclerAdapterNewStories);
 
        FirebaseRecyclerAdapter<ItemFeed, RelatoViewHolderStructure_h> firebaseRecyclerAdapterTrending = new FirebaseRecyclerAdapter<ItemFeed, RelatoViewHolderStructure_h>(
                ItemFeed.class,
                R.layout.design_structure_relato_menu_horizontal,
                RelatoViewHolderStructure_h.class,
                mDatabase.limitToFirst(15)

        ) {
            @Override
            protected void populateViewHolder(RelatoViewHolderStructure_h viewHolder, ItemFeed model, int position) {
                final String post_key = getRef(position).getKey();

                viewHolder.setTitle_h(model.getTitle());
                viewHolder.setCatergory_h(model.getCategory());
                viewHolder.setAuthor_h(model.getAuthor());
                viewHolder.setImage_h(getContext(), model.getImage());

                viewHolder.mViewStructure_h.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mProgress.setMessage("Accediendo...");
                        mProgress.show();
                        Intent singleBlogIntent = new Intent(getContext(), DetailsRelato.class);
                        singleBlogIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        singleBlogIntent.putExtra("blog_id", post_key);
                        startActivity(singleBlogIntent);
                        Log.v("id","id"+post_key);
                    }
                });
            }

        };

        mRecyclerTrending.setAdapter(firebaseRecyclerAdapterTrending);

        FirebaseRecyclerAdapter<ItemFeed, RelatoViewHolderStructure_h> firebaseRecyclerAdapterLast = new FirebaseRecyclerAdapter<ItemFeed, RelatoViewHolderStructure_h>(
                ItemFeed.class,
                R.layout.design_structure_relato_menu_horizontal,
                RelatoViewHolderStructure_h.class,
                queryLenyendas
        ) {
            @Override
            protected void populateViewHolder(RelatoViewHolderStructure_h viewHolder, ItemFeed model, int position) {
                final String post_key = getRef(position).getKey();
                viewHolder.setTitle_h(model.getTitle());
                viewHolder.setCatergory_h(model.getCategory());
                viewHolder.setAuthor_h(model.getAuthor());
                viewHolder.setImage_h(getContext(), model.getImage());
                viewHolder.mViewStructure_h.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mProgress.setMessage("Accediendo...");
                        mProgress.show();
                        Intent singleBlogIntent = new Intent(getContext(), DetailsRelato.class);
                        singleBlogIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        singleBlogIntent.putExtra("blog_id", post_key);
                        startActivity(singleBlogIntent);
                        Log.v("id","id"+post_key);
                    }
                });
            }
        };

        mRecyclerLast.setAdapter(firebaseRecyclerAdapterLast);

        FirebaseRecyclerAdapter<ItemFeed, RelatoViewHolderStructure_h> firebaseRecyclerAdapterEpisodios = new FirebaseRecyclerAdapter<ItemFeed, RelatoViewHolderStructure_h>(
                ItemFeed.class,
                R.layout.design_structure_relato_menu_horizontal,
                RelatoViewHolderStructure_h.class,
                queryEpisodios
        ) {
            @Override
            protected void populateViewHolder(RelatoViewHolderStructure_h viewHolder, ItemFeed model, int position) {
                final String post_key = getRef(position).getKey();
                viewHolder.setTitle_h(model.getTitle());
                viewHolder.setCatergory_h(model.getCategory());
                viewHolder.setAuthor_h(model.getAuthor());

                viewHolder.setImage_h(getContext(), model.getImage());

                viewHolder.mViewStructure_h.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mProgress.setMessage("Accediendo...");
                        mProgress.show();
                        Intent singleBlogIntent = new Intent(getContext(), DetailsRelato.class);
                        singleBlogIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        singleBlogIntent.putExtra("blog_id", post_key);
                        startActivity(singleBlogIntent);
                        Log.v("id","id"+post_key);
                    }
                });
            }
        };

        mRecyclerEpisodios.setAdapter(firebaseRecyclerAdapterEpisodios);


        FirebaseRecyclerAdapter<ItemFeed, RelatoViewHolderStructure_h> firebaseRecyclerAdapterAsesinos = new FirebaseRecyclerAdapter<ItemFeed, RelatoViewHolderStructure_h>(
                ItemFeed.class,
                R.layout.design_structure_relato_menu_horizontal,
                RelatoViewHolderStructure_h.class,
                queryAsesinos
        ) {
            @Override
            protected void populateViewHolder(RelatoViewHolderStructure_h viewHolder, ItemFeed model, int position) {
                final String post_key = getRef(position).getKey();
                viewHolder.setTitle_h(model.getTitle());
                viewHolder.setCatergory_h(model.getCategory());
                viewHolder.setAuthor_h(model.getAuthor());

                viewHolder.setImage_h(getContext(), model.getImage());

                viewHolder.mViewStructure_h.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mProgress.setMessage("Accediendo...");
                        mProgress.show();
                        Intent singleBlogIntent = new Intent(getContext(), DetailsRelato.class);
                        singleBlogIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        singleBlogIntent.putExtra("blog_id", post_key);
                        startActivity(singleBlogIntent);
                        Log.v("id","id"+post_key);
                    }
                });
            }
        };

        mRecyclerAsesinos.setAdapter(firebaseRecyclerAdapterAsesinos);

        return v;
    }


    @Override
    public void onStop() {
        mProgress.dismiss();
        super.onStop();
    }


}

















