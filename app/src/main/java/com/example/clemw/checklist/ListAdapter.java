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
    private List<String> placeNames = new ArrayList<String>();

    @Override
    public int getCount() {
        return placeNames.size();
    }

    @Override
    public Object getItem(int position) {
        if (position >= placeNames.size()) {
            return null;
        }
        return placeNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    //http://stackoverflow.com/questions/13889225/create-views-programmatically-using-xml-on-android
    //can't use findById here to grab the text view of the inflated layout. need to figure out
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.list_item, null, false);
//        return layout;
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) { // create a new view if no recycling available
            textView = new TextView(context);
        } else {
            textView = (TextView) convertView;
        }
        if (position >= placeNames.size()) { // data not yet downloaded!
            return textView;
        }
        String placeName = placeNames.get(position);
//        Bitmap bitmap = imageCache.get(imageUrl);
        if (placeName != null) {
            textView.setText(placeName);
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

    public ListAdapter(Context c) {
        context = c;
    }

    public void setPlaceNames(List<String> placeNames) {
        this.placeNames = placeNames;
    }

}
