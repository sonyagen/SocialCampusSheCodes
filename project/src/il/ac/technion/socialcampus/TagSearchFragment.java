package il.ac.technion.socialcampus;

import il.ac.technion.logic.TagManager;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link TagSearchFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link TagSearchFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class TagSearchFragment extends Fragment {

	private OnFragmentInteractionListener mListener;
	private AutoCompleteTextView restName; 

	public static TagSearchFragment newInstance() {
		TagSearchFragment fragment = new TagSearchFragment();
		Bundle args = new Bundle();
		//args.putString(ARG_PARAM1, param1);
		fragment.setArguments(args);
		return fragment;
	}

	public TagSearchFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			//mParam1 = getArguments().getString(ARG_PARAM1);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		
		
		View v = inflater.inflate(R.layout.fragment_tag_search, container, false);
		ImageButton addBtn = (ImageButton)v.findViewById(R.id.addBtn);
		addBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onAddBtnClick();
			}
		});
		restName = (AutoCompleteTextView) v.findViewById(R.id.searchBox);
		restName.setThreshold(0);
		
		//set auto complete adapter
		final ArrayList<String> tagStrings = new ArrayList<String>();
		tagStrings.addAll(TagManager.INSTANCE.getAllTagStrings());
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, tagStrings);
		
		restName.setAdapter(adapter);
		
		return v;
	}

	public void onAddBtnClick() {
		if (mListener != null) {
			String tagName = getTagFromView();
			mListener.addNewTag(tagName, TagManager.INSTANCE.getIdByTagName(tagName));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	private String getTagFromView(){
		return restName.getText().toString();
	}
	
	public interface OnFragmentInteractionListener {
		public void addNewTag(String tagName, Long id);
	}

}
