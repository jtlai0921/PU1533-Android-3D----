package tstc.fxq.utils;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
public class DBUtil {
	static SQLiteDatabase sld;

    //開啟或建立資料庫的方法
    public static void openOrCreateDatabase()
    {
    	try
    	{
	    	sld=SQLiteDatabase.openDatabase
	    	(
	    			"/data/data/tstc.fxq.main/mydb", //資料庫所在路徑
	    			null, 								//CursorFactory
	    			SQLiteDatabase.OPEN_READWRITE|SQLiteDatabase.CREATE_IF_NECESSARY //讀寫、若不存在則建立
	    	);
	    	String sql="create table if not exists highScore" +
	    			"( " +
	    			"mode interger,"+//游戲玩法，0代表8球模式，1代表9球模式
	    			"score integer," +
	    			"date varchar(20)" +
	    			");";
	    	sld.execSQL(sql);
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
    //插入記錄的方法
    public static void insert(int mode, int score, String date)
    {
    	//開啟或建立資料庫
    	DBUtil.openOrCreateDatabase();
    	try
    	{
        	String sql="insert into highScore values("+mode+","+score+",'"+date+"');";
        	sld.execSQL(sql);
    	}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeDatabase();
		}
    }
    //查詢的方法
    public static String query(int mode, int posFrom, int length)//開始的位置，要查尋的記錄條數
    {
    	StringBuilder sb=new StringBuilder();//要傳回的結果
    	Cursor cur=null;
    	openOrCreateDatabase();
        String sql="select score,date from highScore " +
        		"where mode = " +mode+" "+
        		"order by score desc;";
        cur=sld.rawQuery(sql, null);
    	try
    	{    		
        	cur.moveToPosition(posFrom);//將游標搬移到特殊的開始位置
        	int count=0;//目前查詢記錄條數
        	while(cur.moveToNext()&&count<length)
        	{
        		int score=cur.getInt(0);
        		String date=cur.getString(1);        		
        		sb.append(score);
        		sb.append("/");
        		sb.append(date);
        		sb.append("/");//將記錄用"/"分隔開
        		count++;
        	}        			
    	}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			cur.close();
			closeDatabase();
		}
		//轉換成字元，並傳回
		return sb.toString();
    }
    //得到資料庫中記錄條數的方法
    public static int getRowCount(int mode)
    {
    	int result=0;
    	Cursor cur=null;
    	openOrCreateDatabase();
    	try
    	{
    		String sql="select count(score) from highScore " +
    		"where mode = "+mode+";";
            cur=sld.rawQuery(sql, null);
        	if(cur.moveToNext())
        	{
        		result=cur.getInt(0);
        	}
    	}
    	catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			cur.close();
			closeDatabase();
		}
        
    	return result;
    }
    //取得最高分的方法
    public static int getHighestScore(int mode)
    {
    	int result = 0;
    	Cursor cur=null;
    	openOrCreateDatabase();//開啟或建立資料庫
    	try{
    		//從資料庫中選出最高分
        	String sql="select max(score) from highScore "+
        		"where mode = "+mode+";";;   	
        	cur=sld.rawQuery(sql, null);
        	if(cur.moveToNext())//若果結果集不為空，移到下一行
        	{
        		result = cur.getInt(0);
        	}   	
    	}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			cur.close();
			closeDatabase();
		}
		return result;    	
    }
}
