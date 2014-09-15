package il.ac.technion.logic;


import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;



public class HotSpot extends TagableObject{
	private Long mId;
	private Long mTime;
	private Long mEndTime;
	private String mName;
	private double mLong;
	private double mLat;

	private String mLocation;
	private String mdescription;
	private String mAdminId;
	private String mImageURL;
	private Bitmap mIconBitmap = null;
	private Set<Long> mTags = new TreeSet<Long>();//tag ids 
	private Set<String> mUsers = new TreeSet<String>();	

	

	
	public HotSpot(HotSpot hs) {
		  mId = hs.mId;
		  mTime = hs.mTime;
		  mEndTime = hs.mEndTime;
		  mName = hs.mName;
		  mLong = hs.mLong;
		  mLat = hs.mLat;

		  mLocation = hs.mLocation;
		  mdescription = hs.mdescription;
		  mAdminId = hs.mAdminId;
		  mImageURL = hs.mImageURL;
		  mIconBitmap = hs.mIconBitmap;
		  mTags.addAll(hs.mTags);
		  mUsers.addAll(hs.mUsers);
	
	}	

	public HotSpot(Long mId, Long mTime, Long mEndTime, String mName, Double lat, Double lon,
			String mLocation, String mdescription, String mAdminId, String mImageURL) {
		  this.mId = mId;
		  this.mTime = mTime;
		  this.mEndTime = mEndTime;
		  this.mName = mName;
		  this.mLong = lon;
		  this.mLat = lat;

		  this.mLocation = mLocation;
		  this.mdescription = mdescription;
		  this.mAdminId = mAdminId;
		  this.mImageURL = mImageURL;
		  
		  new LoadHotSpotImage(mIconBitmap).execute(this.mImageURL);
	

	}
		
	public HotSpot(Long mId, Long mTime,Long mEndTime,String mName,Double lat, Double lon ,
			String mLocation,String mdescription ,String mAdminId,String mImageURL, Bitmap bitmap,
			Set<Long> mTags, Set<String> mUseres) {
		  this.mId = mId;
		  this.mTime = mTime;
		  this.mEndTime = mEndTime;
		  this.mName = mName;
		  this.mLong = lon;
		  this.mLat = lat;

		  this.mLocation = mLocation;
		  this.mdescription = mdescription;
		  this.mAdminId = mAdminId;
		  this.mImageURL = mImageURL;
		  this.mIconBitmap = bitmap;
		  this.mTags.addAll(mTags);
		  this.mUsers.addAll(mUseres);
	}

	public HotSpot() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Set<String> getmUsers() {
		return mUsers;
	}

	public void setmUsers(Set<String> mUsers) {
		this.mUsers = mUsers;
	}

	public double getLongt() {
		return mLong;
	}

	public void setLongt(double longt) {
		this.mLong = longt;
	}

	public double getLangt() {
		return mLat;
	}

	public void setLangt(double langt) {
		this.mLat = langt;
	}

	public String getAdminId() {
		return mAdminId;
	}

	public void setAdmind(String mId) {
		this.mAdminId = mId;
	}
	
	public Long getmId() {
		return mId;
	}

	public void setmId(Long mId) {
		this.mId = mId;
	}

	public Long getmTime() {
		return mTime;
	}

	public void setmTime(Long mTime) {
		this.mTime = mTime;
	}
	
	public Long getEndTime() {
		return mEndTime;
	}

	public void setEndTime(Long mTime) {
		this.mEndTime = mTime;
	}

	public String getmLocation() {
		return mLocation;
	}

	
	public String getImageURL() {
		return mImageURL;
	}
	public void setImageURL(String mImageURL) {
		this.mImageURL = mImageURL;
	}
	
	public Bitmap getImage(){
		return mIconBitmap;
	}
	public void setmLocation(String mLocation) {
		this.mLocation = mLocation;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}
	
	public String getmDesc() {
		return mdescription;
	}

	public void setmDesc(String mdescription) {
		this.mdescription = mdescription;
	}


	public Set<Long> getmTags() {
		return mTags;
	}

	public void setmTags(Set<Long> mTags) {
		this.mTags = mTags;
	}

	public Set<String> getmUseres() {
		return mUsers;
	}

	public void setmUseres(Set<String> mUseres) {
		this.mUsers = mUseres;
	}

	


	public String getTimeStr(){
		//return (DateFormat.getTimeInstance()).format(new Date(m_orderingTime));
        return (new SimpleDateFormat("HH:mm").format(new Date(mTime)));
	}
	

	
	public Boolean IsUserJoined(Long id){
		return mUsers.contains(id);
	}


	public void leaveHotSpot(String userId){
		if(mUsers.contains(userId)){
			mUsers.remove(userId);
		}
	}
	public void joinHotSpot(String userId){
		mUsers.add(userId);
	}
	
	public void removeTag(long tId){
		if(mTags.contains(tId)){
			mTags.remove(tId);
		}
	}
	public void addTag(long tId){
		mTags.add(tId);
	}

	public static class LoadHotSpotImage extends AsyncTask<String, Void, Bitmap> {

		Bitmap iconBitmap;
		
		public LoadHotSpotImage(Bitmap icon){
			iconBitmap = icon;
		}
		
		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			iconBitmap = result;
		}
	}
}
