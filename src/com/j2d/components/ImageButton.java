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

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.ImageObserver;
import java.awt.image.Kernel;

import com.j2d.controlers.AbstractControler;
import com.j2d.framework.GraphicsUtils;

/**
 * 图片按钮，可按“横排”和“竖排”2种不同的排列方式连续生成风格一致的多个按钮   
 * @author Leijun
 * @version   
 *       1.0.0, Jan 11, 2013 4:41:20 PM
 */
public class ImageButton extends ComponentAdapter {

	private int id;//按钮在数组中的序号

	//private BufferedImage unFocusImage;//按钮未获得焦点时的图片

	private BufferedImage normalImg,normalImg_bak;//一般图片
	private BufferedImage mouseDownImg,mouseDownImg_bak;//鼠标按下时的图片
	private BufferedImage grayImg;//disabled状态时的图片
	
	//模糊效果的一个参数，需要为奇数
	private int oddSize=15;
	
	private boolean blurringEffect=false;//是否开启模糊效果

	public ImageButton(final int no, final int firstX, final int firstY, final int space, final boolean isRow,
			final BufferedImage normalImg, final BufferedImage mouseDownImg,
			final Font font, final Color color) {
		super(0,0,0,0);
		this.id = no;
		//this.unFocusImage = unFocusedImage;
		this.normalImg = normalImg;
		this.mouseDownImg=mouseDownImg;
		this.normalImg_bak = normalImg;
		this.mouseDownImg_bak=mouseDownImg;
		this.grayImg=GraphicsUtils.getGray(normalImg);
		this.font=font;
		this.fontColor=color;
		this.visable = true;
		//this.label = ("button" + no).intern();
		this.width = normalImg.getWidth(null);
		this.height = normalImg.getHeight(null);
		if (isRow) {//横排
			//this.x = space;
			//this.y = this.width * id + space;
			this.x=firstX + (this.width+space)*no;
			this.y=firstY;
		} else {//竖排
			//this.x = this.height * id + space;
			//this.y = this.height;
			this.x=firstX;
			this.y=firstY + (this.height+space)*no;
		}
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		if(!enabled){//显示灰度图片
			this.normalImg=this.grayImg;
			this.mouseDownImg=this.grayImg;
		}else{//还原图片
			this.normalImg=this.normalImg_bak;
			this.mouseDownImg=this.mouseDownImg_bak;
		}
		super.setEnabled(enabled);
	}

	public void setDrawXY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/*public Image getSelectImage() {
		return selectImage;
	}*/

	/*private void draw(final Graphics2D g) {
		this.draw(g, null, 0, 0);
	}*/
	
	public void drawBlurredImage(Graphics2D g2d, BufferedImage im, int x,
			int y, int size)
	/*
	 * The size argument is used to specify a size*size blur kernel, filled with
	 * 1/(size*size) values.
	 * 
	 * Size should be odd so that the center cell of the kernel corresponds to
	 * the coordinate being blurred.
	 * 
	 * The larger the size value, the larger the kernel, and more blurry the
	 * resulting image.
	 * 
	 * An issue is the edge effects, which will produce a nasty black border or
	 * a border with no bluriness, depending on which ConvolveOp EDGE constant
	 * is used.
	 */
	{
		if (im == null) {
			System.out.println("getBlurredImage: input image is null");
			return;
		}
		//int imWidth = im.getWidth();
		//int imHeight = im.getHeight();
		int maxSize = (width > height) ? width : height;

		if ((maxSize % 2) == 0) // if even
			maxSize--; // make it odd

		if ((size % 2) == 0) { // if even
			size++; // make it odd
			System.out.println("Blur size must be odd; adding 1 to make size = "+ size);
		}

		if (size < 3) {
			System.out.println("Minimum blur size is 3");
			size = 3;
		} else if (size > maxSize) {
			System.out.println("Maximum blur size is " + maxSize);
			size = maxSize;
		}

		// create the blur kernel
		int numCoords = size * size;
		float blurFactor = 1.0f / (float) numCoords;

		float[] blurKernel = new float[numCoords];
		for (int i = 0; i < numCoords; i++)
			blurKernel[i] = blurFactor;

		ConvolveOp blurringOp = new ConvolveOp(new Kernel(size, size,
				blurKernel), ConvolveOp.EDGE_NO_OP, null); // leaves edges
															// unaffected
		// ConvolveOp.EDGE_ZERO_FILL, null);
		// edges are filled with black

		g2d.drawImage(im, blurringOp, x, y);
	} // end of drawBlurredImage() with size argument

	
	private void drawImage(Graphics2D g2d, BufferedImage img, int x, int y, int width, int height, ImageObserver observer){
		if(!isBlurringEffect() || this.oddSize==1){//一般显示
			g2d.drawImage(img,x,y,width,height,observer);
		}else{//模糊效果
			drawBlurredImage(g2d,img,x,y,this.oddSize);
		}
	}

