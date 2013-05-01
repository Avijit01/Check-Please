package com.example.checkplease;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PersonAdapter extends ArrayAdapter<Person> {

	private Context context;
	private int layoutResourceId;
	private ArrayList<Person> usuarios;
	private float propina;
	private int action;
	private ArrayList<Integer> positions;

	public PersonAdapter(Context context, int layoutResourceId, ArrayList<Person> usuarios, float propina, int action, ArrayList<Integer> positions) {
		super(context, layoutResourceId, usuarios);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.usuarios = usuarios;
		this.propina = propina;
		this.action = action;
		this.positions = positions;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			view = inflater.inflate(layoutResourceId, parent, false);
		}
		final Person p = usuarios.get(position);

		// Vista normal, despliega a los usuarios
		if(action == 0) {
			float tTip;
			if (p != null) {
				ImageButton ib = (ImageButton)view.findViewById(R.id.ibPicture);
				TextView tvTotal = (TextView)view.findViewById(R.id.tvTotal);
				TextView tvTotalTip = (TextView)view.findViewById(R.id.tvTotalTip);
				CheckBox cb = (CheckBox)view.findViewById(R.id.cbPaid);
				if(!p.getPicture().equals("null")) {
					ib.setImageBitmap(BitmapFactory.decodeFile(p.getPicture()));
				}
				ib.setOnClickListener(new  CustomOnClickListener(position){
					public void onClick(View view){
						Intent intent = new Intent(view.getContext(), Detalles.class);
						intent.putExtra("Nombre", p.getName());
						intent.putExtra("Picture", p.getPicture());
						intent.putExtra("Total", p.getTotal());
						intent.putExtra("Position", this.getPosition());
						context.startActivity(intent);
					}
				});
				tvTotal.setOnClickListener(new  CustomOnClickListener(position){
					public void onClick(View view){
						Intent intent = new Intent(view.getContext(), Calculadora.class);
						intent.putExtra("position", this.getPosition());
						context.startActivity(intent);
					}
				});
				cb.setOnCheckedChangeListener(new CustomOnCheckedChangeListener(p) {
					public void onCheckedChanged(CompoundButton cb, boolean paid){
						getPerson().setPaid(paid);
						((Lista)context).updateRemaining();
					}
				});
				cb.setChecked(p.isPaid());
				tvTotal.setText(String.valueOf(p.getTotal()));
				tTip = p.getTotal() * (propina / 100.0f) + p.getTotal();
				tvTotalTip.setText(String.valueOf(tTip));
				p.setTotalTip(tTip);
			}
		}
		// Vista para eliminar usuarios
		else if(action == 1) {
			if (p != null) {
				ImageView iv = (ImageView)view.findViewById(R.id.ivPictureDelete);
				TextView tvTotal = (TextView)view.findViewById(R.id.tvTotalDelete);
				final CheckBox cb = (CheckBox)view.findViewById(R.id.cbDelete);
				tvTotal.setText(String.valueOf(p.getTotal()));
				if(!p.getPicture().equals("null")) {
					iv.setImageBitmap(BitmapFactory.decodeFile(p.getPicture()));
				}
				view.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						cb.setChecked(cb.isChecked() ^ true);
					}
				});
				cb.setOnCheckedChangeListener(new CustomOnCheckedChangeListener(position) {
					public void onCheckedChanged(CompoundButton cb, boolean isChecked){
						int index;
						if(isChecked) {
							positions.add(this.getPosition());
						} else {
							index = positions.indexOf(this.getPosition());
							if(index > -1)
								positions.remove(index);
						}
					}
				});
			}
		}
		((Lista)context).updateTotal();
		((Lista)context).updateRemaining();
		return view;
	}

	public ArrayList<Person> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(ArrayList<Person> usuarios) {
		this.usuarios = usuarios;
	}
	
	public ArrayList<Integer> getPositions() {
		return positions;
	}
}
