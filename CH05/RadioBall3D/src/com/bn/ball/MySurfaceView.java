package com.bn.ball;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.view.MotionEvent;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import com.bn.ball.cube.CubeGroup;
import com.bn.ball.wt.BoLi;
import com.bn.ball.wt.Container;
import com.bn.ball.wt.LiFangTi;
import com.bn.ball.wt.TextureRect;
import com.bn.ball.wt.YuanZhu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import static com.bn.ball.Constant.*;
import static com.bn.ball.jiemian.Constant.*;
public class MySurfaceView extends GLSurfaceView {
	RadioBallActivity activity;
	LiFangTi lft;
	BoLi bl;
	private int load_step=0;//�i�׫��ܾ��B��
	YuanZhu yz;    	
	Container container;
	CubeGroup cubeGroup;
	public boolean rotate_flag=false;//����ЧӦ�
	public float[][] loction_offset=new float[27][3];
	public float[][] angle_offset=new float[27][4];
	private SceneRenderer mRenderer;//�����ۦ⾹	
	public int groundId;//�a�����zid
	public int ballId;//���z�W��ID
	public int textureID;//�޽u���zid	
	int topYuanId;//����id
	int bottomYuanId;//����id
	int circleSideId;//��W����id
	int containerId;
	int whiteId;
	int greenId;
	
	boolean isLoadedOk=false;
	int blId;
	public int lftId;
	int cubeId;
	int returnId;
	int retryId;
	int loseId;
	int winId;
	int jumpId;
	int minusId;
	int plusId;
	
	private boolean isFirstFrame=true;
	
	public static boolean flag=true;
	static float bx;//�yx�y��
    static float bz;//�yz�y��	
    BallThread bt;    

    List<WuTiForControl> zawList;
    float ratio;
    int numberId[]=new int[10];
    int tempNumber;
    int scoreId;
    TextureRect effect;
    TextureRect scoreRect;
    TextureRect number;
    TextureRect button;
    TextureRect bground;
    TextureRect bgbutton;
    
    TextureRect loadingView;
    TextureRect processBar;
    TextureRect loading;
    int tex_loadingviewId;
    int processBeijing;
    int tex_processId;
    int loadId;
    
	int tempNum[]=new int[5];
	
