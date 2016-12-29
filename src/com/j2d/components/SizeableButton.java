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
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;

import com.j2d.controlers.AbstractControler;
import com.j2d.framework.GraphicsUtils;
import com.j2d.framework.LSystem;

/**
 * 可自定义大小的风格按钮控件，支持enabled,disabled,mouse-down这三种不同状态的不同形态 
 * @author Leijun
 * @version   
 *       1.0.0, Jan 24, 2013 11:26:11 AM
 */
public class SizeableButton extends ComponentAdapter {
	private BufferedImage[] img_top_fix;
	private BufferedImage[] img_fill;
	private BufferedImage[] img_bottom_fix;
	private BufferedImage[] img_left_top;
	private BufferedImage[] img_right_top;
	private BufferedImage[] img_bottom_left;
	private BufferedImage[] img_bottom_right;
	private Color[] cl_side;
	private Color[] cl_label;
	private AlphaComposite alpha_half=AlphaComposite	//半透明
		.getInstance(AlphaComposite.SRC_OVER, 0.5f);
	private AlphaComposite alpha_none=AlphaComposite	//不透明
		.getInstance(AlphaComposite.SRC_OVER, 1.0f);
	//private int labelX,labelY;//文字的X,Y坐标
	private FontMetrics fm;
	//private Font font;
	//材质
	private TexturePaint tp;
	//private Color color=
	
	/**
	 * 获取指定的透明度
	 */
	private AlphaComposite getAlphaComposite(float alpha){
		return AlphaComposite	//半透明
		.getInstance(AlphaComposite.SRC_OVER, alpha);
	}
	
	public SizeableButton(int x,int y,int width,int height){
		super(x, y, width, height);
		//super.setFontColor(new Color(170,170,170));
		
		img_top_fix=new BufferedImage[3];
		img_fill=new BufferedImage[3];
		img_bottom_fix=new BufferedImage[3];
		img_left_top=new BufferedImage[3];
		img_right_top=new BufferedImage[3];
		img_bottom_left=new BufferedImage[3];
		img_bottom_right=new BufferedImage[3];
		cl_side=new Color[3];
		cl_label=new Color[3];
		
		//disabled
		img_top_fix[0]=LSystem.imsLoader.getImage("btn_top_fix_", 0);
		img_fill[0]=LSystem.imsLoader.getImage("btn_fill_", 0);
		img_bottom_fix[0]=LSystem.imsLoader.getImage("btn_bottom_fix_", 0);
		img_left_top[0]=LSystem.imsLoader.getImage("btn_left_top_", 0);
		img_right_top[0]=LSystem.imsLoader.getImage("btn_right_top_", 0);
		img_bottom_left[0]=LSystem.imsLoader.getImage("btn_bottom_left_", 0);
		img_bottom_right[0]=LSystem.imsLoader.getImage("btn_bottom_right_", 0);
		cl_side[0]=new Color(189,186,189);
		cl_label[0]=new Color(170,170,170);
		
		//enabled
		img_top_fix[1]=LSystem.imsLoader.getImage("btn_top_fix_", 1);
		img_fill[1]=LSystem.imsLoader.getImage("btn_fill_", 1);
		img_bottom_fix[1]=LSystem.imsLoader.getImage("btn_bottom_fix_", 1);
		img_left_top[1]=LSystem.imsLoader.getImage("btn_left_top_", 1);
		img_right_top[1]=LSystem.imsLoader.getImage("btn_right_top_", 1);
		img_bottom_left[1]=LSystem.imsLoader.getImage("btn_bottom_left_", 1);
		img_bottom_right[1]=LSystem.imsLoader.getImage("btn_bottom_right_", 1);
		cl_side[1]=new Color(247,162,33);		
		cl_label[1]=new Color(50,50,50);
		
		//mouse down
		img_top_fix[2]=LSystem.imsLoader.getImage("btn_top_fix_", 2);
		img_fill[2]=LSystem.imsLoader.getImage("btn_fill_", 2);
		img_bottom_fix[2]=LSystem.imsLoader.getImage("btn_bottom_fix_", 2);
		img_left_top[2]=LSystem.imsLoader.getImage("btn_left_top_", 2);
		img_right_top[2]=LSystem.imsLoader.getImage("btn_right_top_", 2);
		img_bottom_left[2]=LSystem.imsLoader.getImage("btn_bottom_left_", 2);
		img_bottom_right[2]=LSystem.imsLoader.getImage("btn_bottom_right_", 2);
		cl_side[2]=new Color(206,142,24);
		cl_label[2]=cl_label[1];
	}
	
	@Override
	public void draw(Graphics2D g2d){
		if(!this.enabled){
			styleDraw(g2d,0);
		}
		else if(this.active && AbstractControler.leftMouseDown){//enabled, active and mouse down
			styleDraw(g2d,2);
		}
		else{
			styleDraw(g2d,1);
		}
	}
	
