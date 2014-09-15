package il.ac.technion.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

//import com.google.gson.Gson;
//import com.google.gson.JsonSyntaxException;
//import com.google.gson.reflect.TypeToken;

/**
 * 
 * This singleton supply implementation for the LetsEatHotSpot, ILetsEatRestaurant,
 *	ILetsEatUser interfaces  these functions must be called in the back ground by using service or Asyntasck.
 *
 */
public enum DBManager  {
	INSTANCE;
	
	public User getSonya(){
		return new User("103967014019877216822","https://lh6.googleusercontent.com/-ajL_apwzsJ4/AAAAAAAAAAI/AAAAAAAADK0/RmYXAMzxYOo/photo.jpg?sz=50","Sonya Gendelman");
	}
	private SCReturnCode resCode = SCReturnCode.SUCCESS;
	public SCReturnCode getResult(){
		return resCode;
	}
	
	ArrayList<HotSpot> HS = new ArrayList<HotSpot>();
	ArrayList<User> USR = new ArrayList<User>();
	ArrayList<Tag> TAG = new ArrayList<Tag>();
	
	DBManager(){
		Tag t1 = new Tag(1L,"Frisbee");
		Tag t2 = new Tag(2L,"Sports");
		Tag t3 = new Tag(3L,"TeamSports");
		Tag t4 = new Tag(4L,"CentralLawn");
		Tag t5 = new Tag(5L,"Study");
		Tag t6 = new Tag(6L,"SocialCampusProj");
		Tag t7 = new Tag(7L,"Colloquium");
		Tag t8 = new Tag(8L,"Salsa");
		Tag t9 = new Tag(9L,"ClearHall");
		
		User u1 = new User("103967014019877216822","https://lh6.googleusercontent.com/-ajL_apwzsJ4/AAAAAAAAAAI/AAAAAAAADK0/RmYXAMzxYOo/photo.jpg?sz=50","Sonya Gendelman");
		User u2 = new User("2L","","Hanna-John Jadon");
		User u3 = new User("3L","http://www.a2gs.com/_Media/jimmaricondo_med.jpeg","Jim Maricondo");
		User u4 = new User("4L","","Xin Song");
		User u5 = new User("5L","https://lh3.googleusercontent.com/-47yBoS19cSw/AAAAAAAAAAI/AAAAAAAAAAA/rqg003pc3OQ/photo.jpg","Victoria Bellotti");
		User u6 = new User("","","anonym temp");
		
		u1.addTag(1L);
		u1.addTag(2L);
		u1.addTag(3L);
		u1.addTag(4L);
		u1.addTag(5L);
		u1.addTag(6L);
		u1.addTag(7L);
		u1.addTag(8L);
		u1.addTag(9L);
		
		t1.addUser("103967014019877216822");
		t2.addUser("103967014019877216822");
		t3.addUser("103967014019877216822");
		t4.addUser("103967014019877216822");
		t5.addUser("103967014019877216822");
		t6.addUser("103967014019877216822");
		t7.addUser("103967014019877216822");
		t8.addUser("103967014019877216822");
		t9.addUser("103967014019877216822");

		//Set<Long> mTags, Set<Long> mUseres
		HotSpot h1 = new HotSpot(1L,0L,0L,"Ultimate Frisbee",32.777261, 35.0230416,"at taub 5","Ultimate Frisbee","1L","http://www.discgolfstation.com/assets/images/ultimate-frisbee2.jpg");
		HotSpot h2 = new HotSpot(2L,0L,0L,"Social Campus Meeting #5",32.777929, 35.021593,"at taub 5","Social Campus Meeting #5","2L","http://img1.wikia.nocookie.net/__cb20120402214339/masseffect/images/d/db/Citadel_Space_Codex_Image.jpg");
		HotSpot h3 = new HotSpot(3L,0L,0L,"Colloquium Prof Jan Vitek",32.776448, 35.022885,"at taub 5","Colloquium Prof Jan Vitek","1L","");
		//HotSpot h4 = new HotSpot(4L,0L,0L,"Cubban Salasa Party",32.776449, 35.022886,"at taub 5","Cubban Salasa Party","2L","");
		
		HS.add(h1);
		HS.add(h2);
		HS.add(h3);
		//HS.add(h4);
		
		USR.add(u1);
		USR.add(u2);
		USR.add(u3);
		USR.add(u4);
		USR.add(u5);
		USR.add(u6);

		TAG.add(t1);
		TAG.add(t2);
		TAG.add(t3);
		TAG.add(t4);
		TAG.add(t5);
		TAG.add(t6);
		TAG.add(t7);
		TAG.add(t8);
		TAG.add(t9);
		
		joinUserHotSpot(3L,"103967014019877216822");
//		joinUserTag("",6L);
//		joinUserTag("",7L);
//		joinUserTag("",2L);
//		joinUserTag("",1L);
//		joinUserTag("",3L);
//		joinUserTag("",4L);
//		joinUserTag("",5L);
		
		joinSpotTag(1L, 1L);
		joinSpotTag(1L, 2L);
		joinSpotTag(1L, 3L);
		joinSpotTag(1L, 4L);
		
		
		joinUserHotSpot(1L, "3L");
		joinUserHotSpot(1L, "5L");
		joinUserHotSpot(1L,"103967014019877216822");
		
		
	}
	//get by id
	private Tag getTag(Long id){
		for(Tag t:TAG){
			if (t.getmId().equals(id))
				return t;
		}
		return null;
	}
	private User getUser(String id){
		for(User t:USR){
			if (t.getmId().equals(id))
				return t;
		}
		return null;
	}
	private HotSpot getSpot(Long id){
		for(HotSpot t:HS){
			if (t.getmId().equals(id))
				return t;
		}
		return null;
	}
	
