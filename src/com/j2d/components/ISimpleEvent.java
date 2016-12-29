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

package com.j2d.components;

import java.awt.Point;
import java.awt.event.MouseEvent;

public interface ISimpleEvent {
	
	/**
	 * 左键按下
	 */
	public abstract void leftMousePressed(ComponentAdapter c, Point p);
	
	/**
	 * 左键被释放</p>
	 * 注：释放的地点不一定是点击地点，但是该事件的执行一定是在被点击的控件上，而不是被释放的控件上</p>
	 * @author Leijun
	 */
	public abstract void leftMouseReleased(ComponentAdapter c, Point p);
	
	/**
	 * 键盘有键被按下
	 */
	public abstract void keyPressed(ComponentAdapter c, int keyCode, char keyChar);
	
	/**
	 * 鼠标拖拽
	 * @param e
	 * @author Leijun
	 */
	public abstract void mouseDragged(ComponentAdapter c, MouseEvent e);

}
