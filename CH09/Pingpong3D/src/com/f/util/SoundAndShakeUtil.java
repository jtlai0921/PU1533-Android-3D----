package com.f.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.f.pingpong.Constant;
import com.f.pingpong.MainActivity;
import com.f.pingpong.R;
import com.f.view.MainMenuSurfaceView;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class SoundAndShakeUtil 
{
	public SoundPool soundPool;
	public HashMap<Integer, Integer> soundPoolMap;
	public MainActivity activity;
	public static MediaPlayer mp;
	public static boolean is_playing = false;
	public SoundAndShakeUtil(MainActivity activity)
	{
		this.activity=activity;
	}
	
	//音效緩沖池的起始化
    public void initSounds()
    {
    	 //建立音效緩沖池
	     soundPool = new SoundPool
	     (
	    		 10, 							//同時能最多播放的個數
	    		 AudioManager.STREAM_MUSIC,     //音頻的型態
	    		 100							//音效的播放質量，目前無效
	     );
	     
	     //建立音效資源Map	     
	     soundPoolMap = new HashMap<Integer, Integer>();
	     //將載入的音效資源id放進此Map
	     soundPoolMap.put(0, soundPool.load(activity,R.raw.ball_paddle, 1));
	     soundPoolMap.put(1, soundPool.load(activity,R.raw.ball_table, 1));
	     soundPoolMap.put(2, soundPool.load(activity,R.raw.cheer_short, 1));
	     soundPoolMap.put(3, soundPool.load(activity,R.raw.cheer_long , 1));
	     soundPoolMap.put(4, soundPool.load(activity,R.raw.catcall , 1));//噓聲
	     soundPoolMap.put(5, soundPool.load(activity,R.raw.button_click , 1));//按鈕
	     soundPoolMap.put(6, soundPool.load(activity,R.raw.paddle , 1));//扇球聲
    }
    
    List<Integer> task=new ArrayList<Integer>();
    public void playSound(int sound, int loop)
    {
    	synchronized(Constant.soundTaskLock)
    	{
    		task.add(sound);
    	}
    }
    
    public int[] getTask()
    {
    	int[] taskList=null;
    	synchronized(Constant.soundTaskLock)
    	{
    		taskList=new int[task.size()];
    		for(int i=0;i<task.size();i++)
    		{
    			taskList[i]=task.get(i);
    		}
    		task.clear();
    	}
    	return taskList;
    }
    
    public void doTask(int[] taskList)
    {
    	for(int i:taskList)
    	{
    		playSoundL(i, 0);
    	}
    }
    
    //播放音效的方法
    public void playSoundL(int sound, int loop) 
    {
    	if(!MainMenuSurfaceView.soundflag)return;
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
    		1f					 	//重播速度0.5f～2.0f之間
	    );		
    }
    public void shake()
    {
    	if(!MainMenuSurfaceView.vibrateflag) return;
    	activity.vibrator.vibrate(Constant.COLLISION_SOUND_PATTERN,-1);//震動
    }
    public void palyBgSound()
    {
    	if(is_playing || !MainMenuSurfaceView.soundflag){return;}
    	mp = MediaPlayer.create(activity, R.raw.background_music);
    	mp.start();
    	mp.setLooping(true);
    	is_playing = true;
    }
    
    public void stopBgSound()
    {
    	if((!is_playing && !MainMenuSurfaceView.soundflag) || mp == null){return;}
	    mp.stop();
	    mp.release();
	    is_playing = false;
    }
}