	LoadedObjectVertexNormalTexture lovo[]=new LoadedObjectVertexNormalTexture[3];
	public MySurfaceView(Context context) {
        super(context);  
        this.activity=(RadioBallActivity) context;
        bx=BALL_X;
        bz=BALL_Z;
        mRenderer = new SceneRenderer();	//�إ߳����ۦ⾹
        setRenderer(mRenderer);				//�]�w�ۦ⾹		
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//�]�w�ۦ�Ҧ����D�ʵۦ�   
    }
	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		int x=(int)e.getX();//���oĲ���I��XY�y��
		int y=(int)e.getY();
		switch(e.getAction())
		{		
		case MotionEvent.ACTION_DOWN:
			if(Constant.flag&&!Constant.ballYFlag&&x>Game_Jump_Left&&x<Game_Jump_Right&&y>Game_Jump_Top&&y<Game_Jump_Buttom)
			{
				Constant.ballYFlag=true;
				new BallYThread().start();
			}
			
			break;
		case  MotionEvent.ACTION_UP:
			if((Constant.winFlag||Constant.loseFlag)&&x>Game_LB_Left&&x<Game_LB_Right&&y>Game_LB_Top&&y<Game_LB_Buttom)	
			{
				activity.hd.sendEmptyMessage(1);
			}
			if((Constant.winFlag||Constant.loseFlag)&&x>Game_RB_Left&&x<Game_RB_Right&&y>Game_RB_Top&&y<Game_RB_Buttom)
			{
				activity.hd.sendEmptyMessage(5);
			}
			break;
		}
		
			
		return true;
	}
	private class SceneRenderer implements GLSurfaceView.Renderer 
    {         
		Ball ball;
         SaiDao sd;
         SaiDaoYC sdyc;
         float Temp;  	
        public void onDrawFrame(GL10 gl) {
      
        	Temp=Constant.BALL_Z+6.5f;
        	//�M���m��֨���`�ק֨�
        	gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
            if(!isLoadedOk)//�p�L�S�����J����
            {
            	 drawOrthLoadingView(gl);            	 
            }
            else
            {
            	drawGameView(gl);
            }
 
            
        }
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //�]�w�����j�p�Φ�m 
         	gl.glViewport((int)com.bn.ball.jiemian.Constant.sx, (int)com.bn.ball.jiemian.Constant.sy, (int)(1024*com.bn.ball.jiemian.Constant.ratio), (int)(720*com.bn.ball.jiemian.Constant.ratio));
         	//�p��GLSurfaceView�����e��
            ratio = (float) 1024 / 720;
        }
        public void onSurfaceCreated(final GL10 gl, EGLConfig config) {
            //�����ܧݰ� 
        	gl.glDisable(GL10.GL_DITHER);
        	//�]�w�S�wHint�M�ת��Ҧ��A�o�̬��]�w���ϥΧֳt�Ҧ�
            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,GL10.GL_FASTEST);
            //�]�w�ù��I����¦�RGBA
            gl.glClearColor(0,0,0,0);            
            //�ҥβ`�״���
            gl.glEnable(GL10.GL_DEPTH_TEST);
            //�]�w�ۦ�ҫ��������ۦ�   
            gl.glShadeModel(GL10.GL_SMOOTH); 
            loadingView=new TextureRect(1f,1f);
            processBar=new TextureRect(1,0.1f);
            loading=new TextureRect(0.8f,0.1f);
            tex_loadingviewId=initTexture(gl,R.drawable.beijing);
            processBeijing=initTexture(gl,R.drawable.processbeijing);
            tex_processId=initTexture(gl,R.drawable.process);
            loadId=initTexture(gl,R.drawable.load);
            load_step=0;
        }
        //�����vø����J�ɭ�
        public void drawOrthLoadingView(GL10 gl){
            if(isFirstFrame){ //�Y�G�O�Ĥ@��
            	gl.glPushMatrix();
            	gl.glMatrixMode(GL10.GL_PROJECTION);
        		gl.glLoadIdentity();//�]�w�ثe�x�}�����x�}
        		gl.glOrthof(-0.8f,0.8f,-0.8f,0.8f,1,200);
        		gl.glMatrixMode(GL10.GL_MODELVIEW);//�]�w�ثe�x�}���Ҧ��x�}
        		gl.glLoadIdentity();//�]�w�ثe�x�}�����x�}          
            	GLU.gluLookAt
                (
                		gl, 
                		0,   //�H����m��X
                		0, 	//�H����m��Y
                		0,   //�H����m��Z
                		0, 	//�H�����ݪ��IX
                		0,   //�H�����ݪ��IY 
                		-5f,   //�H�����ݪ��IZ
                		0, 
                		1,  
                		0
                );
            	gl.glTranslatef(0, 0, -6f);
                loadingView.drawSelf(gl,tex_loadingviewId);
                gl.glPopMatrix();
            	isFirstFrame=false;
            } else {//�o�̶i��귽�����J
            	gl.glPushMatrix();
            	//�]�w�ثe�x�}���Ҧ��x�}
                gl.glMatrixMode(GL10.GL_PROJECTION);
                //�]�w�ثe�x�}�����x�}
                gl.glLoadIdentity(); 
                gl.glOrthof(-0.8f,0.8f,-0.8f,0.8f,1,200);  
            	
            	gl.glMatrixMode(GL10.GL_MODELVIEW);//�]�w�ثe�x�}���Ҧ��x�}
            	gl.glLoadIdentity();//�]�w�ثe�x�}�����x�} 
            	GLU.gluLookAt(gl,0,0,0,0,0,-5f,0,1,0);
            	
            	gl.glPushMatrix();
            	gl.glTranslatef(0, 0, -6f);
                loadingView.drawSelf(gl,tex_loadingviewId);//ø��I����
                gl.glPopMatrix();
                
                gl.glPushMatrix();
            	gl.glEnable(GL10.GL_BLEND);
        		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
                 gl.glTranslatef(0, -0.7f, -5.5f);
                 processBar.drawSelf(gl,processBeijing);
                 gl.glDisable(GL10.GL_BLEND); 
                 gl.glPopMatrix();
                
                 gl.glPushMatrix();  //�]�w�i�׫��ܾ�
             	gl.glEnable(GL10.GL_BLEND);
         		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
                 gl.glTranslatef(-2+2*load_step/(float)25, -0.7f, -5.2f);
                 processBar.drawSelf(gl,tex_processId);
                 gl.glDisable(GL10.GL_BLEND); 
                 gl.glPopMatrix();
                 
                
                 gl.glPushMatrix();
              	gl.glEnable(GL10.GL_BLEND);
          		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
          		gl.glTranslatef(0.2f, 0.2f, -5.5f);
                 loading.drawSelf(gl,loadId);
                 gl.glDisable(GL10.GL_BLEND);
                 gl.glPopMatrix();

                 
                gl.glPopMatrix();
                loadResource(gl);//���J�귽����k
                return;    
            } 
            }
      //���J�Ҧ����귽
        public void loadResource(GL10 gl)
        {
        	switch(load_step)
        	{
        	case 0:
        		load_step++;
        		break;
        	case 1:
        		init_All_Texture(load_step,gl);
        		load_step++;
        		break;
        	case 2:
        		init_All_Texture(load_step,gl);
        		load_step++;
        		break;
        	case 3:
        		init_All_Texture(load_step,gl);
        		load_step++;
        		break;
        	case 4:
        		init_All_Texture(load_step,gl);
        		load_step++;
        		break;
        	case 5:
        		init_All_Texture(load_step,gl);
        		load_step++;
        		break;
        	case 6:
        		init_All_Texture(load_step,gl);
        		load_step++;
        		break;
        	case 7:
        		init_All_Texture(load_step,gl);
        		load_step++;
        		break;
        	case 8:
        		init_All_Texture(load_step,gl);
        		load_step++;
        		break;
        	case 9:
        		init_All_Texture(load_step,gl);
        		load_step++;
        		break;
        	case 10:
        		init_All_Texture(load_step,gl);
        		load_step++;
        		break;
        	case 11:
        		init_All_Texture(load_step,gl);
        		load_step++;
        		break;
        	case 12:
        		init_All_Texture(load_step,gl);
        		load_step++;
        		break;
        	case 13:
        		init_All_Texture(load_step,gl);
        		load_step++;
        		break;
        	case 14:
        		init_All_Texture(load_step,gl);
        		load_step++;
        		break;
        	case 15:
        		init_All_Texture(load_step,gl);
        		load_step++;
        		break;
        	case 16:
        		init_All_Texture(load_step,gl);
        		load_step++;
        		break;
        	case 17:
        		init_All_Texture(load_step,gl);
        		load_step++;
        		break;
        	case 18:
        		init_All_Texture(load_step,gl);
        		load_step++;
        		break;
        	case 19:
        		init_All_Texture(load_step,gl);
        		load_step++;
        		break;
        	case 20:
        		init_All_Texture(load_step,gl);
        		load_step++;
        		break;
        	case 21:
        		init_All_Texture(load_step,gl);
        		load_step++;
        		break;
        	case 22:
        		init_All_Texture(load_step,gl);
        		load_step++;
        		break;
        	case 23:
        		init_All_Texture(load_step,gl);
        		load_step++;
        		break;
        	case 24:
        		init_All_Texture(load_step,gl);
        		load_step++;
        		break;
        	case 25:
        		init_All_Texture(load_step,gl);
        		isLoadedOk=true;//������@�ſ��
        		break;
        	}
        }
        //���J���z����k
        public void init_All_Texture(int index,GL10 gl)
        {
        	switch(index)
        	{
        	case 1:
            	BALL_Z=-1.5f;//�y���_�lZ�y��
            	BALL_X=0;	//�y���_�lX�y��
            	BALL_Y=-1.2f; //�y���_�lY�y��
            	
            	vx=0f;
            	vz=-0.2f;	//�p�y�bZ�b���t��
            	vk=1;
            	
                //�_�l�ƥզ�O
                initWhiteLight(gl);
                //�_�l�Ƨ���
            	initMaterial(gl);

              break;
        	case 2:
        	case 3:	
                groundId=initTexture(gl,R.drawable.ground1); 
                textureID=initTexture(gl,R.drawable.wall2);
                ballId=initTexture(gl,R.drawable.ball);  
      
                topYuanId=initTexture(gl,R.drawable.barrel_red);
                bottomYuanId=initTexture(gl,R.drawable.barrel_red);
                circleSideId=initTexture(gl,R.drawable.barrel_red);    
                
            	whiteId=initTexture(gl,R.drawable.white);
            	greenId=initTexture(gl,R.drawable.green);
        		break;
        	case 4:  
        		lovo[0]=com.bn.ball.util.LoadUtil.loadFromFileVertexOnly("wugui.obj",MySurfaceView.this.getResources(),greenId);
              break;
        	case 5:

              break;
        	case 6:
        	case 7:	
        						
        		break;
        	case 8:  

              break;
        	case 9:
                lftId=initTexture(gl,R.drawable.cube1);
                containerId=initTexture(gl,R.drawable.container);
                blId=initTexture(gl,R.drawable.ss);                                                                               
                cubeId=initTexture(gl,R.drawable.cube2);
                ball=new Ball(Constant.BALL_SCALE,ballId);
                sd=new SaiDao(MySurfaceView.this,textureID,groundId,3);
                scoreId=initTexture(gl,R.drawable.score);
                sdyc=new SaiDaoYC(sd,groundId,textureID);
              break;
        	case 10:	
        	case 11:				
        				
        		break;
        	case 12:  

              break;
        	case 13:      
                numberId[0]=initTexture(gl,R.drawable.n0);
                numberId[1]=initTexture(gl,R.drawable.n1);
                numberId[2]=initTexture(gl,R.drawable.n2);
                numberId[3]=initTexture(gl,R.drawable.n3);
                numberId[4]=initTexture(gl,R.drawable.n4);
                numberId[5]=initTexture(gl,R.drawable.n5);
                numberId[6]=initTexture(gl,R.drawable.n6);
                numberId[7]=initTexture(gl,R.drawable.n7);
                numberId[8]=initTexture(gl,R.drawable.n8);
                numberId[9]=initTexture(gl,R.drawable.n9);
              break;
        	case 14:
        	case 15:
        		lovo[1]=com.bn.ball.util.LoadUtil.loadFromFileVertexOnly("tuzi.obj",MySurfaceView.this.getResources(),whiteId);
        		break;
        	case 16:  
                returnId=initTexture(gl,R.drawable.returna);
                retryId=initTexture(gl,R.drawable.retry);
                loseId=initTexture(gl,R.drawable.lose1);
                winId=initTexture(gl,R.drawable.win1);
                jumpId=initTexture(gl,R.drawable.jumpbutton);
                
                plusId=initTexture(gl,R.drawable.plus500);
                minusId=initTexture(gl,R.drawable.minus200);
              break;


        	case 17:	
                scoreRect=new TextureRect(0.1f,0.05f);
                effect=new TextureRect(0.2f,0.1f);
                tempNumber=numberId[0];
                number=new TextureRect(0.025f,0.05f);
                button=new TextureRect(0.12f,0.18f);
                bground=new TextureRect(0.5f,0.25f);
                bgbutton=new TextureRect(0.2f,0.08f);
        		break;
        	case 18:	
        	case 19:

        		break;
        	case 20:
                yz=new YuanZhu(1,0.5f,1.2f,36,topYuanId,bottomYuanId,circleSideId);//�_�l�ƻ�ê��
                cubeGroup=new CubeGroup(MySurfaceView.this,cubeId);
                container=new Container(containerId);
                lft=new LiFangTi(lftId);
                bl=new BoLi(blId);
        		break;
        	case 21:

                  break;
        	case 22:			 
                MapDate.initHzz(MySurfaceView.this,1,0.4f,1.2f,36,topYuanId,bottomYuanId,circleSideId,lftId,containerId,blId,cubeId);
                zawList=MapDate.genControlZAWList();
                  break;	
        	case 23:

                  break;
        	case 24:
                Constant.flag=true;
                bt=new BallThread(zawList,MySurfaceView.this,gl,activity);
                bt.start(); 
                    break;
        	case 25:
        	} 
        }
        	public void drawGameView(GL10 gl)
        	{
        		//�e�\���� 
                gl.glEnable(GL10.GL_LIGHTING);  
                //�]�w�զ��������m
                float[] positionParams={0,3,0,1};//���Ωw���
                gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_POSITION, positionParams,0);

            	
            	gl.glMatrixMode(GL10.GL_PROJECTION);
        		gl.glLoadIdentity();//�]�w�ثe�x�}�����x�}
        		gl.glFrustumf(-0.8f*ratio,0.8f*ratio,-0.8f,0.8f,2,200);
        		gl.glMatrixMode(GL10.GL_MODELVIEW);//�]�w�ثe�x�}���Ҧ��x�}
        		gl.glLoadIdentity();//�]�w�ثe�x�}�����x�}          
            	GLU.gluLookAt
                (
                		gl, 
                		0,   //�H����m��X
                		0, 	//�H����m��Y
                		Temp,   //�H����m��Z
                		0, 	//�H�����ݪ��IX
                		0,   //�H�����ݪ��IY
                		Temp-2.5f,   //�H�����ݪ��IZ
                		0, 
                		1, 
                		0
                ); 
            	if(daojuFlag)
            	{
               	 	gl.glPushMatrix();
               	 	gl.glTranslatef(0, 0, daoJuZ);
               	 	if(num==0)
               	 	{
               	 		gl.glScalef(0.015f, 0.015f, 0.015f);
               	 	}else
               	 	{
               	 		gl.glScalef(0.04f, 0.04f, 0.04f);
               	 	}
               	 	
               	 	lovo[num].drawSelf(gl);
               	 	gl.glPopMatrix();
               	 	
            	}
                //�T����� 
                gl.glDisable(GL10.GL_LIGHTING);


                
                
            	sd.drawSelf(gl);            
                gl.glPushMatrix();
        		//�h������w��m
        		gl.glTranslatef(Constant.BALL_X,Constant.BALL_Y, Constant.BALL_Z);
        		//¶����b����]����b�����P�B�ʤ�V�A�����ୱ�^
        		if(Math.abs(angleCurr)!=0&&(Math.abs(currAxisX)!=0||Math.abs(currAxisY)!=0||Math.abs(currAxisZ)!=0))
        		{
        			gl.glRotatef(angleCurr, currAxisX, currAxisY, currAxisZ);
        		}
                ball.drawSelf(gl);         
                gl.glPopMatrix();	
                sdyc.drawSelf(gl);
             
                float currSaiDao=Math.abs(BALL_Z)/20;
                gl.glPushMatrix();
                for(WuTiForControl zfcTemp:zawList)//ø���ê��
                {
                	if(zfcTemp.id==3)
                	{
                		gl.glEnable(GL10.GL_CULL_FACE);
                		gl.glEnable(GL10.GL_BLEND);
                		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
                	}
                	    if(zfcTemp.quanHao!=0){
                    	  zfcTemp.drawSelf(gl, currSaiDao);  
                	    }
                	    gl.glDisable(GL10.GL_BLEND);
                }
                
                if(com.bn.ball.cube.Constant.THREAD_FLAG)
        		   {
        			  gl.glTranslatef(Constant.BALL_X, 0, Constant.BALL_Z);
                 	  cubeGroup.go(gl);
        		   }
                 gl.glPopMatrix();
            	 drawNumber(gl);


        	}
        	public void drawNumber(GL10 gl){
            	//�]�w�ثe�x�}���Ҧ��x�}
                gl.glMatrixMode(GL10.GL_PROJECTION);
                  

                //�]�w�ثe�x�}�����x�}
                gl.glLoadIdentity(); 
                gl.glOrthof(-0.8f,0.8f,-0.8f,0.8f,2,200);  
            	
            	gl.glMatrixMode(GL10.GL_MODELVIEW);//�]�w�ثe�x�}���Ҧ��x�}
            	gl.glLoadIdentity();//�]�w�ثe�x�}�����x�} 
            	GLU.gluLookAt(gl,0,0,0,0,0,-5f,0,1,0); 
            

            	if(daojuFlag)
            	{       	 
                	gl.glPushMatrix();
                	gl.glEnable(GL10.GL_BLEND);
            		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
                	gl.glTranslatef(-0.4f,0,-5.5f);
                	if(num==1)
                	{
                		effect.drawSelf(gl,plusId);
                		sumBoardScore+=500;
                	}else
                	{
                		effect.drawSelf(gl,minusId);
                		sumBoardScore-=200;
                	}
                	gl.glDisable(GL10.GL_BLEND);
                	gl.glPopMatrix();
            	}
                
            	gl.glPushMatrix();
            	gl.glEnable(GL10.GL_BLEND);
        		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
            	gl.glTranslatef(-0.68f, 0.7f, -5);
            	scoreRect.drawSelf(gl,scoreId);
            	gl.glDisable(GL10.GL_BLEND);
            	gl.glPopMatrix();
            	
            	Constant.sumBoardScore=Constant.sumEfectScore+Constant.sumLoadScore;
            	
            	tempNum[0]=Constant.sumBoardScore/10000;
            	tempNum[1]=Constant.sumBoardScore%10000/1000;
            	tempNum[2]=Constant.sumBoardScore%1000/100;
            	tempNum[3]=Constant.sumBoardScore%100/10;
            	tempNum[4]=Constant.sumBoardScore%10;
            	for(int i=0;i<5;i++){
            		gl.glPushMatrix();
                	gl.glEnable(GL10.GL_BLEND);
            		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
            		gl.glTranslatef(-0.54f+0.05f*i, 0.7f, -5);
            		number.drawSelf(gl,numberId[tempNum[i]]);
            		gl.glDisable(GL10.GL_BLEND);
                	gl.glPopMatrix();
            	}
            	
            	gl.glPushMatrix();
            	gl.glEnable(GL10.GL_BLEND);
        		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
            	gl.glTranslatef(0.6f,-0.55f, -5);
            	button.drawSelf(gl,jumpId);
            	gl.glDisable(GL10.GL_BLEND);
            	gl.glPopMatrix();
            	

            	
            	if(loseFlag)
            	{
                	gl.glPushMatrix();
                	gl.glEnable(GL10.GL_BLEND);
            		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
                	gl.glTranslatef(0,0, -5);
                	bground.drawSelf(gl,loseId);
                	gl.glDisable(GL10.GL_BLEND);
                	gl.glPopMatrix();
            	}
            	if(winFlag)
            	{
                	gl.glPushMatrix();
                	gl.glEnable(GL10.GL_BLEND);
            		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
                	gl.glTranslatef(0,0, -5);
                	bground.drawSelf(gl,winId);
                	gl.glDisable(GL10.GL_BLEND);
                	gl.glPopMatrix();
            	}
            	if(winFlag||loseFlag)
            	{
                	gl.glPushMatrix();
                	gl.glTranslatef(-0.22f,-0.1f, -4.9f);
                	bgbutton.drawSelf(gl,returnId);
                	gl.glPopMatrix();
                	
                	gl.glPushMatrix();
                	gl.glTranslatef(0.22f,-0.1f, -4.9f);
                	bgbutton.drawSelf(gl,retryId);
                	gl.glPopMatrix();
            	}
        	}
        
    }



	//�_�l�Ư��z
	public int initTexture(GL10 gl,int drawableId)
	{
		//���ͯ��zID
		int[] textures = new int[1];
		gl.glGenTextures(1, textures, 0);    
		int currTextureId=textures[0];    
		gl.glBindTexture(GL10.GL_TEXTURE_2D, currTextureId);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_LINEAR);
        ((GL11)gl).glTexParameterf(GL10.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL10.GL_TRUE);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,GL10.GL_CLAMP_TO_EDGE);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,GL10.GL_CLAMP_TO_EDGE);
        
        InputStream is = this.getResources().openRawResource(drawableId);
        Bitmap bitmapTmp; 
        try 
        {
        	bitmapTmp = BitmapFactory.decodeStream(is);
        } 
        finally 
        {
            try 
            {
                is.close();
            } 
            catch(IOException e) 
            {
                e.printStackTrace();
            }
        }
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmapTmp, 0);
        bitmapTmp.recycle(); 
        return currTextureId;
	}
	
	//�_�l�ƥզ�O
	private void initWhiteLight(GL10 gl)
	{    
        gl.glEnable(GL10.GL_LIGHT1);//�}��1���O  
        
        //���ҥ��]�w
        float[] ambientParams={0.2f,0.2f,0.05f,1.0f};//���Ѽ� RGBA
        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_AMBIENT, ambientParams,0);            
        
        //���g���]�w 
        float[] diffuseParams={0.9f,0.9f,0.2f,1.0f};//���Ѽ� RGBA
        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_DIFFUSE, diffuseParams,0); 
        
        //�Ϯg���]�w 
        float[] specularParams={1f,1f,1f,1.0f};//���Ѽ� RGBA
        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_SPECULAR, specularParams,0); 
	}
	
	//�_�l�Ƨ���
	private void initMaterial(GL10 gl)
	{//���謰�զ�ɤ���m�⪺�����b�W���N�N��{�X����m��
        //���ҥ����զ����
        float ambientMaterial[] = {1.0f, 1.0f, 1.0f, 1.0f};
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, ambientMaterial,0);
        //���g�����զ����
        float diffuseMaterial[] = {1.0f, 1.0f, 1.0f, 1.0f};
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, diffuseMaterial,0);
        //�������謰�զ�
        float specularMaterial[] = {1f, 1f, 1f, 1.0f};
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, specularMaterial,0);
        gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 4.0f);
	}
}
