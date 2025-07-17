package com.fish.manager;

import java.util.ArrayList;

import com.fish.model.Fish;
import com.fish.threads.FishRunThread;

/**
 * ��ͷ��
 * ����಻��ʵ�ʵ��㣬����һ���㣬����������Ⱥ�ƶ�
 * @author Xiloerfan
 *
 */
public class HeadFish {
	private int[] fishOutlinePoint = new int[4];		//�����Ӿ��Σ�x����Сֵ�����ֵ��Y����Сֵ�����ֵ
	//�������ƶ����߳�
	private FishRunThread fishRunThread;
	private boolean isNew = true;						//�Ƿ�����ɵ���	������������Ž�����Ļʱ���·��
	private float fish_x;								//�㵱ǰ��X����
	private float fish_y;								//�㵱ǰ��Y����
	private int currentRotate;							//�㵱ǰ����ת�ĽǶ�
	private float lastX;								//���һ����ת���X����	����XY����������ת������ֱ�ߣ�����������ֵ�L
	private float lastY;								//���һ����ת���Y����	�����Ϳ�����
	private int rotateDirection;						//��ת������ת	���ֵ����;���ڣ�������ת����ֱ��ʱ��Ҫ�������һ����ת����������������¼���ϴ�����ת������ת���ڼ���Ƕȵ�ֱ֪��ʱ������
	//��ǰ��Ⱥ���㣬��Ⱥ���㶼����Ϊ���գ�ͬ�������Ҳ����Ⱥ������
	private Fish fish;
	//��Ⱥ
	private ArrayList<Fish> shoal = new ArrayList<Fish>();
	//��ǰ��������ͷ�����ʼλ��
	private int currentFromPoint;
	public ArrayList<Fish> getShoal() {
		return shoal;
	}
	public void setShoal(ArrayList<Fish> shoal) {
		this.shoal = shoal;
	}
	public int getCurrentFromPoint() {
		return currentFromPoint;
	}
	public void setCurrentFromPoint(int currentFromPoint) {
		this.currentFromPoint = currentFromPoint;
	}
	public Fish getFish() {
		return fish;
	}
	public void setFish(Fish fish) {
		this.fish = fish;
		this.shoal.add(fish);
	}
	/*
	 * ���X,Y�����getter and setter
	 *
	 * */
	public void setFish_X(float x) {
		// TODO Auto-generated method stub
		this.setLastX(this.fish_x);
		this.fish_x = x;
	}
	public void setFish_Y(float y) {
		// TODO Auto-generated method stub
		this.setLastY(this.fish_y);
		this.fish_y = y;
	}
	public float getFish_X() {
		// TODO Auto-generated method stub
		return this.fish_x;
	}
	public float getFish_Y() {
		// TODO Auto-generated method stub
		return this.fish_y;
	}
	public float getLastX() {
		return lastX;
	}
	public void setLastX(float lastX) {
		this.lastX = lastX;
	}
	public float getLastY() {
		return lastY;
	}
	public void setLastY(float lastY) {
		this.lastY = lastY;
	}
	public int getCurrentRotate() {
		return currentRotate;
	}
	public void setCurrentRotate(int currentRotate) {
		if(currentRotate>=360||currentRotate<=-360){
			this.currentRotate = 0;
		}else{
			this.currentRotate = currentRotate;
		}
	}
	public FishRunThread getFishRunThread() {
		return fishRunThread;
	}
	public void setFishRunThread(FishRunThread fishRunThread) {
		this.fishRunThread = fishRunThread;
	}
	public int getRotateDirection() {
		return rotateDirection;
	}
	public void setRotateDirection(int rotateDirection) {
		this.rotateDirection = rotateDirection;
	}
	public boolean isNew() {
		return isNew;
	}
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	public void clearFishOutlinePoint(){
		fishOutlinePoint[0] = 0;
		fishOutlinePoint[1] = 0;
		fishOutlinePoint[2] = 0;
		fishOutlinePoint[3] = 0;
	}
	public int[] getFishOutlinePoint() {
		return fishOutlinePoint;
	}
	public void setFishOutlinePoint(int[] fishOutlinePoint) {
		this.fishOutlinePoint = fishOutlinePoint;
	}
}
