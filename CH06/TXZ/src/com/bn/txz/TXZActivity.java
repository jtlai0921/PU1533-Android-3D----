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
	
	AudioManager audio;//����������q�u�㪫��
	public MediaPlayer beijingyinyue; //����
	public  SharedPreferences sp;
	public boolean yinXiao=true;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  //�]�w�����ù����
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//�]�w����̼Ҧ�
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constant.screenWidth=dm.widthPixels;			//dm.widthPixels    ���o�ù������ѪR��
        Constant.screenHeight=dm.heightPixels;
        Constant.ScaleSR();
        Constant.ratio=screenWidthStandard/screenHeightStandard; 
        sp= this.getSharedPreferences("sysz", Context.MODE_PRIVATE);
        yinXiao=sp.getBoolean("youxiyinxiao", true);
        sharedUtil=new SharedPreferencesUtil(TXZActivity.this);
        sharedUtil.getPassNum();//�]�w�F�_�l�����d��
        new Thread()
        {
        	public void run()
        	{
        		synchronized(Constant.initLock)
        		{
        			//���J�Ϥ���ƶi�O����
                    PicDataManager.loadPicData(TXZActivity.this);                    
                    //���J���鳻�I��m�B���z�y�и�ƶi�O����w�R
                    VertexDataManager.initVertexData(TXZActivity.this.getResources());
                    //���J��������
                    SoundUtil.initSounds(TXZActivity.this);
        		}
        	}  
        }.start();
        gotoWelcomeView();//���w��ɭ�
    }
    
    
    public Handler handler = new Handler() 
    {
		@Override
		public void handleMessage(Message msg) 
		{
			super.handleMessage(msg);

			switch (msg.what) 
			{
			    //�h�����ɭ�
				case COMMAND_GOTO_GAME_VIEW:
					gotoGameView();						
				break;
				//�h���ɭ�
				case COMMAND_GOTO_MENU_VIEW:
					gotoMenuView();						
				break;
				//�h�����ɭ�
				case COMMAND_GOTO_GUAN_VIEW:
					gotoSelectView();
					break;
				case COMMAND_GOTO_SET_VIEW:
					gotoSetView();
					break;
			}
		}
	};
    
	//���w��ɭ��h����k
    public void gotoWelcomeView()  
    {
    	TXZWelcomeView wv=new TXZWelcomeView(this);    	
        setContentView(wv);
    }
    
    //����ɭ��h����k
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
    
    //������ɭ��h����k
    public void gotoGameView()
    {
    	synchronized(Constant.initLock)
    	{
    		gv=new TXZGameSurfaceView(this);
    		setContentView(gv);        
            gv.requestFocus();//���o�J�I
            gv.setFocusableInTouchMode(true);//�]�w���iĲ��  
            currView=WhichView.GAME_VIEW;//��s�ثeView�����View
            
    	}
    }
    
    //������ɭ��h����k
    public void gotoSelectView()
    {
    	TXZSelectView sv=new TXZSelectView(this);    	
        setContentView(sv);
        currView=WhichView.GUAN_VIEW;
    }
    
    //��]�w�ɭ��h����k
    public void gotoSetView()
    {
    	TXZSetView sv=new TXZSetView(this);    	
        setContentView(sv);
        currView=WhichView.SET_VIEW;
    }
    
    //������ɭ��h����k
    public void gotoAboutView()
    {
    	TXTAboutView av=new TXTAboutView(this);    	
        setContentView(av);
        currView=WhichView.ABOUT_VIEW;
    }
    
    //�����U�ɭ��h����k
    public void gotoHelpView()
    {
    	TXZHelpView hv=new TXZHelpView(this);
    	setContentView(hv);
    	currView=WhichView.HELP_VIEW;
    }
    
    
    //���U�Ǧ^�����ܴ��ܥ�͵�������k,�T�w��h�����ɭ�
    public void backdialog()
    {
    	AlertDialog dialog = new AlertDialog.Builder(this)
    			.setTitle("���ܰT��")  
    			.setMessage("�A�T�w�n���}�����ɭ��ܡH")
    			.setPositiveButton("�T�w", new OnClickListener() 
    	    	  {
    		    	   @Override
    		    	   public void onClick(DialogInterface dialog, int which) 
    		    	   {
    			    	    dialog.dismiss();
    			    	    TXZGameSurfaceView.isSkyAngle=false;
    			    	    gotoMenuView();
    		    	   }
    	    	  })
    	    	 .setNegativeButton("����", new OnClickListener() 
    		      {
    		    	   @Override
    		    	   public void onClick(DialogInterface dialog, int which) 
    		    	   {
    		    		   dialog.dismiss();
    		    	   }
    	    	  }).create();  
    	WindowManager.LayoutParams lp=dialog.getWindow().getAttributes();
        lp.alpha=0.8f;		//�ƶV�C�V�z��
        dialog.getWindow().setAttributes(lp);
    	dialog.show();
    }
    
    //��ť�Ǧ^���k
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
    	if(keyCode!=4)                                 //�Y�G���O�Ǧ^��h�����B�z
    	{
    		return false;
    	}
    	if(keyCode==4)
    	{
        	if(currView!=null)
        	{
            	switch(currView)
            	{
            	    case SET_VIEW:		//�ثe�b�]�w�ɭ�
            	    	SET_IS_WHILE=false;
            	    	Constant.set_flag=false;
            	    	gotoMenuView();
            	    break;
            	    case MENU_VIEW:		//�ثe�b���ɭ�
            	    	MENU_IS_WHILE=false;
            	    	Constant.menu_flag=false;
            	    	stopBeiJingYinYue();
            	    	System.exit(0);
            	    break;
            	    case GAME_VIEW:		//�ثe�b�����ɭ�
            	    	backdialog();
            	    break;
            	    case ABOUT_VIEW:	//�ثe�b����ɭ�
            	    	Constant.about_flag=false;
            	    	gotoMenuView();
            	    break;  
            	    case HELP_VIEW:	//�ثe�b���U�ɭ�
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