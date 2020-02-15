precision mediump float;
uniform float uColorId;
uniform float uDaoJuA;
uniform float uDaoJuALevel;
varying vec4 vAmbient;//�����q���I�ۦ⾹�L�Ӫ����ҥ����q
varying vec4 vDiffuse;//�����q���I�ۦ⾹�L�Ӫ����g�����q
varying vec4 vSpecular;//�����q���I�ۦ⾹�L�Ӫ��譱�Ϯg�����q
void main() {  
    vec4 finalColor;
   if(uColorId==0.0){
    finalColor = vec4(1.0,1.0,1.0,1.0);//���������m���
   }else if(uColorId==1.0)
   {
   finalColor= vec4(1.0,1.0,1.0,1.0);//���������m���
   }else if(uColorId==2.0)
   {
   finalColor= vec4(0.0,1.0,0.0,1.0);//���������m���
   }else
   {
    finalColor= vec4(1.0,1.0,0.0,1.0);//���������m���
   }
   if(uDaoJuALevel==0.0)
   {
    finalColor.a=0.2*uDaoJuA;
   }else if(uDaoJuALevel==1.0)
   {
   finalColor.a=1.0-0.05*(uDaoJuA-5.0);
   }
  
  gl_FragColor=finalColor*vAmbient + finalColor*vDiffuse + finalColor*vSpecular;
}