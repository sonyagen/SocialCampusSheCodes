package il.ac.technion.logic;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;

public enum UserManager{
	INSTANCE;
	public static final String anonymousUID = "";
	private User currentUser = createAnonymous();


	protected HashMap<String,User> mData = new HashMap<String,User>();

	public  boolean isLoggedIn(){
		return !currentUser.getmId().equals(anonymousUID);
	}
	public static boolean isLoggedIn(Context appContext){
		SharedPreferences prefs = appContext.getSharedPreferences(
				"il.ac.technion.socialcampus", Context.MODE_PRIVATE);
		return !prefs.getString("il.ac.technion.socialcampus.LoggedIn",UserManager.anonymousUID)
				.equals(UserManager.anonymousUID);
	}
	public User getItemById(String id){
		return mData.get(id);
	}

	public  Set<User> getAllObjs() {
		return (Set<User>) mData.values();
	}

	public  Set<User> getItemsbyIds(Set<String> Ids) {
		Set<User> $ = new TreeSet<User>();
		for(String id : Ids){
			$.add(getItemById(id));
		}
		return $;
	}

	public static String getLoggedIn(Context appContext){
		SharedPreferences prefs = appContext.getSharedPreferences(
				"il.ac.technion.socialcampus", Context.MODE_PRIVATE);
		return prefs.getString("il.ac.technion.socialcampus.LoggedIn",UserManager.anonymousUID);
	}

	public static void setLoggedIn(Context appContext, String uID){
		SharedPreferences prefs = appContext.getSharedPreferences(
				"il.ac.technion.socialcampus", Context.MODE_PRIVATE);
		prefs.edit().putString("il.ac.technion.socialcampus.LoggedIn", uID).commit();
	}

	User createAnonymous(){
		return new User(anonymousUID,"","Anonymous");
		//TODO: what anonymous user is? default tags?
	}

	UserManager(){
		//TODO update all users from database
	}
	//Log in or register user
	public void setCurrentUser(String id){
		if (!mData.containsKey(id)){
			currentUser = createAnonymous();
		}else{
			currentUser = (User) getItemById(id);
		}
	}

	public String getMyID() {
		return currentUser.getmId();
	}

	public User getMyData() {
		return currentUser;
	}

	public boolean isRegistered(String uid) {
		return mData.containsKey(uid);
	}

	public void syncUsers(final UiOnDone onRes, final UiOnError onErr){

		final List<User> res = new ArrayList<User>();
		SCAsyncRequest r = new SCAsyncRequest(SCPriority.IMMEDIATELY) {

			@Override
			public Void onResult(SCConnectionStatus status) {
				if(status == SCConnectionStatus.RESULT_OK){
					mData.clear();
					for (User h:res){
						mData.put(h.getmId(), h);
					}

					if(onRes != null){
						onRes.execute();
					}	
				}	else {
					if(onErr != null){
						onErr.execute();
					}
				}
				return null;
			}

			@Override
			public Void actionOnServer(Void... params) throws IOException,
			ConnectException {
				res.clear();
				res.addAll(DBManager.INSTANCE.getUsers());

				return null;
			}
		};
		r.run();
		return;
	}

	public void addNewUser(final User user, final UiOnDone uif, 
			final UiOnError uierror){

		new SCAsyncRequest(SCPriority.IMMEDIATELY) {

			@Override
			public Void onResult(SCConnectionStatus status) {
				if(status != SCConnectionStatus.RESULT_OK){
					uierror.execute();
					return null;
				}
				//add the hotSpot with the new id to owned list
				mData.put(user.getmId(), user);
				uif.execute();
				return null;
			}

			@Override
			public Void actionOnServer(Void... params) throws IOException,
			ConnectException {
				user.setmId( DBManager.INSTANCE.addUser(user).getmId());
				return null;
			}
		}.run();
	}

	public void editUser(final User user, final UiOnDone uiOnDone,
			final UiOnError uiOnError) {
		new SCAsyncRequest(SCPriority.IMMEDIATELY) {

			@Override
			public Void onResult(SCConnectionStatus status) {
				if(status != SCConnectionStatus.RESULT_OK){
					uiOnError.execute();
					return null;
				}
				mData.remove(user.getmId());
				mData.put(user.getmId(), user);
				uiOnDone.execute();
				return null;
			}

			@Override
			public Void actionOnServer(Void... params) throws IOException,
			ConnectException {
				DBManager.INSTANCE.updateUser(user);
				return null;
			}
		}.run();
	}

	public void removeUser(final User user, final UiOnDone uiOnDone,
			final UiOnError uiOnError){

		new SCAsyncRequest(SCPriority.IMMEDIATELY) {

			@Override
			public Void onResult(SCConnectionStatus status) {
				if(status != SCConnectionStatus.RESULT_OK){
					uiOnError.execute();
					return null;
				}
				Set<Long> tags = user.getmTags();
				for(Long i: tags){
					((Tag)TagManager.INSTANCE.getItemById(i)).removeUser(user.getmId());
				}
				Set<Long> hots = user.getmHotSpots();
				for(Long i: hots){
					((HotSpot)HotSpotManager.INSTANCE.getItemById(i)).leaveHotSpot(user.getmId());
				}
				mData.remove(user.getmId());
				uiOnDone.execute();
				return null;
			}

			@Override
			public Void actionOnServer(Void... params) throws IOException,
			ConnectException {
				//TODO: this function on the server removes all tags and users
				DBManager.INSTANCE.removeUser(user);
				return null;
			}
		}.run();
	}

	public void logout(Context appContext) {
		currentUser = createAnonymous();
		SharedPreferences prefs = appContext.getSharedPreferences(
				"il.ac.technion.socialcampus", Context.MODE_PRIVATE);
		prefs.edit().remove("il.ac.technion.socialcampus.LoggedIn").commit();

	}


}

