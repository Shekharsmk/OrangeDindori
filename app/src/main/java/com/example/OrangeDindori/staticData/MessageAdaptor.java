package com.example.OrangeDindori.staticData;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.OrangeDindori.R;

import java.util.List;



public class MessageAdaptor extends ArrayAdapter<FriendlyMessage> {
    public  MessageAdaptor(Context context, int resource, List<FriendlyMessage> objects){
        super(context,resource,objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.message_item,parent,false);
        }

        TextView messageTextView = (TextView) convertView.findViewById(R.id.messageTextView);
        TextView autjorname = (TextView) convertView.findViewById(R.id.nameTextView);

        FriendlyMessage message = getItem(position);

        messageTextView.setText(message.getMessage());
        autjorname.setText(message.getName());


        return convertView;

    }
}
