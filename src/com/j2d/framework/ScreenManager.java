/****************************************************************************
 Copyright (c) 2013-2015 Leijun

 http://github.com/ljdzxx

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 ****************************************************************************/

package com.j2d.framework;

import java.awt.DisplayMode;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;


public class ScreenManager {

	final public static GraphicsEnvironment environment = GraphicsEnvironment
			.getLocalGraphicsEnvironment();

	final public static GraphicsDevice graphicsDevice = environment
			.getDefaultScreenDevice();

	final public static GraphicsConfiguration graphicsConfiguration = graphicsDevice
			.getDefaultConfiguration();
	
	public static DisplayMode searchFullScreenModeDisplay() {
		return searchFullScreenModeDisplay(graphicsDevice);
	}
	
	public static DisplayMode searchFullScreenModeDisplay(GraphicsDevice device) {
		DisplayMode displayModes[] = device.getDisplayModes();
		int currentDisplayPoint = 0;
		DisplayMode fullScreenMode = null;
		DisplayMode normalMode = device.getDisplayMode();
		DisplayMode adisplaymode[] = displayModes;
		int i = 0;
		for (int j = adisplaymode.length; i < j; i++) {
			DisplayMode mode = adisplaymode[i];
			if (mode.getWidth() == LSystem.WIDTH
					&& mode.getHeight() == LSystem.HEIGHT) {
				int point = 0;
				if (normalMode.getBitDepth() == mode.getBitDepth())
					point += 40;
				else
					point += mode.getBitDepth();
				if (normalMode.getRefreshRate() == mode.getRefreshRate())
					point += 5;
				if (currentDisplayPoint < point) {
					fullScreenMode = mode;
					currentDisplayPoint = point;
				}
			}
		}

		return fullScreenMode;
	}
}
