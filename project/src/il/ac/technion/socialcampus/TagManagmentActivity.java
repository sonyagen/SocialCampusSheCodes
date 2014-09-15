package il.ac.technion.socialcampus;

import il.ac.technion.logic.HotSpotManager;
import il.ac.technion.logic.Tag;
import il.ac.technion.logic.TagManager;
import il.ac.technion.logic.UiOnDone;
import il.ac.technion.logic.UiOnError;
import il.ac.technion.logic.UserManager;

import java.util.ArrayList;
import java.util.Set;

import org.apache.commons.lang3.text.WordUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class TagManagmentActivity extends FragmentActivity 
	implements TagSearchFragment.OnFragmentInteractionListener {

	private TagListAdapter adapter;
	private ArrayList<Tag> mTagList = new ArrayList<Tag>();
	private Context mContext = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag_managment);
		if (savedInstanceState == null)
			setAutoCompleteSerchBox();
		SetTagListView();
	}

	private void setAutoCompleteSerchBox() {
			TagSearchFragment f = (TagSearchFragment)TagSearchFragment.newInstance();
			getSupportFragmentManager().beginTransaction().add(R.id.frameBoxForTagSearch, f).commit();
	}

	private void SetTagListView() {
		String callerType = (String) getIntent().getExtras().get("type");
		Long hotSpotId = callerType.equals("hotspot") ? (Long)getIntent().getExtras().get("id") : -1L;
		
		adapter = new TagListAdapter(this, 
				R.layout.tags_view_row, getTagsListFromCaller(),callerType.equals("hotspot"), hotSpotId);

		ListView lv = (ListView) findViewById(R.id.tagListView );         
		if (lv != null){  lv.setAdapter(adapter) ;}
		
		//set listeners
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adp, View v, int pos, long id) {
				//TODO implement
			}});
	}
	
	private ArrayList<Tag> getTagsListFromCaller(){
		String type = (String) getIntent().getExtras().get("type");
		
		Set<Long> Ids;
		
		if(type.equals("hotspot"))
			Ids = HotSpotManager.INSTANCE.
			getItemById((Long)getIntent().getExtras().get("id")).getmTags();
		else if(type.equals("user"))
			Ids = UserManager.INSTANCE.
			getItemById((String)getIntent().getExtras().get("id")).getmTags();
		else
			return null;
		
		
		mTagList.clear();
		mTagList.addAll(TagManager.INSTANCE.getItemsbyIds(Ids));
		return mTagList;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tag_managment, menu);
		return true;
	}

	@Override
	public void addNewTag(String tagName, final Long tagId) {
		// tag dosent exist in the system
		if (tagId.equals(TagManager.defaultTagId)){
			
			//fixString
			tagName = tagName.trim();
			tagName = WordUtils.capitalize(tagName);
			tagName = tagName.replaceAll("\\s+","");
			
			final String editedTagName = tagName;
			
			//dialog to approve new tagging
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
	
			 builder.setTitle("New Tag")
			 .setMessage("The tag you're looking for dosn't exist. Press ok to create:  #"+ tagName + "\n")
             .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int id) {
                	 TagManager.INSTANCE.addNewTag(new Tag(0L,editedTagName), new UiOnDone() {
						@Override
						public void execute() {
							addTagToObj(TagManager.INSTANCE.getIdByTagName(editedTagName));
						}
					}, new UiOnError(mContext));
                 }
             })
             .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int id) {
                     // User cancelled the dialog
                 }
             });
			 // Create the AlertDialog object and return it
			 AlertDialog d = builder.create();
			 d.show();
		}
		else{
			addTagToObj(tagId);
		}
		
	}
	
	void addTagToObj(final Long tagId){
		//add tag id to user/hotSpot and refresh the view
		String callerType = (String) getIntent().getExtras().get("type");
		Long hotSpotId = callerType.equals("hotspot") ? (Long)getIntent().getExtras().get("id") : -1L;
		
		//if dealing with hotspot tags
		if (callerType.equals("hotspot")){
			if(TagManager.INSTANCE.getItemsbyId(tagId).getmHotSpots().contains(hotSpotId)){
				Toast.makeText(mContext, "Tag already attached.", Toast.LENGTH_SHORT).show();
				return;
			}
			TagManager.INSTANCE.joinSpotTag(tagId, hotSpotId, new UiOnDone() {
				@Override
				public void execute() {
					mTagList.add(TagManager.INSTANCE.getItemsbyId(tagId));
					adapter.notifyDataSetChanged();
				}
			}, new UiOnError(this));
			
		}
		// dealing with user tags
		else{
			if(TagManager.INSTANCE.getItemsbyId(tagId).getmUsers().contains(UserManager.INSTANCE.getMyID())){
				Toast.makeText(mContext, "Tag already attached.", Toast.LENGTH_SHORT).show();
				return;
			}
			TagManager.INSTANCE.joinUserTag(tagId, UserManager.INSTANCE.getMyID(), new UiOnDone() {
				@Override
				public void execute() {
					mTagList.add(TagManager.INSTANCE.getItemsbyId(tagId));
					adapter.notifyDataSetChanged();
				}
			}, new UiOnError(this));
		}
	}

}
