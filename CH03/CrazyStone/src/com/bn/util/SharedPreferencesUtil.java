package com.bn.util;

import com.bn.zxl.MyActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtil 
{
	private SharedPreferences sp;
	
	public SharedPreferencesUtil(MyActivity activity) 
	{
		sp=activity.getSharedPreferences("playerPrefers", Context.MODE_PRIVATE);
	}
	
	public boolean getEffectSoundStatus() 
	{
		return sp.getBoolean("effectSoundStatus", false);
	}
	
	public boolean getBackgroundMusicStatus() 
	{
		return sp.getBoolean("backgroundMusicStatus", false);
	}
	
	public int getPassNum() 
	{
		return sp.getInt("passNum", 0);
	}
	
	public void putEffectSoundStatus(boolean value)
	{
		Editor editor=sp.edit();
		editor.putBoolean("effectSoundStatus", value);
		editor.commit();
	}
	
	public void putBackgroundMusicStatus(boolean value)
	{
		Editor editor=sp.edit();
		editor.putBoolean("backgroundMusicStatus", value);
		editor.commit();
	}
	
	public void putPassNum(int value)
	{
		Editor editor=sp.edit();
		editor.putInt("passNum", value);
		editor.commit();
	}
}
