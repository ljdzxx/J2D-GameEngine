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

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.j2d.framework.LSystem;

/**
 * 键帽   
 * @author Leijun
 * @version   
 *       1.0.0, Jan 30, 2013 2:29:23 PM
 */
public class Keys {
	private int x;
	private int y;
	//private int width;
	//private int height;
	private int minArea;//(x*y)
	private int maxArea;//(x+width * y+height)
	private BufferedImage normalImg;//一般图片
	private Font normalFont;
	private FontMetrics normalFM;
	private BufferedImage downImg;//被按下时的图片
	private int downW,downH;
	private BufferedImage tipImg;//被按下时在其上悬停的提示照片
	//private int tipW,tipH;
	private Font tipFont;
	private FontMetrics tipFM;
	private char KeyChar;//键值(char)
	private int KeyCode;//键值(int)
	private boolean active=false;//激活否
	//private String label=null;//显示在按键上的文字，如果为null则显示KeyChar
	
	
	public Keys(int x,int y, BufferedImage normalImg,BufferedImage downImg,
			BufferedImage tipImg, char KeyChar, int KeyCode, Font normalFont, Font tipFont){
		this.x=x;
		this.y=y;
		//this.width=width;
		//this.height=height;
		this.normalImg=normalImg;
		this.downImg=downImg;
		this.downH=downImg.getHeight();
		this.downW=downImg.getWidth();
		this.tipImg=tipImg;
		//this.tipH=tipImg.getHeight();
		//this.tipW=tipImg.getWidth();
		this.KeyChar=KeyChar;
		this.KeyCode=KeyCode;
		this.normalFont=normalFont;
		this.tipFont=tipFont;
		
		refreshArea();
	}
	
	public void draw(Graphics2D g2d){
		this.normalFM=g2d.getFontMetrics(this.normalFont);
		this.tipFM=g2d.getFontMetrics(this.tipFont);
		String lab;
		int labX,labBaseLine;
		if(active){//处于激活状态
			//mouse down时显示
			g2d.drawImage(downImg, x, y, null);
			
			if(null!=tipImg){
				int tipW=this.tipImg.getWidth();
				int tipH=this.tipImg.getHeight();
				//画tip悬浮图
				int tipX=x - (tipW-downW)/2;
				if(tipX<1){
					tipX=1;
				}else if(tipX+tipW>LSystem.WIDTH-2){
					tipX=LSystem.WIDTH-tipW-2;
				}
				int tipY=y - tipH - 10;
				g2d.drawImage(tipImg, tipX, tipY, null);
				
				//画Tip悬浮图上的文字
				if(KeyChar!=0xffff){//非功能键(功能键拟用图片，没有文字)
					g2d.setFont(tipFont);
					if(!LSystem.keyboard.isCapslock()){
						lab=String.valueOf(KeyChar).toLowerCase();
					}else{
						lab=String.valueOf(KeyChar).toUpperCase();
					}
					labX=tipX+(tipW-tipFM.stringWidth(lab))/2;
					labBaseLine=tipY+tipH/2+(tipFM.getAscent()-tipFM.getDescent())/2;
					g2d.drawString(lab, labX, labBaseLine);
				}
			}
		}else{
			//normal时显示
			g2d.drawImage(normalImg, x, y, null);
		}
		
		//画键帽上的文字
		if(KeyChar!=0xffff){//非功能键(功能键拟用图片，没有文字)
			g2d.setFont(normalFont);
			if(!LSystem.keyboard.isCapslock()){
				lab=String.valueOf(KeyChar).toLowerCase();
			}else{
				lab=String.valueOf(KeyChar).toUpperCase();
			}
			labX=x+(downW-normalFM.stringWidth(lab))/2;
			labBaseLine=y+downH/2+(normalFM.getAscent()-normalFM.getDescent())/2;
			g2d.drawString(lab, labX, labBaseLine);
		}
	}
	
	/**
	 * 计算最大、最小面积，鼠标点击的时候比较用
	 * @author Leijun
	 */
	private void refreshArea(){
		minArea=x*y;
		maxArea=(x+normalImg.getWidth())*(y+normalImg.getHeight());
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
		refreshArea();
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
		refreshArea();
	}
	public int getWidth() {
		return downW;
	}
	public int getHeight() {
		return downH;
	}
	public int getMinArea() {
		return minArea;
	}
	/*public void setMinArea(int minArea) {
		this.minArea = minArea;
	}*/
	public int getMaxArea() {
		return maxArea;
	}
	/*public void setMaxArea(int maxArea) {
		this.maxArea = maxArea;
	}*/
	public BufferedImage getNormalImg() {
		return normalImg;
	}
	public void setNormalImg(BufferedImage normalImg) {
		this.normalImg = normalImg;
		refreshArea();
	}
	public BufferedImage getDownImg() {
		return downImg;
	}
	public void setDownImg(BufferedImage downImg) {
		this.downImg = downImg;
		//refreshArea();
		this.downH=downImg.getHeight();
		this.downW=downImg.getWidth();
	}
	public char getKeyChar() {
		return KeyChar;
	}
	public void setKeyChar(char keyChar) {
		KeyChar = keyChar;
	}
	public int getKeyCode() {
		return KeyCode;
	}
	public void setKeyCode(int keyCode) {
		KeyCode = keyCode;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setNormalFont(Font normalFont) {
		this.normalFont = normalFont;
	}

	/*public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}*/
}
