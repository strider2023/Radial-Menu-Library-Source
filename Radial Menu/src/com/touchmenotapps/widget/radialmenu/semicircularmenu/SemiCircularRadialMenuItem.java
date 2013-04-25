package com.touchmenotapps.widget.radialmenu.semicircularmenu;

import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public class SemiCircularRadialMenuItem {

	private String mMenuID;
	private Drawable mIcon;
	private String mText;
	private int mBackgroundColor;
	private int mMenuNormalColor;
	private int mMenuSelectedColor;
	private int mTextColor;
	private Path mPath;
	private RectF mBounds;
	private OnSemiCircularRadialMenuPressed mCallback;
	private int mIconDimen;
	
	public interface OnSemiCircularRadialMenuPressed {
		public void onMenuItemPressed();
	}

	/**
	 * @param mIcon
	 * @param mText
	 */
	public SemiCircularRadialMenuItem(String id, Drawable mIcon, String mText) {
		super();
		this.mMenuID = id;
		this.mIcon = mIcon;
		this.mText = mText;
		this.mMenuNormalColor = Color.WHITE;
		this.mMenuSelectedColor = Color.LTGRAY;
		this.mBackgroundColor = mMenuNormalColor;
		this.mTextColor = Color.BLACK;
		this.mIconDimen = 64;
		mPath = new Path();
		mBounds = new RectF();
	}
		
	/**
	 * @return the mTextColor
	 */
	public int getTextColor() {
		return mTextColor;
	}

	/**
	 * @param mTextColor the mTextColor to set
	 */
	public void setTextColor(int mTextColor) {
		this.mTextColor = mTextColor;
	}

	/**
	 * @return the mMenuID
	 */
	public String getMenuID() {
		return mMenuID;
	}
	
	/**
	 * @return the mIconDimen
	 */
	public int getIconDimen() {
		return mIconDimen;
	}

	/**
	 * @param mIconDimen the mIconDimen to set
	 */
	public void setIconDimen(int mIconDimen) {
		this.mIconDimen = mIconDimen;
	}

	/**
	 * @return the mBounds
	 */
	public Path getMenuPath() {
		return mPath;
	}

	/**
	 * @return the mBounds
	 */
	public RectF getBounds() {
		return mBounds;
	}

	/**
	 * @param mPath
	 *            the mBounds to set
	 */
	public void setMenuPath(RectF menuButtonRect, RectF menuRect, float StartArc, float ArcWidth, float radius, Point anchorPoint) {
		int left, right, top, bottom;
		left = right = top = bottom= 0;
		//Draw the widget path
		mPath.arcTo(menuRect, StartArc, ArcWidth);
		mPath.arcTo(menuButtonRect, StartArc + ArcWidth, -ArcWidth);
		mPath.close();
		mPath.computeBounds(mBounds, true);
		//Get the drawable bounds
		Point drawableCenter = pointOnCircle((radius - (radius/5)), 
				StartArc + (ArcWidth/2),
				anchorPoint);
		left = (int) drawableCenter.x - (mIconDimen/2);
		top = (int) drawableCenter.y - (mIconDimen/2);
		right = left + (mIconDimen);
		bottom = top + (mIconDimen);
		mIcon.setBounds(left, top, right, bottom);
	}
	
	private Point pointOnCircle(float radius, float angleInDegrees, Point origin) {    
        int x = (int)(radius * Math.cos(angleInDegrees * Math.PI / 180F)) + origin.x;
        int y = (int)(radius * Math.sin(angleInDegrees * Math.PI / 180F)) + origin.y;
        return new Point(x, y);
    }

	/**
	 * @return the mIcon
	 */
	public Drawable getIcon() {
		return mIcon;
	}

	/**
	 * @return the mText
	 */
	public String getText() {
		return mText;
	}

	/**
	 * @param mCallback the mCallback to set
	 */
	public void setOnSemiCircularRadialMenuPressed(OnSemiCircularRadialMenuPressed mCallback) {
		this.mCallback = mCallback;
	}

	/**
	 * @return the mCallback
	 */
	public OnSemiCircularRadialMenuPressed getCallback() {
		return mCallback;
	}
	
	/**
	 * @return the mColor
	 */
	public int getBackgroundColor() {
		return mBackgroundColor;
	}
	
	public void setBackgroundColor(int color) {
		this.mBackgroundColor = color;
	}

	/**
	 * @return the mMenuNormalColor
	 */
	public int getMenuNormalColor() {
		return mMenuNormalColor;
	}

	/**
	 * @param mMenuNormalColor the mMenuNormalColor to set
	 */
	public void setMenuNormalColor(int mMenuNormalColor) {
		this.mMenuNormalColor = mMenuNormalColor;
	}

	/**
	 * @return the mMenuSelectedColor
	 */
	public int getMenuSelectedColor() {
		return mMenuSelectedColor;
	}

	/**
	 * @param mMenuSelectedColor the mMenuSelectedColor to set
	 */
	public void setMenuSelectedColor(int mMenuSelectedColor) {
		this.mMenuSelectedColor = mMenuSelectedColor;
	}
}
