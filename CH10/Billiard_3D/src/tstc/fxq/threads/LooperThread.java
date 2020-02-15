package tstc.fxq.threads;

import tstc.fxq.constants.Constant;
import tstc.fxq.main.MyActivity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;
/*
 * �ۤv�}�o�����T���`���������,�Ω�X�{�ǳW�ɡA�X�{Toast
 */

public class LooperThread extends Thread
{
	MyActivity activity;
	//�T���B�z��
	Handler hd;
	//�W�U��
	public Context context;
	
	public LooperThread(Context context,MyActivity activity)
	{
		this.context=context;
		this.activity=activity;
	}
	
	@Override
	public void run()
	{
		//�i��prepare�A�إ߰T����C
		Looper.prepare();
		
		//�إ߰T���B�z������
		hd=new Handler()
        {
        	@Override
        	public void handleMessage(Message msg)
        	{
        		//�I�s�����O�B�z
        		super.handleMessage(msg);
        		//�ھڰT��what�s�������P�A���椣�P���~���޿�
        		switch(msg.what)
        		{
        		   //�N�T���������e���R�X����ܦbToast��
        		   case Constant.OVERTIMETOAST :
        			   //���Toast
	        		   Toast.makeText
	               	   (
	               		  context, 
	               		  "�ǳW�G���y�O�ɡI", 
	               		  Toast.LENGTH_SHORT
	               	   ).show();
        		   break;
        		   
        		   //15��᥼���y�A�N�����y�O�ɥǳW�B�z
        		   case Constant.REMINDPLAYER :
        			   //���Toast
	        		   Toast.makeText
	               	   (
	               		  context, 
	               		  "���ܡG15�������y�A�N���y�O�ɥǳW�I", 
	               		  Toast.LENGTH_SHORT
	               	   ).show();
        		   break;
        		   
        		   //�N�T���������e���R�X����ܦbToast��
        		   case Constant.MAINBALL_FLOP :
        			   //���Toast
	        		   Toast.makeText
	               	   (
	               		  context, 
	               		  "�ǳW�G�D�y���U�I", 
	               		  Toast.LENGTH_SHORT
	               	   ).show();
        		   break;
          		   case Constant.NO_FIGHT :
        			   //���Toast
	        		   Toast.makeText
	               	   (
	               		  context, 
	               		  "�ǳW�G�D�y���P����y�o�͸I���I", 
	               		  Toast.LENGTH_SHORT
	               	   ).show();
        		   break;
          		 case Constant.NO_FIGHT_AND_FLOP :
      			   //���Toast
	        		   Toast.makeText
	               	   (
	               		  context, 
	               		  "�ǳW�G�D�y�i�}�I\n�ǳW�G�D�y���P����y�o�͸I���I", 
	               		  Toast.LENGTH_SHORT
	               	   ).show();
	        		   break;
          		 case Constant.MAINBALLFLOP_AND_NOTFIGHTTARGET :
        			   //���Toast
  	        		   Toast.makeText
  	               	   (
  	               		  context, 
  	               		  "�ǳW�G�D�y�i�}�I\n�ǳW�G���������O�ت��y�I", 
  	               		  Toast.LENGTH_SHORT
  	               	   ).show();
  	        		   break;
          		 case Constant.NO_FIGHT_TARGET :
        			   //���Toast
  	        		   Toast.makeText
  	               	   (
  	               		  context, 
  	               		  "�ǳW�G���������O�ت��y�I", 
  	               		  Toast.LENGTH_SHORT
  	               	   ).show();
  	        		   break;
          		 case Constant.EXIT_SYSTEM_LOSE9 :
          			 
          			activity.showDialog(activity.EXIT_SYSTEM9);//�Y�G9���y�i�}
          			
	        		   break;
          		 case Constant.EXIT_SYSTEM_LOSE8 :
           			activity.showDialog(activity.EXIT_SYSTEM8);//�Y�G9���y�i�}
 	        		   break;
          		 case Constant.EXIT_SYSTEM_WIN :          			
          			activity.showDialog(activity.EXIT_WIN);//�Y�G9���y�i�}          			
	        		   break;
          		 case Constant.TIME_UP :          			
          			activity.showDialog(activity.EXIT_TIME_UP);//�Y�G9���y�i�}          			
	        		   break;
          		 case Constant.BREAK_RECORD :          			
          			activity.showDialog(activity.EXIT_BREAK_RECORD);//�Y�G9���y�i�}          			
	        		   break;
        		}
        	}
        };        		
		//�إ߰T���`��
		Looper.loop();
	}
}
