package tstc.fxq.threads;

import tstc.fxq.constants.Constant;
import tstc.fxq.main.MyActivity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;
/*
 * 自己開發的有訊息循環的執行緒,用於出現犯規時，出現Toast
 */

public class LooperThread extends Thread
{
	MyActivity activity;
	//訊息處理器
	Handler hd;
	//上下文
	public Context context;
	
	public LooperThread(Context context,MyActivity activity)
	{
		this.context=context;
		this.activity=activity;
	}
	
	@Override
	public void run()
	{
		//進行prepare，建立訊息佇列
		Looper.prepare();
		
		//建立訊息處理器物件
		hd=new Handler()
        {
        	@Override
        	public void handleMessage(Message msg)
        	{
        		//呼叫父類別處理
        		super.handleMessage(msg);
        		//根據訊息what編號的不同，執行不同的業務邏輯
        		switch(msg.what)
        		{
        		   //將訊息中的內容分析出來顯示在Toast中
        		   case Constant.OVERTIMETOAST :
        			   //顯示Toast
	        		   Toast.makeText
	               	   (
	               		  context, 
	               		  "犯規：擊球逾時！", 
	               		  Toast.LENGTH_SHORT
	               	   ).show();
        		   break;
        		   
        		   //15秒後未擊球，將按擊球逾時犯規處理
        		   case Constant.REMINDPLAYER :
        			   //顯示Toast
	        		   Toast.makeText
	               	   (
	               		  context, 
	               		  "提示：15秒內未擊球，將擊球逾時犯規！", 
	               		  Toast.LENGTH_SHORT
	               	   ).show();
        		   break;
        		   
        		   //將訊息中的內容分析出來顯示在Toast中
        		   case Constant.MAINBALL_FLOP :
        			   //顯示Toast
	        		   Toast.makeText
	               	   (
	               		  context, 
	               		  "犯規：主球落袋！", 
	               		  Toast.LENGTH_SHORT
	               	   ).show();
        		   break;
          		   case Constant.NO_FIGHT :
        			   //顯示Toast
	        		   Toast.makeText
	               	   (
	               		  context, 
	               		  "犯規：主球未與任何球發生碰撞！", 
	               		  Toast.LENGTH_SHORT
	               	   ).show();
        		   break;
          		 case Constant.NO_FIGHT_AND_FLOP :
      			   //顯示Toast
	        		   Toast.makeText
	               	   (
	               		  context, 
	               		  "犯規：主球進洞！\n犯規：主球未與任何球發生碰撞！", 
	               		  Toast.LENGTH_SHORT
	               	   ).show();
	        		   break;
          		 case Constant.MAINBALLFLOP_AND_NOTFIGHTTARGET :
        			   //顯示Toast
  	        		   Toast.makeText
  	               	   (
  	               		  context, 
  	               		  "犯規：主球進洞！\n犯規：擊打的不是目的球！", 
  	               		  Toast.LENGTH_SHORT
  	               	   ).show();
  	        		   break;
          		 case Constant.NO_FIGHT_TARGET :
        			   //顯示Toast
  	        		   Toast.makeText
  	               	   (
  	               		  context, 
  	               		  "犯規：擊打的不是目的球！", 
  	               		  Toast.LENGTH_SHORT
  	               	   ).show();
  	        		   break;
          		 case Constant.EXIT_SYSTEM_LOSE9 :
          			 
          			activity.showDialog(activity.EXIT_SYSTEM9);//若果9號球進洞
          			
	        		   break;
          		 case Constant.EXIT_SYSTEM_LOSE8 :
           			activity.showDialog(activity.EXIT_SYSTEM8);//若果9號球進洞
 	        		   break;
          		 case Constant.EXIT_SYSTEM_WIN :          			
          			activity.showDialog(activity.EXIT_WIN);//若果9號球進洞          			
	        		   break;
          		 case Constant.TIME_UP :          			
          			activity.showDialog(activity.EXIT_TIME_UP);//若果9號球進洞          			
	        		   break;
          		 case Constant.BREAK_RECORD :          			
          			activity.showDialog(activity.EXIT_BREAK_RECORD);//若果9號球進洞          			
	        		   break;
        		}
        	}
        };        		
		//建立訊息循環
		Looper.loop();
	}
}
