package com.example.checkplease;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<String> data = new ArrayList<String>();
    private static LayoutInflater inflater=null;
    
    public LazyAdapter(Activity a, ArrayList<String> d) {
        activity = a;
        for( int i = 0; i < d.size(); i++ ){
        	data.add( d.get(i) );
        }
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.detalles_item, null);

        EditText text=(EditText)vi.findViewById(R.id.precio);
        text.setText(data.get(position));
        return vi;
    }
}