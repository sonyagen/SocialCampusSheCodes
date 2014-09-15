package il.ac.technion.socialcampus;

import il.ac.technion.logic.User;
import il.ac.technion.logic.UserManager;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link PrifileDetailsFragment.OnFragmentInteractionListener} interface to
 * handle interaction events. Use the {@link PrifileDetailsFragment#newInstance}
 * factory method to create an instance of this fragment.
 * 
 */
public class PrifileDetailsFragment extends Fragment {
	private String mCurrUserId;
	private ImageView imgProfilePic;
	private TextView txtName;
	private LinearLayout all;
	private TagsBoxFragment tagsBox;

	//private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment PrifileDetailsFragment.
	 */
	public static PrifileDetailsFragment newInstance(String uid) {
		PrifileDetailsFragment fragment = new PrifileDetailsFragment();
		Bundle args = new Bundle();
		args.putString("uid", uid);
	
		fragment.setArguments(args);
		return fragment;
	}

	public PrifileDetailsFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mCurrUserId = getArguments().getString("uid");			
		}
	}

	public String alias = "Profile";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_prifile_details, container,	false);
		
		all = (LinearLayout) v.findViewById(R.id.all);
		imgProfilePic = (ImageView) v.findViewById(R.id.imgProfilePic);
		txtName = (TextView) v.findViewById(R.id.txtName);
		tagsBox = (TagsBoxFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.tagBox);
		
		User currentU = UserManager.INSTANCE.getItemById(mCurrUserId);
		if (currentU==null) return v;
		
		txtName.setText(currentU.getmName());
		currentU.setUserPhoto(imgProfilePic);
		tagsBox.buildTags(UserManager.INSTANCE.getMyData().getmTags());
				
		return v;
	}

//	@Override
//	public void onAttach(Activity activity) {
//		super.onAttach(activity);
//		try {
//			mListener = (OnFragmentInteractionListener) activity;
//		} catch (ClassCastException e) {
//			throw new ClassCastException(activity.toString()
//					+ " must implement OnFragmentInteractionListener");
//		}
//	}
//
//	@Override
//	public void onDetach() {
//		super.onDetach();
//		mListener = null;
//	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
//	public interface OnFragmentInteractionListener {
//		// TODO: Update argument type and name
//		public void onFragmentInteraction(Uri uri);
//	}

}