	//sync
	public List<HotSpot> getAllHotSpots() {
		return HS;
	}
	List<HotSpot> getHotSpotsByRadios(double latitude,double lontitude, double radios){
		return HS;
	}
	List<User> getUsers(){
		return USR;
	}
	List<Tag> getTags(){
		return TAG;
	}
	
	//add
	Tag addTag(Tag tag){
		TAG.add(tag);
		return tag;
	}
	HotSpot addHotSpot(HotSpot h){
		HS.add(h);
		return h;
	}
	User addUser(User u){
		USR.add(u);
		return u;
	}
	
	//remove
	void removeTag(Tag tag){
		TAG.remove(tag);
	}
	void removeHotSpot(HotSpot h){
		HS.remove(h);
	}
	void removeUser(User u){
		USR.remove(u);
	}

	//update
	void updateHotSpot(HotSpot hotSpot){
		
	}
	void updateTag(Tag t){
		
	}
	void updateUser(User u){
		
	}
	
	//join/break 
	void breakUserHotSpot(Long hid, String uid){
		getSpot(hid).getmUsers().remove(uid);
		getUser(uid).getmHotSpots().remove(hid);
	}
	void joinUserHotSpot(Long hid, String uid){
		getSpot(hid).getmUsers().add(uid);
		getUser(uid).getmHotSpots().add(hid);
	}
	
	void breakUserTag(String uid, Long tid){
		getTag(tid).getmUsers().remove(uid);
		getUser(uid).getmTags().remove(tid);
	}
	void joinUserTag(String uid, Long tid){
		getTag(tid).getmUsers().add(uid);
		getUser(uid).getmTags().add(tid);
	}
	
