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
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.j2d.components.AndrodGlideBar;
import com.j2d.components.ComponentAdapter;
import com.j2d.components.SimpleEventAdapter;
import com.j2d.components.SizeableButton;
import com.j2d.components.SizeableEdit;
import com.j2d.components.SizeablePanel;
import com.j2d.framework.GraphicsUtils;
import com.j2d.framework.LSystem;

/**
 * 系统设置   
 * @author Leijun
 * @version   
 *       1.0.0, Mar 5, 2013 8:49:46 AM
 */
public class SettingControler extends BaseControler implements Runnable {
	
	//注册按钮的事件定义
	class BtnRegEvent extends SimpleEventAdapter{
		Runnable ctrl;
		public BtnRegEvent(Runnable ctrl){
			this.ctrl=ctrl;

		}
		
		@Override
		public void leftMousePressed(ComponentAdapter c, Point p) {//
			super.leftMousePressed(c, p);//按钮内部需要执行的

		}
	}
	
	//注册输入框的事件定义
	class EdtRegEvent extends SimpleEventAdapter{
		private SizeableEdit[] edits;
		private SizeableButton btn;
		public EdtRegEvent(SizeableEdit[] edits, SizeableButton btn){
			this.edits = edits;
			this.btn=btn;
		}
		
		@Override
		public void leftMousePressed(ComponentAdapter c, Point p) {//
			super.leftMousePressed(c, p);
			//挂载键盘
			LSystem.keyboard.setComp(c);
			LSystem.keyboard.setVisable(true);
		}
		
		@Override
		public void keyPressed(ComponentAdapter c, int keyCode, char keyChar) {
			super.keyPressed(c, keyCode, keyChar);
			String posId,regCode;
			posId=edits[0].getLabel();
			regCode=edits[1].getLabel();
			if((posId==null)||(posId.length()<12)){
				btn.setEnabled(false);
				return;
			}
			if((regCode==null)||(regCode.equals(""))){
				btn.setEnabled(false);
				return;
			}
			btn.setEnabled(true);
		}
	}
	
	private List<ComponentAdapter> list = new ArrayList<ComponentAdapter>();
	private AndrodGlideBar[] bars=new AndrodGlideBar[3];
	private SizeableEdit[] edits=new SizeableEdit[2];
	private SizeableButton regBtn;
	private SizeableEdit edtPsw;
	private SizeableButton btnPsw;
	private Font songti_20=new Font("宋体",Font.PLAIN,20);
	private Font songti_16=new Font("宋体",Font.PLAIN,16);
	private Font heiti_30=new Font("黑体",Font.PLAIN,30);
	private Font heiti_20=new Font("黑体",Font.PLAIN,20);
	//private Font heiti_16=new Font("黑体",Font.PLAIN,16);
	private String sTitle,sDisplay,sSound,sRegister,sSecurity;
	private String sLumin,sContrast,sVolume;
	private String sPosId,sRegCode;//终端Id、注册码
	private String sPsw;//管理员密码
	private int sTitleY,sDisplayY,sSoundY,sRegisterY,sSecurityY;
	
	private int plVerticalSpace=23;//上下panel之间的间距
	private int textToPanelDest=10;//panel标题文字基座与panel之间的距离
	private SizeablePanel plDisplay,plSound,plRegister,plSecurity;//panel
	private int plDisplayH,plSoundH,plRegisterH,plSecurityH;//各panel的高度
	private int plDisplayY,plSoundY,plRegisterY,plSecurityY;//各panel的Y坐标
	private int plX,plW,plTitleX;
	private int barW=200;//进度条宽度
	private int barH=30;//进度条高度
	private int sLuminX,sContrastX,sVolumeX;
	private int sLuminY,sContrastY,sVolumeY;
	private int sPosIdX,sRegCodeX,sPosIdY,sRegCodeY;//注册表单参数的坐标
	private int sPswX,sPswY;//管理员密码的显示坐标
	private int barLuminX,barContrastX,barVolumeX;
	private int barLuminY,barContrastY,barVolumeY;
	private int innerLeftSpace=10;//panel内文字距离panel左边的距离
	private int editCount=0;
	
