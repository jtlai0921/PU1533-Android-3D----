package lwz.com.tank.game;

import static lwz.com.tank.activity.Constant.*;
import java.util.Queue;

//����ʧ@��C�������
public class DoActionThread extends Thread
{
	public boolean workFlag=true;		//������O�_�`���u�@�ЧӦ�
	MySurfaceView msv;
	Queue<Action> aq;					//�ʧ@��C
	
    //�Z�J�����ਤ��
    float vAngle=0;
    //���ު���������
    float pAngle=0;
    //�Z�J�C�طh�����B�|
    float hd_x=0;
    float hd_y=0;
    //�n�줤�߶��I�������q
    public static float scale_x=0;
    public static float scale_y=0;
    //�Z�J1�h�����B�|
    public static float bx=0;
    public static float by=0;
  	float tkwz_x=0;
  	float tkwz_y=0;
  	//�Z�J1���_�l��m
	public  float Tank1_x=0;
	public  float Tank1_y=-100;
	//�n�줤���I�����y�ФΥb�|
	public  float INIT_x=(115+ssr.lucX)*ssr.ratio;
	public  float INIT_y=(425+ssr.lucY)*ssr.ratio;
	public  float INIT_R=100*(SCREEN_WIDTH-ssr.lucX*ssr.ratio*2)/SCREEN_WIDTH_STANDARD;
	//�Z�J�_�l�������y��
    float tkposition_x=480f;
    float tkposition_y=438.75f;
    public static float fireflag=0;
    
