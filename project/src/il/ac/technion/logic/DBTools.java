package il.ac.technion.logic;

import java.util.LinkedList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBTools extends SQLiteOpenHelper {


	public DBTools(Context applicationContext) {

		super(applicationContext, "socialcampus_technion.db", null, 1);

	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		createTables(database);

	}

	private static void createTables(SQLiteDatabase database) {
		String createUsersTB = usersCreateStatment();

		String createHotSpotTB = hotspotsCreateStatment();

		String createTagTB = tagsCreateStatment();

		String createUserHotspotRTB = "CREATE TABLE IF NOT EXISTS userHotSpotRelation (userID TEXT, hotspotID INTEGER)";
		String createUserTagRTB =  "CREATE TABLE IF NOT EXISTS userTagRelation (userID TEXT, tagID INTEGER)";
		String createTagHotspotRTB = "CREATE TABLE IF NOT EXISTS tagHotSpotRelation (tagID INTEGER, hotspotID INTEGER)";
		String createPinnedHotspotsRTB = "CREATE TABLE IF NOT EXISTS pinnedHotSpots (userID TEXT, hotspotID INTEGER)";
		database.execSQL(createUsersTB);
		database.execSQL(createPinnedHotspotsRTB);
		database.execSQL(createHotSpotTB);
		database.execSQL(createTagTB);
		database.execSQL(createUserHotspotRTB);
		database.execSQL(createUserTagRTB);
		database.execSQL(createTagHotspotRTB);
	}

	/**
	 * @return
	 */
	private static String hotspotsCreateStatment() {
		String createUsersTB = "CREATE TABLE IF NOT EXISTS hotSpots ("
					+ HotspotsFeilds.ID.value() +" "+ HotspotsFeilds.ID.type() +" PRIMARY KEY UNIQUE ";
		for (HotspotsFeilds f : HotspotsFeilds.values()) {
			  if(f == HotspotsFeilds.ID ){
				  continue;
			  }
			  createUsersTB += " , "+ f.value() +" "+ f.type() + " "; 
			}
		createUsersTB += " )";
		return createUsersTB;
	}
	
	/**
	 * @return
	 */
	private static String tagsCreateStatment() {
		String createUsersTB = "CREATE TABLE IF NOT EXISTS tags ("
					+ TagFeilds.ID.value() +" "+ TagFeilds.ID.type() +" PRIMARY KEY UNIQUE ";
		for (TagFeilds f : TagFeilds.values()) {
			  if(f == TagFeilds.ID ){
				  continue;
			  }
			  createUsersTB += " , "+ f.value() +" "+ f.type() + " "; 
			}
		createUsersTB += " )";
		return createUsersTB;
	}
	/**
	 * @return
	 */
	private static String usersCreateStatment() {
		String createUsersTB = "CREATE TABLE IF NOT EXISTS users ("
					+ UsersFeilds.ID.value() +" "+ UsersFeilds.ID.type() +" PRIMARY KEY UNIQUE ";
		for (UsersFeilds f : UsersFeilds.values()) {
			  if(f == UsersFeilds.ID ){
				  continue;
			  }
			  createUsersTB += " , "+ f.value() +" "+ f.type() + " "; 
			}
		createUsersTB += " )";
		return createUsersTB;
	}
	/**
	 *   a class describes the userHotSpotRelation table, used for sorting the elements
	 * @author Hanna John Jadon
	 *
	 */
	public enum HotspotTagRelationFeilds{
		HOTSPOTID("hotspotID","INTEGER"), TAGID("tagID","INTEGER");
		
		private final String value;
		private final String type;
		
		private HotspotTagRelationFeilds(String v,String t) {
			value = v;
			type = t;
		}

		public String value() {
			return value;
		}
		public String type() {
			return type;
		}
	}
	/**
	 *   a class describes the userHotSpotRelation table, used for sorting the elements
	 * @author Hanna John Jadon
	 *
	 */
	public enum UserHotspotRelationFeilds{
		UID("userId","TEXT"), HOTSPOTID("hotspotID","INTEGER");
		
		private final String value;
		private final String type;
		
		private UserHotspotRelationFeilds(String v,String t) {
			value = v;
			type = t;
		}

		public String value() {
			return value;
		}
		public String type() {
			return type;
		}
	}
	/**
	 *   a class describes the userTagRelation table, used for sorting the elements
	 * @author Hanna John Jadon
	 *
	 */
	public enum UserTagRelationFeilds{
		UID("userId","TEXT"), TAGID("tagID","INTEGER");
		
		private final String value;
		private final String type;
		
		private UserTagRelationFeilds(String v,String t) {
			value = v;
			type = t;
		}

		public String value() {
			return value;
		}
		public String type() {
			return type;
		}
	}
	/**
	 *   a class describes the hotspots table, used for sorting the elements
	 * @author Hanna John Jadon
	 *
	 */
	public enum HotspotsFeilds{
		ID("hotspotId","INTEGER"), NAME("name","TEXT"),IMAGEURL("imageUrl","TEXT"),
		TIME("time","INTEGER"), ENDTIME("endTime", "INTEGER"), LATITUDE("latitude","REAL"), LONTITUDE("lontitude","REAL"),
		LOCATION("location","TEXT"),DESCTIPTION("description","TEXT"), ADMIN("ADMINID","TEXT");
		
		private final String value;
		private final String type;
		
		private HotspotsFeilds(String v,String t) {
			value = v;
			type = t;
		}

		public String value() {
			return value;
		}
		public String type() {
			return type;
		}
	}
	/**
	 *   a class describes the users table, used for sorting the elements
	 * @author Hanna John Jadon
	 *
	 */
	public enum UsersFeilds{
		ID("userId","TEXT"), NAME("name","TEXT"),IMAGEURL("imageUrl","TEXT");
		
		private final String value;
		private final String type;
		
		private UsersFeilds(String v,String t) {
			value = v;
			type = t;
		}

		public String value() {
			return value;
		}
		public String type() {
			return type;
		}
	}
	
	/**
	 *   a class describes the tags table, used for sorting the elements
	 * @author Hanna John Jadon
	 *
	 */
	public enum TagFeilds{
		ID("tagID","INTEGER"), NAME("name","TEXT"), TIME("time","INTEGER");
		private final String value;
		private final String type;
		private TagFeilds(String v,String t) {
			value = v;
			type = t;
		}

		public String value() {
			return value;
		}
		public String type() {
			return type;
		}
	}

	/**
	 *   a class describes the order of the required items
	 * @author Hanna John Jadon
	 *
	 */
	public enum Order{
		ASC("ASC"), DESC("DESC");
		private final String value;
		private Order(String s) {
			value = s;
		}

		public String value() {
			return value;
		}
	}
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {

		String deleteUsers = "DROP TABLE IF EXISTS users";
		String deleteHotspots = "DROP TABLE IF EXISTS hotSpots";
		String deleteTags = "DROP TABLE IF EXISTS tags";
		String deleteUserHotSpotRelation = "DROP TABLE IF EXISTS userHotSpotRelation";
		String deleteUserTagRelation = "DROP TABLE IF EXISTS userTagRelation";
		String deleteTagHotSpotRelation = "DROP TABLE IF EXISTS tagHotSpotRelation";

		database.execSQL(deleteUsers);
		database.execSQL(deleteHotspots);
		database.execSQL(deleteTags);
		database.execSQL(deleteUserHotSpotRelation);
		database.execSQL(deleteUserTagRelation);
		database.execSQL(deleteTagHotSpotRelation);

		onCreate(database);

	}
	///////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * @param tag
	 * @param values
	 */
	private void setHotSpotData(HotSpot hs, ContentValues values) {
		values.put(HotspotsFeilds.ID.value(), hs.getmId());
		values.put(HotspotsFeilds.NAME.value(), hs.getmName());
		values.put(HotspotsFeilds.IMAGEURL.value(), hs.getImageURL());
		values.put(HotspotsFeilds.ADMIN.value(), hs.getAdminId());
		values.put(HotspotsFeilds.LATITUDE.value(), hs.getLangt());
		values.put(HotspotsFeilds.LONTITUDE.value(), hs.getAdminId());
		values.put(HotspotsFeilds.DESCTIPTION.value(), hs.getmDesc());
		values.put(HotspotsFeilds.ENDTIME.value(), hs.getEndTime());
		values.put(HotspotsFeilds.TIME.value(), hs.getmTime());
		values.put(HotspotsFeilds.LOCATION.value(), hs.getmLocation());
	}


	public void insertHotSpot(HotSpot hs) {

		SQLiteDatabase database = getWritableDatabase();

		ContentValues values = new ContentValues();

		setHotSpotData(hs, values);

		database.insertWithOnConflict("hotSpots", null, values,
				SQLiteDatabase.CONFLICT_IGNORE);

		database.close();

	}



	public void insertHotSpots(List<HotSpot> hotSpots) {

		for (HotSpot hs : hotSpots){
			insertHotSpot(hs);
		}
	}

	public int updateHotSpot(HotSpot u) {

		SQLiteDatabase database = getWritableDatabase();

		ContentValues values = new ContentValues();

		setHotSpotData(u, values);
		int res =  database.update("hotSpots", values, " "+HotspotsFeilds.ID.value() + " = ?",
				new String[] { String.valueOf(u.getmId())});
		database.close();
		return res;
	}

	public void deleteHotSpot(Long hsID) {

		SQLiteDatabase database = getWritableDatabase();

		String deleteQuery = "DELETE FROM hotSpots WHERE "+ HotspotsFeilds.ID.value() +" '" + String.valueOf(hsID) + "'";

		database.execSQL(deleteQuery);
		database.close();

	}

	public HotSpot getHotSpotInfo(Long hsID) {

		HotSpot $ = null;

		SQLiteDatabase database = getReadableDatabase();

		String selectQuery = "SELECT * FROM hotSpots WHERE "+HotspotsFeilds.ID.value() +" '" + String.valueOf(hsID) + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);
		$ = readHotSpot(cursor);
		cursor.close();
		database.close();
		return $;

	}


	/**
	 * reads a row from tags from the received cursor and generates a tag object
	 * 
	 * @param cursor
	 * 			- a tags cursor
	 * @return the requited object, null in case of failure 
	 */
	private HotSpot readHotSpot(Cursor cursor) {
		HotSpot $ = null;
		if (cursor != null && cursor.moveToFirst()) {
			
			$ = new HotSpot(cursor.getLong(0), cursor.getLong(3), cursor.getLong(4), cursor.getString(1), cursor.getDouble(5) ,
					cursor.getDouble(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(2));
		} 
		return $;
	}

	/**
	 * returns all tags in the database sorted by tagID, name or time
	 * @param fieldSort - an enum for the column 
	 * @return
	 */
	public List<HotSpot> getAllHotSpots(HotspotsFeilds fieldSort, Order o) {

		List<HotSpot> result = new LinkedList<HotSpot>();

		String selectAllQuery = "SELECT * FROM hotspots ORDER BY "
				+ fieldSort.value() +" "+ o.value() ;

		SQLiteDatabase database = getWritableDatabase();

		Cursor cursor = database.rawQuery(selectAllQuery, null);
		HotSpot u = readHotSpot(cursor) ;
		while (u != null){
			result.add(u);
			u = readHotSpot(cursor) ;
		} 
		cursor.close();
		database.close();
		return result;

	}
	////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * @param tag
	 * @param values
	 */
	private void setUserData(User u, ContentValues values) {
		values.put(UsersFeilds.ID.value(), u.getmId());
		values.put(UsersFeilds.NAME.value(), u.getmName());
		values.put(UsersFeilds.IMAGEURL.value(), u.getmImage());
	}


	public void insertUser(User u) {

		SQLiteDatabase database = getWritableDatabase();

		ContentValues values = new ContentValues();

		setUserData(u, values);

		database.insertWithOnConflict("users", null, values,
				SQLiteDatabase.CONFLICT_IGNORE);

		database.close();

	}



	public void insertUserss(List<User> users) {

		for (User u : users){
			insertUser(u);
		}
	}

	public int updateUser(User u) {

		SQLiteDatabase database = getWritableDatabase();

		ContentValues values = new ContentValues();

		setUserData(u, values);
		int res =  database.update("users", values, " "+UsersFeilds.ID.value() + " = ?",
				new String[] { String.valueOf(u.getmId())});
		database.close();
		return res;
	}

	public void deleteUser(String userID) {

		SQLiteDatabase database = getWritableDatabase();

		String deleteQuery = "DELETE FROM users WHERE "+UsersFeilds.ID.value() +" '" + userID + "'";

		database.execSQL(deleteQuery);
		database.close();

	}

	public User getUserInfo(String userID) {

		User $ = null;

		SQLiteDatabase database = getReadableDatabase();

		String selectQuery = "SELECT * FROM users WHERE "+UsersFeilds.ID.value() +" '" +userID + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);
		$ = readUser(cursor);
		cursor.close();
		database.close();
		return $;

	}



	/**
	 * returns all tags in the database sorted by tagID, name or time
	 * @param fieldSort - an enum for the column 
	 * @return
	 */
	public List<User> getAllUsers(UsersFeilds fieldSort, Order o) {

		List<User> result = new LinkedList<User>();

		String selectAllQuery = "SELECT * FROM users ORDER BY "
				+ fieldSort.value() +" "+ o.value() ;

		SQLiteDatabase database = getWritableDatabase();

		Cursor cursor = database.rawQuery(selectAllQuery, null);
		User u = readUser(cursor) ;
		while (u != null){
			result.add(u);
			u = readUser(cursor) ;
		} 
		cursor.close();
		database.close();
		return result;

	}
	/**
	 * reads a row from tags from the received cursor and generates a tag object
	 * 
	 * @param cursor
	 * 			- a tags cursor
	 * @return the requited object, null in case of failure 
	 */
	private User readUser(Cursor cursor) {
		User $ = null;
		if (cursor != null && cursor.moveToFirst()) {
	
			$ = new User(cursor.getString(0),
					cursor.getString(2), 
					cursor.getString(1));
		} 
		return $;
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * @param tag
	 * @param values
	 */
	private void setTagData(Tag tag, ContentValues values) {
		values.put("tagId", tag.getmId());
		values.put("name", tag.getmName());
		values.put("time", tag.getmTime());
	}


	public void insertTag(Tag tag) {

		SQLiteDatabase database = getWritableDatabase();

		ContentValues values = new ContentValues();

		setTagData(tag, values);

		database.insertWithOnConflict("tags", null, values,
				SQLiteDatabase.CONFLICT_IGNORE);

		database.close();

	}



	public void insertTags(List<Tag> tags) {

		for (Tag t : tags){
			insertTag(t);
		}
	}

	public int updateTag(Tag t) {

		SQLiteDatabase database = getWritableDatabase();

		ContentValues values = new ContentValues();

		setTagData(t, values);
		int res =  database.update("tags", values, "tagId" + " = ?",
				new String[] { String.valueOf(t.getmId())});
		database.close();
		return res;
	}

	public void deleteTag(Long tagId) {

		SQLiteDatabase database = getWritableDatabase();

		String deleteQuery = "DELETE FROM tags WHERE tagId='" + String.valueOf(tagId) + "'";

		database.execSQL(deleteQuery);
		database.close();

	}

	public Tag getTagInfo(Long tagId) {

		Tag $ = null;

		SQLiteDatabase database = getReadableDatabase();

		String selectQuery = "SELECT * FROM tags WHERE tagId='" 
				+ String.valueOf(tagId) + "'";
		Cursor cursor = database.rawQuery(selectQuery, null);
		$ = readTag(cursor);
		cursor.close();
		database.close();
		return $;

	}

	/**
	 * reads a row from tags from the received cursor and generates a tag object
	 * 
	 * @param cursor
	 * 			- a tags cursor
	 * @return the requited object, null in case of failure 
	 */
	private Tag readTag(Cursor cursor) {
		Tag $ = null;
		if (cursor != null && cursor.moveToFirst()) {
			$ = new Tag(cursor.getLong(0),
					cursor.getString(1), 
					cursor.getLong(2));
		} 
		return $;
	}


	/**
	 * returns all tags in the database sorted by tagID, name or time
	 * @param fieldSort - an enum for the column 
	 * @return
	 */
	public List<Tag> getAllTags(TagFeilds fieldSort, Order o) {

		List<Tag> result = new LinkedList<Tag>();

		String selectAllQuery = "SELECT * FROM tags ORDER BY "
				+ fieldSort.value() +" "+ o.value() ;

		SQLiteDatabase database = getWritableDatabase();

		Cursor cursor = database.rawQuery(selectAllQuery, null);
		Tag t = readTag(cursor) ;
		while (t != null){
			result.add(t);
			t = readTag(cursor) ;
		} 
		cursor.close();
		database.close();
		return result;

	}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	
	//join/break 
	void breakUserHotSpot(Long hid, String uid){

		SQLiteDatabase database = getWritableDatabase();

		String deleteQuery = "DELETE FROM userHotSpotRelation WHERE userID='" + uid + "' AND hotspotID='"+hid+"'";

		database.execSQL(deleteQuery);
		database.close();
		
	}
	void joinUserHotSpot(Long hid, String uid){
		SQLiteDatabase database = getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put("userID", uid);
		values.put("hotspotID", hid);
		database.insertWithOnConflict("userHotSpotRelation", null, values,
				SQLiteDatabase.CONFLICT_IGNORE);

		database.close();

	}
	
	void breakUserTag(String uid, Long tid){
		SQLiteDatabase database = getWritableDatabase();

		String deleteQuery = "DELETE FROM userTagRelation WHERE userID='" + uid + "' AND tagID='"+tid+"'";

		database.execSQL(deleteQuery);
		database.close();
	}
	void joinUserTag(String uid, Long tid){
		SQLiteDatabase database = getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put("userID", uid);
		values.put("tagID", tid);
		database.insertWithOnConflict("userTagRelation", null, values,
				SQLiteDatabase.CONFLICT_IGNORE);

		database.close();
	}
	
	void breakSpotTag(Long hid, Long tid){
		SQLiteDatabase database = getWritableDatabase();

		String deleteQuery = "DELETE FROM tagHotSpotRelation WHERE hotspotID='" + hid + "' AND tagID='"+tid+"'";

		database.execSQL(deleteQuery);
		database.close();
	}
	void joinSpotTag(Long hid, Long tid){
		SQLiteDatabase database = getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put("userID", hid);
		values.put("tagID", tid);
		database.insertWithOnConflict("tagHotSpotRelation", null, values,
				SQLiteDatabase.CONFLICT_IGNORE);

		database.close();
	}

	void deletePinnedHotSpotTag(Long hid, String uid){
		SQLiteDatabase database = getWritableDatabase();

		String deleteQuery = "DELETE FROM pinnedHotSpots WHERE hotspotID='" + hid + "' AND userID='"+uid+"'";

		database.execSQL(deleteQuery);
		database.close();
	}
	void addPinnedHotSpots(Long hid, String uid){
		SQLiteDatabase database = getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put("userID", uid);
		values.put("hotspotID", hid);
		database.insertWithOnConflict("pinnedHotSpots", null, values,
				SQLiteDatabase.CONFLICT_IGNORE);

		database.close();
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void clearTables() {
		SQLiteDatabase database = getWritableDatabase();
		String deletePinnedHotSpots = "DROP TABLE IF EXISTS pinnedHotSpots";
		database.execSQL(deletePinnedHotSpots);

		database.close();
		clearSyncedTables();

	}

	public void clearSyncedTables() {

		SQLiteDatabase database = getWritableDatabase();

		String deleteUsers = "DROP TABLE IF EXISTS users";
		String deleteHotspots = "DROP TABLE IF EXISTS hotSpots";
		String deleteTags = "DROP TABLE IF EXISTS tags";
		String deleteUserHotSpotRelation = "DROP TABLE IF EXISTS userHotSpotRelation";
		String deleteUserTagRelation = "DROP TABLE IF EXISTS userTagRelation";
		String deleteTagHotSpotRelation = "DROP TABLE IF EXISTS tagHotSpotRelation";

		database.execSQL(deleteUsers);
		database.execSQL(deleteHotspots);
		database.execSQL(deleteTags);
		database.execSQL(deleteUserHotSpotRelation);
		database.execSQL(deleteUserTagRelation);
		database.execSQL(deleteTagHotSpotRelation);

		database.close();

	}

}
