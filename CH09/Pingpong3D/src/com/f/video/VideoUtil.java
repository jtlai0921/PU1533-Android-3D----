package com.f.video;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import android.content.res.Resources;
import android.util.Log;

public class VideoUtil 
{
	@SuppressWarnings("unchecked")
	public static List<FrameData> getFrameData(Resources res,String fileName)
	{
		ObjectInputStream objIn = null;
		List<FrameData> listdata = null;
		try
		{
			objIn=new ObjectInputStream(new BufferedInputStream(res.getAssets().open(fileName)));
			listdata=(List<FrameData>)objIn.readObject();
			objIn.close();
		}
		catch (IOException e)
		{
			Log.v("TEST","IOException:"+e);
		} 
		catch (ClassNotFoundException e)
		{
			Log.v("TEST","ClassNotFoundException:"+e);
		}
		return listdata;
	}
}
