package com.f.util;

//�p���Y�񱡪p���u�����O
public class ScreenScaleUtil
{
	static final float sHpWidth=800;//��l��̪��e��
	static final float sHpHeight=480;//��l��̪�����
	static final float whHpRatio=sHpWidth/sHpHeight;//��l��̪����e��
	
	
	static final float sSpWidth=480;//��l�������e��
	static final float sSpHeight=800;//��l����������
	static final float whSpRatio=sSpWidth/sSpHeight;//��l���������e��
	
	
	public static ScreenScaleResult calScale
	(
		float targetWidth,	//�ت��e��
		float targetHeight	//�ت�����
	)
	{
		ScreenScaleResult result=null;
		ScreenOrien so=null;
		
		//�����P�_�ت��O����٬O����
		if(targetWidth>targetHeight)
		{
			so=ScreenOrien.HP;
		}
		else
		{
			so=ScreenOrien.SP;
		}
		
		
		//�i���̵��G���p��
		if(so==ScreenOrien.HP)
		{
			//�p��ت������e��
			float targetRatio=targetWidth/targetHeight;
			
			if(targetRatio>whHpRatio)
			{
				//�Y�ت����e��j���l���e��h�H�ت������׭p�⵲�G			    
			    float ratio=targetHeight/sHpHeight;
			    float realTargetWidth=sHpWidth*ratio;
			    float lcuX=(targetWidth-realTargetWidth)/2.0f;
			    float lcuY=0;
			    result=new ScreenScaleResult((int)lcuX,(int)lcuY,ratio,so);	
			}
			else
			{
				//�Y�ت����e��p���l���e��h�H�ت����e�׭p�⵲�G	
				float ratio=targetWidth/sHpWidth;
				float realTargetHeight=sHpHeight*ratio;
				float lcuX=0;
				float lcuY=(targetHeight-realTargetHeight)/2.0f;
				result=new ScreenScaleResult((int)lcuX,(int)lcuY,ratio,so);	
			}
		}
		
		//�i�櫫�����G���p��
		if(so==ScreenOrien.SP)
		{
			//�p��ت������e��
			float targetRatio=targetWidth/targetHeight;
			
			if(targetRatio>whSpRatio)
			{
				//�Y�ت����e��j���l���e��h�H�ت������׭p�⵲�G			    
			    float ratio=targetHeight/sSpHeight;
			    float realTargetWidth=sSpWidth*ratio;
			    float lcuX=(targetWidth-realTargetWidth)/2.0f;
			    float lcuY=0;
			    result=new ScreenScaleResult((int)lcuX,(int)lcuY,ratio,so);	
			}
			else
			{
				//�Y�ت����e��p���l���e��h�H�ت����e�׭p�⵲�G	
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