package il.ac.technion.logic;


import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public enum TagManager {
	INSTANCE;
	static public Long defaultTagId = -1L;
	protected HashMap<Long,Tag> mData = new HashMap<Long,Tag>();
	protected HashMap<String,Long> mId = new HashMap<String,Long>();
	
	Tag getItemById(Long id){
		return mData.get(id);
		
	}
	
	public Collection<Tag> getAllObjs() {
		return  mData.values();
	}
	
	public Collection<String> getAllTagStrings() {
		Collection<String> c = new ArrayList<String>();
		for(Tag t : mData.values()){
			c.add(t.getmName());
		}
		return c;
	}
	
	public  Set<Tag> getItemsbyIds(Set<Long> Ids) {
		Set<Tag> $ = new TreeSet<Tag>();
		for(Long id : Ids){
			$.add(getItemById(id));
		}
		return $;
	}
	
	public  Tag getItemsbyId(long Id) {
		return mData.get(Id);
	}
	
	public  Long getIdByTagName(String tagName) {
		Long id = mId.get(tagName);
		if (id==null)
			return defaultTagId;
		return mId.get(tagName);
	}

	
	
	public void syncTags(final UiOnDone onRes, final UiOnError onErr){
		
		final List<Tag> res = new ArrayList<Tag>();
		SCAsyncRequest r = new SCAsyncRequest(SCPriority.IMMEDIATELY) {

			@Override
			public Void onResult(SCConnectionStatus status) {
				if(status == SCConnectionStatus.RESULT_OK){
					mData.clear();
					mId.clear();
					for (Tag h:res){
						mData.put(h.getmId(), h);
						mId.put(h.getmName(), h.getmId());
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
				res.addAll(DBManager.INSTANCE.getTags());
						
				return null;
			}
		};
		r.run();
		return;
	}

	public void addNewTag(final Tag tag, final UiOnDone uif, 
			final UiOnError uierror){
		
		new SCAsyncRequest(SCPriority.IMMEDIATELY) {
			
			@Override
			public Void onResult(SCConnectionStatus status) {
				if(status != SCConnectionStatus.RESULT_OK){
					uierror.execute();
					return null;
				}
				//add the hotSpot with the new id to owned list
				mData.put(tag.getmId(), tag);
				mId.put(tag.getmName(), tag.getmId());
				uif.execute();
				return null;
			}
			
			@Override
			public Void actionOnServer(Void... params) throws IOException,
			ConnectException {
				tag.setmId( DBManager.INSTANCE.addTag(tag).getmId());
				return null;
			}
		}.run();
	}
	
	public void editTag(final Tag tag, final UiOnDone uiOnDone,
		final UiOnError uiOnError) {
		new SCAsyncRequest(SCPriority.IMMEDIATELY) {
			
			@Override
			public Void onResult(SCConnectionStatus status) {
				if(status != SCConnectionStatus.RESULT_OK){
					uiOnError.execute();
					return null;
				}
				mData.remove(tag.getmId());
				mId.remove(tag.getmName());
				mData.put(tag.getmId(), tag);
				mId.put(tag.getmName(), tag.getmId());
				
				uiOnDone.execute();
				return null;
			}
			
			@Override
			public Void actionOnServer(Void... params) throws IOException,
			ConnectException {
				DBManager.INSTANCE.updateTag(tag);
				return null;
			}
		}.run();
	}

	public void removeTag(final Tag tag, final UiOnDone uiOnDone,
			final UiOnError uiOnError){
		
		new SCAsyncRequest(SCPriority.IMMEDIATELY) {
			
			@Override
			public Void onResult(SCConnectionStatus status) {
				if(status != SCConnectionStatus.RESULT_OK){
					uiOnError.execute();
					return null;
				}
				Set<String> users = tag.getmUsers();
				for(String i: users){
					((User)UserManager.INSTANCE.getItemById(i)).removeTag(tag.getmId());
				}
				Set<Long> hots = tag.getmHotSpots();
				for(Long i: hots){
					((HotSpot)HotSpotManager.INSTANCE.getItemById(i)).removeTag(tag.getmId());
				}
				mData.remove(tag);
				mId.remove(tag.getmName());
				uiOnDone.execute();
				return null;
			}
			
			@Override
			public Void actionOnServer(Void... params) throws IOException,
			ConnectException {
				//TODO: this function on the server removes all tags and users
				DBManager.INSTANCE.removeTag(tag);
				return null;
			}
		}.run();
	}

	
	//user - tag
	public  void breakUserTag(final Tag tag, final String uid,
			final UiOnDone uiOnDone, final UiOnError uiOnError){
		
		new SCAsyncRequest(SCPriority.IMMEDIATELY) {
			
			@Override
			public Void onResult(SCConnectionStatus status) {
				if(status != SCConnectionStatus.RESULT_OK){
					uiOnError.execute();
					return null;
				}
				//user.removeTag
				User usr = ((User)UserManager.INSTANCE.getItemById(uid));
				usr.removeTag(tag.getmId());
				//tag.removeUser
				tag.removeUser(uid);
				uiOnDone.execute();
				return null;
			}
			
			@Override
			public Void actionOnServer(Void... params) throws IOException,
			ConnectException {
				DBManager.INSTANCE.breakUserTag(uid, tag.getmId());
				return null;
			}
		}.run();
	}
	//user - tag
	public  void joinUserTag(final Long tagid, final String uid,
			final UiOnDone uiOnDone, final UiOnError uiOnError){
		
		new SCAsyncRequest(SCPriority.IMMEDIATELY) {
			
			@Override
			public Void onResult(SCConnectionStatus status) {
				if(status != SCConnectionStatus.RESULT_OK){
					uiOnError.execute();
					return null;
				}
				Tag tag = getItemById(tagid);
				//user.addTag
				((User)UserManager.INSTANCE.getItemById(uid)).addTag(tag.getmId());
				//tag.addUser
				tag.addUser(UserManager.INSTANCE.getMyID());
		
				uiOnDone.execute();
				return null;
			}
			
			@Override
			public Void actionOnServer(Void... params) throws IOException,
			ConnectException {
				DBManager.INSTANCE.joinUserTag(uid, tagid);
			return null;
			}
		}.run();
	}
	
	//hotspot - tag
	public  void breakSpotTag(final Tag tag, final Long hsid,
			final UiOnDone uiOnDone, final UiOnError uiOnError){
		
		new SCAsyncRequest(SCPriority.IMMEDIATELY) {
			
			@Override
			public Void onResult(SCConnectionStatus status) {
				if(status != SCConnectionStatus.RESULT_OK){
					uiOnError.execute();
					return null;
				}
				//hotSpot.removeTag
				((HotSpot)HotSpotManager.INSTANCE.getItemById(hsid)).removeTag(tag.getmId());
				//Tag.removeHotSpot
				tag.leaveHotSpot(hsid);
				
				uiOnDone.execute();
				return null;
			}
		
			
			@Override
			public Void actionOnServer(Void... params) throws IOException,
			ConnectException {
				DBManager.INSTANCE.breakSpotTag( hsid,tag.getmId());
				return null;
			}
		}.run();
	}
	//hotspot - tag
	public  void joinSpotTag(final Long tagid, final Long hsid,
			final UiOnDone uiOnDone, final UiOnError uiOnError){
		
		new SCAsyncRequest(SCPriority.IMMEDIATELY) {
			
			@Override
			public Void onResult(SCConnectionStatus status) {
				if(status != SCConnectionStatus.RESULT_OK){
					uiOnError.execute();
					return null;
				}
				Tag tag = getItemById(tagid);
				//hotSpot.addTag
				((HotSpot)HotSpotManager.INSTANCE.getItemById(hsid)).addTag(tag.getmId());
				//Tag.addHotSpot
				tag.joinHotSpot(hsid);
				uiOnDone.execute();
				return null;
			}
			
			@Override
			public Void actionOnServer(Void... params) throws IOException,
			ConnectException {
				DBManager.INSTANCE.joinSpotTag(hsid,tagid);
			return null;
			}
		}.run();
	}
}


