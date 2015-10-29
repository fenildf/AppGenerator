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
	
	/** 模板文件包名 */
	private String mTempFilePackage = null;
	
	/** 生产后的目标文件 */
	private File mDestFile = null;
	
	/** 生成后的目标文件名称 */
	private String mDestFileName = null;
	
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
			FileInputStream fileInput = new FileInputStream(getFilePath(sConfigPackageName, sFileName));
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
		mTempFilePackage = mProp.getProperty("TemplatePackage");
		
		try {
			//先删除目录
			FileUtils.deleteDirectory(new File(outputPath));
			
			mDestFileName = ".classpath";
			mDestFile = new File(outputPath, mDestFileName);
			mTempFile = new File(getFilePath(mTempFilePackage, ".classpath"));
			stringToFile(mDestFile, readTpl(mTempFile));
			
			mDestFileName = ".project";
			mDestFile = new File(outputPath, mDestFileName);
			mTempFile = new File(getFilePath(mTempFilePackage, ".project"));
			stringToFile(mDestFile, readTpl(mTempFile));
			
			mDestFileName = "project.properties";
			mDestFile = new File(outputPath, mDestFileName);
			mTempFile = new File(getFilePath(mTempFilePackage, "project.properties"));
			stringToFile(mDestFile, readTpl(mTempFile));
			
			mDestFileName = "AndroidManifest.xml";
			mDestFile = new File(outputPath, mDestFileName);
			mTempFile = new File(getFilePath(mTempFilePackage, "AndroidManifest.xml"));
			stringToFile(mDestFile, readManifestTpl(mTempFile));
			
			mDestFileName = "proguard-project.txt";
			mDestFile = new File(outputPath, mDestFileName);
			mTempFile = new File(getFilePath(mTempFilePackage, "proguard-project.txt"));
			stringToFile(mDestFile, readProguardTpl(mTempFile));
			
			//复制assets目录
			mDestFile = new File(outputPath + "/assets");
			mDestFile.mkdirs();
			
			if ("true".equals(mProp.getProperty("ShareSDK"))) {//需要分享SDK
				File destFile = new File(mDestFile.getAbsolutePath(), "ShareSDK.xml");
				stringToFile(destFile, readTpl(new File(getFilePath(mTempFilePackage, "/assets/ShareSDK.xml"))));
			}
			
			//复制libs目录
			mDestFile = new File(outputPath + "/libs");
			mDestFile.mkdirs();
			File libsDir = new File(getFilePath(mTempFilePackage, "libs"));
			copyLibs(libsDir, mDestFile);
			
			//复制res目录
			mDestFile = new File(outputPath + "/res");
			mDestFile.mkdirs();
			File resDir = new File(getFilePath(mTempFilePackage, "res"));
			copyRes(resDir, mDestFile);
			
			//复制源码目录
			mDestFile = new File(outputPath + "/src");
			mDestFile.mkdirs();
			File srcDir = new File(getFilePath(mTempFilePackage, "src"));
			copySrc(srcDir, mDestFile);
			
			
			System.out.println("生产app项目完成");
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
	
	/**
	 * 生成代码混淆代码
	 * @param tpl
	 * @return
	 */
	private String readProguardTpl(File tpl) {
		String content = readTpl(tpl);
		
		//加载系统库
		File libsDir = new File(getFilePath(mTempFilePackage, "libs"));
		File[] libsFile = libsDir.listFiles();
		StringBuilder libBuilder = new StringBuilder();
		if (libsDir != null){
			if (libsFile != null){
				for (File lib : libsFile){
					if (lib.isFile()){
						if (!"true".equals(mProp.getProperty("ShareSDK")) && lib.getName().indexOf("ShareSDK-") == 0) {//不需要分享SDK
							continue;
						}
						
						if (!"true".equals(mProp.getProperty("PushSDK")) && lib.getName().indexOf("Getui") == 0) {//不需要推送SDK
							continue;
						}
						
						if (!"true".equals(mProp.getProperty("FindLocSDK")) && lib.getName().indexOf("BaiduLBS") == 0) {//不需要定位SDK
							continue;
						}
						
						libBuilder.append("-libraryjars libs/" + lib.getName()).append("\n");
					}
				}
			}
			
			content = content.replaceAll("\\#\\{Libraryjars\\}", libBuilder.toString());
		}
		
		StringBuilder keepClassBuilder = new StringBuilder();
		if ("true".equals(mProp.getProperty("ShareSDK"))) {//需要分享SDK
			keepClassBuilder.append("-keep class cn.sharesdk.** { *; }").append("\n");
		}
		
		if ("true".equals(mProp.getProperty("PushSDK"))) {//需要推送SDK
			keepClassBuilder.append("-keep class com.igexin.** { *; }").append("\n");
		}
		
		if ("true".equals(mProp.getProperty("FindLocSDK"))) {//需要定位SDK
			keepClassBuilder.append("-keep class com.baidu.** { *; }").append("\n");
		}
		
		content = content.replaceAll("\\#\\{KeepClass\\}", keepClassBuilder.toString());
		
		return content;
	}
	
	/**
	 * 生成manifest代码
	 * @param tpl
	 * @return
	 */
	private String readManifestTpl(File tpl) {
		String content = readTpl(tpl);
		
		StringBuilder userPermissionBuilder = new StringBuilder();
		String shareContent = "";
		String getuiContent = "";
		String findLocContent = "";
		
		if ("true".equals(mProp.getProperty("ShareSDK"))) {//需要分享SDK
			File shareTempFile = new File(getFilePath(mTempFilePackage, "sharesdk-config.txt"));
			shareContent = readTpl(shareTempFile);
		}
		
		if ("true".equals(mProp.getProperty("PushSDK"))) {//需要推送SDK
			userPermissionBuilder.append("    <uses-permission android:name=\"android.permission.SYSTEM_ALERT_WINDOW\" />").append("\n")
			.append("    <uses-permission android:name=\"android.permission.RECEIVE_BOOT_COMPLETED\" />").append("\n");
			
			File getuiTempFile = new File(getFilePath(mTempFilePackage, "getuisdk-config.txt"));
			getuiContent = readTpl(getuiTempFile);
		}
		
		if ("true".equals(mProp.getProperty("FindLocSDK"))) {//需要定位SDK
			userPermissionBuilder.append("    <uses-permission android:name=\"android.permission.ACCESS_FINE_LOCATION\" />").append("\n")
			.append("    <uses-permission android:name=\"android.permission.ACCESS_COARSE_LOCATION\" />").append("\n");
			
			findLocContent = "<meta-data android:name=\"com.baidu.lbsapi.API_KEY\" android:value=\"\" /> <!-- 百度定位 -->";
		}
		
		content = content.replaceAll("\\#\\{ShareSDKConfig\\}", shareContent);
		content = content.replaceAll("\\#\\{GeTuiSDKConfig\\}", getuiContent);
		content = content.replaceAll("\\#\\{Baidulbsapi\\}", findLocContent);
		content = content.replaceAll("\\#\\{UserPermission\\}", userPermissionBuilder.toString());
		
		return content;
	}
	
	private void stringToFile(File file, String s) throws IOException {
		FileUtils.writeStringToFile(file, s, ENCODING);
	}
	
	private void copyLibs(File srcDir, File desDir){
		if (srcDir != null){
			File[] files = srcDir.listFiles();
			if (files != null){
				for (File file : files){
					if (file.isDirectory()){
						File newDir = new File(desDir, file.getName());
						newDir.mkdirs();
						copyLibs(file, newDir);
					}else{
						if (!"true".equals(mProp.getProperty("ShareSDK")) && file.getName().indexOf("ShareSDK-") == 0) {//不需要分享SDK
							continue;
						}else if (!"true".equals(mProp.getProperty("PushSDK")) && (file.getName().indexOf("Getui") == 0 || "libgetuiext.so".equals(file.getName()))) {//不需要推送SDK
							continue;
						}else if (!"true".equals(mProp.getProperty("FindLocSDK")) && (file.getName().indexOf("BaiduLBS") == 0 || "liblocSDK5.so".equals(file.getName()))) {//不需要定位SDK
							continue;
						}
						
						try {
							FileUtils.copyFile(file, new File(desDir.getAbsolutePath(), file.getName()));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	private void copyRes(File srcDir, File desDir){
		if (srcDir != null){
			File[] files = srcDir.listFiles();
			if (files != null){
				for (File file : files){
					if (file.isDirectory()){
						File newDir = new File(desDir, file.getName());
						newDir.mkdirs();
						copyRes(file, newDir);
					}else{
						if (!"true".equals(mProp.getProperty("ShareSDK")) && (file.getName().indexOf("logo_") == 0 || "oks_strings.xml".equals(file.getName()) || "ssdk_strings.xml".equals(file.getName()))) {//不需要分享SDK
							continue;
						}else if (!"true".equals(mProp.getProperty("PushSDK")) && (file.getName().indexOf("getui_") == 0 || "push.png".equals(file.getName()))) {//不需要推送SDK
							continue;
						}
						
						try {
							if (file.getName().indexOf(".xml") > 0){
								stringToFile(new File(desDir.getAbsolutePath(), file.getName()), readTpl(file));
							}else{
								FileUtils.copyFile(file, new File(desDir.getAbsolutePath(), file.getName()));
							}
							
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	private void copySrc(File srcDir, File desDir){
		if (srcDir != null){
			File[] files = srcDir.listFiles();
			if (files != null){
				for (File file : files){
					if (file.isDirectory()){
						File newDir = new File(desDir, file.getName());
						newDir.mkdirs();
						copySrc(file, newDir);
					}else{
						if (!file.getName().endsWith(".txt")){
							continue;
						}
						
						/*if (!"true".equals(mProp.getProperty("ShareSDK")) && (file.getName().indexOf("logo_") == 0 || "oks_strings.xml".equals(file.getName()) || "ssdk_strings.xml".equals(file.getName()))) {//不需要分享SDK
							continue;
						}else if (!"true".equals(mProp.getProperty("PushSDK")) && (file.getName().indexOf("getui_") == 0 || "push.png".equals(file.getName()))) {//不需要推送SDK
							continue;
						}*/
						
						//替换文件名
						String fileName = file.getName().replace(".txt", ".java");
						
						Set<Object> ps = mProp.keySet();
						for (Object o : ps) {
							String key = (String) o;
							String value = mProp.getProperty(key);
							fileName = fileName.replaceAll("\\#\\{" + key + "\\}", value);
						}
						
						try {
							stringToFile(new File(desDir.getAbsolutePath(), fileName), readTpl(file));
						} catch (IOException e) {
							e.printStackTrace();
						}
						
					}
				}
			}
		}
	}
	
	private String getFilePath(String packageName, String name) {
		String path = packageName.replaceAll("\\.", "/");
		return "src/" + path + "/" + name;
	}

}
