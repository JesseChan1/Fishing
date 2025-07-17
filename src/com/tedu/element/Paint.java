package com.fish.canvas;

/*
 * ��������
 */
public interface Paint {
void setTypeface(Object obj); // ����Paint������

void setAntiAlias(boolean tf);

void setFilterBitmap(boolean tf);

void setDither(boolean tf);

void setTextSize(int size);  // ���ݲ�ͬ�ֱ������������С

void setColor(int color);
}
