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

import java.util.List;
import android.util.Log;

/**
 * This class handles the menu item creation.
 * 
 * @author Jason Valestin (valestin@gmail.com )
 * @author Arindam Nath (strider2023@gmail.com)
 */
public class RadialMenuItem implements RadialMenuInterface {

	private String menuName = "Empty";
	private String menuLabel = null;
	private int menuIcon = 0;
	private List<RadialMenuItem> menuChildren = null;
	private RadialMenuItemClickListener menuListener = null;
	
	/**
	 * Creates an instance of the RadialMenuItem.
	 * @param name - (String) If there is no name to be assigned pass null. (Name is mostly used for debugging)
	 * @param displayName - (String) If there is no display name to be assigned pass null.
	 */
	public RadialMenuItem(String name, String displayName) {
		if(name != null)
			this.menuName = name;
		this.menuLabel = displayName;
	}
	
	/**
	 * Set menu item icon.
	 * @param displayIcon - (int) Icon resource ID.
	 * <strong>secondChildItem.setDisplayIcon(R.drawable.ic_launcher);</strong>
	 */
	public void setDisplayIcon(int displayIcon) {
		this.menuIcon = displayIcon;
	}
	
	/**
	 * Set the on menu item click event.
	 * @param listener
	 */
	public void setOnMenuItemPressed(RadialMenuItemClickListener listener) {
		menuListener = listener;
	}
	
	/**
	 * Set the menu child items.
	 * @param childItems - Pass the list of child items.
	 */
	public void setMenuChildren(List<RadialMenuItem> childItems) {
		this.menuChildren = childItems;
	}
	
	@Override
	public String getName() {
		return menuName;
	}

	@Override
	public String getLabel() {
		return menuLabel;
	}

	@Override
	public int getIcon() {
		return menuIcon;
	}

	@Override
	public List<RadialMenuItem> getChildren() {
		return menuChildren;
	}

	@Override
	public void menuActiviated() {
		Log.i(this.getClass().getName(), menuName + " menu pressed.");
		if(menuListener != null)
			menuListener.execute();
	}
	
	public interface RadialMenuItemClickListener {
		public void execute();
	}
}
