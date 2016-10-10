package com.example.adamenko.loyalty.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
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
import com.example.adamenko.loyalty.OnMyRequestListener;
import com.example.adamenko.loyalty.R;
import com.example.adamenko.loyalty.Request.RequestToHeroku;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private com.example.adamenko.loyalty.Activity.LoginActivity.UserLoginTask mAuthTask = null;

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private MySQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new MySQLiteHelper(this);

        if (db.getSettings("BarCode").equals("")) {
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
                    backspacingFlag = count > after;
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
                    backspacingFlag = count > after;
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String string = s.toString();
                    String phone = string.replaceAll("[^\\d]", "");
                    if (!editedFlag) {
                        if (phone.length() >= 12 && !backspacingFlag) {
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

            Intent myIntent = new Intent(LoginActivity.this, Home.class);
            myIntent.putExtra("ITEM_ID", db.getSettings("BarCode"));
            startActivity(myIntent);
            finish();
        }


    }

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

        HashMap<String, String> param = new HashMap<String, String>();
        FirebaseMessaging.getInstance().subscribeToTopic("news");

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        String deviceId = FirebaseInstanceId.getInstance().getId();

        List<FirebaseApp> app = FirebaseApp.getApps(this);
        FirebaseApp al = app.get(0);
        FirebaseOptions aMz = al.getOptions();
        String ve = aMz.getApiKey();

        db = new MySQLiteHelper(this);
        db.addSettings(new SettingsContent(1, "app", ve));

        param.put("name", email);
        param.put("phone", password);
        param.put("token", refreshedToken);
        param.put("app", ve);

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            RequestToHeroku rth = new RequestToHeroku();
            rth.HerokuPost(param, "contacts", new OnMyRequestListener() {
                @Override
                public void onSuccess(JSONObject valueTrue) {

                    Toast.makeText(com.example.adamenko.loyalty.Activity.LoginActivity.this,
                            "Success", Toast.LENGTH_SHORT).show();
                    try {
                        String barCode = new JSONObject(valueTrue.getString("contact")).getString("code");
                        StringCrypter crypter = new StringCrypter();
                        String encBase64Str = crypter.encrypt(barCode);
                        db.addSettings(new SettingsContent(2, "BarCode", encBase64Str));

                        Intent myIntent = new Intent(LoginActivity.this, Home.class);
                        myIntent.putExtra("ITEM_ID", barCode);
                        startActivity(myIntent);
                        finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String value) {

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

