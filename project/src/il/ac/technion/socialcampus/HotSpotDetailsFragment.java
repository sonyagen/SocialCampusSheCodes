package il.ac.technion.socialcampus;

import il.ac.technion.logic.HotSpot;
import il.ac.technion.logic.HotSpotManager;
import il.ac.technion.logic.Tag;
import il.ac.technion.logic.TagManager;
import il.ac.technion.logic.User;
import il.ac.technion.logic.UserManager;

import java.util.Set;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class HotSpotDetailsFragment extends InfoBoxFragment {

	GoogleMap mMap;
	TagsBoxFragment tagsBox;
	
	public HotSpotDetailsFragment() {
		// Required empty public constructor
	}
	
	public static HotSpotDetailsFragment newInstance(Long mHotSpotId) {
		HotSpotDetailsFragment fragment = new HotSpotDetailsFragment();
		Bundle args = new Bundle();
		args.putLong(HotSpotId, mHotSpotId);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_hot_spot_details, container,
				false);
		mView = v;
		share = ((ImageButton)mView.findViewById(R.id.shareImgBtn));
		joinLeave = ((ImageButton)mView.findViewById(R.id.joinImgBtn));
		pinUnpin = ((ImageButton)mView.findViewById(R.id.pinImgBtn));
		headline = ((TextView) mView.findViewById(R.id.name));
		timeStr = ((TextView)mView.findViewById(R.id.timeStr));
		desc = ((TextView)mView.findViewById(R.id.description));
		image = ((ImageView)mView.findViewById(R.id.image));
		attending1 = (ImageView)mView.findViewById(R.id.usr1);
		attending2 = (ImageView)mView.findViewById(R.id.usr2);
		attending3 = (ImageView)mView.findViewById(R.id.usr3);
		
		tagsBox = (TagsBoxFragment)getActivity().getSupportFragmentManager()
				.findFragmentById(R.id.hsTagBox);
		tagsBox.buildTags(HotSpotManager.INSTANCE.getItemById(mHotSpotDataId).getmTags());
			
		setView();
		setMap();
		return v;	
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(tagsBox!=null) 
			tagsBox.buildTags(HotSpotManager.INSTANCE.getItemById(mHotSpotDataId).getmTags());
	}

	
	private void setGoing() {
		Set<User> u = UserManager.INSTANCE.getItemsbyIds(
				HotSpotManager.INSTANCE.getItemById(mHotSpotDataId).getmUseres());
		//TODO set the view
		//min(5,u.size)
		//for: make view
	}

	private void setMap() {
		if(mHotSpotDataId==null) return;
		
		HotSpot hs = HotSpotManager.INSTANCE.getItemById(mHotSpotDataId);
		LatLng ll = new LatLng( hs.getLangt(),hs.getLongt());
		SupportMapFragment f = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.My_map));
		mMap = f.getMap();

		CameraPosition CurrPos = new CameraPosition.Builder().target(ll).zoom(17f).bearing(300).tilt(50).build();
        if (mMap != null) {
        	mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CurrPos));
        	mMap.addMarker(new MarkerOptions().position(ll));
        }
	}

	public void refresh(Long id){
		if (id==null)return;
		resetInfoBox(id);
		setMap();
		
	}

//	@Override
//	public void onTagClick(long tid) {
//		Tag t = TagManager.INSTANCE.getItemsbyId(tid);
//		// TODO Auto-generated method stub
//		Toast.makeText(mContext, t.getmName(), Toast.LENGTH_SHORT).show();
//		
//	}
}