	public DoActionThread(MySurfaceView msv)
    {
    	this.msv=msv;		//����View
    	this.aq=msv.aq;		//�ʧ@��C
    }
	 @Override
	public void run()
	{
		 while(workFlag)
		 {
			Action ac=null;					//�ʧ@�Ѧ�
    		synchronized(msv.aqLock)		//�ʧ@��C��
    		{
    			ac=aq.poll();				//�q�ʧ@��C�����X�@�Ӱʧ@�A�Y��C���S���ޱ��ʧ@�h���Xnull
    		} 
    		if(ac!=null)					//�Y�ޱ��ʧ@�ѦҤ��Onull�A�Y���ʧ@�ݭn����
    		{
    			switch(ac.at)
    			{
    				case MOVE_ACTION:
    					float x;
    					float y;
    					if(Math.sqrt((ac.data[0]-INIT_x)*(ac.data[0]-INIT_x)+(ac.data[1]-INIT_y)*(ac.data[1]-INIT_y))<INIT_R)
					    {
							x=ac.data[0];
							y=ac.data[1];
					    }
					    else
					    {
						   //�ثe�I�y�Ш�n�줤���I���Z��
						   float tempR=(float) Math.sqrt((ac.data[0]-INIT_x)*(ac.data[0]-INIT_x)+(INIT_y-ac.data[1])*(INIT_y-ac.data[1]));
						   //�N�ثe�I�y�бj���ର�n��Ĳ���d����t�y��
						   x=INIT_x+(ac.data[0]-INIT_x)*INIT_R/tempR;
						   y=INIT_y+(ac.data[1]-INIT_y)*INIT_R/tempR;
						   
					    }
    					//�n�줤�߶��I��x�b�����q
    					scale_x=(x-INIT_x)/INIT_R;
    					//�n�줤�߶��I��y�b�����q
    					scale_y=(INIT_y-y)/INIT_R;
    					if(y<INIT_y)
    				   	{
    				   		//�p��Z�J�����ਤ��
    					   	vAngle=-(float)Math.toDegrees( Math.asin((x-INIT_x)/Math.sqrt((x-INIT_x)*(x-INIT_x)+(y-INIT_y)*(y-INIT_y))));
    				   	}
    					if(y>INIT_y)
    				   	{
    				   		vAngle=-180+(float)Math.toDegrees( Math.asin((x-INIT_x)/Math.sqrt((x-INIT_x)*(x-INIT_x)+(y-INIT_y)*(y-INIT_y))));
    				   	}
    					hd_x=-(float) (MOVE_SPAN*Math.sin(Math.toRadians(vAngle)));
    					hd_y=(float) (MOVE_SPAN*Math.cos(Math.toRadians(vAngle)));
    					if(y==INIT_y)
    					{
    						hd_x=0;
    						hd_y=0;
    					}
    					
    					bx=bx+hd_x;//�Z�J1�C���ܴ���x�y��
    					by=by+hd_y;//�Z�J1�C���ܴ���y�y��
    					//���o�G��}�C������
    					int rows=lowWallmapdata.length;
    					//��}�C�i��`�����I������
    					for(int i=0;i<rows;i++)
    					{	
    						if(bx>lowWallmapdata[i][0]*Wall_UNIT_SIZE-20f-Tank1_x&&bx<(lowWallmapdata[i][0]+lowWallmapdata[i][3])*Wall_UNIT_SIZE+20f-Tank1_x&&by<lowWallmapdata[i][1]*Wall_UNIT_SIZE+20f-Tank1_y&&by>(lowWallmapdata[i][1]-lowWallmapdata[i][4])*Wall_UNIT_SIZE-20f-Tank1_y)
    						{
    							if(bx-hd_x>lowWallmapdata[i][0]*Wall_UNIT_SIZE-20f-Tank1_x&&bx-hd_x<(lowWallmapdata[i][0]+lowWallmapdata[i][3])*Wall_UNIT_SIZE+20f-Tank1_x)	
    							{
									by=by-hd_y;
    							}
    							else
    							{
									bx=bx-hd_x;
    							}
    						}
    					}
    					tkwz_x=bx+Tank1_x;
    					tkwz_y=by+Tank1_y;
    					synchronized(msv.gdMain.dataLock) 	//�N�D�����W
    					{
    						msv.gdMain.maintkwz[0]=tkwz_x;
    						msv.gdMain.maintkwz[1]=tkwz_y;
    						msv.gdMain.maintkwz[2]=vAngle;
    						msv.gdMain.maintkwz[3]=scale_x;
    						msv.gdMain.maintkwz[4]=scale_y;
    						msv.activity.sendMessage(tkwz_x+"<#>"+tkwz_y+"<#>"+vAngle+"<#>");
    						synchronized(msv.gdMainDraw.dataLock) //�Nø������W
    						{
    							msv.gdMainDraw.maintkwz[0]=tkwz_x;
        						msv.gdMainDraw.maintkwz[1]=tkwz_y;
        						msv.gdMainDraw.maintkwz[2]=vAngle;
        						msv.gdMainDraw.maintkwz[3]=scale_x;
        						msv.gdMainDraw.maintkwz[4]=scale_y;
    						}
    					}
    					break;
    				case FIRE_ACTION:
    					fireflag=1;
    					float x2=ac.data[0];
    					float y2=ac.data[1];
    					//��ɭp��Z�J��v��ù��W���y�СA���ɬO��v�����H�۩Z�J�h���C
				  		 tkposition_x=tkwz_x*1.5f*SCREEN_WIDTH/SCREEN_WIDTH_STANDARD+SCREEN_WIDTH/2;//�_�l�Z�J��mx
				  		 tkposition_y=SCREEN_HEIGHT/2-(tkwz_y)*1f*SCREEN_WIDTH/SCREEN_WIDTH_STANDARD;//�_�l�Z�J��my
    					//���ɬO��v���H�۩Z�J�h���U�A��ɭp��Z�J��v��ù��W���y�СC
//				  		 tkposition_x=(Tank1_x)*1.5f*SCREEN_WIDTH/SCREEN_WIDTH_STANDARD+SCREEN_WIDTH/2;//�_�l�Z�J��mx
//				  		 tkposition_y=SCREEN_HEIGHT/2-(Tank1_y-12.5f)*1.5f*SCREEN_WIDTH/SCREEN_WIDTH_STANDARD;//�_�l�Z�J��my
				  		 if(y2>tkposition_y)
    					 {
    						//�p�⬶�ު����ਤ��
    						 pAngle=-180+(float)Math.toDegrees( Math.asin((x2-tkposition_x)/Math.sqrt((x2-tkposition_x)*(x2-tkposition_x)+(y2-tkposition_y)*(y2-tkposition_y))));
    					 }
    					 else
    					 {
    					   //�p�⬶�ު����ਤ��
    					   pAngle=-(float)Math.toDegrees( Math.asin((x2-tkposition_x)/Math.sqrt((x2-tkposition_x)*(x2-tkposition_x)+(y2-tkposition_y)*(y2-tkposition_y))));
    					 }
    					synchronized(msv.gdMain.dataLock) 	//�N�D�����W
    					{
    						msv.gdMain.maintkfp[0]=fireflag;
    						msv.gdMain.maintkfp[1]=pAngle;
    						msv.activity.sendMessage(fireflag+"<#>"+pAngle+"<#>");
    						synchronized(msv.gdMainDraw.dataLock) //�Nø������W
    						{
    							msv.gdMainDraw.maintkfp[0]=fireflag;
    							msv.gdMainDraw.maintkfp[1]=pAngle;
    						}
    					}
    					break;
    			}
    		}
    		else
    		{
    			 scale_x=0;
    		     scale_y=0;
    		     
    		}
    		try 
    		{
				Thread.sleep(10);
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		 }
	 }
}
