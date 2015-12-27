package com.bizdesc.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.bizdesc.adapter.CardListAdapter;
import com.bizdesc.data.Card;

import java.util.ArrayList;
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

    // Get the message from the intent
    Intent intent = getIntent();
    cards = (ArrayList) intent.getSerializableExtra(LoginActivity.CARDS);

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
