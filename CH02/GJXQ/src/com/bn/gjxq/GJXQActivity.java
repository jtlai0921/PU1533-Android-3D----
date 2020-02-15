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
	
	GameSurfaceView gv;//����View�Ѧ�
	public static float screenWidthStandard=960;//�ù��e��
	public static float screenHeightStandard=540;//�ù�����
	
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
        new Thread()
        {
        	public void run()
        	{
        		synchronized(Constant.initLock)
        		{
        			//���J�Ϥ���ƶi�O����
                    PicDataManager.loadPicData(GJXQActivity.this);                    
                    //���J���鳻�I��m�B���z�y�и�ƶi�O����w�R
                    VertexDataManager.initVertexData(GJXQActivity.this.getResources());
                    //���J��������
                    SoundUtil.initSounds(GJXQActivity.this);
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
				//��ܫ��w���e��Toast
				case COMMAND_TOAST_MSG:
					Bundle b=msg.getData();
					String msgStr=b.getString("msg");
					Toast.makeText(GJXQActivity.this, msgStr, Toast.LENGTH_SHORT).show();
				break;		
				//��ܥ�͵���
				case COMMAND_DIALOG_MSG:
					Bundle bundle=msg.getData();
					String msgStrl=bundle.getString("msg");
					showdialog(msgStrl);
					break;
			}
		}
	};
    
	//���w��ɭ��h����k
    public void gotoWelcomeView()  
    {
    	WelcomeView wv=new WelcomeView(this);    	
        setContentView(wv);
    }
    
    //����ɭ�����k
    public void gotoMenuView()
    {    	
    	MenuView mv=new MenuView(this);
    	setContentView(mv);
    	currView=WhichView.MENU_VIEW;//��s�ثeView����View
    }
    
    //��]�w�ɭ�����k
    public void gotoSetView()
    {
    	SetView sv=new SetView(this);
    	setContentView(sv);
    	currView=WhichView.SET_VIEW;//��s�ثeView��]�wView
    }
    
    //������ɭ��h����k
    public void gotoGameView()
    {
    	synchronized(Constant.initLock)
    	{
    		gv=new GameSurfaceView(this);
    		setContentView(gv);        
            gv.requestFocus();//���o�J�I
            gv.setFocusableInTouchMode(true);//�]�w���iĲ��  
            currView=WhichView.GAME_VIEW;//��s�ثeView�����View
    	}
    }
    
    //������ɭ��h����k
    public void gotoAboutVeiw()
    {
    	AboutView av=new AboutView(this);
    	this.setContentView(av);
    	currView=WhichView.ABOUT_VIEW;//��s�ثeView������View
    }
    //�����U�ɭ��h����k
    public void gotoHelpView()
    {
    	HelpView hv=new HelpView(this);
    	this.setContentView(hv);
    	currView=WhichView.HELP_VIEW;//��s�ثeView�����UView
    }
    
    //��ܿ�Ĺ����͵���
    public void showdialog(String msg)
    {
    	AlertDialog dialog = new AlertDialog.Builder(this)
		.setMessage(msg)
		.setPositiveButton("�T�w", new OnClickListener() 
    	  {
	    	   @Override
	    	   public void onClick(DialogInterface dialog, int which) 
	    	   {
		    	    dialog.dismiss();//��͵�������
		    	    gotoMenuView();//���樺�ɭ�
	    	   }
    	  }).create();  
		WindowManager.LayoutParams lp=dialog.getWindow().getAttributes();
		lp.alpha=0.8f;//�ƶV�C�V�z��
		dialog.getWindow().setAttributes(lp);
		dialog.show();
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
    	if(currView!=null)
    	{
        	switch(currView)
        	{
        	    case SET_VIEW:		//�ثe�b�]�w�ɭ�
        	    	gotoMenuView();
        	    break;
        	    case MENU_VIEW:		//�ثe�b���ɭ�
        	    	System.exit(0);
        	    break;
        	    case GAME_VIEW:		//�ثe�b�����ɭ�
        	    	backdialog();
        	    break;
        	    case ABOUT_VIEW:	//�ثe�b����ɭ�
        	    	gotoMenuView();
        	    break;
        	    case HELP_VIEW:	//�ثe�b���U�ɭ�
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