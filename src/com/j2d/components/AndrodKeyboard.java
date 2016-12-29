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
 * 仿Androd风格键盘   
 * @author Leijun
 * @version   
 *       1.0.0, Jan 30, 2013 2:01:34 PM
 */
public class AndrodKeyboard extends ComponentAdapter {
	private boolean capslock=false;//大写锁定
	private Keys[] keys=new Keys[41];//41个按键
	private BufferedImage Img,downImg,numImg,numDownImg,tipImg;//,Img_94,downImg_94;//一般键、符号键
	private BufferedImage clImg,clDownImg;//大小写锁定键 ,clTipImg
	private BufferedImage delImg,delDownImg,delImg2,delDownImg2,delTipImg;//删除键
	private BufferedImage enterImg,enterDownImg;//回车键 ,enterTipImg
	private ComponentAdapter comp;//所属"输入框"等控件
	private int lastActiveKeyIndex=-1;//上一个被激活的按键的序号
	private Font normalFont=new Font("Arial",Font.BOLD,22);
	private Font normalNumFont=new Font("Arial",Font.BOLD,40);
	private Font tipFont=new Font("Arial",Font.BOLD,44);
	private static final int LEFT_SPACE=5;
	private static final int H_SPACE=2;
	private static final int V_SPACE=5;
	private static final Color c_top_line=new Color(40,40,40);
	private boolean onlyNumber=false;//是否仅显示数字
	
	
	public AndrodKeyboard(int x,int y){
		super(x, y, LSystem.WIDTH, 160);
		Img=LSystem.imsLoader.getImage("key");
		downImg=LSystem.imsLoader.getImage("key_down");
		numImg=LSystem.imsLoader.getImage("key2");
		numDownImg=LSystem.imsLoader.getImage("key_down2");
		
		tipImg=LSystem.imsLoader.getImage("key_tip");
		//del
		delImg=LSystem.imsLoader.getImage("key_del");
		delDownImg=LSystem.imsLoader.getImage("key_del_down");
		delImg2=LSystem.imsLoader.getImage("key_del2");
		delDownImg2=LSystem.imsLoader.getImage("key_del_down2");
		
		delTipImg=LSystem.imsLoader.getImage("key_del_tip");
		//Caps lock
		clImg=LSystem.imsLoader.getImage("key_cl");
		clDownImg=LSystem.imsLoader.getImage("key_cl_down");
		
		//Enter
		enterImg=LSystem.imsLoader.getImage("key_enter");
		enterDownImg=LSystem.imsLoader.getImage("key_enter_down");
		
		int imgW=Img.getWidth();
		int imgH=Img.getHeight();
		
		//数字1-9
		for(int i=0;i<=8;i++){
			keys[i]=new Keys(x+LEFT_SPACE+i*(imgW+H_SPACE), y+V_SPACE,Img,downImg,tipImg,(char)(0x31+i),0x31+i,normalFont,tipFont);
		}
		//数字0
		keys[9]=new Keys(x+LEFT_SPACE+9*(imgW+H_SPACE), y+V_SPACE,Img,downImg,tipImg,(char)(0x30),0x30,normalFont,tipFont);
		
		//Backspace键
		keys[10]=new Keys(x+LEFT_SPACE+10*(imgW+H_SPACE), y+V_SPACE,delImg,delDownImg,delTipImg,(char)0xffff,KeyEvent.VK_BACK_SPACE,normalFont,tipFont);
		
		if(!isOnlyNumber()){
			//QWERTYUIOP
			char[] chars=new char[]{'Q','W','E','R','T','Y','U','I','O','P'};
			for(int i=0;i<chars.length;i++){
				keys[11+i]=new Keys(x+LEFT_SPACE+imgW/2+H_SPACE+i*(imgW+H_SPACE), y+V_SPACE+(imgH+V_SPACE),Img,downImg,tipImg,chars[i],(int)chars[i],normalFont,tipFont);
			}
			
			//#
			keys[21]=new Keys(x+LEFT_SPACE, y+V_SPACE+2*(imgH+V_SPACE),Img,downImg,tipImg,'#',KeyEvent.VK_3,normalFont,tipFont);
			
			//ASDFGHJKL
			chars=new char[]{'A','S','D','F','G','H','J','K','L'};
			for(int i=0;i<chars.length;i++){
				keys[22+i]=new Keys(x+LEFT_SPACE+(i+1)*(imgW+H_SPACE), y+V_SPACE+2*(imgH+V_SPACE),Img,downImg,tipImg,chars[i],(int)chars[i],normalFont,tipFont);
			}	
			
			//*
			keys[31]=new Keys(x+LEFT_SPACE+(chars.length+1)*(imgW+H_SPACE), y+V_SPACE+2*(imgH+V_SPACE),Img,downImg,tipImg,'*',KeyEvent.VK_8,normalFont,tipFont);
			
			//Caps Lock
			keys[32]=new Keys(x+LEFT_SPACE, y+V_SPACE+3*(imgH+V_SPACE),clImg,clDownImg,null,(char)0xffff,KeyEvent.VK_CAPS_LOCK,normalFont,tipFont);
			
			//ZXCVBNM
			chars=new char[]{'Z','X','C','V','B','N','M'};
			for(int i=0;i<chars.length;i++){
				keys[33+i]=new Keys(x+LEFT_SPACE+(i+1)*(imgW+H_SPACE)+45, y+V_SPACE+3*(imgH+V_SPACE),Img,downImg,tipImg,chars[i],(int)chars[i],normalFont,tipFont);
			}
			
			//Enter
			keys[40]=new Keys(x+LEFT_SPACE+(chars.length+1)*(imgW+H_SPACE)+45, y+V_SPACE+3*(imgH+V_SPACE),enterImg,enterDownImg,null,(char)0xffff,KeyEvent.VK_ENTER,normalFont,tipFont);
		}
		
		setVisable(false);//默认不可视
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
			if(isOnlyNumber())keyCount=11;
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
			if( (kCode==KeyEvent.VK_CAPS_LOCK) && (kChar==0xffff) ){
				this.capslock=!this.capslock;
				return;
			}
			
			if((lastActiveKeyIndex>=0)&&(comp.getEvent()!=null)){
				if( !capslock && (kChar>=65) && (kChar<=90) ){//小写
					kChar+=32;
				}
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
			if(comp.getEvent()!=null){
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
		g2d.setColor(Color.BLACK);
		g2d.fillRect(x, y, width, height);
		//顶部画一条灰线
		g2d.setColor(c_top_line);
		g2d.drawLine(x, y+1, x+width, y+1);
		
		
		//画键帽
		GraphicsUtils.setRenderingHints(g2d);//防锯齿，高质量渲染
		g2d.setColor(Color.WHITE);
		int keyCount=keys.length;
		if(isOnlyNumber())keyCount=11;
		for(int i=0;i<keyCount;i++){//
			keys[i].draw(g2d);
		}
		g2d.setRenderingHints(rh);
	}

	public boolean isCapslock() {
		return capslock;
	}

	public void setCapslock(boolean capslock) {
		this.capslock = capslock;
	}

	public ComponentAdapter getComp() {
		return comp;
	}

	public void setComp(ComponentAdapter comp) {
		this.comp = comp;
	}

	public boolean isOnlyNumber() {
		return onlyNumber;
	}

	public void setOnlyNumber(boolean onlyNumber) {
		this.onlyNumber = onlyNumber;
		
		BufferedImage keyImg,downKeyImg;
		Font font;
		if(!onlyNumber){
			font=normalFont;
			keyImg=Img;
			downKeyImg=downImg;
			height=160;
			
			keys[10].setNormalImg(delImg);
			keys[10].setDownImg(delDownImg);
		}else{
			font=normalNumFont;
			keyImg=numImg;
			downKeyImg=numDownImg;
			height=2*V_SPACE+keyImg.getHeight();

			keys[10].setNormalImg(delImg2);
			keys[10].setDownImg(delDownImg2);
		}
		y=LSystem.HEIGHT-height-60;
		keys[10].setY(y+V_SPACE);
		
		for(int i=0;i<10;i++){
			keys[i].setY(y+V_SPACE);
			keys[i].setNormalFont(font);
			keys[i].setNormalImg(keyImg);
			keys[i].setDownImg(downKeyImg);
		}
	}

}
