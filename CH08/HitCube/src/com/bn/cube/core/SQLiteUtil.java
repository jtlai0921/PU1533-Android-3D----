package com.bn.cube.core;
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
	    			"/data/data/com.bn.cube.view/mydb", //�ثe�M�ε{���u��b�ۤv���]�U�إ߸�Ʈw
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
  //��s�O������k
    public static void update(int level)
    {
    	createOrOpenDatabase();//�}�Ҹ�Ʈw
    	String sql_update="update levels set level="+level+";";
    	try
    	{
        	sld.execSQL(sql_update);
    	}
		catch(Exception e)
		{
            e.printStackTrace();
		}
		closeDatabase();//������Ʈw
    }
    //���J�O������k
    public static void insert(int level)
    {
    	createOrOpenDatabase();//�}�Ҹ�Ʈw
    	String sql_insert="insert into levels values("+level+");";
    	try
    	{
        	sld.execSQL(sql_insert);
    	}  
		catch(Exception e)
		{
            e.printStackTrace();
		}
		closeDatabase();//������Ʈw
    }
    
    public static int query()
    {
    	createOrOpenDatabase();//�}�Ҹ�Ʈw
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
    	closeDatabase();//������Ʈw
    	return result;
    }
  //�إ߸�Ʈw
    public  static void initDatabase(){
    	//�إߪ�
    	int num=0;
    	String sql="create table if not exists levels(level int(4));";  
    	createTable(sql);
    	createOrOpenDatabase();//�}�Ҹ�Ʈw
    	try{
    	Cursor cur=sld.rawQuery("select level from levels", new String[]{});
		num=cur.getCount();
		cur.close(); 
    	}catch(Exception e)
    	{
    		e.printStackTrace();  
    	}
    	closeDatabase();//������Ʈw
    	
    	if(num<1)
    	{
    		insert(1);
    	}
    }
   
}  
