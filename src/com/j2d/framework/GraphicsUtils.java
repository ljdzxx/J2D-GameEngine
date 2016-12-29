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

package com.j2d.framework;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ImageProducer;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.WeakHashMap;


public class GraphicsUtils {

	final static public Toolkit toolKit = Toolkit.getDefaultToolkit();

	final static private MediaTracker mediaTracker = new MediaTracker(
			new Container());

	final static private Map<String,java.awt.Image> cacheImages = new WeakHashMap<String,java.awt.Image>(100);

	final static private ClassLoader classLoader = Thread.currentThread()
			.getContextClassLoader();

	final static String newLine = "\r\n";
	
	final static private GraphicsConfiguration gc;
	
	//final static private ImageSFXs imageSfx=new ImageSFXs();

	final static RenderingHints hints;
	
	final static Stroke defaultStroke=new BasicStroke();
	
	static {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
		// 设定图像显示状�??
		hints = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		hints.put(RenderingHints.KEY_DITHERING,
				RenderingHints.VALUE_DITHER_ENABLE);
		hints.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_SPEED);
		hints.put(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		hints.put(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		hints.put(RenderingHints.KEY_COLOR_RENDERING,
				RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		hints.put(RenderingHints.KEY_DITHERING,
				RenderingHints.VALUE_DITHER_DISABLE);
	}

	private GraphicsUtils() {
	}

	public static Font getFont() {
		return getFont(LSystem.FONT, LSystem.FONT_TYPE);
	}

	public static Font getFont(int size) {
		return getFont(LSystem.FONT, size);
	}

	public static Font getFont(String fontName, int size) {
		return getFont(fontName, 0, size);
	}

	public static Font getFont(String fontName, int style, int size) {
		return new Font(fontName, style, size);
	}
	
	/**
	 * 获取文字宽度
	 * @param fm
	 * @param str
	 * @return
	 * @author Leijun
	 */
	public static int getFontWidth(FontMetrics fm, String str){
		return fm.stringWidth(str);
	}
	
	/**
	 * 取文字高度
	 * @param fm
	 * @return
	 * @author Leijun
	 */
	public static int getFontHeight(FontMetrics fm){
		return fm.getAscent()-fm.getDescent();
	}

	public static void drawStyleString(final Graphics2D g2d,
			final String message, final int x, final int y, final Color boardColor,
			final Color labelColor) {

		g2d.setColor(boardColor);
		g2d.drawString(message, x + 1, y);
		g2d.drawString(message, x - 1, y);
		g2d.drawString(message, x, y + 1);
		g2d.drawString(message, x, y - 1);
		g2d.setColor(labelColor);
		g2d.drawString(message, x, y);

	}

	final static public int[] getPixels(final Image img) {
		int width = img.getWidth(null);
		int height = img.getHeight(null);
		if (width < 1 || height < 1) {
			return null;
		}
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_BGR);
		if (image == null) {
			return null;
		}
		image.getGraphics().drawImage(img, 0, 0, null);
		int[] data = new int[2 + width * height];
		if (data == null) {
			return null;
		}
		data[0] = width;
		data[1] = height;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				data[2 + j + i * width] = image.getRGB(j, i);
			}
		}
		return data;
	}

	final static public BufferedImage getImage(int[] data) {
		if (data == null || data.length < 3 || data[0] < 1 || data[1] < 1) {
			return null;
		}
		int width = data[0];
		int height = data[1];
		if (data.length < 2 + width * height) {
			return null;
		}
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_BGR);
		if (image == null) {
			return null;
		}
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				image.setRGB(j, i, data[2 + j + i * width]);
			}
		}
		return image;
	}

	public static Image getResize(Image bufferedimage, int w, int h) {
		BufferedImage result = null;
		Graphics2D graphics2d;
		(graphics2d = (result = GraphicsUtils.createImage(w, h, true))
				.createGraphics()).setRenderingHint(
				RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2d.drawImage(bufferedimage, 0, 0, w, h, 0, 0, bufferedimage
				.getWidth(null), bufferedimage.getHeight(null), null);
		graphics2d.dispose();
		return result;
	}

	final static private RenderingHints VALUE_TEXT_ANTIALIAS_ON = new RenderingHints(
			RenderingHints.KEY_TEXT_ANTIALIASING,
			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

	final static private RenderingHints VALUE_TEXT_ANTIALIAS_OFF = new RenderingHints(
			RenderingHints.KEY_TEXT_ANTIALIASING,
			RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

	public static void setAntialias(Graphics g, boolean flag) {
		if (flag) {
			((Graphics2D) g).setRenderingHints(VALUE_TEXT_ANTIALIAS_ON);
		} else {
			((Graphics2D) g).setRenderingHints(VALUE_TEXT_ANTIALIAS_OFF);
		}
	}
	
	final static private RenderingHints VALUE_RENDER_QUALITY = new RenderingHints(
			RenderingHints.KEY_RENDERING,
			RenderingHints.VALUE_RENDER_QUALITY);

	final static private RenderingHints VALUE_RENDER_SPEED = new RenderingHints(
			RenderingHints.KEY_RENDERING,
			RenderingHints.VALUE_RENDER_SPEED);

	public static void setRenderQuality(Graphics g, boolean flag) {
		if (flag) {
			((Graphics2D) g).setRenderingHints(VALUE_RENDER_QUALITY);
		} else {
			((Graphics2D) g).setRenderingHints(VALUE_RENDER_SPEED);
		}
	}


	public static Image[] getSplitImages(String fileName, int row, int col) {
		return getSplitImages(fileName, row, col, true);
	}

	/**
	 * 分解整图为图片数�?
	 * 
	 * @param fileName
	 * @param row
	 * @param col
	 * @return
	 */
	public static Image[] getSplitImages(String fileName, int row, int col,
			boolean isFiltrate) {
		Image image = GraphicsUtils.loadImage(fileName);
		return getSplitImages(image, row, col, isFiltrate);
	}

	/**
	 * 分割指定图像为image[]
	 * 
	 * @param image
	 * @param row
	 * @param col
	 * @return
	 */
	public static Image[] getSplitImages(Image image, int row, int col,
			boolean isFiltrate) {
		int index = 0;
		int wlenght = image.getWidth(null) / row;
		int hlenght = image.getHeight(null) / col;
		int l = wlenght * hlenght;
		Image[] abufferedimage = new Image[l];
		for (int y = 0; y < hlenght; y++) {
			for (int x = 0; x < wlenght; x++) {
				abufferedimage[index] = GraphicsUtils.createImage(row, col, true);
				Graphics g = abufferedimage[index].getGraphics();
				g.drawImage(image, 0, 0, row, col, (x * row), (y * col), row
						+ (x * row), col + (y * col), null);
				g.dispose();
				// 透明化处�?
				PixelGrabber pgr = new PixelGrabber(abufferedimage[index], 0,
						0, -1, -1, true);
				try {
					pgr.grabPixels();
				} catch (InterruptedException ex) {
					ex.getStackTrace();
				}
				int pixels[] = (int[]) pgr.getPixels();
				if (isFiltrate) {
					// 循环像素
					for (int i = 0; i < pixels.length; i++) {
						// 去色
						LColor color = LColor.getLColor(pixels[i]);
						if ((color.R == 247 && color.G == 0 && color.B == 255)
								|| (color.R == 255 && color.G == 255 && color.B == 255)) {
							// 透明�?
							pixels[i] = 0;
						}
					}
				}
				ImageProducer ip = new MemoryImageSource(pgr.getWidth(), pgr
						.getHeight(), pixels, 0, pgr.getWidth());
				abufferedimage[index] = toolKit.createImage(ip);
				index++;
			}
		}
		return abufferedimage;
	}

	/**
	 * 分解整图为图片数�?
	 * 
	 * @param fileName
	 * @param row
	 * @param col
	 * @return
	 */
	public static Image[][] getSplit2Images(String fileName, int row, int col,
			boolean isFiltrate) {
		Image image = GraphicsUtils.loadImage(fileName);
		return getSplit2Images(image, row, col, isFiltrate);
	}

	public static Image[][] getSplit2Images(String fileName, int row, int col) {
		Image image = GraphicsUtils.loadImage(fileName);
		return getSplit2Images(image, row, col, true);
	}

	/**
	 * 分割指定图像为image[]
	 * 
	 * @param image
	 * @param row
	 * @param col
	 * @return
	 */
	public static Image[][] getSplit2Images(Image image, int row, int col,
			boolean isFiltrate) {
		int wlenght = image.getWidth(null) / row;
		int hlenght = image.getHeight(null) / col;
		Image[][] abufferedimage = new Image[row][col];
		for (int y = 0; y < hlenght; y++) {
			for (int x = 0; x < wlenght; x++) {
				abufferedimage[x][y] = GraphicsUtils.createImage(row, col, true);
				Graphics g = abufferedimage[x][y].getGraphics();
				g.drawImage(image, 0, 0, row, col, (x * row), (y * col), row
						+ (x * row), col + (y * col), null);
				g.dispose();
				// 透明化处�?
				PixelGrabber pgr = new PixelGrabber(abufferedimage[x][y], 0, 0,
						-1, -1, true);
				try {
					pgr.grabPixels();
				} catch (InterruptedException ex) {
					ex.getStackTrace();
				}
				int pixels[] = (int[]) pgr.getPixels();
				if (isFiltrate) {
					// 循环像素
					for (int i = 0; i < pixels.length; i++) {
						// 去色
						LColor color = LColor.getLColor(pixels[i]);
						if ((color.R == 247 && color.G == 0 && color.B == 255)
								|| (color.R == 255 && color.G == 0 && color.B == 255)
								|| (color.R == 0 && color.G == 0 && color.B == 0)) {
							// 透明�?
							pixels[i] = 0;
						}
					}
				}
				ImageProducer ip = new MemoryImageSource(pgr.getWidth(), pgr
						.getHeight(), pixels, 0, pgr.getWidth());
				abufferedimage[x][y] = toolKit.createImage(ip);
			}
		}
		return abufferedimage;
	}
	
	public static Image[][] getFlipHorizintalImage2D(Image[][] pixels) {
		int w = pixels.length;
		int h = pixels[0].length;
		Image pixel[][] = new Image[h][w];
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				pixel[i][j] = pixels[j][i];
			}
		}
		return pixel;
	}
	
	
	public static Image drawClipImage(final Image image, int objectWidth,
			int objectHeight, int x1, int y1, int x2, int y2) {
		BufferedImage buffer = GraphicsUtils.createImage(objectWidth,
				objectHeight, true);
		Graphics g = buffer.getGraphics();
		Graphics2D graphics2D = (Graphics2D) g;
		graphics2D.drawImage(image, 0, 0, objectWidth, objectHeight, x1, y1,
				x2, y2, null);
		graphics2D.dispose();
		graphics2D = null;
		return buffer;
	}

	public static Image drawClipImage(final Image image, int objectWidth,
			int objectHeight, int x, int y) {
		BufferedImage buffer = GraphicsUtils.createImage(objectWidth,
				objectHeight, true);
		Graphics g = buffer.getGraphics();
		Graphics2D graphics2D = (Graphics2D) g;
		graphics2D.drawImage(image, 0, 0, objectWidth, objectHeight, x, y, x
				+ objectWidth, objectHeight + y, null);
		graphics2D.dispose();
		graphics2D = null;
		return buffer;
	}

	/**
	 * 水平翻转当前图像
	 * 
	 * @return
	 */
	public static BufferedImage rotateImage(final BufferedImage image) {
		int w = image.getWidth();
		int h = image.getHeight();
		BufferedImage img;
		Graphics2D graphics2d;
		(graphics2d = (img = new BufferedImage(w, h, image.getColorModel()
				.getTransparency())).createGraphics()).drawImage(image, 0, 0,
				w, h, w, 0, 0, h, null);
		graphics2d.dispose();
		return img;
	}
	public static BufferedImage rotateImage(final Image image) {
		int w = image.getWidth(null);
		int h = image.getHeight(null);
		BufferedImage img;
		Graphics2D graphics2d;
		(graphics2d = (img = new BufferedImage(w, h, 2)).createGraphics()).drawImage(image, 0, 0,
				w, h, w, 0, 0, h, null);
		graphics2d.dispose();
		return img;
	}
	/**
	 * 旋转图像为指定角�?
	 * 
	 * @param degree
	 * @return
	 */
	public static BufferedImage rotateImage(final BufferedImage image,
			final int angdeg, final boolean d) {
		int w = image.getWidth(null);
		int h = image.getHeight(null);
		BufferedImage img;
		Graphics2D graphics2d;
		(graphics2d = (img = GraphicsUtils.createImage(w, h, true))
				.createGraphics()).setRenderingHint(
				RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2d.rotate(d ? -Math.toRadians(angdeg) : Math.toRadians(angdeg),
				w / 2, h / 2);
		graphics2d.drawImage(image, 0, 0, null);
		graphics2d.dispose();
		return img;
	}

	/**
	 * 生成大图的截取图
	 * 
	 * @param source
	 * @param src_x
	 * @param src_y
	 * @param desc_width
	 * @param desc_height
	 * @return
	 */
	public static BufferedImage drawClip(BufferedImage source, int src_x, int src_y,
			int desc_width, int desc_height) {
		return GraphicsUtils.drawClip(source, src_x, src_y, desc_width, desc_height);

	}

	/*public static void drawString(String s, final Graphics g, int i, int j,
			int k) {
		Graphics2D graphics2D = (Graphics2D) g;
		Font font = graphics2D.getFont();
		int size = graphics2D.getFontMetrics(font).stringWidth(s);
		GraphicsUtils.setAlpha(g, 0.9f);
		graphics2D.drawString(s, i + (k - size) / 2, j);
		GraphicsUtils.setAlpha(g, 1.0f);
	}*/

	public static void setRenderingHints(final Graphics2D g2d) {
		g2d.setRenderingHints(hints);
	}

	public static void drawString(String s, final Graphics2D graphics2D, int i,
			int j, int k) {
		Font font = graphics2D.getFont();
		int size = graphics2D.getFontMetrics(font).stringWidth(s);
		GraphicsUtils.setAlpha(graphics2D, 0.9f);
		graphics2D.drawString(s, i + (k - size) / 2, j);
		GraphicsUtils.setAlpha(graphics2D, 1.0f);
	}

	/**
	 * 在graphics上描绘文�?
	 * 
	 * @param message
	 * @param fontName
	 * @param g
	 * @param x1
	 * @param y1
	 * @param style
	 * @param size
	 */
	/*public static void drawString(String message, String fontName,
			final Graphics g, int x1, int y1, int style, int size) {
		Graphics2D graphics2D = (Graphics2D) g;
		graphics2D.setFont(new Font(fontName, style, size));
		GraphicsUtils.setAlpha(g, 0.9f);
		graphics2D.drawString(message, x1, y1);
		GraphicsUtils.setAlpha(g, 1.0f);
	}*/

	/**
	 * 默认的文字输�?
	 * 
	 * @param message
	 * @param g
	 * @param x1
	 * @param y1
	 * @param font
	 * @param fontSize
	 */
	public static void drawDefaultString(String message, final Graphics2D g,
			int x1, int y1, int font, int fontSize) {
		g.setFont(new Font("华文新魏", font, fontSize));
		g.drawString(message, x1, y1);
	}

	/**
	 * 将图片转色为灰色
	 * 
	 * @param image
	 * @return
	 */
	public static BufferedImage getGray(final BufferedImage image) {

		/*ImageFilter filter = new GrayFilter(true, 25);
		ImageProducer imageProducer = new FilteredImageSource(image.getSource(), filter);

		return toolKit.createImage(imageProducer);*/
		
		//BufferedImage dstImage=new BufferedImage(image.getWidth(),image.getHeight(),image.getType());
		int transparency = image.getColorModel().getTransparency();
	    BufferedImage dstImage =  gc.createCompatibleImage(
	                              image.getWidth(), image.getHeight(), transparency );
		Graphics2D g2d=dstImage.createGraphics();
		try{
			RenderingHints dstHints=g2d.getRenderingHints();
			ColorSpace grayCS=ColorSpace.getInstance(ColorSpace.CS_GRAY);
			ColorConvertOp cOp=new ColorConvertOp(grayCS,dstHints);
			cOp.filter(image, dstImage);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			g2d.dispose();
			g2d=null;
		}
		
		return dstImage;

	}

	/*public static BufferedImage getBufferImage(final Image image) {
		BufferedImage bufferimage = GraphicsUtils.createImage(
				image.getWidth(null), image.getHeight(null), true);
		Graphics g = bufferimage.getGraphics();
		g.drawImage(image, 0, 0, null);
		return bufferimage;
	}*/

	public Image drawClip(final Image image, int objectWidth, int objectHeight,
			int x1, int y1, int x2, int y2) throws Exception {
		BufferedImage buffer = GraphicsUtils.createImage(objectWidth,
				objectHeight, true);
		Graphics g = buffer.getGraphics();
		Graphics2D graphics2D = (Graphics2D) g;
		graphics2D.drawImage(image, 0, 0, objectWidth, objectHeight, x1, y1,
				x2, y2, null);
		graphics2D.dispose();
		graphics2D = null;
		return buffer;
	}

	/**
	 * 生成截取�?
	 * 
	 * @param image
	 * @param src_x
	 * @param src_y
	 * @param desc_width
	 * @param desc_height
	 * @return
	 */
	public static BufferedImage drawClip(final Image image, int src_x,
			int src_y, int desc_width, int desc_height) {
		BufferedImage thumbImage = new BufferedImage(desc_width, desc_height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = thumbImage.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.drawImage(image, 0, 0, desc_width, desc_height, src_x,
				src_y, image.getWidth(null), image.getHeight(null), null);

		graphics2D.dispose();

		return thumbImage;

	}

	public final static InputStream getResource(final String innerFileName) {
		String innerName = LSystem.replaceIgnoreCase(innerFileName, "\\", "/");
		return new BufferedInputStream(classLoader
				.getResourceAsStream(innerName));
	}

	/**
	 * 加载内部file转为Image
	 * 
	 * @param inputstream
	 * @return
	 */
	final static public Image loadImage(final String innerFileName) {
		String innerName = LSystem.replaceIgnoreCase(innerFileName, "\\", "/");
		String keyName = innerName.toLowerCase();
		Image cacheImage = (Image) cacheImages.get(keyName);
		if (cacheImage == null) {
			InputStream in = new BufferedInputStream(classLoader.getResourceAsStream(innerName));
			
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			try {
				byte[] bytes = new byte[16384];
				int read;
				while ((read = in.read(bytes)) >= 0) {
					byteArrayOutputStream.write(bytes, 0, read);
				}
				bytes = byteArrayOutputStream.toByteArray();
				cacheImages.put(keyName, cacheImage = toolKit.createImage(bytes));
				mediaTracker.addImage(cacheImage, 0);
				mediaTracker.waitForID(0);
				waitImage(100, cacheImage);
			} catch (Exception e) {
				//e.printStackTrace();
				throw new RuntimeException(innerName + " not found!");
				
			} finally {
				try {
					if (byteArrayOutputStream != null) {
						byteArrayOutputStream.close();
						byteArrayOutputStream = null;
					}
					if (in != null) {
						in.close();
						in = null;
					}
				} catch (IOException e) {
				}
			}

		}
		if (cacheImage == null) {
			throw new RuntimeException(
					("File not found. ( " + innerName + " )").intern());
		}
		return cacheImage;
	}

	/**
	 * 延迟加载image,以使其同步�??
	 * 
	 * @param delay
	 * @param image
	 */
	private final static void waitImage(int delay, Image image) {
		try {
			for (int i = 0; i < delay; i++) {
				if (toolKit.prepareImage(image, -1, -1, null)) {
					return;
				}
				Thread.sleep(delay);
			}
		} catch (Exception e) {

		}
	}

	/**
	 * 生成�?个BufferImage
	 * 
	 * @param i
	 * @param j
	 * @param flag
	 * @return
	 */
	final static public BufferedImage createImage(int i, int j, boolean flag) {
		if (flag) {
			return new BufferedImage(i, j, 2);
		} else {
			return new BufferedImage(i, j, 1);
		}

	}

	/**
	 * 延迟指定毫秒
	 * 
	 * @param ms
	 */
	final static public void wait(final int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException ex) {
		}
	}

	/**
	 * 字体设定
	 * 
	 * @param g
	 * @param size
	 * @param type
	 */
	final static public void font(final Graphics2D g, final int size, int type) {
		if (type == 0) {
			type = 0;
		}
		if (type == 1) {
			type = 1;
		}
		g.setFont(new Font("SansSerif", type, size));
	}

	final static public void font(final Graphics2D g, final String fontName,
			final int size, int type) {
		/*if (type == 0) {
			type = 0;
		}
		else if (type == 1) {
			type = 1;
		}*/
		g.setFont(new Font(fontName, type, size));
	}

	/**
	 * 颜色设定
	 * 
	 * @param gr
	 * @param r
	 * @param g
	 * @param b
	 */
	final static public void color(final Graphics2D gr, final int r,
			final int g, final int b) {
		gr.setColor(new Color(r, g, b));
	}

	/**
	 * 透明度设�?
	 * 
	 * @param g
	 * @param d
	 */
	/*final static public void setAlpha(Graphics g, float alpha) {
		AlphaComposite alphacomposite = AlphaComposite
				.getInstance(3, alpha);
		((Graphics2D) g).setComposite(alphacomposite);
	}*/

	final static public void setAlpha(Graphics2D g2d, float alpha) {
		AlphaComposite alphacomposite = AlphaComposite
				.getInstance(3, alpha);
		g2d.setComposite(alphacomposite);
	}
	
	/**
	 * 恢复为默认画笔
	 * @param g2d
	 * @author Leijun
	 */
	final static public void restoreStroke(Graphics2D g2d){
		g2d.setStroke(defaultStroke);
	}

	/**
	 * 分解小图
	 * 
	 * @param image
	 * @param objectWidth
	 * @param objectHeight
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 * @throws Exception
	 */
	final static private Image getClipImage(final Image image, int objectWidth,
			int objectHeight, int x1, int y1, int x2, int y2) throws Exception {
		BufferedImage buffer = createImage(objectWidth, objectHeight, true);
		Graphics g = buffer.getGraphics();
		Graphics2D graphics2D = (Graphics2D) g;
		graphics2D.drawImage(image, 0, 0, objectWidth, objectHeight, x1, y1,
				x2, y2, null);
		graphics2D.dispose();
		graphics2D = null;
		return buffer;
	}

	/**
	 * 按横行宽度分解图�?
	 * 
	 * @param img
	 * @param width
	 * @return
	 */
	final static public Image[] getImageRows(Image img, int width) {
		int iWidth = img.getWidth(null);
		int iHeight = img.getHeight(null);
		int size = iWidth / width;
		Image[] imgs = new Image[size];
		for (int i = 1; i <= size; i++) {
			try {
				imgs[i - 1] = transBlackColor(getClipImage(img, width, iHeight,
						width * (i - 1), 0, width * i, iHeight));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return imgs;
	}

	/**
	 * 将黑色颜色部分�?�明�?
	 * 
	 * @param img
	 * @return
	 */
	final static public Image transBlackColor(final Image img) {
		int width = img.getWidth(null);
		int height = img.getHeight(null);
		PixelGrabber pg = new PixelGrabber(img, 0, 0, width, height, true);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int pixels[] = (int[]) pg.getPixels();
		for (int i = 0; i < pixels.length; i++) {
			if (pixels[i] <= -11500000) {
				pixels[i] = 16777215;
			}
		}
		return toolKit.createImage(new MemoryImageSource(width, height, pixels,
				0, width));
	}

	/**
	 * 清空image缓存
	 * 
	 */
	final static public void destroyImages() {
		cacheImages.clear();
		System.gc();
	}

}