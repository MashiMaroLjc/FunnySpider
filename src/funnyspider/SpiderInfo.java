package funnyspider;

import java.io.IOException;
import java.io.PrintStream;

import org.apache.log4j.Logger;


/**
 * 爬虫运行时信息封装的信息块
 */
public class SpiderInfo {

	private  String cookie = null;
	private  static String begUrl = null;
	private static SpiderInfo self = null;
	
	private static Logger logger = null;
	
	/**
	 * 单例创造信息块，第一次创立记录，第二次创立则页数自动加一,更新当前url
	 * @param url 当前访问的url
	 * @param allPageNum 全部页数
	 */
	protected synchronized static SpiderInfo createInfoBlock(String url,int allPageNum,int nowPageNum){
		if(self == null){
			self = new SpiderInfo(url, allPageNum, 1);
			self.setBegUrl(url);
			return self;
		}else{
			nowPageNum += 1;
			SpiderInfo.nowPageNum = nowPageNum;
			self.setUrl(url);
			return self;
		}
	}
	

	
	/**
	 * 设置日志标志
	 * @return
	 */
	protected static void setLogger(Logger log){
		logger = log;
	}
	
	
	/**
	 * 如果设置了日志，则往日志里输出，否则向输出流里输出
	 * @param message
	 */
	public void outputMessage(Object message,PrintStream stream){
		if(null!=logger){
			if(stream.equals(System.err)){
				logger.error(message);
			}else{
				logger.info(message);
			}
		}else{
			stream.println(message);
		}
	}
	
	
	private SpiderInfo(String url,int allPageNum,int nowPageNum){
		this.url = url;
		SpiderInfo.allPageNum = allPageNum;
		SpiderInfo.nowPageNum = nowPageNum;
	}

	//当前状态
	private static boolean  alive = true;
	
	//正在访问的url
	private String url;
	
	//正在等待爬取的页面
	//private int waitNum;
	
	//正在设置好要爬取得页面
	private static int  allPageNum;
	
	//当前页
	private static int  nowPageNum;

	
	
	/**
	 * 获取当前url
	 * @return url
	 */
	public String getUrl(){
		return url;
	}
	
	/**
	 * 设置当前url
	 * @param url
	 */
	private void setUrl(String url){
		this.url = url;
	}
	
	
	/**
	 * 获取剩余页面
	 * @return number
	 */
	public int getWaitNum(){
		return allPageNum-nowPageNum; 
	}
	
	/**
	 * 获取所有需要爬取的页数
	 * @return number
	 */
	public int getAllPageNum(){
		return allPageNum;
	}
	
	/**
	 * 获取当前已经完成的页数
	 * @return number
	 */
	public int getNowPageNum(){
		return nowPageNum;
	}
	
	/**
	 * 设置起始地url
	 * @param url
	 */
	private  void setBegUrl(String url){
		if(begUrl == null){
			begUrl = url;
		}
	}
	
	
	/**
	 * 获取起始地url
	 * @return begurl
	 */
	public  String getBegUrl(){
		return begUrl;
	}

	

	/**
	* 杀死该爬虫
	*/
	public synchronized void kill(){
		alive = false;
		//更新记录文件
		try {
			SpiderManager.dump(begUrl);
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
		System.exit(-1);
	}

	/**
	 * 设置cookie
	 * @param cookie
	 */
	public synchronized  void setCookie(String cookie){
		this.cookie = cookie;
	}
	
	/**
	 * 获取cookie
	 * @return cookie
	 */
	public synchronized String getCookie(){
		return cookie; 
	}
	

	/**
	 * 信息序列化
	 * @return 序列后的内容
	 */
	protected  static String dump(){
		return "<begin_url>" +begUrl + "</begin_url>\r\n"
				+ "<allPageNum>" + allPageNum+"</allPageNum>\r\n"
				+ "<nowPageNum>" + nowPageNum+"</nowPageNum>\r\n"
				+ "<alive>" + alive + "</alive>";
	}
	
	
}