	/*@Override
	public void draw(java.awt.Graphics2D g2d) {
		this.draw(g2d);
	}*/

	/*public void draw(final Graphics g, final String fontName, final int type,
			final int size) {
		this.draw((Graphics2D) g, fontName, type, size);
	}*/
	
	/**
	 * 绘制按钮：自定义按钮字体，文字的Y坐标，文字在X轴上居中显示
	 * @param g
	 * @param font-字体
	 * @param color-文字颜色
	 * @param local_y-文字在图片区域内的Y坐标
	 * @author Leijun
	 */
	public void draw(final Graphics2D g2d, final Font font, final Color color,int local_y) {
		if (!this.visable) {
			return;
		}
		this.font=font;
		this.fontColor=color;
		
		GraphicsUtils.setAlpha(g2d, 0.95f);
		//drawImage(g2d,focusImage, (int) x, (int) y, this.width, this.height,null);
		if (this.active && AbstractControler.leftMouseDown) {//complete || 
			drawImage(g2d,this.mouseDownImg, (int) x, (int) y, this.width, this.height,
					null);
		} else {
			drawImage(g2d,this.normalImg, (int) x, (int) y, this.width, this.height,
					null);
		}
		
		//文字
		if((null!=font)&&(null!=label)&&(!label.equals(""))){
			GraphicsUtils.setAlpha(g2d, 1.0f);
			GraphicsUtils.setRenderingHints(g2d);
			//GraphicsUtils.font(g2d, fontName, size, type);
			g2d.setFont(font);
			int fontSize = g2d.getFontMetrics(font).stringWidth(label);
			g2d.setColor(color);
			GraphicsUtils.setAntialias(g2d,true);//抗锯齿
			g2d.drawString(label, x + this.width/2 - fontSize/2, y+local_y);
			GraphicsUtils.setAntialias(g2d,false);
			/*GraphicsUtils.drawStyleString(g2d, label, (int) x + this.width / 2
					- fontSize / 2, (int) y + this.height / 2 + 5,
					isSelect ? Color.red : Color.black, color);*/
		}
	}
	
	
	/*public void draw(final Graphics g, final String fontName, final int type,
			final int size, final Color color,int local_x,int local_y) {
		this.draw((Graphics2D)g, fontName, type, size, color, local_x, local_y);
	}*/
	
	/**
	 * 根据字体、字体颜色、字体大小及显示坐标的参数来绘制按钮
	 * @param g2d
	 * @param font-字体
	 // @param fontName-字体名称
	 // @param type-字体风格类型(ITALIC or BOLD|ITALIC)
	 // @param size-字体大小
	 * @param color-文字颜色
	 * @param local_x-文字在图片区域内的X坐标
	 * @param local_y-文字在图片区域内的Y坐标
	 * @author Leijun
	 */
	public void draw(final Graphics2D g2d, final Font font, final Color color,int local_x,int local_y) {
		if (!this.visable) {
			return;
		}
		this.font=font;
		this.fontColor=color;
		draw(g2d, local_x,local_y);
	}
	
