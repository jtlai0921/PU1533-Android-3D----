package tstc.fxq.parts;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import tstc.fxq.main.MySurfaceView;
import tstc.fxq.utils.MatrixState;
import tstc.fxq.utils.ShaderManager;
import android.opengl.GLES20;
import static tstc.fxq.constants.Constant.*;
//�Ω�ø��y
public class BallDingDian {	
	int mProgram;//�ۭq�ۦ�ް������id
    int muMVPMatrixHandle;//�`�ܴ��x�}�Ѧ�id
    int muMMatrixHandle;//��m�B�����ܴ��x�}�Ѧ�id
    int muCameraMatrixHandle;//��v���x�}�Ѧ�id
    int muProjMatrixHandle;//��v�x�}�Ѧ�id
    int maPositionHandle; //���I��m�ݩʰѦ�id  
    int maTexCoorHandle; //���I���z�y���ݩʰѦ�id  
    int maNormalHandle; //���I�k�V�q�ݩʰѦ�id  
    int maLightLocationHandle;//������m�ݩʰѦ�id  
    int maCameraHandle; //��v����m�ݩʰѦ�id 
    int muIsShadow;//�O�_ø��v�ݩʰѦ�id  
    int muIsShadowFrag;//�O�_ø��v�ݩʰѦ�id 
    int muBallTexHandle;//��y���z�ݩʰѦ�id 
    int muTableTexHandle;//�Ω�ø��v���ୱ���z�ݩʰѦ�id 
	
