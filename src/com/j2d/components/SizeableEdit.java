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
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import com.j2d.framework.GraphicsUtils;
import com.j2d.framework.LSystem;

/**
 * 
 * 可定义大小的输入框控件   
 * @author Leijun
 * @version   
 *       1.0.0, Jan 17, 2013 11:50:40 AM
 */
public class SizeableEdit extends ComponentAdapter {
	//彩色输入框的4个角
	private static final BufferedImage imgLeftTop=LSystem.imsLoader.getImage("edit_left_top_",0);
	private static final BufferedImage imgLeftBottom=LSystem.imsLoader.getImage("edit_left_bottom_0");
	private static final BufferedImage imgRightTop=LSystem.imsLoader.getImage("edit_right_top_",0);
	private static final BufferedImage imgRightBottom=LSystem.imsLoader.getImage("edit_right_bottom_0");
	//彩色输入框的各条边的颜色定义
	private static final Color c_top_main=new Color(255,154,33);//编辑框主色
	//private static final Color c_top_shadow=new Color(255,207,148);//上阴影1px
	//private static final Color c_left_shadow=new Color(250,200,135);//左阴影1px
	private static final Color c_shadow=new Color(255,190,132);//底上1px，底下1px
	
	private static final Color c_half_fix=new Color(255,154,33);//渐变从
	private static final Color c_to=new Color(247,125,8);//渐变到
	
	private static final Color c_bottom_main=new Color(247,121,8);//中2px
	
	//灰色输入框的4个角
	private static final BufferedImage imgLeftTop_2=LSystem.imsLoader.getImage("edit_left_top_",1);//7*10
	private static final BufferedImage imgTop_2=LSystem.imsLoader.getImage("edit_top_1");//1*7
	private static final BufferedImage imgRightTop_2=LSystem.imsLoader.getImage("edit_right_top_",1);//7*10
	//灰色输入框的各条边的颜色定义
	
	private static final Color c_left_2=new Color(231,231,231);//左1px
	private static final Color c_right_2=new Color(214,211,214);//右1px
	private static final Color c_bottom_2=new Color(239,239,239);//下1px
	private static final Color c_shadow_2=new Color(255,251,255);
	
	private static final int C_TEXT_SPACE=8;	//框中的文字缩进
	private static final int C_CURSOR_SPACE=10; //光标距离输入框顶部的距离
	private static final int C_TITLE_SPACE=10;	//标题距离输入框顶部的距离
	
	private int id;	//在数组中的编号
	
	private FontMetrics fm=null;
	
	private String title;//输入框标题
	private Font titleFont=null;//标题字体
	private Color titleFontColor=null;//标题文字颜色
	
	private String hint;//输入框中没有字符时的提示文本
	private Font hintFont=null;//提示文本字体
	private Color hintFontColor=null;//提示文字颜色
	
	private int MinLength=0;//最小文字长度(0为不限制)
	private int MaxLength=0;//最大文字长度限制（0为不限制）
	
	private int cursorX;//用户左键点击在在屏幕的X坐标
	private boolean cursorXChanged=false;//光标位置是否变更
	private int realCursorX;//闪烁光标准确的位置
	private int position=0;//插入文字在label串中的位置
	private boolean cursor;//光标显示与否
	private BufferedImage focusImg,unFocusImg;//聚焦、未聚焦时的图片
	private boolean password;//是否为密码输入框
	private StringBuilder sbLabel;//输入的文字
	private boolean onlyNumber=false;//是否仅数字输入
	
	private static final AlphaComposite trans_half = AlphaComposite	//半透明
		.getInstance(AlphaComposite.SRC_OVER, 0.5f);
	private static final AlphaComposite trans_none = AlphaComposite	//不透明
		.getInstance(AlphaComposite.SRC_OVER, 1.0f);
	
	private GraphicsConfiguration gc;
	
	public SizeableEdit(int id,int x,int y, int width,int height,String label,Font font,Color fontColor,Font titleFont, Color titleFontColor){
		super(x, y, width, height);
		this.id=id;
		this.sbLabel=new StringBuilder();
		setLabel(label);
		this.font=font;
		this.fontColor=fontColor;
		this.titleFont=titleFont;
		this.titleFontColor=titleFontColor;
		this.password=false;
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
	    
		realCursorX=this.x+C_TEXT_SPACE;
		focusImg=getFocusedImage();
		unFocusImg=getUnFocusedImage();
	}
	
