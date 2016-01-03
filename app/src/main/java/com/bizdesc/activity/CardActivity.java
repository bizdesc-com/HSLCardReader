package com.bizdesc.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.bizdesc.adapter.CardListAdapter;
import com.bizdesc.data.Card;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CardActivity extends AppCompatActivity {
  private static final String TAG = CardActivity.class.getSimpleName();
  private ListView listView;
  private CardListAdapter listAdapter;
  private List<Card> cards;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_card);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // Get the message from the intent
    Intent intent = getIntent();
    Card card1 = new Card("Birhanu Hailemariam", "Matkakortti", new BigDecimal(23.334), new Date());
    Card card2 = new Card("Jhon Boyer", "Travel card", new BigDecimal(43.12), new Date());
    cards = new ArrayList<>();
    cards.add(card1);
    cards.add(card2);

    listView = (ListView) findViewById(R.id.list);
    listAdapter = new CardListAdapter(this, cards);
    listView.setAdapter(listAdapter);


    /**
    // Create the text view
    TextView textView = new TextView(this);
    textView.setTextSize(40);
    textView.setText(message);

    // Set the text view as the activity layout
    setContentView(textView);
    **/
    //if (savedInstanceState == null) {
    //  getSupportFragmentManager().beginTransaction()
    //      .add(R.id.container, new PlaceholderFragment()).commit();
    //}
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle app bar item clicks here. The app bar
    // automatically handles clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * A placeholder fragment containing a simple view.
   */
  public static class PlaceholderFragment extends Fragment {

    public PlaceholderFragment() { }

    //@Override
    //public View onCreateView(LayoutInflater inflater, ViewGroup container,
    //                         Bundle savedInstanceState) {
    //  View rootView = inflater.inflate(R.layout.fragment_display_message,
    //      container, false);
    //  return rootView;
    //}
  }
}
