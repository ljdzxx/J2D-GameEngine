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
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;


import com.j2d.components.AndrodKeyboard;
import com.j2d.components.SimpleEventAdapter;
import com.j2d.framework.GraphicsUtils;
import com.j2d.framework.LSystem;
import com.j2d.resource.Configuration;
import com.j2d.resource.ImagesLoader;

/**
 * Loading
 * @author Leijun
 * @version   
 *       1.0.0, Mar 19, 2013 8:49:44 AM
 */
public class LoadingControler extends AbstractControler implements Runnable  {
	//private int i=0;
	private int imgIndex=-1;
	private int imgW,imgH,logoX,logoY;
	private Font font = new Font("黑体", Font.PLAIN, 18);//信息字体
	private String deviceMsg="正在启动...";
	/**
	 * 初始化状态:
	 * 0-尚未进行初始化
	 * 1-初始化成功
	 * 2-初始化失败，致命故障，无法继续下一步
	 */
	private int InitializedResult=0;
	//错误列表
	private ArrayList<String> errList=new ArrayList<String>();
	
	
	public LoadingControler(){
		imgW=500;//logo的宽和高
		imgH=100;
		logoX=(LSystem.WIDTH-imgW)/2;
		logoY=(LSystem.HEIGHT-imgH)/2;
		
		//启动线程初始化外设
		new Thread(this).start();
	}
	
	/**
	 * 显示错误列表
	 * @param g2d
	 * @param x
	 * @param y
	 * @author Leijun
	 */
	private void drawErrList(Graphics2D g2d, int x,int y){
		int offsetY=20;
		for(int i=0;i<errList.size();i++){
			g2d.drawString(errList.get(i), x, y+offsetY*(i+1));
		}
	}
	
	@Override
	public void draw(Graphics2D g2d) {
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, LSystem.WIDTH, LSystem.HEIGHT);
		
		//显示LOGO图片
		if(null!=LSystem.imsLoader){//要判断一下图片是否已经加载完成
			int i=(imgIndex<19)?imgIndex:0;
			BufferedImage bi=LSystem.imsLoader.getImage("pt879-",i);
			g2d.drawImage(bi, logoX, logoY, null);
		}
		
		
		switch (InitializedResult) {
		case 1://成功
			if((null!=LSystem.PosId)&&(LSystem.PosId.length()==12)){
				AbstractControler.setCurrentControl(new HomeControler());
			}else{
				AbstractControler.setCurrentControl(new SecurityControler());
			}
			break;
		case 2://失败，严重故障
			//deviceMsg="初始化失败:";
			break;
		default://检测中

			break;
		}
		GraphicsUtils.setAntialias(g2d,true);
		g2d.setFont(font);
		g2d.setColor(Color.white);
		
		int infoX=logoX+40;
		int infoY=logoY+imgH+40;
		//显示正在干的活
		g2d.drawString(deviceMsg, infoX, infoY);
		//有错误则显示错误
		drawErrList(g2d, infoX, infoY);
		
		GraphicsUtils.setAntialias(g2d,false);
	}
	
	@Override
	 public void next() {
		//super.next();
		
		if(imgIndex<19*5){
			imgIndex++;
		}else{
			imgIndex=0;
		}
		
		/*if(AbstractControler.leftMouseDown){
			AbstractControler.setCurrentControl(new HomeControler());
		}*/
	}
	
	@Override
	public boolean isBusy() {
		return false;
	}
	
	@Override
	public void setBusy(boolean busy) {
		//
	}
	
	/**
	 * 加载配置文件
	 * @author Leijun
	 */
	private void loadResource(){
		deviceMsg="正在加载资源...";
		
		//图片资源
		LSystem.imsLoader=new ImagesLoader("./images/");//binPath+   +File.separator
		
		//属性文件
		//PropertiesReadWrite properties=new PropertiesReadWrite();
		//String binPath=this.getClass().getResource("/").getPath();
		String device_url = "./config/device/drivers.properties";
		String comm_url   = "./config/info.properties";//binPath+
		String coin_url   = "./config/device/coinset/storage/detail.properties";
		String note_url   = "./config/device/noteset/gba/detail.properties";
		//字体文件
		String msyhPath="./res/msyh.ttf";//binPath+
		File file=new File(msyhPath);
		try {
			//LSystem.deviceConfig=properties.readProperties(device_url);
			//LSystem.commConfig=properties.readProperties(comm_url);
			LSystem.commConfig=new Configuration(comm_url);
			LSystem.PosId=LSystem.commConfig.getValue("POSID");
			LSystem.deviceConfig=new Configuration(device_url);
			LSystem.coinBoxList=new Configuration(coin_url);
			LSystem.noteBoxList=new Configuration(note_url);
			
			//加载微软雅黑字体
			FileInputStream fi=new FileInputStream(file);
			BufferedInputStream fontStream=new BufferedInputStream(fi);
			LSystem.msyh=Font.createFont(Font.TRUETYPE_FONT, fontStream);
		} catch (Exception e) {
			InitializedResult=2;
			errList.add(e.getMessage());
		}
		//Androd风格键盘初始化
		LSystem.keyboard=new AndrodKeyboard(0,LSystem.HEIGHT-160-60);//键盘初始化
		LSystem.keyboard.setEvent(new SimpleEventAdapter());//纳入事件控制
	}
	
	/*@Override
	public boolean update(Object o, Object arg){
		System.out.println("this的类名:"+this.getClass().getName());
		return false;
	}*/
	
	@Override
	public void run(){
		loadResource();
		
	}
	
}