	@Override
	public void setWidth(int width) {
		super.setWidth(width);
		focusImg=getFocusedImage();
		unFocusImg=getUnFocusedImage();
	}
	
	@Override
	public void setHeight(int height) {
		super.setHeight(height);
		focusImg=getFocusedImage();
		unFocusImg=getUnFocusedImage();
	}
	
	/**
	 * 绘制聚焦状态的输入框
	 * @param g2d
	 * @param text
	 * @author Leijun
	 */
	private BufferedImage getFocusedImage(){
		int transparency = imgLeftTop.getColorModel().getTransparency();
		
		BufferedImage img=gc.createCompatibleImage(this.width, this.height, transparency);
		Graphics2D tmpG2d=img.createGraphics();
		try{
			//先画左，上，右3条边的主色
			tmpG2d.setColor(c_top_main);
			tmpG2d.fillRect(3, 1, this.width-3-3, 2);//上
			
			//左、右边上半固定色
			tmpG2d.setColor(c_half_fix);
			int up_half_len=(this.height-3-4)/2-2;
			//int down_half_len=(height-3-4)-up_half_len;
			//左、右边下半渐变色
			GradientPaint gPat=new GradientPaint(0f,(float)(3+up_half_len),c_half_fix,0f,(float)(this.height-4),c_to);
			tmpG2d.setPaint(gPat);
			
			tmpG2d.fillRect(0, 3, 2, this.height-3-4);//左上半部
			tmpG2d.fillRect(this.width-3, 3, 3, this.height-3-4);//右上半部
			tmpG2d.setPaint(null);
			
			//再画左、上边的阴影线
			tmpG2d.setColor(c_shadow);
			tmpG2d.drawLine(2, 3, 2, this.height-4);//左
			tmpG2d.drawLine(3, 0, this.width-3, 0);//上
	
			//再画下边
			tmpG2d.setComposite(trans_half);//半透明
			tmpG2d.drawLine(3, this.height-4, this.width-3, this.height-4);//下1
			tmpG2d.drawLine(3, this.height-1, this.width-3, this.height-1);//下2
			tmpG2d.setComposite(trans_none);//不透明
			tmpG2d.setColor(c_bottom_main);//
			tmpG2d.fillRect(3, this.height-3, this.width-3-3, 2);//下边主色
			
			//再4个角贴图
			tmpG2d.drawImage(imgLeftTop, 0, 0, null);//左上角
			tmpG2d.drawImage(imgLeftBottom, 0, this.height-4, null);//左下角
			tmpG2d.drawImage(imgRightTop, this.width-3, 0, null);//右上角
			tmpG2d.drawImage(imgRightBottom, this.width-3, this.height-4, null);//右下角
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			tmpG2d.dispose();
		}
		return img;
	}
	
	/**
	 * 绘制未聚焦的输入框
	 * @return
	 * @author Leijun
	 */
	private BufferedImage getUnFocusedImage(){
		int transparency = imgLeftTop.getColorModel().getTransparency();
		
		BufferedImage img = gc.createCompatibleImage(this.width, this.height, transparency);
		Graphics2D tmpG2d=img.createGraphics();
		try{
			//画左边
			tmpG2d.setColor(c_left_2);
			tmpG2d.drawLine(0, 10, 0, this.height-1);
			tmpG2d.setColor(c_shadow_2);
			tmpG2d.fillRect(1, 10, 2, this.height-10-1);
			
			//画右边
			tmpG2d.setColor(c_right_2);
			tmpG2d.drawLine(this.width-1, 10, this.width-1, this.height-1);
			tmpG2d.setColor(c_shadow_2);
			tmpG2d.fillRect(this.width-3, 10, 2, this.height-10-1);
			
			//画底边
			tmpG2d.setColor(c_bottom_2);
			tmpG2d.drawLine(0, this.height-1, this.width, this.height-1);
			
			//3个角贴图
			tmpG2d.drawImage(imgLeftTop_2, 0, 0, null);//左上角
			tmpG2d.drawImage(imgRightTop_2, width-imgRightTop_2.getWidth(), 0, null);//右上角
			tmpG2d.drawImage(imgTop_2, imgLeftTop_2.getWidth(), 0, this.width-imgLeftTop_2.getWidth()-imgRightTop_2.getWidth(), imgTop_2.getHeight(), null);//顶部拉伸图片
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			tmpG2d.dispose();
			tmpG2d=null;
		}
		return img;
	}
	
