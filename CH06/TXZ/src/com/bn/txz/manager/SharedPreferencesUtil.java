package com.bn.txz.manager;

import com.bn.txz.TXZActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtil 
{
	private SharedPreferences sp;
	
	public SharedPreferencesUtil(TXZActivity activity) 
	{
		sp=activity.getSharedPreferences("playerPrefers", Context.MODE_PRIVATE);
	}
	
	public int getPassNum() 
	{
		return sp.getInt("passNum", 1);
	}
	
	public void putPassNum(int value)
	{
		Editor editor=sp.edit();
		editor.putInt("passNum", value);
		editor.commit();
	}
	
}
