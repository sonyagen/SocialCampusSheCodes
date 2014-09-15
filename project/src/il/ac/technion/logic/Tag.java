package il.ac.technion.logic;
import java.util.Calendar;
import java.util.Set;
import java.util.TreeSet;

import android.location.Location;
import android.view.View;
import android.view.View.OnClickListener;


public class Tag implements OnClickListener,Comparable<Tag>{
	private Long mId;
	private String mName;
	private Long mTime;
	private Set<String> mUsers = new TreeSet<String>(); 
	private Set<Long> mHotSpots = new TreeSet<Long>();


	public Tag(Tag hs) {
		this.mId = hs.mId;
		this.mName = hs.mName;
		this.mTime = hs.mTime;
		this.mUsers.addAll(hs.mUsers);
		this.mHotSpots.addAll( hs.mHotSpots);
	}	

	public Tag(Long mId, String mName,
			Set<String> mUsers, Set<Long> mSpots) {
		super();
		this.mTime = Calendar.getInstance().getTimeInMillis();
		this.mId = mId;
		this.mName = mName;
		this.mUsers.addAll(mUsers);
		this.mHotSpots.addAll(mSpots);
	}
	
	public Tag(Long mId, String mName,Long mTime) {
		super();
		this.mTime = mTime;
		this.mId = mId;
		this.mName = mName;
	}
	
	public Tag(Long mId, String mName) {
		super();
		this.mId = mId;
		this.mName = mName;
		this.mTime = Calendar.getInstance().getTimeInMillis();
	}

	public Tag() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
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


	public Set<String> getmUsers() {
		return mUsers;
	}

	public void setmUsers(Set<String> mUsers) {
		this.mUsers = mUsers;
	}

	public Set<Long> getmHotSpots() {
		return mHotSpots;
	}

	public void setmHotSpots(Set<Long> mHotSpots) {
		this.mHotSpots = mHotSpots;
	}

	public Boolean IsUserJoined(Long id){
		return mHotSpots.contains(id);
	}


	public void leaveHotSpot(long hsId){
		if(mHotSpots.contains(hsId)){
			mHotSpots.remove(hsId);
		}
	}
	public void joinHotSpot(long userId){
		mHotSpots.add(userId);
	}
	
	public void removeUser(String tId){
		if(mUsers.contains(tId)){
			mUsers.remove(tId);
		}
	}
	public void addUser(String tId){
		mUsers.add(tId);
	}

	@Override
	public void onClick(View v) {
		if (mListener!=null)
			mListener.onTagClick(mId);
	}
	
	public void setListener(onTagClickListener listener){
		mListener = listener;
	}
	
	public onTagClickListener mListener;
	
	/*
	 * to be implemented in a ui object
	 * the cottect use it to implement the interface
	 * and to set the listener using: setListener(this);
	 * (equivalent to fragment's onAttach()) 
	 */
	public interface onTagClickListener{
		void onTagClick(Long tid);
	}

	@Override
	public int compareTo(Tag another) {
		if(mId.equals(another.getmId()))
			return 0;
		return 1;
	}

}
