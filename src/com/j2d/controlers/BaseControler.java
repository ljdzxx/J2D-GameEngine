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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.j2d.components.ComponentAdapter;
import com.j2d.components.ImageButton;
import com.j2d.components.SimpleEventAdapter;
import com.j2d.framework.GraphicsUtils;
import com.j2d.framework.LSystem;

/**
 * 
 * 实现了顶部和底部风格绘制的场景基类   
 * @author Leijun
 * @version   
 *       1.0.0, Jan 29, 2013 12:04:37 PM
 */
public abstract class BaseControler extends AbstractControler implements INaviEvent {
	private static final long serialVersionUID = 1L;
	private ImageButton[] bottomBtns = new ImageButton[3];
	private List<ComponentAdapter> list = new ArrayList<ComponentAdapter>();
	private List<ComponentAdapter> kb_list = new ArrayList<ComponentAdapter>();
	//private int clickbuttonId = -1;
	private String clockString=String.format("%1$tH:%tM", new Date());//时钟
	//private int clockString=0;
	protected int x,y,width,height;//继承本类的子类应该在该区域范围内绘图
	protected static final Font labelFont=new Font("幼圆", Font.PLAIN, 20);
	protected static final Color C_GRAY=Color.gray;
	protected static final Color C_WHITE=Color.white;
	public static final Color COLOR_GRAY_231 = new Color(231,231,231);
	private boolean busy=false;//是否繁忙
	private String busyStr="Please wait...";
	private boolean isShowTips=false;//是否显示Tips
	private int tipMiSec;//Tips显示的毫秒数
	private String tipText;//Tips内容
	private final int tipW=LSystem.WIDTH*2/3;
	private final int tipH=100;
	private final int tipX=(LSystem.WIDTH-tipW)/2;
	private final int tipY=(LSystem.HEIGHT-tipH)/2;
	private Font tipFont=new Font("黑体",Font.PLAIN,24);
	private FontMetrics fontMtr;
	private Color c_gray_90=new Color(90,90,90);
	private int rotateAngle=0;//忙碌圈圈的旋转角度
	private BufferedImage busyImg,busyImg2;
	private BaseControler previousControler=null;
	private Font heiti_19=new Font("黑体",Font.PLAIN,19);

	
	class HomeEvent extends SimpleEventAdapter{
		private INaviEvent nav;
		
		@Override
		public void leftMouseReleased(ComponentAdapter c, Point p) {//
			super.leftMouseReleased(c, p);//
			if(nav!=null){
				nav.goHome();
			}
		}

		public INaviEvent getNav() {
			return nav;
		}

		public void setNav(INaviEvent nav) {
			this.nav = nav;
		}
	}
	
	class BackEvent extends SimpleEventAdapter{
		private INaviEvent nav;
		
		@Override
		public void leftMouseReleased(ComponentAdapter c, Point p) {//
			super.leftMouseReleased(c, p);
			if(nav!=null){
				nav.goBack();
			}
		}

		public INaviEvent getNav() {
			return nav;
		}

		public void setNav(INaviEvent nav) {
			this.nav = nav;
		}
	}
	
	/**
	 * 挂载软键盘(其实只是把软键盘纳入操作系统的鼠标、键盘等事件控制中)
	 * @author Leijun
	 */
	protected void AddKeyboard(){
		kb_list.add(LSystem.keyboard);
		if(compList.size()>0){
			compList.add(0, kb_list);
		}else{
			compList.add(kb_list);
		}
	}
	
