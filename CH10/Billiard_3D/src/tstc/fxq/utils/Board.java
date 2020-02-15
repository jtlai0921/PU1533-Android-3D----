package tstc.fxq.utils;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import tstc.fxq.constants.Constant;
import tstc.fxq.main.MySurfaceView;
import android.opengl.GLES20;

//����O���O
public class Board 
{	
	int mProgram;//�ۭq�ۦ�ް������id
    int muMVPMatrixHandle;//�`�ܴ��x�}�Ѧ�id
    int maPositionHandle; //���I��m�ݩʰѦ�id  
    int maTexCoorHandle; //���I���z�y���ݩʰѦ�id
    int muIsTransparent;//����O�O�_�z�����ݩʰѦ�id 
	
	FloatBuffer   mVertexBuffer;//���I�y�и�ƽw�R
	FloatBuffer   mTexCoorBuffer;//���I���z�y�и�ƽw�R 
    int vCount=0;
    
    //�������O�������ܼ�
    float sBegin = 0;//����O���W����s�Bt��
    float tBegin = 0;
    float sEnd;//����O�k�U����s�Bt��
    float tEnd;
	int texId;//���U�M��_������O�Ϥ�id
	int isTransparent;//����O�O�_�z����
    //������Ĳ���d��
    float addedTouchScaleX;//������x��VĲ���d��
    float addedTouchScaleY;//������Y��VĲ���d��
	
	//3D�@�ɤ����ܼ�
	float x;//����O�b3D�@�ɤ�����m
	float y;
    float width;//����O�b3D�@�ɤ����e�שM���׭�
    float height;
	//2D�@�ɤ����ܼ�
	float x2D;//�����ù��������W����m�y��
	float y2D;
    float width2D;//����O�b�ù������e�שM���׭�
    float height2D;
    //2D��3D�@�ɤ��ؤo����ҡG3D = 2D*ratio2DTo3D
    float ratio2DTo3D;
    
