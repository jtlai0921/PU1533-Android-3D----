package tstc.fxq.parts;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import tstc.fxq.main.MySurfaceView;
import tstc.fxq.utils.MatrixState;
import tstc.fxq.utils.ShaderManager;
import android.opengl.GLES20;
import static tstc.fxq.constants.Constant.*;

public class Qiang {
	int mProgram;// �ۭq�ۦ�ް������id
	int muMVPMatrixHandle;// �`�ܴ��x�}�Ѧ�id
	int muMMatrixHandle;// ��m�B�����ܴ��x�}
	int maCameraHandle; // ��v����m�ݩʰѦ�id
	int maPositionHandle; // ���I��m�ݩʰѦ�id
	int maNormalHandle; // ���I�k�V�q�ݩʰѦ�id
	int maTexCoorHandle; // ���I���z�y���ݩʰѦ�id
	int maSunLightLocationHandle;// ������m�ݩʰѦ�id

	private FloatBuffer mVertexBuffer;// ���I�y�и�ƽw�R
	private FloatBuffer mNormalBuffer;// ���I�k�V�q��ƽw�R
	private FloatBuffer mTexCoorBuffer; // ���I���z��ƽw�R
	int vCount = 0;
	float xOffset;

	public Qiang(MySurfaceView mv) {
		float vertices[] = new float[] {
				// �᭱
				-WALL_SIZE, WALL_SIZE, -WALL_SIZE,// 5
				-WALL_SIZE, 0, -WALL_SIZE,// 1
				WALL_SIZE, 0, -WALL_SIZE,// 2

				-WALL_SIZE, WALL_SIZE, -WALL_SIZE,// 5
				WALL_SIZE, 0, -WALL_SIZE,// 2
				WALL_SIZE, WALL_SIZE, -WALL_SIZE,// 6
				// �e��
				-WALL_SIZE, 0, WALL_SIZE,// 3
				-WALL_SIZE, WALL_SIZE, WALL_SIZE,// 7
				WALL_SIZE, WALL_SIZE, WALL_SIZE,// 8

				-WALL_SIZE, 0, WALL_SIZE,// 3
				WALL_SIZE, WALL_SIZE, WALL_SIZE,// 8
				WALL_SIZE, 0, WALL_SIZE,// 4
				// ����
				-WALL_SIZE, WALL_SIZE, -WALL_SIZE,// 5
				-WALL_SIZE, WALL_SIZE, WALL_SIZE,// 7
				-WALL_SIZE, 0, WALL_SIZE,// 3

				-WALL_SIZE, WALL_SIZE, -WALL_SIZE,// 5
				-WALL_SIZE, 0, WALL_SIZE,// 3
				-WALL_SIZE, 0, -WALL_SIZE,// 1
				// �k��
				WALL_SIZE, 0, -WALL_SIZE,// 2
				WALL_SIZE, 0, WALL_SIZE,// 4
				WALL_SIZE, WALL_SIZE, WALL_SIZE,// 8

				WALL_SIZE, 0, -WALL_SIZE,// 2
				WALL_SIZE, WALL_SIZE, WALL_SIZE,// 8
				WALL_SIZE, WALL_SIZE, -WALL_SIZE,// 6
				// ����
				-WALL_SIZE, 0, -WALL_SIZE,// 1
				-WALL_SIZE, 0, WALL_SIZE,// 3
				WALL_SIZE, 0, WALL_SIZE,// 4

				-WALL_SIZE, 0, -WALL_SIZE,// 1
				WALL_SIZE, 0, WALL_SIZE,// 4
				WALL_SIZE, 0, -WALL_SIZE,// 2
		};
		// �إ߳��I�y�и�ƽw�R
		// vertices.length*4�O�]���@�Ӿ�ƥ|�Ӧ줸��
		vCount = vertices.length / 3;
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());// �]�w�줸�ն���
		mVertexBuffer = vbb.asFloatBuffer();// �ରint���w�R
		mVertexBuffer.put(vertices);// �V�w�R�Ϥ���J���I�y�и��
		mVertexBuffer.position(0);// �]�w�w�R�ϰ_�l��m
		// �S�O���ܡG�ѩ󤣦P���x�줸�ն��Ǥ��P��Ƴ椸���O�줸�ժ��@�w�n�g��ByteBuffer
		// �ഫ�A����O�n�z�LByteOrder�]�wnativeOrder()�A�_�h���i��|�X���D

