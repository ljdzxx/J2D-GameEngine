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

public class LColor {

	public int R = 0;

	public int G = 0;

	public int B = 0;

	private LColor() {
	}

	/**
	 * �ж�����lcolor�Ƿ����?
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean equals(final LColor a, final LColor b) {
		return (a.R == b.R) && (a.G == b.G) && (a.B == b.B);
	}

	/**
	 * ����ɫPixel��ֵ����ΪLColor
	 * 
	 * @param c
	 * @return
	 */
	public static LColor getLColor(int pixel) {
		LColor color = new LColor();
		color.R = (pixel & 0x00ff0000) >> 16;
		color.G = (pixel & 0x0000ff00) >> 8;
		color.B = pixel & 0x000000ff;
		return color;
	}

	/**
	 * ��color����Ϊ����
	 * 
	 * @param color
	 * @return
	 */
	public int getPixel(final LColor color) {
		return (color.R << 16) | (color.G << 8) | color.B;
	}

	public int getPixel() {
		return (R << 16) | (G << 8) | B;
	}

	/**
	 * ע��r,g,b��ֵ
	 * 
	 * @param r
	 * @param g
	 * @param b
	 */
	public LColor(final int r, final int g, final int b) {
		this.R = r;
		this.G = g;
		this.B = b;
	}

	public static LColor fromArgb(final int r, final int g, final int b) {
		return new LColor(r, g, b);
	}

}
