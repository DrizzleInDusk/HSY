package com.zm.hsy.https;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.zm.hsy.entity.Filedata;
import com.zm.hsy.entity.Music;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Futil {
    private static Toast sToast = null;
    public static void showMessage(Context context, String string) {
        try {
            Toast toast=new Toast(context);
            if (sToast != null)
                sToast.cancel();
            sToast = toast;
            sToast.makeText(context, string, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * 显示对话框
     *
     * @param context
     * @param message
     * @param positive
     * @param negative
     * @param listener
     */
    public static void showDialog(Context context, String message,
                                  String positive, String negative,
                                  DialogInterface.OnClickListener listener) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, positive, listener);
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, negative, listener);
        alertDialog.show();
    }

    /**
     * 获取缓存大小
     */
    public static String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }

    /**
     * 清除缓存
     */
    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    // 获取文件
    // Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/
    // 目录，一般放一些长时间保存的数据
    // Context.getExternalCacheDir() -->
    // SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            // return size + "Byte";
            return "0.00KB";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    /**
     * 删除文件或文件夹
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        }
    }

    /**
     * 获取指定后缀名的 文件集合
     *
     * @param path //检测SD卡是否存在
     * if (Environment.getExternalStorageState().equals(
     * Environment.MEDIA_MOUNTED)) {
     * path = Environment.getExternalStorageDirectory();
     * }else{
     * Toast.makeText(this, "没有SD卡", Toast.LENGTH_LONG).show();
     * finish();
     * }
     * @param pos 必须为 1
     * @return 文件集合
     */
    private static ArrayList<String> fileTempList = null;

    public static ArrayList<String> getAllFiles(String path, int pos) {
        if (pos == 1) {
            fileTempList = new ArrayList<String>();
        }
        File file = new File(path);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] fileArray = file.listFiles();
                for (File f : fileArray) {
                    if (f.isDirectory()) {
                        pos++;
                        getAllFiles(f.getPath(), pos);
                    } else {
                        if (f.getName().endsWith("mp3")) {
                            fileTempList.add(f.getAbsolutePath());
                        }
                    }
                }
            }
        }
        return fileTempList;
    }

    public static ArrayList<Filedata> gethsyFiles(String path) {
        ArrayList<Filedata> fileTempList = new ArrayList<>();
        File file = new File(path);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] fileArray = file.listFiles();
                for (File f : fileArray) {
                    Filedata filedata = new Filedata();
                    filedata.setFpath(f.getAbsolutePath());
                    if (f.isDirectory()) {
                        filedata.setType("filedir");
                        fileTempList.add(filedata);
                    } else {
                        if (f.getName().endsWith("mp3")) {
                            filedata.setType("mp3");
                        } else if (f.getName().endsWith("mp4")) {
                            filedata.setType("mp4");
                        } else if (f.getName().endsWith("jpg")) {
                            filedata.setType("jpg");
                        } else if (f.getName().endsWith("png")) {
                            filedata.setType("png");
                        } else {
                            filedata.setType("def");
                        }
                        fileTempList.add(filedata);
                    }
                }
            }
        }
        return fileTempList;
    }

    /**
     * 获取SD卡中的音乐文件
     *
     * @param context
     * @return
     */
    public static ArrayList<Music> getMusicFile(Context context) {
        //ArrayList<Music>存放音乐
        ArrayList<Music> MusicFiles = new ArrayList<>();
        //查询媒体数据库
        ContentResolver resolver = context.getContentResolver();
        /**
         * Uri：这个Uri代表要查询的数据库名称加上表的名称。
         这个Uri一般都直接从MediaStore里取得，例如我要取所有歌的信息，
         就必须利用MediaStore.Audio.Media. EXTERNAL _CONTENT_URI这个Uri。
         *
         */
        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        //遍历媒体数据库
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                //歌曲编号MediaStore.Audio.Media._ID
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));

                //歌曲标题
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));

                //歌曲的专辑名MediaStore.Audio.Media.ALBUM
                String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));

                //歌曲的歌手名MediaStore.Audio.Media.ARTIST
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));

                //歌曲文件的路径MediaStore.Audio.Media.DATA
                String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));

                //歌曲的总播放时长MediaStore.Audio.Media.DURATION
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

                //歌曲文件的大小MediaStore.Audio.Media.SIZE
                long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));

//                if (size > 1024 * 800) {     //是否大于800K
                if (title.equals("<unknown>") || title.equals("")) {
                    title = "未知";
                }
                if ("<unknown>".equals(artist) || "".equals(artist)) {
                    artist = "未知";
                }

                Music music = new Music(id, title, artist,
                        url, album, duration, size);
                MusicFiles.add(music);
