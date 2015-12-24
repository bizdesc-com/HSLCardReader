package com.bizdesc.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bizdesc.birhanu.activity.R;
import com.bizdesc.cardapi.CardAPI;
import com.bizdesc.data.Card;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

  /**
   * ATTENTION: This was auto-generated to implement the App Indexing API.
   * See https://g.co/AppIndexing/AndroidStudio for more information.
   */
  private GoogleApiClient client;
  public final static String CARDS = "com.bizdesc.CARDS";
  private static final String DUMMY_CREDENTIALS = "user@test.com:hello";

  private UserLoginTask userLoginTask = null;
  private View loginFormView;
  private View progressView;
  private AutoCompleteTextView usernameTextView;
  private EditText passwordTextView;
  private TextView signUpTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    usernameTextView = (AutoCompleteTextView) findViewById(R.id.username);
    loadAutoComplete();

    passwordTextView = (EditText) findViewById(R.id.password);
    passwordTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
        if (id == EditorInfo.IME_NULL) {
          initLogin();
          return true;
        }
        return false;
      }
    });

    Button loginButton = (Button) findViewById(R.id.username_sign_in_button);
    loginButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        initLogin();
      }
    });

    loginFormView = findViewById(R.id.login_form);
    progressView = findViewById(R.id.login_progress);

    //adding underline and link to signup textview
    signUpTextView = (TextView) findViewById(R.id.signUpTextView);
    signUpTextView.setPaintFlags(signUpTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    Linkify.addLinks(signUpTextView, Linkify.ALL);

    signUpTextView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.i("LoginActivity", "Sign Up Activity activated.");
        // this is where you should start the signup Activity
        // LoginActivity.this.startActivity(new Intent(LoginActivity.this, SignupActivity.class));
      }
    });

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
      }
    });

    // Make sure we're running on Honeycomb or higher to use ActionBar APIs
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
      // For the login activity, make sure the app icon in the action bar
      // does not behave as a button
      ActionBar actionBar = getActionBar();
      actionBar.setHomeButtonEnabled(false);
    }

    // ATTENTION: This was auto-generated to implement the App Indexing API.
    // See https://g.co/AppIndexing/AndroidStudio for more information.
    client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
  }

  private void loadAutoComplete() {

    getLoaderManager().initLoader(0, null, this);
  }

  /**
   * Validate Login form and authenticate.
   */
  public void initLogin() {
    if (userLoginTask != null) {
      return;
    }

    usernameTextView.setError(null);
    passwordTextView.setError(null);

    String username = usernameTextView.getText().toString();
    String password = passwordTextView.getText().toString();

    boolean cancelLogin = false;
    View focusView = null;

    isUsernameValid(username, focusView, cancelLogin);
    isPasswordValid(password, focusView, cancelLogin);

    if (cancelLogin) {
      // error in login
      focusView.requestFocus();
    } else {
      // show progress spinner, and start background task to login
      showProgress(true);
      userLoginTask = new UserLoginTask(username, password);
      userLoginTask.execute((Void) null);
    }
  }

  //minor username validation
  private boolean isUsernameValid(String username, View focusView, boolean cancelLogin) {
    //add your own logic
    if (TextUtils.isEmpty(username)) {
      usernameTextView.setError(getString(R.string.field_required));
      focusView = usernameTextView;
      cancelLogin = true;
    } else if (username.length() < 6) {
      usernameTextView.setError(getString(R.string.short_username));
      focusView = usernameTextView;
      cancelLogin = true;
    }

    return true;
  }

  //minor password validation
  private boolean isPasswordValid(String password, View focusView, boolean cancelLogin) {
    //validation checker for HSL password
    if (password.length() < 8) {
      passwordTextView.setError(getString(R.string.short_password));
      focusView = passwordTextView;
      cancelLogin = true;
    } else if ((password.length() > 15)) {
      passwordTextView.setError(getString(R.string.long_password));
      focusView = passwordTextView;
      cancelLogin = true;
    } else if (!TextUtils.isEmpty(password)) {
      passwordTextView.setError(getString(R.string.empty_password));
      focusView = passwordTextView;
      cancelLogin = true;
    }
    return true;
  }

  /**
   * Shows the progress UI and hides the login form.
   */
  @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
  public void showProgress(final boolean show) {
    // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
    // for very easy animations. If available, use these APIs to fade-in
    // the progress spinner.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
      int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

      loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
      loginFormView.animate().setDuration(shortAnimTime).alpha(
          show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
          loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
      });

      progressView.setVisibility(show ? View.VISIBLE : View.GONE);
      progressView.animate().setDuration(shortAnimTime).alpha(
          show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
          progressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
      });
    } else {
      // The ViewPropertyAnimator APIs are not available, so simply show
      // and hide the relevant UI components.
      progressView.setVisibility(show ? View.VISIBLE : View.GONE);
      loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
  }

  @Override
  public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
    return new CursorLoader(this,
        // Retrieve data rows for the device user's 'profile' contact.
        Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
            ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

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
    List<String> emails = new ArrayList<String>();
    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      emails.add(cursor.getString(ProfileQuery.ADDRESS));
      cursor.moveToNext();
    }

    addUsernamesToAutoComplete(emails);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> cursorLoader) {

  }

  private void addUsernamesToAutoComplete(List<String> emailAddressCollection) {
    //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
    ArrayAdapter<String> adapter =
        new ArrayAdapter<String>(LoginActivity.this,
            android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

    usernameTextView.setAdapter(adapter);
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
   * Async Login Task to authenticate
   */
  public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

    private final String usernameStr;
    private final String passwordStr;

    UserLoginTask(String email, String password) {
      usernameStr = email;
      passwordStr = password;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
      //this is where you should write your authentication code
      // or call external service
      // following try-catch just simulates network access
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        return false;
      }

      //using a local dummy credentials store to authenticate
      String[] pieces = DUMMY_CREDENTIALS.split(":");
      if (pieces[0].equals(usernameStr) && pieces[1].equals(passwordStr)) {
        return true;
      } else {
        return false;
      }
    }

    @Override
    protected void onPostExecute(final Boolean success) {
      userLoginTask = null;
      //stop the progress spinner
      showProgress(false);

      if (success) {
        //  login success and move to main Activity here.
      } else {
        // login failure
        passwordTextView.setError(getString(R.string.incorrect_password));
        passwordTextView.requestFocus();
      }
    }

    @Override
    protected void onCancelled() {
      userLoginTask = null;
      showProgress(false);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  /**
   * Called when the user clicks the Send button
   */
  public void sendMessage(View view) {
    Intent intent = new Intent(this, CardActivity.class);
    AutoCompleteTextView emailText = (AutoCompleteTextView) findViewById(R.id.username);
    EditText passwordText = (EditText) findViewById(R.id.password);
    String email = emailText.getText().toString();
    String password = passwordText.getText().toString();
    CardAPI cardAPI = new CardAPI(email, password);
    List<Card> cards = null;
    try {
      cards = cardAPI.getCards();
    } catch (Exception e) {
      e.printStackTrace();
    }
    intent.putExtra(CARDS, (ArrayList) cards);
    startActivity(intent);
  }


}
