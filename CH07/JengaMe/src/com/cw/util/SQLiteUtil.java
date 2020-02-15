package com.cw.util;

import java.util.Date;
import java.util.Vector;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteUtil 
{
	static SQLiteDatabase sld;
	//�إߩζ}�Ҹ�Ʈw����k
    public static void createOrOpenDatabase()
    {
    	try
    	{
	    	sld=SQLiteDatabase.openDatabase
	    	(
	    			"/data/data/com.cw.game/mydb", //�ثe�M�ε{���u��b�ۤv���]�U�إ߸�Ʈw
	    			null, 								//CursorFactory
	    			SQLiteDatabase.OPEN_READWRITE|SQLiteDatabase.CREATE_IF_NECESSARY //Ū�g�B�Y���s�b�h�إ�
	    	);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
  //������Ʈw����k
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
    //�ت�
    public static void createTable(String sql)
    {
    	createOrOpenDatabase();//�}�Ҹ�Ʈw
    	try
    	{
        	sld.execSQL(sql);//�ت�
    	}
		catch(Exception e)
		{
            e.printStackTrace();
		}
    	closeDatabase();//������Ʈw
    }
  //���J�O������k
    public static void insert(String sql)
    {
    	createOrOpenDatabase();//�}�Ҹ�Ʈw
    	try
    	{
        	sld.execSQL(sql);
    	}
		catch(Exception e)
		{
            e.printStackTrace();
		}
		closeDatabase();//������Ʈw
    }
    //�d�ߪ���k
    public static Vector<Vector<String>> query(String sql)
    {
    	createOrOpenDatabase();//�}�Ҹ�Ʈw
    	Vector<Vector<String>> vector=new Vector<Vector<String>>();//�s�W�s��d�ߵ��G���V�q
    	try
    	{
           Cursor cur=sld.rawQuery(sql, new String[]{});
        	while(cur.moveToNext())
        	{
        		Vector<String> v=new Vector<String>();
        		int col=cur.getColumnCount();		//�Ǧ^�C�@�泣�h�֦r�q
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
		closeDatabase();//������Ʈw
		return vector;
    }  
    
  //�إ߸�Ʈw
    public  static void initDatabase(){
    	//�إߪ�
    	String sql="create table if not exists paihangbang(grade int(4),time char(20));";
    	createTable(sql);
    }
    //���J�ɶ�����k
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