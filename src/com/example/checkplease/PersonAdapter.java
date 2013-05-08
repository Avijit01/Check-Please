package com.example.checkplease;

import com.checkplease.R;
import java.math.BigDecimal;
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

/**
 * Objeto que crea cad elemento de la lista 
 * de usuarios y asigna los valores apropiado
 * @author Cesar
 *
 */
public class PersonAdapter extends ArrayAdapter<Person> {

	private Context context;
	private int layoutResourceId;
	private ArrayList<Person> usuarios;
	private float propina;
	private int action;
	private ArrayList<Integer> positions;

	/**
	 * Contructor del Objeto adapter
	 * @param context
	 * @param layoutResourceId
	 * @param usuarios
	 * @param propina
	 * @param action
	 * @param positions
	 */
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
				TextView ib = (TextView)view.findViewById(R.id.ibNombre);
				TextView tvTotal = (TextView)view.findViewById(R.id.tvTotal);
				TextView tvTotalTip = (TextView)view.findViewById(R.id.tvTotalTip);
				CheckBox cb = (CheckBox)view.findViewById(R.id.cbPaid);
				ib.setText(p.getName());
				ib.setOnClickListener(new  CustomOnClickListener(position){
					public void onClick(View view){
						//llama a la actividad Detalles
						Intent intent = new Intent(view.getContext(), Detalles.class);
						intent.putExtra("viene", "personAdapter");
						intent.putExtra("Nombre", p.getName());
						intent.putExtra("Picture", p.getPicture());
						intent.putExtra("Total", p.getTotal());
						intent.putExtra("Path", p.getPicture());
						intent.putExtra("Position", this.getPosition());
						intent.putExtra("Paid", p.isPaid());
						intent.putExtra("IdUsr", p.getuId());
						context.startActivity(intent);
					}
				});
				tvTotal.setOnClickListener(new  CustomOnClickListener(position){
					public void onClick(View view){
						Intent intent = new Intent(view.getContext(), Calculadora.class);
						intent.putExtra("Position", this.getPosition());
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
				tTip = p.getTotal() * (propina / 100.0f) /*+ p.getTotal()*/;
				int decimalPlaces = 2;
				BigDecimal bd = new BigDecimal(tTip);
				// setScale is immutable
				bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
				tTip = bd.floatValue();
				tvTotalTip.setText(String.valueOf(tTip));
				p.setTotalTip(tTip);
			}
		}
		// Vista para eliminar usuarios
		else if(action == 1) {
			if (p != null) {
				//ImageView iv = (ImageView)view.findViewById(R.id.ivPictureDelete);
				TextView tvNombre = (TextView)view.findViewById(R.id.ibNombreDelete);
				TextView tvTotal = (TextView)view.findViewById(R.id.tvTotalDelete);
				final CheckBox cb = (CheckBox)view.findViewById(R.id.cbDelete);
				tvNombre.setText(p.getName());
				tvTotal.setText(String.valueOf(p.getTotal()));
				Log.e("nombres", p.getName());
				
				/*if(!p.getPicture().equals("null")) {
					iv.setImageBitmap(BitmapFactory.decodeFile(p.getPicture()));
				}*/
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
