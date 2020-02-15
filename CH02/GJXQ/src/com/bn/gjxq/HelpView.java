package com.bn.gjxq;

import static com.bn.gjxq.Constant.screenHeight;
import static com.bn.gjxq.Constant.screenWidth;

import com.bn.gjxq.manager.ScreenScaleResult;
import com.bn.gjxq.manager.ScreenScaleUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class HelpView extends SurfaceView implements SurfaceHolder.Callback{

	GJXQActivity activity;
	Paint paint;
	
	static Bitmap[] bg_bitmap;//�I���Ϥ��}�C
	static Bitmap pre_page;			//�W�@�����s�Ϥ�
	static Bitmap next_page;			//�U�@�����s�Ϥ�
	static Bitmap back;				//�Ǧ^���s�Ϥ�
	static boolean loadFlag=false;//�O�_���J�Ϥ����ЧӦ�
	static boolean cycle_flag;
	ScreenScaleResult ssr;
	
	int page_index=0;//�ثe���U�I���Ϥ������ޭ�
	public HelpView(GJXQActivity activity) {
		super(activity);
		this.activity=activity;
		ssr=ScreenScaleUtil.calScale(screenWidth, screenHeight);
		this.loadBitmap();//�_�l�ƹϤ�
		this.getHolder().addCallback(this);
		paint=new Paint();
		paint.setAntiAlias(true);
	}

	//�N�Ϥ����J�i�O���骺��k
	public void loadBitmap()  
	{
		if(loadFlag)//�Y���J�Ϥ��ЧӦ쬰true�A�h����return
		{
			return;
		}
		loadFlag=true;  
		bg_bitmap=new Bitmap[6];
		//���J�I���Ϥ�
		bg_bitmap[0]=BitmapFactory.decodeResource(this.getResources(),R.drawable.help_menu); 
		bg_bitmap[1]=BitmapFactory.decodeResource(this.getResources(),R.drawable.help_set); 
		bg_bitmap[2]=BitmapFactory.decodeResource(this.getResources(),R.drawable.youxi1); 
		bg_bitmap[3]=BitmapFactory.decodeResource(this.getResources(),R.drawable.youxi2); 
		bg_bitmap[4]=BitmapFactory.decodeResource(this.getResources(),R.drawable.youxi3); 
		bg_bitmap[5]=BitmapFactory.decodeResource(this.getResources(),R.drawable.youxi4); 
		//���J���s�Ϥ�
		back=BitmapFactory.decodeResource(this.getResources(),R.drawable.help_back); 
		pre_page=BitmapFactory.decodeResource(this.getResources(),R.drawable.prepage);
		next_page=BitmapFactory.decodeResource(this.getResources(),R.drawable.nextpage);
	}	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x=event.getX();
		float y=event.getY();
		switch(event.getAction())
		{
		    case MotionEvent.ACTION_DOWN:
		    	if((x>=(80+ssr.lucX)*ssr.ratio&&x<=(80+97+ssr.lucX)*ssr.ratio&&y>=(450+ssr.lucY)*ssr.ratio
		    			&&y<=(450+85+ssr.lucY)*ssr.ratio)
		    			&&page_index==0)//�Y�G���U���O���U�������s�åB���Ĥ@���A�h���U���O�Ǧ^���s
		    	{
		    		cycle_flag=false;
		    		activity.gotoMenuView();
		    	}
		    	if((x>=(80+ssr.lucX)*ssr.ratio&&x<=(80+97+ssr.lucX)*ssr.ratio
		    			&&y>=(450*ssr.lucY)*ssr.ratio&&y<=(450+85+ssr.lucY)*ssr.ratio)
		    			&&page_index!=0)//�Y�G���U���O���U�������s�åB�������Ĥ@���A�h���U���O�W�@�����s
		    	{
		    		page_index--;
		    	}
		    	//�Y�G���U���O�k�U�������s�åB���̫�@���A�h���U���O�Ǧ^���s
		    	if((x>=(786+ssr.lucX)*ssr.ratio&&x<=(786+97+ssr.lucX)*ssr.ratio
		    			&&y>=(450+ssr.lucY)*ssr.ratio&&y<=(450+85+ssr.lucY)*ssr.ratio)
		    			&&page_index==bg_bitmap.length-1)
		    	{
		    		cycle_flag=false;
		    		activity.gotoMenuView();
		    	}
		    	//�Y�G���U���O�k�U�������s�åB�����̫�@���A�h���U���O�U�@�����s
		    	if((x>=(786+ssr.lucX)*ssr.ratio&&x<=(786+97+ssr.lucX)*ssr.ratio
		    			&&y>=(450+ssr.lucY)*ssr.ratio&&y<=(450+85+ssr.lucY)*ssr.ratio)
		    			&&page_index!=bg_bitmap.length-1)
		    	{
		    		page_index++;
		    	}
			break;
		}
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.save();
		canvas.translate(ssr.lucX,ssr.lucY);
		canvas.scale(ssr.ratio,ssr.ratio); 
		
		canvas.drawColor(Color.BLACK);//�M���ù����¦�
		canvas.drawBitmap(bg_bitmap[page_index],0,0, null);//�e�ثe�I��
		
		if(page_index==0)//�Ĥ@��
		{//���Ǧ^�A�k�U�@��
			canvas.drawBitmap(back, 80, 425, paint);//ø��back���s
			canvas.drawBitmap(next_page, 786, 450, paint);//ø��U�@�����s
		}
		if(page_index>0&&page_index<bg_bitmap.length-1)
		{//�ثe���������ޤj��0�B�p��Ϥ��}�C����-1�A�Y���O�Ĥ@���]���O�̫�@���A���W�@���A�k�U�@��
			canvas.drawBitmap(pre_page,80, 450, paint);//ø��W�@�����s
			canvas.drawBitmap(next_page, 786, 450, paint);//ø��U�@�����s
		}
		if(page_index==bg_bitmap.length-1)//�̫�@���A���W�@���A�k�Ǧ^
		{
			canvas.drawBitmap(pre_page,80, 450, paint);//ø��U�@�����s
			canvas.drawBitmap(back, 786, 425, paint);//ø��back���s
		}
		canvas.restore();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		new Thread()
		{
			public void run()
			{
				cycle_flag=true;
				while(cycle_flag){
					SurfaceHolder myholder = HelpView.this.getHolder();
					Canvas canvas = myholder.lockCanvas();
					try {
						synchronized (myholder) {
							onDraw(canvas);
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (canvas != null) {
							myholder.unlockCanvasAndPost(canvas);
						}
					}
				}
			}
		}.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) 
	{
		cycle_flag=false;
	}

	
}
