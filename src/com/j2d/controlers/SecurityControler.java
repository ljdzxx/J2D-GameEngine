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

import com.j2d.components.ComponentAdapter;
import com.j2d.components.SimpleEventAdapter;
import com.j2d.components.SizeableButton;
import com.j2d.components.SizeableEdit;
import com.j2d.components.SizeablePanel;
import com.j2d.framework.GraphicsUtils;
import com.j2d.framework.LSystem;

/**
 * 安全校验   
 * @author Leijun
 * @version   
 *       1.0.0, Mar 5, 2013 8:50:10 AM
 */
public class SecurityControler extends BaseControler {

	class btnOkEvent extends SimpleEventAdapter{
		private BaseControler controler=null;
		private SizeableEdit edit;
		@Override
		public void leftMousePressed(ComponentAdapter c, Point p) {//
			super.leftMousePressed(c, p);//按钮内部需要执行的
			if(edit==null)return;
			//校验密码---------------------------------------------------
			String psw=edit.getLabel();
			if(psw.equals("111111")){
				SettingControler setCtrl=new SettingControler();
				AbstractControler.setCurrentControl(setCtrl);
			}else{
				ShowTips("密码错误！");
			}
			//----------------------------------------------------------
		}
		public BaseControler getControler() {
			return controler;
		}
		public void setControler(BaseControler controler) {
			this.controler = controler;
		}
		
		public SizeableEdit getEdit() {
			return edit;
		}
		public void setEdit(SizeableEdit edit) {
			this.edit = edit;
		}
	}
	
	private List<ComponentAdapter> list = new ArrayList<ComponentAdapter>();
	//private Font songti_20=new Font("宋体",Font.PLAIN,20);
	private Font songti_16=new Font("宋体",Font.PLAIN,16);
	private Font heiti_40=new Font("黑体",Font.PLAIN,40);
	private Font heiti_20=new Font("黑体",Font.PLAIN,20);
	private Font heiti_16=new Font("黑体",Font.PLAIN,16);
	private int titleX, titleY;
	private int plH,plW,plX,plY;
	private int edtW,edtH,edtX,edtY;
	private int btnW,btnH,btnX,btnY;
	private SizeablePanel sPanel;
	private SizeableEdit[] edits=new SizeableEdit[1];
	private SizeableButton[] btns=new SizeableButton[1];
	private String title;
	private int pswEditCount=0;
	
	public SecurityControler(){
		//super();
		//panel
		plW=width/2;
		plH=200;
		plX=x+(width-plW)/2;
		plY=y+(height-plH)/2;
		//标题
		title="系统维护";
		
		//密码框和按钮
		edtH=btnH=50;
		int ebW=(plW-40);
		int spaceW=20;
		btnW=80;
		edtW=ebW-(spaceW+btnW);
		edtX=plX+(plW-ebW)/2;
		edtY=btnY=plY+(plH-edtH)/2;
		btnX=edtX+edtW+spaceW;
		
		sPanel=new SizeablePanel(plX,plY,plW,plH,10);
		
		//确认按钮
		btns[0]=new SizeableButton(this.btnX, this.btnY, this.btnW,this.btnH);
		btns[0].setLabel("确定");
		btns[0].setFont(songti_16);
		btns[0].setEnabled(true);
		btnOkEvent okEvent=new btnOkEvent();
		okEvent.setControler(this);
		btns[0].setEvent(okEvent);
		list.add(btns[0]);
		
		SizeableEdit.initializeColumns(edits, edtX, edtY, edtW, edtH, 0, heiti_20, Color.black, heiti_16, Color.black);
		edits[0].setPassword(true);
		edits[0].setTitle("管理员密码");
		edits[0].setHint("请输入密码");
		edits[0].setHintFont(heiti_20);
		edits[0].setHintFontColor(Color.gray);
		edits[0].setMaxLength(6);
		edits[0].setEvent(new SimpleEventAdapter(){
			@Override
			public void leftMousePressed(ComponentAdapter c, Point p) {//
				super.leftMousePressed(c, p);
				//挂载键盘
				LSystem.keyboard.setComp(c);
				LSystem.keyboard.setVisable(true);
			}
		});
		okEvent.setEdit(edits[0]);
		list.add(edits[0]);
		
		compList.add(list);//加入compList后才可获得鼠标、键盘事件
		LSystem.keyboard.setOnlyNumber(false);
		AddKeyboard();
	}
	
	@Override
	protected void SubDraw(Graphics2D g2d) {
		g2d.setColor(Color.blue);
		g2d.fillRect(x, y, width, height);
		

		
		//环境设置
		GraphicsUtils.setRenderingHints(g2d);
		
		//绘制标题
		g2d.setColor(Color.white);
		g2d.setFont(heiti_40);
		FontMetrics fm=g2d.getFontMetrics();
		int titleW=GraphicsUtils.getFontWidth(fm, title);
		titleX=plX+(plW-titleW)/2;
		titleY=plY-30;
		
		g2d.drawString(title, titleX, titleY);
		//绘制panel
		sPanel.draw(g2d);
		//密码框
		edits[0].draw(g2d);
		//按钮
		btns[0].draw(g2d);
	}
	
	@Override
	public void next() {
		super.next();
		
		if(pswEditCount>=LSystem.fps){//1秒之后重新计数
			pswEditCount=0;
		}
		if(pswEditCount<LSystem.fps/2){//0.5秒显示，0.5秒隐藏
			edits[0].setCursor(true);
		}else{
			edits[0].setCursor(false);
		}
		pswEditCount++;
	}

}
