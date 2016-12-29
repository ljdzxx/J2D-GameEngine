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
import java.awt.Cursor;
import java.awt.Font;

import com.j2d.components.AndrodKeyboard;
import com.j2d.resource.Configuration;
import com.j2d.resource.ImagesLoader;

public class LSystem{
	public static final LogManager logger=new LogManager();
	//public final static Random rand = new Random();
	public final static  String encoding = "UTF-8";
	public final static String LS = System.getProperty("line.separator", "\n");
	public final static String FS = System.getProperty("file.separator", "\\");
	public static int FONT_TYPE = 15;
	public static int FONT_SIZE = 1;
	public static String FONT = "宋体";
	public static Font msyh=null;
	public static IHandler currentGameHandler;
	public static ICanvas currentCanvas;
	public volatile static int WIDTH;
	public volatile static int HEIGHT;
	public volatile static int fps=50;//FPS
	public static ImagesLoader imsLoader=null;
	public static Configuration commConfig,deviceConfig,coinBoxList,noteBoxList;
	public static boolean isLogined=false;
	
	//各种透明度
	public static AlphaComposite alpha_05,
	alpha_10,
	alpha_15,
	alpha_20,
	alpha_25,
	alpha_30,
	alpha_35,
	alpha_40,
	alpha_45,
	alpha_50,
	alpha_55,
	alpha_60,
	alpha_65,
	alpha_70,
	alpha_75,
	alpha_80,
	alpha_85,
	alpha_90,
	alpha_95,
	alpha_100;//各种透明度定义

	public static AndrodKeyboard keyboard;//键盘
	public static String PosId;

	static{
		alpha_05=AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.05f);
		alpha_10=AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.10f);
		alpha_15=AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.15f);
		alpha_20=AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.20f);
		alpha_25=AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f);
		alpha_30=AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.30f);
		alpha_35=AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.35f);
		alpha_40=AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.40f);
		alpha_45=AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.45f);
		alpha_50=AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.50f);
		alpha_55=AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.55f);
		alpha_60=AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.60f);
		alpha_65=AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.65f);
		alpha_70=AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.70f);
		alpha_75=AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f);
		alpha_80=AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.80f);
		alpha_85=AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f);
		alpha_90=AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.90f);
		alpha_95=AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.95f);
		alpha_100=AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.00f);
	}
	
	public static void setSystemCursor(Cursor cursor) {
		if (currentCanvas != null) {
			currentCanvas.setCursor(cursor);
		}
	}

	public static String getExtension(String name) {
		int index = name.lastIndexOf(".");
		if (index == -1) {
			return "";
		} else {
			return name.substring(index + 1).toLowerCase();
		}
	}

	public static final String replaceIgnoreCase(String line, String oldString,
			String newString) {
		if (line == null)
			return null;
		String lcLine = line.toLowerCase();
		String lcOldString = oldString.toLowerCase();
		int i = 0;
		if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
			char line2[] = line.toCharArray();
			char newString2[] = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j;
			for (j = i; (i = lcLine.indexOf(lcOldString, i)) > 0; j = i) {
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
			}
			buf.append(line2, j, line2.length - j);
			return buf.toString();
		} else {
			return line;
		}
	}

}
