package com.fish.canvas;


/*
 * ����ӿ�
 */
public interface Matrix {
	/*
	 * ����ƽ��
	 */
	public void setTranslate(float x, float y);
	/*
	 * ���þ���
	 */
	public void reset();
	/*
	 * ����
	 */
	public void preScale(float x,float y);
	/*
	 * ��ת
	 */
	public void preRotate(float angle,float x,float y);
}
