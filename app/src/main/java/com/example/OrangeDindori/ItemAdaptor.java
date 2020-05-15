package com.example.OrangeDindori;

import android.content.Context;
import android.graphics.Paint;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.OrangeDindori.staticData.ProductObject;


import java.util.ArrayList;


public class ItemAdaptor extends RecyclerView.Adapter<ItemAdaptor.ItemViewHolder> implements Filterable{


    public interface ListItemListener{
        void onItemClicked(int clikedItemIndex, ArrayList<ProductObject> filerList);
    }

    private int mNoOfItems = 0;
    private static String TAG = ItemAdaptor.class.getSimpleName();

    final private ListItemListener mOnClickedListener;
    ArrayList<ProductObject> mListOfItems;
    ArrayList<ProductObject> mListOfItemsFilter;


    public ItemAdaptor(int mNoOfItem, ArrayList<ProductObject> mListOfItem,ListItemListener listItemListener){
        mNoOfItems=mNoOfItem;
        mListOfItems=mListOfItem;
        mOnClickedListener=listItemListener;
        mListOfItemsFilter=mListOfItem;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.product_show;

        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttackToParentImmediaty = false;

        View view = inflater.inflate(layoutIdForListItem,viewGroup,shouldAttackToParentImmediaty);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Log.d(TAG," Position#"+position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mListOfItemsFilter.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();
                if(charString.isEmpty()){
                    mListOfItemsFilter = mListOfItems;

                }else{

                    ArrayList<ProductObject> filteredList = new ArrayList<ProductObject>();

                    System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
                    for(ProductObject productObject : mListOfItems){
                        if(productObject.getProductName().toLowerCase().contains(charString)){
                            filteredList.add(productObject);
                            System.out.println(productObject.toString());
                        }
                    }

                    mListOfItemsFilter = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mListOfItemsFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mListOfItemsFilter = (ArrayList<ProductObject>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class ItemViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        private ImageView mImageView;
        private TextView mProductTitle;
        private TextView mPorductMrpPrice;
        private TextView mProductMrpDiscountPrice;
        private TextView mProductDicountPricentage;
        private TextView mMrpTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.im_product);
            mProductTitle = (TextView) itemView.findViewById(R.id.tv_product_name);
            mPorductMrpPrice = (TextView) itemView.findViewById(R.id.tv_orignal_price);
            mProductMrpDiscountPrice = (TextView) itemView.findViewById(R.id.tv_discount_price);
            mProductDicountPricentage = (TextView) itemView.findViewById(R.id.tv_discount_precentage);
            mMrpTextView = (TextView) itemView.findViewById(R.id.tv_mrp);
            itemView.setOnClickListener(this);
        }

        void bind(int onBindIndex){
            ProductObject obj = mListOfItemsFilter.get(onBindIndex);
            mProductTitle.setText(obj.getProductName());
            mPorductMrpPrice.setText(String.valueOf(obj.getMrpPrice()));

            mPorductMrpPrice.setPaintFlags(mPorductMrpPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            mProductMrpDiscountPrice.setText(String.valueOf(obj.getDiscountPrice()));
            mProductDicountPricentage.setText(String.valueOf(obj.getDiscountPercentage()));

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);
            Glide.with(mImageView.getContext())
                    .load(obj.getUrl())
                    .apply(options.override(800, 750))
                    .into(mImageView);
        }


        @Override
        public void onClick(View v) {
            int itemPosition = getAdapterPosition();
            mOnClickedListener.onItemClicked(itemPosition,mListOfItemsFilter);
        }
    }
}
