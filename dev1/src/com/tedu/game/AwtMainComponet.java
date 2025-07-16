package com.fish.src;

import com.fish.manager.CannonManager;
import com.fish.manager.GameInitManager;
import com.fish.manager.LayoutManager;
import com.fish.model.GamingInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class AwtMainComponet {
public static void main(String[] args) throws Exception {
    /**
     * ʹ���ھ��������ַ�����
     * 1������ͨ��toolkit��ȡ��Ļ��С�Ӷ�ʹ�ô��ھ���
     * 2������ͨ��setRelativeTo(null)�����λ��Ϊ��
     */
    Toolkit tool = Toolkit.getDefaultToolkit();
    Dimension d = tool.getScreenSize();

    JFrame frame = new JFrame();
    //��ʼ��GamingInfo
    GamingInfo.getGamingInfo().setGaming(true);
    GamingInfo.getGamingInfo().setScreenWidth(1200);
    GamingInfo.getGamingInfo().setScreenHeight(800);

    frame.setSize(GamingInfo.getGamingInfo().getScreenWidth(), GamingInfo.getGamingInfo().getScreenHeight());
    //frame.setUndecorated(true); // ȥ�����ڵ�װ��
    frame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);//����ָ���Ĵ���װ�η��
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //ʹ���ھ��еļ򵥷�����ֱ������relativeToΪnull
    frame.setLocationRelativeTo(null);
    MainSurface pane = new MainSurface();
    GamingInfo.getGamingInfo().setSurface(pane);
    frame.setContentPane(pane);
    //�Ƿ�ʹ����ʼ�������Ϸ�
    //frame.setAlwaysOnTop(true);
    frame.setVisible(true);
    frame.addMouseListener(new MouseListener() {
        @Override
        public void mouseReleased(MouseEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (GameInitManager.getGameInitManager().isIniting()) {
                return;
            }
            if (!LayoutManager.getLayoutManager().onClick(e.getX(), e.getY())) {
                CannonManager.getCannonManager().shot(e.getX(), e.getY());
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void mouseClicked(MouseEvent e) {
            // TODO Auto-generated method stub

        }
    });

    frame.addKeyListener(new KeyListener() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (GameInitManager.getGameInitManager().isIniting()) {
                return;
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                CannonManager.getCannonManager().upCannon();
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                CannonManager.getCannonManager().downCannon();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {}

        @Override
        public void keyTyped(KeyEvent e) {}
    });

    frame.setFocusable(true);
    frame.requestFocus();
//		frame.pack();
    pane.action();
    /**
     * ����һ���߳����첽��ʼ����Ϸ����
     */
    new Thread(new Runnable() {

        public void run() {
            //ʹ����Ϸ��ʼ����������ʼ����Ϸ
            GameInitManager.getGameInitManager().init();
        }

    }).start();
}

}
