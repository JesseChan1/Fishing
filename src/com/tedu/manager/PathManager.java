package com.fish.manager;

import com.fish.model.GamingInfo;

/**
 * ·��������
 *
 * @author Xiloer
 */
public class PathManager {
public static int PATH_MODE_STRAIGHT = 1;    //�н�ģʽ:ֱ��
public static int PATH_MODE_ROTATE = 0;        //�н�ģʽ:ת��

/**
 * ��ȡһ��Ĭ��·������ʽΪ{{�ƶ���ʽ,��С},...}
 * Ĭ��·��ֻ��4��Ԫ�أ���������껹û�е�����Ļ��Ե��������ٵ����������
 *
 * @param maxRotate �����ת�Ƕ�
 * @return
 */
public static int[][] getDefaultPath(HeadFish fish) {
    //�����������ת�Ƕ�Ϊ0��ֻ����ֱ��
    if (fish.getFish().getFishInfo().getMaxRotate() == 0) {
        return new int[][]{{PATH_MODE_STRAIGHT, GamingInfo.getGamingInfo().getScreenWidth() * 2}};
    }
    int[][] path = new int[4][2];
    int i = 0;
    //����㻹û�д���Ļ����
    if (fish.getFish_X() + fish.getFish().getPicWidth() <= 0 || fish.getFish_X() >= GamingInfo.getGamingInfo().getScreenWidth()) {
        path[0][0] = PATH_MODE_STRAIGHT;
        path[0][1] = (int) (Math.random() * (GamingInfo.getGamingInfo().getScreenWidth()) + 1);
        i = 1;
    }
    for (; i < 4; i++) {
        if (Math.random() > 0.5) {
            if (i > 0 && path[i - 1][0] == PATH_MODE_STRAIGHT) {
                //ת��
                path[i][0] = PATH_MODE_ROTATE;
                path[i][1] = (int) (45 + Math.random() * (fish.getFish().getFishInfo().getMaxRotate() - 45) + 1);    //������������С45�ȣ����Ϊ��������ת�Ƕȣ����Ǻ��ڿ��Ǹĳ����90��
            } else {
                //ֱ��
                path[i][0] = PATH_MODE_STRAIGHT;
                path[i][1] = (int) (30 + Math.random() * (GamingInfo.getGamingInfo().getScreenHeight() / 2) + 1);    //�н�·�����Ϊ��Ļ�߶ȵ�һ�룬��ʵ��׼ȷ������Ҳ����̫����ֵ
            }
        } else {
            if (i > 0 && path[i - 1][0] == PATH_MODE_ROTATE) {
                //ֱ��
                path[i][0] = PATH_MODE_STRAIGHT;
                path[i][1] = (int) (30 + Math.random() * (GamingInfo.getGamingInfo().getScreenHeight() / 2) + 1);    //�н�·�����Ϊ��Ļ�߶ȵ�һ�룬��ʵ��׼ȷ������Ҳ����̫����ֵ
            } else {
                //ת��
                path[i][0] = PATH_MODE_ROTATE;
                path[i][1] = (int) (45 + Math.random() * (fish.getFish().getFishInfo().getMaxRotate() - 45) + 1);
            }
        }
    }
    return path;
}

/**
 * ��ȡһ��·������ʽΪ{{�ƶ���ʽ,��С},...}
 * ���������Ҫ��Ϊ�Ժ�����ؿ�Ԥ��������תȦ��Ӿ,�˷�����ʱ��ʵ��
 *
 * @param Model ģʽ
 * @return
 */
public static int[][] getPath(int Model) {

    return null;
}

private PathManager() {
}
}
