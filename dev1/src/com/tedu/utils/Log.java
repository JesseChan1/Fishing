package com.fish.tools;


import com.fish.model.GamingInfo;

public class Log {
public static void e(String name, String info) {
    System.err.println("error:" + name + ":" + info);
}

public static void w(String name, String info) {
    System.out.println("warning:" + name + ":" + info);
}

/**
 * ���쳣��Ϣ��¼��־
 *
 * @param e
 */
public static void doLogForException(Exception e) {
    e.printStackTrace();
    //�����Ϸ�����ˣ���ô�������쳣
    if (!GamingInfo.getGamingInfo().isGaming()) {
        return;
    }
    for (StackTraceElement ste : e.getStackTrace()) {
        Log.e(ste.getClassName() + ":", e.toString());
        Log.e("line:", ste.getLineNumber() + "");
        Log.e("method:", ste.getMethodName());
        Log.e("file:", ste.getFileName());
    }
}
}
