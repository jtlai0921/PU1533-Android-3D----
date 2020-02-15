package lwz.com.tank.activity;

import lwz.com.tank.util.ScreenScaleResult;
import lwz.com.tank.util.ScreenScaleUtil;
import android.graphics.Bitmap;
import android.graphics.Matrix;

public class Constant {
	public static final float[][]lowWallmapdata=
		{
			{-6.5f,4.5f,0.0f,13.0f,0.5f,0.3f},{-6.5f,4.0f,0.0f,0.5f,8.0f,0.3f},{-6.5f,-4.0f,0.0f,13.0f,0.5f,0.3f},{6.0f,4.0f,0.0f,0.5f,8.0f,0.3f},//�~������
			{-4.5f,2.5f,0.0f,2.5f,0.4f,0.2f},{2.0f,2.5f,0.0f,2.5f,0.4f,0.2f},
			{-4.0f,0.2f,0.0f,8.0f,0.4f,0.2f},{-0.2f,2.0f,0.0f,0.4f,4.0f,0.2f},
			{-4.5f,-2.1f,0.0f,2.5f,0.4f,0.2f},{2.0f,-2.1f,0.0f,2.5f,0.4f,0.2f}
		};
	public static final float[][]middleWallmapdata=
		{
			{-6.4f,4.4f,0.3f,12.8f,0.3f,0.3f},{-6.4f,4.1f,0.3f,0.3f,8.2f,0.3f},{-6.4f,-4.1f,0.3f,12.8f,0.3f,0.3f},{6.1f,4.1f,0.3f,0.3f,8.2f,0.3f},//�~������
			{-4.4f,2.4f,0.2f,2.3f,0.2f,0.2f},{2.1f,2.4f,0.2f,2.3f,0.2f,0.2f},
			{-3.9f,0.1f,0.2f,7.8f,0.2f,0.2f},{-0.1f,1.9f,0.2f,0.2f,3.8f,0.2f},
			{-4.4f,-2.2f,0.2f,2.3f,0.2f,0.2f},{2.1f,-2.2f,0.2f,2.3f,0.2f,0.2f}
		};
	public static final float[][]highWallmapdata=
		{
			{-6.4f,4.4f,0.6f,0.3f,0.3f,0.2f},{-5.0f,4.4f,0.6f,0.4f,0.3f,0.2f},{-3.4f,4.4f,0.6f,0.4f,0.3f,0.2f},{-1.8f,4.4f,0.6f,0.4f,0.3f,0.2f},{-0.2f,4.4f,0.6f,0.4f,0.3f,0.2f},{1.4f,4.4f,0.6f,0.4f,0.3f,0.2f},{3.0f,4.4f,0.6f,0.4f,0.3f,0.2f},{4.6f,4.4f,0.6f,0.4f,0.3f,0.2f},{6.1f,4.4f,0.6f,0.3f,0.3f,0.2f},//�W��
			{-6.4f,3.4f,0.6f,0.3f,0.4f,0.2f},{-6.4f,1.8f,0.6f,0.3f,0.4f,0.2f},{-6.4f,0.2f,0.6f,0.3f,0.4f,0.2f},{-6.4f,-1.4f,0.6f,0.3f,0.4f,0.2f},{-6.4f,-3.0f,0.6f,0.3f,0.4f,0.2f},//����
			{-6.4f,-4.1f,0.6f,0.3f,0.3f,0.2f},{-5.0f,-4.1f,0.6f,0.4f,0.3f,0.2f},{-3.4f,-4.1f,0.6f,0.4f,0.3f,0.2f},{-1.8f,-4.1f,0.6f,0.4f,0.3f,0.2f},{-0.2f,-4.1f,0.6f,0.4f,0.3f,0.2f},{1.4f,-4.1f,0.6f,0.4f,0.3f,0.2f},{3.0f,-4.1f,0.6f,0.4f,0.3f,0.2f},{4.6f,-4.1f,0.6f,0.4f,0.3f,0.2f},{6.1f,-4.1f,0.6f,0.3f,0.3f,0.2f},//�U��
			{6.0f,3.4f,0.6f,0.3f,0.4f,0.2f},{6.0f,1.8f,0.6f,0.3f,0.4f,0.2f},{6.0f,0.2f,0.6f,0.3f,0.4f,0.2f},{6.0f,-1.4f,0.6f,0.3f,0.4f,0.2f},{6.0f,-3.0f,0.6f,0.3f,0.4f,0.2f},//�k��
			{-4.4f,2.4f,0.4f,0.2f,0.2f,0.2f},{-3.4f,2.4f,0.4f,0.3f,0.2f,0.2f},{-2.3f,2.4f,0.4f,0.2f,0.2f,0.2f},
			{2.1f,2.4f,0.4f,0.2f,0.2f,0.2f},{3.1f,2.4f,0.4f,0.3f,0.2f,0.2f},{4.2f,2.4f,0.4f,0.2f,0.2f,0.2f},
			{-3.9f,0.1f,0.4f,0.2f,0.2f,0.2f},{-2.7f,0.1f,0.4f,0.3f,0.2f,0.2f},{-1.4f,0.1f,0.4f,0.3f,0.2f,0.2f},{-0.1f,0.1f,0.4f,0.2f,0.2f,0.5f},{1.1f,0.1f,0.4f,0.3f,0.2f,0.2f},{2.4f,0.1f,0.4f,0.3f,0.2f,0.2f},{3.7f,0.1f,0.4f,0.2f,0.2f,0.2f},
			{-0.1f,1.9f,0.4f,0.2f,0.2f,0.2f},{-0.1f,1.0f,0.4f,0.2f,0.2f,0.2f},{-0.1f,-0.8f,0.4f,0.2f,0.2f,0.2f},{-0.1f,-1.7f,0.4f,0.2f,0.2f,0.2f},
			{-4.4f,-2.2f,0.4f,0.2f,0.2f,0.2f},{-3.4f,-2.2f,0.4f,0.3f,0.2f,0.2f},{-2.3f,-2.2f,0.4f,0.2f,0.2f,0.2f},
			{2.1f,-2.2f,0.4f,0.2f,0.2f,0.2f},{3.1f,-2.2f,0.4f,0.3f,0.2f,0.2f},{4.2f,-2.2f,0.4f,0.2f,0.2f,0.2f}
		};
	
