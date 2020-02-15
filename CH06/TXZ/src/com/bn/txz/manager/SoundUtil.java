package com.bn.txz.manager;
import java.util.HashMap;

import com.bn.txz.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundUtil 
{	
    //音效編號
	public static final int XUANZHONG=1;//棋子勾選音效編號
	
	
	public static SoundPool soundPool;//音效緩沖池
	public static HashMap<Integer, Integer> soundPoolMap; //存放音效ID的Map
	
    //音效緩沖池的起始化
    public static void initSounds(Context context)
    {
    	 //建立音效緩沖池
	     soundPool = new SoundPool
	     (
	    		 2, 							//同時能最多播放的個數
	    		 AudioManager.STREAM_MUSIC,     //音頻的型態
	    		 100							//音效的播放質量，目前無效
	     );
	     
	     //建立音效資源Map	     
	     soundPoolMap = new HashMap<Integer, Integer>();   
	     //將載入的音效資源id放進此Map
	     soundPoolMap.put(XUANZHONG, soundPool.load(context, R.raw.zhuangsui, 1));
	} 
    
    //播放音效的方法
	public static void playSounds(int key, int loop,Context context) 
	{
		AudioManager mgr = (AudioManager)  context.getSystemService(Context.AUDIO_SERVICE); 
		float streamVolumeCurrent = mgr
				.getStreamVolume(AudioManager.STREAM_MUSIC); 
		float streamVolumeMax = mgr
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC); 
		float volume = streamVolumeCurrent / streamVolumeMax; 
		soundPool.play(
				soundPoolMap.get(key), //音效id
				volume, //左聲道
				volume, //右聲道
				1, //優先級
				loop,//是否循環
				0.5f //rate
				);
	}
}
