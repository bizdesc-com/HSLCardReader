package com.bizdesc.birhanu.myapplication;

import android.app.ActionBar;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bizdesc.birhanu.data.Card;
import com.bizdesc.birhanu.cardapi.CardAPI;
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
  public final static String CARDS = "com.bizdesc.myapplication.CARDS";
  private static final String DUMMY_CREDENTIALS = "user@test.com:hello";

  private UserLoginTask userLoginTask = null;
  private View loginFormView;
  private View progressView;
  private AutoCompleteTextView emailTextView;
  private EditText passwordTextView;
  private TextView signUpTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    emailTextView = (AutoCompleteTextView) findViewById(R.id.email);
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

    Button loginButton = (Button) findViewById(R.id.email_sign_in_button);
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
    AutoCompleteTextView emailText = (AutoCompleteTextView) findViewById(R.id.email);
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
