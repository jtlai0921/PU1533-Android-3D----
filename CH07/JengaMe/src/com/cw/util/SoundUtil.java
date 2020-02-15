package com.cw.util;

import java.io.IOException;
import java.util.HashMap;

import com.cw.game.JengaMeActivity;
import com.cw.game.R;
import com.cw.game.R.raw;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class SoundUtil
{
	public MediaPlayer mp;
	SoundPool soundPool;//音效池
	HashMap<Integer, Integer> soundPoolMap;
	JengaMeActivity activity;
//	boolean music_closed=true;
	public SoundUtil(JengaMeActivity activity)//建構器
	{
		this.activity=activity;
	}
	//音效緩沖池的起始化
    public void initSounds()
    {
    	 //建立音效緩沖池
	     soundPool = new SoundPool
	     (
	    		 6, 							//同時能最多播放的個數
	    		 AudioManager.STREAM_MUSIC,     //音頻的型態
	    		 100							//音效的播放質量，目前無效
	     );
	     //建立音效資源Map
	     soundPoolMap = new HashMap<Integer, Integer>();   
	     //將載入的音效資源id放進此Map
	     soundPoolMap.put(1, soundPool.load(activity, R.raw.wall_sound, 1));//撞木頭
	} 
       
   //播放音效的方法
   public void playEffectsSound(int sound, int loop) {
	   if(true)
	   {
		   AudioManager mgr = (AudioManager)activity.getSystemService(Context.AUDIO_SERVICE);
		    float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);//目前音量   
		    float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//最大音量       
		    float volume = streamVolumeCurrent / streamVolumeMax;   
		    
		    soundPool.play
		    (
	    		soundPoolMap.get(sound), //音效資源id
	    		volume, 				 //左聲道音量
	    		volume, 				 //右聲道音量
	    		1, 						 //優先級				 
	    		loop, 					 //循環次數 -1帶表永遠循環
	    		1f					 //重播速度0.5f∼2.0f之間
		    );
	   }
	}
   public void play_bg_sound()//
   {
	   if(activity.playBackMusic)
	   {
		   //透過assets 載入音樂
	     AssetManager assetManager = activity.getAssets();  
	     try {  
	     mp = new MediaPlayer();  
	     String s;
	     s= "back.mp3"; 
	     AssetFileDescriptor fileDescriptor= assetManager.openFd(s); 
	     mp.setDataSource(fileDescriptor.getFileDescriptor(),  
	     fileDescriptor.getStartOffset(),  
	     fileDescriptor.getLength()); 
	     mp.setLooping(true);
	     mp.prepare();  
	     mp.start();  
	     } catch (IOException e) 
	     {  
	      e.printStackTrace();  
	     } 
	   }
   }
   
   public void stop_bg_sound()
   {
	   if(!activity.playBackMusic&&mp!=null)
	   {
		   mp.stop();
		   mp.release();
	   }
   }
}
