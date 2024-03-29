package com.lib.core.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.lib.core.baseapp.BaseApplication;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;

/**
 */
public class AnalysisUtil {
    public AnalysisUtil() {
    }

    public static void onResume(Activity ac, boolean isPage, String eventId) {
        if (isPage) {
            MobclickAgent.onPageStart(eventId);
        }

        MobclickAgent.onResume(ac);
    }

    public static void onResume(Activity ac) {
        if (ac instanceof FragmentActivity) {
            List<Fragment> fragments = ((FragmentActivity) ac).getSupportFragmentManager().getFragments();
            if (null != fragments && fragments.size() > 0) {
                onResume(ac, false);
            } else {
                onResume(ac, true);
            }
        } else {
            onResume(ac, true);
        }
    }


    public static void onResume(Activity ac, boolean isPage) {
        String acName = ac.getClass().getSimpleName().toString();
        onResume(ac, isPage, acName);
    }

    /**
     * 自定义页面名称
     * @param ac
     * @param eventId
     */
    public static void onResume(Activity ac, String eventId) {
        if (ac instanceof FragmentActivity) {
            List<Fragment> fragments = ((FragmentActivity) ac).getSupportFragmentManager().getFragments();
            if (null != fragments && fragments.size() > 0) {
                onResume(ac, false, eventId);
            } else {
                onResume(ac, true, eventId);
            }
        } else {
            onResume(ac, true, eventId);
        }
    }


    public static void onPause(Activity ac, boolean isPage, String eventId) {
        if (isPage) {
            MobclickAgent.onPageEnd(eventId);
        }

        MobclickAgent.onPause(ac);
    }

    public static void onPause(Activity ac) {
        String acName = ac.getClass().getSimpleName().toString();
        if (ac instanceof FragmentActivity) {
            List<Fragment> fragments = ((FragmentActivity) ac).getSupportFragmentManager().getFragments();
            if (null != fragments && fragments.size() > 0) {
                onPause(ac, false, acName);
            } else {
                onPause(ac, true, acName);
            }
        } else {
            onPause(ac, true, acName);
        }
    }

    public static void onPause(Activity ac, boolean isPage) {
        String acName = ac.getClass().getSimpleName().toString();
        onPause(ac, isPage, acName);
    }

    public static void onPause(Activity ac, String eventId) {
        if (ac instanceof FragmentActivity) {
            List<Fragment> fragments = ((FragmentActivity) ac).getSupportFragmentManager().getFragments();
            if (null != fragments && fragments.size() > 0) {
                onPause(ac, false, eventId);
            } else {
                onPause(ac, true, eventId);
            }
        } else {
            onPause(ac, true, eventId);
        }
    }

    public static void onPause(Fragment fg) {
        if (fg != null) {
            String fgClass = fg.getClass().getSimpleName().toString();
            MobclickAgent.onPageEnd(fgClass);
        }
    }

    public static void onPause(Fragment fg, String eventId) {
        if (fg != null) {
            MobclickAgent.onPageEnd(eventId);
        }
    }

    public static void onResume(Fragment fg) {
        if (fg != null) {
            String fgClass = fg.getClass().getSimpleName().toString();
            MobclickAgent.onPageStart(fgClass);
        }
    }

    public static void onResume(Fragment fg, String eventId) {
        if (fg != null) {
            MobclickAgent.onPageStart(eventId);
        }
    }

    public static void onEvent(Context ctx, String eventID) {
        MobclickAgent.onEvent(ctx, eventID);
    }

    public static void onEvent(Context ctx, String eventID, HashMap<String, String> map) {
        MobclickAgent.onEvent(ctx, eventID, map);
    }

    public static void onEvent(Context ctx, String eventID, String item) {
        MobclickAgent.onEvent(ctx, eventID, item);
    }


    public static String getName(Object _activity) {
        String acName = _activity.getClass().getSimpleName();
        int strResId = BaseApplication.getAppResources().getIdentifier(acName, "string", BaseApplication.getAppContext().getPackageName());
        if (strResId <= 0) {
            return acName;
        } else {
            String name = BaseApplication.getAppResources().getString(strResId);
            if (name.length() == 0) {
                return acName;
            } else {
                return name;
            }
        }
    }
}
