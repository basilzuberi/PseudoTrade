package com.project.pseudotrade;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import android.os.Bundle;
import android.widget.ArrayAdapter;

public class webAdapter extends ArrayAdapter<Webs> {
    private Context mContext;
    private int mResource;
    String[] urls;


    public webAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Webs> objects, String[] urls) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.urls = urls;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource,parent,false);
        //view.setBackgroundColor(getColor(R.color. colorPrimaryDark ));
        ImageView imageView = convertView.findViewById(R.id.image);
        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtDes = convertView.findViewById(R.id.txtDes);
        imageView.setImageResource(getItem(position).getImage());
        txtName.setText(getItem(position).getName());
        txtDes.setText(getItem(position).getDes());
//        View row = super.getView(position, convertView, parent);
//        row.setBackground(Color.BLACK);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openLinksIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(urls[position]));
                getContext().startActivity(openLinksIntent);
            }
        });
        return convertView;
    }
}

