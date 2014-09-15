package il.ac.technion.socialcampus;

import java.util.Set;
import java.util.TreeSet;

import il.ac.technion.logic.Tag;
import il.ac.technion.logic.TagManager;
import il.ac.technion.logic.UserManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link TagsBoxFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link TagsBoxFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class TagsBoxFragment extends Fragment implements Tag.onTagClickListener{
	private Context mContext = getActivity();
	private OnTagClickListener mListener;
	private TextView tagsView;
	private ImageButton editBtn;
	
	public TagsBoxFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_tags_box, container, false);
		tagsView = (TextView)v.findViewById(R.id.tags);
		editBtn = (ImageButton)v.findViewById(R.id.editBtn);
		setListeners();
		return v;
	}
	
	private void setListeners(){
		editBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onEditBtnClick();
			}
		});
	}
	
	private void onEditBtnClick(){
		Intent intent = new Intent(getActivity(),TagManagmentActivity.class).
				putExtra("type", isHotSpot() ? "hotspot" : "user").
				putExtra("id", isHotSpot() ?  getHotSpotId(): getUserId());
		 
		startActivity(intent);
	}
	
	boolean isHotSpot(){
		return mListener.isHotSpot();
	}
	private Long getHotSpotId(){return mListener.getHotSpotId();}
	private String getUserId(){return mListener.getUserId();}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnTagClickListener) activity;
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

	public interface OnTagClickListener {
		public void onTagClick(long tid);

		public String getUserId();

		public Long getHotSpotId();

		public boolean isHotSpot();
	}
	
	/*
	 * implementation of Tag.onTagClickListener:
	 * Tag sets the id and the onTagClick passes it on 
	 * to the listener for the activity to handle
	 */
	@Override
	public void onTagClick(Long tid) {
		mListener.onTagClick(tid);
	}
	
	private SpannableString makeLinkSpan(CharSequence text, View.OnClickListener listener) {
	    SpannableString link = new SpannableString(text);
	    link.setSpan(new ClickableString(listener), 0, text.length(), 
	        SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
	    return link;
	}
	
	private void makeLinksFocusable(TextView tv) {
	    MovementMethod m = tv.getMovementMethod();  
	    if ((m == null) || !(m instanceof LinkMovementMethod)) {  
	        if (tv.getLinksClickable()) {  
	            tv.setMovementMethod(LinkMovementMethod.getInstance());  
	        }  
	    }  
	}
	
	public static class ClickableString extends ClickableSpan {  
	    private View.OnClickListener mListener;          
	    public ClickableString(View.OnClickListener listener) {              
	        mListener = listener;  
	    }          
	    @Override  
	    public void onClick(View v) {  
	        mListener.onClick(v);  
	    }        
	}


	public void buildTags(Set<Long> tagIds){
		
		tagsView.setText("");
		
		Set<Tag> Tags = TagManager.INSTANCE.getItemsbyIds(tagIds);
		for(Tag t:Tags){
			//override of onTagClick
			t.setListener(this);
			SpannableString link = makeLinkSpan(t.getmName(), t);
			tagsView.append("#");
			tagsView.append(link);
			tagsView.append("	");
		}
		
		makeLinksFocusable(tagsView);
		
	}
}
