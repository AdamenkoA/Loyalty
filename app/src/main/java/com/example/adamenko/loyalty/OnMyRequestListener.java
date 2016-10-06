package com.example.adamenko.loyalty;

import org.json.JSONObject;

/**
 * Created by Adamenko on 06.10.2016.
 */

public  interface OnMyRequestListener {
    void onSuccess(final JSONObject valueTrue);
    void onFailure( String value);
}