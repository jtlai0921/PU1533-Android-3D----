package lwz.com.tank.view;

import java.io.IOException;
import java.util.HashMap;

import lwz.com.tank.activity.MainActivity;
import lwz.com.tank.activity.R;


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
	MainActivity activity;
//	boolean music_closed=true;
	public SoundUtil(MainActivity activity)//�غc��
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
	     soundPoolMap.put(1, soundPool.load(activity, R.raw.canon_fire_a, 1));//�o��
	     soundPoolMap.put(2, soundPool.load(activity, R.raw.explosion_a, 1));//�z��
	} 
       
   //���񭵮Ī���k
   public void playEffectsSound(int sound, int loop) {
	   if(SettingView.soundflag)
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
	    		loop, 					 //�`������ -1�a��û��`��
	    		1f					 //�����t��0.5f��2.0f����
		    );
	   }
	}
   public void play_bg_sound()//
   {
	   if(activity.playbackmusic)
//		   if(SettingView.musicflag)
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
	   if(!activity.playbackmusic&&mp!=null)
	   {
		   mp.stop();
		   mp.release();
	   }
   }
}
