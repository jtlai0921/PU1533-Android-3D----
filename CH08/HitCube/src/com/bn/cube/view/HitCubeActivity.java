package com.bn.cube.view;

import java.util.HashMap;

import com.bn.cube.core.SQLiteUtil;
import com.bn.cube.game.LoseView;
import com.bn.cube.game.MySurfaceView;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

enum WhichView
{
	MENU_VIEW,CONT_VIEW,SOUND_VIEW,MySurfaceView,LOSE_VIEW,HELP_VIEW
};
public class HitCubeActivity extends Activity {
	MyGLSurfaceView mGLSurfaceView;
	MenuView menuView;
	ContView contView;
	SoundView soundView;
	MySurfaceView mv;
	LoseView loseView;
	WhichView whichView;
	HelpView helpView;
	public Handler hd;//訊息控制器
	
	 SoundPool shengyinchi;//音效池
	 HashMap<Integer,Integer> soundId;//音效id 
	 public MediaPlayer beijingyinyue; //背景音樂播放器
	 
	 public  SharedPreferences sp;
	 public boolean yinXiao=true;
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //設定為全螢幕
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//設定為橫屏模式
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		SQLiteUtil.initDatabase();//建立資料庫
        WindowManager windowManager = getWindowManager();    //取得螢幕的解析度
		Display display = windowManager.getDefaultDisplay();    
		Constant.screenWidth = display.getWidth();    
		Constant.screenHigth = display.getHeight();		
		com.bn.cube.game.Constant.screenWidth=display.getWidth();
		com.bn.cube.game.Constant.screenHigth=display.getHeight();
		// 取得螢幕尺寸
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		 Constant.ScaleSR();
		 com.bn.cube.game.Constant.ScaleSRGame();
			sp= this.getSharedPreferences("sysz", Context.MODE_PRIVATE);
			
	//		bgSound=sp.getBoolean("beijingyinyue", true);
			yinXiao=sp.getBoolean("youxiyinxiao", true);
			chuangJianSound();
//			if(Constant.bn==0)
//			{
				playBeiJingYinYue();
//				Constant.bn=1;
//			}
	    	
