package il.ac.technion.socialcampus;

import il.ac.technion.logic.HotSpot;
import il.ac.technion.logic.HotSpotManager;
import il.ac.technion.logic.LocationFactury;
import il.ac.technion.logic.TimeDateStringFactory;
import il.ac.technion.logic.UiOnDone;
import il.ac.technion.logic.UiOnError;
import il.ac.technion.logic.UserManager;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Set;
import java.util.TreeSet;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link CreateHotSpotFragment.OnFragmentInteractionListener} interface to
 * handle interaction events. Use the {@link CreateHotSpotFragment#newInstance}
 * factory method to create an instance of this fragment.
 * 
 */
public class CreateHotSpotFragment extends Fragment {
	
	final private String START_TIME_PICKER_HEDLINE = "Choose Start Time";
	final private String END_TIME_PICKER_HEDLINE = "Choose End Time";
	final private String START_DATE_PICKER_HEDLINE = "Choose Start Date";
	final private String END_DATE_PICKER_HEDLINE = "Choose Date Time";
	
	private Long mCurrHotSpotId;
	private HotSpot mCurrHotSpotData;
	private GoogleMap mMap;
	private ImageButton ok;
	private ImageButton cancel;
	private EditText headline;
	private ImageView imageView;
	private EditText timeInput;
	private EditText dateInput;
	private EditText endTimeInput;
	private EditText endDateInput;
	private EditText place;
	private EditText description;
	private ImageView userImage1;
	private ImageView userImage2;
	private ImageView userImage3;
	private boolean isEdit;
	public static Long defHotSpotID = -1L;
	private Bitmap bitmap;
	private String imgUriStr;
	private CameraPosition chosenPos;
	
  	public static CreateHotSpotFragment newInstance(Long hsid) {
  		if(hsid==null){
  			hsid=defHotSpotID;
  		}
		CreateHotSpotFragment fragment = new CreateHotSpotFragment();
		Bundle args = new Bundle();
		args.putLong("id", hsid);
		fragment.setArguments(args);
		return fragment;
	}

	public CreateHotSpotFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			
			mCurrHotSpotId = getArguments().getLong("id");
			mCurrHotSpotData = HotSpotManager.INSTANCE.getItemById(mCurrHotSpotId);
			if(mCurrHotSpotData == null || mCurrHotSpotId == defHotSpotID ) {
				isEdit = false;
			}
			else{ 
				isEdit = true;
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_create_hot_spot, container, false);
		
		//btn
		ok = (ImageButton)v.findViewById(R.id.okBtn);
		cancel = (ImageButton)v.findViewById(R.id.cancelBtn);
		
		//text views
		headline = (EditText)v.findViewById(R.id.name);
		imageView = (ImageView)v.findViewById(R.id.image);
		timeInput = (EditText)v.findViewById(R.id.timeStr);
		dateInput = (EditText)v.findViewById(R.id.dateeStr);
		endTimeInput = (EditText)v.findViewById(R.id.endtimeStr);
		endDateInput = (EditText)v.findViewById(R.id.enddateeStr);
		place = (EditText)v.findViewById(R.id.place);
		description = (EditText)v.findViewById(R.id.desc);
		
		//going user pics
		userImage1 = (ImageView)v.findViewById(R.id.usr1);
		userImage2 = (ImageView)v.findViewById(R.id.usr2);
		userImage3 = (ImageView)v.findViewById(R.id.usr3);
		
		//init view
		initTextFilds();
		initImage();
		initMap();
		initGoing();
		setListeners();
		return v;
	}
	
	//init fielsds foe new or edit
	private void initTextFilds(){
		if(isEdit){
			headline.setText(mCurrHotSpotData.getmName());
			timeInput.setText(TimeDateStringFactory.getTimeStr(mCurrHotSpotData.getmTime()));
			dateInput.setText(TimeDateStringFactory.getDateStr(mCurrHotSpotData.getmTime()));
			endTimeInput.setText(TimeDateStringFactory.getTimeStr(mCurrHotSpotData.getEndTime()));
			endDateInput.setText(TimeDateStringFactory.getDateStr(mCurrHotSpotData.getEndTime()));
			description.setText(mCurrHotSpotData.getmDesc());
		}
		else{
			Calendar now = Calendar.getInstance();
			timeInput.setText(TimeDateStringFactory.getTimeStr(now));
			dateInput.setText(TimeDateStringFactory.getDateStr(now));
			endTimeInput.setText(TimeDateStringFactory.getTimeStr(now));
			endDateInput.setText(TimeDateStringFactory.getDateStr(now));
		}
	}
	
	private void initImage(){
		imgUriStr = null;
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				gotoGalery();
			}
		});
		
		if(isEdit){
			imgUriStr = HotSpotManager.INSTANCE.getItemById(mCurrHotSpotId).getImageURL();
			bitmap = HotSpotManager.INSTANCE.getItemById(mCurrHotSpotId).getImage();
			if (bitmap!=null) imageView.setImageBitmap(bitmap);
		}
	}

	private void initMap() {
		SupportMapFragment f = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.My_map));
		mMap = f.getMap();
		
		if(!isEdit){
			chosenPos = LocationFactury.TECHNION;
			if (mMap != null) 
	        	mMap.moveCamera(CameraUpdateFactory.newCameraPosition(chosenPos));
		}else{
			LatLng ll = new LatLng( mCurrHotSpotData.getLangt(),mCurrHotSpotData.getLongt());
			
			chosenPos = new CameraPosition.Builder().target(ll).zoom(17f).bearing(300).tilt(50).build();
	        if (mMap != null) {
	        	mMap.moveCamera(CameraUpdateFactory.newCameraPosition(chosenPos));
	        	mMap.addMarker(new MarkerOptions().position(ll));
	        }
			
		}
	}
	
	private void initGoing(){
		if(!isEdit){
			//set imge me: userImage1.set...
			UserManager.INSTANCE.getMyData().setUserPhoto(userImage1);
			userImage2.setVisibility(View.GONE);
			userImage3.setVisibility(View.GONE);
		}
		else{
			//set 3 imges of users
		}
		
	}
	
	private void setListeners(){
		timeInput.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {	
				Calendar now = Calendar.getInstance();
				TimePickerDialog tm = new TimePickerDialog(
						(Context)getActivity(), new innerFirstTime(), 
						now.get(Calendar.HOUR_OF_DAY),
						now.get(Calendar.MINUTE), true);
				tm.setTitle(START_TIME_PICKER_HEDLINE);
				tm.show();
			}
		});
		dateInput.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {	
				Calendar now = Calendar.getInstance();
				DatePickerDialog tm = new DatePickerDialog(
						(Context)getActivity(),  new innerFirstDate(), 
						now.get(Calendar.YEAR),
						now.get(Calendar.MONTH), 
						now.get(Calendar.DAY_OF_MONTH));
				tm.setTitle(START_DATE_PICKER_HEDLINE);
				tm.show();
			}
		});
		endTimeInput.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {	
				Calendar now = Calendar.getInstance();
				TimePickerDialog tm = new TimePickerDialog(
						(Context)getActivity(), new secondInnerTime(), 
						now.get(Calendar.HOUR_OF_DAY),
						now.get(Calendar.MINUTE), true);
				tm.setTitle(START_TIME_PICKER_HEDLINE);
				tm.show();
			}
		});
		endDateInput.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {	
				Calendar now = Calendar.getInstance();
				DatePickerDialog tm = new DatePickerDialog(
						(Context)getActivity(),  new secondInnerDate(), 
						now.get(Calendar.YEAR),
						now.get(Calendar.MONTH), 
						now.get(Calendar.DAY_OF_MONTH));
				tm.setTitle(START_DATE_PICKER_HEDLINE);
				tm.show();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().finish();
				
			}
		});
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
								
				HotSpot fromView = createHotSpotFromView();
				
				if (isEdit){
					HotSpotManager.INSTANCE.editHotSpot(fromView, new UiOnDone() {
						@Override
						public void execute() {
							getActivity().finish();
						}
					}, new UiOnError(getActivity()));
				}else{
					//create new: 
					HotSpotManager.INSTANCE.addNewHotSpot(fromView, new UiOnDone() {
						@Override
						public void execute() {
							getActivity().finish();
						}
					}, new UiOnError(getActivity()));
				}
				
			}
		});
		mMap.setOnMapClickListener(new OnMapClickListener() {
			
			@Override
			public void onMapClick(LatLng arg0) {
				double lat = chosenPos.target.latitude;
				double lng = chosenPos.target.longitude;
				startActivityForResult(new Intent(getActivity(),MapPickerActivity.class).
						putExtra("lat", lat).putExtra("lng", lng),requestCodeMap);
			}
		});
	}

	int requestCodeMap = 100;
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		getActivity();
		if (requestCode == requestCodeMap && 
				resultCode == Activity.RESULT_OK) {
			 
			 //resetCamera on mMap 
			 Double lat = data.getExtras().getDouble("lat");
			 Double lng = data.getExtras().getDouble("lng");
			 chosenPos = LocationFactury.buildCameraPosition(lat,lng);
					
			 mMap.clear();
			 mMap.moveCamera(CameraUpdateFactory.newCameraPosition(chosenPos));
			 mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)));
		 }
		if(requestCode == SELECT_PHOTO && 
				resultCode == Activity.RESULT_OK){
			//back from galary
			Uri selectedImage = data.getData();
			imgUriStr = selectedImage.toString();
			try {
				bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
            imageView.setImageBitmap(bitmap);
		}
		 
	 }

	//generate obj from view
	private HotSpot createHotSpotFromView(){
		
		Long mId = isEdit ? mCurrHotSpotData.getmId(): 0L;
		Long mTime = startTime.getTimeInMillis();
		Long mEndTime = endTime.getTimeInMillis();
		String mLocation = place.getText().toString();
		String mName = headline.getText().toString();
		double lat = chosenPos.target.latitude;
		double lon = chosenPos.target.longitude;
		String mdescription = description.getText().toString();
		String mAdminId = UserManager.INSTANCE.getMyID();
		//TODO: if edit, was the picture changed? no get from mCurrHotSpotData
		String mImageURL = imgUriStr.toString();
		Set<Long> mTags = new TreeSet<Long>();
		if(isEdit) mTags.addAll(mCurrHotSpotData.getmTags());
		Set<String> mUseres = new TreeSet<String>();
		if(isEdit) mUseres.addAll(mCurrHotSpotData.getmUseres());
		else mUseres.add(UserManager.INSTANCE.getMyID());
		
		return new HotSpot(mId, mTime, mEndTime, mName, lat, lon, 
				mLocation, mdescription, mAdminId, mImageURL, bitmap, mTags, mUseres);
	}
	
	//open galary
	int SELECT_PHOTO = 200;
	private void gotoGalery(){
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, SELECT_PHOTO);    
	}

	// time date objects
	Calendar startTime = Calendar.getInstance();
	Calendar endTime = Calendar.getInstance();
	
	class innerFirstDate implements DatePickerDialog.OnDateSetListener{
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        	startTime.set(Calendar.YEAR, year);
    		startTime.set(Calendar.MONTH, monthOfYear);
    		startTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        }
	}
	class secondInnerDate implements DatePickerDialog.OnDateSetListener{
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        	endTime.set(Calendar.YEAR, year);
        	endTime.set(Calendar.MONTH, monthOfYear);
        	endTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        }
	}
	class innerFirstTime implements OnTimeSetListener{
		@Override
		public void onTimeSet(TimePicker arg0, int hour, int minets) {
			startTime.set(Calendar.HOUR_OF_DAY, hour);
			startTime.set(Calendar.MINUTE, minets);
		}
        
	}
	class secondInnerTime implements OnTimeSetListener{
			@Override
			public void onTimeSet(TimePicker arg0, int hour, int minets) {
				endTime.set(Calendar.HOUR_OF_DAY, hour);
				endTime.set(Calendar.MINUTE, minets);
			}
		}
}
