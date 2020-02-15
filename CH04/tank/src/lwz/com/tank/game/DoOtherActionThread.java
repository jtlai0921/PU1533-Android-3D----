package lwz.com.tank.game;

import static lwz.com.tank.activity.Constant.*;
import java.util.Queue;

public class DoOtherActionThread extends Thread
{
	public boolean workFlag=true;		//������O�_�`���u�@�ЧӦ�
	OtherSurfaceView osv;
	Queue<Action> aq;					//�ʧ@��C
	
    //�Z�J�����ਤ��
    float vAngle=0;
    //���ު���������
    float pAngle=0;
    //�Z�J�C�طh�����B�|
    float hd_x=0;
    float hd_y=0;
    //�n�줤�߶��I�������q
    static float scale_x=0;
    static float scale_y=0;
    //�Z�J1�h�����B�|
  	static float bx=0;
  	static float by=0;
  	float tkwz_x=0;
  	float tkwz_y=0;
	//�Z�J2���_�l��m
	public  float Tank2_x=0;
	public  float Tank2_y=100;
	//�n�줤���I�����y�ФΥb�|
	public  float INIT_x=(115+ssr.lucX)*ssr.ratio;
	public  float INIT_y=(425+ssr.lucY)*ssr.ratio;
	public  float INIT_R=100*(SCREEN_WIDTH-ssr.lucX*ssr.ratio*2)/SCREEN_WIDTH_STANDARD;
	//�Z�J�_�l�������y��
    float tkposition_x=480f;
    float tkposition_y=438.75f;
    
    public static float fireflag=0;
    
    public DoOtherActionThread(OtherSurfaceView osv)
    {
    	this.osv=osv;		//����View
    	this.aq=osv.aq;		//�ʧ@��C
    }
    @Override
	public void run()
	{
		 while(workFlag)
		 {
			Action ac=null;					//�ʧ@�Ѧ�
    		synchronized(osv.aqLock)		//�ʧ@��C��
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
    						if(bx>lowWallmapdata[i][0]*Wall_UNIT_SIZE-20f-Tank2_x&&bx<(lowWallmapdata[i][0]+lowWallmapdata[i][3])*Wall_UNIT_SIZE+20f-Tank2_x&&by<lowWallmapdata[i][1]*Wall_UNIT_SIZE+20f-Tank2_y&&by>(lowWallmapdata[i][1]-lowWallmapdata[i][4])*Wall_UNIT_SIZE-20f-Tank2_y)
    						{
    							if(bx-hd_x>lowWallmapdata[i][0]*Wall_UNIT_SIZE-20f-Tank2_x&&bx-hd_x<(lowWallmapdata[i][0]+lowWallmapdata[i][3])*Wall_UNIT_SIZE+20f-Tank2_x)	
    							{
									by=by-hd_y;
    							}
    							else
    							{
									bx=bx-hd_x;
    							}
    						}
    					}
    					tkwz_x=bx+Tank2_x;
    					tkwz_y=by+Tank2_y;
    					synchronized(osv.gdMain.dataLock) 	//�N�D�����W
    					{
    						osv.gdMain.followtkwz[0]=tkwz_x;
    						osv.gdMain.followtkwz[1]=tkwz_y;
    						osv.gdMain.followtkwz[2]=vAngle;
    						osv.gdMain.followtkwz[3]=scale_x;
    						osv.gdMain.followtkwz[4]=scale_y;
    						osv.activity.sendMessage(tkwz_x+"<#>"+tkwz_y+"<#>"+vAngle+"<#>");
    						synchronized(osv.gdMainDraw.dataLock) //�Nø������W
    						{
    							osv.gdMainDraw.followtkwz[0]=tkwz_x;
    							osv.gdMainDraw.followtkwz[1]=tkwz_y;
    							osv.gdMainDraw.followtkwz[2]=vAngle;
    							osv.gdMainDraw.followtkwz[3]=scale_x;
    							osv.gdMainDraw.followtkwz[4]=scale_y;
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
//				  		 tkposition_x=(Tank2_x)*1.5f*SCREEN_WIDTH/SCREEN_WIDTH_STANDARD+SCREEN_WIDTH/2;//�_�l�Z�J��mx
//				  		 tkposition_y=SCREEN_HEIGHT/2-(Tank2_y-12.5f)*1.5f*SCREEN_WIDTH/SCREEN_WIDTH_STANDARD;//�_�l�Z�J��my
				  		System.out.println(tkposition_x+"====================="+tkposition_y);
				  		 
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
    					synchronized(osv.gdMain.dataLock) 	//�N�D�����W
    					{
    						osv.gdMain.followtkfp[0]=fireflag;
    						osv.gdMain.followtkfp[1]=pAngle;
    						osv.activity.sendMessage(fireflag+"<#>"+pAngle+"<#>");
    						synchronized(osv.gdMainDraw.dataLock) //�Nø������W
    						{
    							osv.gdMainDraw.followtkfp[0]=fireflag;
    							osv.gdMainDraw.followtkfp[1]=pAngle;
    						}
    					}
    					break;
    			}
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
