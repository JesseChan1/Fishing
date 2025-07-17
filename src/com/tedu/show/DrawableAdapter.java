package com.fish.model;

import com.fish.canvas.Canvas;
import com.fish.canvas.Matrix;
import com.fish.canvas.Paint;
import com.fish.canvas.JMatrix;
import com.fish.model.interfaces.Drawable;

public abstract class DrawableAdapter implements Drawable{
	private Matrix matrix = new JMatrix();

	public Matrix getPicMatrix() {
		// TODO Auto-generated method stub
		return matrix;
	}

	public void onDraw(Canvas canvas, Paint paint) {
		canvas.drawBitmap(this.getCurrentPic(),
				this.getPicMatrix(), paint);
	}


}
