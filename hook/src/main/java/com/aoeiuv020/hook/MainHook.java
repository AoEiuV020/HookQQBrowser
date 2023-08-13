package com.aoeiuv020.hook;

import java.util.Objects;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

@SuppressWarnings({"RedundantThrows", "unused", "StatementWithEmptyBody", "RedundantSuppression"})
public class MainHook implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("handleLoadPackage: " + lpparam.processName + ", " + lpparam.packageName);
        if (Objects.equals(lpparam.processName, lpparam.packageName)) {
            Utils.hookDebug(lpparam);
            hookGDT(lpparam);
        }
    }

    private void hookGDT(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod("com.qq.e.comm.managers.GDTADManager", lpparam.classLoader, "initWith", android.content.Context.class, java.lang.String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(false);
            }
        });
    }

}