	public SettingControler(){
		sTitle="设置";
		sDisplay="显示";
		sSound="声音";
		sRegister="注册";
		sSecurity="安全";
		
		sLumin="亮度";
		sContrast="对比度";
		sVolume="音量";
		
		sPosId="终端编号";
		sRegCode="注册码";
		
		sPsw="管理员密码";
		
		sTitleY=y+40;
		
		//display
		sDisplayY=sTitleY+plVerticalSpace-10;
		int plHorizSpace=10;//panel距离屏幕左边的水平间距
		plX=x+plHorizSpace;
		plTitleX=plX+10;//panel标题的X坐标
		plW=width-plHorizSpace*2;//panel宽度
		plDisplayY=sDisplayY+textToPanelDest;
		plDisplayH=90;
		
		sLuminX=plX+innerLeftSpace;
		int fixedTextH=5;//文字高度补偿
		sLuminY=sContrastY=plDisplayY+plDisplayH/2+fixedTextH;
		sContrastX=plX+plW/2+innerLeftSpace;
		int fixedTextW=60;//bar距离文字的水平距离
		barLuminX=plX+fixedTextW;
		barLuminY=barContrastY=plDisplayY+(plDisplayH-barH)/2;
		barContrastX=plX+plW/2+fixedTextW;
		
		
		//sound
		sSoundY=plDisplayY+plDisplayH+plVerticalSpace;
		plSoundY=sSoundY+textToPanelDest;
		plSoundH=90;
		
		sVolumeX=plX+innerLeftSpace;
		sVolumeY=plSoundY+plSoundH/2+fixedTextH;
		barVolumeX=plX+fixedTextW;
		barVolumeY=plSoundY+(plSoundH-barH)/2;
		
		//register
		sRegisterY=plSoundY+plSoundH+plVerticalSpace;
		plRegisterY=sRegisterY+textToPanelDest;
		plRegisterH=210;
		int btnRegX,btnRegY;//注册按钮的坐标
		//int regLeftSpace=20;
		sPosIdX=btnRegX=plX+innerLeftSpace;
		int firstRowOffset=60;//表单内第1排(文字)的Y坐标偏移
		sPosIdY=sRegCodeY=plRegisterY+firstRowOffset;
		sRegCodeX=plX+plW/2+innerLeftSpace;
		int secondRowOffset=100;//表单内第2排(按钮)的Y坐标偏移
		btnRegY=plRegisterY+secondRowOffset;
		
		//输入框
		//int edtW=250;
		SizeableEdit.initializeRows(edits, plX+100, plRegisterY+35, 250, 35, plW/2, songti_16, Color.black, null, null);
		edits[0].setHint("12位的数字");
		edits[0].setHintFont(heiti_20);
		edits[0].setHintFontColor(Color.gray);
		edits[0].setMaxLength(12);
		list.add(edits[0]);
		edits[1].setMaxLength(8);
		list.add(edits[1]);
		
		//注册按钮
		regBtn=new SizeableButton(btnRegX,btnRegY,150,35);
		regBtn.setLabel("注册");
		regBtn.setFont(songti_16);
		regBtn.setEnabled(false);
		BtnRegEvent btnEvent=new BtnRegEvent(this);//,edits
		regBtn.setEvent(btnEvent);
		list.add(regBtn);
		
		EdtRegEvent edtRegEvent=new EdtRegEvent(edits,regBtn);
		edits[0].setEvent(edtRegEvent);
		edits[1].setEvent(edtRegEvent);
		
		//security
		sSecurityY=plRegisterY+plRegisterH+plVerticalSpace;
		plSecurityY=sSecurityY+textToPanelDest;
		plSecurityH=90;
		
		sPswX=plX+innerLeftSpace;
		sPswY=plSecurityY+plSecurityH/2+8;
		int edtPswX=plX+120;
		int edtPswW=250;
		int edtPswH=35;
		int edtPswY=plSecurityY+(plSecurityH-edtPswH)/2;
		
		edtPsw=new SizeableEdit(0,edtPswX, edtPswY, edtPswW, edtPswH, "111111", songti_16, Color.black, null, null);
		edtPsw.setPassword(true);
		edtPsw.setHint("请输入密码");
		edtPsw.setHintFont(heiti_20);
		edtPsw.setHintFontColor(Color.gray);
		edtPsw.setMaxLength(6);
		edtPsw.setEvent(new SimpleEventAdapter(){
			@Override
			public void leftMousePressed(ComponentAdapter c, Point p) {//
				super.leftMousePressed(c, p);
				//挂载键盘
				LSystem.keyboard.setComp(c);
				LSystem.keyboard.setVisable(true);
			}
		});
		list.add(edtPsw);
		
		btnPsw=new SizeableButton(edtPswX+edtPswW+50,edtPswY, 150,edtPswH);
		btnPsw.setLabel("修改");
		btnPsw.setFont(songti_16);
		btnPsw.setEnabled(false);
		btnPsw.setEvent(new SimpleEventAdapter());
		list.add(btnPsw);
		
		plDisplay = new SizeablePanel(plX,plDisplayY,plW,plDisplayH,10);
		plSound = new SizeablePanel(plX,plSoundY,plW,plSoundH,10);
		plRegister = new SizeablePanel(plX,plRegisterY,plW,plRegisterH,10);
		plSecurity = new SizeablePanel(plX,plSecurityY,plW,plSecurityH,10);
		
		bars[0]=new AndrodGlideBar(barLuminX,barLuminY,barW,barH,1.0f);
		bars[0].setSmallRadius(5);
		SimpleEventAdapter barEvent=new SimpleEventAdapter();
		bars[0].setEvent(barEvent);
		list.add(bars[0]);
		
		bars[1]=new AndrodGlideBar(barContrastX,barContrastY,barW,barH,0.5f);
		bars[1].setSmallRadius(5);
		bars[1].setEvent(barEvent);
		list.add(bars[1]);
		
		bars[2]=new AndrodGlideBar(barVolumeX,barVolumeY,barW,barH,0.0f);
		bars[2].setSmallRadius(5);
		bars[2].setEvent(barEvent);
		list.add(bars[2]);
		
		compList.add(list);
		super.AddKeyboard();
	}

