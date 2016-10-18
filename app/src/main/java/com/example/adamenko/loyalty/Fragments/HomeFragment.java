package com.example.adamenko.loyalty.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.adamenko.loyalty.Crypter.EncodeAsBitmap;
import com.example.adamenko.loyalty.Crypter.StringCrypter;
import com.example.adamenko.loyalty.DataBase.MySQLiteHelper;
import com.example.adamenko.loyalty.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

public class HomeFragment extends Fragment {
    private int mColumnCount = 1;
    private static final String ARG_COLUMN_COUNT = "column-count";
    private MySQLiteHelper db;

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
        StringCrypter crypter = new StringCrypter();
        String barcode_data;
        db=new MySQLiteHelper(this.getContext());
        if (this.getArguments() != null && !this.getArguments().getString("message").equals("")) {
            barcode_data = this.getArguments().getString("message");
        } else {
            barcode_data = crypter.decrypt(db.getSettings("BarCode"));
        }

        if (barcode_data != null && barcode_data.equals("")) {
            barcode_data = "1010101";
        }

        Bitmap bitmap = null;
        ImageView iv = (ImageView) rootView.findViewById(R.id.bar_code_view);

        try {
            EncodeAsBitmap eab = new EncodeAsBitmap();
            bitmap = eab.encodeAsBitmap(barcode_data, BarcodeFormat.CODE_128, 600, 300);
            iv.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }

        return rootView;
    }


}
