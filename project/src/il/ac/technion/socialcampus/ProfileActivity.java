package il.ac.technion.socialcampus;


import il.ac.technion.logic.Tag;
import il.ac.technion.logic.TagManager;
import il.ac.technion.logic.UiOnDone;
import il.ac.technion.logic.UiOnError;
import il.ac.technion.logic.User;
import il.ac.technion.logic.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
//import com.google.android.gms.tagmanager.TagManager;

public class ProfileActivity extends FragmentActivity  
	implements ActionBar.TabListener,
		OnClickListener,
		ConnectionCallbacks,
		OnConnectionFailedListener, 
		TagsBoxFragment.OnTagClickListener {

	private static final int STATE_DEFAULT = 0;
	private static final int STATE_SIGN_IN = 1;
	private static final int STATE_IN_PROGRESS = 2;
	private  ProgressDialog progressDialog;

	// We use mSignInProgress to track whether user has clicked sign in.
	// mSignInProgress can be one of three values:
	//
	//       STATE_DEFAULT: The default state of the application before the user
	//                      has clicked 'sign in', or after they have clicked
	//                      'sign out'.  In this state we will not attempt to
	//                      resolve sign in errors and so will display our
	//                      Activity in a signed out state.
	//       STATE_SIGN_IN: This state indicates that the user has clicked 'sign
	//                      in', so resolve successive errors preventing sign in
	//                      until the user has successfully authorized an account
	//                      for our app.
	//   STATE_IN_PROGRESS: This state indicates that we have started an intent to
	//                      resolve an error, and so we should not start further
	//                      intents until the current intent completes.
	
	private int mSignInProgress;
	private static final String SAVED_PROGRESS = "sign_in_progress";
	private static final int RC_SIGN_IN = 0;
	// Logcat tag
	private static final String TAG = "MainActivity";

	Context mContext = this;
	// Google client to interact with Google API
	private GoogleApiClient mGoogleApiClient;

	/**
	 * A flag indicating that a PendingIntent is in progress and prevents us
	 * from starting further intents.
	 */
	private boolean mIntentInProgress;
	
	private boolean mSignInClicked;
	private ConnectionResult mConnectionResult;
	private SignInButton btnSignIn;
	private MenuItem btnSignOut, btnRevokeAccess;
	
	private ViewPager pager;
	private PagerAdapter mSectionsPagerAdapter;
	
	// lifecycle 
	//=====================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		progressDialog = new ProgressDialog(this);
		setContentView(R.layout.activity_profile);
		pager = (ViewPager) findViewById(R.id.pager);
		btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
		btnSignIn.setOnClickListener(this);
		
//		all = (LinearLayout) findViewById(R.id.all);
//		imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
//		txtName = (TextView) findViewById(R.id.txtName);
//		tagsBox = (TagsBoxFragment)getSupportFragmentManager().findFragmentById(R.id.tagBox);
//		pinBoard = (LinearLayout) findViewById(R.id.infoBoxesHolderPinned); 
//		joinBoard = (LinearLayout) findViewById(R.id.infoBoxesHolderJoined); 
//		myBoard = (LinearLayout) findViewById(R.id.myboard); 
		
		
		if (savedInstanceState != null) {
			mSignInProgress = savedInstanceState
					.getInt(SAVED_PROGRESS, STATE_DEFAULT);
		}

		mGoogleApiClient = new GoogleApiClient.Builder(this)
		.addConnectionCallbacks(this)
		.addOnConnectionFailedListener(this).addApi(Plus.API)
		.addScope(Plus.SCOPE_PLUS_LOGIN).build();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		btnSignOut = menu.findItem(R.id.signout);
		btnRevokeAccess = menu.findItem( R.id.revoke);
		
		//onPrepare(menu);
		return true;
	}

	protected void onStart() {
		super.onStart();

		if(!UserManager.isLoggedIn(mContext)){
			mGoogleApiClient.connect();
		}else{
			updateUI(true);
		}
	}

	protected void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		//TODO: refresh the fragments
