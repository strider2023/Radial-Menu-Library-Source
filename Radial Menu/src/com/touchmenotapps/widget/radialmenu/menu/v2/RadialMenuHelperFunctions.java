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

/**
 * This is the helper class for Radial Menu widget.
 * It contains certain functions that are used in creating the radial menu.
 * 
 * @author Arindam Nath (strider2023@gmail.com)
 */
public class RadialMenuHelperFunctions {

	/**
	 * 
	 * @param mWidth
	 * @param mHeight
	 * @param x2
	 * @param y2
	 * @return
	 */
	public float distance(float mWidth, float mHeight, float x2, float y2) {
		double dx = mWidth - x2; //horizontal difference 
		double dy = mHeight - y2; //vertical difference 
		float dist = (float) Math.sqrt(dx * dx + dy * dy); //distance using Pythagoras theorem
		return dist;
	}

	/**
	 * 
	 * @param mWidth
	 * @param mHeight
	 * @param x2
	 * @param y2
	 * @param alt
	 * @param items
	 * @return
	 */
	public float angle(float mWidth, float mHeight, float x2, float y2, boolean alt, int items) {
		double dx = x2 - mWidth; //horizontal difference 
		double dy = y2 - mHeight; //vertical difference 
		float angle = (float) (Math.atan2(dy, dx) * 180 / Math.PI) + 90 + (alt ? (360 / items) / 2 : 0);
		if (angle < 0)
			return (angle + 360) / (360 / items);
		return angle / (360 / items);
	}
}
