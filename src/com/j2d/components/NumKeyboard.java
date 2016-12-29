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
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import com.j2d.framework.GraphicsUtils;
import com.j2d.framework.LSystem;

/**
 * 九宫格键盘   
 * @author Leijun
 * @version   
 *       1.0.0, May 25, 2013 2:01:34 PM
 */
public class NumKeyboard extends ComponentAdapter {
	private Keys[] keys=new Keys[12];//12个按钮
	private BufferedImage Img,downImg;//一般图片
	private BufferedImage delImg,delDownImg,clearImg,clearDownImg;//删除、清空图片
	private ComponentAdapter comp;//所属"输入框"等控件	//ComponentAdapter
	private int lastActiveKeyIndex=-1;//上一个被激活的按键的序号
	private Font normalFont=new Font("Arial",Font.BOLD,40);
	private Font tipFont=new Font("Arial",Font.BOLD,44);//为了共用Keys，此域保留
	private static final int LEFT_SPACE=10;//按钮距离左边的距离
	private static final int TOP_SPACE=10;//按钮距离顶部的距离
	private static final int H_SPACE=5;//水平间距
	private static final int V_SPACE=5;//垂直间距
	//4个角弧的半径
	private static final int arcRadius=5;
	//private static final Color c_top_line=new Color(40,40,40);
	
	
	public NumKeyboard(int x,int y){
		super(x, y, 0, 0);
		
		Img=LSystem.imsLoader.getImage("key2");
		downImg=LSystem.imsLoader.getImage("key_down2");

		//del
		delImg=LSystem.imsLoader.getImage("key_del2");
		delDownImg=LSystem.imsLoader.getImage("key_del_down2");
		//clear
		clearImg=LSystem.imsLoader.getImage("key_del2");
		clearDownImg=LSystem.imsLoader.getImage("key_del_down2");
		
		int imgW=Img.getWidth();
		int imgH=Img.getHeight();
		
		this.width  = 2*LEFT_SPACE+3*imgW+2*H_SPACE;
		this.height = 2*TOP_SPACE+4*imgH+3*V_SPACE;
		
		int j=0;
		//数字7-9
		for(int i=7;i<=9;i++){
			keys[j]=new Keys(x+LEFT_SPACE+((i-1)%3)*(imgW+H_SPACE), y+TOP_SPACE,Img,downImg,null,(char)(0x30+i),0x30+i,normalFont,tipFont);
			j++;
		}
		//数字4-6
		for(int i=4;i<=6;i++){
			keys[j]=new Keys(x+LEFT_SPACE+((i-1)%3)*(imgW+H_SPACE), y+TOP_SPACE+(imgH+V_SPACE),Img,downImg,null,(char)(0x30+i),0x30+i,normalFont,tipFont);
			j++;
		}
		//数字1-3
		for(int i=1;i<=3;i++){
			keys[j]=new Keys(x+LEFT_SPACE+((i-1)%3)*(imgW+H_SPACE), y+TOP_SPACE+2*(imgH+V_SPACE),Img,downImg,null,(char)(0x30+i),0x30+i,normalFont,tipFont);
			j++;
		}
		//Backspace
		keys[j]=new Keys(x+LEFT_SPACE, y+TOP_SPACE+3*(imgH+V_SPACE),delImg,delDownImg,null,(char)0xffff,KeyEvent.VK_BACK_SPACE,normalFont,tipFont);
		j++;
		//数字0
		keys[j]=new Keys(x+LEFT_SPACE+(imgW+H_SPACE), y+TOP_SPACE+3*(imgH+V_SPACE),Img,downImg,null,(char)(0x30),0x30,normalFont,tipFont);
		j++;
		//clear
		keys[j]=new Keys(x+LEFT_SPACE+2*(imgW+H_SPACE), y+TOP_SPACE+3*(imgH+V_SPACE),clearImg,clearDownImg,null,(char)0xffff,KeyEvent.VK_CONTROL,normalFont,tipFont);//Ctrl键在edit中被设定为清空功能
		
		setVisable(true);//默认可见
	}
	
	@Override
	public void leftMousePressed(Point p){
		try {			
			//int keyIndex=-1;
			if(lastActiveKeyIndex>=0){//考虑触屏上有多个点在点击?
				keys[lastActiveKeyIndex].setActive(false);
			}
			int area=p.x*p.y;
			int keyCount=keys.length;
			boolean isFound=false;
			for(int i=0;i<keyCount;i++){
				if( (area>=keys[i].getMinArea()) && (area<=keys[i].getMaxArea()) ){//初步筛选
					if( (p.x>=keys[i].getX()) && (p.x<=keys[i].getX()+keys[i].getWidth()) &&
						(p.y>=keys[i].getY()) && (p.y<=keys[i].getY()+keys[i].getHeight()) )
					{
						lastActiveKeyIndex=i;
						keys[i].setActive(true);
						isFound=true;
						break;
					}
				}
			}
			if(!isFound){
				if(lastActiveKeyIndex>=0){
					keys[lastActiveKeyIndex].setActive(false);
				}
				lastActiveKeyIndex=-1;
				return;
			}
			
			int kCode=keys[lastActiveKeyIndex].getKeyCode();
			char kChar=keys[lastActiveKeyIndex].getKeyChar();
			//System.out.println(kCode+"|"+kChar);
			
			if((null!=comp)&&(lastActiveKeyIndex>=0)&&(comp.getEvent()!=null)){
				comp.getEvent().keyPressed(comp, kCode, kChar);//
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	@Override
	public void leftMouseReleased(Point p){
		if(lastActiveKeyIndex>=0){
			keys[lastActiveKeyIndex].setActive(false);
		}
	}
	
	@Override
	public void keyPressed(int keyCode, char keyChar){
	//将软键盘上的键盘事件转嫁到被绑定的控件上
		try {
			if((null!=comp)&&(comp.getEvent()!=null)){
				comp.getEvent().keyPressed(comp, keyCode, keyChar);//
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public void draw(Graphics2D g2d) {
		if (!this.visable) return;
		RenderingHints rh=g2d.getRenderingHints();
		
		GraphicsUtils.setAlpha(g2d, 1.0f);//设置不透明
		//画背景(圆角矩形)
		g2d.setColor(Color.BLACK);
		//g2d.fillRect(x, y, width, height);
		g2d.fillRoundRect(x, y, width, height, arcRadius*2, arcRadius*2);
		
		//顶部画一条灰线
		//g2d.setColor(c_top_line);
		//g2d.drawLine(x, y+1, x+width, y+1);
		
		
		//画键帽
		GraphicsUtils.setRenderingHints(g2d);//防锯齿，高质量渲染
		g2d.setColor(Color.WHITE);
		int keyCount=keys.length;
		for(int i=0;i<keyCount;i++){//
			keys[i].draw(g2d);
		}
		g2d.setRenderingHints(rh);
	}

	public ComponentAdapter getComp() {
		return comp;
	}

	public void setComp(ComponentAdapter comp) {
		this.comp = comp;
	}
}
