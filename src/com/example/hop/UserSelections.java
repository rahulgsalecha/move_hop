package com.example.hop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class UserSelections extends Activity
{
  private static String logtag = "User Selections Page";
  public String mover_data;
  public String mover_location;
  private TextView moverdata;
  private TextView moverlocation;

  public void onBackPressed()
  {
    setResult(-1, new Intent());
    finish();
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903044);
    moverdata = ((TextView)findViewById(R.id.mover_data));
    moverlocation = ((TextView)findViewById(R.id.mover_location));
    Intent localIntent = getIntent();
    localIntent.getStringExtra("user");
    localIntent.getStringExtra("start_time");
    localIntent.getStringExtra("end_time");
    localIntent.getStringExtra("date");
    localIntent.getStringExtra("location");
    
    ParseQuery<ParseObject> query = ParseQuery.getQuery("MoverDataStore");
    query.whereEqualTo("mover", "user");
    query.getFirstInBackground(new GetCallback<ParseObject>() {
    	public void done(ParseObject obj, ParseException e) {
        if (e == null)
        {
         moverdata.setText(obj.getString("mover"));
         moverlocation.setText(obj.getString("location"));
        }
      }
    });
  }

  protected void onDestroy()
  {
    Log.d(logtag, "onDestroy() called");
    super.onDestroy();
  }

  protected void onPause()
  {
    Log.d(logtag, "onPause() called");
    super.onPause();
  }

  protected void onRestart()
  {
    super.onRestart();
  }

  protected void onResume()
  {
    Log.d(logtag, "onResume() called");
    super.onResume();
  }

  protected void onStart()
  {
    Log.d(logtag, "onStart() called");
    super.onStart();
  }

  protected void onStop()
  {
    Log.d(logtag, "onStop() called");
    super.onStop();
  }
}

/* Location:           /Users/rsalecha/Downloads/dex2jar-0.0.9.15/classes_dex2jar.jar
 * Qualified Name:     com.example.hop.UserSelections
 * JD-Core Version:    0.6.2
 */