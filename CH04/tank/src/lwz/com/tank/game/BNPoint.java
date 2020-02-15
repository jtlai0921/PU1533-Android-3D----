package lwz.com.tank.game;
import static lwz.com.tank.activity.Constant.*;

public class BNPoint {
	   //Ĳ���IX�BY�y��
	   float x;
	   float y;
	   int countl;
       //�Z�J�����ਤ��
       float vAngle=0;
       //���ު���������
       float pAngle=0;
       //�Z�J�C�طh�����B�|
       float hd_x=0;
       float hd_y=0;
       //�n�줤�߶��I�������q
       float scale_x=0;
       float scale_y=0;
 		//�n�줤���I�����y�ФΥb�|
 		public float INIT_x=(115+ssr.lucX)*ssr.ratio;
 		public float INIT_y=(425+ssr.lucY)*ssr.ratio;
 		public float INIT_R=100*(SCREEN_WIDTH-ssr.lucX*ssr.ratio*2)/SCREEN_WIDTH_STANDARD;
       //Ĳ���I���ЧӦ�
       public  int touchflag=0;
       //�Z�J�_�l�������y��
  	   float tkposition_x=480f;
  	   float tkposition_y=438.75f;
  	   
	   public BNPoint(float x,float y,int countl)
	   {
			//�P�_Ĳ���I�O�_�b�n��ϰ줺
		   if(Math.sqrt((x-INIT_x)*(x-INIT_x)+(y-INIT_y)*(y-INIT_y))<INIT_R)
		   {
			   this.x=x;
			   this.y=y;
			   this.countl=countl;
				//Ĳ���ЧӦ�m��1,�����n��Ĳ���I
			   touchflag=1;
		   }
		   else
		   { 
			   this.x=x;
			   this.y=y;
			   this.countl=countl;
			   //Ĳ���ЧӦ�m��1,�����o��Ĳ���I
			   touchflag=2;
		   }
	   }
	   public void setLocation(float x,float y)//move�ƥ�U�ҭק諸�y��
	   {
		   //�Y�n��Ĳ���I�h��
		   if(touchflag==1)
		   {
			   //�P�_�ثe�I�y�ЬO�_�b�n��Ĳ���ϰ줺
			   if(Math.sqrt((x-INIT_x)*(x-INIT_x)+(y-INIT_y)*(y-INIT_y))<INIT_R)
			   {
				   this.x=x;
				   this.y=y;
				   //Ĳ���ЧӦ�m��1,�����n��Ĳ���I
				   touchflag=1;
			   }
			   else
			   {
				   //�ثe�I�y�Ш�n�줤���I���Z��
				   float tempR=(float) Math.sqrt((x-INIT_x)*(x-INIT_x)+(INIT_y-y)*(INIT_y-y));
				   //�N�ثe�I�y�бj���ର�n��Ĳ���d����t�y��
				   this.x=INIT_x+(x-INIT_x)*INIT_R/tempR;
				   this.y=INIT_y+(y-INIT_y)*INIT_R/tempR;
				   //Ĳ���ЧӦ�m��1,�����n��Ĳ���I
				   touchflag=1;
			   }
		   }
		   if(touchflag==2)
		   {
			   this.x=x;
			   this.y=y;
			 //Ĳ���ЧӦ�m��2,�����o��Ĳ���I
			   touchflag=2;
		   }
	   }
	//���oĲ�����ЧӦ�
	public int gettouchflag()
	{
		return touchflag;
	}
	//���o�ثeĲ���I��X�y��
	public float getx()
	{
		return x;
	}
	//���o�ثeĲ���I��X�y��
	public float gety()
	{
		return y;
	}
}
