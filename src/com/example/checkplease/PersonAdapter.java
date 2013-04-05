package com.example.checkplease;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class PersonAdapter extends ArrayAdapter<Person> {
	
	private Context context;
	private int layoutResourceId;
	private ArrayList<Person> usuarios;
	private float propina;
	
	public PersonAdapter(Context context, int layoutResourceId, ArrayList<Person> usuarios, float propina) {
        super(context, layoutResourceId, usuarios);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.usuarios = usuarios;
        this.propina = propina;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	        View view = convertView;
	        if (view == null) {
	            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	            view = inflater.inflate(layoutResourceId, parent, false);
	        }
	        Person p = usuarios.get(position);
	        float tTip;
	        if (p != null) {
	                ImageButton ib = (ImageButton)view.findViewById(R.id.ibPicture);
	                EditText etTotal = (EditText)view.findViewById(R.id.etTotal);
	                EditText etTotalTip = (EditText)view.findViewById(R.id.etTotalTip);
	                CheckBox cb = (CheckBox)view.findViewById(R.id.cbPaid);
	                cb.setChecked(p.isPaid());
	                etTotal.setText(String.valueOf(p.getTotal()));
	                tTip = p.getTotal() * (propina / 10.0f) + p.getTotal();
	                etTotalTip.setText(String.valueOf(tTip));
	        }
	        return view;
	}

}
