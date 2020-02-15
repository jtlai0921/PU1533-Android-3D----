package com.f.util;

//計算縮放情況的工具類別
public class ScreenScaleUtil
{
	static final float sHpWidth=800;//原始橫屏的寬度
	static final float sHpHeight=480;//原始橫屏的高度
	static final float whHpRatio=sHpWidth/sHpHeight;//原始橫屏的長寬比
	
	
	static final float sSpWidth=480;//原始垂直的寬度
	static final float sSpHeight=800;//原始垂直的高度
	static final float whSpRatio=sSpWidth/sSpHeight;//原始垂直的長寬比
	
	
	public static ScreenScaleResult calScale
	(
		float targetWidth,	//目的寬度
		float targetHeight	//目的高度
	)
	{
		ScreenScaleResult result=null;
		ScreenOrien so=null;
		
		//首先判斷目的是橫屏還是垂直
		if(targetWidth>targetHeight)
		{
			so=ScreenOrien.HP;
		}
		else
		{
			so=ScreenOrien.SP;
		}
		
		
		//進行橫屏結果的計算
		if(so==ScreenOrien.HP)
		{
			//計算目的的長寬比
			float targetRatio=targetWidth/targetHeight;
			
			if(targetRatio>whHpRatio)
			{
				//若目的長寬比大於原始長寬比則以目的的高度計算結果			    
			    float ratio=targetHeight/sHpHeight;
			    float realTargetWidth=sHpWidth*ratio;
			    float lcuX=(targetWidth-realTargetWidth)/2.0f;
			    float lcuY=0;
			    result=new ScreenScaleResult((int)lcuX,(int)lcuY,ratio,so);	
			}
			else
			{
				//若目的長寬比小於原始長寬比則以目的的寬度計算結果	
				float ratio=targetWidth/sHpWidth;
				float realTargetHeight=sHpHeight*ratio;
				float lcuX=0;
				float lcuY=(targetHeight-realTargetHeight)/2.0f;
				result=new ScreenScaleResult((int)lcuX,(int)lcuY,ratio,so);	
			}
		}
		
		//進行垂直結果的計算
		if(so==ScreenOrien.SP)
		{
			//計算目的的長寬比
			float targetRatio=targetWidth/targetHeight;
			
			if(targetRatio>whSpRatio)
			{
				//若目的長寬比大於原始長寬比則以目的的高度計算結果			    
			    float ratio=targetHeight/sSpHeight;
			    float realTargetWidth=sSpWidth*ratio;
			    float lcuX=(targetWidth-realTargetWidth)/2.0f;
			    float lcuY=0;
			    result=new ScreenScaleResult((int)lcuX,(int)lcuY,ratio,so);	
			}
			else
			{
				//若目的長寬比小於原始長寬比則以目的的寬度計算結果	
				float ratio=targetWidth/sSpWidth;
				float realTargetHeight=sSpHeight*ratio;
				float lcuX=0;
				float lcuY=(targetHeight-realTargetHeight)/2.0f;
				result=new ScreenScaleResult((int)lcuX,(int)lcuY,ratio,so);	
			}
			
		}
		
		return result;
	}
}