//                }
                cursor.moveToNext();
            }
        }
        return MusicFiles;
    }

    /**
     * 清空
     *
     * @param context
     */
    public static void clearValues(Context context) {
        SharedPreferences sp = context.getSharedPreferences("oneapp_share",
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 存Array 1.录制的声音 2.播放历史3录制的视频4.搜索历史 5.电台播放历史
     */
    public static boolean saveKeyArray(Context context, ArrayList<String> sKey,
                                       String TAG) {
        SharedPreferences sp = context.getSharedPreferences("oneapp_share",
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        if (TAG.equals("1")) {
            editor.putInt("audio_size", sKey.size()); /* sKey is an array */
            for (int i = 0; i < sKey.size(); i++) {
                editor.remove("audio_" + i);
                editor.putString("audio_" + i, sKey.get(i));
            }
        } else if (TAG.equals("2")) {
            editor.putInt("histroy_size", sKey.size()); /* sKey is an array */
            for (int i = 0; i < sKey.size(); i++) {
                editor.remove("history_" + i);
                editor.putString("history_" + i, sKey.get(i));
            }
        } else if (TAG.equals("3")) {
            editor.putInt("video_size", sKey.size());
            for (int i = 0; i < sKey.size(); i++) {
                editor.remove("video_" + i);
                editor.putString("video_" + i, sKey.get(i));
            }
        } else if (TAG.equals("4")) {
            editor.putInt("search_history_size", sKey.size());
            for (int i = 0; i < sKey.size(); i++) {
                editor.remove("search_history_" + i);
                editor.putString("search_history_" + i, sKey.get(i));
            }
        } else if (TAG.equals("5")) {
            editor.putInt("station_history_size", sKey.size());
            for (int i = 0; i < sKey.size(); i++) {
                editor.remove("station_history_" + i);
                editor.putString("station_history_" + i, sKey.get(i));
            }
        }
        return editor.commit();

    }

    /**
     * 取Array 1.录制的声音 2.播放历史 3.录制的视频4.搜索历史 5.电台播放历史
     */
    public static ArrayList<String> loadKeyArray(Context context, String TAG) {
        ArrayList<String> sKey = new ArrayList<String>();
        SharedPreferences sp = context.getSharedPreferences("oneapp_share",
                Context.MODE_PRIVATE);
        if (TAG.equals("1")) {
            int size = sp.getInt("audio_size", 0);
            for (int i = 0; i < size; i++) {
                sKey.add(sp.getString("audio_" + i, null));

            }
        } else if (TAG.equals("2")) {
            int size = sp.getInt("histroy_size", 0);
            for (int i = 0; i < size; i++) {
                sKey.add(sp.getString("history_" + i, null));
            }
        } else if (TAG.equals("3")) {
            int size = sp.getInt("video_size", 0);
            for (int i = 0; i < size; i++) {
                sKey.add(sp.getString("video_" + i, null));
            }
        } else if (TAG.equals("4")) {
            int size = sp.getInt("search_history_size", 0);
            for (int i = 0; i < size; i++) {
                sKey.add(sp.getString("search_history_" + i, null));
            }
        } else if (TAG.equals("5")) {
            int size = sp.getInt("station_history_size", 0);
            for (int i = 0; i < size; i++) {
                sKey.add(sp.getString("station_history_" + i, null));
            }
        }

        return sKey;
    }

    /**
     * 从SP文件中移除指定Key的值
     *
     * @param TAG TAG1.录制的声音
     * @param TAG TAG2.播放历史
     * @param TAG TAG3.录制的视频
     * @param TAG TAG4.其他User"userid"搜索历史
     * @param TAG TAG5.电台播放历史
     */
    public static void romveValue(Context context, String key, String TAG) {
        SharedPreferences sp = context.getSharedPreferences("oneapp_share",
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        if (TAG.equals("1")) {
            editor.remove(key + "time");
            editor.remove(key + "addtime");
            editor.remove(key + "name");
            editor.remove(key + "path");
            editor.remove(key + "cover");
        } else if (TAG.equals("2")) {
            editor.remove(key + "id");
            editor.remove(key + "audioName");
            editor.remove(key + "cover");
            editor.remove(key + "path");
            editor.remove(key + "blurb");
            editor.remove(key + "audioAlbumId");
            editor.remove(key + "addTime");
        } else if (TAG.equals("3")) {
            editor.remove(key + "time");
            editor.remove(key + "addtime");
            editor.remove(key + "name");
            editor.remove(key + "path");
            editor.remove(key + "cover");
            editor.remove(key + "blurb");
        } else if (TAG.equals("4")) {
            editor.remove(key);
        } else if (TAG.equals("5")) {
            editor.remove(key + "name");
            editor.remove(key + "mmsh");
            editor.remove(key + "addTime");
        }
        editor.commit();
    }

    /**
     * 向SP文件存储数据
     *
     * @param value 键名1.录制的声音 + "time"时长+ "addtime"添加时间+ "name"音乐名+
     *              "path"音乐路径+"cover"封面
     * @param value 键名2.播放历史+ "id"+ "audioName" + "cover"+ "path"+ "blurb"+
     *              "audioAlbumId"+ "addTime"
     * @param value 键名3.录制的视频 + "time"时长+ "addtime"添加时间+ "name"视频名+
     *              "path"视频路径+"cover"视频封面+"blurb"视频简介
     * @param value 键名4.User"userid" "shield"是否为黑名单1不是2是
     * @param value 键名5.搜索历史
     * @param value 键名6.电台播放历史+ "name"+ "mmsh"+"addTime"
     */
    public static void saveValue(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("oneapp_share",
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.remove(key);
        editor.putString(key, value);

        editor.commit();
    }

    /**
     * 从SP文件中读取指定Key的值
     *
     * @param key 键名1.录制的声音 + "time"时长+ "addtime"添加时间+ "name"音乐名+
     *            "path"音乐路径+"cover"封面
     * @param key 键名2.播放历史+ "id"+ "audioName" + "cover"+ "path"+ "blurb"+
     *            "audioAlbumId"+ "addTime"
     * @param key 键名3.录制的视频 + "time"时长+ "addtime"添加时间+ "name"视频名+
     *            "path"视频路径+"cover"视频封面+"blurb"视频简介
     * @param key 键名4.User"userid" "shield"是否为黑名单1不是2是
     * @param key 键名5.搜索历史
     * @param key 键名6.电台播放历史+ "name"+ "mmsh"+"addTime"
     */
    public static String getValue(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("oneapp_share",
                Context.MODE_PRIVATE);
        String st = null;
        st = sp.getString(key, "");
        return st;
    }

    public static void xutils(String url, HashMap<String, String> map,
                              final Handler handler, final int ms) {
        RequestParams params = new RequestParams(url);
        Set<Map.Entry<String, String>> set = map.entrySet();
        for (Map.Entry<String, String> e : set) {
            params.addBodyParameter(e.getKey(), e.getValue());
        }
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Log.v("String转换json成功", "当前获取json字符>>>" + jsonObject);
                    Message msg = new Message();
                    msg.obj = jsonObject;
                    msg.what = ms;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.v("String转换json失败", "当前获取String字符>>>" + result);
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof HttpException) { // 网络错误
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                    Log.v("网络错误", "当前获取字符>>>" + httpEx);
                } else { // 其他错误
                    // ...
                    Log.v("其他错误", "当前获取字符>>>" + ex.getMessage());
                }
                showMessage(x.app(), "无法链接到网络或服务器");

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public static void xutils(String url,
                              final Handler handler, final int ms) {
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Log.v("String转换json成功", "当前获取json字符>>>" + jsonObject);
                    Message msg = new Message();
                    msg.obj = jsonObject;
                    msg.what = ms;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.v("String转换json失败", "当前获取String字符>>>" + result);
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof HttpException) { // 网络错误
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                    Log.v("网络错误", "当前获取字符>>>" + httpEx);
                } else { // 其他错误
                    // ...
                    Log.v("其他错误", "当前获取字符>>>" + ex.getMessage());
                }
                showMessage(x.app(), "无法链接到网络或服务器");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public static void xutils(String url, HashMap<String, String> map,
                              String filekey, File file, final Handler handler, final int ms) {
        RequestParams params = new RequestParams(url);
        Set<Map.Entry<String, String>> set = map.entrySet();
        for (Map.Entry<String, String> e : set) {
            params.addBodyParameter(e.getKey(), e.getValue());
        }
        params.addBodyParameter(filekey, file);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Log.v("String转换json成功", "当前获取json字符>>>" + jsonObject);
                    Message msg = new Message();
                    msg.obj = jsonObject;
                    msg.what = ms;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.v("String转换json失败", "当前获取String字符>>>" + result);
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof HttpException) { // 网络错误
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                    Log.v("网络错误", "当前获取字符>>>" + httpEx);
                } else { // 其他错误
                    // ...
                    Log.v("其他错误", "当前获取字符>>>" + ex.getMessage());
                }
                showMessage(x.app(), "无法链接到网络或服务器");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public static void xutils(String url, HashMap<String, String> map,
                              HashMap<String, File> mapfile, final Handler handler, final int ms) {
        RequestParams params = new RequestParams(url);
        Set<Map.Entry<String, String>> set = map.entrySet();
        for (Map.Entry<String, String> e : set) {
            params.addBodyParameter(e.getKey(), e.getValue());
        }
        for (String key : mapfile.keySet()) {
            params.addBodyParameter(key, mapfile.get(key));
        }
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Log.v("String转换json成功", "当前获取json字符>>>" + jsonObject);
                    Message msg = new Message();
                    msg.obj = jsonObject;
                    msg.what = ms;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.v("String转换json失败", "当前获取String字符>>>" + result);
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof HttpException) { // 网络错误
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                    Log.v("网络错误", "当前获取字符>>>" + httpEx);
                } else { // 其他错误
                    // ...
                    Log.v("其他错误", "当前获取字符>>>" + ex.getMessage());
                }
                showMessage(x.app(), "无法链接到网络或服务器");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

}
