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

package com.j2d.controlers;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;

import com.j2d.framework.GraphicsUtils;
import com.j2d.framework.LSystem;

/**
 * 带操作步骤的场景的基类
 * @author Leijun
 * @version   
 *       1.0.0, Jan 22, 2013 3:13:04 PM
 */
public abstract class StepControler extends BaseControler {
	private String[] steps;//步骤
	private int currentStep=0;//当前步骤0 - steps.length()
	private TexturePaint tp;
	private BufferedImage bg;
	private BufferedImage[] lights;
	private int bgHeight;//背景图高
	private int lightWidth;//亮点的宽度
	private int lightHeight;//亮点的高度
	private static final int C_LEFT_SPACE=10;//首个亮点距离左边的距离
	private static final int C_TEXT_SPACE=5;//文字距离亮点的距离
	private int space=100;
	private static final Font font=new Font("黑体",Font.PLAIN,24);
	private FontMetrics fm;
	private static final Color finishedColor=new Color(57,166,231);//已经完成的和正在进行的步骤的文字颜色
	private static final Color fartherColor=Color.gray;
	private volatile int count=0;//计数器
	
	public int getBgHeight() {
		return bgHeight;
	}

	public void setBgHeight(int bgHeight) {
		this.bgHeight = bgHeight;
	}
	
	public StepControler(){}

	public StepControler(String[] steps){
		//亮点
		this.lights=new BufferedImage[5];
		this.lights[0]=LSystem.imsLoader.getImage("step_light_",0);
		this.lights[1]=LSystem.imsLoader.getImage("step_light_",1);
		this.lights[2]=LSystem.imsLoader.getImage("step_light_",2);
		this.lights[3]=LSystem.imsLoader.getImage("step_light_",3);
		this.lights[4]=LSystem.imsLoader.getImage("step_light_",4);
		this.lightWidth=this.lights[0].getWidth();
		this.lightHeight=this.lights[0].getHeight();
		//背景素材
		this.bg=LSystem.imsLoader.getImage("step_bg");
		setBgHeight(this.bg.getHeight()-4);//-4是因为该图片底部有4个像素是半透明的

		
		//步骤文字
		setSteps(steps);

		//素材填充区域
		Rectangle r=new Rectangle(super.x, super.y, bg.getWidth(), this.bg.getHeight());
		//创建素材填充
		this.tp=new TexturePaint(bg,r);//,TexturePaint.NEAREST_NEIGHBOR
	}
	
	/**
	 * 子类需要实现的绘图操作
	 * @author Leijun
	 */
	protected abstract void SubDraw2(Graphics2D g2d);
	
	@Override
	protected void SubDraw(Graphics2D g2d) {
		SubDraw2(g2d);//由子类实现的绘图
		
		if((steps==null)||(steps.length==0)) return;//没有操作步骤
		//用图片素材渲染顶部背景
		g2d.setPaint(this.tp);
		g2d.fillRect(super.x, super.y, super.width, this.bg.getHeight());
		g2d.setPaint(null);
		//绘制亮光和提示文字
		int imgX;//图片的X坐标位置
		this.fm=g2d.getFontMetrics(font);
		int textY=super.y+10+this.lightHeight/2+fm.getAscent()/2;//文字的基线位置 +fm.getDescent()
		textY-=1;//中文位置修正
		for(int i=0;i<getSteps().length;i++){
			imgX=super.x+C_LEFT_SPACE+i*(this.lightWidth+this.space);
			if(i<this.currentStep){//已完成的步骤
				g2d.drawImage(this.lights[0], imgX, super.y+10, null);
				g2d.setColor(finishedColor);
			}
			else if(i>this.currentStep){//未完成的步骤
				g2d.drawImage(this.lights[4], imgX, super.y+10, null);
				g2d.setColor(fartherColor);
			}
			else{//正在进行的步骤(图片动画)
				if(this.count<=15)
					g2d.drawImage(this.lights[0], imgX, super.y+10, null);
				else if(this.count<=30)
					g2d.drawImage(this.lights[1], imgX, super.y+10, null);
				else if(this.count<=45)
					g2d.drawImage(this.lights[2], imgX, super.y+10, null);
				else if(this.count<=60)
					g2d.drawImage(this.lights[3], imgX, super.y+10, null);
				else this.count=0;//归0
				
				g2d.setColor(finishedColor);
			}
			g2d.setFont(font);
			GraphicsUtils.setAntialias(g2d,true);//抗锯齿
			g2d.drawString(getSteps()[i], imgX+this.lightWidth+C_TEXT_SPACE, textY);
			GraphicsUtils.setAntialias(g2d,false);
		}
	}
	
	@Override
	public void next(){
		super.next();
		this.count++;
	}

	public String[] getSteps() {
		return steps;
	}

	public void setSteps(String[] steps) {
		this.steps = steps;
		if((steps==null)||(steps.length==0))return;
		space=(super.width-2*C_LEFT_SPACE-steps.length*this.lightWidth)/steps.length;
	}

	public int getCurrentStep() {
		return currentStep;
	}

	/**
	 * 设置当前进行的步骤，从0开始算起，即0为第1步...
	 * @param currentStep
	 * @author Leijun
	 */
	public void setCurrentStep(int currentStep) {
		this.currentStep = currentStep;
	}
	
	/*@Override
	public boolean update(Object o, Object arg){
		return super.update(o, arg);
	}*/

}