	public BaseControler(){
		busyStr="请稍候...";//这里要根据语种来设置
		BufferedImage backBtn=LSystem.imsLoader.getImage("back_btn_",0);
		int space=(LSystem.WIDTH-backBtn.getWidth()*3)/(bottomBtns.length-1);
		ImageButton.initializeRows(bottomBtns, 0, LSystem.HEIGHT-backBtn.getHeight(), space, backBtn, LSystem.imsLoader.getImage("back_btn_",1),labelFont,C_WHITE);
		BackEvent backEvent=new BackEvent();
		backEvent.setNav(this);
		bottomBtns[0].setEvent(backEvent);
		list.add(bottomBtns[0]);
		
		//bottomBtns[1].setUnFocusImage(Constant.imsLoader.getImage("home_btn_",0));
		bottomBtns[1].setNormalImg(LSystem.imsLoader.getImage("home_btn_",0));
		bottomBtns[1].setMouseDownImg(LSystem.imsLoader.getImage("home_btn_",1));
		HomeEvent homeEvent=new HomeEvent();
		homeEvent.setNav(this);
		bottomBtns[1].setEvent(homeEvent);
		list.add(bottomBtns[1]);
		
		//bottomBtns[2].setUnFocusImage(Constant.imsLoader.getImage("menu_btn_",0));
		bottomBtns[2].setNormalImg(LSystem.imsLoader.getImage("menu_btn_",0));
		bottomBtns[2].setMouseDownImg(LSystem.imsLoader.getImage("menu_btn_",1));
		bottomBtns[2].setEvent(new SimpleEventAdapter(){
			@Override
			public void leftMousePressed(ComponentAdapter c, Point p) {//
				super.leftMousePressed(c, p);
			}
		});
		list.add(bottomBtns[2]);
		
		compList.add(list);
		
		//定义子类的绘图区域
		x=0;
		y=30;
		width=LSystem.WIDTH;		
		height=LSystem.HEIGHT-y-backBtn.getHeight();
		busyImg=LSystem.imsLoader.getImage("busy");
		

	}
	
	/**
	 * 显示正在忙碌
	 * @param g2d
	 * @author Leijun
	 */
	private void drawBusy(Graphics2D g2d){
		int w=653,h=94;//loading信息的宽、高
		RenderingHints rh=g2d.getRenderingHints();
		Composite comp=g2d.getComposite();
		g2d.setComposite(LSystem.alpha_60);
		g2d.setColor(Color.black);
		g2d.fillRect(x, y, width, height);
		
		//GraphicsUtils.setRenderingHints(g2d);//高质量、防锯齿绘制
		//GraphicsUtils.setRenderQuality(g2d, true);
		GraphicsUtils.setAntialias(g2d, true);//抗锯齿
		g2d.setColor(Color.black);
		g2d.setComposite(LSystem.alpha_05);
		g2d.drawRoundRect(x+(width-w)/2, y+(height-h)/2, w, h, 20, 20);//外1
		
		//AlphaComposite alpha_08=AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.08f);
		g2d.setComposite(LSystem.alpha_10);
		g2d.drawRoundRect(x+(width-w)/2+1, y+(height-h)/2+1, w-1*2, h-1*2, 2*(10-1), 2*(10-1));//外2
		
		//AlphaComposite alpha_11=AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.11f);
		g2d.setComposite(LSystem.alpha_15);
		g2d.drawRoundRect(x+(width-w)/2+2, y+(height-h)/2+2, w-2*2, h-2*2, 2*(10-2), 2*(10-2));//外3
		
		g2d.setComposite(LSystem.alpha_25);
		g2d.drawRoundRect(x+(width-w)/2+3, y+(height-h)/2+3, w-3*2, h-3*2, 2*(10-3), 2*(10-3));//外4
		
		//AlphaComposite alpha_40=AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);
		g2d.setComposite(LSystem.alpha_40);
		g2d.drawRoundRect(x+(width-w)/2+4, y+(height-h)/2+4, w-4*2, h-4*2, 2*(10-4), 2*(10-4));//外5
		
		//AlphaComposite alpha_65=AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f);
		g2d.setComposite(LSystem.alpha_60);
		g2d.drawRoundRect(x+(width-w)/2+5, y+(height-h)/2+5, w-5*2, h-5*2, 2*(10-5), 2*(10-5));//外6
		
		//AlphaComposite alpha_85=AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f);
		g2d.setComposite(LSystem.alpha_85);//内层透明度
		GraphicsUtils.setAntialias(g2d, false);//不抗锯齿
		//GraphicsUtils.setRenderQuality(g2d, false);
		g2d.fillRect(x+(width-w)/2+8, y+(height-h)/2+8, w-2*8, h-2*8);//
		
		//GraphicsUtils.setAntialias(g2d, true);
		//GraphicsUtils.setRenderQuality(g2d, true);
		g2d.setComposite(LSystem.alpha_100);//100%不透明
		
		GraphicsUtils.setAntialias(g2d, true);//抗锯齿
		GraphicsUtils.setRenderQuality(g2d, true);
		Stroke sk=g2d.getStroke();//设置画笔
		Stroke stroke=new BasicStroke(2,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
		g2d.setStroke(stroke);
		g2d.setColor(c_gray_90);
		g2d.drawRect(x+(width-w)/2+7, y+(height-h)/2+7, w-2*7, h-2*7);//Round	, 8, 8
		
		busyImg2=GraphicsUtils.rotateImage(busyImg,rotateAngle,false);
		g2d.drawImage(busyImg2, x+(width-w)/2+25, y+(height-h)/2+(h-busyImg.getHeight())/2, null);
		g2d.setFont(new Font("宋体",Font.PLAIN,18));
		g2d.setColor(Color.white);
		FontMetrics fm=g2d.getFontMetrics();
		GraphicsUtils.setRenderQuality(g2d, true);
		g2d.drawString(busyStr, x+(width-w)/2+25+50+25, y+(height-h)/2+h/2+(fm.getAscent()-fm.getDescent())/2);
		
		//还原环境设置
		g2d.setComposite(comp);
		g2d.setStroke(sk);
		g2d.setRenderingHints(rh);
	}
	
	/**
	 * 按钮绘制完成后，判断按钮是否按下采用轮询机制，轮询每个按钮是否为按下状态
	 * @param g2d
	 * @author Leijun
	 */
	private void drawBottomButtons(Graphics2D g2d) {//synchronized
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0,y+height,LSystem.WIDTH, LSystem.HEIGHT-(y+height));
		
		/*if (clickbuttonId == 0) {//返回键

			g2d.setColor(Color.black);
			g2d.drawString("你按了返回键", 200, 200);
		}
		else if (clickbuttonId == 1) {//Home键

			g2d.setColor(Color.black);
			g2d.drawString("你按了Home键", 200, 300);
		}
		else if (clickbuttonId == 2) {//菜单键(语言选择)

			g2d.setColor(Color.black);
			g2d.drawString("你按了菜单键", 200, 400);
		}*/
		
		for (int i = 0; i < list.size(); i++) {
			ImageButton button = ((ImageButton) list.get(i));
			button.draw(g2d);//, "宋体", 0, 9
		}
	}
	
