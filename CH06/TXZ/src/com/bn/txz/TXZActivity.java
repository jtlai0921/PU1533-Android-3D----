package com.bn.txz;

import static com.bn.txz.Constant.*;
import com.bn.txz.game.TXZGameSurfaceView;
import com.bn.txz.manager.PicDataManager;
import com.bn.txz.manager.SharedPreferencesUtil;
import com.bn.txz.manager.SoundUtil;
import com.bn.txz.manager.VertexDataManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class TXZActivity extends Activity {
	TXZGameSurfaceView gv;
	public SharedPreferencesUtil sharedUtil;
	
	AudioManager audio;//游戲中控制音量工具物件
	public MediaPlayer beijingyinyue; //播放器
	public  SharedPreferences sp;
	public boolean yinXiao=true;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  //設定為全螢幕顯示
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//設定為橫屏模式
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constant.screenWidth=dm.widthPixels;			//dm.widthPixels    取得螢幕水平解析度
        Constant.screenHeight=dm.heightPixels;
        Constant.ScaleSR();
        Constant.ratio=screenWidthStandard/screenHeightStandard; 
        sp= this.getSharedPreferences("sysz", Context.MODE_PRIVATE);
        yinXiao=sp.getBoolean("youxiyinxiao", true);
        sharedUtil=new SharedPreferencesUtil(TXZActivity.this);
        sharedUtil.getPassNum();//設定了起始的關卡數
        new Thread()
        {
        	public void run()
        	{
        		synchronized(Constant.initLock)
        		{
        			//載入圖片資料進記憶體
                    PicDataManager.loadPicData(TXZActivity.this);                    
                    //載入物體頂點位置、紋理座標資料進記憶體緩沖
                    VertexDataManager.initVertexData(TXZActivity.this.getResources());
                    //載入游戲音效
                    SoundUtil.initSounds(TXZActivity.this);
        		}
        	}  
        }.start();
        gotoWelcomeView();//到歡迎界面
    }
    
    
    public Handler handler = new Handler() 
    {
		@Override
		public void handleMessage(Message msg) 
		{
			super.handleMessage(msg);

			switch (msg.what) 
			{
			    //去游戲界面
				case COMMAND_GOTO_GAME_VIEW:
					gotoGameView();						
				break;
				//去選單界面
				case COMMAND_GOTO_MENU_VIEW:
					gotoMenuView();						
				break;
				//去選關界面
				case COMMAND_GOTO_GUAN_VIEW:
					gotoSelectView();
					break;
				case COMMAND_GOTO_SET_VIEW:
					gotoSetView();
					break;
			}
		}
	};
    
	//到歡迎界面去的方法
    public void gotoWelcomeView()  
    {
    	TXZWelcomeView wv=new TXZWelcomeView(this);    	
        setContentView(wv);
    }
    
    //到選單界面去的方法
    public void gotoMenuView()
    {
    	TXZMenuView mv=new TXZMenuView(this);    	
        setContentView(mv);
        currView=WhichView.MENU_VIEW;
        if(IS_BEIJINGYINYUE)
        {
        	if(beijingyinyue==null)
        	{
        		playBeiJingYinYue();
        	}
        	 
        }
    }
    
    //到游戲界面去的方法
    public void gotoGameView()
    {
    	synchronized(Constant.initLock)
    	{
    		gv=new TXZGameSurfaceView(this);
    		setContentView(gv);        
            gv.requestFocus();//取得焦點
            gv.setFocusableInTouchMode(true);//設定為可觸控  
            currView=WhichView.GAME_VIEW;//更新目前View到游戲View
            
    	}
    }
    
    //到選關界面去的方法
    public void gotoSelectView()
    {
    	TXZSelectView sv=new TXZSelectView(this);    	
        setContentView(sv);
        currView=WhichView.GUAN_VIEW;
    }
    
    //到設定界面去的方法
    public void gotoSetView()
    {
    	TXZSetView sv=new TXZSetView(this);    	
        setContentView(sv);
        currView=WhichView.SET_VIEW;
    }
    
    //到關於界面去的方法
    public void gotoAboutView()
    {
    	TXTAboutView av=new TXTAboutView(this);    	
        setContentView(av);
        currView=WhichView.ABOUT_VIEW;
    }
    
    //到幫助界面去的方法
    public void gotoHelpView()
    {
    	TXZHelpView hv=new TXZHelpView(this);
    	setContentView(hv);
    	currView=WhichView.HELP_VIEW;
    }
    
    
    //按下傳回鍵時顯示提示交談視窗的方法,確定後去往選單界面
    public void backdialog()
    {
    	AlertDialog dialog = new AlertDialog.Builder(this)
    			.setTitle("提示訊息")  
    			.setMessage("你確定要離開游戲界面嗎？")
    			.setPositiveButton("確定", new OnClickListener() 
    	    	  {
    		    	   @Override
    		    	   public void onClick(DialogInterface dialog, int which) 
    		    	   {
    			    	    dialog.dismiss();
    			    	    TXZGameSurfaceView.isSkyAngle=false;
    			    	    gotoMenuView();
    		    	   }
    	    	  })
    	    	 .setNegativeButton("取消", new OnClickListener() 
    		      {
    		    	   @Override
    		    	   public void onClick(DialogInterface dialog, int which) 
    		    	   {
    		    		   dialog.dismiss();
    		    	   }
    	    	  }).create();  
    	WindowManager.LayoutParams lp=dialog.getWindow().getAttributes();
        lp.alpha=0.8f;		//數越低越透明
        dialog.getWindow().setAttributes(lp);
    	dialog.show();
    }
    
    //監聽傳回鍵方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
    	if(keyCode!=4)                                 //若果不是傳回鍵則不做處理
    	{
    		return false;
    	}
    	if(keyCode==4)
    	{
        	if(currView!=null)
        	{
            	switch(currView)
            	{
            	    case SET_VIEW:		//目前在設定界面
            	    	SET_IS_WHILE=false;
            	    	Constant.set_flag=false;
            	    	gotoMenuView();
            	    break;
            	    case MENU_VIEW:		//目前在選單界面
            	    	MENU_IS_WHILE=false;
            	    	Constant.menu_flag=false;
            	    	stopBeiJingYinYue();
            	    	System.exit(0);
            	    break;
            	    case GAME_VIEW:		//目前在游戲界面
            	    	backdialog();
            	    break;
            	    case ABOUT_VIEW:	//目前在關於界面
            	    	Constant.about_flag=false;
            	    	gotoMenuView();
            	    break;  
            	    case HELP_VIEW:	//目前在幫助界面
            	    	gotoMenuView();
            	    break;
            	    case GUAN_VIEW:
            	    	Constant.select_flag=false;
            	    	Constant.SELECT_IS_WHILE=false;
            	    	gotoMenuView();
            	    break;
            	}    	
        	}
    	}
    	return false;
    }

    public void playBeiJingYinYue(){
    	beijingyinyue=MediaPlayer.create(this,R.raw.mainmp3);
    	beijingyinyue.setLooping(true);
    	beijingyinyue.start();
    }

    public void stopBeiJingYinYue()
    {
    	if(beijingyinyue!=null)
    	{
    		beijingyinyue.stop();
    		beijingyinyue=null;
    	}
    	SharedPreferences.Editor editor=sp.edit();
    	editor.clear();
		editor.commit();
    }
}