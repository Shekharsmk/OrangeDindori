package com.example.OrangeDindori;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
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

public class DetailActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private static final String TAG = "Mytag";
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReferenceUserOrder;
    private DatabaseReference mDatabaseReferenceUserdetail;
    private DatabaseReference mDatabaseReferenceStatus;
    private DatabaseReference mDatabaseReferenceServerOrder;
    private DatabaseReference mDatabaseReferenceServerOrder2;
    private List<CreateCart> mListCart = new ArrayList<CreateCart>();;
    String userId;
    int mamount = 1;
    private AlertDialog.Builder alertDialog;

    private ProductSerial productSerial;
    private ProductObject product;
    private CreateCartSerial cartSerial;
    private CreateCart cart;

    private ImageView mImageView;
    private TextView mProductName;
    private TextView mProductPrice;
    private TextView mProductDiscount;
    private TextView mExpiryDate;
    private TextView mDiscription;
    private Button mOrder;
    private Button mCart;
    private int totalbill=0;


    private UserAccount account;
    private Boolean status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ActionBar actionBar = this.getSupportActionBar();

        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        alertDialog = new AlertDialog.Builder(DetailActivity.this);
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
                // Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        showSnack(isNetworkAvailable());

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        userId = mFirebaseUser.getUid();

        mDatabaseReferenceStatus = mFirebaseDatabase.getReference().child("Customer").child(userId).child("DetailPresent");
        mDatabaseReferenceUserdetail = mFirebaseDatabase.getReference().child("Customer").child(userId).child("accountDetail");

        mImageView = (ImageView) findViewById(R.id.im_detail);
        mProductName = (TextView) findViewById(R.id.tv_product_name_detail);
        mProductPrice = (TextView) findViewById(R.id.tv_product_price_detail);
        mProductDiscount =(TextView) findViewById(R.id.tv_product_discount_detail);
        mExpiryDate = (TextView) findViewById(R.id.tv_product_expirydate_detail);
        mDiscription = (TextView) findViewById(R.id.tv_product_desciption_detail);
        mOrder =(Button) findViewById(R.id.bt_submit_detail);
        mCart =(Button) findViewById(R.id.bt_addToCart_detail);

        final Intent intent = getIntent();
        if(intent.hasExtra("ProductDetail")){
            productSerial = (ProductSerial) intent.getSerializableExtra("ProductDetail");
        }

        product = new ProductObject();
        if(productSerial!=null) {
            totalbill = mamount * productSerial.getDiscountPrice();
            product.setDiscountPercentage(productSerial.getDiscountPercentage());
            product.setExpiryDate(productSerial.getExpiryDate());
            product.setProductName(productSerial.getProductName());
            product.setMrpPrice(productSerial.getMrpPrice());
            product.setDiscountPrice(productSerial.getDiscountPrice());
            product.setDescription(productSerial.getDescription());
            product.setUrl(productSerial.getUrl());
            product.setItemID(productSerial.getItemID());
            Log.d(TAG, " product.setItemID#" + productSerial.getItemID());
            Glide.with(mImageView.getContext())
                    .load(productSerial.getUrl())
                    .into(mImageView);
            mProductName.setText(product.getProductName());
            mProductPrice.setText(String.valueOf(product.getDiscountPrice()));
            mProductDiscount.setText(String.valueOf(product.getDiscountPercentage()));
            mExpiryDate.setText(product.getExpiryDate());
            mDiscription.setText(product.getDescription());

            mDatabaseReferenceStatus.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    boolean bool = false;
                    String key = dataSnapshot.getKey();
                    bool = dataSnapshot.getValue(Boolean.class);
                    status = bool;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
            mCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(productSerial != null) {

                        if (status == false) {
                            Intent intent = new Intent(DetailActivity.this, AccountActivity.class);
                            startActivity(intent);
                        } else {

                            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(DetailActivity.this);
                            View mView = getLayoutInflater().inflate(R.layout.payment_dialog_order, null);

                           // final EditText mEditText = (EditText) mView.findViewById(R.id.et_amount_detail);


                            {
                              /*  CreateCart Cart1=new CreateCart();
                                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());


                                Cart1.setProductName(product.getProductName());
                                Cart1.setAmount(mamount);
                                Cart1.setMrpPrice(product.getMrpPrice());
                                Cart1.setDescription(product.getDescription());
                                Cart1.setExpiryDate(product.getExpiryDate());
                                Cart1.setdiscountPercentage(product.getdiscountPercentage());
                                Cart1.setDiscountPrice(product.getDiscountPrice());
                                Cart1.setTotalCost(mamount * product.getDiscountPrice());
                                Cart1.setUrl(product.getUrl());
*/
                                CartBasicDetails cartBasicDetails=new CartBasicDetails();
                                cartBasicDetails.setAmount(mamount);
                                cartBasicDetails.setItemID(product.getItemID());


                                Random ran = new Random();
                                int r = ran.nextInt(99999999);
                                String ranString = String.valueOf(r);
                                mDatabaseReferenceUserOrder = mFirebaseDatabase.getReference().child("Customer").child(userId).child("Cart");
                                mDatabaseReferenceUserOrder.child(ranString).setValue(cartBasicDetails);

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


                                Toast.makeText(getApplicationContext(), "Item added to cart", Toast.LENGTH_SHORT).show();
                            }


                        }
                    }

                }
            });

            mOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (productSerial != null) {

                        if (status == false) {
                            Intent intent = new Intent(DetailActivity.this, AccountActivity.class);
                            startActivity(intent);
                        } else {
                            CreateCart Cart1=new CreateCart();
                            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                            Cart1.setProductName(product.getProductName());
                            Cart1.setAmount(mamount);
                            Cart1.setMrpPrice(product.getMrpPrice());
                            Cart1.setDescription(product.getDescription());
                            Cart1.setExpiryDate(product.getExpiryDate());
                            Cart1.setDiscountPercentage(product.getDiscountPercentage());
                            Cart1.setDiscountPrice(product.getDiscountPrice());
                            Cart1.setTotalCost(mamount * product.getDiscountPrice());
                            Cart1.setUrl(product.getUrl());
                            Cart1.setItemID(product.getItemID());
                            Log.d(TAG, "product.getProductName(): "+product.getProductName()+mamount+
                                    product.getMrpPrice()+product.getDescription()+product.getExpiryDate()+
                                    "product.getdiscountPercentage()"+product.getDiscountPercentage()+productSerial.getDiscountPercentage()+product.getDiscountPrice()+mamount * product.getDiscountPrice()+
                                    product.getUrl()+product.getItemID()

                            );
                            mListCart.add(Cart1);
                            Intent instentdestion = new Intent(DetailActivity.this, ConfirmOrderActivity.class);

                            instentdestion.putExtra("listofitems", (Serializable) mListCart);

                            startActivity(instentdestion);
                        /*{

                            mDatabaseReferenceUserdetail.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    UserAccount obj = null;
                                    obj = dataSnapshot.getValue(UserAccount.class);

                                    account = obj;
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            totalbill= mamount*product.getDiscountPrice();

                            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(DetailActivity.this);
                            View mView = getLayoutInflater().inflate(R.layout.payment_dialog_order, null);

                          //  final EditText mEditText = (EditText) mView.findViewById(R.id.et_amount_detail);
                            final RadioGroup mRadioGroup = (RadioGroup) mView.findViewById(R.id.rg_payment_detail_order);
                            TextView tv1=(TextView)mView.findViewById(R.id.et_amount_detail_order);
                            tv1.setText(String.valueOf(totalbill));

                            Button mSubmit = (Button) mView.findViewById(R.id.bt_submit_payment_order);
                            Button mCancle = (Button) mView.findViewById(R.id.bt_cancle_payment_order);
                            final RadioButton[] mRadioButton = new RadioButton[1];

                            mBuilder.setView(mView);

                            final AlertDialog dialog = mBuilder.create();
                            dialog.show();

                            mSubmit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    CreateOrder createOrder = new CreateOrder();
                                   // int count = 0;

                                    String methodOfPayment = null;
                                  //  int totalamount;
                                    int selectId = R.id.rb_cod_detail_order;
                                    selectId = mRadioGroup.getCheckedRadioButtonId();

                                  //  amount = mEditText.getText().toString();
                                    if(mamount==0) {

                                   // }else if(mamount<0){
                                        Toast.makeText(DetailActivity.this,"no item selected",Toast.LENGTH_SHORT).show();
                                    }
                                    else if(mamount > 0) {


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
                                        if (selectId == R.id.rb_cod_detail_order) {
                                            methodOfPayment = "cod";

                                            createOrder.setPaymentmethod(methodOfPayment);
                                            createOrder.setPaymentstatus("N");
                                        }
                                        if (selectId == R.id.rb_online_detail_order) {
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

                        }*/
                    }
                }

            }
            });



    }


    private  int getRandom(){
        Random random = new Random();
        int r  = random.nextInt(99999);
        return r;
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
        MyApplication.getInstance().setConnectivityListener(DetailActivity.this);
    }
*/

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }


    public void increaseInteger(View view) {
        mamount = mamount + 1;
        display(mamount);

    }public void decreaseInteger(View view) {
        if(mamount>1)
        {
            mamount = mamount - 1;
            display(mamount);
        }

    }

    private void display(int number) {
        TextView displayInteger = (TextView) findViewById(R.id.integer_number);
        displayInteger.setText("" + number);
    }

    public void totalAmountOrder(View view) {
        TextView displayInteger = (TextView) findViewById(R.id.et_amount_detail_order);
        displayInteger.setText("" + totalbill);
    }

}