	/**
	 * 画未聚焦的输入框
	 * @param g2d
	 * @author Leijun
	 */
	private void drawUnFocus(Graphics2D g2d){
		g2d.drawImage(this.unFocusImg, this.x, this.y, null);
		if(this.sbLabel.length()>0){
			int fontH = fm.getAscent()-fm.getDescent();	//.stringWidth(this.text);
			if(this.fontColor!=null)g2d.setColor(this.fontColor);
			if(this.font!=null)g2d.setFont(this.font);
			GraphicsUtils.setAntialias(g2d,true);//抗锯齿
			String text;
			if(!isPassword()){
				text=this.getLabel();
			}else{
				text=getFixedLength('*',this.getLabel().length());
			}
			g2d.drawString(text, x+C_TEXT_SPACE, y + ((this.height+fontH)/2) );//this.text, x+8, y+(this.height-fontH)/2
			GraphicsUtils.setAntialias(g2d,false);
		}else if((null!=hint)&&(!hint.equals(""))){//Hint
			FontMetrics fm=g2d.getFontMetrics(this.hintFont);
			int fontH = fm.getAscent()+fm.getDescent();	//.stringWidth(this.text);
			if(this.hintFontColor!=null)g2d.setColor(this.hintFontColor);
			if(this.hintFont!=null)g2d.setFont(this.hintFont);
			GraphicsUtils.setAntialias(g2d,true);//抗锯齿
			g2d.drawString(this.hint, x+C_TEXT_SPACE, y + ((this.height+fontH)/2) );
			GraphicsUtils.setAntialias(g2d,false);
		}
		
	}
	
	private String getFixedLength(char c,int len){
		StringBuilder sb=new StringBuilder();
		if(len<=0)return null;
		for(int i=0;i<len;i++){
			sb.append(c);
		}
		return sb.toString();
	}
	
	/**
	 * 绘制已聚焦的输入框
	 * @param g2d
	 * @author Leijun
	 */
	private void drawFocus(Graphics2D g2d){
		g2d.drawImage(this.focusImg, this.x, this.y, null);
		
		
		if(this.sbLabel.length()>0){
			
			int fontH = fm.getAscent()-fm.getDescent();	//.stringWidth(this.text);
			g2d.setColor(this.fontColor);
			g2d.setFont(this.font);
			GraphicsUtils.setAntialias(g2d,true);//抗锯齿		
			String text;
			if(!isPassword()){
				text=this.getLabel();
			}else{
				text=getFixedLength('*',this.getLabel().length());
			}
			g2d.drawString(text, this.x+C_TEXT_SPACE, this.y+this.height/2+fontH/2);//String.valueOf(fm.getAscent())+"|"+String.valueOf(fm.getDescent())+"|"+String.valueOf(fm.getLeading())
			GraphicsUtils.setAntialias(g2d,false);
			
			if(this.cursorXChanged){
				try{
					//if((this.cursorX > this.x+C_TEXT_SPACE)&&(this.cursorX < x+this.width)){//光标位置在控件范围内
						int len=text.length();
						int[] ws=new int[len];
						for(int i=0;i<len;i++){
							ws[i]=fm.stringWidth(text.substring(0, i+1));
						}
						
						for(int i=0;i<len;i++){
							if(this.cursorX<=this.x+C_TEXT_SPACE+ws[i]){
								realCursorX=this.x+C_TEXT_SPACE+ws[i];
								position=i+1;
								if(i>0){
									if((this.cursorX<=this.x+C_TEXT_SPACE+ws[i-1]+(ws[i]-ws[i-1])/2)){//是否超过了半个字
										realCursorX=this.x+C_TEXT_SPACE+ws[i-1];
										position=i;
									}
								}else{
									if(this.cursorX<=this.x+C_TEXT_SPACE+ws[0]/2){
										realCursorX=this.x+C_TEXT_SPACE;
										position=0;
									}
								}
								
								break;
							}
							if(i==len-1){//最后一个字符了
								realCursorX=this.x+C_TEXT_SPACE+ws[i];
								position=i+1;
							}
						}
						
					//}
				}
				finally {
					this.cursorXChanged=false;//光标变更处理完毕
				}
			}
		}else{
			realCursorX=this.x+C_TEXT_SPACE;
		}
		
		//画光标
		if(this.cursor){
			g2d.setColor(Color.black);
			g2d.drawLine(realCursorX+1, this.y+C_CURSOR_SPACE, realCursorX+1, this.y+C_CURSOR_SPACE+(this.height-2*C_CURSOR_SPACE));
		}
		
		//g2d.setColor(Color.black);
		//g2d.drawString(String.valueOf(position), 300, 300);
	}
	
