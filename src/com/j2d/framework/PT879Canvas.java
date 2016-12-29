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
 * Canvas负责实现场景图形绘制，各种场景控制器最终成图都在这里实现
 */
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

import com.j2d.controlers.IControler;

public class PT879Canvas extends Canvas implements ICanvas {

	private static final long serialVersionUID = 1L;

	final static private Toolkit systemToolKit = GraphicsUtils.toolKit;

	private boolean start;

	private boolean isShowFPS;

	final static private Font fpsFont = GraphicsUtils.getFont("Dialog", 0, 20);

	final private int fpsX;// = 5;

	final static private int fpsY = 20;

	private int fps = 50;
	private long period=(1000/fps)*1000000;//一帧所需要的时间(纳秒)

	//这3个变量在计算平均fps时用到，平均fps算法：显示10帧所需要的总时间 除以 10
	//private int frames = 0;
	//private long timePoint = 0;
	//private double avgFps=0;
	//--------------------------------------------------

	private IHandler handler;

	private Graphics canvasGraphics = null;

	private BufferStrategy bufferStrategy;
	
	private long beforeTime=0, afterTime=0, timeDiff=0, sleepTime=0, endTime=0;
	private long overSleepTime=0L;
	private int noDelays=0;
	private long excess=0L;
	
	/** 
	 * Number of frames with a delay of 0 ms before the
     * animation thread yields to other running threads. 
    */	
	private static final int NO_DELAYS_PER_YIELD = 16;
	
	/**
	 * no. of frames that can be skipped in any one animation loop
     * i.e the games state is updated but not rendered
     */
  private static int MAX_FRAME_SKIPS = 5;

	public PT879Canvas(IHandler handler, int width, int height,int fps) {
		fpsX=width/2-40;
		format(handler, width, height,fps);
	}

	/*public PT879Canvas(int fps) {
		format(LSystem.currentGameHandler, LSystem.WIDTH, LSystem.HEIGHT, fps);
	}*/

	public void format(IHandler handler, int width, int height,int fps) {
		setFPS(fps);
		LSystem.WIDTH = width;
		LSystem.HEIGHT = height;
		
		this.handler = handler;
		this.start = false;
		this.setBackground(Color.black);
		this.setPreferredSize(new Dimension(width, height));
		this.setIgnoreRepaint(true);
		this.addKeyListener(handler);
		this.addMouseListener(handler);
		this.addMouseMotionListener(handler);
		//createBufferGraphics();
	}

	public void createBufferGraphics() {
		createBufferStrategy(3);
		bufferStrategy = getBufferStrategy();
	}

	// synchronized
	public void paintScreen() {
		
		if (isShowing()) {
			canvasGraphics = bufferStrategy.getDrawGraphics();
			timeDiff=endTime-beforeTime;//每帧真正消耗的时间
			beforeTime = System.nanoTime();//1纳秒= 1/10^9 秒
			
			//Update();
			drawNextScreen((Graphics2D)canvasGraphics);
		    
		    //显示FPS?
		    if (isShowFPS){
		    	if(timeDiff>0){
					//Font font = canvasGraphics.getFont();
					canvasGraphics.setFont(fpsFont);
					canvasGraphics.setColor(Color.white);
					GraphicsUtils.setAntialias(canvasGraphics,true);
					canvasGraphics.drawString(String.format("FPS:%1$4.2f", (double)1000000000/timeDiff), fpsX, fpsY);//("FPS:" + avgFps).intern()
					GraphicsUtils.setAntialias(canvasGraphics,false);
					//canvasGraphics.setFont(font);
		    	}
		    }
		    
			bufferStrategy.show();
			canvasGraphics.dispose();
			systemToolKit.sync();
			
			
			// 关于休眠时间、更新时间补偿的实现------------------------------------------------------------ 
			//尚存疑问的是，bufferStrategy.show()到底是否同步执行？如果不是，那以下的算法有误
			afterTime=System.nanoTime();
		    timeDiff=afterTime-beforeTime;
		    sleepTime=(period-timeDiff)-overSleepTime;//time left in this loop
		      
		    if(sleepTime>0){
			      try {
			        Thread.sleep(sleepTime/1000000L);  // nano->ms
			      }
			      catch(InterruptedException ex){
			    	  overSleepTime=(System.nanoTime()-afterTime)-sleepTime;
			      }
		    }else{//处理一帧所需要的时间超过了设置值
		    	  excess-=sleepTime;	//累计超出的时长
		    	  overSleepTime=0L;
		    	  if(++noDelays>=NO_DELAYS_PER_YIELD){
		    		  Thread.yield();
		    		  noDelays=0;
		    		  //System.out.println("thread yielded.");
		    	  }
		   }
	      //beforeTime = System.nanoTime();
	      
	      /* If frame animation is taking too long, update the game state
	      without rendering it, to get the updates/sec nearer to
	      the required FPS. */
	      int skips=0;
	      while((excess>period)&&(skips<MAX_FRAME_SKIPS)){
	    	  excess-=period;
	    	  handler.getControl().next();
	    	  skips++;
	    	  //System.out.println("next() executed.");
	      }
	      endTime = System.nanoTime();
			//--------------------------------------------------------------------------------------
		}else{//这里放广告
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	/*private void Update(){
		if(this.start){
			
			//IControler control = handler.getControl();
			//control.next();
			handler.getControl().next();
		}
	}*/

	private void drawNextScreen(Graphics2D g) {
		if (this.start) {
			IControler control = handler.getControl();
			control.next();
			control.draw(g);
		}
	}

	public void startPaint() {
		this.start = true;
	}

	public void endPaint() {
		this.start = false;
	}

	public int getFPS() {
		return this.fps;
	}
	
	public void setFPS(int fps) {
		this.fps=fps;
		this.period=(1000/fps)*1000000;
	}

	public void setShowFPS(boolean isShowFPS) {
		this.isShowFPS = isShowFPS;
	}

}
