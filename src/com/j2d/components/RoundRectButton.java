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

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import com.j2d.controlers.AbstractControler;
import com.j2d.framework.GraphicsUtils;

/**
 * 可定义大小，颜色的圆角矩形按钮   
 * @author Leijun
 * @version   
 *       1.0.0, Jan 26, 2013 4:53:50 PM
 */
public class RoundRectButton extends ComponentAdapter {

	private int arcRadius;//圆弧半径
	private int borderWidth;//边的宽度
	private Color fromCr=new Color(222,227,33);
	private Color toCr=new Color(57,182,74);//渐变色
	private Color fromCr2=new Color(250,250,250);//内填充色从
	private Color toCr2=new Color(210,210,210);//内填充色至
	private Color mouseDownFontColor=Color.BLACK;//鼠标按下时字体的颜色
	GradientPaint gPaint,gPaint2,gPaint3,gPaint4;
	
	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param arcRadius-圆弧半径
	 * @param borderWidth-外边距离内边的距离
	 */
	public RoundRectButton(int x, int y, int width, int height, int arcRadius, int borderWidth) {
		super(x, y, width, height);
		this.arcRadius=arcRadius;
		this.borderWidth=borderWidth;
		intiParint();
	}

	@Override
	public void draw(Graphics2D g2d) {
		if(this.active && AbstractControler.leftMouseDown){
			drawMouseDown(g2d);
		}
		else{
			drawNormal(g2d);
		}
	}
	
	public void drawMouseDown(Graphics2D g2d){
		RenderingHints rh=g2d.getRenderingHints();
		GraphicsUtils.setRenderingHints(g2d);//高质量、防锯齿绘制
		
		
		g2d.setPaint(gPaint3);
		g2d.fillRoundRect(x, y, width, height, 2*arcRadius, 2*arcRadius);
		
		g2d.setPaint(gPaint4);
		g2d.fillRoundRect(x+this.borderWidth, y+this.borderWidth, width-2*this.borderWidth, height-2*this.borderWidth, 2*(arcRadius-borderWidth), 2*(arcRadius-borderWidth));
		
		g2d.setPaint(null);
		
		
		//文字
		if(this.font!=null){
			g2d.setFont(font);
		}
		if(this.mouseDownFontColor!=null){
			g2d.setColor(mouseDownFontColor);
		}
		FontMetrics fm=g2d.getFontMetrics(g2d.getFont());
		int fontWidth=fm.stringWidth(this.label);
		int fontHeight=fm.getAscent();
		g2d.drawString(this.label, x+(this.width-fontWidth)/2, y+this.height/2+fontHeight/2);		
		g2d.setRenderingHints(rh);
	}
	
	public void drawNormal(Graphics2D g2d){
		RenderingHints rh=g2d.getRenderingHints();
		GraphicsUtils.setRenderingHints(g2d);//高质量、防锯齿绘制
		
		
		g2d.setPaint(gPaint);
		g2d.fillRoundRect(x, y, width, height, 2*arcRadius, 2*arcRadius);
		
		g2d.setPaint(gPaint2);
		g2d.fillRoundRect(x+this.borderWidth, y+this.borderWidth, width-2*this.borderWidth, height-2*this.borderWidth, 2*(arcRadius-borderWidth), 2*(arcRadius-borderWidth));
		
		g2d.setPaint(null);
		
		
		//文字
		if(this.font!=null){
			g2d.setFont(font);
		}
		if(this.fontColor!=null){
			g2d.setColor(fontColor);
		}
		FontMetrics fm=g2d.getFontMetrics(g2d.getFont());
		int fontWidth=fm.stringWidth(this.label);
		int fontHeight=fm.getAscent();
		g2d.drawString(this.label, x+(this.width-fontWidth)/2, y+this.height/2+fontHeight/2);
		
		g2d.setRenderingHints(rh);
	}
	
	/**
	 * 渐变渲染初始化
	 * @author Leijun
	 */
	private void intiParint(){
		gPaint=new GradientPaint(x,y,fromCr,x,y+height,toCr);
		gPaint2=new GradientPaint(x+this.borderWidth,y+this.borderWidth,fromCr2,x+this.borderWidth,y+this.height-this.borderWidth,toCr2);
		gPaint3=new GradientPaint(x,y,toCr,x,y+height,fromCr);
		gPaint4=new GradientPaint(x+this.borderWidth,y+this.borderWidth,toCr2,x+this.borderWidth,y+this.height-this.borderWidth,fromCr2);
	}

	public void setArcRadius(int arcRadius) {
		this.arcRadius = arcRadius;
		intiParint();
	}

	public void setFromCr(Color fromCr) {
		this.fromCr = fromCr;
		intiParint();
	}

	public void setToCr(Color toCr) {
		this.toCr = toCr;
		intiParint();
	}

	public void setBorderWidth(int borderWidth) {
		this.borderWidth = borderWidth;
		intiParint();
	}

	public void setMouseDownFontColor(Color mouseDownFontColor) {
		this.mouseDownFontColor = mouseDownFontColor;
	}
	
	@Override
	public void setX(int x) {
		this.x = x;
		intiParint();
	}
	
	@Override
	public void setY(int y) {
		this.y = y;
		intiParint();
	}
	
	@Override
	public void setWidth(int width) {
		this.width = width;
		intiParint();
	}
	
	@Override
	public void setHeight(int height) {
		this.height = height;
		intiParint();
	}

}