//		if(tagsBox!=null) 
//			tagsBox.buildTags(UserManager.INSTANCE.getMyData().getmTags());
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(SAVED_PROGRESS, mSignInProgress);
	}
	
	// handle connection 
	//=====================================================
	@Override
	protected void onActivityResult(int requestCode, int responseCode,
			Intent intent) {
		if (requestCode == RC_SIGN_IN) {
			if (responseCode != RESULT_OK) {

				// If the error resolution was successful we should continue
				// processing errors.
				mSignInProgress = STATE_SIGN_IN;
				mSignInClicked = false;
			} else {
				// If the error resolution was not successful or the user canceled,
				// we should stop processing errors.
				mSignInProgress = STATE_DEFAULT;
			}

			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		mSignInClicked = false;		
		// Get user's information
		final User u = getProfileInformation();
		//Get the currently logged in user

		if(UserManager.INSTANCE.isRegistered(u.getmId())){
			//Set the current logged user
			//in case of illegal ID the current user will stay the anonymous
			UserManager.INSTANCE.setCurrentUser(u.getmId());
			UserManager.setLoggedIn(mContext, u.getmId());
			if(progressDialog!=null){
				progressDialog.dismiss();
			}
			updateUI(true);
		}else {
			UserManager.INSTANCE.addNewUser(u,  new UiOnDone() {

				@Override
				public void execute() {

					// Update the UI after signin
					UserManager.INSTANCE.setCurrentUser(u.getmId());
					UserManager.setLoggedIn(mContext, u.getmId());
					updateUI(true);

					// Indicate that the sign in process is complete.
					mSignInProgress = STATE_DEFAULT;
					progressDialog.dismiss();
				}
			},new UiOnError(getApplicationContext()){
				@Override
				public void execute() {
					super.execute();
					signOutFromGplus();
					progressDialog.dismiss();
				}
			});
		}
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		mGoogleApiClient.connect();
		updateUI(false);
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!result.hasResolution()) {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
					0).show();
			return;
		}

		if (!mIntentInProgress) {
			// Store the ConnectionResult for later usage
			mConnectionResult = result;

			if (mSignInClicked) {
				// The user has already clicked 'sign-in' so we attempt to
				// resolve all
				// errors until the user is signed in, or they cancel.
				resolveSignInError();
			}
		}

	}

	/**
	 * Method to resolve any signin errors
	 * */
	private void resolveSignInError() {

		if (mConnectionResult!= null && mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				mSignInProgress = STATE_IN_PROGRESS;
				mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
			} catch (SendIntentException e) {
				mIntentInProgress = false;
				mSignInProgress = STATE_SIGN_IN;
				mGoogleApiClient.connect();
			}
		}
	}

	
	/**
	 * Updating the UI, showing/hiding buttons and profile layout
	 * */
	private void updateUI(boolean isSignedIn) {
		if(this.progressDialog!=null){
			progressDialog.dismiss();
		}
		if (isSignedIn) {
			btnSignIn.setVisibility(View.GONE);
			pager.setVisibility(View.VISIBLE);
			if(btnSignOut!=null) btnSignOut.setVisible(true);
			if(btnRevokeAccess!=null) btnRevokeAccess.setVisible(true);
			
			// Set up the action bar.
			final ActionBar actionBar =  getActionBar();
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			
			//create and set adapter
			mSectionsPagerAdapter = new PagerAdapter(
					getSupportFragmentManager(), getFragments());
			pager.setAdapter(mSectionsPagerAdapter);
			
			pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
				@Override
				public void onPageSelected(int position) {
					actionBar.setSelectedNavigationItem(position);
				}
			});
			
//			for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
//		        actionBar.addTab(
//		                actionBar.newTab()
//		                       .setText("Tab " + (i + 1))
//		                       .setTabListener(this));
//		    }
			
			actionBar.addTab(actionBar.newTab()
					.setText("Profile")
					.setTabListener(this));
			actionBar.addTab(actionBar.newTab()
					.setText("Admin")
					.setTabListener(this));
			actionBar.addTab(actionBar.newTab()
					.setText("Joined")
					.setTabListener(this));
			actionBar.addTab(actionBar.newTab()
					.setText("Pinned")
					.setTabListener(this));			

		} else {
			btnSignIn.setVisibility(View.VISIBLE);
			pager.setVisibility(View.GONE);

		}
	}
	
	
	
	private List<Fragment> getFragments() {
		
		PrifileDetailsFragment userDetailsFrag = PrifileDetailsFragment.
				newInstance(UserManager.INSTANCE.getMyID());
		
		Set<Long> adminIds = UserManager.INSTANCE.getMyData().getmAdmin();
		Set<Long> joinIds = UserManager.INSTANCE.getMyData().getmHotSpots();
		Set<Long> pinIds = UserManager.INSTANCE.getMyData().getmPinnedSpots();
		
		long[] arr1 = new long[adminIds.size()];
		int i=0;
		for(long l : adminIds){
			arr1[i++] = l;
		}
		long[] arr2 = new long[joinIds.size()];
		i=0;
		for(long l : joinIds){
			arr2[i++] = l;
		}
		long[] arr3 = new long[pinIds.size()];
		i=0;
		for(long l : pinIds){
			arr3[i++] = l;
		}
		
		EventBoardFragment admin = EventBoardFragment.newInstance(arr1,adminIds.size(),"admin");
		EventBoardFragment joined = EventBoardFragment.newInstance(arr2,joinIds.size(),"joined");
		EventBoardFragment pinned = EventBoardFragment.newInstance(arr3,pinIds.size(),"pinned");
		
		List<Fragment> fl = new ArrayList<Fragment>();
		
		fl.add(userDetailsFrag);
		fl.add(admin);
		fl.add(joined);
		fl.add(pinned);
		
		return fl;
	}
	
