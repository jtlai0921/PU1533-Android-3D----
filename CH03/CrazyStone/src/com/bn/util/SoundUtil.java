package com.bn.util;

import java.util.HashMap;

import com.bn.zxl.MyActivity;

import com.bn.zxl.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;


public class SoundUtil 
{
	
	public MediaPlayer mp;
	public SoundPool soundPool;
	HashMap<Integer, Integer> soundPoolMap;
	MyActivity activity;
	public SoundUtil(MyActivity activity) 
	{
		this.activity=activity;
		initSounds();
	}
	
	public void initSounds() 
	{
		soundPool=new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
		soundPoolMap=new HashMap<Integer, Integer>();
		
		soundPoolMap.put(Constant.BUTTON_SOUND, soundPool.load(activity, R.raw.button, 1));
	}
	
	public void playEffectSound(int soundID,int loop)
	{
		if (!Constant.getEffectSoundStatus()) 
		{
			return;
		}
		AudioManager am=(AudioManager)activity.getSystemService(Context.AUDIO_SERVICE);
		float currVolume=am.getStreamVolume(AudioManager.STREAM_MUSIC);
		float maxVolume=am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume=currVolume/maxVolume;
		soundPool.play(soundPoolMap.get(soundID), volume, volume, 1, loop, 1f);
	}
	
	public void playBackgroungMusic() 
	{
		mp=MediaPlayer.create(activity, R.raw.musicgame);
		mp.setLooping(true);
		try {
				mp.prepare();
		} catch (Exception e) {
			e.printStackTrace();
		}
		mp.start();
	}
	
	public void stopBackgroungMusic()
	{
		mp.stop();
		mp.release();
	}
}
