package tstc.fxq.utils;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
public class DBUtil {
	static SQLiteDatabase sld;

    //�}�ҩΫإ߸�Ʈw����k
    public static void openOrCreateDatabase()
    {
    	try
    	{
	    	sld=SQLiteDatabase.openDatabase
	    	(
	    			"/data/data/tstc.fxq.main/mydb", //��Ʈw�Ҧb���|
	    			null, 								//CursorFactory
	    			SQLiteDatabase.OPEN_READWRITE|SQLiteDatabase.CREATE_IF_NECESSARY //Ū�g�B�Y���s�b�h�إ�
	    	);
	    	String sql="create table if not exists highScore" +
	    			"( " +
	    			"mode interger,"+//�������k�A0�N��8�y�Ҧ��A1�N��9�y�Ҧ�
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
    //���J�O������k
    public static void insert(int mode, int score, String date)
    {
    	//�}�ҩΫإ߸�Ʈw
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
    //�d�ߪ���k
    public static String query(int mode, int posFrom, int length)//�}�l����m�A�n�d�M���O������
    {
    	StringBuilder sb=new StringBuilder();//�n�Ǧ^�����G
    	Cursor cur=null;
    	openOrCreateDatabase();
        String sql="select score,date from highScore " +
        		"where mode = " +mode+" "+
        		"order by score desc;";
        cur=sld.rawQuery(sql, null);
    	try
    	{    		
        	cur.moveToPosition(posFrom);//�N��зh����S���}�l��m
        	int count=0;//�ثe�d�߰O������
        	while(cur.moveToNext()&&count<length)
        	{
        		int score=cur.getInt(0);
        		String date=cur.getString(1);        		
        		sb.append(score);
        		sb.append("/");
        		sb.append(date);
        		sb.append("/");//�N�O����"/"���j�}
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
		//�ഫ���r���A�öǦ^
		return sb.toString();
    }
    //�o���Ʈw���O�����ƪ���k
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
    //���o�̰�������k
    public static int getHighestScore(int mode)
    {
    	int result = 0;
    	Cursor cur=null;
    	openOrCreateDatabase();//�}�ҩΫإ߸�Ʈw
    	try{
    		//�q��Ʈw����X�̰���
        	String sql="select max(score) from highScore "+
        		"where mode = "+mode+";";;   	
        	cur=sld.rawQuery(sql, null);
        	if(cur.moveToNext())//�Y�G���G�������šA����U�@��
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