	@Override
	public void draw(Graphics2D g2d){
		if (!this.visable) {
			return;
		}
		if(fm==null){
			fm=g2d.getFontMetrics(this.font);
		}
		//是否有标题
		if( (this.title!=null) && (!this.title.equals("")) && (this.titleFont!=null) && (this.titleFontColor!=null) ){
			g2d.setFont(this.titleFont);
			g2d.setColor(this.titleFontColor);
			//GraphicsUtils.setAntialias(g2d,true);//抗锯齿
			RenderingHints rh=g2d.getRenderingHints();
			GraphicsUtils.setRenderingHints(g2d);
			g2d.drawString(this.title, this.x, this.y-C_TITLE_SPACE);
			g2d.setRenderingHints(rh);
			//GraphicsUtils.setAntialias(g2d,false);
		}
		if(!this.isActive()){//是否为激活状态
			drawUnFocus(g2d);
		}else{
			drawFocus(g2d);
		}
	}
	
	/**
	 * “横排”初始化多个输入框
	 */
	public static void initializeRows(final SizeableEdit[] edits,final int firstX, final int firstY,
			final int width, final int height, final int space, 
			final Font font, final Color fontColor,final Font titleFont, final Color titleFontColor){
		for (int i = 0; i < edits.length; i++) {
			edits[i]=new SizeableEdit(i,firstX+i*space,firstY,width,height,null,font,fontColor,titleFont,titleFontColor);
		}
	}
	
	/**
	 * “竖排”初始化多个输入框
	 */
	public static void initializeColumns(final SizeableEdit[] edits,final int firstX, final int firstY,
			final int width, final int height,final int space, 
			final Font font, final Color fontColor,final Font titleFont, final Color titleFontColor){
		for (int i = 0; i < edits.length; i++) {
			edits[i]=new SizeableEdit(i,firstX,firstY+i*space,width,height,null,font,fontColor,titleFont,titleFontColor);
		}
	}
	
	/**
	 * 鼠标左键是否点击了
	 * @return
	 * @author Leijun
	 */
	/*@Override
	public boolean isLeftMouseClicked() {
		if(!visable){//按钮不可见
			return false;
		}
		if(AbstractControler.leftPressed){//左键按下了还没松开
			return false;
		}
		
		if((AbstractControler.leftPressPoint!=null)&&(AbstractControler.leftReleasePoint!=null)){//按下点 和 释放点都不为null
			try{
				if ( AbstractControler.leftPressPoint.x > this.x && AbstractControler.leftReleasePoint.x > this.x
						&& AbstractControler.leftPressPoint.x < this.x + this.width && AbstractControler.leftReleasePoint.x < this.x + this.width
						&& AbstractControler.leftPressPoint.y > this.y && AbstractControler.leftReleasePoint.y > this.y
						&& AbstractControler.leftPressPoint.y < this.y + this.height && AbstractControler.leftReleasePoint.y < this.y + this.height
					)
				{
					//记录点击时的坐标
					setCursorX(AbstractControler.leftPressPoint.x);
					//本次处理完了之后重置按下和释放状态
					AbstractControler.leftPressPoint=null;
					AbstractControler.leftReleasePoint=null;
					return true;
				} else {
					return false;
				}
			}catch (Exception e) {
				// TODO: handle exception
				return false;
			}
		}
		else{
			return false;
		}
		
		//return this.leftMousePressed;
	}*/

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public int getMaxLength() {
		return MaxLength;
	}

