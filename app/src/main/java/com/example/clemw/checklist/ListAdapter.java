package com.example.clemw.checklist;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends BaseAdapter {

    private final Context context;
    private List<Place> places = new ArrayList<Place>();

    public ListAdapter(Context c) {
        context = c;
    }

    public void setPlaces(List<Place> placeNames) {
        this.places = placeNames;
    }

    @Override
    public int getCount() {
        return places.size();
    }

    @Override
    public Object getItem(int position) {
        if (position >= places.size()) {
            return null;
        }
        return places.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) { // create a new view if no recycling available
            textView = new TextView(context);
        } else {
            textView = (TextView) convertView;
        }
        if (position >= places.size()) { // data not yet downloaded!
            return textView;
        }
        Place place = places.get(position);
//        Bitmap bitmap = imageCache.get(imageUrl);
        if (place != null) {
            textView.setText(place.getName());
        } else {
//            if (!downloadingImageUrls.contains(imageUrl)) {
//                Log.i("ImageAdapter", "!downloadingImageUrls.contains(imageUrl). downloading image. position: " + position);
//                downloadingImageUrls.add(imageUrl);
//                new DownloadImageAsyncTask(imageUrl).execute();
//            }
            notifyDataSetChanged();
        }
        return textView;
    }
}