	public static float SCREEN_WIDTH_STANDARD=960;  //�ù��Э�e��	
	public static float SCREEN_HEIGHT_STANDARD=540;  //�ù��Э㰪��
	public static float SCREEN_WIDTH;  //�ù��e��	
	public static float SCREEN_HEIGHT;  //�ù�����
	public static float RATIO=SCREEN_WIDTH_STANDARD/SCREEN_HEIGHT_STANDARD;//�ù����e��

	//�۾A���ù��u�����O����
	public static ScreenScaleResult ssr;
	public static boolean XSFLAG=false;//����scaleCL()�ЧӦ�
	public static void scaleCL()
	{
		if(XSFLAG) return;        
        ssr=ScreenScaleUtil.calScale(SCREEN_WIDTH, SCREEN_HEIGHT);
        
        XSFLAG=true;
	}
	
	//public static final int Wall_UNIT_SIZE=55;	
	public static final int Wall_UNIT_SIZE=40;	
	public static final float UNIT_SIZE = 4;
	
	//��Service����Handler�ǰe���T�����A
    public static final int MSG_READ = 2;
    public static final int MSG_DEVICE_NAME = 4;
	// �qService����Handler�o�Ӫ��D��W
    public static final String DEVICE_NAME = "device_name"; 
	    
    public static float TIME_SPAN=0.1f;//���u�ͦs�ɶ��W����
    public static float BULLET_V=50f;//���X�{���`�t��
    //�Z�J�C�طh�����B�|
//    public static float MOVE_SPAN=2.5f;
    public static float MOVE_SPAN=4f;
    //�Y����
    public static float ratio_width=1;
    public static float ratio_height=1;
    //�Y�񯾲z
	public static Bitmap scaleToFit(Bitmap bm,float width_Ratio,float height_Ratio)
	{		
    	int width = bm.getWidth(); 							//�Ϥ��e��
    	int height = bm.getHeight();							//�Ϥ�����
    	Matrix matrix = new Matrix(); 
    	matrix.postScale((float)width_Ratio, (float)height_Ratio);				//�Ϥ�����ҩԻ�����Ӫ�fblRatio��
    	Bitmap bmResult = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);//�ŧi�I�}��        	
    	return bmResult;									//�Ǧ^�Q�Y�񪺹Ϥ�
    }
//	public static long[] COLLISION_SOUND_PATTERN={0l,280};//�Ĥ@�ӰѼƬ�����_�ʤ�k��h���ɶ��}�l�_��
//	//�ĤG�ӰѼƬ��_�ʫ���ɶ��A��ӰѼƥ�����long���A
	public static long[] COLLISION_SOUND_PATTERN={0l,0};//�Ĥ@�ӰѼƬ�����_�ʤ�k��h���ɶ��}�l�_��
	//�ĤG�ӰѼƬ��_�ʫ���ɶ��A��ӰѼƥ�����long���A
		
		
		
		
}
