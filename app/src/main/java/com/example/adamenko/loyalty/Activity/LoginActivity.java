package com.example.adamenko.loyalty.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adamenko.loyalty.Content.SettingsContent;
import com.example.adamenko.loyalty.Crypter.StringCrypter;
import com.example.adamenko.loyalty.DataBase.MySQLiteHelper;
import com.example.adamenko.loyalty.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private com.example.adamenko.loyalty.Activity.LoginActivity.UserLoginTask mAuthTask = null;
    private SharedPreferences sPref;
    final String SAVED_TEXT = "saved";
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private String fileName = R.string.file_name + "";
    private String decryptedStr = "";
    private String strLine = "";
    private StringCrypter crypter = new StringCrypter();
    private Intent mIntent = new Intent();
    private   MySQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            File tempFile = new File(getBaseContext().getCacheDir().getPath() + "/" + fileName);
            FileReader fReader = new FileReader(tempFile);
            BufferedReader bReader = new BufferedReader(fReader);
            while ((strLine = bReader.readLine()) != null) {
                decryptedStr = crypter.decrypt(strLine).toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db = new MySQLiteHelper(this);


        Intent myIntent = new Intent(LoginActivity.this, Home.class);
        myIntent.putExtra("ITEM_ID", decryptedStr);
        startActivity(myIntent);
        finish();
        if (decryptedStr.equals("")) {
            mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

            mPasswordView = (EditText) findViewById(R.id.password);
            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == R.id.login || id == EditorInfo.IME_NULL) {
                        attemptLogin();
                        return true;
                    }
                    return false;
                }
            });

            mEmailView.addTextChangedListener(new PhoneNumberFormattingTextWatcher() {
                private boolean backspacingFlag = false;
                private boolean editedFlag = false;
                private int cursorComplement;

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    cursorComplement = s.length() - mEmailView.getSelectionStart();
                    if (count > after) {
                        backspacingFlag = true;
                    } else {
                        backspacingFlag = false;
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String string = s.toString();
                    String phone = string.replaceAll("[\\W]", "");
                    if (!editedFlag) {
                        if (phone.length() >= 2 && !backspacingFlag) {
                            editedFlag = true;
                            String ans = phone.replaceAll("[\\W]", "");
                            mEmailView.setText(ans);
                            mEmailView.setSelection(mEmailView.getText().length() - cursorComplement);
                        }
                    } else {
                        editedFlag = false;
                    }
                }
            });

            mPasswordView.addTextChangedListener(new PhoneNumberFormattingTextWatcher() {
                private boolean backspacingFlag = false;
                private boolean editedFlag = false;
                private int cursorComplement;

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    cursorComplement = s.length() - mPasswordView.getSelectionStart();
                    if (count > after) {
                        backspacingFlag = true;
                    } else {
                        backspacingFlag = false;
                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String string = s.toString();
                    String phone = string.replaceAll("[^\\d]", "");
                    if (!editedFlag) {
                        if (phone.length() >= 11 && !backspacingFlag) {
                            editedFlag = true;
                            String ans = phone.substring(0, 1) + "(" + phone.substring(1, 4) + ")" + phone.substring(4);
                            mPasswordView.setText(ans);
                            mPasswordView.setSelection(mPasswordView.getText().length() - cursorComplement);
                        }
                    } else {
                        editedFlag = false;
                    }
                }
            });

            Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
            mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });

            mLoginFormView = findViewById(R.id.login_form);
            mProgressView = findViewById(R.id.login_progress);
        } else {
//
//            Intent myIntent = new Intent(LoginActivity.this, Home.class);
//            myIntent.putExtra("ITEM_ID", decryptedStr);
//            startActivity(myIntent);
//            finish();
        }


    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        String passwordCheck = password.replaceAll("[^\\d]", "");
        if (password.isEmpty() || passwordCheck.length() >= 11) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        // Check for a valid email address.
        if (email.isEmpty()) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        AsyncHttpClient client = new AsyncHttpClient();
        HashMap<String, String> param = new HashMap<String, String>();

        FirebaseMessaging.getInstance().subscribeToTopic("news");
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        String deviceId = FirebaseInstanceId.getInstance().getId();

        param.put("name", email);
        param.put("phone", password);
        param.put("token", refreshedToken);
        //    param.put("token", deviceId);
        param.put("app", "AIzaSyB2zA4TL9napLFnR0cNI_I9gcdfg9qmZ6g");

        if (cancel) {
            focusView.requestFocus();
        } else {

            showProgress(true);
            RequestParams params = new RequestParams(param);
            client.post("https://safe-forest-50436.herokuapp.com/api/contacts", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Toast.makeText(com.example.adamenko.loyalty.Activity.LoginActivity.this,
                            "Success", Toast.LENGTH_SHORT).show();
                    try {
                        String value = new String(responseBody);
                        JSONObject valueTrue = new JSONObject(value);
                        String barCode = new JSONObject(valueTrue.getString("contact")).getString("code");
                        try {
                            String code = new String(responseBody, "UTF-8");

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        File tempFile = new File(getBaseContext().getCacheDir().getPath() + "/" + fileName);
                        FileWriter writer = null;
                        try {
                            writer = new FileWriter(tempFile);
                            StringCrypter crypter = new StringCrypter();
                            String encBase64Str = crypter.encrypt(barCode);
                            writer.write(encBase64Str);

                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        db.addSettings(new SettingsContent(1,"BarCode",barCode));
                        Intent myIntent = new Intent(LoginActivity.this, Home.class);
                        myIntent.putExtra("ITEM_ID", barCode);
                        startActivity(myIntent);
                        finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(com.example.adamenko.loyalty.Activity.LoginActivity.this, "Error" + statusCode + "", Toast.LENGTH_SHORT).show();
                    try {
                        String value = new String(responseBody);
                        String code = new String(responseBody, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

            });
        }

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), com.example.adamenko.loyalty.Activity.LoginActivity.ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(com.example.adamenko.loyalty.Activity.LoginActivity.ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(com.example.adamenko.loyalty.Activity.LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }


            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

