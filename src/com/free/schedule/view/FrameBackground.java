package com.free.schedule.view;

import com.free.schedule.All;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;

/**��������ڿ����ı߿�
 * @author Administrator
 *
 */
public class FrameBackground extends Drawable{
	private int width,height;
	private boolean vertical = false;
	private boolean isFocus = false;
	/**
	 * @param width ��
	 * @param height ��
	 * @param vertical ��ܵķ���trueΪ��ֱ,falseΪˮƽ
	 */
	public FrameBackground (int width,int height,boolean vertical){
		this.width = width;
		this.height = height;
		this.vertical = vertical;
	}
	
	/**
	 * @param width ��
	 * @param height ��
	 * @param vertical ��ܵķ���trueΪ��ֱ,falseΪˮƽ
	 * @param isFoucs �Ƿ�Ϊ��ǰ����
	 */
	public FrameBackground (int width,int height,boolean vertical,boolean isFoucs){
		this.width = width;
		this.height = height;
		this.vertical = vertical;
		this.isFocus = isFoucs;
	}
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		Paint paint = new Paint();
		paint.setColor(All.stoneColor);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(5);
		if(isFocus){
			Rect rect = new Rect(0, 0, width, height);
			canvas.drawRect(rect, paint);
		}else{
			paint.setStyle(Style.STROKE);
			if(vertical){
				canvas.drawLine(width, 0, width, height, paint);
				canvas.drawLine(0, height, width, height, paint);
			}else{
				canvas.drawLine(width, 0, width, height, paint);
				canvas.drawLine(0, height, width , height, paint);
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