	/**
	 * 按钮绘制
	 * @param g2d
	 * @param local_x-文字在图片区域内的X坐标
	 * @param local_y-文字在图片区域内的Y坐标
	 * @author Leijun
	 */
	public void draw(final Graphics2D g2d, int local_x,int local_y) {
		if (!this.visable) {
			return;
		}
		GraphicsUtils.setAlpha(g2d, 0.95f);
		//drawImage(g2d,focusImage, (int) x, (int) y, this.width, this.height,null);
		if (this.active && AbstractControler.leftMouseDown) {//complete || 
			drawImage(g2d,this.mouseDownImg, (int) x, (int) y, this.width, this.height,
					null);
		} else {
			drawImage(g2d,this.normalImg, (int) x, (int) y, this.width, this.height,
					null);
		}
		
		//文字
		if((null!=font)&&(null!=label)&&(!label.equals(""))){
			GraphicsUtils.setAlpha(g2d, 1.0f);
			GraphicsUtils.setRenderingHints(g2d);
			//GraphicsUtils.font(g2d, fontName, size, type);
			g2d.setFont(this.font);

			//Font font = g2d.getFont();
			//int fontSize = g2d.getFontMetrics(font).stringWidth(label);
			g2d.setColor(this.fontColor);
			GraphicsUtils.setAntialias(g2d,true);//抗锯齿
			g2d.drawString(label, x+local_x, y+local_y);
			GraphicsUtils.setAntialias(g2d,false);
			/*GraphicsUtils.drawStyleString(g2d, label, (int) x + this.width / 2
					- fontSize / 2, (int) y + this.height / 2 + 5,
					isSelect ? Color.red : Color.black, color);*/
		}
	}

	/**
	 * 绘制标签居中的按钮
	 * @param g2d
	 * @author Leijun
	 */
	@Override
	public void draw(final Graphics2D g2d) {
		if (!this.visable) {
			return;
		}
		GraphicsUtils.setAlpha(g2d, 0.95f);
		//drawImage(g2d,focusImage, (int) x, (int) y, this.width, this.height,null);
		if (this.active && AbstractControler.leftMouseDown) {//complete || 
			drawImage(g2d,this.mouseDownImg, (int) x, (int) y, this.width, this.height,
					null);
		} else {

			drawImage(g2d,this.normalImg, (int) x, (int) y, this.width, this.height,
					null);
		}
		
		//文字
		if((null!=font)&&(null!=label)&&(!label.equals(""))){
			GraphicsUtils.setAlpha(g2d, 1.0f);//不透明
			GraphicsUtils.setRenderingHints(g2d);
			g2d.setFont(font);
			FontMetrics fm=g2d.getFontMetrics(font);
			int fontW = fm.stringWidth(label);
			int fontH = fm.getAscent()-fm.getDescent();

			/*boolean isSelect = (visable) || focus;//complete && 
			if (isSelect) {
				color = Color.white;
			} else {
				color = Color.gray;
			}*/
			
			/*GraphicsUtils.drawStyleString(g2d, label, (int) x + this.width / 2
					- fontW / 2, (int) y + this.height / 2 + 5,
					isActive() ? Color.red : Color.black, fontColor);*/
			g2d.setColor(fontColor);
			g2d.drawString(label, x + this.width/2 - fontW/2, y+(this.height+fontH)/2);
		}
	}

	/*public void setLabel(final String label) {
		//this.click = true;
		this.label = label;
	}*/

	/*public int getClickedId() {
		if (!visable)
			return -1;

		if (isLeftMouseClicked()) {
			return id;
		}

		return -1;
	}*/
	
	/**
	 * 此方法检查且更新按钮的聚焦状态
	 * @return
	 * @author Leijun
	 */
	/*public boolean updateFocus() {
		if (((double) AbstractControler.mouse.x > x
				&& (double) AbstractControler.mouse.x < x + (double) this.width
				&& (double) AbstractControler.mouse.y > y && (double) AbstractControler.mouse.y < y
				+ (double) this.height)) {
			this.focus = true;
		} else {
			this.focus = false;
		}

		return this.focus;
	}*/

