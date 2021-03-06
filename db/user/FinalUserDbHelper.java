package com.lingtuan.firefly.db.user;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lingtuan.firefly.db.TableField;

/**
 * The user information database
 */
public class FinalUserDbHelper extends SQLiteOpenHelper {

	public static final int DB_VERSION = 1;
	
	public FinalUserDbHelper(Context context, String dbName) {
		super(context, dbName, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTable(db);
		createIndex(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		createTable(db);
		createIndex(db);
	}

	//Increase the part of the index, improve the query speed
	private void createIndex(SQLiteDatabase db){
		String sql1 = "CREATE INDEX IF NOT EXISTS chat_chatid_data7 ON " + TableField.TABLE_CHAT
				  + "(" + TableField.FIELD_CHAT_ID + "," + TableField.FIELD_RESERVED_DATA7 + "," + TableField._ID + " desc)";
		String sql2 = "CREATE INDEX IF NOT EXISTS chat_data1 on " + TableField.TABLE_CHAT
				  + "(" + TableField.FIELD_RESERVED_DATA1  + ")";
		String sql3 = "CREATE INDEX IF NOT EXISTS chatevent_hidden_data7 ON " + TableField.TABLE_CHAT_EVENT + "("+ TableField.FIELD_CHAT_HIDDEN + "," + TableField.FIELD_RESERVED_DATA7 + " ," + TableField.FIELD_CHAT_MSGTIME + " desc)";
		String sql4 = "CREATE INDEX IF NOT EXISTS chatevent_chatid ON " + TableField.TABLE_CHAT_EVENT
				  + "(" + TableField.FIELD_CHAT_ID  + ")";
		String sql5 = "CREATE INDEX IF NOT EXISTS friend_gid_uid ON " + TableField.TABLE_FRIEND
				+ "(" + TableField.FIELD_GROUP_GID + "," + TableField.FIELD_FRIEND_UID + ")";
		db.execSQL(sql1);
		db.execSQL(sql2);
		db.execSQL(sql3);
		db.execSQL(sql4);
		db.execSQL(sql5);
	}
	/**
	 * abandoned
	 * @param db
	 */
	@Deprecated
	private void dropTable(SQLiteDatabase db) {
		String sql_friend = "DROP TABLE IF EXISTS " + TableField.TABLE_FRIEND;
//		String sql_group = "DROP TABLE IF EXISTS " + TableField.TABLE_GROUP;
		String sql_chat = "DROP TABLE IF EXISTS " + TableField.TABLE_CHAT;
		String sql_chat_event = "DROP TABLE IF EXISTS " + TableField.TABLE_CHAT_EVENT;
		String sql_dynamic = "DROP TABLE IF EXISTS " + TableField.TABLE_DYNAMIC_NOTIF;
		db.execSQL(sql_friend);
//		db.execSQL(sql_group);
		db.execSQL(sql_chat);
		db.execSQL(sql_chat_event);
		db.execSQL(sql_dynamic);
	}

	private void createTable(SQLiteDatabase db) {
		//The contact
		String sql_user_info = "CREATE TABLE IF NOT EXISTS "
						+ TableField.TABLE_USER_INFO  + "("
						+ TableField._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
						+ TableField.FIELD_FRIEND_UID + " text,"
						+ TableField.FIELD_USER_INFO + " text)"
						;
		//Friends list
		String sql_friend = "CREATE TABLE IF NOT EXISTS "
				+ TableField.TABLE_FRIEND  + "("
				+ TableField._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ TableField.FIELD_FRIEND_UID + " text,"
				+ TableField.FIELD_FRIEND_UNAME + " text,"
				+ TableField.FIELD_FRIEND_DISTANCE + " text,"
				+ TableField.FIELD_FRIEND_LOGINTIME + " long,"
				+ TableField.FIELD_FRIEND_SIGHTML + " text,"
				+ TableField.FIELD_FRIEND_AGE + " integer,"
				+ TableField.FIELD_FRIEND_GENDER + " integer,"
				+ TableField.FIELD_FRIEND_PIC + " text,"
				+ TableField.FIELD_FRIEND_THUMB + " text,"
				+ TableField.FIELD_FRIEND_NOTE + " text,"
				+ TableField.FIELD_GROUP_GID + " integer,"
				+ TableField.FIELD_RESERVED_DATA1 + " text,"
				+ TableField.FIELD_RESERVED_DATA2 + " text,"
				+ TableField.FIELD_RESERVED_DATA3 + " text,"
				+ TableField.FIELD_RESERVED_DATA4 + " text,"
				+ TableField.FIELD_RESERVED_DATA5 + " text,"
				+ TableField.FIELD_RESERVED_DATA6 + " text,"
				+ TableField.FIELD_RESERVED_DATA7 + " text,"
				+ TableField.FIELD_RESERVED_DATA8 + " text,"
				+ TableField.FIELD_RESERVED_DATA9 + " text,"
				+ TableField.FIELD_RESERVED_DATA10 + " text,"
				+ TableField.FIELD_RESERVED_DATA11 + " text,"
				+ TableField.FIELD_RESERVED_DATA12 + " text,"
				+ TableField.FIELD_RESERVED_DATA13 + " text)"
				;
//		//Friends group table
//		String sql_group = "CREATE TABLE IF NOT EXISTS "
//				+ TableField.TABLE_GROUP  + "("
//				+ TableField._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//				+ TableField.FIELD_GROUP_GID + " integer,"
//				+ TableField.FIELD_GROUP_NAME + " text,"
//				+ TableField.FIELD_RESERVED_DATA1 + " text,"
//				+ TableField.FIELD_RESERVED_DATA2 + " text,"
//				+ TableField.FIELD_RESERVED_DATA3 + " text)"
//				;
		//Chat table
		String sql_chat = "CREATE TABLE IF NOT EXISTS "
				+ TableField.TABLE_CHAT				 + "("
				+ TableField._ID					 + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ TableField.FIELD_FRIEND_UID		 + " text,"
				+ TableField.FIELD_FRIEND_UNAME		 + " text,"
				+ TableField.FIELD_FRIEND_PIC		 + " text,"
				+ TableField.FIELD_CHAT_MSGTIME		 + " long,"
				+ TableField.FIELD_FRIEND_GENDER	 + " integer,"
				+ TableField.FIELD_CHAT_TYPE		 + " integer,"
				+ TableField.FIELD_CHAT_BODY		 + " text,"
				+ TableField.FIELD_CHAT_ID			 + " text,"
				+ TableField.FIELD_CHAT_UNREAD		 + " integer,"
				+ TableField.FIELD_CHAT_ISSEND		 + " integer,"
				+ TableField.FIELD_CHAT_COVER		 + " text,"
				+ TableField.FIELD_CHAT_SECOND		 + " text,"
				+ TableField.FIELD_CHAT_LON			 + " text,"
				+ TableField.FIELD_CHAT_LAT			 + " text,"
				+ TableField.FIELD_CHAT_THIRDNAME	 + " text,"
				+ TableField.FIELD_CHAT_THIRDIMAGE	 + " text,"
				+ TableField.FIELD_CHAT_THIRDID	 	 + " text,"
				+ TableField.FIELD_CHAT_SHOPADDRESS	 + " text,"
				+ TableField.FIELD_CHAT_CARDSIGN	 + " text,"
				+ TableField.FIELD_CHAT_LOCALURL	 + " text,"
				+ TableField.FIELD_CHAT_SHOWTIME	 + " integer,"
				+ TableField.FIELD_RESERVED_DATA1	 + " text,"
				+ TableField.FIELD_RESERVED_DATA2	 + " text,"
				+ TableField.FIELD_RESERVED_DATA3	 + " text,"
				+ TableField.FIELD_RESERVED_DATA4	 + " text,"
				+ TableField.FIELD_RESERVED_DATA5	 + " text,"
				+ TableField.FIELD_RESERVED_DATA6	 + " text,"
				+ TableField.FIELD_RESERVED_DATA7	 + " text,"
				+ TableField.FIELD_RESERVED_DATA8	 + " text,"
				+ TableField.FIELD_RESERVED_DATA9	 + " text,"
				+ TableField.FIELD_RESERVED_DATA10	 + " text,"
				+ TableField.FIELD_RESERVED_DATA11	 + " text,"
				+ TableField.FIELD_RESERVED_DATA12	 + " text,"
				+ TableField.FIELD_RESERVED_DATA13	 + " text,"
				+ TableField.FIELD_RESERVED_DATA14	 + " text,"
				+ TableField.FIELD_RESERVED_DATA15	 + " text,"
				+ TableField.FIELD_RESERVED_DATA16	 + " text,"
				+ TableField.FIELD_RESERVED_DATA17	 + " text,"
				+ TableField.FIELD_CHAT_GROUP_IMAGE	 + " text,"
				+ TableField.FIELD_CHAT_OBJECT	 	 + " text)"
				;
		//Friend recommended table
		String sql_friends_recomment = "CREATE TABLE IF NOT EXISTS "
				+ TableField.TABLE_FRIENDS_RECOMMENT + "("
				+ TableField._ID					 + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ TableField._MSGID					 + " text,"
				+ TableField.FIELD_CHAT_THIRDID	 	 + " text,"
				+ TableField.FIELD_CHAT_THIRDNAME	 + " text,"
				+ TableField.FIELD_FRIEND_UID		 + " text,"
				+ TableField.FIELD_FRIEND_UNAME		 + " text,"
				+ TableField.FIELD_FRIEND_PIC		 + " text,"
				+ TableField.FIELD_FRIEND_THUMB		 + " text,"
				+ TableField.FIELD_CHAT_AGREE		 + " integer,"
				+ TableField.FIELD_CHAT_MSGTIME		 + " long,"
				+ TableField.FIELD_CHAT_UNREAD		 + " integer,"
				+ TableField.FIELD_CHAT_TYPE		 + " integer,"
				
				+ TableField.FIELD_RESERVED_DATA1 	 + " text,"
				+ TableField.FIELD_RESERVED_DATA2	 + " text,"
				+ TableField.FIELD_RESERVED_DATA3	 + " text,"
				+ TableField.FIELD_RESERVED_DATA4	 + " text,"
				+ TableField.FIELD_RESERVED_DATA5	 + " text,"
				+ TableField.FIELD_RESERVED_DATA6	 + " text,"
				+ TableField.FIELD_RESERVED_DATA7	 + " text,"
				+ TableField.FIELD_RESERVED_DATA8	 + " text,"
				+ TableField.FIELD_RESERVED_DATA9 	 + " text,"
				+ TableField.FIELD_RESERVED_DATA10   + " text,"
				+ TableField.FIELD_RESERVED_DATA11	 + " text)"
				;
		//My information table
		String sql_chat_event = "CREATE TABLE IF NOT EXISTS "
				+ TableField.TABLE_CHAT_EVENT		 + "("
				+ TableField._ID					 + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ TableField.FIELD_FRIEND_UID		 + " text,"
				+ TableField.FIELD_FRIEND_UNAME		 + " text,"
				+ TableField.FIELD_FRIEND_PIC		 + " text,"
				+ TableField.FIELD_CHAT_MSGTIME		 + " long,"
				+ TableField.FIELD_FRIEND_GENDER	 + " integer,"
				+ TableField.FIELD_CHAT_TYPE		 + " integer,"
				+ TableField.FIELD_CHAT_BODY		 + " text,"
				+ TableField.FIELD_CHAT_ID			 + " text,"
				+ TableField.FIELD_CHAT_UNREAD		 + " integer,"
				+ TableField.FIELD_CHAT_SYSTEM		 + " integer,"
				+ TableField.FIELD_CHAT_HIDDEN		 + " integer,"
				+ TableField.FIELD_CHAT_AGREE		 + " integer,"
				+ TableField.FIELD_CHAT_COVER		 + " text,"
				+ TableField.FIELD_CHAT_SECOND		 + " text,"
				+ TableField.FIELD_CHAT_LON			 + " text,"
				+ TableField.FIELD_CHAT_LAT			 + " text,"
				+ TableField.FIELD_CHAT_THIRDNAME	 + " text,"
				+ TableField.FIELD_CHAT_THIRDIMAGE	 + " text,"
				+ TableField.FIELD_CHAT_THIRDID	 	 + " text,"
				+ TableField.FIELD_CHAT_SHOPADDRESS	 + " text,"
				+ TableField.FIELD_CHAT_CARDSIGN	 + " text,"
				+ TableField.FIELD_CHAT_CREATEID	 + " text,"
				+ TableField.FIELD_CHAT_CREATENAME	 + " text,"
				+ TableField.FIELD_CHAT_CREATEIMAGE	 + " text,"
				+ TableField.FIELD_CHAT_CREATEAGE	 + " text,"
				+ TableField.FIELD_CHAT_CREATEGENDER + " text,"
				+ TableField.FIELD_CHAT_CREATEGSIGN  + " text,"
				+ TableField.FIELD_CHAT_SORT  		 + " text,"
				+ TableField.FIELD_CHAT_INVITEMSG  	 + " text,"
				+ TableField.FIELD_CHAT_GUEST	  	 + " text,"
				+ TableField.FIELD_CHAT_CREATETIME 	 + " text,"
				+ TableField.FIELD_CHAT_INVITEID	 + " text,"
				
				+ TableField.FIELD_CHAT_KICKGROUP	 + " integer,"
				+ TableField.FIELD_CHAT_DISMISSGROUP + " integer,"
				
				+ TableField.FIELD_RESERVED_DATA1 	 + " text,"
				+ TableField.FIELD_RESERVED_DATA2	 + " text,"
				+ TableField.FIELD_RESERVED_DATA3	 + " text,"
				+ TableField.FIELD_RESERVED_DATA4	 + " text,"
				+ TableField.FIELD_RESERVED_DATA5	 + " text,"
				+ TableField.FIELD_RESERVED_DATA6	 + " text,"
				+ TableField.FIELD_RESERVED_DATA7	 + " text,"
				+ TableField.FIELD_RESERVED_DATA8	 + " text,"
				+ TableField.FIELD_RESERVED_DATA9 	 + " text,"
				+ TableField.FIELD_RESERVED_DATA10   + " text,"
				+ TableField.FIELD_RESERVED_DATA11	 + " text,"
				+ TableField.FIELD_RESERVED_DATA12   + " text,"
				+ TableField.FIELD_RESERVED_DATA13	 + " text,"
				+ TableField.FIELD_RESERVED_DATA14	 + " integer,"
				+ TableField.FIELD_RESERVED_DATA15	 + " text,"
				+ TableField.FIELD_RESERVED_DATA16	 + " text,"
				+ TableField.FIELD_RESERVED_DATA17	 + " text,"
				+ TableField.FIELD_RESERVED_DATA18	 + " text,"
				+ TableField.FIELD_RESERVED_DATA19	 + " integer,"
				+ TableField.FIELD_RESERVED_DATA20	 + " text,"
				+ TableField.FIELD_CHAT_GROUP_IMAGE	 + " text,"
				+ TableField.FIELD_CHAT_OBJECT	 	 + " text)"
				;
		
		//The dynamic new message
		String sql_dynamic_notif = "CREATE TABLE IF NOT EXISTS "
				+ TableField.TABLE_DYNAMIC_NOTIF	 + "("
				+ TableField._ID					 + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ TableField.FIELD_FRIEND_UID		 + " text,"
				+ TableField.FIELD_FRIEND_UNAME		 + " text,"
				+ TableField.FIELD_FRIEND_PIC		 + " text,"
				+ TableField.FIELD_FRIEND_GENDER	 + " integer,"
				+ TableField.FIELD_CHAT_TYPE		 + " integer,"
				+ TableField.FIELD_CHAT_BODY		 + " text,"
				+ TableField.FIELD_DYNAMIC_MSGTYPE	 + " integer,"
				+ TableField.FIELD_DYNAMIC_ID		 + " integer,"
				+ TableField.FIELD_DYNAMIC_COMMENT	 + " text,"
				+ TableField.FIELD_DYNAMIC_IMAGE	 + " text,"
				+ TableField.FIELD_CHAT_MSGTIME		 + " long,"
				+ TableField._MSGID					 + " text,"
				+ TableField.FIELD_CHAT_UNREAD		 + " integer,"
				
				+ TableField.FIELD_RESERVED_DATA1 	 + " text,"
				+ TableField.FIELD_RESERVED_DATA2	 + " text,"
				+ TableField.FIELD_RESERVED_DATA3	 + " text,"
				+ TableField.FIELD_RESERVED_DATA4	 + " text,"
				+ TableField.FIELD_RESERVED_DATA5	 + " text,"
				+ TableField.FIELD_RESERVED_DATA6	 + " text,"
				+ TableField.FIELD_RESERVED_DATA7	 + " text,"
				+ TableField.FIELD_RESERVED_DATA8	 + " text,"
				+ TableField.FIELD_RESERVED_DATA9 	 + " text,"
				+ TableField.FIELD_RESERVED_DATA10   + " text,"
				+ TableField.FIELD_RESERVED_DATA11	 + " text)"
				;
		
		//Social circle table of contents
		String sql_dynamic_content = "CREATE TABLE IF NOT EXISTS "
				+ TableField.TABLE_DYNAMIC_CONTENT	 + "("
				+ TableField._ID					 + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ TableField.FIELD_FRIEND_UID		 + " text,"
				+ TableField.FIELD_FRIEND_UNAME		 + " text,"
				+ TableField.FIELD_FRIEND_NOTE		 + " text,"
				+ TableField.FIELD_FRIEND_GENDER	 + " integer,"
				+ TableField.FIELD_FRIEND_PIC		 + " text,"
				+ TableField.FIELD_FRIEND_THUMB		 + " text,"
				+ TableField.FIELD_FRIEND_FRIEND_LOG + " integer,"
				+ TableField.FIELD_FRIEND_RELATION	 + " text,"
				+ TableField.FIELD_FRIEND_RELATION_ID+ " text,"
				+ TableField.FIELD_DYNAMIC_ID		 + " integer,"
				+ TableField.FIELD_CHAT_MSGTIME		 + " long,"
				+ TableField.FIELD_CHAT_BODY		 + " text,"
				+ TableField.FIELD_DYNAMIC_REPLYNUM	 + " integer,"
				+ TableField.FIELD_DYNAMIC_PRAISE	 + " text,"
				+ TableField.FIELD_DYNAMIC_PRAISENUM + " integer,"
				+ TableField.FIELD_DYNAMIC_FAV		 + " text,"
				+ TableField.FIELD_DYNAMIC_IMAGES	 + " text,"//图片json
				
				+ TableField.FIELD_RESERVED_DATA1 	 + " text,"
				+ TableField.FIELD_RESERVED_DATA2	 + " text,"
				+ TableField.FIELD_RESERVED_DATA3	 + " text,"
				+ TableField.FIELD_RESERVED_DATA4	 + " text,"
				+ TableField.FIELD_RESERVED_DATA5	 + " text,"
				+ TableField.FIELD_RESERVED_DATA6	 + " text,"
				+ TableField.FIELD_RESERVED_DATA7	 + " text,"
				+ TableField.FIELD_RESERVED_DATA8	 + " text,"
				+ TableField.FIELD_RESERVED_DATA9 	 + " text,"
				+ TableField.FIELD_RESERVED_DATA10   + " text,"
				+ TableField.FIELD_RESERVED_DATA11   + " text,"
				+ TableField.FIELD_RESERVED_DATA12	 + " text)"
				;
		
		//Social comments table
		String sql_dynamic_comment = "CREATE TABLE IF NOT EXISTS "
				+ TableField.TABLE_DYNAMIC_COMMENT	 + "("
				+ TableField._ID					 + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ TableField.FIELD_DYNAMIC_ID		 + " integer,"
				+ TableField.FIELD_DYNAMIC_COMMENT_ID+ " integer,"
				+ TableField.FIELD_FRIEND_UID		 + " text,"
				+ TableField.FIELD_FRIEND_UNAME		 + " text,"
				+ TableField.FIELD_FRIEND_NOTE		 + " text,"
				+ TableField.FIELD_FRIEND_GENDER	 + " integer,"
				+ TableField.FIELD_FRIEND_PIC		 + " text,"
				+ TableField.FIELD_FRIEND_THUMB		 + " text,"
				+ TableField.FIELD_CHAT_BODY		 + " text,"
				+ TableField.FIELD_CHAT_MSGTIME		 + " long,"
				+ TableField.FIELD_FRIEND_FRIEND_LOG + " integer,"
				+ TableField.FIELD_FRIEND_RELATION	 + " text,"
				+ TableField.FIELD_DYNAMIC_REPLY	 + " text,"//Information was man json
				
				+ TableField.FIELD_RESERVED_DATA1 	 + " text,"//Comments, did not send success
				+ TableField.FIELD_RESERVED_DATA2	 + " text,"
				+ TableField.FIELD_RESERVED_DATA3	 + " text,"
				+ TableField.FIELD_RESERVED_DATA4	 + " text,"
				+ TableField.FIELD_RESERVED_DATA5	 + " text,"
				+ TableField.FIELD_RESERVED_DATA6	 + " text,"
				+ TableField.FIELD_RESERVED_DATA7	 + " text,"
				+ TableField.FIELD_RESERVED_DATA8	 + " text,"
				+ TableField.FIELD_RESERVED_DATA9 	 + " text,"
				+ TableField.FIELD_RESERVED_DATA10   + " text,"
				+ TableField.FIELD_RESERVED_DATA11	 + " text)"
				;
		
		//Social access tables
		String sql_dynamic_praise = "CREATE TABLE IF NOT EXISTS "
				+ TableField.TABLE_DYNAMIC_PRAISE	 + "("
				+ TableField._ID					 + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ TableField.FIELD_DYNAMIC_ID		 + " integer,"
				+ TableField.FIELD_FRIEND_UID		 + " text,"
				+ TableField.FIELD_FRIEND_UNAME		 + " text,"
				+ TableField.FIELD_FRIEND_NOTE		 + " text,"
				+ TableField.FIELD_FRIEND_GENDER	 + " integer,"
				+ TableField.FIELD_FRIEND_PIC		 + " text,"
				+ TableField.FIELD_FRIEND_THUMB		 + " text,"
				+ TableField.FIELD_CHAT_MSGTIME		 + " long,"
				+ TableField.FIELD_FRIEND_FRIEND_LOG + " integer,"
				+ TableField.FIELD_FRIEND_RELATION	 + " text,"
				
				+ TableField.FIELD_RESERVED_DATA1 	 + " text,"
				+ TableField.FIELD_RESERVED_DATA2	 + " text,"
				+ TableField.FIELD_RESERVED_DATA3	 + " text,"
				+ TableField.FIELD_RESERVED_DATA4	 + " text,"
				+ TableField.FIELD_RESERVED_DATA5	 + " text,"
				+ TableField.FIELD_RESERVED_DATA6	 + " text,"
				+ TableField.FIELD_RESERVED_DATA7	 + " text,"
				+ TableField.FIELD_RESERVED_DATA8	 + " text,"
				+ TableField.FIELD_RESERVED_DATA9 	 + " text,"
				+ TableField.FIELD_RESERVED_DATA10   + " text,"
				+ TableField.FIELD_RESERVED_DATA11	 + " text)"
				;
		
		//The address book table
		String sql_contact = "CREATE TABLE IF NOT EXISTS "
				+ TableField.TABLE_CONTACT			 + "("
				+ TableField._ID					 + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ TableField.FIELD_CHAT_THIRDID		 + " text,"
				+ TableField.FIELD_FRIEND_UNAME		 + " text,"
				+ TableField.FIELD_FRIEND_NOTE		 + " text,"
				+ TableField.FIELD_CHAT_TYPE		 + " integer,"
				+ TableField.FIELD_FRIEND_UID		 + " integer,"
				+ TableField.FIELD_FRIEND_RELATION	 + " integer,"
				
				+ TableField.FIELD_RESERVED_DATA1 	 + " text,"
				+ TableField.FIELD_RESERVED_DATA2	 + " text,"
				+ TableField.FIELD_RESERVED_DATA3	 + " text,"
				+ TableField.FIELD_RESERVED_DATA4	 + " text,"
				+ TableField.FIELD_RESERVED_DATA5	 + " text,"
				+ TableField.FIELD_RESERVED_DATA6	 + " text,"
				+ TableField.FIELD_RESERVED_DATA7	 + " text,"
				+ TableField.FIELD_RESERVED_DATA8	 + " text,"
				+ TableField.FIELD_RESERVED_DATA9 	 + " text,"
				+ TableField.FIELD_RESERVED_DATA10   + " text,"
				+ TableField.FIELD_RESERVED_DATA11	 + " text)"
				;

		//Collect the GIF table
		String sql_giffav = "CREATE TABLE IF NOT EXISTS "
				+ TableField.TABLE_GIF_FAV			 + "("
				+ TableField._ID					 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ TableField.FIELD_GIF_ID		     + " text,"
				+ TableField.FIELD_CHAT_MSGTIME		 + " long,"
				+ TableField.FIELD_RESERVED_DATA1	 + " text,"
				+ TableField.FIELD_RESERVED_DATA2	 + " text)"
				;

		//Group of file upload table
		String sql_groupupload = "CREATE TABLE IF NOT EXISTS "
				+ TableField.TABLE_GROUP_UPLOAD_FILE	+ "("
				+ TableField._ID					 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ TableField.FIELD_GROUP_GID		 + " text,"
				+ TableField.FIELD_CHAT_MSGTIME		 + " long,"
				+ TableField.FIELD_RESERVED_DATA1	 + " text,"
		        + TableField.FIELD_RESERVED_DATA2	 + " text,"
				+ TableField.FIELD_RESERVED_DATA3	 + " integer)"
				;

		
		db.execSQL(sql_user_info);
//		db.execSQL(sql_group);
		db.execSQL(sql_friend);
		db.execSQL(sql_chat);
		db.execSQL(sql_chat_event);
		db.execSQL(sql_dynamic_notif);
		db.execSQL(sql_dynamic_content);
		db.execSQL(sql_dynamic_comment);
		db.execSQL(sql_dynamic_praise);
		db.execSQL(sql_friends_recomment);
		db.execSQL(sql_contact);
		db.execSQL(sql_giffav);
		db.execSQL(sql_groupupload);
	}
}