    public Board(
    		MySurfaceView mv,
    		float x2D, float y2D, //��������O�����ù��������W����m�y��
    		float width2D, float height2D, //����O�b�ù������e�שM���׭�
    		float addedTouchScaleX, float addedTouchScaleY,//������Ĳ���d��
    		float sEnd, float tEnd, //����O�k�U����s�Bt��
    		int texId, //��_������O�Ϥ�id
    		int isTransparent
    )
    {
    	this.x2D = x2D;
    	this.y2D = y2D;
    	this.width2D = width2D;
    	this.height2D = height2D;
    	this.addedTouchScaleX = addedTouchScaleX;
    	this.addedTouchScaleY = addedTouchScaleY;
    	this.sEnd = sEnd;
    	this.tEnd = tEnd;
    	this.texId = texId;
    	this.isTransparent = isTransparent;
    	
    	//�N����O����m�B�ؤo�ഫ��3D�@�ɤ�����m�M�ؤo
    	changePositionAndScaleFrom2DTo3D();
    	//�_�l�Ƴ��I�y�лP�ۦ���
    	initVertexData();
    	//�_�l��shader        
    	intShader(mv);
    }
    public Board(
    		MySurfaceView mv,
    		float x2D, float y2D, //��������O�����ù��������W����m�y��
    		float width2D, float height2D, //����O�b�ù������e�שM���׭�
    		float addedTouchScaleX, float addedTouchScaleY,//������Ĳ���d��
    		float sBegin, float tBegin, //����O���W����s�Bt��
    		float sEnd, float tEnd, //����O�k�U����s�Bt��
    		int texId //��_������O�Ϥ�id
    )
    {
    	this.x2D = x2D;
    	this.y2D = y2D;
    	this.width2D = width2D;
    	this.height2D = height2D;
    	this.addedTouchScaleX = addedTouchScaleX;
    	this.addedTouchScaleY = addedTouchScaleY;
    	this.sBegin = sBegin;
    	this.tBegin = tBegin;
    	this.sEnd = sEnd;
    	this.tEnd = tEnd;
    	this.texId = texId;
    	
    	//�N����O����m�B�ؤo�ഫ��3D�@�ɤ�����m�M�ؤo
    	changePositionAndScaleFrom2DTo3D();
    	//�_�l�Ƴ��I�y�лP�ۦ���
    	initVertexData();
    	//�_�l��shader        
    	intShader(mv);
    }
    //�N����O����m�B�ؤo�ഫ��3D�@�ɤ�����m�M�ؤo����k
    public void changePositionAndScaleFrom2DTo3D(){
    	/*
    	 * �p��2D��3D�@�ɤ��ؤo�����
    	 * 
    	 * �ھڥ����v�x�}�����ѼƭȡG
         * MatrixState.setProjectFrustum(-Constant.RATIO, Constant.RATIO, -1, 1, 1, 10);
         * �i��o�ഫ��Ҭ��G
         * ratio2DTo3D = (top+bottom)/Constant.SCREEN_HEIGHT
    	 */
    	ratio2DTo3D = 2.0f/Constant.SCREEN_HEIGHT;
    	//�e�װ��׵�����ܴ�
    	width = width2D * ratio2DTo3D;
    	height = height2D * ratio2DTo3D;
    	/*
    	 * �ù�����m�z�L����O�����W�I�y�Ш�2D�M3D�@�ɤ������ߦ�m���Z������Ҩӭp��
    	 * 3D�@�ɤ������ߦ�m�b�@�ɮy�Шt���I
    	 * 2D�@�ɤ������ߦ�m�b�ù���������
    	 */
    	x = -(ratio2DTo3D * (Constant.SCREEN_WIDTH/2.0f - x2D) - width/2.0f);
    	y = ratio2DTo3D * (Constant.SCREEN_HEIGHT/2.0f - y2D) - height/2.0f;
    }
    //�_�l�Ƴ��I�y�лP�ۦ��ƪ���k
    public void initVertexData()
    {
    	//���I�y�и�ƪ��_�l��================begin============================
        vCount=6;
       
        float vertices[]=new float[]
        {
        	-width/2.0f, height/2.0f, 0,
        	-width/2.0f, -height/2.0f, 0,
        	width/2.0f, height/2.0f, 0,
        	
        	-width/2.0f, -height/2.0f, 0,
        	width/2.0f, -height/2.0f, 0,
        	width/2.0f, height/2.0f, 0,
        };
		
        //�إ߳��I�y�и�ƽw�R
        //vertices.length*4�O�]���@�Ӿ�ƥ|�Ӧ줸��
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mVertexBuffer = vbb.asFloatBuffer();//�ରFloat���w�R
        mVertexBuffer.put(vertices);//�V�w�R�Ϥ���J���I�y�и��
        mVertexBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        //�S�O���ܡG�ѩ󤣦P���x�줸�ն��Ǥ��P��Ƴ椸���O�줸�ժ��@�w�n�g��ByteBuffer
        //�ഫ�A����O�n�z�LByteOrder�]�wnativeOrder()�A�_�h���i��|�X���D
        //���I�y�и�ƪ��_�l��================end============================
        
        //���I���z�y�и�ƪ��_�l��================begin============================
        float texCoor[]=new float[]//���z�y��
        {
        		sBegin,tBegin, sBegin,tEnd, sEnd,tBegin,
        		sBegin,tEnd, sEnd,tEnd, sEnd,tBegin        		
        };        
        //�إ߳��I���z�y�и�ƽw�R
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mTexCoorBuffer = cbb.asFloatBuffer();//�ରFloat���w�R
        mTexCoorBuffer.put(texCoor);//�V�w�R�Ϥ���J���I�ۦ���
        mTexCoorBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        //�S�O���ܡG�ѩ󤣦P���x�줸�ն��Ǥ��P��Ƴ椸���O�줸�ժ��@�w�n�g��ByteBuffer
        //�ഫ�A����O�n�z�LByteOrder�]�wnativeOrder()�A�_�h���i��|�X���D
        //���I���z�y�и�ƪ��_�l��================end============================

    }

    //�_�l��shader
    public void intShader(MySurfaceView mv)
    {
    	//���o�ۭq�ۦ�ް������id 
        mProgram = ShaderManager.getTexBoardShader();
        //���o�{�������I��m�ݩʰѦ�id  
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        //���o�{�������I���z�y���ݩʰѦ�id  
        maTexCoorHandle= GLES20.glGetAttribLocation(mProgram, "aTexCoor");
        //���o�{�����`�ܴ��x�}�Ѧ�id
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix"); 
        //���o�{��������O�O�_�z�����ݩʰѦ�id
        muIsTransparent=GLES20.glGetUniformLocation(mProgram, "isTransparent"); 
    }
    