	public void setMaxLength(int maxLength) {
		MaxLength = maxLength;
	}

	/*public int getCursorX() {
		return cursorX;
	}*/

	public void setCursorX(int cursorX) {
		this.cursorX = cursorX;
		this.cursorXChanged=true;
	}

	public void setCursor(boolean cursor) {
		this.cursor = cursor;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Font getTitleFont() {
		return titleFont;
	}

	public void setTitleFont(Font titleFont) {
		this.titleFont = titleFont;
	}

	public Color getTitleFontColor() {
		return titleFontColor;
	}

	public void setTitleFontColor(Color titleFontColor) {
		this.titleFontColor = titleFontColor;
	}

	public Font getHintFont() {
		return hintFont;
	}

	public void setHintFont(Font hintFont) {
		this.hintFont = hintFont;
	}

	public Color getHintFontColor() {
		return hintFontColor;
	}

	public void setHintFontColor(Color hintFontColor) {
		this.hintFontColor = hintFontColor;
	}

	public int getId() {
		return id;
	}

	public int getPosition() {
		return position;
	}
	
	@Override
	public void leftMousePressed(Point p){
		//点击位置坐标
		setCursorX(p.x);
	}
	
	@Override
	public void keyPressed(int keyCode, char keyChar){
		//System.out.println(""+(int)keyChar+"|"+keyCode);
		
		//if(keyChar==0xffff)return;//无效键
		if(keyCode==8){//退格键
			if(this.position>0)//如果在头位置则啥都不干
			{
				//修正文字插入位置
				this.position--;
				
				//删除一个字符
				char c=sbLabel.charAt(this.position);
				sbLabel.deleteCharAt(this.position);
				
				//修正光标
				if(fm!=null){
					this.realCursorX-=fm.stringWidth(String.valueOf(c));
				}
				
			}
		}
		else if(keyCode==KeyEvent.VK_CONTROL){//ctrl键清空
			if(this.position>0)//如果在头位置则啥都不干
			{
				//修正文字插入位置
				this.position=0;
				
				//清空字符
				sbLabel.delete(0, sbLabel.length());
				
				//修正光标
				this.realCursorX=this.x+C_TEXT_SPACE;
				
			}
		}
		else if(keyCode==10){//回车键
			//setLabel("Enter");
		}
		else if(keyChar==0xffff){
			return;
		}
		else if( (this.MaxLength>0 && this.getLabel().length()>=this.MaxLength) || 
				(fm.stringWidth(this.getLabel())>=this.width-3*C_TEXT_SPACE) ){//最大长度判断
			return;
		}
		else if( (keyChar>='a' && keyChar<='z') || (keyChar>='A' && keyChar<='Z') || 
				(keyChar>='0' && keyChar<='9') || (keyChar=='*') || (keyChar=='#') ){
			if(isOnlyNumber() && ((keyChar<'0') || (keyChar>'9')))return;//仅接受数字，而输入的又非数字
			//插入字符
			this.sbLabel.insert(this.position, keyChar);
			//修正文字插入位置
			this.position++;
			//修正光标
			if(fm!=null){
				this.realCursorX+=fm.stringWidth(String.valueOf(keyChar));
			}
			
		}
	}
	
	@Override
	public String getLabel() {
		return sbLabel.toString();
	}

	@Override
	public void setLabel(String label) {
		this.sbLabel.delete(0, this.sbLabel.length());//先清空
		if((null!=label)&&(!label.equals("")))//
		{
			this.sbLabel.append(label);
		}
	}

	public int getMinLength() {
		return MinLength;
	}

	public void setMinLength(int minLength) {
		MinLength = minLength;
	}

	public boolean isPassword() {
		return password;
	}

	public void setPassword(boolean password) {
		this.password = password;
	}

	public boolean isOnlyNumber() {
		return onlyNumber;
	}

	/**
	 * 是否仅支持数字
	 * @param onlyNumber
	 * @author Leijun
	 */
	public void setOnlyNumber(boolean onlyNumber) {
		this.onlyNumber = onlyNumber;
	}
	
}
