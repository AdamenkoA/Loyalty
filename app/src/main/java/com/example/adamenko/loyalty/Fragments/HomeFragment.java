package com.example.adamenko.loyalty.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.adamenko.loyalty.Crypter.EncodeAsBitmap;
import com.example.adamenko.loyalty.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

public class HomeFragment extends Fragment {
    private int mColumnCount = 1;
    private static final String ARG_COLUMN_COUNT = "column-count";
    public HomeFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);


        String barcode_data;
        if (this.getArguments() != null) {
            if (!this.getArguments().getString("message").equals("")) {
                barcode_data = this.getArguments().getString("message");
            } else {
                barcode_data = "101010";
            }
        } else {
            barcode_data = "101010";
        }
        // barcode image
        Bitmap bitmap = null;
        ImageView iv = (ImageView) rootView.findViewById(R.id.bar_code_view);

        try {
            EncodeAsBitmap eab=new EncodeAsBitmap();
            bitmap =  eab.encodeAsBitmap(barcode_data, BarcodeFormat.CODE_128, 600, 300);
            iv.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }

        // Inflate the layout for this fragment
        return rootView;
    }





}
