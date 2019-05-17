package com.brunel.team28.maraudersmap.Geofences;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.brunel.team28.maraudersmap.R;

import java.util.List;

/**
 * RecyclerView adapter that transforms geofences to instances of CardView
 */

public class AllGeofencesAdapter extends RecyclerView.Adapter<AllGeofencesAdapter.ViewHolder> {

	// Region Properties
	private List<NamedGeofence> namedGeofences;
	private AllGeofencesAdapterListener listener;

	void setListener(AllGeofencesAdapterListener listener) {
		this.listener = listener;
	}

	// Constructors
	AllGeofencesAdapter(List<NamedGeofence> namedGeofences) {
		this.namedGeofences = namedGeofences;
	}

	// Region Overrides
	@Override
	public AllGeofencesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		ViewGroup v = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_geofence, parent, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		final NamedGeofence geofence = namedGeofences.get(position);

		holder.name.setText(geofence.name);
		holder.longitude.setText(String.valueOf(geofence.latitude) + holder.latitide.getResources().getString(R.string.Units_Degrees));
		holder.latitide.setText(String.valueOf(geofence.longitude) + holder.longitude.getResources().getString(R.string.Units_Degrees));
		holder.radius.setText(String.valueOf(geofence.radius / 1000.0) + " " + holder.radius.getResources().getString(R.string.Units_Kilometers));

		holder.deleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
				builder.setMessage(R.string.AreYouSure)
						.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								if (listener != null) {
									listener.onDeleteTapped(geofence);
								}
							}
						})
						.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// User cancelled the dialog
							}
						})
						.create()
						.show();
			}
		});

	}

	@Override
	public int getItemCount() {
		return namedGeofences.size();
	}

	// Region Interfaces
	interface AllGeofencesAdapterListener {
		void onDeleteTapped(NamedGeofence namedGeofence);
	}

	// Region Inner classes
	static class ViewHolder extends RecyclerView.ViewHolder {
		TextView name;
		TextView latitide;
		TextView longitude;
		TextView radius;
		Button deleteButton;

		ViewHolder(ViewGroup v) {
			super(v);

			name = (TextView) v.findViewById(R.id.listitem_geofenceName);
			latitide = (TextView) v.findViewById(R.id.listitem_geofenceLatitude);
			longitude = (TextView) v.findViewById(R.id.listitem_geofenceLongitude);
			radius = (TextView) v.findViewById(R.id.listitem_geofenceRadius);
			deleteButton = (Button) v.findViewById(R.id.listitem_deleteButton);
		}
	}
}