	@Override
	protected void SubDraw(Graphics2D g2d) {
		g2d.setColor(Color.blue);
		g2d.fillRect(x, y, width, height);
		GraphicsUtils.setRenderingHints(g2d);
		GraphicsUtils.setAntialias(g2d, true);
		
		//页面标题
		g2d.setColor(Color.white);
		g2d.setFont(heiti_30);
		FontMetrics fm=g2d.getFontMetrics();
		int sTitleW=GraphicsUtils.getFontWidth(fm, sTitle);
		int sTitleX=x+(width-sTitleW)/2;
		g2d.drawString(sTitle, sTitleX, sTitleY);
		
		//显示-----------------------------------------------------------
		g2d.setColor(Color.white);
		g2d.setFont(heiti_20);
		g2d.drawString(sDisplay, plTitleX, sDisplayY);
		//------------------
		plDisplay.draw(g2d);
		
		g2d.setColor(Color.black);
		g2d.setFont(songti_20);
		g2d.drawString(sLumin, sLuminX, sLuminY);
		bars[0].draw(g2d);
		
		g2d.setColor(Color.black);
		g2d.setFont(songti_20);
		g2d.drawString(sContrast, sContrastX, sContrastY);
		bars[1].draw(g2d);
		//--------------------------------------------------------------
		
		//声音-----------------------------------------------------------
		g2d.setColor(Color.white);
		g2d.setFont(heiti_20);
		g2d.drawString(sSound, plTitleX, sSoundY);
		//------------------
		plSound.draw(g2d);
		
		g2d.setColor(Color.black);
		g2d.setFont(songti_20);
		g2d.drawString(sVolume, sVolumeX, sVolumeY);
		bars[2].draw(g2d);
		//--------------------------------------------------------------
		
		//注册-----------------------------------------------------------
		g2d.setColor(Color.white);
		g2d.setFont(heiti_20);
		g2d.drawString(sRegister, plTitleX, sRegisterY);
		//------------------
		plRegister.draw(g2d);
		
		g2d.setColor(Color.black);
		g2d.setFont(songti_20);
		g2d.drawString(sPosId, sPosIdX, sPosIdY);
		edits[0].draw(g2d);
		GraphicsUtils.setAntialias(g2d, true);
		
		g2d.setColor(Color.black);
		g2d.setFont(songti_20);
		g2d.drawString(sRegCode, sRegCodeX, sRegCodeY);
		edits[1].draw(g2d);
		GraphicsUtils.setAntialias(g2d, true);
		
		regBtn.draw(g2d);
		//--------------------------------------------------------------
		
		//安全-----------------------------------------------------------
		g2d.setColor(Color.white);
		g2d.setFont(heiti_20);
		g2d.setComposite(LSystem.alpha_100);
		g2d.drawString(sSecurity, plTitleX, sSecurityY);
		//------------------
		plSecurity.draw(g2d);
		g2d.setColor(Color.black);
		g2d.setFont(songti_20);
		g2d.drawString(sPsw, sPswX, sPswY);
		edtPsw.draw(g2d);
		btnPsw.draw(g2d);
	}
	
	@Override
	public void next() {
		super.next();
		
		//编辑框的光标控制
		if(editCount>=LSystem.fps){//1秒之后重新计数
			editCount=0;
		}
		if(editCount<LSystem.fps/2){//0.5秒显示，0.5秒隐藏
			for(int i=0;i<edits.length;i++)
				edits[i].setCursor(true);
		}else{
			for(int i=0;i<edits.length;i++)
				edits[i].setCursor(false);
		}
		editCount++;
		
		//
	}
	
	@Override
	public void goHome(){
		if(null!=LSystem.PosId && LSystem.PosId.length()==12){
			super.goHome();
		}
	}

	@Override
	public void run(){
		
	}
}
