package com.free.schedule.view;


/*�����γ̱����ͼ
 * 
 * 
 * 
 * */


import com.free.schedule.All;
import com.free.schedule.R;
import com.free.schedule.databases.ClassManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ScheduleView extends RelativeLayout{
	private static TextView sectionText[];//��ʾ�ڿ�
	public TextView dayOfWeekText[];//��ʾ����
	public static ClassTextView[][] classText;//��ʾ�γ�
	public static RelativeLayout titleBar;//������
	private static RelativeLayout classRelative;//��ſγ̵�relativelayout
	private static LinearLayout rowLinear;//��Žڿε�LinearLayout
	private LinearLayout listLinear;//������ڵ�LinearLayout
	private static int screenHeight;//��Ļ��
	private int screenWidth;//��Ļ��
	public static int classHeight = 1;//�ڿεĸ�,Ҳ�ǿγ̵ĸ�
	private static int stoneWidth = 28;//�ڿεĿ�,���ڵĿ�
	private ImageButton setting;//���ð�ť
	public static int classWidth ;//���ڵĸ�
	public static int titleBarHeight = 70;//�������߶�
	private static int minTop,maxTop;//�γ̱��϶�ʱ������С��topֵ
	private int minLeft,maxLeft;//�γ̱��϶�ʱ������С��Leftֵ
	public static int list = 7;//
	public static int row = 8;//
	public int x1,x2,y1,y2,moveX = 0,moveY = 0, moveX2,moveY2;
	private static int backgroundColor;//������ɫ
	private String [] dayOfWeek;//����һ�������յ��ַ�����
	public static Context cx;
	public static ClassEditBar classEditBar;//�γ̱༭��
	private static RelativeLayout.LayoutParams lpx;
	private TextView stone;
	public static boolean isEdit = false;//�ж��Ƿ��ڱ༭״̬
	public static ClassDetail classDetail;//��ʾ�γ�����
	private ScheduleSettings scheduleSetting;
	private static SharedPreferences sp;
	public ScheduleView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		cx = context;
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		titleBar.layout(0, 0, screenWidth,titleBarHeight);
		stone.layout(0, titleBarHeight, stoneWidth, titleBarHeight + stoneWidth);
		l = stoneWidth + moveX2;//�����γ̱�����
		l = l > maxLeft ? maxLeft : l;
		l = l < minLeft ? minLeft : l;
		
		t = maxTop + moveY2;//�����γ̱�ĵ׶�
		t = t > maxTop? maxTop : t;
		t = t < minTop? minTop : t;
		classRelative.layout(l,t, l + list * classWidth,t + row * classHeight);
		rowLinear.layout(0 , t, stoneWidth,t + row * classHeight);
		listLinear.layout(l, titleBarHeight,l + classWidth * list, titleBarHeight + stoneWidth);
	}
	
	/**���ô�������,���ڿγ̱��϶�
	 * @param event
	 * @return0
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			x1  = (int)event.getX();
			y1  = (int)event.getY();
			moveX = moveX2;
			moveY = moveY2;
		}
		if(event.getAction() == MotionEvent.ACTION_MOVE){
			x2 = (int)event.getX();
			y2 = (int)event.getY();
			if( classRelative.getLeft() < x2 && classRelative.getRight() > y2 && classRelative.getTop() < y2 && classRelative.getBottom() > y2){
				moveX2 = x2 - x1 + moveX;
				moveY2 = y2 - y1 + moveY;
				requestLayout();
			}
		}
		return super.dispatchTouchEvent(event);
	}
	
	/**
	 * ��ʼ���γ̱���ͼ
	 */
	public void initView(){
		WindowManager wm = (WindowManager)cx.getSystemService(Context.WINDOW_SERVICE);
		sp = cx.getSharedPreferences(All.sharedName, 0);
		screenHeight = wm.getDefaultDisplay().getHeight();
		screenWidth = wm.getDefaultDisplay().getWidth();
		titleBarHeight = screenHeight * 70 / 854;
		stoneWidth = screenWidth * 28 / 480;
		classWidth = (screenWidth - stoneWidth) / 5;
		backgroundColor = All.backgroundColor;
		dayOfWeek = cx.getResources().getStringArray(R.array.dayOfWeek);
		
		setBackgroundColor(backgroundColor);
		
		stone = new TextView(cx);
		titleBar = new RelativeLayout(cx);
		classRelative = new RelativeLayout(cx);
		rowLinear = new LinearLayout(cx);
		rowLinear.setOrientation(1);//���ô�ֱLinear
		listLinear = new LinearLayout(cx);
		
		dayOfWeekText = new TextView[list];
		classEditBar = new ClassEditBar(cx);
		setting = new ImageButton(cx);
		titleBar.setBackgroundColor(All.titleBarColor);
		rowLinear.setBackgroundColor(backgroundColor);
		listLinear.setBackgroundColor(backgroundColor);
		stone.setBackgroundColor(All.stoneColor);
		classRelative.setBackgroundColor(backgroundColor);
		
		for(int i = 0;i < list;i++){
			dayOfWeekText[i] = new TextView(cx);
			dayOfWeekText[i].setGravity(Gravity.CENTER);
			dayOfWeekText[i].setId(10+i);
			dayOfWeekText[i].setText(dayOfWeek[i]);
			dayOfWeekText[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
			dayOfWeekText[i].setTextColor(All.dayOfWeekColor);
			if(i == sp.getInt(All.dayOfWeek, 0)){
				dayOfWeekText[i].setBackgroundDrawable(new FrameBackground(classWidth, stoneWidth, true,true));
			}else{
				dayOfWeekText[i].setBackgroundDrawable(new FrameBackground(classWidth, stoneWidth, true));
			}
		}
		
		scheduleSetting = new ScheduleSettings(cx);
		setting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				scheduleSetting.showAsDropDown(titleBar);
			}
		});
		
		maxLeft = stoneWidth;
		minLeft = (list - 5) >= 0 ? maxLeft - (list - 5) * classWidth : maxLeft;
	};
	
	/**
	 * ���ؿγ̱���ͼ
	 */
	public void addAllView(){
		
		lpx = new LayoutParams(titleBarHeight,titleBarHeight);//������ð�ť
		lpx.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		titleBar.addView(setting,lpx);
		setting.setScaleType(ScaleType.CENTER);
		setting.setBackgroundResource(R.drawable.settings);
		setting.setId(520);
		
		TitleBarContent newsView = new TitleBarContent(cx);//���������ͼ
		lpx = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.FILL_PARENT);
		lpx.addRule(RelativeLayout.LEFT_OF, setting.getId());
		titleBar.addView(newsView,lpx);
		
		lpx = new LayoutParams(4*titleBarHeight,titleBarHeight);//��ӿγ̱༭��
		lpx.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		titleBar.addView(classEditBar,lpx);
		addView(classRelative,new LayoutParams(LayoutParams.WRAP_CONTENT , LayoutParams.WRAP_CONTENT));//��ӿγ̱�
		addView(listLinear,new LayoutParams(list * classWidth , stoneWidth));//���������
		addView(rowLinear, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));//��ӽڿ���
		for(int i = 0;i < list;i++){
			listLinear.addView(dayOfWeekText[i],new LayoutParams(classWidth, stoneWidth));//����������������ı�
		}
	    refreshSchedule();
		addView(titleBar,new LayoutParams(-1,titleBarHeight));//��ӱ�����
		//���ö���
		Animation translate_down = AnimationUtils.loadAnimation(cx, R.anim.translate_down);
		Animation translate_up = AnimationUtils.loadAnimation(cx, R.anim.translate_up);
		Animation translate_left = AnimationUtils.loadAnimation(cx, R.anim.translate_left);
		titleBar.setAnimation(translate_down);
		listLinear.startAnimation(translate_left);
		rowLinear.startAnimation(translate_up);
		
		classDetail = new ClassDetail(titleBar,-1,-2,cx,classText[0][0]);
		addView(stone, new LayoutParams(stoneWidth,stoneWidth));//
		
		scheduleSetting.initView();
	}
	
	
	/**
	 * ˢ�¿γ̱�
	 */
	public static void refreshSchedule(){
		//�����
		classRelative.removeAllViews();
		rowLinear.removeAllViews();
		
		row = sp.getInt(All.everyDaySections, 8);
		sectionText = new TextView[row];
		classHeight = (screenHeight - All.getStatusBarHeight(cx) -titleBarHeight - stoneWidth) /  (row > 10 ? 10 : row);
		maxTop = titleBarHeight + stoneWidth;
		minTop = (row - 10) >= 0 ? maxTop - (row - 10) * classHeight :  maxTop;
		classText= new ClassTextView[list][row];
		classRelative.setBackgroundDrawable(new ClassRelativeBackground());
		for(int i = 0;i < row;i++){//�ڿ�����ӽڿ�
			sectionText[i] = new TextView(cx);
			sectionText[i].setGravity(Gravity.CENTER);
			sectionText[i].setText(i +1 +"");
			sectionText[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
			sectionText[i].setTextColor(0xFF526375);
			sectionText[i].setBackgroundDrawable(new FrameBackground(stoneWidth, classHeight, false));
			rowLinear.addView(sectionText[i],new LayoutParams(stoneWidth, classHeight));
		}
		
		for(int i = 0;i < list;i++){//�������пγ̸񣬲����õ����������
			for(int i2 = 0;i2 < row ;i2++){
				classText[i][i2] = new ClassTextView(cx);
				classText[i][i2].setId(100 * (i + 1) + i2);
				lpx = new LayoutParams(classWidth, classHeight);
				lpx.setMargins(i * classWidth, i2 * classHeight, 0, 0);
				classRelative.addView(classText[i][i2],lpx);
				classText[i][i2].dayOfWeek = i;
				classText[i][i2].setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
				classText[i][i2].endSection = classText[i][i2].startSection = i2;
				classText[i][i2].setGravity(Gravity.CENTER_HORIZONTAL);
				classText[i][i2].setTextColor(All.classTextColor);
				classText[i][i2].setPadding(5, 10, 5, 0);
				classText[i][i2].setBackground(0);
				classText[i][i2].setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						ClassTextView nowClickClass = (ClassTextView)v;
						boolean action0 = false;//�Ƿ�ֻѡ��û�ε�classText
						boolean action1 = false;//�Ƿ�ֻѡ���пε�classText
						boolean action2 = false;//�Ƿ�ֻѡ��һ���пε�classText,��ȡ��ѡ��
						if(isEdit){
							if(nowClickClass.isSelected){
								nowClickClass.setSelected(false);
								if(nowClickClass.isClass){
									nowClickClass.clearAnimation();
									action2 = true;
								}else{
									nowClickClass.setBackground(0);
								}
							}else{
								nowClickClass.setSelected(true);
								if(nowClickClass.isClass){
									nowClickClass.animationSelect();
								}else{
									nowClickClass.setBackground(0);
								}
							}
						}else{
							if(!nowClickClass.isClass){
								nowClickClass.isSelected = true;
								nowClickClass.setBackground(0);
								isEdit = true;
							}
						}

						for(int i = 0;i < list;i++){//�ж�ѡ��classText�Ƿ��п�
							for(int i2 = 0;i2 < row ;i2++){
								if(classText[i][i2].isSelected){
									if(classText[i][i2].isClass){
										action1 = true;
										i2 += classText[i][i2].howManyClasses - 1;
									}else{
										action0 = true;
									}
								}
							}
						}
						if(action0 && action1){//�ж϶����������Ҫ������ʾ�༭��
							classEditBar.setClassEditorAction(1);
						}else{
							if(action0){
								isEdit = true;
								classEditBar.setClassEditorAction(0);
							}else if(action1){
								if(!classDetail.isShowing())classEditBar.setClassEditorAction(1);
							}else{
								if(classEditBar.isShown)classEditBar.dismiss();
								isEdit = false;
								if(!action2){
									if(nowClickClass.isClass){
										nowClickClass.setSelected(true);
										classDetail = new ClassDetail(rowLinear,-1,-2,cx,nowClickClass);
										classDetail.setAction(1);
									}
								}
							}
						}
					}
				});
				
			}
		}
		
		for(int i = 0; i < row;i++){//����textView������Ļ���Զ���С������,ֻ���������
			lpx = new LayoutParams(classWidth, classHeight);
			lpx.addRule(RelativeLayout.ALIGN_RIGHT,classText[6][0].getId());
			lpx.addRule(RelativeLayout.ALIGN_LEFT,classText[4][0].getId());
			lpx.setMargins(classWidth, i * classHeight, classWidth, 0);
			classText[5][i].setLayoutParams(lpx);
		}
		isEdit = false;
		ClassManager classManager = new ClassManager(cx);
		SQLiteDatabase db = classManager.getReadableDatabase();
		Cursor c = db.query(All.classTableName, null, null, null, null, null, null);
		if(c.moveToFirst()){
			for(int i = 0;i < c.getCount();i++){//��ȡ���ݿ����ݣ�����ʾ���γ̸���
				String name = c.getString(c.getColumnIndex("name"));
				String place = c.getString(c.getColumnIndex("place"));
				int dayOfWeek = c.getInt(c.getColumnIndex("dayOfWeek"));
				int startSection = c.getInt(c.getColumnIndex("startSection"));
				int endSection = c.getInt(c.getColumnIndex("endSection"));
				String color = c.getString(c.getColumnIndex("color"));
				boolean hasNote = cx.getSharedPreferences(All.noteShared, 0).getBoolean(dayOfWeek +"" + startSection, false);
				classText[dayOfWeek][startSection].place = place;
				place = (place != null) ? "\n" + place : "";
				classText[dayOfWeek][startSection].setText(name + place);
				classText[dayOfWeek][startSection].name = name;
				classText[dayOfWeek][startSection].howManyClasses = endSection - startSection + 1;
				classText[dayOfWeek][startSection].endSection = endSection;
				classText[dayOfWeek][startSection].isClass = true;
				classText[dayOfWeek][startSection].hasNote = hasNote;
				classText[dayOfWeek][startSection].teacher = c.getString(c.getColumnIndex("teacher"));
				classText[dayOfWeek][startSection].setBackground(Color.parseColor(color));
				classText[dayOfWeek][startSection].getLayoutParams().height = classText[dayOfWeek][startSection].howManyClasses * classHeight;
				for(int i2 = startSection + 1;i2 <= endSection;i2++){//�����ظ���classTextView
					classText[dayOfWeek][i2].setClickable(false);
					classText[dayOfWeek][i2].isClass = true;
					classText[dayOfWeek][i2].setBackgroundDrawable(null);
				}
				classText[dayOfWeek][startSection].setOnLongClickListener(new OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View v) {
						// TODO Auto-generated method stub
						ClassTextView nowLongClickClass = (ClassTextView)v;
						if(!isEdit){
							classEditBar.setClassEditorAction(1);
							nowLongClickClass.animationSelect();
							isEdit = true;
						}else{
							nowLongClickClass.animationSelect();
						}
						
						return true;
					}
				});
				c.moveToNext();
			}
		}
		c.close();
		db.close();
	}
	
	public void showSettings(){
		scheduleSetting.showAsDropDown(titleBar);
	}
	
	
}
