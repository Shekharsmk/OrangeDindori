package com.example.OrangeDindori;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.OrangeDindori.staticData.Address;
import com.example.OrangeDindori.staticData.CreateCart;
import com.example.OrangeDindori.staticData.CreateOrder;
import com.example.OrangeDindori.staticData.CustomerOrder;
import com.example.OrangeDindori.staticData.ItemCompanyOrder;
import com.example.OrangeDindori.staticData.OrderDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ConfirmOrderActivity extends AppCompatActivity implements  View.OnClickListener, ConfirmOrderAdaptor.ListItemListener {
    private static final String TAG = "My tag";
    Integer noOfProduct = 0,noofaddress=0,totalbill=0;
    Address chosenAddress=new Address();
    private List<CreateCart> mListCart;
    private RecyclerView mRecyclerView;
    private ConfirmOrderAdaptor mAdaptor;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReferenceAddress;
    private DatabaseReference mDatabaseReferenceCompanyOrder;
    private DatabaseReference mDatabaseReferenceCustomerOrder;
    private DatabaseReference mDatabaseReferenceCompany;
    private DatabaseReference mDatabaseReferenceOrderDetails;

    private String userId,ranString;
    final List<Address> mList = new ArrayList<Address>();
    final List<String> mListKey = new ArrayList<String>();
    private TextView mTextViewAddName;
    private TextView mTextViewAddFull;
    private TextView mTextViewAddPhno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        Intent i = getIntent();
        mListCart = (List<CreateCart>) i.getSerializableExtra("listofitems");
        findViewById(R.id.bt_proceed_to_payment_aco).setOnClickListener(this);
        findViewById(R.id.bt_Addaddress_aco).setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_cart1data_aco);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        if (mListCart != null) {
            for (CreateCart l : mListCart) {
                noOfProduct++;
                totalbill += l.getTotalCost();
            }
            Log.d(TAG, " Noof producttttttttttttttttttttttttt#" + noOfProduct);
            mRecyclerView = null;
            mRecyclerView = (RecyclerView) findViewById(R.id.rv_cart1data_aco);

            mRecyclerView.setHasFixedSize(true);
            mAdaptor = new ConfirmOrderAdaptor(noOfProduct, mListCart, ConfirmOrderActivity.this);
            mRecyclerView.setAdapter(mAdaptor);
        }

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        userId = mFirebaseUser.getUid();
        mDatabaseReferenceAddress = mFirebaseDatabase.getReference().child("Customer").child(userId).child("Addresses");
        mDatabaseReferenceAddress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {

                    Intent instentdestion = new Intent(ConfirmOrderActivity.this, AddressFormActivity.class);
                    startActivity(instentdestion);

                } else {
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    for (DataSnapshot singleDataSnapshot : children) {
                        Address obj = singleDataSnapshot.getValue(Address.class);
                        String objs = singleDataSnapshot.getKey();
                        mList.add(obj);
                        mListKey.add(objs);
                        noofaddress += 1;

                    }
                    chosenAddress = mList.get(0);
                    Log.d(TAG, "noofaddress: " + noofaddress);
                    if (mList != null) {
                        for (Address a : mList) {


                            mTextViewAddName = (TextView) findViewById(R.id.address_name_aco);
                            mTextViewAddFull = (TextView) findViewById(R.id.address_full_aco);
                            mTextViewAddPhno = (TextView) findViewById(R.id.address_phno_aco);
                            mTextViewAddName.setText(a.getName());
                            String gaddress = String.valueOf(a.getAddress());
                            String gcity = String.valueOf(a.getCity());
                            String glandmark = String.valueOf(a.getLandmark());
                            String gpincode = String.valueOf(a.getPincode());
                            gaddress = gaddress + ", " + glandmark + ", " + gcity.toUpperCase() + " - " + gpincode;
                            String gphno = String.valueOf(a.getPhoneNo());
                            mTextViewAddFull.setText(gaddress);
                            mTextViewAddPhno.setText(gphno);
                            break;
                        }
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onItemClicked(int clickItemIndex, List<CreateCart> filerList) {

    }

    @Override
    public void onClick(View v) {
            int id = v.getId();
            if(id == R.id.bt_proceed_to_payment_aco) {
                Log.d(TAG, "onClick: bt_proceed_to_payment_aco");
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ConfirmOrderActivity.this);
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
                        if(totalbill==0) {

                            // }else if(mamount<0){
                            Toast.makeText(ConfirmOrderActivity.this,"no item selected",Toast.LENGTH_SHORT).show();
                        }
                        else if(totalbill > 0) {

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
                            ranString = String.valueOf(r);

                            int itemcounter=1;
                            for (CreateCart l : mListCart) {
                                ItemCompanyOrder itemCompanyOrder = new ItemCompanyOrder();
                                itemCompanyOrder.setAmount(l.getAmount());
                                itemCompanyOrder.setDescription(l.getDescription());
                                itemCompanyOrder.setDiscountPrice(l.getDiscountPrice());
                                itemCompanyOrder.setMrpPrice(l.getMrpPrice());
                                itemCompanyOrder.setProductName(l.getProductName());
                                itemCompanyOrder.setUrl(l.getUrl());
                                itemCompanyOrder.setTotalCost(l.getTotalCost());
                                itemCompanyOrder.setItemID(l.getItemID());


                                mDatabaseReferenceCompanyOrder = mFirebaseDatabase.getReference().child("CompanyOrder").child(userId);
                                mDatabaseReferenceCompanyOrder.child(ranString).child("Items").child("Item"+itemcounter).setValue(itemCompanyOrder);
                                mDatabaseReferenceCustomerOrder = mFirebaseDatabase.getReference().child("Customer").child(userId).child("Orders");
                                mDatabaseReferenceCustomerOrder.child(ranString).setValue(createOrder);
                                itemcounter++;

                            }
                            //setting ordering address
                            mDatabaseReferenceAddress = mFirebaseDatabase.getReference().child("CompanyOrder").child(userId);
                            mDatabaseReferenceAddress.child(ranString).child("AddressDetails").setValue(chosenAddress);
//setting order details
                            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                            OrderDetails orderDetails=new OrderDetails();
                            //   private String deliveryDate,dispatchDate,OrderDate,paymentMethod,paymentstatus,status,odextra1="odextra1",
                            //            odextra2="odextra2",odextra3="odextra3",odextra4="odextra4";
                            orderDetails.setDeliveryDate("TBD");
                            orderDetails.setDispatchDate("TBD");
                            orderDetails.setOrderDate(currentDateTimeString);
                            orderDetails.setPaymentMethod(methodOfPayment);
                            orderDetails.setPaymentstatus("N");
                            orderDetails.setStatus("NotDelivered");

                            mDatabaseReferenceOrderDetails = mFirebaseDatabase.getReference().child("CompanyOrder").child(userId);
                            mDatabaseReferenceOrderDetails.child(ranString).child("OrderDetails").setValue(orderDetails);
//setting customer order tree

                            CustomerOrder customerOrder=new CustomerOrder();
                            customerOrder.setOrderID(ranString);
                            mDatabaseReferenceOrderDetails = mFirebaseDatabase.getReference().child("Customer").child(userId);
                            mDatabaseReferenceOrderDetails.child("Orders").child(ranString).setValue(customerOrder);

//setting Company order filing
                            final int[] flag = {0};
                            final int[] couterorder = new int[1];
                            mDatabaseReferenceCompany = mFirebaseDatabase.getReference().child("Company").child("Orders").child(userId);
                            Log.d(TAG, "all attribute set: bt_proceed_to_payment_aco");
                            mDatabaseReferenceCompany.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    int counter = 0;

                                    if (flag[0] == 0) {
                                        //  counter = dataSnapshot.getValue(Integer.class);
                                        couterorder[0] = counter;
                                        if (couterorder[0] == -1) {
                                            mDatabaseReferenceCompany.setValue(1);
                                        } else {
                                            mDatabaseReferenceCompany.setValue(couterorder[0] + 1);
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
            }

        if(id == R.id.bt_Addaddress_aco)
        {
            Log.d(TAG, "onClick: bt_Addaddress_aco enter");
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ConfirmOrderActivity.this);
            alertDialog.setTitle("Select Address");
            String[] items =new String[noofaddress];//= {"java","android","Data Structures","HTML","CSS"};
            int icount=0;
            for (Address a : mList) {
                if(icount==0){
                    chosenAddress=a;
                }
                String gname=a.getName();
                String gaddress = a.getAddress();
                String gcity =a.getCity();
                String glandmark = a.getLandmark();
                String gpincode = a.getPincode();
                String gphno = a.getPhoneNo();
                gaddress=gname+"\n"+gaddress+", "+glandmark+", "+gcity.toUpperCase()+" - "+gpincode+"\n"+gphno;
                items[icount]=gaddress;
                Log.d(TAG," gaddress#"+gaddress);
                icount++;
            }

            for(String e:items){
                Log.d(TAG," nameeeee#"+e);
            }
            int checkedItem = 1;
            alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    int countlist=0;
                    if (mList != null) {
                        for (Address a : mList) {
                            chosenAddress=a;
                            if(countlist==which) {
                                mTextViewAddName.setText(a.getName());
                                String gaddress = String.valueOf(a.getAddress());
                                String gcity = String.valueOf(a.getCity());
                                String glandmark = String.valueOf(a.getLandmark());
                                String gpincode = String.valueOf(a.getPincode());
                                gaddress = gaddress + ", " + glandmark + ", " + gcity.toUpperCase() + " - " + gpincode;
                                String gphno = String.valueOf(a.getPhoneNo());
                                mTextViewAddFull.setText(gaddress);
                                mTextViewAddPhno.setText(gphno);
                                break;
                            }
                            countlist++;
                        }
                    }
                }
            });
            AlertDialog alert = alertDialog.create();
            alert.setCanceledOnTouchOutside(true);
            alert.show();
        }
    }
}
