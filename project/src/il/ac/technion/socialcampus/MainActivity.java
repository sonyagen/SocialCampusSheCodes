package il.ac.technion.socialcampus;

import il.ac.technion.logic.HotSpot;
import il.ac.technion.logic.HotSpotManager;
import il.ac.technion.logic.LocationFactury;
import il.ac.technion.logic.TagManager;
import il.ac.technion.logic.UiOnDone;
import il.ac.technion.logic.UiOnError;
import il.ac.technion.logic.UserManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends FragmentActivity implements InfoBoxFragment.ButtonInteraction	{
	private ImageView imgProfilePic;
	private ImageButton addNewBtn;
	private GoogleMap mMap;
	private final Context mContext = this;
	
	CameraPosition TECHNION = LocationFactury.TECHNION;
	
	private HashMap<String,Long> mMarkersHotSpotsTrans = new HashMap<String,Long>();
	private HashMap<Long,Marker> mHotSpotsMarkersTrans = new HashMap<Long,Marker>();

    @Override
	protected void onStart() {
		super.onStart();
	}

    private void syncDb(){
    	HotSpotManager.INSTANCE.syncHotSpots(new UiOnDone() {
			@Override
			public void execute() {
				setUpMapIfNeeded();
			}
		}, new UiOnError(this));
    	UserManager.INSTANCE.syncUsers(new UiOnDone() {
			@Override
			public void execute() {
				
				UserManager.INSTANCE.setCurrentUser(
						UserManager.getLoggedIn(getApplicationContext() )
				);
			}
		}, new UiOnError(this));
    	TagManager.INSTANCE.syncTags(new UiOnDone() {
			@Override
			public void execute() {}
		}, new UiOnError(this));
    }
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        if (savedInstanceState==null){
        	syncDb();
        }else{
        	setUpMapIfNeeded();
        }
        setListeners();
        hideInfoBox();
    }

    private void setListeners(){
    	imgProfilePic = (ImageView) findViewById(R.id.ProfilePic);
        imgProfilePic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
			}
		});
        addNewBtn = (ImageButton) findViewById(R.id.addNewBtn);
        addNewBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), CreateNewHotSpotActivity.class));
			}
		});
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(UserManager.isLoggedIn(mContext)){
        	UserManager.INSTANCE.getMyData().setUserPhoto(imgProfilePic);
        }
        hideInfoBox();
        setUpMap();
    }

    private void resetMarkers(){
    	List<HotSpot> hs = HotSpotManager.INSTANCE.getAllObjs();
    	if (mMap==null || hs==null) return; 
    	for(HotSpot h: hs){
        	Marker m = mMap.addMarker(new MarkerOptions().
        			position(new LatLng(h.getLangt(),h.getLongt())));

        	mMarkersHotSpotsTrans.put(m.getId(), h.getmId());
        	mHotSpotsMarkersTrans.put(h.getmId(), m);
    	}
    }
    
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maimMap))
                    .getMap();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(false);
            mMap.getUiSettings().setCompassEnabled(false);

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
                addMapListeners();
            }
        }
    }

    private void setUpMap() {
    	if (mMap==null) return;
    	mMap.clear();
    	mMap.moveCamera(CameraUpdateFactory.newCameraPosition(TECHNION));
    	resetMarkers();
    }
    
    private void hideInfoBox(){
    	FragmentManager fm = getSupportFragmentManager();
        InfoBoxFragment fr = (InfoBoxFragment) fm.findFragmentById(R.id.infoBoxFrag);
        fm.beginTransaction().hide(fr).commit();
    }
    
    private void ShowInfoBox(Long hid){
    	FragmentManager fm = getSupportFragmentManager();
        InfoBoxFragment fr = (InfoBoxFragment) fm.findFragmentById(R.id.infoBoxFrag);
        fr.resetInfoBox(hid);
        fm.beginTransaction().show(fr).commit();
    }
    
	private InfoBoxFragment getInfoBox() {
		FragmentManager fm = getSupportFragmentManager();
        InfoBoxFragment fr = (InfoBoxFragment) fm.findFragmentById(R.id.infoBoxFrag);
        return fr;
	}
	
    private void addMapListeners() {
    	mMap.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public void onMapClick(LatLng arg0) {
				hideInfoBox();
			}
		});
	    mMap.setOnMarkerClickListener(new OnMarkerClickListener() {
	        @Override 
	        public boolean onMarkerClick(final Marker m) {
	        	Long hid = mMarkersHotSpotsTrans.get(m.getId());	        	
	        	ShowInfoBox(hid);
	            return true;
	        }
	      });
	    findViewById(R.id.infoBoxFrag).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//TODO: check if large view - set fragmet nearby
				//FragmentManager fm = getSupportFragmentManager();
				//fm.beginTransaction().add...().comit();
				
				// else start activity
				startActivity(new Intent(mContext, HotSpotInfoActivity.class).
						putExtra("id",getInfoBox().getCurrHotSpotId()));

			}
		});

    }

	@Override
	public void joinBtnClick(Long id) {
		// TODO Auto-generated method stub
	}

	@Override
	public void leaveBtnClick(Long id) {
		// TODO Auto-generated method stub
	}

	@Override
	public void pinBtnClick(Long id) {
		// TODO Auto-generated method stub
	}

	@Override
	public void unpinBtnClick(Long id) {
		// TODO Auto-generated method stub
	}

	@Override
	public void shareBtnClick(Long id) {
		// TODO Auto-generated method stub
	}

	@Override
	public void editBtnClick(Long id) {
		// TODO Auto-generated method stub
	}

	@Override
	public void discardBtnClick(Long id) {
		mHotSpotsMarkersTrans.get(id).remove();
		hideInfoBox();
	}

}
