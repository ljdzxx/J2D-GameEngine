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
/**
 * 各种场景控制器的父类
 */

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;
import com.j2d.components.AndrodKeyboard;
import com.j2d.components.ComponentAdapter;
import com.j2d.framework.LSystem;


public abstract class AbstractControler implements IControler {
	
	/**
	 * 场景中所有的ComponentAdapter类型控件都要放在此变量中管理
	 */
	protected List<List<ComponentAdapter>> compList=new ArrayList<List<ComponentAdapter>>();

	public static IControler currentControl=null;
	
	/**
	 * 当前激活的ComponentAdapter控件
	 */
	protected ComponentAdapter activeComponent=null;

	public volatile static boolean leftMouseDown=false;

	private boolean showKeyboard=false;

	public AbstractControler() {
	}
	
	/**
	 * 切换场景
	 * @param control
	 * @author Leijun
	 */
	public synchronized static void setCurrentControl(final IControler control) {
		AbstractControler.currentControl = control;
		if(null!=control){
			if(!control.defaultShowKeyboard()){
				LSystem.keyboard.setVisable(false);
			}
		}
	}


	public static synchronized boolean validControl() {
		return currentControl!=null;	//!AbstractControler.isControlOfNull;
	}
	
	/*public void next() {
	}*/

	public void setFrame(int i) {
	}
	
	//在同一个位置点击并释放
	public void mouseClicked(MouseEvent e) {
	}

	/**
	 * 鼠标有键按下
	 */
	public void mousePressed(MouseEvent e) {
		boolean isFound=false;
		if ( (e.getButton() == MouseEvent.BUTTON1) && AbstractControler.validControl() ) {
			if(AbstractControler.currentControl.isBusy())return;//当前场景繁忙
			leftMouseDown=true;
			Point p = e.getPoint();
			List<List<ComponentAdapter>> l = AbstractControler.currentControl.getCompList();
			if (l.size() > 0) {
				ComponentAdapter comp;
				try {
					for (int i = 0; i < l.size(); i++) {
						List<ComponentAdapter> ll=l.get(i);
						if(null==ll)continue;
						for(int j=0;j<ll.size();j++){
							comp=ll.get(j);
							if(null==comp)continue;
							if(!comp.isVisable())continue;//不可视
							if(!isFound && ( p.x > comp.getX()
									&& p.x < comp.getX() + comp.getWidth()
									&& p.y > comp.getY() 
									&& p.y < comp.getY() + comp.getHeight())
							)
							{
								comp.setActive(true);
								currentControl.setActiveComponent(comp);//设置激活的控件
								//comp.leftMousePressed(p);
								if(null!=comp.getEvent() && comp.isEnabled()){
									comp.getEvent().leftMousePressed(comp,p);//执行该控件上定义的鼠标点击事件
								}
								isFound=true;
								
								if(comp instanceof AndrodKeyboard){//如果点击在软键盘上，则其他控件状态不变
									//System.out.println("OK,AndrodKeyboard!");
									return;
								}
							}else{
								comp.setActive(false);
								//break;
							}
							
						}
						
					}
				} catch (Exception ee) {
					// TODO: handle exception
				}
			}
			
			if(!isFound){
				AbstractControler.currentControl.setActiveComponent(null);//没有激活的控件(点击了画布空白处)
			}
			if(!isFound || (isFound && (currentControl.getActiveComponent()!=LSystem.keyboard.getComp()))){//找到了，但是找到的控件不是键盘的绑定控件
				LSystem.keyboard.setVisable(false);//不显示键盘(如果原来是显示的话)
			}
		}	
		/*else if(e.getButton() == MouseEvent.BUTTON3){
			right_press=true;
		}*/
		
	}

	/**
	 * 鼠标释放
	 */
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {

			if (AbstractControler.validControl()) {
				if(AbstractControler.currentControl.isBusy())return;//当前场景繁忙
				ComponentAdapter comp = AbstractControler.currentControl
						.getActiveComponent();
				if ( (comp != null) && comp.isVisable() ) {
					Point p = e.getPoint();
					//comp.leftMouseReleased(p);
					if (null != comp.getEvent() && comp.isEnabled()) {
						comp.getEvent().leftMouseReleased(comp,p);// 执行该控件上定义的鼠标释放事件
					}
				}
			}
			
			leftMouseDown=false;
		}
	}

	public void mouseEntered(MouseEvent e) {
		//mouse = e.getPoint();
	}

	public void mouseExited(MouseEvent e) {
		//mouse = e.getPoint();
	}

	public void mouseDragged(MouseEvent e) {
		if (AbstractControler.validControl()) {
			if(AbstractControler.currentControl.isBusy())return;//当前场景繁忙
			ComponentAdapter comp = AbstractControler.currentControl
					.getActiveComponent();
			if ( (comp != null) && comp.isVisable() ) {
				if (null != comp.getEvent() && comp.isEnabled()) {
					comp.getEvent().mouseDragged(comp,e);// 执行接口上定义的拖拽事件
				}
			}
		}
	}

	public void mouseMoved(MouseEvent e) {
		//mouse = e.getPoint();
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		if (AbstractControler.validControl()) {
			if(AbstractControler.currentControl.isBusy())return;//当前场景繁忙
			ComponentAdapter comp = AbstractControler.currentControl
					.getActiveComponent();
			if ( (comp != null) && comp.isVisable() ) {
				int keyCode = e.getKeyCode();
				char keyChar = e.getKeyChar();
				//comp.keyPressed(keyCode, keyChar);
				if (null != comp.getEvent() && comp.isEnabled()) {
					comp.getEvent().keyPressed(comp,keyCode, keyChar);// 执行该控件上定义的键盘事件
				}
			}
		}
	}

	// 键按下后被释放
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_ESCAPE) {//ESC键
			System.exit(0);// 退出程序
		}
	}

	@Override
	public List<List<ComponentAdapter>> getCompList() {
		return compList;
	}
	
	@Override
	public synchronized void setActiveComponent(ComponentAdapter c){
		this.activeComponent=c;
	}
	
	@Override
	public ComponentAdapter getActiveComponent(){
		return this.activeComponent;
	}
	
	@Override
	public boolean defaultShowKeyboard(){
		return showKeyboard;
	}

	public void setShowKeyboard(boolean showKeyboard) {
		this.showKeyboard = showKeyboard;
	}

}
