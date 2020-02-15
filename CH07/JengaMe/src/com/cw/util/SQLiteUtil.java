package com.cw.util;

import java.util.Date;
import java.util.Vector;
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
	    			"/data/data/com.cw.game/mydb", //目前套用程式只能在自己的包下建立資料庫
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
  //插入記錄的方法
    public static void insert(String sql)
    {
    	createOrOpenDatabase();//開啟資料庫
    	try
    	{
        	sld.execSQL(sql);
    	}
		catch(Exception e)
		{
            e.printStackTrace();
		}
		closeDatabase();//關閉資料庫
    }
    //查詢的方法
    public static Vector<Vector<String>> query(String sql)
    {
    	createOrOpenDatabase();//開啟資料庫
    	Vector<Vector<String>> vector=new Vector<Vector<String>>();//新增存放查詢結果的向量
    	try
    	{
           Cursor cur=sld.rawQuery(sql, new String[]{});
        	while(cur.moveToNext())
        	{
        		Vector<String> v=new Vector<String>();
        		int col=cur.getColumnCount();		//傳回每一行都多少字段
        		for( int i=0;i<col;i++)
				{
					v.add(cur.getString(i));					
				}				
				vector.add(v);
        	}
        	cur.close();		
    	}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		closeDatabase();//關閉資料庫
		return vector;
    }  
    
  //建立資料庫
    public  static void initDatabase(){
    	//建立表
    	String sql="create table if not exists paihangbang(grade int(4),time char(20));";
    	createTable(sql);
    }
    //插入時間的方法
    public static void insertTime(int grade)
    {
    	Date d=new Date();
        String curr_time=(d.getYear()+1900)+"-"+(d.getMonth()+1<10?"0"+
        		(d.getMonth()+1):(d.getMonth()+1))+"-"+d.getDate()+"-"+
        		d.getHours()+"-"+d.getMinutes()+"-"+d.getSeconds();
    	String sql_insert="insert into paihangbang values("+grade+","+"'"+curr_time+"');";
    	insert(sql_insert);
    }
}