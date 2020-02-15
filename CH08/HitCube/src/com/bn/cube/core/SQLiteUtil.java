package com.bn.cube.core;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteUtil 
{
	static SQLiteDatabase sld;
	//建立或開啟資料庫的方法   
    public static void createOrOpenDatabase()
    {
    	try
    	{
	    	sld=SQLiteDatabase.openDatabase
	    	(
	    			"/data/data/com.bn.cube.view/mydb", //目前套用程式只能在自己的包下建立資料庫
	    			null, 								//CursorFactory
	    			SQLiteDatabase.OPEN_READWRITE|SQLiteDatabase.CREATE_IF_NECESSARY //讀寫、若不存在則建立
	    	);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
  //關閉資料庫的方法
    public static void closeDatabase()
    {
    	try
    	{
	    	sld.close();    
    	}
		catch(Exception e)
		{
            e.printStackTrace();
		}
    }
    //建表
    public static void createTable(String sql)
    {
    	createOrOpenDatabase();//開啟資料庫
    	try
    	{
        	sld.execSQL(sql);//建表
    	}
		catch(Exception e)
		{
            e.printStackTrace();
		}
    	closeDatabase();//關閉資料庫
    }
  //更新記錄的方法
    public static void update(int level)
    {
    	createOrOpenDatabase();//開啟資料庫
    	String sql_update="update levels set level="+level+";";
    	try
    	{
        	sld.execSQL(sql_update);
    	}
		catch(Exception e)
		{
            e.printStackTrace();
		}
		closeDatabase();//關閉資料庫
    }
    //插入記錄的方法
    public static void insert(int level)
    {
    	createOrOpenDatabase();//開啟資料庫
    	String sql_insert="insert into levels values("+level+");";
    	try
    	{
        	sld.execSQL(sql_insert);
    	}  
		catch(Exception e)
		{
            e.printStackTrace();
		}
		closeDatabase();//關閉資料庫
    }
    
    public static int query()
    {
    	createOrOpenDatabase();//開啟資料庫
    	int result=0;
    	String sql="select level from levels";
    	try
    	{ 
    		 Cursor cur=sld.rawQuery(sql, new String[]{});
    		 while(cur.moveToNext())
         	{
    			 result=cur.getInt(0);
         	}
    		cur.close(); 
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	closeDatabase();//關閉資料庫
    	return result;
    }
  //建立資料庫
    public  static void initDatabase(){
    	//建立表
    	int num=0;
    	String sql="create table if not exists levels(level int(4));";  
    	createTable(sql);
    	createOrOpenDatabase();//開啟資料庫
    	try{
    	Cursor cur=sld.rawQuery("select level from levels", new String[]{});
		num=cur.getCount();
		cur.close(); 
    	}catch(Exception e)
    	{
    		e.printStackTrace();  
    	}
    	closeDatabase();//關閉資料庫
    	
    	if(num<1)
    	{
    		insert(1);
    	}
    }
   
}  
