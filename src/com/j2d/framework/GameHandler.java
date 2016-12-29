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
/**
 * Handler是一个场景和事务控制器，负责切换场景，包括将当前Frame上的鼠标、键盘等事件都切换到指定场景
 */

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.j2d.controlers.AbstractControler;
import com.j2d.controlers.IControler;


public class GameHandler implements IHandler {

	public GameHandler() {

	}

	public synchronized IControler getControl() {
		return AbstractControler.currentControl;
	}

	public synchronized void setControl(final IControler control) {
		AbstractControler.currentControl = control;
	}

	

	public void keyPressed(KeyEvent e) {
		if (AbstractControler.validControl())
			AbstractControler.currentControl.keyPressed(e);
	}

	public void keyReleased(KeyEvent e) {
		if (AbstractControler.validControl())
			AbstractControler.currentControl.keyReleased(e);
	}

	public void keyTyped(KeyEvent e) {
		if (AbstractControler.validControl())
			AbstractControler.currentControl.keyTyped(e);
	}

	public void mouseClicked(MouseEvent e) {
		if (AbstractControler.validControl())
			AbstractControler.currentControl.mouseClicked(e);
	}

	public void mouseEntered(MouseEvent e) {
		if (AbstractControler.validControl())
			AbstractControler.currentControl.mouseEntered(e);
	}

	public void mouseExited(MouseEvent e) {
		if (AbstractControler.validControl())
			AbstractControler.currentControl.mouseExited(e);
	}

	public void mousePressed(MouseEvent e) {
		if (AbstractControler.validControl())
			AbstractControler.currentControl.mousePressed(e);
	}

	public void mouseReleased(MouseEvent e) {
		if (AbstractControler.validControl())
			AbstractControler.currentControl.mouseReleased(e);
	}

	public void mouseDragged(MouseEvent e) {
		if (AbstractControler.validControl())
			AbstractControler.currentControl.mouseDragged(e);
	}

	public void mouseMoved(MouseEvent e) {
		if (AbstractControler.validControl())
			AbstractControler.currentControl.mouseMoved(e);
	}

}
