/**
 * Title: ReadMe.java
 * Description: 
 * Copyright: Copyright (c) 2013 gjfax.com
 * Company: 深圳市前海融资租赁金融交易中心
 * Author: 罗旭东
 * Date: 2015年6月12日 下午4:21:17
 * Version: 1.0
 */
package #{AppPackage}.ui.codestyle;



/** 
 * ClassName: ReadMe
 * Description:代码风格注意项
 * Create by: 
 * Date: 2015年6月12日 下午4:21:17
 */
public class ReadMe {
/**
 * 介绍：
 * APP使用MVC三层架构设计，数据访问层代码在#{AppPackage}.logic.db包下，参考UserInfoDao，
 * 控制层按模块划分，在#{AppPackage}.logic包下，参照#{AppPackage}.logic.user，
 * UI层代码在#{AppPackage}.ui包下。logic包中的代码不能出现UI控件方面的类，如：View，#{AppPackage}.R等，
 * 可通过Context链接UI层跟控制层。
 * 
 * 主要基础库：
 * 系统中封装了几个独立于业务代码的公用库，包括网络传输、文件上传下载、数据库操作、线程池管理等，可在不同项目中使用。
 * 1、CommonUtils-x.x.jar  工具类，使用例子参考./simples/CommonUtils.java
 * 2、DatabaseHelp-x.x.jar  数据库管理类，使用对象关系映射管理数据库，使用例子参考UserInfoDao.java
 * 3、SingletonFactory-x.x.jar 单利管理类，使用该类创建单例实例，统一管理，使用例子参照UserFactory.java
 * 4、ThreadPoolHelp-x.x.jar 线程池管理，创建的线程使用该线程池管理，使用例子参照./simples/ThreadPoolHelp.java
 * 5、AsyncHttpHelp-x.x.jar 网络传输管理库，http网络传输，文件上传下载等操作，使用例子参照./simples/AsyncHttpHelp.java
 * 
 * 
 * 注意项：
 * 1、导入代码注释模板，codestyle包下有一个codetemplates.xml模板，导入到eclipse中，配置好自己的个人信息
 * 2、原则上所有activity必须继承BaseActivity
 * 3、原则上所有fragment必须继承BaseFragment
 * 4、所有listview，GridView中使用的adapter必须继承AbsBaseAdapter
 * 5、点击事件使用OnClickAvoidForceListener代替OnClickListener
 * 6、list中item的点击事件使用OnItemClickAvoidForceListener代替OnItemClickListener
 * 7、logic层包按模块命名包名，参考logic.user模块
 * 8、每个模块的manager使用单例管理，参考logic.user模块
 * 9、http传输实现参考#{AppPackage}.logic.user.manager.UserManager中的loginRequest()类
 * 
 * 
 * 
 * 
 * 
 * 常用api
 * 
 * 1、新开线程任务
 new ThreadTaskObject(){
	public void run() {
		
	};
 }.start();
 * 
 * 2、BaseActivity中常用抽象类的用法
 protected int getContentViewLayoutResId() //布局资源文件
 protected void onCreateFindView(Bundle savedInstanceState)  //创建view，如findViewById(id)代码放在该方法中
 protected void onCreateAddListener(Bundle savedInstanceState)   //注册监听的代码放在该方法中
 protected void onCreateInitData(Bundle savedInstanceState)  //UI线程中初始化数据的代码放在该方法中
 protected void onCreateTaskLoadData()  //异步处理代码放在该方法中
 protected void onCreateTaskAddView()  //异步处理完以后调用该方法，该方法在UI线程中执行
 
 * 3、Activity中发送消息的方法，调用sendMessage(obtainMessage(what, obj))即可
 * 4、Activity中接收消息的方法，重写protected void handleMessage(Message msg)方法
 * 
 * 
 * 
 * 
 */
}
