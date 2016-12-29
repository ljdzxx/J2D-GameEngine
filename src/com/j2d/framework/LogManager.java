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

import org.apache.log4j.Logger;

/**
 * 日志管理器   
 * @author Leijun
 * @version   
 *       1.0.0, Mar 26, 2013 3:59:12 PM
 */
public class LogManager {
	private final Logger logger=Logger.getLogger("main");
	
	public LogManager(){}
	
	public void debug(Class<Object> c, Object message){
		logger.debug(new StringBuilder("[").append(c.getName()).append("] - ").append(message));
	}
	
	public void info(Class<Object> c, Object message){
		logger.info(new StringBuilder("[").append(c.getName()).append("] - ").append(message));
	}
	
	public void warn(Class<Object> c, Object message){
		logger.warn(new StringBuilder("[").append(c.getName()).append("] - ").append(message));
	}
	
	public void error(Class<Object> c, Object message){
		logger.error(new StringBuilder("[").append(c.getName()).append("] - ").append(message));
	}
	
	public void fatal(Class<Object> c, Object message){
		logger.fatal(new StringBuilder("[").append(c.getName()).append("] - ").append(message));
	}
}