		// ���I�k�V�q���
		float normals[] = new float[] {
				// �᭱
				0, 0, 1, 0, 0, 1, 0, 0, 1,

				0, 0, 1, 0, 0, 1, 0, 0, 1,
				// �e��
				0, 0, -1, 0, 0, -1, 0, 0, -1,

				0, 0, -1, 0, 0, -1, 0, 0, -1,
				// ����
				1, 0, 0, 1, 0, 0, 1, 0, 0,

				1, 0, 0, 1, 0, 0, 1, 0, 0,
				// �k��
				-1, 0, 0, -1, 0, 0, -1, 0, 0,

				-1, 0, 0, -1, 0, 0, -1, 0, 0,
				// ����
				0, 1, 0, 0, 1, 0, 0, 1, 0,

				0, 1, 0, 0, 1, 0, 0, 1, 0, };
		// �إ�ø��I�k�V�q�w�R
		ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length * 4);
		nbb.order(ByteOrder.nativeOrder());// �]�w�줸�ն���
		mNormalBuffer = nbb.asFloatBuffer();// �ରfloat���w�R
		mNormalBuffer.put(normals);// �V�w�R�Ϥ���J���I�y�и��
		mNormalBuffer.position(0);// �]�w�w�R�ϰ_�l��m

		// ���z�y�и��
		float textureCoors[] = new float[] {
				// �᭱
				0f,
				0f, // 0
				0f,
				0.496f,// 1
				1f,
				0.496f,// 2

				0f,
				0f, // 0
				1f,
				0.496f,// 2
				1f,
				0f,// 3
				// �e��
				0f,
				0.496f,// 1
				0f,
				0f, // 0
				1f,
				0f,// 3

				0f,
				0.496f,// 1
				1f,
				0f,// 3
				1f,
				0.496f,// 2
				// ����
				0f,
				0f, // 0
				1f,
				0f,// 3
				1f,
				0.496f,// 2

				0f,
				0f, // 0
				1f,
				0.496f,// 2
				0f,
				0.496f,// 1
				// �k��
				0f,
				0.496f,// 1
				1f,
				0.496f,// 2
				1f,
				0f,// 3

				0f,
				0.496f,// 1
				1f,
				0f,// 3
				0f,
				0f, // 0
				// ����
				0, 0.496f, 0, 1f, 0.797f, 1f, 0, 0.496f, 0.797f, 1f, 0.797f,
				0.496f };

		// �إ߳��I���z��ƽw�R
		// textureCoors.length��4�O�]���@��float����ƥ|�Ӧ줸��
		ByteBuffer cbb = ByteBuffer.allocateDirect(textureCoors.length * 4);
		cbb.order(ByteOrder.nativeOrder());// �]�w�줸�ն���
		mTexCoorBuffer = cbb.asFloatBuffer();// �ରint���w�R
		mTexCoorBuffer.put(textureCoors);// �V�w�R�Ϥ���J���I�ۦ���
		mTexCoorBuffer.position(0);// �]�w�w�R�ϰ_�l��m
		// �S�O���ܡG�ѩ󤣦P���x�줸�ն��Ǥ��P��Ƴ椸���O�줸�ժ��@�w�n�g��ByteBuffer
		// �ഫ�A����O�n�z�LByteOrder�]�wnativeOrder()�A�_�h���i��|�X���D
		// ���I���z��ƪ��_�l��================end============================*/

		// �_�l��shader
		intShader(mv);
	}

	// �_�l��shader
	public void intShader(MySurfaceView mv) {
		// ���o�ۭq�ۦ�ް������id
		mProgram = ShaderManager.getTexLightShader();
		// ���o�{�������I��m�ݩʰѦ�id
		maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		// ���o�{�������I�g�n���ݩʰѦ�id
		maTexCoorHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoor");
		// ���o�{�������I�k�V�q�ݩʰѦ�id
		maNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");
		// ���o�{�����`�ܴ��x�}�Ѧ�id
		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		// ���o�{������v����m�Ѧ�id
		maCameraHandle = GLES20.glGetUniformLocation(mProgram, "uCamera");
		// ���o�{����������m�Ѧ�id
		maSunLightLocationHandle = GLES20.glGetUniformLocation(mProgram,
				"uLightLocationSun");
		// ���o��m�B�����ܴ��x�}�Ѧ�id
		muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");
	}

	public void drawSelf(int texId) {

		MatrixState.rotate(xOffset, 1, 0, 0);

		// ��w�ϥάY�Mshader�{��
		GLES20.glUseProgram(mProgram);
		// �N�̲��ܴ��x�}�ǤJshader�{��
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,
				MatrixState.getFinalMatrix(), 0);
		// �N��m�B�����ܴ��x�}�ǤJshader�{��
		GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false,
				MatrixState.getMMatrix(), 0);
		// �N��v����m�ǤJshader�{��
		GLES20.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);
		// �N������m�ǤJshader�{��
		GLES20.glUniform3fv(maSunLightLocationHandle, 1,
				MatrixState.lightPositionFB);

		// ���e�����w���I��m���
		GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT,
				false, 3 * 4, mVertexBuffer);
		// ���e�����w���I�g�n�׸��
		GLES20.glVertexAttribPointer(maTexCoorHandle, 2, GLES20.GL_FLOAT,
				false, 2 * 4, mTexCoorBuffer);
		// ���e�����w���I�k�V�q���
		GLES20.glVertexAttribPointer(maNormalHandle, 3, GLES20.GL_FLOAT, false,
				3 * 4, mNormalBuffer);
		// �e�\���I��m��ư}�C
		GLES20.glEnableVertexAttribArray(maPositionHandle);
		GLES20.glEnableVertexAttribArray(maTexCoorHandle);
		GLES20.glEnableVertexAttribArray(maNormalHandle);
		// �j�w���z
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
		// ø��T����
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
	}
}
