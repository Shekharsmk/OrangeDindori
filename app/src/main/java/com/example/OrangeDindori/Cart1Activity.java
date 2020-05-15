package com.example.OrangeDindori;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.OrangeDindori.staticData.CartBasicDetails;
import com.example.OrangeDindori.staticData.ConnectivityReceiver;
import com.example.OrangeDindori.staticData.CreateCart;
import com.example.OrangeDindori.staticData.CreateCartSerial;
import com.example.OrangeDindori.staticData.CreateOrder;
import com.example.OrangeDindori.staticData.ProductObject;
import com.example.OrangeDindori.staticData.ProductSerial;
import com.example.OrangeDindori.staticData.UserAccount;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Cart1Activity extends AppCompatActivity implements Cart1Adaptor.ListItemListener, ConnectivityReceiver.ConnectivityReceiverListener{

    private static final String TAG = "MYtag";
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReferenceCart;
    private DatabaseReference mDatabaseReferenceProduct;
    private DatabaseReference getmDatabaseReferenceOrdersChange;

    private AlertDialog.Builder alertDialog;

    private String userId;
    private RecyclerView mRecyclerView;
    private List<CreateCart> mListCart;
   // private List<String> mListCartKey;
    private int noOfProduct,totalbill=0;
    private Cart1Adaptor mAdaptor;
    private Toast mToast;
    private Button proceed;
    private UserAccount account;
    private List<CreateCart>mList = new ArrayList<CreateCart>();
//    final List<String>mListKey = new ArrayList<String>();
    private List<ProductSerial>mListProduct = new ArrayList<ProductSerial>();
  //  final List<String>mListProductKey = new ArrayList<String>();
    private List<CartBasicDetails>mListCartBasic = new ArrayList<CartBasicDetails>();
//    final List<String>mListCartBasicKey = new ArrayList<String>();
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart1);


        ActionBar actionBar = this.getSupportActionBar();

        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        alertDialog = new AlertDialog.Builder(Cart1Activity.this);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("No Internet Connection");
        alertDialog.setMessage("We can not detect any internet connection please check your internet connection and try again");

        alertDialog.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                showSnack(isNetworkAvailable());
            }
        });


        alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
                onBackPressed();
                //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        showSnack(isNetworkAvailable());



        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        userId = mFirebaseUser.getUid();
        mDatabaseReferenceCart = mFirebaseDatabase.getReference().child("Customer").child(userId).child("Cart");
        mDatabaseReferenceProduct = mFirebaseDatabase.getReference().child("Product_detail");

        mRecyclerView = (RecyclerView)findViewById(R.id.rv_cart1data);
        proceed =(Button) findViewById(R.id.bt_proceed_to_buy);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mDatabaseReferenceProduct.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for(DataSnapshot singleDataSnapshot : children){
                    ProductSerial obj = singleDataSnapshot.getValue(ProductSerial.class);
                    String objs = singleDataSnapshot.getKey();
                    mListProduct.add(obj);
                  //  mListProductKey.add(objs);
                 }
            }

            @Override
            public void onCancelled( DatabaseError databaseError) {

            }
        });
        mDatabaseReferenceCart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

              /* Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for(DataSnapshot singleDataSnapshot : children){
                    CreateCart obj = singleDataSnapshot.getValue(CreateCart.class);
                    String objs = singleDataSnapshot.getKey();
                    mList.add(obj);
                    mListKey.add(objs);
                    noOfProduct += 1;

                }*/
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for(DataSnapshot singleDataSnapshot : children) {
                    CartBasicDetails obj = singleDataSnapshot.getValue(CartBasicDetails.class);
                    String objs = singleDataSnapshot.getKey();
                    mListCartBasic.add(obj);
                //    mListKey.add(objs);
                    noOfProduct += 1;
                }


                for(CartBasicDetails cbd:mListCartBasic){
                    long itemid=cbd.getItemID();
                    int quantity=cbd.getAmount();
                    Log.d(TAG, "itemid: "+itemid);
                    Log.d(TAG, "quantity: "+quantity);
                    for(ProductSerial ps:mListProduct) {
                       if(itemid==ps.getItemID()) {
                           CreateCart cc = new CreateCart();
                           cc.setAmount(cbd.getAmount());
                           cc.setTotalCost(ps.getDiscountPrice()*quantity);
                           cc.setUrl(ps.getUrl());
                           cc.setProductName(ps.getProductName());
                           cc.setDescription(ps.getDescription());
                           cc.setExpiryDate(ps.getExpiryDate());
                           cc.setDiscountPercentage(ps.getDiscountPercentage());
                           cc.setItemID(ps.getItemID());
                           cc.setMrpPrice(ps.getMrpPrice());
                           cc.setDiscountPrice(ps.getDiscountPrice());
                           Log.d(TAG, "values: "+cbd.getAmount()+ps.getDiscountPrice()*quantity+ps.getUrl()+ps.getProductName()
                                   +ps.getDescription()+ps.getExpiryDate()+ps.getItemID()+ps.getMrpPrice()+ps.getDiscountPrice());
                           mList.add(cc);

                       }

                    }
                }

                mListCart =null;
                mListCart = mList;
               // mListCartKey = null;
               // mListCartKey = mListKey;

                mRecyclerView = null;
                mRecyclerView = (RecyclerView)findViewById(R.id.rv_cart1data);

                mRecyclerView.setHasFixedSize(true);
                mAdaptor = new Cart1Adaptor(noOfProduct,mListCart, Cart1Activity.this);
                mRecyclerView.setAdapter(mAdaptor);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListCart !=null){
                    for(CreateCart l:mListCart){
                        totalbill+=l.getTotalCost();
                    }

                }
                Log.d(TAG,"#mListCartooooooooooooooooooooooooitemID#"+mListCart.get(0).getItemID());
                Intent instentdestion = new Intent(Cart1Activity.this, ConfirmOrderActivity.class);

                instentdestion.putExtra("listofitems", (Serializable) mListCart);

                startActivity(instentdestion);
            }
        });
      /*  proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mListCart !=null){
                    for(CreateCart l:mListCart){
                        totalbill+=l.getTotalCost();
                    }
                }

                final android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(Cart1Activity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_to_payment_Direct_order, null);

                final RadioGroup mRadioGroup = (RadioGroup) mView.findViewById(R.id.rg_payment_detail);
                Button mSubmit = (Button) mView.findViewById(R.id.bt_submit_payment);
                Button mCancle = (Button) mView.findViewById(R.id.bt_cancle_payment);
                final RadioButton[] mRadioButton = new RadioButton[1];

                mBuilder.setView(mView);

                final android.app.AlertDialog dialog = mBuilder.create();
                dialog.show();

                mSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CreateOrder createOrder = new CreateOrder();
                        String methodOfPayment = null;
                        int selectId = R.id.rb_cod_detail;
                        selectId = mRadioGroup.getCheckedRadioButtonId();

                        if(totalbill==0) {

                            // }else if(mamount<0){
                            Toast.makeText(Cart1Activity.this,"Cart is Empty",Toast.LENGTH_SHORT).show();
                        }
                        else if(totalbill > 0) {


                            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                            createOrder.setPincode(account.getPincode());
                            createOrder.setLandSmark(account.getLandmark());
                            createOrder.setStatus("NotDeliver");
                            createOrder.setProductName(product.getProductName());
                            createOrder.setAmount(mamount);
                            createOrder.setCustomername(account.getName());
                            createOrder.setCustomerNo(account.getPhoneno());
                            createOrder.setPrice(product.getDiscountPrice());
                            createOrder.setTotalCost(mamount * product.getDiscountPrice());
                            createOrder.setDeliverydate("N");
                            createOrder.setDispatchdate("N");
                            createOrder.setOrderdate(currentDateTimeString);
                            createOrder.setUserid(userId);
                            createOrder.setAddress(account.getAddress());
                            createOrder.setUrl(product.getUrl());
                            if (selectId == R.id.rb_cod_detail) {
                                methodOfPayment = "cod";

                                createOrder.setPaymentmethod(methodOfPayment);
                                createOrder.setPaymentstatus("N");
                            }
                            if (selectId == R.id.rb_online_detail) {
                                methodOfPayment = "online";
                                createOrder.setPaymentmethod(methodOfPayment);
                                createOrder.setPaymentstatus("Y");
                            }

                            Random ran = new Random();
                            int r = ran.nextInt(99999999);
                            String ranString = String.valueOf(r);
                            mDatabaseReferenceUserOrder = mFirebaseDatabase.getReference().child("Customer").child(userId).child("Orders");
                            mDatabaseReferenceUserOrder.child(ranString).setValue(createOrder);
                            mDatabaseReferenceServerOrder2 = mFirebaseDatabase.getReference().child("CompanyOrder").child(userId);
                            mDatabaseReferenceServerOrder2.child(ranString).setValue(createOrder);

                            final int[] flag = {0};
                            final int[] couterorder = new int[1];
                            mDatabaseReferenceServerOrder = mFirebaseDatabase.getReference().child("Company").child("Orders").child(userId);

                            mDatabaseReferenceServerOrder.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    int counter = 0;

                                    if (flag[0] == 0) {
                                        //  counter = dataSnapshot.getValue(Integer.class);
                                        couterorder[0] = counter;
                                        if (couterorder[0] == -1) {
                                            mDatabaseReferenceServerOrder.setValue(1);
                                        } else {
                                            mDatabaseReferenceServerOrder.setValue(couterorder[0] + 1);
                                        }
                                        flag[0] = 1;

                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                            Toast.makeText(getApplicationContext(), "Order successfully Placed", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"Order Not Placed",Toast.LENGTH_LONG).show();
                        }
                        dialog.cancel();



                    }
                });

                mCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
            }
        });*/
    }

    @Override
    public void onItemClicked(int clikedItemIndex,List<CreateCart> filerList) {

        if(mToast!=null){
            mToast=null;
        }

        Intent intent = new Intent(Cart1Activity.this, DetailActivity.class);
      //  intent.putExtra("key",mListCartKey.get(clickItemIndex));
        CreateCart product = filerList.get(clikedItemIndex);

        CreateCartSerial createCartSerial = new CreateCartSerial();
        createCartSerial.setUrl(product.getUrl());
        createCartSerial.setDescription(product.getDescription());
        createCartSerial.setDiscountPercentage(product.getDiscountPercentage());
        createCartSerial.setExpiryDate(product.getExpiryDate());
        createCartSerial.setDiscountPrice(product.getDiscountPrice());
        createCartSerial.setMrpPrice(product.getMrpPrice());
        createCartSerial.setProductName(product.getProductName());
        createCartSerial.setAmount(product.getAmount());
        createCartSerial.setTotalCost(product.getTotalCost());

        intent.putExtra("ProductDetail",createCartSerial);
      /*  intent.putExtra("status1",mListCart.get(clickItemIndex).getStatus());
        intent.putExtra("disdate",mListOrder.get(clickItemIndex).getDispatchdate());
        intent.putExtra("deldate",mListOrder.get(clickItemIndex).getDeliverydate());
        intent.putExtra("odate",mListOrder.get(clickItemIndex).getOrderdate());*/

        startActivity(intent);
        //mDatabaseReferenceOrders = mFirebaseDatabase.getReference().child("Customer").child(userId).child("Orders").child(mListOrderKey.get(clickItemIndex));
        //mDatabaseReferenceOrders.child("status").setValue("Cancle");

        //mToast = Toast.makeText(CartActivity.this,"Order : "+clickItemIndex+" cancle",Toast.LENGTH_SHORT);
        //mToast.show();

    }

    private boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {

        } else {
            message = "Sorry! Not connected to internet";
            alertDialog.show();
            color = Color.RED;
            View parentView = findViewById(android.R.id.content);
            Snackbar snackbar = Snackbar
                    .make(parentView, message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }


    }

   /* @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }*/


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
}