	private void drawClock(Graphics2D g2d){
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0,0,LSystem.WIDTH, y);
		//GraphicsUtils.setRenderingHints(g2d);
		
		g2d.setColor(Color.white);
		GraphicsUtils.setAntialias(g2d,true);
		if(LSystem.PosId!=null){
			g2d.setFont(heiti_19);
			g2d.drawString("终端编号:"+LSystem.PosId, 5, 20);
		}
		g2d.setFont(GraphicsUtils.getFont("Dialog", 0, 20));
		g2d.drawString(clockString, LSystem.WIDTH-60, 20);//R="%tH:%tM"
		//System.out.println(clockString);
		GraphicsUtils.setAntialias(g2d,false);
	}
	
	@Override
	public void next(){
		//super.next();
		//System.out.println("update executed.");
		clockString=String.format("%1$tH:%tM", new Date());
		//System.out.println(clockString);
		
		/*for (int i = 0; i < list.size(); i++) {
			ImageButton button = ((ImageButton) list.get(i));
			button.isFocus();//更新按钮的聚焦状态
			int buttonId = button.getClickedId();
			if (buttonId != -1) {
				clickbuttonId = buttonId;
			}
		}*/
		
		if(isShowTips){
			tipMiSec -= (1000/LSystem.fps);
			if(tipMiSec<=0){
				isShowTips=false;
			}
		}
		
		if(isBusy()){//控制"忙碌圈圈"的旋转角度
			rotateAngle+=5;
			if(rotateAngle>=360){
				rotateAngle=0;
			}
		}
	}
	
	private void drawTips(Graphics2D g2d){
		g2d.setColor(Color.black);
		g2d.setComposite(LSystem.alpha_80);
		GraphicsUtils.setRenderingHints(g2d);
		GraphicsUtils.setAntialias(g2d, true);
		g2d.fillRect(tipX, tipY, tipW, tipH);
		g2d.setComposite(LSystem.alpha_100);
		g2d.setColor(Color.white);
		g2d.setFont(tipFont);
		fontMtr=g2d.getFontMetrics();
		int tipTextW=fontMtr.stringWidth(tipText);
		int tipTextH=fontMtr.getAscent()-fontMtr.getDescent();
		int tipTextX=tipX+(tipW-tipTextW)/2;
		int tipTextY=tipY+(tipH+tipTextH)/2;
		g2d.drawString(this.tipText, tipTextX, tipTextY);
	}
	
	/**
	 * 实现顶部时间和底部功能按钮的绘制等
	 */
	@Override
	public void draw(Graphics2D g2d) {
		//System.out.println("是否忙碌1111111:"+isBusy());
		//System.out.println("draw中的busyStr:"+getBusyStr());
		//打底
		g2d.setColor(COLOR_GRAY_231);
		g2d.fillRect(0, 0, LSystem.WIDTH, LSystem.HEIGHT);
		
		SubDraw(g2d);//子类的draw方法
		//System.out.println("是否忙碌2222222:"+isBusy());
		//System.out.println("draw中的busyStr:"+getBusyStr());
		drawClock(g2d);//顶部时钟
		//System.out.println("是否忙碌3333333:"+isBusy());
		//System.out.println("draw中的busyStr:"+getBusyStr());
		drawBottomButtons(g2d);//底部按钮
		//System.out.println("是否忙碌444444444:"+isBusy());
		//System.out.println("draw中的busyStr:"+getBusyStr());
		if(LSystem.keyboard.isVisable()){//是否显示键盘
			LSystem.keyboard.draw(g2d);
		}	
		//System.out.println("是否忙碌555555555:"+isBusy());
		//System.out.println("draw中的busyStr:"+getBusyStr());
		if(isShowTips){//是否有提示信息要显示
			drawTips(g2d);
		}
		//System.out.println("是否忙碌6666666666:"+isBusy());
		//System.out.println("draw中的busyStr:"+getBusyStr());
		if(isBusy()){//是否显示正在忙碌
			drawBusy(g2d);
		}
		//System.out.println("是否忙碌777777777:"+isBusy());
		//System.out.println("draw中的busyStr:"+getBusyStr());
	
	}
	
	abstract protected void SubDraw(Graphics2D g2d);

	public boolean isBusy() {
		return this.busy;
	}

	synchronized public void setBusy(boolean busy) {
		this.busy = busy;
	}

	public String getBusyStr() {
		return busyStr;
	}

	synchronized public void setBusy(String busyStr) {
		this.busyStr = busyStr;
		this.busy=true;
	}
	
	@Override
	public void goHome(){
		if(AbstractControler.currentControl instanceof HomeControler){
			return;
		}
		AbstractControler.setCurrentControl(new HomeControler());
	}
	
	@Override
	public void goBack(){
		if(AbstractControler.currentControl instanceof HomeControler){
			return;
		}
		if(null!=previousControler){
			previousControler.setBusy(false);
			previousControler.rotateAngle=0;
			AbstractControler.setCurrentControl(previousControler);
		}
	}

	public BaseControler getPreviousControler() {
		return previousControler;
	}

	public void setPreviousControler(BaseControler previousControler) {
		this.previousControler = previousControler;
	}
	
	/**
	 * 显示提示信息
	 * @param s-提示内容
	 * @param sec-显示时长(秒)
	 * @author Leijun
	 */
	public void ShowTips(String s, int sec){
		this.tipText=s;
		this.isShowTips=true;
		this.tipMiSec=sec*1000;
	}
	
	public void ShowTips(String s){
		ShowTips(s,2);
	}

}
