package tstc.fxq.main;
import tstc.fxq.constants.Constant;
import tstc.fxq.utils.PicLoadUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;
/**
 * 
 *  ���U�ɭ�
 *
 */
public class HelpView extends SurfaceView implements SurfaceHolder.Callback{
	MyActivity activity;//activity���Ѧ�
	Paint paint;//�e���Ѧ�
	//�Ҧ��Ϥ���id
	private final int[] bmpIds = 
	{
			R.drawable.help0, R.drawable.help1, R.drawable.help2,
			R.drawable.help3, R.drawable.help4, R.drawable.help5, 
			R.drawable.help6,
	};
	Bitmap[] helpBmps = new Bitmap[bmpIds.length];
	Bitmap pointerBmp;
	int currIndex;//�ثe���U�Ϥ��b�}�C��������
	float currY;//�ثe�Ϥ���y��m
	float bmpHeight;//��Ϥ�������
	float pointerBmpWidth;//���йϤ�������
	float pointerX = 700f;
	float pointerY = 5f;
	//����Ĳ�N���ܼ�
	private boolean movingFlag = false;//���b�h�����ЧӦ�
	float mPreviousY;
	float dy; 	
	int fullTime=0;//�O���q�}�l��ثe���ɶ�
	long startTime;//�}�l�ɶ�
	DrawThread drawThread;//ø�������Ѧ�	
	
