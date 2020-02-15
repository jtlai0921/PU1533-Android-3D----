package tstc.fxq.main;
import tstc.fxq.constants.Constant;
import tstc.fxq.utils.DBUtil;
import tstc.fxq.utils.PicLoadUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/**
 * 
 * �n���]�ɭ�
 *
 */
public class HighScoreView extends SurfaceView implements SurfaceHolder.Callback{
	MyActivity activity;//activity���Ѧ�
	Paint paint;//�e���Ѧ�
	DrawThread drawThread;//ø�������Ѧ�		
	Bitmap bgBitmap;//�I���Ϥ�	
	Bitmap bmp;//��r���Ϥ�
	Bitmap defenBitmap;
	Bitmap riqiBitmap;
	Bitmap[] numberBitmaps;//�Ʀr�Ϥ�	
	Bitmap gangBitmap;//�Ÿ�"-"�������Ϥ�
	String queryResultStr;//�d�߸�Ʈw�����G
	String[] splitResultStrs;//�N�d�ߵ��G�����᪺�}�C
	private int numberWidth;//�Ʀr�Ϥ����e��
	private int numberHeight;//�Ʀr�Ϥ����e��
	private int posFrom=-1;//�d�ߪ��}�l��m
	private int length=5;//�d�ߪ��̤j�O���Ӽ�
	int downY=0;//���U�M��_��y�y��
	int upY=0;
	public HighScoreView(MyActivity activity) {
		super(activity);
		this.activity=activity;
		//��o�J�I�ó]�w���iĲ��
		this.requestFocus();
        this.setFocusableInTouchMode(true);
		getHolder().addCallback(this);//�n���^�ձ��f	
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//ø��I��
		canvas.drawColor(Color.GRAY);
		canvas.drawBitmap(bgBitmap, 0, 0, paint);
		//ø���r+Constant.X_OFFSET,  +Constant.Y_OFFSET
		canvas.drawBitmap(bmp, 95*Constant.wRatio, 148*Constant.hRatio, paint);
		canvas.drawBitmap(riqiBitmap, 230*Constant.wRatio, 180*Constant.hRatio, paint);
		canvas.drawBitmap(defenBitmap, 470*Constant.wRatio, 180*Constant.hRatio, paint);
		//ø��o���M�ɶ�
		float x;
		float y;
		for(int i=0;i<splitResultStrs.length;i++)
		{
			if(i%2==0)//�p��o������m
			{
				x=570;				
			}
			else//�p��ɶ�����m
			{
				x=410;
			}
			y=180+(numberHeight+10)*(i/2+1);
			//ø��r��
			drawDateBitmap(splitResultStrs[i], x*Constant.wRatio, y*Constant.hRatio, canvas,paint);
		}
	}
	//�e����Ϥ�����k
	public void drawDateBitmap(String numberStr,float endX,float endY,Canvas canvas,Paint paint)
	{
		for(int i=0;i<numberStr.length();i++)
		{
			char c=numberStr.charAt(i);
			if(c=='-')
			{
				canvas.drawBitmap(gangBitmap,(endX-numberWidth*(numberStr.length()-i))*Constant.wRatio, endY*Constant.hRatio, paint);
			}
			else
			{
				canvas.drawBitmap
				(
						numberBitmaps[c-'0'], 
						(endX-numberWidth*(numberStr.length()-i))*Constant.wRatio, 
						endY*Constant.hRatio, 
						paint
				);
			}			
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int y = (int) event.getY();		
    	switch(event.getAction())
    	{
    	case MotionEvent.ACTION_DOWN:
    		downY=y;
    		break;
    	case MotionEvent.ACTION_UP:
    		upY=y;    		
        	if(Math.abs(downY-upY)<20)//�b��Ƚd�򤺡A��½��
        	{
        		return true;
        	}
        	else if(downY<upY)//���U��
        	{	
        		//�Y�G�٨�̫e���A���i�A��
        		if(this.posFrom-this.length>=-1)
        		{
        			this.posFrom-=this.length;        			
        		}
        	}
        	else//���W��
        	{	
        		//�Y�G�٨�̫᭶�A���i�A��
        		if(this.posFrom+this.length<DBUtil.getRowCount(Constant.POS_INDEX)-1)
        		{
        			this.posFrom+=this.length;        			
        		}
        	}
        	queryResultStr=DBUtil.query(Constant.POS_INDEX, posFrom, length);//�o���Ʈw�������
			splitResultStrs=queryResultStr.split("/", 0);//��"/"�����A�B�˱��Ŧr��
    		break;    	
    	}
		return true;
	}	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder){
		paint=new Paint();//�إߵe��
		paint.setAntiAlias(true);//�}�ҧܿ���	
		createAllThreads();//�إߩҦ������
		initBitmap();//�_�l���I�}�ϸ귽	
		numberWidth=numberBitmaps[0].getWidth()+3;//�o��Ʀr�Ϥ����e��
		numberHeight = numberBitmaps[0].getHeight();
		posFrom=-1;//�d�ߪ��}�l��m�m-1			
		queryResultStr = DBUtil.query(Constant.POS_INDEX, posFrom, length);//�o���Ʈw�������
		splitResultStrs = queryResultStr.split("/", 0);//��"/"�����A�B�˱��Ŧr��		
		//�ϹϤ��۾A����
		changeBmpToFitScreen();
		startAllThreads();//�}�ҩҦ������
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		  boolean retry = true;
	        stopAllThreads();
	        while (retry) {
	            try {
	            	drawThread.join();
	                retry = false;
	            } 
	            catch (InterruptedException e) {e.printStackTrace();}//���_�a�`���A�����ذ��������
	        }
	}
	//�N�Ϥ����J
	public void initBitmap(){
		bmp=BitmapFactory.decodeResource(this.getResources(), R.drawable.bmp);	
		bgBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.bg);
		numberBitmaps=new Bitmap[]{
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number0),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number1),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number2),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number3),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number4),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number5),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number6),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number7),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number8),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number9),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number0),
		};
		gangBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.gang);
		defenBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.defen);
		riqiBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.riqi);
		
	}
	void changeBmpToFitScreen(){
		//�A����
		bmp=PicLoadUtil.scaleToFitFullScreen(bmp, Constant.wRatio, Constant.hRatio);
		gangBitmap=PicLoadUtil.scaleToFitFullScreen(gangBitmap, Constant.wRatio, Constant.hRatio);
		riqiBitmap=PicLoadUtil.scaleToFitFullScreen(riqiBitmap, Constant.wRatio, Constant.hRatio);
		defenBitmap=PicLoadUtil.scaleToFitFullScreen(defenBitmap, Constant.wRatio, Constant.hRatio);				
		for(int i=0;i<numberBitmaps.length;i++){
			numberBitmaps[i]=PicLoadUtil.scaleToFitFullScreen(numberBitmaps[i], Constant.wRatio, Constant.hRatio);
		}
		bgBitmap=PicLoadUtil.scaleToFitFullScreen(bgBitmap, Constant.wRatio, Constant.hRatio);
	}
	void createAllThreads()
	{
		drawThread=new DrawThread(this);//�إ�ø������		
	}
	void startAllThreads()
	{
		drawThread.setFlag(true);     
		drawThread.start();
	}
	void stopAllThreads()
	{
		drawThread.setFlag(false);       
	}
	private class DrawThread extends Thread{
		private boolean flag = true;	
		private int sleepSpan = 100;
		HighScoreView fatherView;
		SurfaceHolder surfaceHolder;
		public DrawThread(HighScoreView fatherView){
			this.fatherView = fatherView;
			this.surfaceHolder = fatherView.getHolder();
		}
		public void run(){
			Canvas c;
	        while (this.flag) {
	            c = null;
	            try {
	            	// ��w��ӵe���A�b�O����n�D����������p�U�A��ĳ�ѼƤ��n��null
	                c = this.surfaceHolder.lockCanvas(null);
	                synchronized (this.surfaceHolder) {
	                	fatherView.onDraw(c);//ø��
	                }
	            } finally {
	                if (c != null) {
	                	//��������
	                    this.surfaceHolder.unlockCanvasAndPost(c);
	                }
	            }
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
}