	private static void initialize(final ImageButton[] buttons,
			final int firstX, final int firstY, final int space, final boolean isRow, 
			final BufferedImage normalImg, final BufferedImage mouseDownImg,
			final Font font, final Color color) {
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new ImageButton(i, firstX, firstY, space, isRow, normalImg,
					mouseDownImg,font,color);
			//buttons[i].click = false;
			buttons[i].visable = true;
		}
	}

	/**
	 * 按“横排”初始化按钮数组
	 * @param buttons		-按钮数组
	 * @param firstX		-首个按钮的X坐标
	 * @param firstY		-首个按钮的Y坐标
	 * @param space			-按钮间的水平间隔
	 * @param normalImg		-正常显示时的图片
	 * @param mouseDownImg	-鼠标按下时显示的图片
	 * @param font			-按钮上的文字字体
	 * @param color			-按钮上的文字颜色
	 * @author Leijun
	 */
	public static void initializeRows(final ImageButton[] buttons,
			final int firstX, final int firstY, final int space, 
			final BufferedImage normalImg, final BufferedImage mouseDownImg,
			final Font font, final Color color) {
		ImageButton.initialize(buttons, firstX, firstY, space, true,
				normalImg,
				mouseDownImg == null ? normalImg : mouseDownImg,
				font,color);
	}
	
	/**
	 * 按“竖排”初始化按钮数组
	 * @param buttons		-按钮数组
	 * @param firstX		-首个按钮的X坐标
	 * @param firstY		-首个按钮的Y坐标
	 * @param space			-按钮间的垂直间隔
	 * @param normalImg		-正常显示时的图片
	 * @param mouseDownImg	-鼠标按下时显示的图片
	 * @param font			-按钮上的文字字体
	 * @param color			-按钮上的文字颜色
	 * @author Leijun
	 */
	public static void initializeColumns(final ImageButton[] buttons,
			final int firstX, final int firstY, final int space, 
			final BufferedImage normalImg, final BufferedImage mouseDownImg,
			final Font font, final Color color) {
			ImageButton.initialize(buttons, firstX, firstY, space, false,
					normalImg,
					mouseDownImg == null ? normalImg : mouseDownImg,
					font,color);
	}

	/**
	 * 检查是否所有按钮都未聚焦
	 * @param buttons
	 * @return
	 * @author Leijun
	 */
	/*public static boolean isAllUnFocused(final ImageButton[] buttons) {
		boolean result;
		for (int i = 0; i < buttons.length; i++) {
			result = buttons[i].isFocus();
			if (result) {
				return false;
			}
		}
		return true;
	}*/

	/*public void setUnFocusImage(BufferedImage unFocusImage) {
		this.unFocusImage = unFocusImage;
	}*/

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isBlurringEffect() {
		return blurringEffect;
	}

	public void setBlurringEffect(boolean blurringEffect) {
		this.blurringEffect = blurringEffect;
	}

	public int getOddSize() {
		return oddSize;
	}
	
	/**
	 * 递减oddSize的值，如到1则不再递减
	 * @author Leijun
	 */
	public void nextOddSize(){
		if(oddSize>1){
			oddSize-=2;
		}else{
			oddSize=1;
		}
	}

	public void setOddSize(int oddSize) {
		this.oddSize = oddSize;
	}
	
	@Override
	public void leftMousePressed(Point p){
		
	}
	
	@Override
	public void keyPressed(int keyCode, char keyChar){
		
	}

	public void setNormalImg(BufferedImage normalImg) {
		this.normalImg = normalImg;
		this.grayImg=GraphicsUtils.getGray(normalImg);
		this.width=normalImg.getWidth();
	}
	
	public void setMouseDownImg(BufferedImage mouseDownImg) {
		this.mouseDownImg = mouseDownImg;
	}

}