	/**
	 * 显示带阴影效果的文本
	 * @param g2d
	 * @param str
	 * @param x
	 * @param y
	 * @param styleId-0:disabled,1:enabled,2:mousedown
	 * @author Leijun
	 */
	private void drawStyleString(Graphics2D g2d,String str,int x,int y, int styleId){
		GraphicsUtils.setAntialias(g2d, true);//抗锯齿
		GraphicsUtils.setRenderQuality(g2d, true);//高质量
		//下
		g2d.setComposite(alpha_half);//半透明
		g2d.setColor(Color.WHITE);//文字颜色
		g2d.drawString(str, x, y+1);
		//左
		g2d.setComposite(getAlphaComposite(0.2f));//20%不透明
		g2d.setColor(cl_label[styleId]);//文字颜色
		g2d.drawString(str, x-1, y);
		//左
		g2d.setComposite(getAlphaComposite(0.2f));//20%不透明
		g2d.setColor(cl_label[styleId]);//文字颜色
		g2d.drawString(str, x+1, y);
		//上
		g2d.setComposite(getAlphaComposite(0.1f));//10%不透明
		g2d.setColor(cl_label[styleId]);//文字颜色
		g2d.drawString(str, x, y-1);
		//本体
		g2d.setComposite(alpha_none);//不透明
		g2d.setColor(cl_label[styleId]);//文字颜色
		g2d.drawString(str, x, y);
		//g2d.setComposite(alpha_none);//不透明
	}
	
	/**
	 * 为激活状态绘制
	 * @param g2d
	 * @author Leijun
	 */
	private void styleDraw(Graphics2D g2d, int styleId){
		RenderingHints rh=g2d.getRenderingHints();
		GraphicsUtils.setAntialias(g2d, false);//不防锯齿
		GraphicsUtils.setRenderQuality(g2d, false);//速度优先
		
		Stroke sk=g2d.getStroke();//设置画笔
		Stroke stroke=new BasicStroke();//默认画笔
		g2d.setStroke(stroke);
		
		Composite comp=g2d.getComposite();
		g2d.setComposite(alpha_none);//设置不透明
		
		//顶部图案拉伸---------------------------------------------------------------
		//素材填充区域
		Rectangle r=new Rectangle(this.x+img_left_top[styleId].getWidth(), this.y, 
									img_top_fix[styleId].getWidth(), this.img_top_fix[styleId].getHeight());
		//创建素材填充
		this.tp=new TexturePaint(img_top_fix[styleId],r);
		//用图片素材渲染顶部背景
		g2d.setPaint(this.tp);
		g2d.fillRect(super.x+img_left_top[styleId].getWidth(), super.y, 
						super.width-img_left_top[styleId].getWidth()-img_right_top[styleId].getWidth(), img_top_fix[styleId].getHeight());
		g2d.setPaint(null);
		
		//左、右边1像素描边-------------------------------------------------------
		g2d.setColor(cl_side[styleId]);
		g2d.drawLine(this.x, this.y+img_left_top[styleId].getHeight(), this.x, 
						this.y+this.height-img_bottom_left[styleId].getHeight()-1);
		g2d.drawLine(this.x+this.width-1, this.y+img_right_top[styleId].getHeight(), 
						this.x+this.width-1, this.y+this.height-img_bottom_right[styleId].getHeight()-1);
		//填充图案------------------------------------------------------
		g2d.drawImage(img_fill[styleId], this.x+1, this.y+img_left_top[styleId].getHeight(), 
						this.width-2, this.height-img_left_top[styleId].getHeight()-img_bottom_left[styleId].getHeight(), null);
		//底部图案拉伸-------------------------------------------------------------------
		g2d.drawImage(img_bottom_fix[styleId], this.x+img_bottom_left[styleId].getWidth(), 
						this.y+this.height-img_bottom_left[styleId].getHeight(), 
							super.width-img_bottom_left[styleId].getWidth()-img_bottom_right[styleId].getWidth(), 
								img_bottom_fix[styleId].getHeight(), null);
		//4个角贴图----------------------------------------------------------------
		g2d.drawImage(img_left_top[styleId], this.x, this.y, null);
		g2d.drawImage(img_bottom_left[styleId], this.x, this.y+this.height-img_bottom_left[styleId].getHeight(), null);
		g2d.drawImage(img_bottom_right[styleId], this.x+this.width-img_bottom_right[styleId].getWidth(), 
						this.y+this.height-img_bottom_right[styleId].getHeight(), null);
		g2d.drawImage(img_right_top[styleId], this.x+this.width-img_right_top[styleId].getWidth(), this.y, null);
		//底部1像素半透明-------------------------------------------------------
		g2d.setComposite(alpha_half);
		g2d.setColor(Color.WHITE);
		g2d.drawLine(this.x+img_bottom_left[styleId].getWidth(), this.y+this.height-1, 
						this.x+this.width-img_bottom_right[styleId].getWidth()-1, this.y+this.height-1);
		g2d.setComposite(alpha_none);
		//显示文字
		if((this.label!=null)&&(!this.label.equals("")))
		{
			if(this.font!=null){
				g2d.setFont(font);
			}else{
				font=g2d.getFont();
			}
			this.fm=g2d.getFontMetrics(this.font);
			int fontH=this.fm.getAscent();//+this.fm.getDescent();
			int fontW=this.fm.stringWidth(this.label);
			drawStyleString(g2d, this.label, this.x+(this.width-fontW)/2, this.y+this.height/2+fontH/2, styleId);
			//g2d.drawString(this.label, this.x+(this.width-fontW)/2, this.y+this.height/2+fontH/2);//中文字符高度有修正(-3)
		}
		//g2d.translate(100, 100);
		//还原环境设置
		g2d.setComposite(comp);
		g2d.setStroke(sk);
		g2d.setRenderingHints(rh);
	}

	/*public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}*/

	/*public FontMetrics getFm() {
		return fm;
	}

	public void setFm(FontMetrics fm) {
		this.fm = fm;
	}*/
	
	/*@Override
	public void setLabel(String label) {
		super.setLabel(label);
		FontMetrics fm=g2d.getFontMetrics();
	}*/
}
