/*
 * Copyright (C) 2012 
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

package com.touchmenotapps.widget.radialmenu.menu.v2;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

/**
 * 
 * @author Arindam Nath (strider2023@gmail.com)
 * TODO To add in icons
 */
public class RadialMenuView extends View {

	private ArrayList<RadialMenuItem> mRadialMenuContent = new ArrayList<RadialMenuItem>(0);
	
	boolean alt;
	
	float mWidth = -1;//center of screen, will change to touch location
	
	float mHeight = -1;
	
	float mThickness;
	
	float mRadius;
	
	int selected = -1;
	
	int lastE = -1;//last event, used to prevent excessive redrawing
	
	float[] endTouch;

	private Paint mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

	private Paint mSelectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

	private Paint mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	private RadialMenuHelperFunctions mHelperFunctions;
	
	/**
	 * 
	 * @param context
	 * @param renderer
	 */
	public RadialMenuView(Context context, RadialMenuRenderer renderer) {
		super(context);
		mHelperFunctions = new RadialMenuHelperFunctions();
		mRadialMenuContent = renderer.getRadialMenuContent();
		alt = renderer.isAlt();
		mThickness = renderer.getMenuThickness();
		mRadius = renderer.getRadius();
		setVisibility(GONE);
		initSetPaint(renderer);
	}
	
	/**
	 * 
	 * @param renderer
	 */
	private void initSetPaint(RadialMenuRenderer renderer) {
		mBgPaint.setColor(renderer.getMenuBackgroundColor());
		mBgPaint.setStrokeWidth(renderer.getMenuThickness());
		mBgPaint.setStyle(Paint.Style.STROKE);

		mSelectedPaint.setColor(renderer.getMenuSelectedColor());
		mSelectedPaint.setStrokeWidth(renderer.getMenuThickness());
		mSelectedPaint.setStyle(Paint.Style.STROKE);

		mBorderPaint.setColor(renderer.getMenuBorderColor());
		mBorderPaint.setStrokeWidth(renderer.getMenuThickness());
		mBorderPaint.setStyle(Paint.Style.STROKE);

		mTextPaint.setColor(renderer.getMenuTextColor());
		mTextPaint.setTextSize((float) (renderer.getMenuThickness() / 2));
	}

	//prevents offscreen drawing and calcs
	public void setLoc(float x, float y) {
		if (x < mRadius + mThickness / 2)
			x = mRadius + mThickness / 2;
		if (y < mRadius + mThickness / 2)
			y = mRadius + mThickness / 2;

		if (y > this.getHeight() - (mRadius + mThickness / 2))
			y = this.getHeight() - (mRadius + mThickness / 2);
		if (x > this.getWidth() - (mRadius + mThickness / 2))
			x = this.getWidth() - (mRadius + mThickness / 2);

		mWidth = x;
		mHeight = y;
	}

