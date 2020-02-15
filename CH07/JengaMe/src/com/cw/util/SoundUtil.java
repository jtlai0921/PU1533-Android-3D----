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
	SoundPool soundPool;//���Ħ�
	HashMap<Integer, Integer> soundPoolMap;
	JengaMeActivity activity;
//	boolean music_closed=true;
	public SoundUtil(JengaMeActivity activity)//�غc��
	{
		this.activity=activity;
	}
	//���Ľw�R�����_�l��
    public void initSounds()
    {
    	 //�إ߭��Ľw�R��
	     soundPool = new SoundPool
	     (
	    		 6, 							//�P�ɯ�̦h���񪺭Ӽ�
	    		 AudioManager.STREAM_MUSIC,     //���W�����A
	    		 100							//���Ī������q�A�ثe�L��
	     );
	     //�إ߭��ĸ귽Map
	     soundPoolMap = new HashMap<Integer, Integer>();   
	     //�N���J�����ĸ귽id��i��Map
	     soundPoolMap.put(1, soundPool.load(activity, R.raw.wall_sound, 1));//�����Y
	} 
       
   //���񭵮Ī���k
   public void playEffectsSound(int sound, int loop) {
	   if(true)
	   {
		   AudioManager mgr = (AudioManager)activity.getSystemService(Context.AUDIO_SERVICE);
		    float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);//�ثe���q   
		    float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//�̤j���q       
		    float volume = streamVolumeCurrent / streamVolumeMax;   
		    
		    soundPool.play
		    (
	    		soundPoolMap.get(sound), //���ĸ귽id
	    		volume, 				 //���n�D���q
	    		volume, 				 //�k�n�D���q
	    		1, 						 //�u����				 
	    		loop, 					 //�`������ -1�a���û��`��
	    		1f					 //�����t��0.5f��2.0f����
		    );
	   }
	}
   public void play_bg_sound()//
   {
	   if(activity.playBackMusic)
	   {
		   //�z�Lassets ���J����
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