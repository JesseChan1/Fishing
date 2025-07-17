package com.fish.model;

/**
 * ĳһ�����ϸ��������Ϣ
 * @author Xiloerfan
 *
 */
public class FishInfo {
	private int actSpeed = 200;					//�����ٶ�
	private int maxRotate = 90;					//�����ת�Ƕ�
	private int fishRunSpeed = 60;				//�ƶ��ٶ�
	private int fishShoalMax = 0;				//������Ⱥ��
	private int fishInLayer = 1;				//����ͼ��
	private int catchProbability;				//��׽����
	private int worth;							//��ֵ
	public FishInfo(){}
	public FishInfo(int actSpeed,int maxRotate,int fishRunSpeed,int fishShoalMax,int fishInLayer){
		this.actSpeed = actSpeed;
		this.maxRotate = maxRotate;
		this.fishRunSpeed = fishRunSpeed;
		this.fishShoalMax = fishShoalMax;
		this.fishInLayer = fishInLayer;
	}

	public void setActSpeed(int actSpeed) {
		this.actSpeed = actSpeed;
	}

	public void setMaxRotate(int maxRotate) {
		this.maxRotate = maxRotate;
	}

	public void setFishRunSpeed(int fishRunSpeed) {
		this.fishRunSpeed = fishRunSpeed;
	}

	public void setFishShoalMax(int fishShoalMax) {
		this.fishShoalMax = fishShoalMax;
	}

	public void setFishInLayer(int fishInLayer) {
		this.fishInLayer = fishInLayer;
	}

	/**
	 * ��ȡ���������ת�Ƕ�
	 * @return
	 */
	public int getMaxRotate() {
		// TODO Auto-generated method stub
		return maxRotate;
	}

	/**
	 * ��ȡ���ƶ��ٶ�
	 * @return
	 */
	public int getFishRunSpeed() {
		// TODO Auto-generated method stub
		return fishRunSpeed;
	}

	/**
	 * ��ȡ��Ⱥ������
	 * @return
	 */
	public int getFishShoalMax() {
		// TODO Auto-generated method stub
		return fishShoalMax;
	}

	/**
	 * ��ȡ��ǰ������ͼ���
	 * @return
	 */
	public int getFishInLayer() {
		// TODO Auto-generated method stub
		return fishInLayer;
	}


	public int getPicActSpeed() {
		// TODO Auto-generated method stub
		return this.actSpeed;
	}
	public int getCatchProbability() {
		return catchProbability;
	}
	public void setCatchProbability(int catchProbability) {
		this.catchProbability = catchProbability;
	}
	public int getWorth() {
		return worth;
	}
	public void setWorth(int worth) {
		this.worth = worth;
	}


}
