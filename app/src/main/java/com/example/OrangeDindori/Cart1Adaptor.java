package com.example.OrangeDindori;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.OrangeDindori.staticData.CreateCart;
import com.example.OrangeDindori.staticData.ProductObject;

import java.util.ArrayList;
import java.util.List;

public class Cart1Adaptor extends RecyclerView.Adapter<Cart1Adaptor.ItemViewHolder>  {

    public Cart1Adaptor(int noOfProduct, List<CreateCart> mListCart, Cart1Activity listItemListener, Cart1Adaptor.ListItemListener mOnClickedListener) {
        this.mOnClickedListener = mOnClickedListener;
    }

    public interface ListItemListener{
        void onItemClicked(int clickItemIndex, List<CreateCart> filerList);
    }

    private int mNoOFItem = 0;

    private static String TAG = Cart1Adaptor.class.getSimpleName();
    final private Cart1Adaptor.ListItemListener mOnClickedListener;

    List<CreateCart> mListOfCart;
    List<CreateCart> mListOfCartFilter;

    public Cart1Adaptor(int mNoOFItem, List<CreateCart> mListOfCart, Cart1Adaptor.ListItemListener listItemListener){
        this.mListOfCart = mListOfCart;
        this.mNoOFItem = mNoOFItem;
        this.mOnClickedListener = listItemListener;
        mListOfCartFilter= mListOfCart;
    }


    @Override
    public Cart1Adaptor.ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.cart1item;

        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttackToParentImediatly = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttackToParentImediatly);
        Cart1Adaptor.ItemViewHolder itemViewHolder = new Cart1Adaptor.ItemViewHolder(view);
        return itemViewHolder;
    }


    @Override
    public void onBindViewHolder(Cart1Adaptor.ItemViewHolder holder, int position) {
        Log.d(TAG,"#position#"+position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNoOFItem;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        private ImageView mImageViewCart;
        private TextView mTextViewName;
        private TextView mTextViewPrice;
        private TextView mTextViewQnt;
        private TextView mTextViewStatus;

        public ItemViewHolder(View itemView) {
            super(itemView);

            mImageViewCart = (ImageView) itemView.findViewById(R.id.im_cartimage1);
            mTextViewName = (TextView) itemView.findViewById(R.id.tv_card_productname1);
            mTextViewPrice = (TextView) itemView.findViewById(R.id.tv_cart_price1);
            mTextViewQnt = (TextView) itemView.findViewById(R.id.tv_cart_qnt1);
          //  mTextViewStatus = (TextView) itemView.findViewById(R.id.tv_cart_status);

            itemView.setOnClickListener(this);
        }

        void bind(int onBindIndex){
            CreateCart createCart = mListOfCart.get(onBindIndex);

            Glide.with(mImageViewCart.getContext())
                    .load(createCart.getUrl())
                    .into(mImageViewCart);

            mTextViewName.setText(createCart.getProductName());
         //   mTextViewStatus.setText(createCart.getStatus());
            String qnt = String.valueOf(createCart.getAmount());
            String price = String.valueOf(createCart.getTotalCost());
            mTextViewQnt.setText(qnt);
            mTextViewPrice.setText(price);
        }

        @Override
        public void onClick(View v) {
            int itemposition = getAdapterPosition();
            mOnClickedListener.onItemClicked(itemposition,mListOfCartFilter);
        }
    }

}
