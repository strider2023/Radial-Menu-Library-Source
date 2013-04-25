/** Copyright (C) 2012 
 * Arindam Nath (strider2023@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.touchmenotapps.widget.radialmenu.semicircularmenu;

import java.util.HashMap;

import com.touchmenotapps.widget.radialmenu.RadialMenuColors;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * This is the core class that handles the widget display and user interaction.
 * TODO At times the arc area bound check fails. Gotta check that.
 * @author Arindam Nath (strider2023@gmail.com)
 */
public class SemiCircularRadialMenu extends View {

	//Static Access Variables
	public static final int VERTICAL_RIGHT = 0; 
	public static final int VERTICAL_LEFT = 1; 
	public static final int HORIZONTAL_TOP = 2;
	public static final int HORIZONTAL_BOTTOM = 3;
	//Private non-shared variables
	private boolean isMenuVisible = false;	
	private boolean isMenuTogglePressed = false;	
	private boolean isMenuItemPressed = false;	
	private String mPressedMenuItemID = null;	
	private int mDiameter = 0;	
	private float mRadius = 0.0f;	
	private int mStartAngle = 0;		
	private RectF mMenuRect;	
	private RectF mMenuCenterButtonRect;	
	private Paint mRadialMenuPaint = new Paint(Paint.ANTI_ALIAS_FLAG);	
	private Point mViewAnchorPoints;	
	private HashMap<String, SemiCircularRadialMenuItem> mMenuItems = new HashMap<String, SemiCircularRadialMenuItem>();
	//Variables that can be user defined	
	private float mShadowRadius = 5 * getResources().getDisplayMetrics().density;	
	private boolean isShowMenuText = false;	
	private int mOrientation = HORIZONTAL_BOTTOM;	
	private int centerRadialColor = Color.WHITE;	
	private int mShadowColor = Color.GRAY;	
	private String openMenuText = "Open";	
	private String closeMenuText = "Close";	
	private String centerMenuText = openMenuText;	//Not to be set using setter method
	private int mToggleMenuTextColor = Color.DKGRAY;
	private float textSize = 12 * getResources().getDisplayMetrics().density;
	private int mOpenButtonScaleFactor = 3;
		
	public SemiCircularRadialMenu(Context context) {
		super(context);
		init();
	}
	
