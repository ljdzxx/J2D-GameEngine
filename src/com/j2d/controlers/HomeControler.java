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

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.j2d.components.ComponentAdapter;
import com.j2d.components.ImageButton;
import com.j2d.components.SimpleEventAdapter;
import com.j2d.framework.LSystem;

/**
 * 主界面
 * @author Leijun
 * @version   
 *       1.0.0, Mar 6, 2013 11:22:01 AM
 */
public class HomeControler extends BaseControler implements Runnable {
	public enum BusinessType{
		NONE,	//无业务类型
		BUSINESS_1,	//模拟业务1
		BUSINESS_2,	//模拟业务2
		BUSINESS_3,	//模拟业务3
		BUSINESS_4,	//模拟业务4
		BUSINESS_5	//模拟业务5
	}
	
	private ImageButton[] iconBtns_1 = new ImageButton[5];
	private ImageButton[] iconBtns_2 = new ImageButton[4];
	private ImageButton[] iconBtns_3 = new ImageButton[5];
	private List<ComponentAdapter> list = new ArrayList<ComponentAdapter>();
	//private int clickbuttonId = -1;
	private static final int BLANK_LEFT=7;
	private static final int BLANK_TOP=51;
	private static final int ICON_SPACE=5;
	private int count=0;
	private int blurNeedSec=1;//button的模糊效果需要持续的时间(秒)
	private boolean isHitGesture=false;
	//鼠标坐标X*Y
	private ArrayList<Point> mPoint=new ArrayList<Point>();
	//private BusinessType businessType=BusinessType.NONE;//默认无业务类型
	
	class TopUpEvent extends SimpleEventAdapter{
		private HomeControler controler=null;
		@Override
		public void leftMousePressed(ComponentAdapter c, Point p) {//
			super.leftMousePressed(c, p);
			if(LSystem.isLogined){
				
			}else{
				
			}
			
		}
		public BaseControler getControler() {
			return controler;
		}
		public void setControler(HomeControler controler) {
			this.controler = controler;
		}
	}
	
