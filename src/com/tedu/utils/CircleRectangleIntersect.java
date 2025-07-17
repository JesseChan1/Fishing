package com.fish.tools;

/**
 * Բ�������ײ�����
 *
 * @author Xiloerfan
 */
public class CircleRectangleIntersect {

/**
 * @param x Բ��x
 * @param y Բ��y
 * @param X ��������X
 * @param Y ��������Y
 * @param c ���θ߶�
 * @param k ���ο��
 * @param r Բ�뾶 void 2010-6-18 author mayankai
 */

// ���������ıߣ��˺����ƺ��е�����
private static boolean sxzy(double x, double y, double X, double Y, double c,
                            double k, double r) {
    double xX = x - X;
    double yY = y - Y;
    if (((Math.abs(xX) <= k / 2 + r) && (Math.abs(yY) <= c / 2))
            || (Math.abs(xX) <= k / 2 && (y >= Y + c / 2 && y <= Y + c / 2 + r))
            || (Math.abs(xX) <= k / 2 && (y >= Y - c / 2 - r && y <= Y - c / 2))) {
        //System.out.println("�������������ı�");
        return true;
    } else {
        //System.out.println("���������������ı�");
        return false;
    }
}

// ���Ͻ�
private static boolean zsj(double x, double y, double X, double Y, double c,
                           double k, double r) {
    if ((x >= X - k / 2 - r && x <= X - k / 2)
            && (y >= Y - c / 2 - r && y <= Y - c / 2)
            && zxjl(x, y, X, Y, c, k, r)) {
        //System.out.println("�������Ͻ�");
        return true;
    } else {
        //System.out.println("���������Ͻ�");
        return false;
    }
}

// ���Ͻ�
private static boolean ysj(double x, double y, double X, double Y, double c,
                           double k, double r) {
    if ((x >= X + k / 2 && x <= X + k / 2 + r)
            && (y >= Y - c / 2 - r && y <= Y - c / 2)
            && zxjl(x, y, X, Y, c, k, r)) {
        //System.out.println("�������Ͻ�");
        return true;
    } else {
        //System.out.println("���������Ͻ�");
        return false;
    }
}

// ���½�
private static boolean zxj(double x, double y, double X, double Y, double c,
                           double k, double r) {
    if ((x >= X - k / 2 - r && x <= X - k / 2)
            && (y >= Y + c / 2 && y <= Y + c / 2 + r)
            && zxjl(x, y, X, Y, c, k, r)) {
        //System.out.println("�������½�");
        return true;
    } else {
        //System.out.println("���������½�");
        return false;
    }
}

// ���½�
private static boolean yxj(double x, double y, double X, double Y, double c,
                           double k, double r) {
    if ((x >= X + k / 2 && x <= X + k / 2 + r)
            && (y >= Y + c / 2 && y <= Y + c / 2 + r)
            && zxjl(x, y, X, Y, c, k, r)) {
        //System.out.println("�������½�");
        return true;
    } else {
        //System.out.println("���������½�");
        return false;
    }
}

// Բ�ĺ;��ζԽ����ཻ��(���ľ���)
private static boolean zxjl(double x, double y, double X, double Y, double c,
                            double k, double r) {
    double zx = X - k / 2;// ��x
    double yx = X + k / 2;// ��x
    double sy = Y - c / 2;// ��y
    double xy = Y + c / 2;// ��y
    //System.out.println("���Ͻǣ�" + zx + ":" + sy);
    //System.out.println("���½ǣ�" + zx + ":" + xy);
    //System.out.println("���Ͻǣ�" + yx + ":" + sy);
    //System.out.println("���½ǣ�" + yx + ":" + xy);

    if ((Math.pow((x - zx), 2) + Math.pow((y - sy), 2) <= Math.pow(r, 2))
            || (Math.pow((x - yx), 2) + Math.pow((y - sy), 2) <= Math.pow(
            r, 2))
            || (Math.pow((x - zx), 2) + Math.pow((y - xy), 2) <= Math.pow(
            r, 2))
            || (Math.pow((x - yx), 2) + Math.pow((y - xy), 2) <= Math.pow(
            r, 2))) {
        //System.out.println("�������ľ�");
        return true;
    } else {
        //System.out.println("���������ľ�");
        return false;
    }
}

/**
 * �ж�Բ������Ƿ��ཻ
 *
 * @param x Բ��x
 * @param y Բ��y
 * @param X ��������X
 * @param Y ��������Y
 * @param c ���θ߶�
 * @param k ���ο��
 * @param r Բ�뾶
 * @return true:�ཻ false:���ཻ
 */
public static boolean isIntersect(double x, double y, double X, double Y,
                                  double c, double k, double r) {
    if (sxzy(x, y, X, Y, c, k, r) || zsj(x, y, X, Y, c, k, r)
            || ysj(x, y, X, Y, c, k, r) || zxj(x, y, X, Y, c, k, r)
            || yxj(x, y, X, Y, c, k, r)) {
        //System.out.println("�ཻ");
        return true;
    } else {
        //System.out.println("���ཻ");
        return false;
    }
}
}