	public SemiCircularRadialMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public SemiCircularRadialMenu(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		mRadialMenuPaint.setTextSize(textSize);
		mRadialMenuPaint.setColor(Color.WHITE);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mRadialMenuPaint.setShadowLayer(mShadowRadius, 0.0f, 0.0f, mShadowColor);  
		//Draw the menu if the menu is to be displayed.
		if(isMenuVisible) {
			canvas.drawArc(mMenuRect, mStartAngle, 180, true, mRadialMenuPaint);
			//See if there is any item in the collection
			if(mMenuItems.size() > 0) {
				float mStart = mStartAngle;
				//Get the sweep angles based on the number of menu items
				float mSweep = 180/mMenuItems.size();
				for(SemiCircularRadialMenuItem item : mMenuItems.values()) {
					mRadialMenuPaint.setColor(item.getBackgroundColor());
					item.setMenuPath(mMenuCenterButtonRect, mMenuRect, mStart, mSweep, mRadius, mViewAnchorPoints);
					canvas.drawPath(item.getMenuPath(), mRadialMenuPaint);
					if(isShowMenuText) {
						mRadialMenuPaint.setShadowLayer(mShadowRadius, 0.0f, 0.0f, Color.TRANSPARENT);  
						mRadialMenuPaint.setColor(item.getTextColor());
						canvas.drawTextOnPath(item.getText(), item.getMenuPath(), 5, textSize, mRadialMenuPaint);
						mRadialMenuPaint.setShadowLayer(mShadowRadius, 0.0f, 0.0f, mShadowColor);
					}
					item.getIcon().draw(canvas);
					mStart += mSweep;
				}
				mRadialMenuPaint.setStyle(Style.FILL);
			}
		}
		//Draw the center menu toggle piece
		mRadialMenuPaint.setColor(centerRadialColor);
		canvas.drawArc(mMenuCenterButtonRect, mStartAngle, 180, true, mRadialMenuPaint);
		mRadialMenuPaint.setShadowLayer(mShadowRadius, 0.0f, 0.0f, Color.TRANSPARENT);  
		//Draw the center text
		drawCenterText(canvas, mRadialMenuPaint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if(mMenuCenterButtonRect.contains(x, y)) {
				centerRadialColor = RadialMenuColors.HOLO_LIGHT_BLUE;
				isMenuTogglePressed = true;
				invalidate();
			} else if(isMenuVisible) {
				if(mMenuItems.size() > 0) {
					for(SemiCircularRadialMenuItem item : mMenuItems.values()) {
						if(mMenuRect.contains((int) x, (int) y))
							if(item.getBounds().contains((int) x, (int) y)) {
								isMenuItemPressed = true;
								mPressedMenuItemID = item.getMenuID();
								break;
							}
					}
					mMenuItems.get(mPressedMenuItemID)
						.setBackgroundColor(mMenuItems.get(mPressedMenuItemID).getMenuSelectedColor());
					invalidate();
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if(isMenuTogglePressed) {
				centerRadialColor = Color.WHITE;
				if(isMenuVisible) {
					isMenuVisible = false;
					centerMenuText = openMenuText;
				} else {
					isMenuVisible = true;
					centerMenuText = closeMenuText;
				}
				isMenuTogglePressed = false;
				invalidate();
			}
			
			if(isMenuItemPressed) {
				if(mMenuItems.get(mPressedMenuItemID).getCallback() != null) {
					mMenuItems.get(mPressedMenuItemID).getCallback().onMenuItemPressed();
				}
				mMenuItems.get(mPressedMenuItemID)
					.setBackgroundColor(mMenuItems.get(mPressedMenuItemID).getMenuNormalColor());
				isMenuItemPressed = false;
				invalidate();
			}
			break;
		}
		
		return true;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		//Determine the diameter and the radius based on device orientation
		if(w > h) {
			mDiameter = h;
			mRadius = mDiameter/2 - (getPaddingTop() + getPaddingBottom());
		} else {
			mDiameter = w;
			mRadius = mDiameter/2 - (getPaddingLeft() + getPaddingRight());
		}
		//Init the draw arc Rect object
		mMenuRect = getRadialMenuRect(false);
		mMenuCenterButtonRect = getRadialMenuRect(true);
	}
		
	/**
	 * Draw the toggle menu button text.
	 * @param canvas
	 * @param paint
	 */
	private void drawCenterText(Canvas canvas, Paint paint) {
		paint.setColor(mToggleMenuTextColor);
		switch(mOrientation) {
		case VERTICAL_RIGHT:
			canvas.drawText(centerMenuText, getWidth() - paint.measureText(centerMenuText), getHeight()/2, paint);
			break;
		case VERTICAL_LEFT:
			canvas.drawText(centerMenuText, 2, getHeight()/2, paint);
			break;
		case HORIZONTAL_TOP:
			canvas.drawText(centerMenuText, (getWidth()/2) - (paint.measureText(centerMenuText)/2), textSize, paint);
			break;
		case HORIZONTAL_BOTTOM:
			canvas.drawText(centerMenuText, (getWidth()/2) - (paint.measureText(centerMenuText)/2), getHeight() - (textSize), paint);
			break;
		}
	}
	
	/**
	 * Get the arc drawing rects
	 * @param isCenterButton
	 * @return
	 */
	private RectF getRadialMenuRect(boolean isCenterButton) {
		int left, right, top, bottom;
		left = right = top = bottom= 0;
		switch(mOrientation) {
		case VERTICAL_RIGHT:
			if(isCenterButton) {
				left = getWidth() - (int) (mRadius/mOpenButtonScaleFactor);
				right = getWidth() + (int) (mRadius/mOpenButtonScaleFactor);
				top = (getHeight()/2) - (int) (mRadius/mOpenButtonScaleFactor);
				bottom = (getHeight()/2) + (int) (mRadius/mOpenButtonScaleFactor);				
			} else {
				left = getWidth() - (int) mRadius;
				right = getWidth() + (int) mRadius;
				top = (getHeight()/2) - (int) mRadius;
				bottom = (getHeight()/2) + (int) mRadius;
			}
			mStartAngle = 90;
			mViewAnchorPoints = new Point(getWidth(), getHeight()/2);
			break;
		case VERTICAL_LEFT:
			if(isCenterButton) {
				left = -(int) (mRadius/mOpenButtonScaleFactor);
				right = (int) (mRadius/mOpenButtonScaleFactor);
				top = (getHeight()/2) - (int) (mRadius/mOpenButtonScaleFactor);
				bottom = (getHeight()/2) + (int) (mRadius/mOpenButtonScaleFactor);
			} else {
				left = -(int) mRadius;
				right = (int) mRadius;
				top = (getHeight()/2) - (int) mRadius;
				bottom = (getHeight()/2) + (int) mRadius;
			}
			mStartAngle = 270;
			mViewAnchorPoints = new Point(0, getHeight()/2);
			break;
		case HORIZONTAL_TOP:
			if(isCenterButton) {
				left = (getWidth()/2) - (int) (mRadius/mOpenButtonScaleFactor);
				right = (getWidth()/2) + (int) (mRadius/mOpenButtonScaleFactor);
				top = -(int) (mRadius/mOpenButtonScaleFactor);
				bottom = (int) (mRadius/mOpenButtonScaleFactor);
			} else {
				left = (getWidth()/2) - (int) mRadius;
				right = (getWidth()/2) + (int) mRadius;
				top = -(int) mRadius;
				bottom = (int) mRadius;
			}
			mStartAngle = 0;
			mViewAnchorPoints = new Point(getWidth()/2, 0);
			break;
		case HORIZONTAL_BOTTOM:
			if(isCenterButton) {
				left = (getWidth()/2) - (int) (mRadius/mOpenButtonScaleFactor);
				right = (getWidth()/2) + (int) (mRadius/mOpenButtonScaleFactor);
				top = getHeight() - (int) (mRadius/mOpenButtonScaleFactor);
				bottom = getHeight() + (int) (mRadius/mOpenButtonScaleFactor);
			} else {
				left = (getWidth()/2) - (int) mRadius;
				right = (getWidth()/2) + (int) mRadius;
				top = getHeight() - (int) mRadius;
				bottom = getHeight() + (int) mRadius;
			}
			mStartAngle = 180;
			mViewAnchorPoints = new Point(getWidth()/2, getHeight());
			break;
		}
		Rect rect = new Rect(left, top, right, bottom);
		Log.i(VIEW_LOG_TAG, " Top " + top + " Bottom " + bottom + " Left " + left + "  Right " + right);
		return new RectF(rect); 
	}
	
	/********************************************************************************************
	 * Getter and setter methods
	 ********************************************************************************************/
	
	/**
	 * Set the orientation the semi-circular radial menu.
	 * There are four possible orientations only
	 * VERTICAL_RIGHT , VERTICAL_LEFT , HORIZONTAL_TOP, 
	 * HORIZONTAL_BOTTOM
	 * @param orientation
	 */
	public void setOrientation(int orientation) {
		mOrientation = orientation;
		mMenuRect = getRadialMenuRect(false);
		mMenuCenterButtonRect = getRadialMenuRect(true);
		invalidate();
	}
	
	/**
	 * Add a menu item with it's identifier tag
	 * @param idTag - Menu item identifier id
	 * @param mMenuItem - RadialMenuItem object
	 */
	public void addMenuItem(String idTag, SemiCircularRadialMenuItem mMenuItem) {
		mMenuItems.put(idTag, mMenuItem);
		invalidate();
	}
	
	/**
	 * Remove a menu item with it's identifier tag
	 * @param idTag  - Menu item identifier id
	 */
	public void removeMenuItemById(String idTag) {
		mMenuItems.remove(idTag);
		invalidate();
	}
	
	/**
	 * Remove a all menu items
	 */
	public void removeAllMenuItems() {
		mMenuItems.clear();
		invalidate();
	}

	/**
	 * Dismiss an open menu.
	 */
	public void dismissMenu() {
		isMenuVisible = false;
		centerMenuText = openMenuText;
		invalidate();
	}

	/**
	 * @return the mShadowRadius
	 */
	public float getShadowRadius() {
		return mShadowRadius;
	}

	/**
	 * @param mShadowRadius the mShadowRadius to set
	 */
	public void setShadowRadius(int mShadowRadius) {
		this.mShadowRadius = mShadowRadius * getResources().getDisplayMetrics().density;
		invalidate();
	}

	/**
	 * @return the isShowMenuText
	 */
	public boolean isShowMenuText() {
		return isShowMenuText;
	}

	/**
	 * @param isShowMenuText the isShowMenuText to set
	 */
	public void setShowMenuText(boolean isShowMenuText) {
		this.isShowMenuText = isShowMenuText;
		invalidate();
	}

	/**
	 * @return the mOrientation
	 */
	public int getOrientation() {
		return mOrientation;
	}

	/**
	 * @return the centerRadialColor
	 */
	public int getCenterRadialColor() {
		return centerRadialColor;
	}

	/**
	 * @param centerRadialColor the centerRadialColor to set
	 */
	public void setCenterRadialColor(int centerRadialColor) {
		this.centerRadialColor = centerRadialColor;
		invalidate();
	}

	/**
	 * @return the mShadowColor
	 */
	public int getShadowColor() {
		return mShadowColor;
	}

	/**
	 * @param mShadowColor the mShadowColor to set
	 */
	public void setShadowColor(int mShadowColor) {
		this.mShadowColor = mShadowColor;
		invalidate();
	}

	/**
	 * @return the openMenuText
	 */
	public String getOpenMenuText() {
		return openMenuText;
	}

	/**
	 * @param openMenuText the openMenuText to set
	 */
	public void setOpenMenuText(String openMenuText) {
		this.openMenuText = openMenuText;
		if(!isMenuTogglePressed)
			centerMenuText = openMenuText;
		invalidate();
	}

	/**
	 * @return the closeMenuText
	 */
	public String getCloseMenuText() {
		return closeMenuText;
	}

	/**
	 * @param closeMenuText the closeMenuText to set
	 */
	public void setCloseMenuText(String closeMenuText) {
		this.closeMenuText = closeMenuText;
		if(isMenuTogglePressed)
			centerMenuText = closeMenuText;
		invalidate();
	}

	/**
	 * @return the mToggleMenuTextColor
	 */
	public int getToggleMenuTextColor() {
		return mToggleMenuTextColor;
	}

	/**
	 * @param mToggleMenuTextColor the mToggleMenuTextColor to set
	 */
	public void setToggleMenuTextColor(int mToggleMenuTextColor) {
		this.mToggleMenuTextColor = mToggleMenuTextColor;
		invalidate();
	}

	/**
	 * @return the textSize
	 */
	public float getTextSize() {
		return textSize;
	}

	/**
	 * @param textSize the textSize to set
	 */
	public void setTextSize(int textSize) {
		this.textSize = textSize  * getResources().getDisplayMetrics().density;
		mRadialMenuPaint.setTextSize(this.textSize);
		invalidate();
	}

	/**
	 * @return the mOpenButtonScaleFactor
	 */
	public int getOpenButtonScaleFactor() {
		return mOpenButtonScaleFactor;
	}

	/**
	 * @param mOpenButtonScaleFactor the mOpenButtonScaleFactor to set
	 */
	public void setOpenButtonScaleFactor(int mOpenButtonScaleFactor) {
		this.mOpenButtonScaleFactor = mOpenButtonScaleFactor;
		invalidate();
	}
}