	public HomeControler(){
		//super();
		BufferedImage img;
		
		//第1排按钮-------------------------------------------------------------------------------------------
		img=LSystem.imsLoader.getImage("icon_285_topup");
		ImageButton.initializeRows(iconBtns_1, super.x+BLANK_LEFT, super.y+BLANK_TOP, ICON_SPACE, img, img,labelFont,C_WHITE);
		iconBtns_1[0].setLabel("敬请期待");
		iconBtns_1[0].setBlurringEffect(true);//由模糊至清晰的显示效果
		TopUpEvent topUpEvent=new TopUpEvent();
		topUpEvent.setControler(this);
		iconBtns_1[0].setEvent(topUpEvent);
		list.add(iconBtns_1[0]);
		
		img=LSystem.imsLoader.getImage("icon_140_gray");//icon_140_green
		iconBtns_1[1].setNormalImg(img);
		iconBtns_1[1].setMouseDownImg(img);
		iconBtns_1[1].setX(getCurrentImgX(iconBtns_1,1));
		iconBtns_1[1].setLabel("敬请期待");
		iconBtns_1[1].setEvent(new SimpleEventAdapter(){
			@Override
			public void leftMousePressed(ComponentAdapter c, Point p) {//
				// TODO Auto-generated method stub
				super.leftMousePressed(c, p);
			}
		});
		list.add(iconBtns_1[1]);
		
		img=LSystem.imsLoader.getImage("icon_140_gray");//icon_140_red
		iconBtns_1[2].setNormalImg(img);
		iconBtns_1[2].setMouseDownImg(img);
		//iconBtns_1[2].setUnFocusImage(img);
		iconBtns_1[2].setX(getCurrentImgX(iconBtns_1,2));
		iconBtns_1[2].setLabel("敬请期待");
		//iconBtns_1[2].setFontColor(C_GRAY);//字体颜色：灰
		list.add(iconBtns_1[2]);
		
		img=LSystem.imsLoader.getImage("icon_285_wifi");
		iconBtns_1[3].setNormalImg(img);
		iconBtns_1[3].setMouseDownImg(img);
		//iconBtns_1[3].setUnFocusImage(img);
		iconBtns_1[3].setX(getCurrentImgX(iconBtns_1,3));
		iconBtns_1[3].setLabel("敬请期待");
		iconBtns_1[3].setBlurringEffect(true);//由模糊至清晰的显示效果
		iconBtns_1[3].setEvent(new SimpleEventAdapter(){
			@Override
			public void leftMousePressed(ComponentAdapter c, Point p) {//
				// TODO Auto-generated method stub
				super.leftMousePressed(c, p);
			}
		});
		list.add(iconBtns_1[3]);
		
		img=LSystem.imsLoader.getImage("icon_140_call_center");
		iconBtns_1[4].setNormalImg(img);
		iconBtns_1[4].setMouseDownImg(img);
		//iconBtns_1[4].setUnFocusImage(img);
		iconBtns_1[4].setX(getCurrentImgX(iconBtns_1,4));
		iconBtns_1[4].setLabel("敬请期待");
		iconBtns_1[4].setBlurringEffect(true);//由模糊至清晰的显示效果
		iconBtns_1[4].setEvent(new SimpleEventAdapter(){
			@Override
			public void leftMousePressed(ComponentAdapter c, Point p) {//
				// TODO Auto-generated method stub
				super.leftMousePressed(c, p);
			}
		});
		list.add(iconBtns_1[4]);
		
		//第2排按钮-----------------------------------------------------------------------------
		img=LSystem.imsLoader.getImage("icon_140_gray");//"icon_140_blue_",0
		ImageButton.initializeRows(iconBtns_2, super.x+BLANK_LEFT, super.y+BLANK_TOP+img.getHeight()+ICON_SPACE, ICON_SPACE, img, img,labelFont,C_WHITE);
		iconBtns_2[0].setLabel("敬请期待");
		//iconBtns_2[0].setFontColor(C_GRAY);//字体颜色：灰
		list.add(iconBtns_2[0]);
		
		img=LSystem.imsLoader.getImage("icon_140_gray");//icon_140_orange
		iconBtns_2[1].setNormalImg(img);
		iconBtns_2[1].setMouseDownImg(img);
		//iconBtns_2[1].setUnFocusImage(img);
		iconBtns_2[1].setX(getCurrentImgX(iconBtns_2,1));
		iconBtns_2[1].setLabel("敬请期待");
		list.add(iconBtns_2[1]);
		
		img=LSystem.imsLoader.getImage("icon_285_voip");
		iconBtns_2[2].setNormalImg(img);
		iconBtns_2[2].setMouseDownImg(img);
		//iconBtns_2[2].setUnFocusImage(img);
		iconBtns_2[2].setX(getCurrentImgX(iconBtns_2,2));
		iconBtns_2[2].setLabel("敬请期待");
		iconBtns_2[2].setBlurringEffect(true);//由模糊至清晰的显示效果
		iconBtns_2[2].setEvent(new SimpleEventAdapter(){
			@Override
			public void leftMousePressed(ComponentAdapter c, Point p) {//
				// TODO Auto-generated method stub
				super.leftMousePressed(c, p);
			}
		});
		list.add(iconBtns_2[2]);
		
		img=LSystem.imsLoader.getImage("icon_285_barcode");
		iconBtns_2[3].setNormalImg(img);
		iconBtns_2[3].setMouseDownImg(img);
		//iconBtns_2[3].setUnFocusImage(img);
		iconBtns_2[3].setX(getCurrentImgX(iconBtns_2,3));
		iconBtns_2[3].setLabel("敬请期待");
		iconBtns_2[3].setBlurringEffect(true);//由模糊至清晰的显示效果
		iconBtns_2[3].setEvent(new SimpleEventAdapter(){
			@Override
			public void leftMousePressed(ComponentAdapter c, Point p) {//
				// TODO Auto-generated method stub
				super.leftMousePressed(c, p);
			}
		});
		list.add(iconBtns_2[3]);
		
		//第3排按钮-----------------------------------------------------------------------------
		img=LSystem.imsLoader.getImage("icon_140_gray");//icon_140_purple
		ImageButton.initializeRows(iconBtns_3, super.x+BLANK_LEFT, super.y+BLANK_TOP+(img.getHeight()+ICON_SPACE)*2, ICON_SPACE, img, img,labelFont,C_WHITE);
		iconBtns_3[0].setLabel("敬请期待");
		list.add(iconBtns_3[0]);
		
		img=LSystem.imsLoader.getImage("icon_140_gray");//icon_140_green
		iconBtns_3[1].setNormalImg(img);
		iconBtns_3[1].setMouseDownImg(img);
		//iconBtns_3[1].setUnFocusImage(img);
		iconBtns_3[1].setX(getCurrentImgX(iconBtns_3,1));
		iconBtns_3[1].setLabel("敬请期待");
		list.add(iconBtns_3[1]);
		
		img=LSystem.imsLoader.getImage("icon_285_gray");//icon_285_blue
		iconBtns_3[2].setNormalImg(img);
		iconBtns_3[2].setMouseDownImg(img);
		//iconBtns_3[2].setUnFocusImage(img);
		iconBtns_3[2].setX(getCurrentImgX(iconBtns_3,2));
		iconBtns_3[2].setLabel("敬请期待");
		list.add(iconBtns_3[2]);
		
		img=LSystem.imsLoader.getImage("icon_140_gray");//icon_140_green
		iconBtns_3[3].setNormalImg(img);
		iconBtns_3[3].setMouseDownImg(img);
		//iconBtns_3[3].setUnFocusImage(img);
		iconBtns_3[3].setX(getCurrentImgX(iconBtns_3,3));
		iconBtns_3[3].setLabel("敬请期待");
		list.add(iconBtns_3[3]);
		
		img=LSystem.imsLoader.getImage("icon_140_gray");//"icon_140_blue_",2
		iconBtns_3[4].setNormalImg(img);
		iconBtns_3[4].setMouseDownImg(img);
		//iconBtns_3[4].setUnFocusImage(img);
		iconBtns_3[4].setX(getCurrentImgX(iconBtns_3,4));
		iconBtns_3[4].setLabel("敬请期待");
		list.add(iconBtns_3[4]);
		
		compList.add(list);
		
		if(!LSystem.isLogined){//尚未登录
			new Thread(this).start();
		}
	}
	
