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

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PT879Frame extends Frame implements Runnable {
	private static final long serialVersionUID = 1L;
	private String titleName;
	private Thread mainLoop;
	private ICanvas canvas;
	private boolean isFullScreen;
	private DisplayMode normalMode;
	private int fps;
	
	public PT879Frame(){}

	public PT879Frame(String titleName, int width, int height, int fps) {
		this(new PT879Handler(), titleName, width, height,fps);
	}

	public PT879Frame(IHandler handler, String titleName, int width, int height, int fps) {
		super(titleName);
		addShutdownHook();
		LSystem.currentGameHandler = handler;
		setFps(fps);
		//this.handler = handler;
		this.titleName = titleName;
		this.addKeyListener(handler);
		this.setPreferredSize(new Dimension(width + 5, height + 25));
		this.requestFocus();
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.initCanvas(width, height);
		this.pack();
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setIgnoreRepaint(true);
		this.canvas.createBufferGraphics();
		LSystem.currentCanvas = this.canvas;
	}
	
	/**
	 * 退出之前要干的事
	 * @author Leijun
	 */
	private void addShutdownHook(){
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run(){

			}
		});
	}

	public void setCursor(Cursor cursor) {
		LSystem.setSystemCursor(cursor);
	}

	public boolean isFullScreen() {
		return isFullScreen;
	}

	//synchronized 
	public void updateNormalScreen() {
		if (!isFullScreen) {
			return;
		} else {
			isFullScreen = false;
			this.setVisible(false);
			ScreenManager.graphicsDevice.setDisplayMode(normalMode);
			this.removeNotify();
			this.setUndecorated(false);
			ScreenManager.graphicsDevice.setFullScreenWindow(null);
			this.addNotify();
			this.pack();
			this.setVisible(true);
			this.updateDisplayMode();
			return;
		}
	}

	//synchronized 
	public void updateFullScreen() {
		if (isFullScreen) {
			return;
		}
		isFullScreen = true;
		try {
			DisplayMode useDisplayMode = ScreenManager
					.searchFullScreenModeDisplay();
			if (useDisplayMode == null) {
				return;
			}
			this.setVisible(false);
			this.removeNotify();
			this.setUndecorated(true);
			ScreenManager.graphicsDevice.setFullScreenWindow(this);
			ScreenManager.graphicsDevice.setDisplayMode(useDisplayMode);
			this.addNotify();
			this.setVisible(true);
			this.pack();
			this.updateDisplayMode();
			this.requestFocus();
		} catch (RuntimeException e) {
			this.updateNormalScreen();
		}
	}

	//synchronized 
	private void updateDisplayMode() {
		createBufferStrategy(3);
	}

	public void setShowFPS(boolean isFPS) {
		LSystem.currentCanvas.setShowFPS(isFPS);
	}
	
	

	/*public int getShowFPS() {
		return gameCanvas.getShowFPS();
	}*/

	public void showFrame() {
		this.setVisible(true);
	}

	public void hideFrame() {
		this.setVisible(false);
	}

	public void run() {
		try {
			for (;;) {
				LSystem.currentCanvas.paintScreen();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void startThread() {
		this.mainLoop = new Thread(this);
		this.mainLoop.setPriority(Thread.MIN_PRIORITY);
		this.mainLoop.start();
	}

	private void initCanvas(final int width, final int height) {
		PT879Canvas pt879Canvas = new PT879Canvas(LSystem.currentGameHandler, width, height,this.fps);
		this.canvas = pt879Canvas;
		this.canvas.startPaint();
		this.add(pt879Canvas);
	}

	/*public IHandler getHandler() {
		return this.handler;
	}*/

	/*public Thread getMainLoop() {
		return mainLoop;
	}*/

	public String getTitleName() {
		return titleName;
	}

	public int getFps() {
		return fps;
	}

	public void setFps(int fps) {
		this.fps = fps;
		LSystem.fps=fps;
	}

}