	void breakSpotTag(Long hid, Long tid){
		getSpot(hid).getmTags().remove(tid);
		getTag(tid).getmHotSpots().remove(hid);
	}
	void joinSpotTag(Long hid, Long tid){
		getSpot(hid).getmTags().add(tid);
		getTag(tid).getmHotSpots().add(hid);
	}


	
////////////////////////////////	
// 	Old file
////////////////////////////////
//
//	private final String servletName = "LetsEatServlet";
//	private final String operation = "function";
//	private SCReturnCode resCode ;
//
//	public SCReturnCode getResult(){
//		return resCode;
//	}
//
//	
//	public User addUser(User user) {
//		User u = null;
//		try {
//			u = new Gson().fromJson(Communicator.execute(servletName,
//					operation, LetsEatFunctions.ADD_USER.toString(), "user",
//					new Gson().toJson(user)), User.class);
//			if (u == null){
//				resCode =  SCReturnCode.FAILURE;
//			}else{
//				resCode = SCReturnCode.SUCCESS;
//			}
//
//		} catch (JsonSyntaxException e) {
//			resCode = SCReturnCode.BAD_PARAM;
//			e.printStackTrace();
//		} catch (IOException e) {
//			resCode = SCReturnCode.BAD_CONNECTION;
//			e.printStackTrace();
//		}
//	
//		return u;
//	}
//
//
//	
//	public User updateUser(User user) {
//		User u = null;
//		try {
//			u = new Gson().fromJson(Communicator.execute(servletName,
//					operation, LetsEatFunctions.UPDATE_USER.toString(), "user",
//					new Gson().toJson(user)), User.class);
//			if (u == null){
//				resCode =  SCReturnCode.FAILURE;
//			} else{
//				resCode = SCReturnCode.SUCCESS;
//			}
//
//		} catch (JsonSyntaxException e) {
//			resCode = SCReturnCode.BAD_PARAM;
//			e.printStackTrace();
//		} catch (IOException e) {
//			resCode = SCReturnCode.BAD_CONNECTION;
//			e.printStackTrace();
//		}
//		
//		return u;
//	}
//
//	
////	public SCReturnCode removeUser(Long userID) {
////		try {
////			return new Gson().fromJson(Communicator.execute(servletName,
////					operation, LetsEatFunctions.REMOVE_USER.toString(), "userID",
////					new Gson().toJson(userID)), SCReturnCode.class);
////		} catch (JsonSyntaxException e) {
////			resCode = SCReturnCode.BAD_PARAM;
////			e.printStackTrace();
////		} catch (IOException e) {
////			resCode = SCReturnCode.BAD_CONNECTION;
////			e.printStackTrace();
////		}
////		return resCode;
////
////	}
//
//	
////	public User getUser(Long userID) {
////		User u = null;
////		try {
////			u = new Gson().fromJson(Communicator.execute(servletName, "query",
////			        "yes", operation, LetsEatFunctions.GET_USER.toString(), "userID",
////					new Gson().toJson(userID)), User.class);
////			if (u == null){
////				resCode =  SCReturnCode.FAILURE;
////			} else{
////				resCode = SCReturnCode.SUCCESS;
////			}
////
////		} catch (JsonSyntaxException e) {
////			resCode = SCReturnCode.BAD_PARAM;
////			e.printStackTrace();
////		} catch (IOException e) {
////			resCode = SCReturnCode.BAD_CONNECTION;
////			e.printStackTrace();
////		}
////		return u;
////	}
//
//	
////	public SCReturnCode updateUserLocation(Long userID, Double latitude,
////			Double longitude, Long timeStamp) {
////		try{
////			return new Gson().fromJson(Communicator.execute(servletName,
////					operation, LetsEatFunctions.UPDATE_USER_LOCATION.toString(), 
////					"userID", new Gson().toJson(userID),
////					"latitude", new Gson().toJson(latitude),
////					"longitude", new Gson().toJson(longitude),
////					"timeStamp", new Gson().toJson(timeStamp)), SCReturnCode.class);
////		} catch (JsonSyntaxException e) {
////			resCode = SCReturnCode.BAD_PARAM;
////			e.printStackTrace();
////		} catch (IOException e) {
////			resCode = SCReturnCode.BAD_CONNECTION;
////			e.printStackTrace();
////		}
////		return resCode;
////	}
//
//	
////	public List<User> getUsersByRadios(Double latitude, Double longitude,
////			Double radios) {
////		List<User> l = null;
////		try {
////			l =  new Gson().fromJson(Communicator.execute(servletName, "query",
////				        "yes", operation, LetsEatFunctions.GET_USERS_BY_RADIOS.toString(),
////				        "latitude", new Gson().toJson(latitude),
////				        "longitude", new Gson().toJson(longitude),
////				        "radios", new Gson().toJson(radios)), new TypeToken<List<User>>() {}.getType());
////			if (l == null){
////				resCode =  SCReturnCode.FAILURE;
////			} else{
////				resCode = SCReturnCode.SUCCESS;
////			}
////			
////		} catch (JsonSyntaxException e) {
////			resCode = SCReturnCode.BAD_PARAM;
////			e.printStackTrace();
////		} catch (IOException e) {
////			resCode = SCReturnCode.BAD_CONNECTION;
////			e.printStackTrace();
////		}
////		return l;
////		
////	}
//	
////	public Restaurant addRestaurant(Restaurant restaurant) {
////		Restaurant r = null;
////		try {
////			r = new Gson().fromJson(Communicator.execute(servletName,
////					operation, LetsEatFunctions.ADD_RESTAURANT.toString(), "restaurant",
////			        new Gson().toJson(restaurant)), Restaurant.class);
////			if (r == null){
////				resCode= SCReturnCode.FAILURE;
////			} else{
////				resCode = SCReturnCode.SUCCESS;
////			}
////
////		} catch (JsonSyntaxException e) {
////			e.printStackTrace();
////			resCode= SCReturnCode.BAD_PARAM;
////		} catch (IOException e) {
////			e.printStackTrace();
////			resCode= SCReturnCode.BAD_CONNECTION;
////		}
////		return r;
////	}
//
//
//	
////	public Restaurant updateRestaurant(Restaurant restaurant) {
////		Restaurant r = null;
////		try {
////			r = new Gson().fromJson(Communicator.execute(servletName,
////					operation, LetsEatFunctions.UPDATE_RESTAURANT.toString(), "restaurant",
////			        new Gson().toJson(restaurant)), Restaurant.class);
////			if (r == null){
////				resCode= SCReturnCode.FAILURE;
////			} else{
////				resCode = SCReturnCode.SUCCESS;
////			}
////
////		} catch (JsonSyntaxException e) {
////			e.printStackTrace();
////			resCode= SCReturnCode.BAD_PARAM;
////		} catch (IOException e) {
////			e.printStackTrace();
////			resCode= SCReturnCode.BAD_CONNECTION;
////		}
////		return r;
////	}
//
//	
////	public SCReturnCode removeRestaurant(Long restaurantID) {
////		try {
////			return new Gson().fromJson(Communicator.execute(servletName,
////					operation, LetsEatFunctions.REMOVE_RESTAURANT.toString(), "restaurantID",
////			        new Gson().toJson(restaurantID)), SCReturnCode.class);
////
////		} catch (JsonSyntaxException e) {
////			e.printStackTrace();
////			resCode= SCReturnCode.BAD_PARAM;
////		} catch (IOException e) {
////			e.printStackTrace();
////			resCode= SCReturnCode.BAD_CONNECTION;
////		}
////		return resCode;
////	}
//
//	
////	public Restaurant getRestaurant(Long restaurantID) {
////		Restaurant r = null;
////		try {
////			r = new Gson().fromJson(Communicator.execute(servletName, "query",
////			        "yes", operation, LetsEatFunctions.GET_RESTAURANT.toString(), "restaurantID",
////					new Gson().toJson(restaurantID)), Restaurant.class);
////			if (r == null){
////				resCode =  SCReturnCode.FAILURE;
////			} else{
////				resCode = SCReturnCode.SUCCESS;
////			}
////
////		} catch (JsonSyntaxException e) {
////			resCode = SCReturnCode.BAD_PARAM;
////			e.printStackTrace();
////		} catch (IOException e) {
////			resCode = SCReturnCode.BAD_CONNECTION;
////			e.printStackTrace();
////		}
////		return r;
////	}
//
//	
////	public HotSpot addHotSpot(HotSpot delivery) {
////		HotSpot d = null;
////		try {
////			d = new Gson().fromJson(Communicator.execute(servletName,
////					operation, LetsEatFunctions.ADD_DELIVERY.toString(), "delivery",
////			        new Gson().toJson(delivery)), HotSpot.class);
////			if (d == null){
////				resCode= SCReturnCode.FAILURE;
////			} else{
////				resCode = SCReturnCode.SUCCESS;
////			}
////
////		} catch (JsonSyntaxException e) {
////			e.printStackTrace();
////			resCode= SCReturnCode.BAD_PARAM;
////		} catch (IOException e) {
////			e.printStackTrace();
////			resCode= SCReturnCode.BAD_CONNECTION;
////		}
////		return d;
////	}
//
//	
////	public HotSpot updateHotSpot(HotSpot delivery) {
////		HotSpot d = null;
////		try {
////			d = new Gson().fromJson(Communicator.execute(servletName,
////					operation, LetsEatFunctions.UPDATE_DELIVERY.toString(), "delivery",
////			        new Gson().toJson(delivery)), HotSpot.class);
////			if (d == null){
////				resCode= SCReturnCode.FAILURE;
////			} else{
////				resCode = SCReturnCode.SUCCESS;
////			}
////
////		} catch (JsonSyntaxException e) {
////			e.printStackTrace();
////			resCode= SCReturnCode.BAD_PARAM;
////		} catch (IOException e) {
////			e.printStackTrace();
////			resCode= SCReturnCode.BAD_CONNECTION;
////		}
////		return d;
////	}
//
//	
////	public SCReturnCode removeHotSpot(Long deliveryID) {
////		try {
////			return new Gson().fromJson(Communicator.execute(servletName,
////					operation, LetsEatFunctions.REMOVE_DELIVERY.toString(), "deliveryID",
////			        new Gson().toJson(deliveryID)), SCReturnCode.class);
////		} catch (JsonSyntaxException e) {
////			e.printStackTrace();
////			resCode= SCReturnCode.BAD_PARAM;
////		} catch (IOException e) {
////			e.printStackTrace();
////			resCode= SCReturnCode.BAD_CONNECTION;
////		}
////		return resCode;
////	}
//
//	
////	public HotSpot getHotSpot(Long deliveryID) {
////		HotSpot d = null;
////		try {
////			d = new Gson().fromJson(Communicator.execute(servletName, "query",
////			        "yes", operation, LetsEatFunctions.GET_DELIVERY.toString(), "deliveryID",
////					new Gson().toJson(deliveryID)), HotSpot.class);
////			if (d == null){
////				resCode =  SCReturnCode.FAILURE;
////			} else{
////				resCode = SCReturnCode.SUCCESS;
////			}
////
////		} catch (JsonSyntaxException e) {
////			resCode = SCReturnCode.BAD_PARAM;
////			e.printStackTrace();
////		} catch (IOException e) {
////			resCode = SCReturnCode.BAD_CONNECTION;
////			e.printStackTrace();
////		}
////		return d;
////	}
//
//	
////	public List<HotSpot> getHotSpotsByRadios(Double latitude,
////			Double longitude, Double radios) {
////		List<HotSpot> l = null;
////		try {
////			l = new Gson().fromJson(Communicator.execute(servletName, "query",
////				        "yes", operation,
////				        LetsEatFunctions.GET_DELIVERIES_BY_RADIOS.toString(),
////				        "latitude", new Gson().toJson(latitude),
////				        "longitude", new Gson().toJson(longitude),
////				        "radios", new Gson().toJson(radios)), new TypeToken<List<HotSpot>>() {}.getType());
////			if (l == null){
////				resCode =  SCReturnCode.FAILURE;
////			} else{
////				resCode = SCReturnCode.SUCCESS;
////			}
////			
////		} catch (JsonSyntaxException e) {
////			resCode = SCReturnCode.BAD_PARAM;
////			e.printStackTrace();
////		} catch (IOException e) {
////			resCode = SCReturnCode.BAD_CONNECTION;
////			e.printStackTrace();
////		}
////		return l;
////	}
//
//	
////	public List<HotSpot> getDeliveriesByOwner(Long ownerID) {
////		List<HotSpot> l = null;
////		try {
////			l = new Gson().fromJson(Communicator.execute(servletName, "query",
////				        "yes", operation, LetsEatFunctions.GET_DELIVERIES_BY_OWNER.toString(),
////				        "ownerID", new Gson().toJson(ownerID)), 
////				        new TypeToken<List<HotSpot>>() {}.getType());
////			
////			if (l == null){
////				resCode =  SCReturnCode.FAILURE;
////			} else{
////				resCode = SCReturnCode.SUCCESS;
////			}
////			
////		} catch (JsonSyntaxException e) {
////			resCode = SCReturnCode.BAD_PARAM;
////			e.printStackTrace();
////		} catch (IOException e) {
////			resCode = SCReturnCode.BAD_CONNECTION;
////			e.printStackTrace();
////		}
////		return l;
////	}
////
////	
////	public void joinHotSpot(Long hotSpotID, Long UserId) {
////
////		try {
////			new Gson().fromJson(Communicator.execute(servletName, "query",
////				        "yes", operation, LetsEatFunctions.JOIN_DELIVERY.toString(),
////				        "deliveryID", new Gson().toJson(hotSpotID),
////				        "UserId", new Gson().toJson(UserId)), Order.class);
////			if (or == null){
////				resCode =  SCReturnCode.FAILURE;
////			}else{
////				resCode = SCReturnCode.SUCCESS;
////			}
////	
////		} catch (JsonSyntaxException e) {
////			resCode = SCReturnCode.BAD_PARAM;
////			e.printStackTrace();
////		} catch (IOException e) {
////			resCode = SCReturnCode.BAD_CONNECTION;
////			e.printStackTrace();
////		}
////		return or;
////	}
//
//	
//	public SCReturnCode leaveHotSpot(Long deliveryID, Long UserID) {
//		try {
//			return new Gson().fromJson(Communicator.execute(servletName, "query",
//				        "yes", operation, LetsEatFunctions.LEAVE_DELIVERY.toString(),
//				        "deliveryID",  new Gson().toJson(deliveryID),
//				        "UserID",  new Gson().toJson(UserID)), 
//				        SCReturnCode.class);
//	
//		} catch (JsonSyntaxException e) {
//			resCode = SCReturnCode.BAD_PARAM;
//			e.printStackTrace();
//		} catch (IOException e) {
//			resCode = SCReturnCode.BAD_CONNECTION;
//			e.printStackTrace();
//		}
//		return resCode;
//	}
//
//	
//	public List<Order> getOrders(Long deliveryID) {
//		List<Order> l = null;
//		try {
//			l = new Gson().fromJson(Communicator.execute(servletName, "query",
//				        "yes", operation, LetsEatFunctions.GET_ORDERS.toString(),
//				        "deliveryID", new Gson().toJson(deliveryID)), 
//				        new TypeToken<List<Order>>() {}.getType());
//			
//			if (l == null){
//				resCode =  SCReturnCode.FAILURE;
//			} else{
//				resCode = SCReturnCode.SUCCESS;
//			}
//			
//		} catch (JsonSyntaxException e) {
//			resCode = SCReturnCode.BAD_PARAM;
//			e.printStackTrace();
//		} catch (IOException e) {
//			resCode = SCReturnCode.BAD_CONNECTION;
//			e.printStackTrace();
//		}
//		return l;
//	}
//
//
//	public List<BaseObj> getTagsByRadios(Double latitude,
//			Double lontitude, Double radios) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//
//	public Tag addTag(Tag hotSpot) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//
//	public void updateTag(Tag mTag) {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//	public void removeTag(Long getmId) {
//		// TODO Auto-generated method stub
//		
//		
//	}
//
//
//	public void leaveTag(Long getmId, Long uid) {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//	public void joinTag(Long getmId, Long uid) {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//	public void joinTag2(Long getmId, Long hsid) {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//	public void breakUserHotSpot(Long getmId, Long uid) {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//	public void joinUserHotSpot(Long getmId, Long uid) {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//	public void breakUserTag(Long getmId, Long uid) {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//	public void joinUserTag(Long getmId, Long uid) {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//	public void breakSpotTag(Long getmId, Long hsid) {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//	public void joinSpotTag(Long getmId, Long hsid) {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//	public Collection<? extends User> getUsers() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//
//	public Collection<? extends Tag> getTags() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//
//	DBManager.INSTANCE.getAllHotSpots() Collection<? extends HotSpot> getAllHotSpots() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	
}
