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

/**
 * Interface for radial menu item data.
 */
public interface RadialMenuInterface {
	public String getName();
	public String getLabel();
	public int getIcon();
	public List<RadialMenuItem> getChildren();
	public void menuActiviated();
}