	/**
	 * 计算给定的ImageButton[]中指定序号的图像所在的X坐标
	 * @param imgIndex
	 * @return
	 * @author Leijun
	 */
	private int getCurrentImgX(ImageButton[] btns, int imgIndex){
		int x=super.x+BLANK_LEFT;
		if(imgIndex==0)return x;
		for(int i=0;i<imgIndex;i++){
			if(i>=btns.length) return x;
			x+=btns[i].getWidth();
			x+=ICON_SPACE;
		}
		return x;
	}

	/**
	 * 画应用按钮
	 * @param g2d
	 * @author Leijun
	 */
	private synchronized void drawIconButtons(final Graphics2D g2d) {
		for (int i = 0; i < list.size(); i++) {
			ImageButton button = ((ImageButton) list.get(i));
			
			if(285==button.getWidth()){//大图片
				button.draw(g2d, 20, 122);//, "宋体", 0, 9	
			}
			else{//小图片
				button.draw(g2d, 10, 122);
			}
		}
	}
	
	@Override
	public void next(){
		super.next();
		int blurNeedTimes=7;//这是button中定义的一个常量，由模糊到清晰的变换次数
		int frameCount=LSystem.fps*this.blurNeedSec;//实现模糊效果需要的帧数
		int frames=frameCount/blurNeedTimes;
		count=(count+1)%frames;	//每n帧后执行
		
		if(count==0){
			for(int i=0;i<list.size();i++){
				ImageButton button = ((ImageButton) list.get(i));
				if(button.isBlurringEffect()){
					button.nextOddSize();
				}
			}
		}
	}
	
	@Override
	public void SubDraw(Graphics2D g2d) {
		drawIconButtons(g2d);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		super.mouseDragged(e);
	}
	
	@Override
	/**
	 * 手势判断(较长的波浪线)
	 */
	public void mouseReleased(MouseEvent e) {
		super.mouseReleased(e);
		
		isHitGesture=false;
		//判断轨迹(波浪线)
		if(mPoint.size()<10)return;
		Point pt1,pt2;
		pt1=mPoint.get(0);
		pt2=mPoint.get(mPoint.size()-1);
		double[] arrDest=new double[mPoint.size()-2];
		//double maxDest=0;
		double peakDest=10;//波浪线的波峰应超过这个距离，这个值用于排除直线
		int overed=0;//超过设定峰值的点的个数
		int changeDirections=0;//波峰与波谷的变换次数（波浪线的特征）
		int changePosition=0;//变换发生的位置
		//计算每个点到直线的距离
		boolean isPositive=true;//是否递增
		for(int i=1;i<mPoint.size()-1;i++){
			arrDest[i-1]=destFromPointToLine(pt1,pt2,mPoint.get(i));
			if(arrDest[i-1]>peakDest){overed++;}//统计超过指定值的个数
			if(i-1>0){
				if(((arrDest[i-1]-arrDest[i-1-1]>=0) && !isPositive) || ((arrDest[i-1]-arrDest[i-1-1]<0) && isPositive)){
					if(i-1-changePosition>5){//下一次变向要在n步之外才算有效（过滤手指的抖动）
						isPositive=!isPositive;
						changeDirections++;
						changePosition=i-1;
					}
				}
				
			}
		}
		double scale=(double)overed/(double)arrDest.length;//峰值超过设定数值的占比
		System.out.println("point count:"+arrDest.length+" overed:"+overed+" scale:"+scale+" changeDirections:"+changeDirections);
		isHitGesture = (scale>0.5) && (changeDirections>=5);
		mPoint.clear();
		
		
		if(isHitGesture){//符合进入系统设置的手势(波浪线)
			isHitGesture=false;
			BaseControler ctrl=new SecurityControler();
			ctrl.setPreviousControler(this);
			AbstractControler.setCurrentControl(ctrl);	
		}

	}
	
	/**
	 * 点到直线的距离
	 * @param pt1-直线的起点
	 * @param pt2-直线的终点
	 * @param pt0-平面内的一个点
	 * @return
	 * @author Leijun
	 */
	private double destFromPointToLine(Point pt1, Point pt2, Point pt0){
		return (Math.abs((pt2.getY() - pt1.getY()) * pt0.getX() +(pt1.getX() - pt2.getX()) * pt0.getY() + ((pt2.getX() * pt1.getY()) -(pt1.getX() * pt2.getY())))) / (Math.sqrt(Math.pow(pt2.getY() - pt1.getY(), 2) + Math.pow(pt1.getX() - pt2.getX(), 2)));
	}

	public void run() {

	}
	
}

