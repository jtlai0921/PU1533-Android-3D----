package tstc.fxq.threads;

import tstc.fxq.constants.Constant;
import tstc.fxq.main.MySurfaceView;

/*
 * �h����v������������O
 */
public class MoveCameraThread extends Thread{
	MySurfaceView mv;
	private int sleepSpan=5;
	//�ثe�h����v���������
	public static MoveCameraThread currThread;
	//��v���h�����_�l�I
	public static float xFrom;
	public static float zFrom;
	//��v���h�����פ��I
	public static float xTo;
	public static float zTo;
	
	//��v���C���h�����B�i
	private float moveSpan = 0.05f;//��ڷh���B�i
	private float moveSpanX;//x��V�h���B�i
	private float moveSpanZ;//z��V�h���B�i
	//��v���h���`����
	private int totalSteps;
	
	public MoveCameraThread
	(
			MySurfaceView mv
	)
	{
		this.mv = mv;
		
		//�_�I����I���V�q
		float vecX = MoveCameraThread.xTo-MoveCameraThread.xFrom;
		float vecZ = MoveCameraThread.zTo-MoveCameraThread.zFrom;
		//�_�I�M���I���Z��
		float dis = (float) Math.sqrt(vecX*vecX + vecZ*vecZ);
		//�p��x�By��V�W�h���B�i
		moveSpanX = vecX/dis*moveSpan;
		moveSpanZ = vecZ/dis*moveSpan;
		//��v���h���`����
		totalSteps = (int) (dis/moveSpan);
	}
	@Override
	public void run()
	{
		for(int i=0; i<totalSteps+1; i++)
		{
			//�p��ثe�ت��I��m�A�ó]�w��v������m
			mv.setCameraPostion(xFrom + i*moveSpanX, zFrom + i*moveSpanZ);

			try{
            	Thread.sleep(sleepSpan);//�ίv���w�@���
            }
            catch(Exception e){
            	e.printStackTrace();//�C�L����|�T��
            }
		}
		
		//�ʵe���񧹫�A�������p�ЧӦ�	
		Constant.cueFlag=true;//ø��y��ЧӦ�
        //�]�w��v������m��ղy�B
		mv.setCameraPostion(MySurfaceView.ballAl.get(0).xOffset, MySurfaceView.ballAl.get(0).zOffset);
	}
}
