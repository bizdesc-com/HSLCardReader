package com.bizdesc.birhanu.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bizdesc.birhanu.data.Card;
import com.bizdesc.birhanu.myapplication.R;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by birhanu on 12/19/15.
 */
public class CardListAdapter extends BaseAdapter {
  private Activity activity;
  private LayoutInflater inflater;
  private List<Card> cards;

  public CardListAdapter(Activity activity, List<Card> cards) {
    this.activity = activity;
    this.cards = cards;
  }

  @Override
  public int getCount() {
    return cards.size();
  }

  @Override
  public Object getItem(int location) {
    return cards.get(location);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

    if (inflater == null)
      inflater = (LayoutInflater) activity
          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    if (convertView == null)
      convertView = inflater.inflate(R.layout.card_item, null);

    TextView name = (TextView) convertView.findViewById(R.id.name);
    TextView expiryDate = (TextView) convertView
        .findViewById(R.id.expiryDate);
    TextView cardId = (TextView) convertView
        .findViewById(R.id.cardId);
    TextView money = (TextView) convertView
        .findViewById(R.id.money);

    Card card = cards.get(position);

    name.setText(card.getName());

    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd,yyyy hh:mm a");
    expiryDate.setText("Expiry date: " + dateFormat.format(card.getPeriodExpiryDate()));

    cardId.setText(card.getId());

    money.setText(NumberFormat.getCurrencyInstance().format(card.getMoney()));
    
    return convertView;
  }

}
