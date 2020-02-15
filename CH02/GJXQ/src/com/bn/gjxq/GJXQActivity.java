package com.bn.gjxq;
import com.bn.gjxq.game.GameSurfaceView;
import com.bn.gjxq.manager.PicDataManager;
import com.bn.gjxq.manager.VertexDataManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import static com.bn.gjxq.Constant.*;

public class GJXQActivity extends Activity {
	
	GameSurfaceView gv;//游戲View參考
	public static float screenWidthStandard=960;//螢幕寬度
	public static float screenHeightStandard=540;//螢幕高度
	
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
        new Thread()
        {
        	public void run()
        	{
        		synchronized(Constant.initLock)
        		{
        			//載入圖片資料進記憶體
                    PicDataManager.loadPicData(GJXQActivity.this);                    
                    //載入物體頂點位置、紋理座標資料進記憶體緩沖
                    VertexDataManager.initVertexData(GJXQActivity.this.getResources());
                    //載入游戲音效
                    SoundUtil.initSounds(GJXQActivity.this);
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
				//顯示指定內容的Toast
				case COMMAND_TOAST_MSG:
					Bundle b=msg.getData();
					String msgStr=b.getString("msg");
					Toast.makeText(GJXQActivity.this, msgStr, Toast.LENGTH_SHORT).show();
				break;		
				//顯示交談視窗
				case COMMAND_DIALOG_MSG:
					Bundle bundle=msg.getData();
					String msgStrl=bundle.getString("msg");
					showdialog(msgStrl);
					break;
			}
		}
	};
    
	//到歡迎界面去的方法
    public void gotoWelcomeView()  
    {
    	WelcomeView wv=new WelcomeView(this);    	
        setContentView(wv);
    }
    
    //到選單界面的方法
    public void gotoMenuView()
    {    	
    	MenuView mv=new MenuView(this);
    	setContentView(mv);
    	currView=WhichView.MENU_VIEW;//更新目前View到選單View
    }
    
    //到設定界面的方法
    public void gotoSetView()
    {
    	SetView sv=new SetView(this);
    	setContentView(sv);
    	currView=WhichView.SET_VIEW;//更新目前View到設定View
    }
    
    //到游戲界面去的方法
    public void gotoGameView()
    {
    	synchronized(Constant.initLock)
    	{
    		gv=new GameSurfaceView(this);
    		setContentView(gv);        
            gv.requestFocus();//取得焦點
            gv.setFocusableInTouchMode(true);//設定為可觸控  
            currView=WhichView.GAME_VIEW;//更新目前View到游戲View
    	}
    }
    
    //到關於界面去的方法
    public void gotoAboutVeiw()
    {
    	AboutView av=new AboutView(this);
    	this.setContentView(av);
    	currView=WhichView.ABOUT_VIEW;//更新目前View到關於View
    }
    //到幫助界面去的方法
    public void gotoHelpView()
    {
    	HelpView hv=new HelpView(this);
    	this.setContentView(hv);
    	currView=WhichView.HELP_VIEW;//更新目前View到幫助View
    }
    
    //顯示輸贏的交談視窗
    public void showdialog(String msg)
    {
    	AlertDialog dialog = new AlertDialog.Builder(this)
		.setMessage(msg)
		.setPositiveButton("確定", new OnClickListener() 
    	  {
	    	   @Override
	    	   public void onClick(DialogInterface dialog, int which) 
	    	   {
		    	    dialog.dismiss();//交談視窗消失
		    	    gotoMenuView();//到選單那界面
	    	   }
    	  }).create();  
		WindowManager.LayoutParams lp=dialog.getWindow().getAttributes();
		lp.alpha=0.8f;//數越低越透明
		dialog.getWindow().setAttributes(lp);
		dialog.show();
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
    	if(currView!=null)
    	{
        	switch(currView)
        	{
        	    case SET_VIEW:		//目前在設定界面
        	    	gotoMenuView();
        	    break;
        	    case MENU_VIEW:		//目前在選單界面
        	    	System.exit(0);
        	    break;
        	    case GAME_VIEW:		//目前在游戲界面
        	    	backdialog();
        	    break;
        	    case ABOUT_VIEW:	//目前在關於界面
        	    	gotoMenuView();
        	    break;
        	    case HELP_VIEW:	//目前在幫助界面
        	    	gotoMenuView();
        	    break;
        	}    	
    	}

    	if(keyCode==4)
    	{
    		return true;
    	}
    	return false;
    }
    
    
	@Override
	protected void onResume() 
	{
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
    
    
}