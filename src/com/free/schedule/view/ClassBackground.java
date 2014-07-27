package com.free.schedule.view;


import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

/**�γ̸񱳾�
 * @author Administrator
 *
 */
public class ClassBackground extends Drawable{
	private int backgroundColor = 0;
	private int width;
	private int height;
	private boolean hasNote = false;
	/**
	 * @param backgroundColor ������ɫ,0Ϊû����ɫ
	 * @param width ������
	 * @param height ������
	 * @param hasNote �Ƿ��бʼ�
	 */
	public ClassBackground(int backgroundColor,int width,int height,boolean hasNote){
		this(width,height);
		this.backgroundColor = backgroundColor;
		this.hasNote = hasNote;
	}
	
	/**
	 * @param width ������
	 * @param height ������
	 */
	public ClassBackground(int width,int height){
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		RectF outerFrame = new RectF(4,4,width - 4,height - 4);
		Paint outerPaint = new Paint();
		outerPaint.setAntiAlias(true);
		outerPaint.setStrokeWidth(1);
		outerPaint.setColor(0xFFBEC3C7);
		outerPaint.setStyle(Style.STROKE);
		canvas.drawRoundRect(outerFrame, 11,11, outerPaint);
		if(backgroundColor != 0){
			RectF innerFrame = new RectF(5,5,width-5,height-5);
			Paint innerPaint = new Paint();
			innerPaint.setAntiAlias(true);
			innerPaint.setColor(backgroundColor);
			innerPaint.setStyle(Style.FILL);
			innerPaint.setAntiAlias(true);
			innerPaint.setStrokeWidth(1);
			canvas.drawRoundRect(innerFrame, 10,10, innerPaint);
			if(hasNote){//��Բ,������ʾ�бʼ�
				Paint circlePaint = new Paint();
				circlePaint.setColor(0xFFFF0000);
				circlePaint.setAntiAlias(true);
				int offsetY =(height / ScheduleView.classHeight - 1) * ScheduleView.classHeight;//Բ��Բ�ͬ�߽���ƫ��Y��
				canvas.drawCircle(width * 5 / 6,ScheduleView.classHeight * 4 / 5 + offsetY, width / 15,circlePaint);
			}
		}
	}

	@Override
	public int getOpacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAlpha(int alpha) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		// TODO Auto-generated method stub
		
	}
	
}