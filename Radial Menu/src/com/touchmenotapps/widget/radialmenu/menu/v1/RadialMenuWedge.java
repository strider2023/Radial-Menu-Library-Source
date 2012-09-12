/*
 * Copyright (C) 2012 
 * Jason Valestin (valestin@gmail.com ) and Arindam Nath (strider2023@gmail.com)
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

package com.touchmenotapps.widget.radialmenu.menu.v1;

import android.graphics.Path;
import android.graphics.RectF;

/**
 * This class handles the creation of wedges in the menu.
 * 
 * @author Jason Valestin (valestin@gmail.com )
 * @author Arindam Nath (strider2023@gmail.com)
 */
public class RadialMenuWedge extends Path {
	private int x, y;
	private int InnerSize, OuterSize;
	private float StartArc;
	private float ArcWidth;

	/**
	 * 
	 * @param x
	 * @param y
	 * @param InnerSize
	 * @param OuterSize
	 * @param StartArc
	 * @param ArcWidth
	 */
	protected RadialMenuWedge(int x, int y, int InnerSize, int OuterSize,
			float StartArc, float ArcWidth) {
		super();
		if (StartArc >= 360) {
			StartArc = StartArc - 360;
		}
		this.x = x;
		this.y = y;
		this.InnerSize = InnerSize;
		this.OuterSize = OuterSize;
		this.StartArc = StartArc;
		this.ArcWidth = ArcWidth;
		this.buildPath();
	}

	/**
	 * 
	 */
	protected void buildPath() {
		final RectF rect = new RectF();
		final RectF rect2 = new RectF();
		// Rectangles values
		rect.set(this.x - this.InnerSize, this.y - this.InnerSize, this.x
				+ this.InnerSize, this.y + this.InnerSize);
		rect2.set(this.x - this.OuterSize, this.y - this.OuterSize, this.x
				+ this.OuterSize, this.y + this.OuterSize);
		this.reset();
		// this.moveTo(100, 100);
		this.arcTo(rect2, StartArc, ArcWidth);
		this.arcTo(rect, StartArc + ArcWidth, -ArcWidth);
		this.close();
	}
}
