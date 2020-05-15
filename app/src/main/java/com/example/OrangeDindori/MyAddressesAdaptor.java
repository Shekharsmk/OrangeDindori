package com.example.OrangeDindori;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.OrangeDindori.staticData.Address;
import com.example.OrangeDindori.staticData.Address;

import java.util.List;

public class MyAddressesAdaptor extends  RecyclerView.Adapter<MyAddressesAdaptor.ItemViewHolder>  {


    public MyAddressesAdaptor(int noOfProduct, List<Address > mListCart, MyAddressesActivity listItemListener, MyAddressesAdaptor.ListItemListener mOnClickedListener) {
        this.mOnClickedListener = mOnClickedListener;
    }

        public interface ListItemListener{
            void onItemClicked(int clickItemIndex, List<Address> filerList);
        }

        private int mNoOFItem = 0;

        private static String TAG = MyAddressesAdaptor.class.getSimpleName();
        final private MyAddressesAdaptor.ListItemListener mOnClickedListener;

        java.util.List<Address> mListOfCart;
        List<Address> mListOfCartFilter;

    public MyAddressesAdaptor(int mNoOFItem, List<Address> mListOfCart, MyAddressesAdaptor.ListItemListener listItemListener){
        this.mListOfCart = mListOfCart;
        this.mNoOFItem = mNoOFItem;
        this.mOnClickedListener = listItemListener;
        mListOfCartFilter= mListOfCart;
    }


        @Override
        public MyAddressesAdaptor.ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.address;

        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttackToParentImediatly = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttackToParentImediatly);
        MyAddressesAdaptor.ItemViewHolder itemViewHolder = new MyAddressesAdaptor.ItemViewHolder(view);
        return itemViewHolder;
    }


        @Override
        public void onBindViewHolder(MyAddressesAdaptor.ItemViewHolder holder, int position) {
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
            private TextView mTextViewAddName;
            private TextView mTextViewAddFull;
            private TextView mTextViewAddPhno;
            private TextView mTextViewStatus;

            public ItemViewHolder(View itemView) {
                super(itemView);


                mTextViewAddName = (TextView) itemView.findViewById(R.id.address_name);
                mTextViewAddFull = (TextView) itemView.findViewById(R.id.address_full);
                mTextViewAddPhno = (TextView) itemView.findViewById(R.id.address_phno);
                //  mTextViewStatus = (TextView) itemView.findViewById(R.id.tv_cart_status);

                itemView.setOnClickListener(this);
            }

            void bind(int onBindIndex){
                Address address = mListOfCart.get(onBindIndex);



                mTextViewAddName.setText(address.getName());
                //   mTextViewStatus.setText(address.getStatus());
                String gaddress = String.valueOf(address.getAddress());
                String gcity = String.valueOf(address.getCity());
                String glandmark = String.valueOf(address.getLandmark());
                String gpincode = String.valueOf(address.getPincode());
                gaddress=gaddress+", "+glandmark+", "+gcity.toUpperCase()+" - "+gpincode;
                String gphno = String.valueOf(address.getPhoneNo());

                mTextViewAddFull.setText(gaddress);
                mTextViewAddPhno.setText(gphno);
            }

            @Override
            public void onClick(View v) {
                int itemposition = getAdapterPosition();
                mOnClickedListener.onItemClicked(itemposition,mListOfCartFilter);
            }
        }

    }

