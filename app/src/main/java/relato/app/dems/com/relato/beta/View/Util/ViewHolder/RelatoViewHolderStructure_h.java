package relato.app.dems.com.relato.beta.View.Util.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import relato.app.dems.com.relato.beta.R;

/**
 * Created by CORAIMA on 21/11/2017.
 */

public class RelatoViewHolderStructure_h extends RecyclerView.ViewHolder{

    public View mViewStructure_h;
    public TextView mItem_recycler_structure_title_h;
    public TextView mItem_recycler_structure_category_h;
    public TextView mItem_recycler_structure_author_h;
    public ImageView mPost_image_h;

    ImageButton mfavoriteBtn;


    public RelatoViewHolderStructure_h(View itemView) {
        super(itemView);
        mViewStructure_h = itemView ;
    }


    public void setTitle_h(String title){
        //mItem_recycler_structure_title.setTypeface(Pacifico);
        mItem_recycler_structure_title_h = mViewStructure_h.findViewById(R.id.item_recycler_structure_title_h);

        mItem_recycler_structure_title_h.setText(title);
    }

    public void setCatergory_h(String category){
        mItem_recycler_structure_category_h = mViewStructure_h.findViewById(R.id.item_recycler_structure_category_h);
        mItem_recycler_structure_category_h.setText(category);
    }
    public void setAuthor_h(String author){
        mItem_recycler_structure_author_h = mViewStructure_h.findViewById(R.id.item_recycler_structure_author_h);
        mItem_recycler_structure_author_h.setText(author);
    }


    public void setImage_h(Context context, String image){
        mPost_image_h = mViewStructure_h.findViewById(R.id.item_recycler_structure_imagen_h);

        Glide.with(context)
                .load(image)
                .thumbnail(Glide.with(context).load(R.drawable.item_placeholder))
                .into(mPost_image_h);

    }

}