package com.example.clemw.checklist;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MyActivity extends Activity {

    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new ListAdapter (this);
        listView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        List<String> placeNames = new ArrayList<String>();
        placeNames.add(0, "Tartine");
        placeNames.add(1, "Delfina");
        placeNames.add(2, "Bi-Rite");

//                JsonParser atomParser = new JsonParser();
//        atomParser.parse(new JsonParser.ParseCompleteCallback() {
//            @Override
//            public void onParseComplete(List<String> placeNames) {
//                Log.i("first name", placeNames.get(0));
//                adapter.setPlaceNames(placeNames);
//                adapter.notifyDataSetChanged();
//            }
//        });

//        for (int i = 0; i < placeNames.size(); i++) {
//            Log.i("name", placeNames.get(i));
//        }
//
        adapter.setPlaceNames(placeNames);
//        adapter.setPlaceNames(placeNames);
//        adapter.notifyDataSetChanged();




//        JsonParser atomParser = new JsonParser();
//        atomParser.parse(new JsonParser.ParseCompleteCallback() {
//            @Override
//            public void onParseComplete(List<String> placeNames) {
//                Log.i("first name", placeNames.get(0));
////                adapter.setPlaceNames(placeNames);
////                adapter.notifyDataSetChanged();
//            }
//        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
