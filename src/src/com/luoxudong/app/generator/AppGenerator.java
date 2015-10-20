/**
 * Title: AppGenerator.java
 * Description: 
 * Copyright: Copyright (c) 2013-2015 luoxudong.com
 * Company: 个人
 * Author: 罗旭东 (hi@luoxudong.com)
 * Date: 2015年10月20日 上午10:41:57
 * Version: 1.0
 */
package com.luoxudong.app.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;

/** 
 * <pre>
 * ClassName: AppGenerator
 * Description:TODO(这里用一句话描述这个类的作用)
 * Create by: 罗旭东
 * Date: 2015年10月20日 上午10:41:57
 * </pre>
 */
public class AppGenerator {
	public static final String ENCODING = "UTF-8";
	
	/** 配置文件所在包名 */
	private static String sConfigPackageName = "com.luoxudong.app.generator.config";
	
	/** 配置文件名称 */
	private static String sFileName = "app.properties";
	
	/** 配置文件 */
	private Properties mProp = new Properties();
	
	/** 模板文件 */
	private File mTempFile = null;
	
	/** 模板文件名称 */
	private String mTempFileName = null;
	
	/** 生产后的目标文件 */
	private File mDescFile = null;
	
	/** 生成后的目标文件名称 */
	private String mDescFileName = null;
	
	/** app项目包名 */
	private String mAppPackage = null;
	
	public static void main(String[] args) {
		new AppGenerator().generate();
	}
	
	public void generate() {
		loadProperties();
		writeFile();
	}
	
	@SuppressWarnings("unchecked")
	private void loadProperties() {
		try {
			FileInputStream fileInput = new FileInputStream(getFilePath(
					sConfigPackageName, sFileName));
			mProp.load(fileInput);
			mAppPackage = mProp.getProperty("AppPackage");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void writeFile() {
		String outputPath = mProp.getProperty("OutputPath");
		try {
			mDescFileName = ".classpath";
			mDescFile = new File(outputPath, mDescFileName);
			mTempFileName = mProp.getProperty("TemplatePackage");
			mTempFile = new File(getFilePath(mTempFileName, ".classpath"));
			stringToFile(mDescFile, readTpl(mTempFile));
			
			mDescFileName = ".project";
			mDescFile = new File(outputPath, mDescFileName);
			mTempFileName = mProp.getProperty("TemplatePackage");
			mTempFile = new File(getFilePath(mTempFileName, ".project"));
			stringToFile(mDescFile, readTpl(mTempFile));
			
			mDescFileName = "project.properties";
			mDescFile = new File(outputPath, mDescFileName);
			mTempFileName = mProp.getProperty("TemplatePackage");
			mTempFile = new File(getFilePath(mTempFileName, "project.properties"));
			stringToFile(mDescFile, readTpl(mTempFile));
			
			mDescFileName = ".project";
			mDescFile = new File(outputPath, mDescFileName);
			mTempFileName = mProp.getProperty("TemplatePackage");
			mTempFile = new File(getFilePath(mTempFileName, ".project"));
			stringToFile(mDescFile, readTpl(mTempFile));
			
			mDescFileName = "proguard-project.txt";
			mDescFile = new File(outputPath, mDescFileName);
			mTempFileName = mProp.getProperty("TemplatePackage");
			mTempFile = new File(getFilePath(mTempFileName, "proguard-project.txt"));
			stringToFile(mDescFile, readTpl(mTempFile));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String readTpl(File tpl) {
		String content = null;
		try {
			content = FileUtils.readFileToString(tpl, ENCODING);
			Set<Object> ps = mProp.keySet();
			for (Object o : ps) {
				String key = (String) o;
				String value = mProp.getProperty(key);
				content = content.replaceAll("\\#\\{" + key + "\\}", value);
			}
		} catch (IOException e) {
		}
		return content;

	}
	
	private static void stringToFile(File file, String s) throws IOException {
		FileUtils.writeStringToFile(file, s, ENCODING);
	}
	
	private String getFilePath(String packageName, String name) {
		String path = packageName.replaceAll("\\.", "/");
		return "src/" + path + "/" + name;
	}

}
