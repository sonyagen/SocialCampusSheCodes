package il.ac.technion.socialcampus;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link EventBoardFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link EventBoardFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class EventBoardFragment extends Fragment {
	
	private ArrayList<Long> mHotSpotIds = new ArrayList<Long>();
	LinearLayout mBoard;
	
	public static EventBoardFragment newInstance(long[] idArray, int size, String alias) {
		EventBoardFragment fragment = new EventBoardFragment();
		Bundle args = new Bundle();
		args.putLongArray("list", idArray) ;
		args.putInt("size", size) ;
		args.putString("alias", alias) ;
		fragment.setArguments(args);
		return fragment;
	}

	public EventBoardFragment() {
		// Required empty public constructor
	}

	public String alias;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			
			long[] arr = getArguments().getLongArray("list");
			int size = getArguments().getInt("size");
			alias = getArguments().getString("alias");
			for(int i=0;i<size;i++){
				mHotSpotIds.add( arr[i] );
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		 View v = inflater.inflate(R.layout.fragment_event_board, container, false);
		 mBoard = (LinearLayout)v.findViewById(R.id.infoBoxesHolder); 
		// mBoard.setVisibility(View.VISIBLE);
		 if(mHotSpotIds.size()!=0 ){
			mBoard.removeAllViews();
			for(Long id : mHotSpotIds){
				InfoBoxFragment box = InfoBoxFragment.newInstance(id);
				getActivity().getSupportFragmentManager().beginTransaction()
				.add(R.id.infoBoxesHolder,box,id.toString()).commit();
			}
		}
		else{
			//mBoard.setVisibility(View.GONE);
		}
		 
		 
		 return v;
	}

	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		
		for(Long id : mHotSpotIds){
			Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag(id.toString());
			getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();
		}
	}	
}
