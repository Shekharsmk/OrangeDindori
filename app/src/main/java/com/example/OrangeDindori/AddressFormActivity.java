package com.example.OrangeDindori;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.OrangeDindori.staticData.Address;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class AddressFormActivity extends AppCompatActivity {
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference  mDatabaseReferenceAddress;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_form);
        final EditText mname=(EditText)findViewById(R.id.address_form_get_name);
        final EditText mlandmark=(EditText)findViewById(R.id.address_form_get_landmark);
        final EditText mfulladd=(EditText)findViewById(R.id.address_form_get_fulladdress);
        final EditText mcity=(EditText)findViewById(R.id.address_form_get_city);
        final EditText mphno=(EditText)findViewById(R.id.address_form_get_phoneno);
        final EditText mpin=(EditText)findViewById(R.id.address_form_get_pincode);
        Button mSubmit = (Button)findViewById(R.id.bt_add_address_form_data);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        userId = mFirebaseUser.getUid();
          mDatabaseReferenceAddress = mFirebaseDatabase.getReference().child("Customer").child(userId).child("Addresses");


        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code for random string
                Random ran = new Random();
                int r = ran.nextInt(99999999);
                String ranString = String.valueOf(r);

                Address address=new Address();
                address.setName(mname.getText().toString());
                address.setCity(mcity.getText().toString());
                address.setAddress(mfulladd.getText().toString());
                address.setLandmark(mlandmark.getText().toString());
                address.setPhoneNo(mphno.getText().toString());
                address.setPincode(mpin.getText().toString());
                address.setPrimary(false);

                if(mname.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Enter name", Toast.LENGTH_SHORT).show();
                }else if(mfulladd.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Enter Address", Toast.LENGTH_SHORT).show();
                }else if(mcity.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Enter city", Toast.LENGTH_SHORT).show();
                }else if(mpin.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Enter Pincode", Toast.LENGTH_SHORT).show();
                }else if(mphno.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please Enter Phone no.", Toast.LENGTH_SHORT).show();
                }else{
                mDatabaseReferenceAddress.child(ranString).setValue(address);
                Toast.makeText(getApplicationContext(), "Address added successfully", Toast.LENGTH_SHORT).show();
                finish();}
            }
        });
    }
}
