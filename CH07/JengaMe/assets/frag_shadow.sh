precision mediump float;				//����Ĭ�ϵĸ��㾫��
uniform highp int isShadow;			//��Ӱ���Ʊ�־
varying vec4 ambient;  				//�Ӷ�����ɫ�����ݹ����Ļ���������ǿ��
varying vec4 diffuse;					//�Ӷ�����ɫ�����ݹ�����ɢ�������ǿ��
varying vec4 specular;				//�Ӷ�����ɫ�����ݹ����ľ��������ǿ��
varying vec2 vTextureCoord;//���մӶ�����ɫ�������Ĳ���
uniform sampler2D sTexture;//������������
void main() { 
   	if(isShadow==0){						//�������屾��
	    vec4 finalColor=texture2D(sTexture, vTextureCoord); 
	    //�ۺ�����ͨ���������ǿ�ȼ�ƬԪ����ɫ���������ƬԪ����ɫ�����ݸ�����
	    gl_FragColor = finalColor*ambient+finalColor*specular+finalColor*diffuse;
   	}else{								//������Ӱ
	   gl_FragColor = vec4(0.1,0.1,0.1,0.09);//ƬԪ������ɫΪ��Ӱ����ɫ
   	}
}     
