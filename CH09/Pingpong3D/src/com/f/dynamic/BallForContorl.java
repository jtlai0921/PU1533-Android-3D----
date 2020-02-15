
package com.f.dynamic;

import static com.f.pingpong.Constant.*;
import static com.f.pingpong.Constant.DifficultyContorl.*;
import static com.f.util.CalculateUtil.*;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import android.content.SharedPreferences;
import android.graphics.RectF;

import com.f.gamebody.GameBall;
import com.f.gamebody.GameBatContorl;
import com.f.pingpong.Constant;
import com.f.pingpong.MainActivity;
import com.f.view.GameSurfaceView;

public class BallForContorl extends Thread{  

	private GameBall ball = null;
	private GameBatContorl rackets = null;
	private GameBatContorl racketssmart = null;
	private MainActivity ma = null;
	private Vector3f balloutspeed = null;
	private static int ballstep = 0;
	
	private Vector2f mPreviPoint = new Vector2f();   //x,y方向上次的座標
	private Vector2f mTouchPoint = new Vector2f();	 //x,y方向觸控的座標
	
	public BallForContorl(MainActivity ma,GameBall ball,GameBatContorl rackets,GameBatContorl rackets_smart)
	{
		this.ma = ma;
		this.ball = ball;
		this.rackets=rackets;
		this.racketssmart=rackets_smart;
		this.balloutspeed = new Vector3f();
	}
	