    public void drawSelf()
    {
    	//�O�@�x�}
    	MatrixState.pushMatrix();

		// �N����O�h����3D�@�ɤ������w��m
		MatrixState.translate(x, y, 0);

		// ��w�ϥάY�Mshader�{��
		GLES20.glUseProgram(mProgram);
		// �N�̲��ܴ��x�}�ǤJshader�{��
		GLES20.glUniformMatrix4fv
		(
				muMVPMatrixHandle, 
				1, 
				false,
				MatrixState.getFinalMatrix(), 
				0
		);
		// ���e�����w���I��m���
		GLES20.glVertexAttribPointer
		(
				maPositionHandle, 
				3, 
				GLES20.GL_FLOAT,
				false, 
				3 * 4, 
				mVertexBuffer
		);
		// ���e�����w���I���z�y�и��
		GLES20.glVertexAttribPointer
		(
				maTexCoorHandle, 
				2, 
				GLES20.GL_FLOAT,
				false, 
				2 * 4, 
				mTexCoorBuffer
		);
		// �e�\���I��m��ư}�C
		GLES20.glEnableVertexAttribArray(maPositionHandle);
		GLES20.glEnableVertexAttribArray(maTexCoorHandle);
		// �j�w���z
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		
		//�ǻ��Q���U���Ϥ�id
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
		//�N����O�O�_�z�����ݩʶǤJshader�{�� 
        GLES20.glUniform1i(muIsTransparent, isTransparent);
		// ø��T����
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
		
		//�٭�x�}
        MatrixState.popMatrix(); 
	}
    //���s�_�l�Ư��z�y�Ъ���k
    public void initTexCoor(float sBegin, float tBegin,float sEnd,float tEnd)
    {
        //���I���z�y�и�ƪ��_�l��================begin============================
        float texCoor[]=new float[]//���z�y��
        {
        		sBegin,tBegin, sBegin,tEnd, sEnd,tBegin,
        		sBegin,tEnd, sEnd,tEnd, sEnd,tBegin        		
        };        
        //�إ߳��I���z�y�и�ƽw�R
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());//�]�w�줸�ն���
        mTexCoorBuffer = cbb.asFloatBuffer();//�ରFloat���w�R
        mTexCoorBuffer.put(texCoor);//�V�w�R�Ϥ���J���I�ۦ���
        mTexCoorBuffer.position(0);//�]�w�w�R�ϰ_�l��m
        //�S�O���ܡG�ѩ󤣦P���x�줸�ն��Ǥ��P��Ƴ椸���O�줸�ժ��@�w�n�g��ByteBuffer
        //�ഫ�A����O�n�z�LByteOrder�]�wnativeOrder()�A�_�h���i��|�X���D
        //���I���z�y�и�ƪ��_�l��================end============================
        
    }
    //���ܯ��z�y�СAø���O����k
    public void drawSelf(float sBegin, float tBegin,float sEnd,float tEnd)
    {
    	//���s�_�l�Ư��z�y�и��
    	initTexCoor(sBegin, tBegin, sEnd, tEnd);    	
    	//�O�@�x�}
    	MatrixState.pushMatrix();
		// �N����O�h����3D�@�ɤ������w��m
		MatrixState.translate(x, y, 0);

		// ��w�ϥάY�Mshader�{��
		GLES20.glUseProgram(mProgram);
		// �N�̲��ܴ��x�}�ǤJshader�{��
		GLES20.glUniformMatrix4fv
		(
				muMVPMatrixHandle, 
				1, 
				false,
				MatrixState.getFinalMatrix(), 
				0
		);
		// ���e�����w���I��m���
		GLES20.glVertexAttribPointer
		(
				maPositionHandle, 
				3, 
				GLES20.GL_FLOAT,
				false, 
				3 * 4, 
				mVertexBuffer
		);
		// ���e�����w���I���z�y�и��
		GLES20.glVertexAttribPointer
		(
				maTexCoorHandle, 
				2, 
				GLES20.GL_FLOAT,
				false, 
				2 * 4, 
				mTexCoorBuffer
		);
		// �e�\���I��m��ư}�C
		GLES20.glEnableVertexAttribArray(maPositionHandle);
		GLES20.glEnableVertexAttribArray(maTexCoorHandle);
		// �j�w���z
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		
		//�ǻ��Q���U���Ϥ�id
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);

		// ø��T����
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
		
		//�٭�x�}
        MatrixState.popMatrix(); 
	}
	//�P�_����O�W�O�_��Ĳ���ƥ󪺤�k
	public boolean isActionOnBoard(float pressX, float pressY)
	{
		if(
				pressX > x2D - addedTouchScaleX &&
				pressX < x2D + width2D + addedTouchScaleX && 
				pressY > y2D - addedTouchScaleY &&
				pressY < y2D + height2D + addedTouchScaleY
		)
		{
			return true;			
		}
		return false;
	}
}
