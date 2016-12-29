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
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

//import com.j2d.controlers.AbstractControler;

/**
 * 按钮、输入框等控件基类   
 * @author Leijun
 * @version   
 *       1.0.0, Jan 18, 2013 11:37:35 AM
 */
public abstract class ComponentAdapter implements IComponentEvent {
	protected boolean visable=true;//是否可视
	//protected boolean focus=false;//是否聚焦(鼠标悬停)
	protected boolean active=false;//是否激活(被鼠标点击)
	protected boolean enabled=true;//是否可用(可否被点击)
	//protected boolean leftMousePressed=false;//鼠标左键是否按下
	protected String label;//标签
	protected Font font;//字体
	protected Color fontColor;//字体颜色
	protected int width;//宽度
	protected int height;//高度
	protected int x;//X坐标
	protected int y;//Y坐标
	public int intField1;//备用的int域1
	private ISimpleEvent event;//鼠标、键盘事件
	
	public ComponentAdapter(int x,int y,int width,int height){
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
	}
	
	/**
	 * 返回控件的是否聚焦
	 * @return
	 * @author Leijun
	 */
	/*public boolean isFocus() {
		if(!this.visable){
			this.focus=false;
		}else{
			if (AbstractControler.mouse.x > this.x
					&& AbstractControler.mouse.x < this.x + this.width
					&& AbstractControler.mouse.y > this.y 
					&& AbstractControler.mouse.y < this.y + this.height) 
			{
				this.focus = true;
			} else {
				this.focus = false;
			}
		}
		//if(!this.focus) this.leftMousePressed=false;//移除区域范围之外了，重置鼠标按下状态
		return this.focus;
	}*/
	
	/**
	 * 鼠标左键是否点击了
	 * @return
	 * @author Leijun
	 */
	/*public boolean isLeftMouseClicked() {
		if(!visable){//按钮不可见
			return false;
		}
		if(AbstractControler.leftPressed){//左键按下了还没松开
			return false;
		}
		
		if((AbstractControler.leftPressPoint!=null)&&(AbstractControler.leftReleasePoint!=null)){//按下点 和 释放点都不为null
			try{
				if ( AbstractControler.leftPressPoint.x > this.x && AbstractControler.leftReleasePoint.x > this.x
						&& AbstractControler.leftPressPoint.x < this.x + this.width && AbstractControler.leftReleasePoint.x < this.x + this.width
						&& AbstractControler.leftPressPoint.y > this.y && AbstractControler.leftReleasePoint.y > this.y
						&& AbstractControler.leftPressPoint.y < this.y + this.height && AbstractControler.leftReleasePoint.y < this.y + this.height
					)
				{
					//本次处理完了之后重置按下和释放状态
					AbstractControler.leftPressPoint=null;
					AbstractControler.leftReleasePoint=null;
					return true;
				} else {
					return false;
				}
			}catch (Exception e) {
				// TODO: handle exception
				return false;
			}
		}
		else{
			return false;
		}
		
		//return this.leftMousePressed;
	}*/

	/**
	 * 是否可视
	 */
	public boolean isVisable() {
		return visable;
	}

	public void setVisable(boolean visable) {
		this.visable = visable;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public Color getFontColor() {
		return fontColor;
	}

	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	/**
	 * 检查是否激活状态，并返回值
	 * @return
	 * @author Leijun
	 */
	public boolean isActive() {
		/*if(!this.active){
			if(this.isLeftMouseClicked()){
				this.active=true;
			}
		}*/
		return this.active;
	}
	
	/**
	 * 返回是否激活状态
	 * @return
	 * @author Leijun
	 */
	/*public boolean getActive(){
		return this.active;
	}*/
	
	/**
	 * 更新激活状态
	 * @author Leijun
	 */
	/*public void updateActive(){
		Point p=AbstractControler.leftPressPoint;
		if(p!=null){//左键被点击
			try{
				if((p.x<this.x) || (p.x>this.x+this.width) || (p.y<this.y) || (p.y>this.y+this.height)){
					this.active=false;
				}
			}
			catch (Exception e) {
				// TODO: handle exception
			}
			
		}
	}*/

	public void setActive(boolean active) {
		this.active = active;
	}
	
	/**
	 * 鼠标左键按下
	 */
	public void leftMousePressed(Point p){
	}
	
	/**
	 * 鼠标左键被释放
	 */
	public void leftMouseReleased(Point p){
	}
	
	/**
	 * 有按键被按下
	 */
	public void keyPressed(int keyCode, char keyChar){
	}
	
	/**
	 * 鼠标拖拽
	 * @param e
	 * @author Leijun
	 */
	public void mouseDragged(MouseEvent e) {
	}

	public ISimpleEvent getEvent() {
		return event;
	}

	public void setEvent(ISimpleEvent event) {
		this.event = event;
	}
	
	/**
	 * 绘制方法
	 * @param g2d
	 * @author Leijun
	 */
	public abstract void draw(Graphics2D g2d);

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getIntField1() {
		return intField1;
	}

	public void setIntField1(int intField1) {
		this.intField1 = intField1;
	}
}
