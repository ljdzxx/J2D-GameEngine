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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import com.j2d.framework.GraphicsUtils;
import com.j2d.framework.LSystem;

/**
 * 可自定义大小的Panel控件   
 * @author Leijun
 * @version   
 *       1.0.0, Jan 23, 2013 4:37:51 PM
 */
public class SizeablePanel extends ComponentAdapter {
	
	//透明度 
	private float fillAlpha;
	//4个角弧的半径
	private int arcRadius;
	//填充色
	private Color fillColor;
	//描边色
	private Color strokeColor;
	//透明度
	private AlphaComposite fillAlphaComposite;
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param arcRadius-4个圆角的弧半径
	 */
	public SizeablePanel(int x,int y, int width,int height,int arcRadius) {
		super(x, y, width, height);
		this.arcRadius=arcRadius;
		setFillAlpha(1.0f);//不透明
		
		this.fillColor=Color.white;
		this.strokeColor=Color.BLACK;
	}
	
	public SizeablePanel(int x,int y, int width,int height,int arcRadius, float fillAlpha) {
		this(x, y, width, height, arcRadius);
		setFillAlpha(fillAlpha);
	}
	
	public SizeablePanel(int x,int y, int width,int height,int arcRadius, float alpha,Color strokeColorlor) {
		this(x, y, width, height, arcRadius,alpha);
		this.strokeColor=strokeColorlor;
	}
	
	public SizeablePanel(int x,int y, int width,int height,int arcRadius, 
			float alpha,Color strokeColorlor, Color fillColor) {
		this(x, y, width, height, arcRadius,alpha,strokeColorlor);
		this.fillColor=fillColor;
	}
	
	/**
	 * 绘画方法
	 * @param g2d
	 * @author Leijun
	 */
	@Override
	public void draw(Graphics2D g2d){
		
		RenderingHints rh=g2d.getRenderingHints();
		GraphicsUtils.setRenderingHints(g2d);//高质量、防锯齿绘制
		
		//Stroke sk=g2d.getStroke();//设置画笔
		//Stroke stroke=new BasicStroke(this.strokeWidth,BasicStroke.CAP_BUTT,BasicStroke.JOIN_ROUND);
		//g2d.setStroke(stroke);
		
		g2d.setColor(this.strokeColor);
		Composite comp=g2d.getComposite();
		//g2d.setComposite(this.alphaComposite);//设置透明度
		g2d.setComposite(LSystem.alpha_05);//设置透明度
		g2d.drawRoundRect(x, y, width, height, this.arcRadius*2, this.arcRadius*2);
		g2d.setComposite(LSystem.alpha_20);//设置透明度
		g2d.drawRoundRect(x+1, y+1, width-1*2, height-1*2, (this.arcRadius-1)*2, (this.arcRadius-1)*2);
		g2d.setComposite(this.fillAlphaComposite);//设置透明度
		g2d.setColor(this.fillColor);
		g2d.fillRoundRect(x+2, y+2, width-2*2, height-2*2, (this.arcRadius-2)*2, (this.arcRadius-2)*2);
		/*
		int realX=this.x+(int)this.strokeWidth/2;
		int realY=this.y+(int)this.strokeWidth/2;
		int realW=this.width-(int)this.strokeWidth;
		int realH=this.height-(int)this.strokeWidth;
		//外边色
		g2d.setColor(this.strokeColor);
		g2d.drawRect(realX,realY, realW, realH);
		//内描边
		g2d.setColor(this.fillColor);
		g2d.drawRect(++realX,++realY, realW-2, realH-2);
		//瞄底
		g2d.setStroke(new BasicStroke());
		g2d.setColor(this.strokeColor);
		g2d.drawLine(realX, realY+realH+(int)this.strokeWidth/2-1, realX+realW, realY+realH+(int)this.strokeWidth/2-1);
		
		GraphicsUtils.setAntialias(g2d, false);//填充色不抗锯齿
		GraphicsUtils.setRenderQuality(g2d, false);//渲染速度优先
		//填充色
		g2d.setColor(this.fillColor);
		g2d.fillRect(++realX,++realY, realW-2, realH-2);
		*/
		
		//还原环境设置
		g2d.setComposite(comp);
		//g2d.setStroke(sk);
		g2d.setRenderingHints(rh);
	}

	public float getFillAlpha() {
		return fillAlpha;
	}

	public void setFillAlpha(float alpha) {
		this.fillAlpha = alpha;
		fillAlphaComposite=AlphaComposite	//透明度
			.getInstance(AlphaComposite.SRC_OVER, this.fillAlpha);
	}

	/*public float getStrokeWidth() {
		return strokeWidth;
	}

	public void setStrokeWidth(float strokeWidth) {
		this.strokeWidth = strokeWidth;
	}*/

	public Color getFillColor() {
		return fillColor;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	public int getArcRadius() {
		return arcRadius;
	}

	public void setArcRadius(int arcRadius) {
		this.arcRadius = arcRadius;
	}

	public Color getStrokeColor() {
		return strokeColor;
	}

	public void setStrokeColor(Color strokeColor) {
		this.strokeColor = strokeColor;
	}
}