	@Override
	public void run() {
		while(!IS_GAME_OVER){
			if(!PAUSE)
			{
				collision();
				doActionMove();
			}
			try {
				Thread.sleep((long) (240*TIME_UNIT));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	boolean moveflag=false;
	boolean taskflag=false;
	Vector2f tBuffer=new Vector2f();
	public synchronized void setNewPoint(Vector2f mTouchPointIn)
	{
		tBuffer.set(mTouchPointIn.x,mTouchPointIn.y);
		taskflag=true;
	}
	
	public synchronized void setActionMove()
	{	
		if(!taskflag)return;
		taskflag=false;
		float mx = tBuffer.x - mTouchPoint.x;
		float my = tBuffer.y - mTouchPoint.y;
		if(Math.abs(mx)>50||Math.abs(my)>50)
		{
			this.mTouchPoint.set(tBuffer.x, tBuffer.y);
			moveflag=false;
		}
		else
		{
			mPreviPoint.x=this.mTouchPoint.x;
			mPreviPoint.y=this.mTouchPoint.y;
			this.mTouchPoint.set(tBuffer.x, tBuffer.y);
			moveflag=true;
		}
	}

	
	private void doActionMove()
	{
		if(rackets == null)
		{
			return ;
		}
		setActionMove();
		if(!moveflag){return;}
		
		float mx = mTouchPoint.x - mPreviPoint.x;
		float my = mTouchPoint.y - mPreviPoint.y;
		rackets.touchToMove(mx, my);
		rackets.calRacketsSpeed(mx, my); 
		
		if(IS_SHOOTING && IS_SHOOT_MAN && !CHECKING_POINTS)
		{
			ball.runWithRacket(rackets.position.x);
		}
		moveflag=false;  
	}
	private void ballGo()
	{
		/** 球的速度與位移計算*/
		//add time 
		ball.timeLive.x += TIME_UNIT;
		ball.timeLive.y += TIME_UNIT;
		ball.timeLive.z += TIME_UNIT;
		//v = v0 + a * t
		ball.speed.x = ball.speed_start.x + A.x * ball.timeLive.x;
		ball.speed.y = ball.speed_start.y + A.y * ball.timeLive.y;
		ball.speed.z = ball.speed_start.z + A.z * ball.timeLive.z;
		//s = v0 * t + 0.5 * a * t ^ 2
		float tx = ball.speed_start.x * ball.timeLive.x + 0.5f * A.x * ball.timeLive.x * ball.timeLive.x ;
		float ty = ball.speed_start.y * ball.timeLive.y + 0.5f * A.y * ball.timeLive.y * ball.timeLive.y ;
		float tz = ball.speed_start.z * ball.timeLive.z + 0.5f * A.z * ball.timeLive.z * ball.timeLive.z ;
		//add s
		
		synchronized(Constant.videoLock)
		{
			if(!IS_SHOOTING){
				ball.position.x = ball.position_start.x + tx;
			}
			ball.position.y = ball.position_start.y + ty;
			ball.position.z = ball.position_start.z + tz;
			if(IS_PADDLE && ballstep%2 == 0)
			{
				ball.addPosition(new Vector3f(ball.position));
				ballstep %= 10;
			}
			ballstep++;
		}		
	}
	
	private void collision()
	{
		ballGo();
		
		/** 碰到far端牆壁*/
		float rpx = racketssmart.position.x;
		float rpy = racketssmart.position.y;
		float hrl = racketssmart.boundary.x;//RACKETS_LENGTH / 2.0f;
		float hrw = racketssmart.boundary.y;//RACKETS_WIDTH  / 2.0f;
		RectF rect = new RectF(rpx - hrl , rpy - hrw ,rpx + hrl , rpy + hrw );
		if(rect.contains(ball.position.x,ball.position.y) &&  
		   Math.abs(ball.position.z + BALL_R - racketssmart.position.z ) <= 0.3f&&!racketssmart.isBat)
		{
			racketssmart.isBat = true;
			if(balloutspeed.length()< 2.3 + 0.1 * DIFFICULTY)
			{
				ma.soundandshakeutil.playSound(0, 0);
				synchronized (Constant.videoLock) {
					IS_PADDLE = false;
				}
			}else
			{
				balloutspeed.y *=0.8f; 
				balloutspeed.z *=1.1f; 
				ma.soundandshakeutil.playSound(6, 0);
				ball.lList.clear();
				synchronized (Constant.videoLock) {
					IS_PADDLE = true;
				}
			}
			ballFarCollision();
		}

		/** 球與網側面的碰撞檢驗 */
		if( ball.position.y <= NET_Y && 
			Math.abs(ball.position.x) <= TABLE_X_MAX &&
			Math.abs(ball.position.z) <= BALL_R * 4)
		{
			ballNetCollision();
		}

		/** 球與桌面的碰撞檢驗 */
		if( ball.position.y <= TABLE_Y + BALL_R &&
			Math.abs(ball.position.x) <= TABLE_X_MAX &&
			Math.abs(ball.position.z) <= TABLE_Z_MAX )
		{
			ma.soundandshakeutil.playSound(1, 0);
			ballSurfaceCollision();
			
		}
		/** 若果過了網，對面的拍子就隨著動*/
		if(ball.position.z < 0&&!CHECKING_POINTS)
		{
			rackets.isBat = false;
			ballRacketsAICollision();
			
		}else
		{
			racketssmart.isBat = false;
		}
		ballRacketsCollision();
		isOutside();
	}
	/** 碰到AI端 */
	private void ballFarCollision()
	{
		ball.speed_start.x = balloutspeed.x;
		ball.speed_start.y = balloutspeed.y;
		ball.speed_start.z = balloutspeed.z;
		
		ball.timeLive.x = 0.0f;
		ball.timeLive.y = 0.0f;
		ball.timeLive.z = 0.0f;
		
		ball.position_start.x = ball.position.x;
		ball.position_start.y = ball.position.y;
		ball.position_start.z = ball.position.z + BALL_R * 2;
	}
	
	/** 球與網的碰撞檢驗 */
	private void ballNetCollision()
	{
		/** 球與網上面的碰撞檢驗*/
		if( ball.position.y >= NET_Y && 
			ball.position.y <= NET_Y + BALL_R &&
			Math.abs(ball.position.x) <= TABLE_X_MAX &&
			Math.abs(ball.position.z) <= NET_Z_MAX ){
			
			ball.speed_start.y = - ball.speed.y * BALL_ENERGY_LOST / 2.0f;
			ball.timeLive.y = 0.0f;
			ball.position_start.y = NET_Y + BALL_R;
		}
		else/** 與網側面碰撞*/
		{
			ball.timeLive.z = 0.0f;
			ball.speed_start.z = -ball.speed.z * 0.2f;
			ball.position_start.z = ball.speed.z > 0 ? -0.15f : 0.15f;
			if(!CHECKING_POINTS){
				changePoints();
			}
		}
	}
	
	/** 球與桌面的碰撞檢驗 */
	private void ballSurfaceCollision()
	{
		float ball_energy_lost ;
		if(IS_SHOOTING){
			ball_energy_lost = BALL_ENERGY_LOST_SHOOT;
		}else{
			ball_energy_lost = BALL_ENERGY_LOST;
		}
		/** 初速度置反，並損失能量BALL_ENERGY_LOST*/
		ball.speed_start.x = ball.speed.x;
		ball.speed_start.y = - ball.speed.y * ball_energy_lost;
		ball.speed_start.z = ball.speed.z;
		
		/**set timeLive.y = 0*/
		ball.timeLive.x = 0.0f;
		ball.timeLive.y = 0.0f;
		ball.timeLive.z = 0.0f;
		
		/**重設開始位置*/
		ball.position_start.x = ball.position.x;
		ball.position_start.y = TABLE_Y + BALL_R;
		ball.position_start.z = ball.position.z;
		
		if(ball.position.z < 0){
			ball.side = SIDE_AI;
		}else{
			ball.side = SIDE_HAND;
		}
		
		//人發球，打球後第一次與桌子碰撞
		if(!IS_SHOOTING&&IS_SHOOT_BALL_2)
		{
			Vector3f[] ps_a = {new Vector3f(ball.position_start),new Vector3f(ball.speed_start)}; 
			forecastCal(ps_a);
			IS_SHOOT_BALL_2 = false;
		}
	}
	
	/** 若果過了網，對面的拍子就隨著動*/
	private void ballRacketsAICollision()
	{
		Vector3f tempPos    = new Vector3f(racketssmart.position.x,        racketssmart.position.y,        racketssmart.position.z);
		Vector3f tempTar    = new Vector3f(racketssmart.targetposition.x,  racketssmart.targetposition.y,  racketssmart.targetposition.z);
		Vector3f tempAngNow = new Vector3f(racketssmart.rotateAngleNow.x,  racketssmart.rotateAngleNow.y,  racketssmart.rotateAngleNow.z);
		Vector3f tempAngToa = new Vector3f(racketssmart.rotateAngleTotal.x,racketssmart.rotateAngleTotal.y,racketssmart.rotateAngleTotal.z);
		float tempAngZ      = racketssmart.zAngle;
		
		//防止拍子跑到桌子下面去
		if(tempPos.y < 0 && !IS_SHOOT_MAN)
		{
			return;
		}
		//根據AI拍子搬移速度調整難度,此設定
		float moveStep = RACKETS_STEP;
		if(Math.abs(tempTar.x - tempPos.x) > moveStep)
		{
			tempPos.x += moveStep * ((tempTar.x - tempPos.x) > 0 ? 1 : -1);
		}
		if(Math.abs(tempTar.y - tempPos.y) > moveStep)
		{
			tempPos.y += moveStep * ((tempTar.y - tempPos.y) > 0 ? 1 : -1);
		}
		//慢慢旋轉
		float rotateStep = 0.2f;
		if(Math.abs(tempAngToa.x-tempAngNow.x)>rotateStep)
		{
			tempAngNow.x += rotateStep*((tempAngToa.x-tempAngNow.x)> 0 ? 1 : -1);
		}
		if(Math.abs(tempAngToa.y-tempAngNow.y)>rotateStep)
		{
			tempAngNow.y += rotateStep*((tempAngToa.y-tempAngNow.y)> 0 ? 1 : -1);
		}
		tempAngZ = -tempPos.x / (racketssmart.RACKETS_SIZE_H * 3) * 90;
		if(Math.abs(tempAngZ) >= 90.0f )
		{
			tempAngZ = tempAngZ > 0.0f ? 90.0f : -90.0f;
		}
		
		float zStep = 0.015f;
		//球快到拍子的時候 拍子自動向前"擊球"
		if(Math.abs(ball.position.z)>=Math.abs(0.80*AI_Z)&&ball.speed.z<0)
		{
			tempPos.z+= Math.abs(zStep*AI_Z);
		}else if(tempPos.z>=AI_Z&&ball.speed.z>0)
		{
			tempPos.z-=Math.abs(zStep*AI_Z);
		}
		
		synchronized (Constant.videoLock) {
			racketssmart.position.set(tempPos.x, tempPos.y, tempPos.z);
			racketssmart.targetposition.set(tempTar.x, tempTar.y, tempTar.z);
			racketssmart.rotateAngleNow.set(tempAngNow.x, tempAngNow.y, tempAngNow.z);
			racketssmart.rotateAngleTotal.set(tempAngToa.x,tempAngToa.y ,tempAngToa.z);
			racketssmart.zAngle=tempAngZ;
		}
	}
	
	/** 與球拍的碰撞檢驗*/
	private void ballRacketsCollision()
	{
		float rpx = rackets.position.x;
		float rpy = rackets.position.y;
		float hrl = rackets.boundary.x;//RACKETS_LENGTH / 2.0f;
		float hrw = rackets.boundary.y;//RACKETS_WIDTH  / 2.0f;
		RectF rect = new RectF(rpx - hrl , rpy - hrw ,rpx + hrl , rpy + hrw );
		if( !rackets.isBat && rect.contains(ball.position.x, ball.position.y) &&  
		    Math.abs(ball.position.z - BALL_R - rackets.position.z ) <= 0.1f &&
		    (IS_SHOOTING ? rackets.speed.y < 0 : true) )
		{
			rackets.isBat = true;
			if( !IS_SHOOTING && ball.side == SIDE_AI && !CHECKING_POINTS) {
				
				ball.side = SIDE_HAND;
				ma.soundandshakeutil.playSound(0, 0);
				ma.soundandshakeutil.shake();
				changePoints();
				return;
			}
			
			ball.position_start.x = ball.position.x;
			ball.position_start.y = ball.position.y;
			ball.position_start.z = ball.position.z - BALL_R * 2;
			
			ball.timeLive.x = 0.0f;
			ball.timeLive.y = 0.0f;
			ball.timeLive.z = 0.0f;
			
			Vector3f [] ps_a = new Vector3f[2];
			if(IS_SHOOTING){
				
				ball.position_start.y = 0.6f;
				ball.speed_start.x = rackets.speed.x;
				ball.speed_start.y = -0.8f;
				ball.speed_start.z = -1.5f + ball.speed.z * 0.05f;//-1.2f;
				synchronized (Constant.videoLock) {
					IS_PADDLE = false;
				}
				ma.soundandshakeutil.playSound(0, 0);
				rackets.moveDistanceMax.set(2.2f, 1.0f);
			}else{
				ball.speed_start.x = rackets.speed.x;// + ball.speed.x * 0.05f;
				ball.speed_start.y = 0.7f;
				ball.speed_start.z = rackets.speed.z;// + ball.speed.z * 0.05f;
				
				if(ball.speed_start.z<-1.9f)
				{
					ball.speed_start.y -= 0.25f;
					ball.speed_start.z -= 0.1f;
					ma.soundandshakeutil.playSound(6, 0);
					ball.lList.clear();
					synchronized (Constant.videoLock) {
						IS_PADDLE = true;
					}
				}else
				{
					synchronized (Constant.videoLock) {
						IS_PADDLE = false;
					}
					ma.soundandshakeutil.playSound(0, 0);
				}
				
				ps_a[0] = new Vector3f(ball.position_start);
				ps_a[1] = new Vector3f(ball.speed_start);
				
				forecastCal(ps_a);
			}
			ma.soundandshakeutil.shake();
			IS_SHOOTING = false;
		}
	}
	
	//預測計算
	private void forecastCal(Vector3f ps_a[])//Trend
	{
		//-----------------預測計算------------------//
		Vector3f[] ps_b = getPosAndSpeB(ps_a[0],ps_a[1]);
		Vector3f[] ps_c = getPosAndSpeC(ps_b[0],ps_b[1]);
		
		Vector3f center = null;
		Vector3f target = null;
		float zoom = 0.0f;
		float ball_speed_y = BALL_SPEED_Y;

		//若果球在桌子中間部分 失球的機率變為0.8倍   離桌面太遠  就變為原值的1.2倍
		float ratio = Math.abs(racketssmart.position.x)<=TABLE_X_MAX*2/3?0.8f:1;
		      ratio = Math.abs(racketssmart.position.x)<=TABLE_X_MAX*4/3?1.2f:1;
		      
		//LOST_CHANCE失球率
		if(Math.random()>LOST_CHANCE*ratio)
		{
			zoom = RANDOM_ZOOM;
			center = new Vector3f(0, 0, 1.5f);
			target = randomTarget(center, zoom);// new Vector3f(0,TABLE_Y,-0.03f);
			while(isCollNet(ps_c[0], target, ball_speed_y))
			{
				ball_speed_y += 0.05f;
			}
		}else
		{
			zoom=0.0f;
			//一半的機率撞網 一半的機率出界
			if(Math.random()>0.5)
			{
				//給一個過了網  但離網特別近的點
				center = new Vector3f((float) ((Math.random()-0.5f)*TABLE_X_MAX), 0.0f, 0.03f);
			}else
			{
				//若果拍子在靠右邊   球就出右邊界  在左邊就出左邊界
				if(racketssmart.position.x>0)
				{
					center = new Vector3f(TABLE_X_MAX+0.5f, 0.0f, 2.0f);
				}else
				{
					center = new Vector3f(TABLE_X_MIN-0.5f, 0.0f, 2.0f);
				}
			}
			target = randomTarget(center, zoom);// new Vector3f(0,TABLE_Y,-0.03f);
		}
		
		//用BALL_SPEED_Y控制難度,在此調整
		Vector3f outspeed = getOutSpeedUp(target,ps_c[0],ball_speed_y);
		setOutspeed(outspeed,ps_c[1]);
		setTargetPos(ps_c[0]);
	}
	public void setTargetPos(Vector3f pos)
	{
		racketssmart.targetposition = pos;
	}
	public void setOutspeed(Vector3f outspeed,Vector3f inspeed)
	{
		
		balloutspeed.x = outspeed.x;
		balloutspeed.y = outspeed.y;
		balloutspeed.z = outspeed.z;
		
		Vector3f bisector = getBisector(inspeed,outspeed);
		racketssmart.rotateAngleTotal = getAngle(bisector);
	}
	private void isOutside() 
	{
		if( ball.position.y <= 0 && !CHECKING_POINTS)
		{
			changePoints();
		}
	}
	
	//改變計分
	private void changePoints()
	{
		synchronized (Constant.videoLock) {
			IS_PADDLE = false;
		}
		CHECKING_POINTS = true;
		
		rackets.initRackets();
		racketssmart.initRacketsAI();
		//判斷誰得分
	    if(ball.side == SIDE_AI){
	    	synchronized (Constant.videoLock) {
				rackets.points++;
			}
			ma.soundandshakeutil.playSound(2, 0);
		}else if(ball.side == SIDE_HAND){
			synchronized (Constant.videoLock) {
				racketssmart.points++;
			}
			ma.soundandshakeutil.playSound(4, 0);
		}
		//一局結束的條件
		if(((rackets.points >= 11 || racketssmart.points >= 11) && Math.abs(rackets.points - racketssmart.points) >= 2) ||
		   ((rackets.points >= 10 && racketssmart.points >= 10) && Math.abs(rackets.points - racketssmart.points) >= 2))
		{
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			IS_GAME_OVER = true;
			  
//			//影片存入檔案，此版本不需要
//			VideoUtil.saveFrameData("help.pp", FrameData.list);  
			
			//人取得勝利
			if(rackets.points > racketssmart.points)
			{
				int maxContry = ma.sharedpreferences.getInt("passCountry", 0);
				if(DIFFICULTY+1 >= maxContry){
					//存入SharedPreferences
					SharedPreferences.Editor editor = ma.sharedpreferences.edit();
					editor.putInt("passCountry", ++PASS_COUNTRY);
					editor.commit();
				}
			}
			
			GameSurfaceView.state = rackets.points > racketssmart.points ? 3 : 4;
			return;
		}
		
		//根據得分判斷誰發球
		if((rackets.points+racketssmart.points)%2==0)
		{
			IS_SHOOT_MAN = !IS_SHOOT_MAN;
		}
		new Thread()
		{
			@Override
			public void run(){
				//發球延時SHOOT_DELAY繪制標志
				try {
					System.gc();
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				synchronized (Constant.videoLock) {
					IS_PADDLE = false;
				}
				IS_SHOOTING = true;
				//若果是人發球
				if(IS_SHOOT_MAN)
				{
					IS_SHOOT_BALL_2 = true;
					ball.shootBall(rackets.position.x);
				}else//若果是AI發球
				{
					//設定AI端發球點x座標   球隨拍動     0.7f, TABLE_Z_MIN + 0.8f
					float speedTemp = (float) ((Math.random()-0.5f)*2*0.3f);
					
					synchronized (Constant.videoLock) {
						racketssmart.position.x = 0.0f;
						racketssmart.position.y = 1.0f;
						racketssmart.position.z = TABLE_Z_MIN - 1.0f;
					}
					
					racketssmart.targetposition.x = 0.5f*speedTemp>0?1:-1;
					racketssmart.targetposition.y = 0.3f;
					racketssmart.targetposition.z = TABLE_Z_MIN + 0.8f;
					
					ball.shootBallAI(speedTemp);
					ma.soundandshakeutil.playSound(0, 0);
				}
				CHECKING_POINTS = false;
			}
		}.start();
		if(IS_SHOOT_MAN)
		{
			rackets.moveDistanceMax.set(1.5f, 0.3f);
		}
	}
}