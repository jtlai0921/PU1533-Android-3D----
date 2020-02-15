package com.bn.gjxq;
import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundUtil 
{	
    //���Ľs��
	public static final int XUANZHONG=1;//�Ѥl�Ŀﭵ�Ľs��
	public static final int CHIZI=2;//�Y�l���Ľs��
	public static final int PUTONGLUOZI=3;//���q���l���Ľs��
	
	
	public static SoundPool soundPool;//���Ľw�R��
	public static HashMap<Integer, Integer> soundPoolMap; //�s�񭵮�ID��Map
	
    //���Ľw�R�����_�l��
    public static void initSounds(Context context)
    {
    	 //�إ߭��Ľw�R��
	     soundPool = new SoundPool
	     (
	    		 2, 							//�P�ɯ�̦h���񪺭Ӽ�
	    		 AudioManager.STREAM_MUSIC,     //���W�����A
	    		 100							//���Ī������q�A�ثe�L��
	     );
	     
	     //�إ߭��ĸ귽Map	     
	     soundPoolMap = new HashMap<Integer, Integer>();   
	     //�N���J�����ĸ귽id��i��Map
	     soundPoolMap.put(XUANZHONG, soundPool.load(context, R.raw.xuanzhong, 1));
	     soundPoolMap.put(CHIZI, soundPool.load(context, R.raw.chizi,1));
	     soundPoolMap.put(PUTONGLUOZI, soundPool.load(context, R.raw.putongluozi,1));
	} 
    
    //���񭵮Ī���k
	public static void playSounds(int key, int loop,Context context) 
	{
		AudioManager mgr = (AudioManager)  context.getSystemService(Context.AUDIO_SERVICE); 
		float streamVolumeCurrent = mgr
				.getStreamVolume(AudioManager.STREAM_MUSIC); 
		float streamVolumeMax = mgr
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC); 
		float volume = streamVolumeCurrent / streamVolumeMax; 
		soundPool.play(
				soundPoolMap.get(key), //����id
				volume, //���n�D
				volume, //�k�n�D
				1, //�u����
				loop,//�O�_�`��
				0.5f //rate
				);
	}
}
