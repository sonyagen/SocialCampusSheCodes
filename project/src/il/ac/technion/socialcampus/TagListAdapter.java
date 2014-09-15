package il.ac.technion.socialcampus;

import il.ac.technion.logic.Tag;
import il.ac.technion.logic.TagManager;
import il.ac.technion.logic.UiOnDone;
import il.ac.technion.logic.UiOnError;
import il.ac.technion.logic.UserManager;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class TagListAdapter extends ArrayAdapter<Tag> {
	
	private ArrayList<Tag> mTags = new ArrayList<Tag>();
	private Context mContext;
	private boolean mIsHotSpot;
	Long mHotSpotId;
	TagListAdapter mThis = this;
	
	
	public TagListAdapter(Context context, int resource, ArrayList<Tag> objects, boolean isHotSpot, Long hotSpotId ) {
		super(context, resource, objects);
		mTags = objects;
		mContext = context;
		mIsHotSpot = isHotSpot;
		mHotSpotId = hotSpotId;
	}
	
	static class ViewHolder {
	    public TextView tag;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null){
			 LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			 row = inflater.inflate(R.layout.tags_view_row, null);
			 
			 ViewHolder viewHolder = new ViewHolder();
			 viewHolder.tag = (TextView) row.findViewById(R.id.tagName);
			 row.setTag(viewHolder);
		}
		
		ViewHolder hv = (ViewHolder) row.getTag();
		
		//TextView tagView = (TextView) row.findViewById(R.id.tagName);
		TextView tagView = hv.tag;
		final Tag t = mTags.get(position);
				
		SpannableString link = SpanLinkFuctory.makeLinkSpan(t.getmName(), t);
		tagView.setText("");
		tagView.append("#");
		tagView.append(link);
		
		ImageButton delBtn = (ImageButton) row.findViewById(R.id.delBtn);
		delBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(mIsHotSpot){
					TagManager.INSTANCE.breakSpotTag(t, mHotSpotId, new UiOnDone() {
						
						@Override
						public void execute() {
							Toast.makeText(mContext, "delete", Toast.LENGTH_SHORT).show();
							mTags.remove(position);
							mThis.notifyDataSetChanged();
						}
					}, new UiOnError(mContext));
				}else{
					TagManager.INSTANCE.breakUserTag(t, UserManager.INSTANCE.getMyID(), new UiOnDone() {
						
						@Override
						public void execute() {
							Toast.makeText(mContext, "delete", Toast.LENGTH_SHORT).show();
							mTags.remove(position);
							mThis.notifyDataSetChanged();
						}
					}, new UiOnError(mContext));
					
				}
				
				
			}
		});
		
		return row;
	}
	
	

}
