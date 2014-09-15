package il.ac.technion.socialcampus;

import il.ac.technion.logic.LocationFactury;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MapPickerActivity extends FragmentActivity {

	private GoogleMap mMap;
	private Button addButon;
	private Button cancelButon;
	private double lat;
	private double lng;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_picker);
		this.setFinishOnTouchOutside(false);
		lat = (Double) getIntent().getExtras().get("lat");
		lng = (Double) getIntent().getExtras().get("lng");
		setUpMapIfNeeded();
		setResult(RESULT_CANCELED);

		addButon = (Button)findViewById(R.id.add_button);
		cancelButon = (Button)findViewById(R.id.cancel_button);
		addButon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 final LatLng gp = mMap.getCameraPosition().target;
		    	  setResult(RESULT_OK, new Intent().
		    			  putExtra("lat",gp.latitude).putExtra("lng",gp.longitude));
		    	  finish();
				
			}
		});
		cancelButon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
		    	  setResult(RESULT_CANCELED);
				finish();
			}
		});
	}
	
	private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            mMap.setPadding(5, 5, 5, 135);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
            	setCamera();
            }
        }
    }
	
	private void setCamera(){
		 CameraPosition POS = LocationFactury.buildCameraPosition(lat,lng);
		 mMap.moveCamera(CameraUpdateFactory.newCameraPosition(POS));
		 mMap.setMyLocationEnabled(true);

	}

	 @Override
	public void onBackPressed() {
    	  setResult(RESULT_CANCELED);
		finish();
	}

}