		 hd=new Handler()
	        {
				@Override
				public void handleMessage(Message msg)
				{
	        		switch(msg.what)
	        		{
		        		case 0://去主選單界面
		        		{
		        		  goMenuView();
		        		  whichView=WhichView.MENU_VIEW;
		        		  break;
		        		}
		        		case 1://去Cont界面
		        		{
		        		  goContView();
		        		  whichView=WhichView.CONT_VIEW;
		        		  break;
		        		}
		        		case 2:
		        		{
		        			goSoundView();
		        			whichView=WhichView.SOUND_VIEW;
		        			break;
		        		}
		        		case 3://去主選單界面
		        		{
		        		  goMySurfaceView();
		        		  whichView=WhichView.MySurfaceView;
		        		  break;
		        		}
		        		case 4://去失敗界面
		        		{
		        		  goLoseView();
		        		  whichView=WhichView.LOSE_VIEW;
		        		  break;
		        		}
		        		case 6://去幫助界面
		        		{
			        		  goHelpView();
			        		  whichView=WhichView.HELP_VIEW;
			        		  break;
		        		}
		        		case 5://離開游戲
		        		{
		        			exitGame();
		        		    break; 
		        		}
		        		
	        		}
				}
	        };
	        hd.sendEmptyMessage(0); 
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    } 
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent e)  //按鍵監聽方法
    {
    	if(keyCode!=4)                                 //若果不是傳回鍵則不做處理
    	{
    		return false;
    	}
    	if(keyCode==4)
    	{
    		if(whichView==WhichView.MENU_VIEW)           //離開游戲
    		{
    	    	Constant.star_flag=false;
    	    	Constant.menu_flag=false;
    	    	Constant.button_flag=false;
    	    	Constant.line_frag=false;
    	    	Constant.water_flag=false;
    			exitGame();
    		}else if(whichView==WhichView.CONT_VIEW)    //離開游戲
    		{
    	    	Constant.star_flag=false;
    	    	Constant.line_frag=false;    	
    	    	Constant.contbutton_flag=false;
    			hd.sendEmptyMessage(0);
    		}else if(whichView==WhichView.SOUND_VIEW)
    		{
    	    	Constant.star_flag=false;
    	    	Constant.line_frag=false;    	
    	    	Constant.soundbutton_flag=false;
    			hd.sendEmptyMessage(0);
    		}else if(whichView==WhichView.MySurfaceView)
    		{ 
    			com.bn.cube.game.Constant.cube_flag=false;
    			com.bn.cube.game.Constant.guan_flag=false;
    			com.bn.cube.game.Constant.wall_flag=false;
    			com.bn.cube.game.Constant.pieces_flag=false;
    			com.bn.cube.game.Constant.peng_flag=false;
    			com.bn.cube.game.Constant.counter_flag=false;
    			com.bn.cube.game.Constant.pause_flag=false;
    			com.bn.cube.view.Constant.line_frag=false;
    			com.bn.cube.game.Constant.button_flag=false;
    			com.bn.cube.game.Constant.dangban_flag=false;
    			com.bn.cube.game.Constant.win_flag=false;
    			com.bn.cube.game.Constant.move_flag=false;
    			com.bn.cube.game.Constant.ball_flag=false;
    			com.bn.cube.game.Constant.AndScore_flag=false;
    			com.bn.cube.game.Constant.mySur_flag=false;
    			com.bn.cube.game.Constant.lightFlag=false;
    			com.bn.cube.game.Constant.shalouFlag=false;
    			hd.sendEmptyMessage(0);
    		}else if(whichView==WhichView.LOSE_VIEW)
    		{
    			com.bn.cube.game.Constant.star_flag=false; 
    	    	com.bn.cube.game.Constant.lightFlag=false;
    	    	com.bn.cube.game.Constant.losebutton_frag=false;
    			hd.sendEmptyMessage(0);
    		}else if(whichView==WhichView.HELP_VIEW)
    		{
    			hd.sendEmptyMessage(0);
    		}
    		
    	}
    	return true;
    }
    public void goMenuView()
    {
    	menuView=new MenuView(HitCubeActivity.this);
    	setContentView(menuView);
    	menuView.requestFocus();
    	menuView.setFocusableInTouchMode(true);  
    	Constant.maxLevel=SQLiteUtil.query();
    	if(Constant.maxLevel>6)
    	{
    		Constant.maxLevel=6;
    		Constant.boss_flag=true;
    	}
    	Constant.star_flag=false;
    	Constant.line_frag=false;    	
    	Constant.soundbutton_flag=false; 
    	Constant.contbutton_flag=false;
    	Constant.menu_flag=true;
    	
    	com.bn.cube.game.Constant.lifeNum=5;
    	com.bn.cube.game.Constant.sumBoardScore=0;
    	com.bn.cube.game.Constant.level=1;
    	com.bn.cube.game.Constant.star_flag=false;
    	com.bn.cube.game.Constant.lightFlag=false;
    	com.bn.cube.game.Constant.losebutton_frag=false;
//		loseView.setFocusableInTouchMode(true);  
		com.bn.cube.game.Constant.cube_flag=false;
		com.bn.cube.game.Constant.guan_flag=false;
		com.bn.cube.game.Constant.wall_flag=false;
		com.bn.cube.game.Constant.pieces_flag=false;
		com.bn.cube.game.Constant.peng_flag=false;
		com.bn.cube.game.Constant.counter_flag=false;
		com.bn.cube.game.Constant.pause_flag=false;
		com.bn.cube.view.Constant.line_frag=false;
		com.bn.cube.game.Constant.button_flag=false;
		com.bn.cube.game.Constant.dangban_flag=false;
		com.bn.cube.game.Constant.win_flag=false;
		com.bn.cube.game.Constant.move_flag=false;
		com.bn.cube.game.Constant.ball_flag=false;
		com.bn.cube.game.Constant.AndScore_flag=false;
		com.bn.cube.game.Constant.mySur_flag=false;
		com.bn.cube.game.Constant.lightFlag=false;
		com.bn.cube.game.Constant.shalouFlag=false;	
    }
    public void goContView()
    {
    	contView=new ContView(HitCubeActivity.this);
    	setContentView(contView);
    	contView.requestFocus();
    	contView.setFocusableInTouchMode(true); 
    	com.bn.cube.game.Constant.level=1;
    	Constant.star_flag=false;
    	Constant.menu_flag=false;
    	Constant.button_flag=false;
    	Constant.line_frag=false;
    	Constant.water_flag=false;
    }
    public void goSoundView()
    {
    	soundView=new SoundView(HitCubeActivity.this);
    	setContentView(soundView);
    	soundView.requestFocus();
    	soundView.setFocusableInTouchMode(true);  
    	Constant.star_flag=false;
    	Constant.menu_flag=false;
    	Constant.button_flag=false;
    	Constant.line_frag=false;
    	Constant.water_flag=false;
    } 
    public void goHelpView()
    {
    	helpView=new HelpView(HitCubeActivity.this);
    	setContentView(helpView);
    	Constant.star_flag=false;
    	Constant.menu_flag=false;
    	Constant.button_flag=false;
    	Constant.line_frag=false;
    	Constant.water_flag=false;
    }
    public void goMySurfaceView()
    {
    	com.bn.cube.game.Constant.scoreNumberbase=0;
    	com.bn.cube.game.Constant.shalouCount=0;
    	com.bn.cube.game.Constant.shalouAngle=0;
    	com.bn.cube.game.Constant.shalouKaiId=0;
    	com.bn.cube.game.Constant.isLoadedOk=false;
		mv=new MySurfaceView(this);
		setContentView(mv);
		mv.requestFocus();
		mv.setFocusableInTouchMode(true);  
    	Constant.star_flag=false;
    	Constant.menu_flag=false;
    	Constant.button_flag=false;
    	Constant.line_frag=false;
    	Constant.water_flag=false;
    	com.bn.cube.game.Constant.dangBanBigFlag=false;
    	com.bn.cube.game.Constant.threadTime=15;
    	
    	com.bn.cube.game.Constant.ball_X=0;
    	com.bn.cube.game.Constant.ball_Y=0;
    	com.bn.cube.game.Constant.ball_Z=-10;
    	
    	com.bn.cube.game.Constant.i=0;
    	com.bn.cube.game.Constant.j=0;
    	com.bn.cube.game.Constant.k=-0.5f;
    	
		com.bn.cube.game.Constant.cube_flag=false;
		com.bn.cube.game.Constant.guan_flag=false;
		com.bn.cube.game.Constant.wall_flag=false;
		com.bn.cube.game.Constant.pieces_flag=false;
		com.bn.cube.game.Constant.peng_flag=false;
		com.bn.cube.game.Constant.counter_flag=false;
		com.bn.cube.game.Constant.pause_flag=false;
		com.bn.cube.view.Constant.line_frag=false;
		com.bn.cube.game.Constant.button_flag=false;
		com.bn.cube.game.Constant.dangban_flag=false;
		com.bn.cube.game.Constant.win_flag=false;
		com.bn.cube.game.Constant.move_flag=false;
		com.bn.cube.game.Constant.ball_flag=false;
		com.bn.cube.game.Constant.AndScore_flag=false;
		com.bn.cube.game.Constant.mySur_flag=false;
		com.bn.cube.game.Constant.lightFlag=false;
		com.bn.cube.game.Constant.shalouFlag=false;    	
    	com.bn.cube.game.Constant.mySur_flag=true; 
    	yinXiao=sp.getBoolean("youxiyinxiao", true);
//    	if(bgSound)
//    	{
//    		playBeiJingYinYue();  
//    	}
    }
    public void goLoseView()
    {
		loseView=new LoseView(this);
		setContentView(loseView);
		loseView.requestFocus();
		loseView.setFocusableInTouchMode(true);  
//    	if(beijingyinyue!=null)
//    	{
//    	beijingyinyue.stop();
//    	beijingyinyue=null;
//    	}
    	com.bn.cube.game.Constant.lifeNum=5;
    	com.bn.cube.game.Constant.sumBoardScore=0;
		com.bn.cube.game.Constant.cube_flag=false;
		com.bn.cube.game.Constant.guan_flag=false;
		com.bn.cube.game.Constant.wall_flag=false;
		com.bn.cube.game.Constant.pieces_flag=false;
		com.bn.cube.game.Constant.peng_flag=false;
		com.bn.cube.game.Constant.counter_flag=false;
		com.bn.cube.game.Constant.pause_flag=false;
		com.bn.cube.view.Constant.line_frag=false;
		com.bn.cube.game.Constant.button_flag=false;
		com.bn.cube.game.Constant.dangban_flag=false;
		com.bn.cube.game.Constant.win_flag=false;
		com.bn.cube.game.Constant.move_flag=false;
		com.bn.cube.game.Constant.ball_flag=false;
		com.bn.cube.game.Constant.AndScore_flag=false;
		com.bn.cube.game.Constant.mySur_flag=false;
		com.bn.cube.game.Constant.lightFlag=false;
		com.bn.cube.game.Constant.shalouFlag=false;			
    }
    public void exitGame(){
    	if(beijingyinyue!=null)
    	{
    	beijingyinyue.stop();
    	beijingyinyue=null;
    	}
    	SharedPreferences.Editor editor=sp.edit();
    	editor.clear();
		editor.commit();
    	menuView.flag=false;
    	System.exit(0);
    }
    public void chuangJianSound()   //建立音效池的方法
    {
    	shengyinchi=new SoundPool(   //建立音效池
    			6,					 //同時播放流的最大數量
    			AudioManager.STREAM_MUSIC,  //流的型態
        		100
    			);
    	soundId=new HashMap<Integer,Integer>();//建立hashmap
    	soundId.put(1, shengyinchi.load(this, R.raw.pengzhuang, 1));//載入音效並新增到hashmap中
    	soundId.put(2, shengyinchi.load(this, R.raw.zhuangsui, 1));
    	soundId.put(3, shengyinchi.load(this, R.raw.daoju, 1));
    	soundId.put(4, shengyinchi.load(this, R.raw.shengli, 1));                                     
    	soundId.put(5, shengyinchi.load(this, R.raw.game_lose, 1));
    }
    public void playSound(int sound,int loop)//播放音效的方法
    {
    	   
    		AudioManager amg=(AudioManager) this.getSystemService(Context.AUDIO_SERVICE);//得到AudioManager案例物件
        	float streamVolumeCurrent=amg.getStreamVolume(AudioManager.STREAM_MUSIC);//獲得目前音量
        	float streamVolumeMax=amg.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//獲得最大音量
        	float volume=streamVolumeCurrent/streamVolumeMax;
        	shengyinchi.play(soundId.get(sound), volume, volume, 1, loop, 1f);//播放音效    	    	
    }
    public void playBeiJingYinYue(){
    	
    	beijingyinyue=MediaPlayer.create(this,R.raw.backsound);
    	beijingyinyue.setLooping(true);
    	beijingyinyue.start();
    }
}