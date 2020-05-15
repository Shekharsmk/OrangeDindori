package com.example.OrangeDindori;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.OrangeDindori.staticData.Address;
import com.example.OrangeDindori.staticData.CreateCart;
import com.example.OrangeDindori.staticData.UserAccount;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyAddressesActivity extends AppCompatActivity implements MyAddressesAdaptor.ListItemListener {


    private static final String TAG = "address";
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReferenceAddress;
    DatabaseReference mDatabaseReferenceCheckAddresses;

    private AlertDialog.Builder alertDialog;

    private String userId;
    private RecyclerView mRecyclerView;
    private List<Address> mListAddress;
    private List<String> mListAddressKey;
    private int noOfProduct, totalbill = 0;
    private MyAddressesAdaptor mAdaptor;
    private Toast mToast;
    private Button proceed;
    private UserAccount account;
    private LinearLayout mLinearLayout1;
    private LinearLayout mLinearLayout2;
    private Address ad1, ad2;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_addresses);

        final Button btAddress = (Button) findViewById(R.id.bt_Addaddress);
        ActionBar actionBar = this.getSupportActionBar();

        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        alertDialog = new AlertDialog.Builder(MyAddressesActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("No Internet Connection");
        alertDialog.setMessage("We can not detect any internet connection please check your internet connection and try again");

        alertDialog.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
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

        final List<Address> mList = new ArrayList<Address>();
        final List<String> mListKey = new ArrayList<String>();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        userId = mFirebaseUser.getUid();
        mDatabaseReferenceAddress = mFirebaseDatabase.getReference().child("Customer").child(userId).child("Addresses");

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_addressinfo);
        proceed = (Button) findViewById(R.id.bt_Addaddress);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mDatabaseReferenceAddress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {

                    Intent instentdestion = new Intent(MyAddressesActivity.this, AddressFormActivity.class);
                    startActivity(instentdestion);

                } else {
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    for (DataSnapshot singleDataSnapshot : children) {
                        Address obj = singleDataSnapshot.getValue(Address.class);
                        String objs = singleDataSnapshot.getKey();
                        mList.add(obj);
                        mListKey.add(objs);
                        noOfProduct += 1;

                    }
                    mListAddress = null;
                    mListAddress = mList;
                    mListAddressKey = null;
                    mListAddressKey = mListKey;

                    mRecyclerView = null;
                    mRecyclerView = (RecyclerView) findViewById(R.id.rv_addressinfo);

                    mRecyclerView.setHasFixedSize(true);
                    mAdaptor = new MyAddressesAdaptor(noOfProduct, mListAddress, MyAddressesActivity.this);
                    mRecyclerView.setAdapter(mAdaptor);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent instentdestion = new Intent(MyAddressesActivity.this, AddressFormActivity.class);
                startActivity(instentdestion);

            }
        });
    }

        private boolean isNetworkAvailable () {

            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        private void showSnack ( boolean isConnected){
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


  /*  @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }*/

        @Override
        public void onItemClicked ( int clickItemIndex, List<Address > filerList){

        }



}