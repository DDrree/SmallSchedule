package com.free.schedule;

import java.lang.reflect.Field;
import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.free.schedule.databases.ClassManager;

public class All {
	//sharePreference key
	public static String sharedName = "settings";
	public static String everyDaySections = "everydaySections";//ÿ�տγ���
	public static String isAutoMute = "isAutoMute";//�Ƿ��Ͽ��Զ�����
	public static String everyDayRemind = "everydayRemind";//�Ƿ���ÿ������
	public static String isMineSet = "isMineSet";//�Ƿ�Ϊ�������Ϊ����
	public static String colorIndex = "colorIndex";//��ɫ����
	public static String dayOfWeek = "dayOfWeek";//����Ϊ���ڼ�
	public static String howManyClasses = "howManyClasses";//�����ж��ٽڿ�
	public static String todayNotifiy = "todayNotifiy";//�����Ƿ��Ѿ����ѹ�
	public static String dayOfMonth = "dateOfMonth";//����Ϊ����
	public static String userClassName = "className";//�û��༶��
	public static String receviceNews = "receviceNews";//�Ƿ��������
	public static String timeLong = "timeLong";//ÿ�ڿ�ʱ��
	//���±� sharePreference key
	public static String noteShared = "note";//
	//���ݿ���
	public static String classTableName = "classes";//
	public static String databaseName = "schedule.db";//
	public static String timeTableName = "classTime";//
	//��ɫ
	public static int backgroundColor = 0xFFFFFFFF;
	public static int titleBarColor = 0xFFBEC3C7;
	public static int stoneColor = 0xFFECF0F1;
	public static int classTextColor = 0xFFFFFFFF;
	public static int dayOfWeekColor = 0xFF526375;
	//ƽ̨ 
	public final static String APIKEY = "g6v0hGBimrs5fbh1f2yDOrUF";
	public static String classTextColors[] ={"#0080FF","#35C87A","#F78B26","#53C5E7","#48608E","#1D6478","#FEE79B","#91d101","#FE6553","#F6A0BB","#34BC98","#909E9F"};
	/**��ȡ״̬���߶�
	 * @param context
	 * @return ״̬���߶�
	 */
	public static int getStatusBarHeight(Context context){
		
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        } 
        
        
        return statusBarHeight;
    }
	
	
	/**ˢ������(�����Ϊ���ڼ� ,����...),ÿ��ִ��һ��
	 * @param context
	 */
	public static void refreshTodaysData(Context context){
		Calendar calendar = Calendar.getInstance();
		SharedPreferences sp = context.getSharedPreferences(sharedName, 0);
		int date = calendar.get(Calendar.DAY_OF_MONTH);
		if(date != sp.getInt(All.dayOfMonth, 0)){//�жϽ����Ƿ�ˢ�¹�
			SharedPreferences.Editor spe = sp.edit();
			ClassManager classManager = new ClassManager(context);
			//��ȡ�������ڼ�
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
			if(dayOfWeek == 1){
				dayOfWeek = 6;
			}else{
				dayOfWeek -= 2;
			}
			//��ȡ�����ж��ٽڿ�
			int howmayClassesToady = 0;
	    	SQLiteDatabase db = classManager.getReadableDatabase();
	    	Cursor cursor = db.query(All.classTableName, null, "dayOfWeek=?", new String[]{dayOfWeek+""}, null, null, null);
    		while(cursor.moveToNext()){
    			howmayClassesToady++;
    		}
	    	cursor.close();
	    	db.close();
	    	spe.putInt(All.dayOfWeek, dayOfWeek);
	    	spe.putInt(All.howManyClasses, howmayClassesToady);
	    	spe.putInt(All.dayOfMonth, date);
	    	spe.putBoolean(All.todayNotifiy, false).commit();
	    	spe.commit();
		}
	}
	
	/**��ȡ����״̬
     * @return null��Ϊû����
     */ 
    public static NetworkInfo getNetworkState(Context context){
    	ConnectivityManager cm =(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	if(cm == null) return null;
		return cm.getActiveNetworkInfo();
    }
    
}
