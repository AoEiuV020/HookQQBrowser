package com.aoeiuv020.hook;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

@SuppressWarnings({"unused", "ConstantConditions", "deprecation", "RedundantThrows"})
public class Utils {
    @SuppressWarnings("All")
    public static final boolean DEBUG = BuildConfig.DEBUG && true;

    public static void printLayoutTree(Activity activity) {
        printLayoutTree((ViewGroup) getRootView(activity), 0);
    }

    public static void printLayoutTree(Fragment fragment) {
        printLayoutTree((ViewGroup) fragment.getView(), 0);
    }

    public static void printLayoutTree(ViewGroup viewGroup) {
        printLayoutTree(viewGroup, 0);
    }

    public static void printLayoutTree(ViewGroup viewGroup, int level) {
        printView(viewGroup, level);
        int viewCount = viewGroup.getChildCount();
        for (int i = 0; i < viewCount; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup) {
                printLayoutTree((ViewGroup) view, level + 1);
            } else {
                printView(view, level);
            }
        }
    }

    public static void printView(View view, int level) {
        var sb = new StringBuilder();
        for (int i = 0; i < level - 1; i++) {
            sb.append('│');
        }
        if (level > 0) {
            sb.append('├');
        }
        var name = view.getClass().getName();
        if (view.getClass().getPackage().getName().equals("android.view")) {
            sb.append(view.getClass().getSimpleName());
        } else {
            sb.append(view.getClass().getName());
        }
        XposedBridge.log(sb.toString());
    }

    public static Bitmap getActivitySnapshot(Activity activity) {
        return getViewSnapshot(getRootView(activity));
    }

    public static View getRootView(Activity activity) {
        return activity.getWindow().getDecorView();
    }

    public static Bitmap getViewSnapshot(View view) {
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    public static void log(XC_MethodHook.MethodHookParam param) {
        XposedBridge.log("hook: " + (param.thisObject != null ? param.thisObject.getClass() : param.method.getDeclaringClass()).getName() + "." + param.method.getName());
        if (DEBUG) {
            XposedBridge.log(new Throwable());
        }
    }

    public static void nothing(XC_LoadPackage.LoadPackageParam lpparam, String clazz, String... methods) {
        var r = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                log(param);
                param.setResult(null);
            }
        };
        for (String method : methods) {
            try {
                XposedHelpers.findAndHookMethod(
                        clazz,
                        lpparam.classLoader, method, r
                );
            } catch (Throwable t) {
                XposedBridge.log(t);
            }
        }
    }

    public static void hookDebug(XC_LoadPackage.LoadPackageParam lpparam) {
        if (!DEBUG) {
            return;
        }
        var r = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                // 无功能，必要时断点使用的，
                log(param);
            }
        };

        XposedHelpers.findAndHookMethod(
                "android.app.Activity",
                lpparam.classLoader, "onResume", r
        );
    }

}