	@Override
	public void onDraw(Canvas canvas) {
		//Fixes drawing off screen
		setLoc(mWidth, mHeight);
		final RectF rect = new RectF();
		rect.set(mWidth - mRadius, mHeight - mRadius, mWidth + mRadius, mHeight + mRadius);
		int tot = mRadialMenuContent.size();

		mBorderPaint.setStrokeWidth(mThickness);
		//draws back of radial first
		for (int counter = 0; counter < tot; counter++) {
			if (!mRadialMenuContent.get(counter).equals(RadialMenuRenderer.RADIAL_NO_TEXT))
				if (alt) 
					canvas.drawArc(rect, (float) (360 / tot * counter - 90 - 360 / tot / 2), (float) (360 / tot), false, (selected == counter ? mSelectedPaint : mBgPaint));
				else 
					canvas.drawArc(rect, (float) (360 / tot * counter - 90), (float) (360 / tot), false, (selected == counter ? mSelectedPaint : mBgPaint));
		}

		//draws text
		for (int counter = 0; counter < tot; counter++) {
			if (!mRadialMenuContent.get(counter).equals(RadialMenuRenderer.RADIAL_NO_TEXT)) {
				Path arc = new Path();
				if (alt) {
					arc.addArc(rect, (float) (360 / tot * counter - 90 - 360 / tot / 2) + 10, (float) (360 / tot) - 10);
					canvas.drawTextOnPath(mRadialMenuContent.get(counter).getMenuName(), arc, 0, +mThickness / 8, mTextPaint);
				} else {
					arc.addArc(rect, (float) (360 / tot * counter - 90) + 10, (float) (360 / tot) - 10);
					canvas.drawTextOnPath(mRadialMenuContent.get(counter).getMenuName(), arc, 0, -mThickness / 8, mTextPaint);
				}
			}
		}

		//draws separators between each option
		if (tot > 1)
			for (int counter = 0; counter < tot; counter++) {
				if (!mRadialMenuContent.get(counter).equals(RadialMenuRenderer.RADIAL_NO_TEXT))
					if (alt) {
						canvas.drawArc(rect, (float) (360 / tot * counter - 91 - 360 / tot / 2), 2, false, mBorderPaint);
						canvas.drawArc(rect, (float) (360 / tot * (counter + 1) - 91 - 360 / tot / 2), 2, false, mBorderPaint);
					} else {
						canvas.drawArc(rect, (float) (360 / tot * counter - 91), 2, false, mBorderPaint);
						canvas.drawArc(rect, (float) (360 / tot * (counter + 1) - 91), 2, false, mBorderPaint);
					}
			}

		//draws outer and inner boarders
		mBorderPaint.setStrokeWidth(2);
		rect.set(mWidth - mRadius - mThickness / 2, mHeight - mRadius - mThickness / 2, mWidth + mRadius + mThickness / 2, mHeight + mRadius + mThickness / 2);

		for (int counter = 0; counter < tot; counter++) {
			if (!mRadialMenuContent.get(counter).equals(RadialMenuRenderer.RADIAL_NO_TEXT))
				if (alt) {
					canvas.drawArc(rect, (float) (360 / tot * counter - 91 - 360 / tot / 2), (float) (360 / tot) + 2, false, mBorderPaint);
				} else {
					canvas.drawArc(rect, (float) (360 / tot * counter - 91), (float) (360 / tot) + 2, false, mBorderPaint);
				}
		}

		rect.set(mWidth - mRadius + mThickness / 2, mHeight - mRadius + mThickness / 2, mWidth + mRadius - mThickness / 2, mHeight + mRadius - mThickness / 2);

		for (int counter = 0; counter < tot; counter++) {
			if (!mRadialMenuContent.get(counter).equals(RadialMenuRenderer.RADIAL_NO_TEXT))
				if (alt) {
					canvas.drawArc(rect, (float) (360 / tot * counter - 91 - 360 / tot / 2), (float) (360 / tot) + 1, false, mBorderPaint);
				} else {
					canvas.drawArc(rect, (float) (360 / tot * counter - 91), (float) (360 / tot) + 1, false, mBorderPaint);
				}
		}
	}

	/**
	 * Handles resulting event from onTouch up.
	 * @param e
	 * @return
	 */
	private boolean handleEvent(int e) {
		if (e == mRadialMenuContent.size())
			e = 0;
		else if (e == -1) {
			selected = -1;
			return false;
		}
		if (mRadialMenuContent.get(e).getMenuName().equals(RadialMenuRenderer.RADIAL_NO_TEXT)) {
			selected = -1;
			invalidate();
			return false;
		}
		mRadialMenuContent.get(e).getOnRadailMenuClick()
			.onRadailMenuClickedListener(mRadialMenuContent.get(e).getMenuID());
		selected = -1;
		invalidate();
		return true;
	}

	/**
	 * Handles moving gestures that haven't finished yet.
	 * @param e
	 */
	private void preEvent(int e) {
		if (e == mRadialMenuContent.size())
			e = 0;
		else if (lastE == e)
			return;
		lastE = e;
		if (e == -1) {
			selected = -1;
			invalidate();
			return;
		}
		if (mRadialMenuContent.get(e).getMenuName().equals(RadialMenuRenderer.RADIAL_NO_TEXT)) {
			selected = -1;
			invalidate();
			return;
		}
		selected = e;
		invalidate();
		return;
	}

	public boolean gestureHandler(MotionEvent event, boolean eat) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			endTouch = new float[] { event.getX(), event.getY() };
			if (mHelperFunctions.distance(mWidth, mHeight, endTouch[0], endTouch[1]) > mRadius - mThickness / 2) {
				this.setVisibility(View.GONE);
				return handleEvent((int) mHelperFunctions.angle(mWidth, mHeight, endTouch[0], endTouch[1], alt, mRadialMenuContent.size()));
			} else {
				this.setVisibility(View.GONE);
				return handleEvent(-1);
			}
		} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
			mWidth = event.getX();
			mHeight = event.getY();
			this.setVisibility(View.VISIBLE);
			invalidate();
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {//drag
			endTouch = new float[] { event.getX(), event.getY() };
			if (mHelperFunctions.distance(mWidth, mHeight, endTouch[0], endTouch[1]) > mRadius - mThickness / 2) {
				preEvent((int) mHelperFunctions.angle(mWidth, mHeight, endTouch[0], endTouch[1], alt, mRadialMenuContent.size()));
			} else {
				preEvent(-1);
			}
		}
		//Eats touch if needed, fixes scrollable elements from interfering
		return eat;
	}
}
