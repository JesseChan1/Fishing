package com.fish.src;

public class Constant {
	public static int PIC_WIDTH = 64;
	public static int PIC_HEIGHT = 64;
	public static int ON_DRAW_SLEEP = 40;				//ÿ���ӵ�֡��
	public static float PIC_X_SPEED = 1.5f;
	public static float PIC_Y_SPEED = 2;
	public static int PIC_RUN_SPEED = 1000/Constant.ON_DRAW_SLEEP;
//	public static int  SCALE_BASE_NUM = 850;			//���Ż���

	//����Ļ�е���Ⱥ����
	public static final int FISH_SUM_IN_SCREEN = 7;

	// �����ʼλ�ö���
	public static final int FROM_TOP_LEFT = 0; 			// ����
	public static final int FROM_BOTTOM_LEFT = 1; 		// ����
	public static final int FROM_TOP_RIGHT = 2; 		// ����
	public static final int FROM_BOTTOM_RIGHT = 3; 		// ����


	public static double RAD	= Math.PI / 180;		//���ȼ��㹫ʽ


	public static int FISHING_NET_SHOW_TINE = 1500;		//�������ֵĺ�����

	//�����ӵ��ƶ��ٶ�
	public static int AMMO_SPEED = 200;					//ÿ����200����

	//�������ƶ��ٶ�
	public static int Gold_SPEED = 200;					//ÿ����150����

	//�����ڵ����
	public static int CANNON_RELOAD_TIME = 200;			//50����

	//�Զ���Ǯ���
	public static int GIVE_GOLD_TIME = 180;				//180��
	//����ҵĵ��ߣ�ֻ�е�ǰ�����С����������Ų����
	public static int GIVE_GOLD_LESS = 100;				//����100��ң��Ų����
	//��������
	public static int GIVE_GOLD = 100;				   //����100���

	/****************************����*****************************/
	//������ɫ��
	public static int PARTICLE_COLOR_SUM = 10;
	//��������
	public static int PARTICLE_SUM = 10;
	//�ӵ���������
	public static int AMMO_PARTICLE_SUM = 10;
	//�����ƶ�Ƶ��
	public static long PARTICLE_RUN = 30;
	/*******************�����������ͼ����ض���*********************/
	//ˮ����ͼ��
	public static int HUNDRED_WATER_RIPPLE_LAYER = 103;
	//�ٷ�ͼ��
	public static int HUNDRED_POINT_LAYER = HUNDRED_WATER_RIPPLE_LAYER-1;
	//�߷�ͼ��
	public static int HIGH_POINT_LAYER = HUNDRED_POINT_LAYER-1;
	//��������Ч������ͼ��
	public static int CHANGE_CANNON_EFFECT_LAYER = HIGH_POINT_LAYER-1;
	//��������ͼ��
	public static int GUN_LAYER = CHANGE_CANNON_EFFECT_LAYER-1;
	//����ͼ��
	public static int PARTICLE_EFFECT_LAYER = GUN_LAYER-1;
	//��������ͼ��
	public static int FISH_NET_LAYER = PARTICLE_EFFECT_LAYER-1;
	//�ӵ�����ͼ��
	public static int AMMO_LAYER = FISH_NET_LAYER-1;
	//�����л����������ͼ��
	public static int COMPONENTS_LAYER = AMMO_LAYER-1;
	//���ͼ��
	public static int GOLD_LAYER = COMPONENTS_LAYER-1;
	//����������ͼ��
	public static int LOAD_PROGRESS_LAYER = 999;
	//����ͼ��
	public static int BACK_GROUND_LAYER = 0;


}
