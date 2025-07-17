package com.fish.model.componets;

import com.fish.canvas.Canvas;
import com.fish.canvas.Paint;
import com.fish.model.DrawableAdapter;
/**
 * ����ĸ���
 * @author Xiloerfan
 *
 */
public abstract class Componet extends DrawableAdapter{
	/**
	 * ���������Ļ��x,y����
	 */
	private float layoutX;
	private float layoutY;

	/**
	 * ��ȡ�������Ļ��X����
	 * @return
	 */
	public float getLayoutX() {
		return layoutX;
	}
	/**
	 * �����������Ļ��X����
	 * @param layoutX
	 */
	public void setLayoutX(float layoutX) {
		this.layoutX = layoutX;
	}
	/**
	 * ��ȡ�������Ļ��Y����
	 * @return
	 */
	public float getLayoutY() {
		return layoutY;
	}
	/**
	 * �����������Ļ��Y����
	 * @param layoutY
	 */
	public void setLayoutY(float layoutY) {
		this.layoutY = layoutY;
	}

	public void onDraw(Canvas canvas, Paint paint) {
		canvas.drawBitmap(this.getCurrentPic(),
				layoutX, layoutY, paint);
	}
}