//	private void inflateBoard(){
//		LayoutInflater inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
//		
//		Set<Long> joinIds = UserManager.INSTANCE.getMyData().getmHotSpots();
//		Set<Long> pinIds = UserManager.INSTANCE.getMyData().getmPinnedSpots();
//		
//		if(joinIds.size()==0 && pinIds.size()==0){
//			myBoard.setVisibility(View.GONE);
//			return;
//		} else {
//			myBoard.setVisibility(View.VISIBLE);
//		}
	
//		if(joinIds.size()!=0 ){
//			joinBoard.setVisibility(View.VISIBLE);
//			joinBoard.removeAllViews();
//			for(Long id:joinIds){
//				InfoBoxFragment box = InfoBoxFragment.newInstance(id);
//				getSupportFragmentManager().beginTransaction().add(R.id.infoBoxesHolderJoined,box,id.toString()).commit();
//			}
//		}
//		else{
//			joinBoard.setVisibility(View.GONE);
//		}
//		
//		if(pinIds.size()!=0 ){
//			pinBoard.setVisibility(View.VISIBLE);
//			pinBoard.removeAllViews();
//			for(Long id:pinIds){
//				InfoBoxFragment box = InfoBoxFragment.newInstance(id);
//				getSupportFragmentManager().beginTransaction().add(R.id.infoBoxesHolderPinned,box,id.toString()).commit();
//			}
//			//this works too, but with no background
////			View frameLayout = inflater.inflate(R.layout.wide_frag_frame,all);
////			InfoBoxFragment box = InfoBoxFragment.newInstance(id);
////			getSupportFragmentManager().beginTransaction().add(R.id.frameElement,box).commit();
//		}else{
//			pinBoard.setVisibility(View.GONE);
//		}
//	}

	/**
	 * Fetching user's information name, email, profile pic
	 * */
	private User getProfileInformation() {
		User u = null;
		try {
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
				Person currentPerson = Plus.PeopleApi
						.getCurrentPerson(mGoogleApiClient);
				String personName = currentPerson.getDisplayName();
				String personPhotoUrl = currentPerson.getImage().getUrl();


				u = new User();
				u.setmId(currentPerson.getId());
				u.setmName(personName);
				u.setmImage(personPhotoUrl);

			} else {
				Toast.makeText(getApplicationContext(),
						"Person information is null", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return u;
	}


	// showing tags
	//=====================================================
	
	@Override
	public void onTagClick(long tid) {
		Tag t = TagManager.INSTANCE.getItemsbyId(tid);
		//TODO intent to View Tag activity
		Toast.makeText(mContext, t.getmName(), Toast.LENGTH_SHORT).show();
		
	}
	
	

	// google+ account: login/logout/revoke
	//=====================================================
	/**
	 * Button on click listener
	 * */
	@Override
	public void onClick(View v) {
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		switch (v.getId()) {
		case R.id.btn_sign_in:
			// Signin button clicked
			// mGoogleApiClient.connect();
			progressDialog.setMessage(this.getResources().getString(R.string.log_in_msg));
			progressDialog.show();
			signInWithGplus();
			break;
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.signout:
			// Signout button clicked
			progressDialog.setMessage(this.getResources().getString(R.string.log_out_msg));
			progressDialog.show();
			UserManager.INSTANCE.logout(mContext);
			signOutFromGplus();
			progressDialog.dismiss();
			return true;
		case R.id.revoke:
			// Revoke access button clicked
			progressDialog.setMessage(this.getResources().getString(R.string.revoke_access_msg));
			progressDialog.show();
			UserManager.INSTANCE.removeUser( 
					UserManager.INSTANCE.getMyData(),
					new UiOnDone() {
						@Override
						public void execute() {
							revokeGplusAccess();
							UserManager.INSTANCE.logout(mContext);

						}
					}, new UiOnError(getApplicationContext()){
						@Override
						public void execute() {

						}
					});

			return true;
		}
		return false;
	}

	/**
	 * Sign-in into google
	 * */
	private void signInWithGplus() {
		if (!mGoogleApiClient.isConnecting()) {
			mSignInClicked = true;
			mSignInProgress = STATE_IN_PROGRESS;
			resolveSignInError();
		}
	}

	/**
	 * Sign-out from google
	 * */
	private void signOutFromGplus() {
		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			mGoogleApiClient.disconnect();


			//	updateUI(false);
		}
		updateUI(false);
	}

	/**
	 * Revoking access from google
	 * */
	private void revokeGplusAccess() {
		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
			.setResultCallback(new ResultCallback<Status>() {
				@Override
				public void onResult(Status arg0) {
					Log.e(TAG, "User access revoked!");
					mGoogleApiClient.connect();
					updateUI(false);
				}

			});
		}
	}

	@Override
	public boolean isHotSpot() {
		return false;
	}

	@Override
	public String getUserId() {
		return UserManager.INSTANCE.getMyID();
	}

	@Override
	public Long getHotSpotId() {
		return null;
	}


	@Override
	public void onTabReselected(Tab tab, FragmentTransaction arg1) {
		pager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		pager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
	}


}