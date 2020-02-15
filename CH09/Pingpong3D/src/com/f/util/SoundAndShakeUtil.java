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
	
	//���Ľw�R�����_�l��
    public void initSounds()
    {
    	 //�إ߭��Ľw�R��
	     soundPool = new SoundPool
	     (
	    		 10, 							//�P�ɯ�̦h���񪺭Ӽ�
	    		 AudioManager.STREAM_MUSIC,     //���W�����A
	    		 100							//���Ī������q�A�ثe�L��
	     );
	     
	     //�إ߭��ĸ귽Map	     
	     soundPoolMap = new HashMap<Integer, Integer>();
	     //�N���J�����ĸ귽id��i��Map
	     soundPoolMap.put(0, soundPool.load(activity,R.raw.ball_paddle, 1));
	     soundPoolMap.put(1, soundPool.load(activity,R.raw.ball_table, 1));
	     soundPoolMap.put(2, soundPool.load(activity,R.raw.cheer_short, 1));
	     soundPoolMap.put(3, soundPool.load(activity,R.raw.cheer_long , 1));
	     soundPoolMap.put(4, soundPool.load(activity,R.raw.catcall , 1));//�N�n
	     soundPoolMap.put(5, soundPool.load(activity,R.raw.button_click , 1));//���s
	     soundPoolMap.put(6, soundPool.load(activity,R.raw.paddle , 1));//���y�n
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
    
    //���񭵮Ī���k
    public void playSoundL(int sound, int loop) 
    {
    	if(!MainMenuSurfaceView.soundflag)return;
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
    		1f					 	//�����t��0.5f��2.0f����
	    );		
    }
    public void shake()
    {
    	if(!MainMenuSurfaceView.vibrateflag) return;
    	activity.vibrator.vibrate(Constant.COLLISION_SOUND_PATTERN,-1);//�_��
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
