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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;

import com.j2d.framework.GraphicsUtils;
import com.j2d.framework.LSystem;

/**
 * Androd风格滑动条
 * This class is used for ...   
 * @author Leijun
 * @version   
 *       1.0.0, Mar 6, 2013 5:53:54 PM
 */
public class AndrodGlideBar extends ComponentAdapter {
	private int innerLineHeight,outerLineHeight;
	private int smallRadius,largeRadius;
	//private Color largeOvalColor;
	private int position;
	private Stroke stroke_inner,stroke_outer;
	private Color innerLineColor,outerLineColor;
	
	//线长与线端通过计算得到，不是由外界定义，也不能直接获取这2个值
	private int lineWidth,lineX;

	/**
	 * 初始化
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param percent-进度百分比
	 */
	public AndrodGlideBar(int x,int y, int width, int height, float percent){
		super(x, y, width, height);
		innerLineHeight=1;
		outerLineHeight=3;
		setSmallRadius(outerLineHeight);
		setPercent(percent);
		innerLineColor=Color.gray;
		outerLineColor=new Color(51,185,235);
		stroke_inner=new BasicStroke(innerLineHeight,BasicStroke.CAP_BUTT,BasicStroke.JOIN_ROUND);
		stroke_outer=new BasicStroke(outerLineHeight,BasicStroke.CAP_BUTT,BasicStroke.JOIN_ROUND);
		
	}
	
	@Override
	public void draw(Graphics2D g2d) {
		GraphicsUtils.setRenderingHints(g2d);
		int lineY=y+height/2;
		//内线
		g2d.setColor(innerLineColor);
		g2d.setStroke(stroke_inner);
		g2d.drawLine(getLineX(), lineY, getLineX()+getLineWidth(), lineY);
		
		//终点大球
		g2d.setColor(outerLineColor);
		g2d.setComposite(LSystem.alpha_30);
		g2d.fillOval(getLineX()+position-largeRadius, lineY-largeRadius, 2*largeRadius, 2*largeRadius);		
		g2d.setComposite(LSystem.alpha_100);
		
		//外线
		//g2d.setColor(outerLineColor);
		g2d.setStroke(stroke_outer);
		g2d.drawLine(getLineX(), lineY, getLineX()+position, lineY);
		//终点小球
		//g2d.setColor(outerLineColor);
		g2d.fillOval(getLineX()+position-smallRadius, lineY-smallRadius, 2*smallRadius, 2*smallRadius);

		GraphicsUtils.restoreStroke(g2d);
	}

	public int getInnerLineHeight() {
		return innerLineHeight;
	}

	public void setInnerLineHeight(int innerLineHeight) {
		this.innerLineHeight = innerLineHeight;
	}

	public int getOuterLineHeight() {
		return outerLineHeight;
	}

	public void setOuterLineHeight(int outerLineHeight) {
		this.outerLineHeight = outerLineHeight;
	}

	public void setSmallRadius(int smallRadius) {
		this.smallRadius = smallRadius;
		this.largeRadius = 4*smallRadius;
	}

	public void setLargeRadius(int largeRadius) {
		this.largeRadius = largeRadius;
	}

	private int getLineWidth() {
		lineWidth=width-2*largeRadius;
		return lineWidth;
	}

	private int getLineX() {
		lineX=x+largeRadius;
		return lineX;
	}

	/*public int getPosition() {
		return position;
	}*/

	public void setPercent(float percent) {
		this.position = (int)(getLineWidth()*percent);
	}

	public void setInnerLineColor(Color innerLineColor) {
		this.innerLineColor = innerLineColor;
	}

	public void setOuterLineColor(Color outerLineColor) {
		this.outerLineColor = outerLineColor;
	}
	
	@Override
	public void mouseDragged(MouseEvent e){
		System.out.println(e.getX());
	}	

}