	//�����Хܪ��ܼ�
	private boolean touchScreenFlag = false;//�O�_���bĲ���̪��ЧӦ�
	private Bitmap[] markBmps = new Bitmap[5];//�ХܹϤ��}�C�A�@��5�عϤ�
	private final int[] markBmpIds = {R.drawable.mark0, R.drawable.mark1, R.drawable.mark2, R.drawable.mark3, R.drawable.mark4,};
	//���s�Ϥ���id
	private final int BTN_ID = 0;
	private final int BAR_ID = 1;
	private final int ICON_ID = 2;
	private final int TIME_ID = 3;
	private final int MAP_ID = 4;
	//�s��ХܰT����3D�}�C�A�ĤG�������@���A��3D�T�Ӷq���t�N�G�nø��Ϫ����ޡA�Ϫ�x�y�СAy�y��
	private final int[][][] markPos = 
	{
		{//��1��
			//���μХ�
		},
		{//��2��
			{
				BTN_ID, 162, 421 //�y���V�վ���s--��				
			},
			{
				BTN_ID, 259, 421 //�y���V�վ���s--�k		
			},
			{
				BAR_ID, 21, 272 //�y���V�վ���s--�k		
			},
		},
		{//��3��
			{
				BTN_ID, 86, 422 //�Y����s--��
			},
			{
				BTN_ID, 121, 422 //�Y����s--�k
			},
			{
				BTN_ID, 20, 422 //���y���s
			},
		},
		{//��4��
			{
				BTN_ID, 325, 422 //M���s
			},
			{
				MAP_ID, 320, 205 //�g�A��
			},
		},
		{//��5��
			{
				BTN_ID, 396, 422 //���פ������s
			},
		},
		{//��6��
			{
				ICON_ID, 23, 206 //�i�y�ϥ�
			},
		},
		{//��7��
			{
				TIME_ID, 199, 211 //�˭p��
			},
		},
	};
	//���toast��handler
	public Handler hd=new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			// �I�s�����O�B�z
			super.handleMessage(msg);
			Toast.makeText(activity, "�W�U�ٰʿù�½��", Toast.LENGTH_LONG).show();
		}
	};
   
	public HelpView(MyActivity activity) {
		super(activity);
		this.activity=activity;
		//��o�J�I�ó]�w���iĲ��
		this.requestFocus();
        this.setFocusableInTouchMode(true);
		getHolder().addCallback(this);//�n���^�ձ��f
		startTime=System.currentTimeMillis();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas); 
		canvas.drawColor(Color.GRAY);
		//ø��I��
		canvas.drawBitmap(helpBmps[currIndex], 0, currY*Constant.hRatio, paint);
		if(currIndex < bmpIds.length-1){
			canvas.drawBitmap(helpBmps[currIndex+1], 0, (currY+bmpHeight)*Constant.hRatio, paint);			
		}
		if(currIndex > 0){
			canvas.drawBitmap(helpBmps[currIndex-1], 0, (currY-bmpHeight)*Constant.hRatio, paint);
		}
		
		//ø�����U�ɭ��{�ʪ��ϥ�
		long currentTime=System.currentTimeMillis();//�O���ثe�ɶ�
		fullTime=(int) ((currentTime-startTime));//�O���`�ɶ�	
		//�N1���������A�b0.7��ø��A0.3����ø��
		if((fullTime/100)%10 < 7) {
			//ø��½�����ܹϥ�
			drawPointer(canvas, paint);
			//�Y�G�S���h���A�Aø��Х�
			if(!(movingFlag || touchScreenFlag)){
				drawMarks(canvas, paint);
			}
		}
	}
	
	//ø��½�����ܰw����k
	public void drawPointer(Canvas canvas, Paint paint){
		paint.setAlpha(200);
		canvas.drawBitmap(pointerBmp, pointerX*Constant.wRatio, pointerY*Constant.hRatio, paint);
		paint.setAlpha(255);
	}
	
	//ø��Хܫ��s����k
	public void drawMarks(Canvas canvas, Paint paint){
		for(int i=0; i<markPos[currIndex].length; i++){
			canvas.drawBitmap(
					markBmps[markPos[currIndex][i][0]], 
					markPos[currIndex][i][1] * Constant.wRatio, 
					markPos[currIndex][i][2] * Constant.hRatio, 
					paint
					);
		}		
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//�Y�G���b�h���A���iĲ��
		if(movingFlag){
			touchScreenFlag = false;
			return false;
		}
		float x = event.getX();
		float y = event.getY();
    	switch(event.getAction())
    	{
    	case MotionEvent.ACTION_DOWN:
    		touchScreenFlag = true;
    		
    		if(x>pointerX*Constant.wRatio && x<pointerX*Constant.wRatio + pointerBmp.getWidth() &&
    				(y>pointerY*Constant.hRatio && y<pointerY*Constant.hRatio + pointerBmp.getHeight())
    		){
    			hd.sendEmptyMessage(1);
    		}
    		mPreviousY = y;//�O��Ĳ����m
    		break;
    	case MotionEvent.ACTION_MOVE:
        	dy = y - mPreviousY;//�p��Ĳ����Y�첾	
        	//�Y�G�V�e½��
        	if(dy > 0){
        		currY = dy;
        	}
        	//�Y�G�V��½��
        	else {
            	currY = dy;
        	}
    		break;
    	case MotionEvent.ACTION_UP: 
        	touchScreenFlag = false;
        	
    		dy = y - mPreviousY;//�p��Ĳ����Y�첾
    		//�}�Ұ�����A����h���ʵe
    		new Thread()
    		{
    			private final int timeSpan = 2;
    			@Override
    			public void run()
    			{
    				//�Хܤ��iĲ��
    				movingFlag = true;    				
    				final float SPAN = 40;
    				float yFrom = currY;
    				float ySpan = 0;
    				float yTo = 0;
    				int totalSteps = 0;
    	    		
    	    		//�Y�����e½��
    	    		if(dy >0){
    	    			//�i�H½
    	    			if(Math.abs(dy) > Constant.SCREEN_HEIGHT/2.0f && currIndex > 0){
    	    				ySpan = SPAN;
    	    				yTo = Constant.screenHeightTest;
    	    				totalSteps = (int) (Math.abs(yFrom-yTo)/Math.abs(ySpan));  
    	    			}
    	    			//���i�H½
    	    			else{
    	    				ySpan = -SPAN;
    	    				yTo = 0;
    	    				totalSteps = (int) (Math.abs(yFrom-yTo)/Math.abs(ySpan));  
    	    			}
    	    		}
    	    		//�Y���V��½��
    	    		else if(dy <0){
    	    			//�i�H½
    	    			if(Math.abs(dy) > Constant.SCREEN_HEIGHT/2.0f && currIndex < bmpIds.length-1){
    	    				ySpan = -SPAN;
    	    				yTo = -Constant.screenHeightTest;
    	    				totalSteps = (int) (Math.abs(yFrom-yTo)/Math.abs(ySpan));  
    	    			}
    	    			//���i�H½
    	    			else{
    	    				ySpan = SPAN;
    	    				yTo = 0;
    	    				totalSteps = (int) (Math.abs(yFrom-yTo)/Math.abs(ySpan));  
    	    			}    	    		
    	    		}
    				for(int i=0; i<totalSteps+1; i++){
    					//���ܫ��s����m
    					currY = yFrom + i*ySpan;
    					//��ø�ɭ�
    					repaint();
    					try{
    		            	Thread.sleep(timeSpan);//�ίv���w�@���
    		            }
    		            catch(Exception e){
    		            	e.printStackTrace();//�C�L����|�T��
    		            }
    				}
    				
    	    		//�Y�����e½���A�B�j��]�w��
    	    		if(dy >0 && Math.abs(dy) > Constant.SCREEN_HEIGHT/2.0f){
    	    			if(currIndex > 0){
    	        			currIndex -= 1;
    	    			}
    	    		}
    	    		//�Y���V��½���A�B�j��]�w��
    	    		else if(dy <0 && Math.abs(dy) > Constant.SCREEN_HEIGHT/2.0f){
    	    			if(currIndex < bmpIds.length-1){
    	        			currIndex += 1;                      
    	    			}
    	    		}
    	    		currY = 0;
    				//�Хܥi�HĲ��
    				movingFlag = false;
    			}
    		}.start();
    		break;    	
    	}
		return true;
	}	
	//���sø���k
    public void repaint()
	{
		Canvas canvas=this.getHolder().lockCanvas();
		try
		{
			synchronized(canvas)
			{
				onDraw(canvas);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(canvas!=null)
			{
				this.getHolder().unlockCanvasAndPost(canvas);
			}
		}
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {		
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder){
		paint=new Paint();//�إߵe��
		paint.setAntiAlias(true);//�}�ҧܿ���
		initBitmap();//�_�l���I�}�ϸ귽
		currIndex = 0;
		drawThread=new DrawThread(this);//�إ�ø������'
		drawThread.start();
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		drawThread.setFlag(false);
	}
	//���J�Ϥ�����k
	public void initBitmap(){
		for(int i=0 ;i<helpBmps.length; i++){
			helpBmps[i]=BitmapFactory.decodeResource(this.getResources(), bmpIds[i]);			
		}
		pointerBmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.pointer);		
		for(int i=0 ;i<markBmps.length; i++){
			markBmps[i]=BitmapFactory.decodeResource(this.getResources(), markBmpIds[i]);			
		}
		
		//��Ϥ�����
		bmpHeight = helpBmps[0].getHeight();
		pointerBmpWidth = pointerBmp.getWidth();
		//�A����
		for(int i=0 ;i<helpBmps.length; i++){
			helpBmps[i]=PicLoadUtil.scaleToFitFullScreen(helpBmps[i], Constant.wRatio, Constant.hRatio);
		}
		pointerBmp=PicLoadUtil.scaleToFitFullScreen(pointerBmp, Constant.wRatio, Constant.hRatio);
		for(int i=0 ;i<markBmps.length; i++){
			markBmps[i]=PicLoadUtil.scaleToFitFullScreen(markBmps[i], Constant.wRatio, Constant.hRatio);
		}
	}	
}

class DrawThread extends Thread{
	private boolean flag = true;	
	private int sleepSpan = 20;
	HelpView fatherView;
	SurfaceHolder surfaceHolder;
	public DrawThread(HelpView helpView){
		this.fatherView = helpView;
		this.surfaceHolder = helpView.getHolder();
	}
	public void run(){
        while (this.flag)
        {
            fatherView.repaint();
            try{
            	Thread.sleep(sleepSpan);//�ίv���w�@���
            }
            catch(Exception e){
            	e.printStackTrace();//�C�L����|�T��
            }
        }
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}
