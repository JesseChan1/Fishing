package com.fish.model.interfaces;

import com.fish.canvas.Bitmap;
import com.fish.canvas.Canvas;
import com.fish.canvas.Matrix;
import com.fish.canvas.Paint;

public interface Drawable {
Matrix getPicMatrix();//��ȡͼƬ��ת�ľ����ʾ

Bitmap getCurrentPic();//��ȡ��ǰ����ͼƬ����Դ

int getPicWidth();//����ͼƬ�Ŀ��

int getPicHeight();//����ͼƬ�ĸ߶�

void onDraw(Canvas canvas, Paint paint);//���ƵĻص�����
}
