package com.cw.game;

import com.cw.util.SQLiteUtil;
import com.cw.util.SoundUtil;
import com.cw.view.EndMenu;
import com.cw.view.HelpView;
import com.cw.view.MenuView;
import com.cw.view.OptionMenu;
import com.cw.view.RecordView;
import com.cw.view.SplashScreenView;

import dalvik.system.VMRuntime;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class JengaMeActivity extends Activity {
	int countKey=0;
	 public boolean playBackMusic=true;
	public SoundUtil soundutil;
	public SharedPreferences sp;
	public SharedPreferences.Editor editor;//向SharedPreferences中寫回資料
	public static float screenWidth;//螢幕寬度
	public static float screenHeight;//螢幕高度
	public static float scaleratiox=1;
	public static float scaleratioY=1;
	
	  WhichView curr;
	  SplashScreenView ssv;
	  MySurfaceView msv;
      OptionMenu mov;
      EndMenu mev;
      MenuView mmv;
      RecordView mrv;
      HelpView mhv;
      
      int soundCount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉標題
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉標頭
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//強制橫屏
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth=dm.widthPixels;			//dm.widthPixels    取得螢幕水平解析度
        screenHeight=dm.heightPixels;		//dm.heightPixels	取得螢幕豎向解析度
        MenuView.scalX=dm.widthPixels;
        MenuView.scalY=dm.heightPixels;
        
        scaleratiox=screenWidth/540;
        scaleratioY=screenHeight/960;
        
        soundutil=new SoundUtil(this);
        soundutil.initSounds(); 
        sp=getSharedPreferences("com.cw.game",0);
        
        
        
        SQLiteUtil.initDatabase();
        editor=sp.edit();
        playBackMusic=sp.getBoolean("playback", false);
        new MyThread().start();
        hd.sendEmptyMessage(6);
        curr=WhichView.WELCOME_VIEW;
        VMRuntime.getRuntime().setMinimumHeapSize(6*1024*1024);
        VMRuntime.getRuntime().setTargetHeapUtilization((float) 0.75);
    }
    
     
    public class MyThread extends Thread
    {
    	
    	public void run()
    	{
    		 try{
    			Thread.sleep(6000);
    			if(playBackMusic)
    		    {
    					soundutil.play_bg_sound();  
    		    }
    		 }catch(Exception e)
    		 {}
    	}
    }
public Handler hd=new Handler()
{
	public void handleMessage(Message msg)
	{
		switch(msg.what)
		{
		case 0:
			gotoGameView();
			break;
		case 1:
			gotoMenuView();
			break;
		case 2:
			gotoOptionView();
			break;
		case 3:
			gotoEndView();
			break;
		case 4:
			gotRecordView();
			break;
		case 5:
			gotoHelpView();
			break;
		case 6:
			gotoWelcomeView();
			break;
		}
	}
};

	public void gotoWelcomeView()
	{
		ssv=new SplashScreenView(this);
		curr=WhichView.WELCOME_VIEW;
		ssv.requestFocus();
	    setContentView(ssv);
	}

	public void gotoGameView()
	{
    	msv=new MySurfaceView(this);
    	curr=WhichView.GAME_VIEW;
    	msv.requestFocus();
    	msv.backtoorigion=6;
    	MyCube.countD=0;
        setContentView(msv);
	}
	public void gotoMenuView()
	{
		curr=WhichView.MAIN_MENU_VIEW;
		mmv=new MenuView(this);
		mmv.requestFocus();
    	setContentView(mmv);
	}
	public void gotRecordView()
	{
		curr=WhichView.RECORD_VIEW;
		mrv=new RecordView(this);
		mrv.requestFocus();
    	setContentView(mrv);
	}
	
	public void gotoOptionView()
	{
		mov=new OptionMenu(this);
		curr=WhichView.OPTION_VIEW;
		mov.requestFocus();
    	setContentView(mov);
	}
	public void gotoEndView()
	{
		MyCube.turnnable=false;
		curr=WhichView.End_VIEW;
		mev=new EndMenu(this);
		mev.requestFocus();
    	setContentView(mev);
	}
	public void gotoHelpView()
	{
		curr=WhichView.HELP_VIEW;
		mhv=new HelpView(this);
		mhv.requestFocus();
    	setContentView(mhv);
	}
    @Override
    protected void onResume() {
        super.onResume();
        //mGLSurfaceView.onResume();
    }

   
    @Override
    protected void onPause() {
        super.onPause();
      //  mGLSurfaceView.onPause();
    } 
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent e)
    {
    	switch(keyCode)
    	{
    	case 4:
	    	if(curr==WhichView.GAME_VIEW)
			{
    			hd.sendEmptyMessage(1);
    			msv.simulateFlag=false;
				msv.timefalg=false;
				MyCube.flag=0;
				MyCube.turnnable=false;
				MyCube.countD=0;
				MySurfaceView.timecount=0;
				MySurfaceView.changeSelectState=true;
				MySurfaceView.checkedIndex=-1;
				MySurfaceView.index3=53;
				MySurfaceView.maxLayer=18;
				msv.Radius=Constant.EYE_Z;
				msv.backtoorigion=6;
    			MenuView.cdraw=true;;
			}
			 if(curr==WhichView.MAIN_MENU_VIEW)
			{
				if(countKey==0)
        		{
        			countKey++;
        			Toast.makeText(this, "再按一次離開游戲 ", Toast.LENGTH_LONG).show();
        			new Thread()
        			{
        				@Override 
        				public void run()
        				{
        					try
        					{
        						Thread.sleep(3000);
        						countKey=0;
        					}
        					catch(Exception e){}
        				}
        			}.start();
        		}
        		else
        		{
        			System.exit(0);
        		}
			}
			 if(curr==WhichView.OPTION_VIEW)
			{
    			hd.sendEmptyMessage(1);
    			OptionMenu.candraw=false;
    			MenuView.cdraw=true;
			}
			 if(curr==WhichView.RECORD_VIEW)
				{
	    			hd.sendEmptyMessage(1);
	    			RecordView.candraw=false;
	    			MenuView.cdraw=true;
				}
			 if(curr==WhichView.HELP_VIEW)
				{
	    			hd.sendEmptyMessage(1);
	    			HelpView.candraw=false;
	    			MenuView.cdraw=true;
				}
	    	return true;
	    	default:
	    		break;
    	}
		return super.onKeyDown(keyCode, e);
    }
}

enum WhichView{
	GAME_VIEW,
	MAIN_MENU_VIEW,
	OPTION_VIEW,
	End_VIEW,
	RECORD_VIEW,
	HELP_VIEW,
	HELP_VIEW1,
	HELP_VIEW2,
	WELCOME_VIEW
	}