	FloatBuffer   mVertexBuffer;//���I�y�и�ƽw�R
	FloatBuffer   mTexCoorBuffer;//���I���z�y�и�ƽw�R
	FloatBuffer   mNormalBuffer;//���I�k�V�q��ƽw�R
    int vCount=0;//���I�ƶq
    float scale;//�ؤo
    float xOffset;
    float zOffset;
    public BallDingDian(MySurfaceView mv, float scale) 
    {
    	this.scale=scale;    	
    	
    	//���o������Ϫ����z�}�C
    	float[] texCoorArray= 
         generateTexCoor
    	 (
    			 (int)(360/ANGLE_SPAN), //���z�Ϥ������C��
    			 (int)(180/ANGLE_SPAN)  //���z�Ϥ��������
    	);
        int tc=0;//���z�}�C�p�ƾ�
        int ts=texCoorArray.length;//���z�}�C����
    	
    	ArrayList<Float> alVertix=new ArrayList<Float>();//�s���I�y�Ъ�ArrayList
    	ArrayList<Float> alTexture=new ArrayList<Float>();//�s�񯾲z�y�Ъ�ArrayList
    	
        for(float vAngle=90;vAngle>-90;vAngle=vAngle-ANGLE_SPAN)//������VangleSpan�פ@��
        {
        	for(float hAngle=360;hAngle>0;hAngle=hAngle-ANGLE_SPAN)//������VangleSpan�פ@��
        	{
        		//���L�����U��@�Ө��׫�p����������I�b�y���W���|��γ��I�y��
        		//�ëغc��Ӹs�զ��|��Ϊ��T����
        		
        		double xozLength=scale*UNIT_SIZE*Math.cos(Math.toRadians(vAngle));
        		float x1=(float)(xozLength*Math.cos(Math.toRadians(hAngle)));
        		float z1=(float)(xozLength*Math.sin(Math.toRadians(hAngle)));
        		float y1=(float)(scale*UNIT_SIZE*Math.sin(Math.toRadians(vAngle)));
        		
        		xozLength=scale*UNIT_SIZE*Math.cos(Math.toRadians(vAngle-ANGLE_SPAN));
        		float x2=(float)(xozLength*Math.cos(Math.toRadians(hAngle)));
        		float z2=(float)(xozLength*Math.sin(Math.toRadians(hAngle)));
        		float y2=(float)(scale*UNIT_SIZE*Math.sin(Math.toRadians(vAngle-ANGLE_SPAN)));
        		
        		xozLength=scale*UNIT_SIZE*Math.cos(Math.toRadians(vAngle-ANGLE_SPAN));
        		float x3=(float)(xozLength*Math.cos(Math.toRadians(hAngle-ANGLE_SPAN)));
        		float z3=(float)(xozLength*Math.sin(Math.toRadians(hAngle-ANGLE_SPAN)));
        		float y3=(float)(scale*UNIT_SIZE*Math.sin(Math.toRadians(vAngle-ANGLE_SPAN)));
        		
        		xozLength=scale*UNIT_SIZE*Math.cos(Math.toRadians(vAngle));
        		float x4=(float)(xozLength*Math.cos(Math.toRadians(hAngle-ANGLE_SPAN)));
        		float z4=(float)(xozLength*Math.sin(Math.toRadians(hAngle-ANGLE_SPAN)));
        		float y4=(float)(scale*UNIT_SIZE*Math.sin(Math.toRadians(vAngle)));   
        		
        		//�غc�Ĥ@�T����
        		alVertix.add(x1);alVertix.add(y1);alVertix.add(z1);
        		alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
        		alVertix.add(x4);alVertix.add(y4);alVertix.add(z4);        		
        		//�غc�ĤG�T����
        		alVertix.add(x4);alVertix.add(y4);alVertix.add(z4);
        		alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
        		alVertix.add(x3);alVertix.add(y3);alVertix.add(z3); 
        		
        		//�Ĥ@�T����3�ӳ��I��6�ӯ��z�y��
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);        		
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);
        		//�ĤG�T����3�ӳ��I��6�ӯ��z�y��
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);        		
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);       		
        	}
        } 	
        
        vCount=alVertix.size()/3;//���I���ƶq���y�Эȼƶq��1/3�A�]���@�ӳ��I��3�Ӯy��
    	
        //�NalVertix�����y�Э���s��@��float�}�C��
        float vertices[]=new float[vCount*3];
    	for(int i=0;i<alVertix.size();i++)
    	{
    		vertices[i]=alVertix.get(i);
    	}
        
        //�إ�ø��I��ƽw�R
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mVertexBuffer = vbb.asFloatBuffer();//�ରfloat���w�R
        mVertexBuffer.put(vertices);//�V�w�R�Ϥ���J���I�y�и��
        mVertexBuffer.position(0);//�]�w�w�R�ϰ_�l��m     
        
        //�إ�ø��I�k�V�q�w�R
        ByteBuffer nbb = ByteBuffer.allocateDirect(vertices.length*4);
        nbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mNormalBuffer = nbb.asFloatBuffer();//�ରfloat���w�R
        mNormalBuffer.put(vertices);//�V�w�R�Ϥ���J���I�y�и��
        mNormalBuffer.position(0);//�]�w�w�R�ϰ_�l��m     
       
                
        //�إ߯��z�y�нw�R
        float textureCoors[]=new float[alTexture.size()];//���I���z�Ȱ}�C
        for(int i=0;i<alTexture.size();i++) 
        {
        	textureCoors[i]=alTexture.get(i);
        }
        
        ByteBuffer tbb = ByteBuffer.allocateDirect(textureCoors.length*4);
        tbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mTexCoorBuffer = tbb.asFloatBuffer();//�ରint���w�R
        mTexCoorBuffer.put(textureCoors);//�V�w�R�Ϥ���J���I�ۦ���
        mTexCoorBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        

        //�_�l��shader
        intShader(mv);
    }
    //�_�l��shader
    public void intShader(MySurfaceView mv)
    {
    	//���o�ۭq�ۦ�ް������id 
        mProgram = ShaderManager.getShadowShader();
        //���o�{�������I��m�ݩʰѦ�id  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //���o�{�������I���z�y���ݩʰѦ�id  
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        //���o�{�����`�ܴ��x�}�Ѧ�id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        //���o��m�B�����ܴ��x�}�Ѧ�id
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");  
        //���o�{�������I�k�V�q�ݩʰѦ�id  
        maNormalHandle= GLES20.glGetAttribLocation(mProgram, "aNormal");
        //���o�{����������m�Ѧ�id
        maLightLocationHandle=GLES20.glGetUniformLocation(mProgram, "uLightLocation");
        //���o�{������v����m�Ѧ�id
        maCameraHandle=GLES20.glGetUniformLocation(mProgram, "uCamera"); 
        //���o�{�����O�_ø��v�ݩʰѦ�id
        muIsShadow=GLES20.glGetUniformLocation(mProgram, "isShadow"); 
        muIsShadowFrag=GLES20.glGetUniformLocation(mProgram, "isShadowFrag"); 
        //���o�{������v���x�}�Ѧ�id
        muCameraMatrixHandle=GLES20.glGetUniformLocation(mProgram, "uMCameraMatrix"); 
        //���o�{������v�x�}�Ѧ�id
        muProjMatrixHandle=GLES20.glGetUniformLocation(mProgram, "uMProjMatrix");  
        //���o��y���z�ݩʰѦ�id 
        muBallTexHandle=GLES20.glGetUniformLocation(mProgram, "sTextureBall"); 
        //�Ω�ø��v���ୱ���z�ݩʰѦ�id 
        muTableTexHandle=GLES20.glGetUniformLocation(mProgram, "sTextureTable"); 
    }
    
    public void drawSelf(int ballTexId,int tableTexId,int isShadow)//0-no shadow 1-with shadow
    { 
    	 //��w�ϥάY�Mshader�{��
    	 GLES20.glUseProgram(mProgram); 
         //�N�̲��ܴ��x�}�ǤJshader�{��
         GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
         //�N��m�B�����ܴ��x�}�ǤJshader�{��
         GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);  
         //�N������m�ǤJshader�{��   
         GLES20.glUniform3fv(maLightLocationHandle, 1, MatrixState.lightPositionFB);
         //�N��v����m�ǤJshader�{��   
         GLES20.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);
         //�N�O�_ø��v�ݩʶǤJshader�{�� 
         GLES20.glUniform1i(muIsShadow, isShadow);
         GLES20.glUniform1i(muIsShadowFrag, isShadow);         
         //�N��v���x�}�ǤJshader�{��
         GLES20.glUniformMatrix4fv(muCameraMatrixHandle, 1, false, MatrixState.getCaMatrix(), 0); 
         //�N��v�x�}�ǤJshader�{��
         GLES20.glUniformMatrix4fv(muProjMatrixHandle, 1, false, MatrixState.getProjMatrix(), 0); 
         
         //���e�����w���I��m���
         GLES20.glVertexAttribPointer  
         (
         		maPositionHandle,   
         		3, 
         		GLES20.GL_FLOAT, 
         		false,
                3*4,   
                mVertexBuffer
         );       
         //���e�����w���I���z�y�и��
         GLES20.glVertexAttribPointer  
         (
        		maTexCoorHandle, 
         		2, 
         		GLES20.GL_FLOAT, 
         		false,
                2*4,   
                mTexCoorBuffer
         );   

         //���e�����w���I�k�V�q���
         GLES20.glVertexAttribPointer  
         (
        		maNormalHandle, 
         		3,   
         		GLES20.GL_FLOAT, 
         		false,
                3*4,   
                mNormalBuffer
         );   
         
         //�e�\���I��m�B���z�y�СB�k�V�q��ư}�C
         GLES20.glEnableVertexAttribArray(maPositionHandle);  
         GLES20.glEnableVertexAttribArray(maTexCoorHandle);  
         GLES20.glEnableVertexAttribArray(maNormalHandle);  
         
         //�j�w���z
         GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
         GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, ballTexId);    
         GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
         GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tableTexId);            
         GLES20.glUniform1i(muBallTexHandle, 0);
         GLES20.glUniform1i(muTableTexHandle, 1);  
         //ø��z�x��
         GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);          
         
    }
    //�۰ʤ������z���ͯ��z�}�C����k
    public float[] generateTexCoor(int bw,int bh)
    {
    	float[] result=new float[bw*bh*6*2]; 
    	float sizew=1.0f/bw;//�C��
    	float sizeh=1.0f/bh;//���
    	int c=0;
    	for(int i=0;i<bh;i++)
    	{
    		for(int j=0;j<bw;j++)
    		{
    			//�C��C�@�ӯx�ΡA�Ѩ�ӤT���κc���A�@�����I�A12�ӯ��z�y��
    			float s=j*sizew;
    			float t=i*sizeh;
    			
    			result[c++]=s;
    			result[c++]=t;
    			
    			result[c++]=s;
    			result[c++]=t+sizeh;
    			
    			result[c++]=s+sizew;
    			result[c++]=t;
    			
    			
    			result[c++]=s+sizew;
    			result[c++]=t;
    			
    			result[c++]=s;
    			result[c++]=t+sizeh;
    			
    			result[c++]=s+sizew;
    			result[c++]=t+sizeh;    			
    		}
    	}
    	return result;
    }
    
    public void angle()
    {
    	MatrixState.rotate(xOffset, 1, 0, 0);//¶X�b���
    	MatrixState.rotate(zOffset, 0, 0, 1);
    }
    
}

