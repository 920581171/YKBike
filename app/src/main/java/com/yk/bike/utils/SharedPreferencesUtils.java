package com.yk.bike.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.yk.bike.base.BaseApplication;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class SharedPreferencesUtils {
    /**
     * 保存在手机里面的SharedPreference文件名
     */
    private static final String FILE_NAME = "share_data";

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     */
    public static void put(String key, Object object) {

        SharedPreferences.Editor editor = getSharedPreferences().edit();

        if (object == null) {
            editor.putString(key, "");
        } else if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     */
    public static Object get(String key, Object defaultObject) {
        if (defaultObject instanceof String) {
            return getSharedPreferences().getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return getSharedPreferences().getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return getSharedPreferences().getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return getSharedPreferences().getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return getSharedPreferences().getLong(key, (Long) defaultObject);
        }

        return null;
    }

    public static String getString(String key) {
        return getSharedPreferences().getString(key, "");
    }

    public static boolean getBoolean(String key){
        return getSharedPreferences().getBoolean(key, false);
    }

    /**
     * 移除某个key值已经对应的值
     */
    public static void remove(String key) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     */
    public static void clear() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     */
    public static boolean contains(String key) {
        return getSharedPreferences().contains(key);
    }

    /**
     * 返回所有的键值对
     */
    public static Map<String, ?> getAll() {
        return getSharedPreferences().getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException ignored) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ignored) {
            }
            editor.commit();
            try {
                Runtime.getRuntime().exec("sync");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static SharedPreferences getSharedPreferences() {
        return BaseApplication.getApplication().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
    }
}
