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

package com.j2d.resource;

import java.io.*;
import java.util.*;

/**
 * 配置文件读写类
 * @author Leijun
 * @version   
 *       1.0.0, Mar 27, 2013 9:20:26 AM
 */
public class Configuration {
	private Properties config = new Properties();// 记录配置项
	private String fn = null;// 记录配置文件名

	// 此构造方法用于新建配置文件
	public Configuration() {
	}

	// 从指定文件名读入配置信息
	public Configuration(String fileName) throws ConfigurationException {
		try {
			FileInputStream fin = new FileInputStream(fileName);
			config.load(fin); // 载入文件
			fin.close();
		} catch (IOException ex) {
			throw new ConfigurationException("can't read file:" + fileName);
		}
		fn = fileName;
	}

	// 指定配置项名称，返回配置值
	public String getValue(String itemName) {
		return config.getProperty(itemName);
	}

	// 指定配置项名称和默认值，返回配置值
	public String getValue(String itemName, String defaultValue) {
		return config.getProperty(itemName, defaultValue);
	}

	// 设置配置项名称及其值
	public synchronized void setValue(String itemName, String value) {
		config.setProperty(itemName, value);
		return;
	}

	// 保存配置文件，指定文件名和抬头描述
	public synchronized void saveFile(String fileName, String description)
			throws ConfigurationException {
		try {
			FileOutputStream fout = new FileOutputStream(fileName);
			config.store(fout, description);// 保存文件
			fout.close();
		} catch (IOException ex) {
			throw new ConfigurationException("can't save to path:" + fileName);
		}
	}

	// 保存配置文件，指定文件名
	public synchronized void saveFile(String fileName) throws ConfigurationException {
		saveFile(fileName, "");
	}

	// 保存配置文件，采用原文件名
	public synchronized void saveFile() throws ConfigurationException {
		if (fn.length() == 0)
			throw new ConfigurationException("the file name is undefined!");
		saveFile(fn);
	}
}
