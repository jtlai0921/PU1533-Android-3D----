package com.f.util;

import android.content.res.Resources;

import android.opengl.GLES20;

import android.util.Log;

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ShaderUtil {

    /**
     * �إ�shaderProgram�{������k
     * @param vertexSource ���I���O�Z
     * @param fragmentSource �������O�Z
     * @return shaderProgram��id
     */
    public static int createProgram(String vertexSource, String fragmentSource) {

        // ���J���I�ۦ⾹
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);

        if (vertexShader == 0){
            return 0;
        }

        // ���J�����ۦ⾹
        int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);

        if (pixelShader == 0) {
            return 0;
        }

        // �إߵ{��
        int program = GLES20.glCreateProgram();

        // �Y�{���إߦ��\�h�V�{�����[�J���I�ۦ⾹�P�����ۦ⾹
        if (program != 0) {

            // �V�{�����[�J���I�ۦ⾹
            GLES20.glAttachShader(program, vertexShader);
            checkGlError("glAttachShader");

            // �V�{�����[�J�����ۦ⾹
            GLES20.glAttachShader(program, pixelShader);
            checkGlError("glAttachShader");

            // �s���{��
            GLES20.glLinkProgram(program);

            // �s��s�����\program�ƶq���}�C
            int[] linkStatus = new int[1];

            // ���oprogram���s�����p
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);

            // �Y�s�����ѫh��ܥX���ò����{��
            if (linkStatus[0] != GLES20.GL_TRUE) {
                Log.e("ES20_ERROR", "Could not link program: ");
                Log.e("ES20_ERROR", GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }

        return program;
    }

    /**
     *  �إ�shader����k
     * @param shaderType shader�����A  GLES20.GL_VERTEX_SHADER   GLES20.GL_FRAGMENT_SHADER
     * @param source shader�����O�Z�r��
     * @return shader��id
     */
    private static int loadShader(int shaderType, String source) {

        // �إߤ@��shaderType���A���sshader
        int shader = GLES20.glCreateShader(shaderType);

        // �Y�إߦ��\�h���Jshader
        if (shader != 0) {

            // ���Jshader�����{���X
            GLES20.glShaderSource(shader, source);

            // �sĶshader
            GLES20.glCompileShader(shader);

            // �s��sĶ���\shader�ƶq���}�C
            int[] compiled = new int[1];

            // ���oShader���sĶ���p
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);

            if (compiled[0] == 0) {    // �Y�sĶ���ѫh��ܿ��~��Өò�����shader
                Log.e("ES20_ERROR", "Could not compile shader " + shaderType + ":");
                Log.e("ES20_ERROR", GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }

        return shader;
    }

    /**
     * �ˬd�C�@�B�ʧ@�O�_�����~����k
     * @param op
     */
    private static void checkGlError(String op) {
        int error;

        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("ES20_ERROR", op + ": glError " + error);

            throw new RuntimeException(op + ": glError " + error);
        }
    }

    /**
     * ��IO�qAssets�ؿ��UŪ���ɮ�(Ū��*.sh�r��)
     * @param fname �ɦW
     * @param r �귽
     * @return *.sh�r��
     */
    public static String loadFromAssetsFile(String fname, Resources r) {
        String result = null;

        try {
            InputStream           in   = r.getAssets().open(fname);
            int                   ch   = 0;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            while ((ch = in.read()) != -1) {
                baos.write(ch);
            }

            byte[] buff = baos.toByteArray();

            baos.close();
            in.close();
            result = new String(buff, "UTF-8");
            result = result.replaceAll("\\r\\n", "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
