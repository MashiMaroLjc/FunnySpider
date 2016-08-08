package funnyspider;



import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.SimpleLayout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import funnyspider.util.*;
import funnyspider.util.net.HttpRequest;
 

/**
 * 框架主类
 * @author MashiMaroLjc
 */
public class SpiderApp {
	
	private String url;
	
	//App线程数
	private int threadNum = 1; 
	private int maxThreadNum = 20;
	
	//总共访问的次数
	private int pageNum = 1;
	private int nowNum = 1;
	
	//爬虫休眠参数
	private int sleepTime = 1;
	//爬虫访问超时限制
	private int timeout = 5;
	//http头部参数
	private HashMap<String,String> parms = null;
	
	//http get或post的数据
	private HashMap<String,String> datas = null;
	
	
	//默认编码
	private String charset = "utf-8";
	
	//任务队列
	private Queue<String> mission = new LinkedList<String>();
	
	//已经访问队列
	
	private Queue<String> visited = new LinkedList<String>();
	
	//发生异常的处理方法
	private ExceptionHandle errHandle = new ExceptionDealWith();
	
	//文本处理方法
	private ContentHandle contentHandle = new ContentDealWith();
	
	//跳页面
	private JumpHandle jumpHandle = new JumpDealWith();
	
	//处理非200页面
	private BadRequestHandle badRequestHandle = new BadRequestDealWith();
	
	
	//在请求前进行预处理
	private BeforeReuqestHandle berforeReaquestHandle = new BeforeRequestDealWith();
	
	//设置是否周期序列化爬虫至spiderjson文件
	private boolean tojson = false;
	private String jsonFileName = "spider.json";
	
	
	//cookie
	private String cookie = null;
	
	//日志相关设置
	private Logger logger = null;   
	private String logName = "spider.log";
	private boolean logAppend = true;
	
	
	private Level getLogLevel(int level){
		switch (level) {
		case 0:
			return Level.OFF;
		case 1:
			return Level.FATAL ;
		case 2:
			return Level.ERROR;
		case 3:
			return Level.WARN;
		case 4:
			return Level.INFO;
		case 5:
			return Level.DEBUG;
		case 6:
			return Level.ALL;
		default:
			System.err.println("set log level error.It default level is All");
			return Level.ALL;
		}
	}
	private Map<String,Object> logMap = new HashMap<>();
	/**
	 * 设置日志配置
	 * @param level
	 * @param fileName
	 * @param append
	 * @return this
	 */
	public SpiderApp setLogerConfig(int level,String fileName,boolean append,String configFilename){
		logMap.put("level", level);
		logMap.put("fileName", fileName);
		logMap.put("append",append);
		logMap.put("configFilename",configFilename);
		logger = Logger.getLogger(SpiderApp.class);
		Level logLevel = getLogLevel(level);
		try {
			FileAppender appender = new FileAppender(new SimpleLayout(), fileName,append);
			if(null == configFilename){
				BasicConfigurator.configure();
			}else{
				PropertyConfigurator.configure(configFilename);
			}
			logger.addAppender(appender);
			logger.setLevel(logLevel);
		} catch (IOException e) {
			e.printStackTrace(System.err);
		} 
		return this;
	}
	
	/**
	 * 只配置日志信息等级，其他使用默认配置
	 * 默认记录在spider.log文件，日志以追加的形式写入，其余使用log4j的默认配置
	 * @param level
	 * @return
	 */
	
	public SpiderApp setLogerConfig(int level){
		return setLogerConfig(level,logName,logAppend,null);
	}
	
	/**
	 * 构造方法
	 */
	public SpiderApp(){
		
	}
	

	/**
	 * 构造方法
	 * @param url
	 */
	public SpiderApp(String url){
		this.url = url;
	}
		
	public SpiderApp setToJSON(boolean flag){
		return setToJSON(flag,null);
	}	
	
	public SpiderApp setToJSON(boolean flag,String fileName){
		if(null!=fileName){
			jsonFileName = fileName;
		}
		tojson = flag;
		return this;
	}
	
	
	private void setVisted(Queue<String> visited){
		this.visited =  visited;
	}
	
	private void setMission(Queue<String> mission){
		this.mission = mission;
	}
	
	
	/**
	 * 设置访问时携带的数据
	 * @param datas
	 * @return this
	 */
	public SpiderApp setDatas(HashMap<String,String> datas){
		this.datas = datas;
		return this;
	}
	
	/**
	 * 设置设置爬取页数
	 * @param pageNum
	 */
	public SpiderApp setPageNum(int pageNum){
		this.pageNum = pageNum;
		return this;
	}
	
	
	public SpiderApp setTimeout(int second){
		timeout = second;
		return this;
	}
	
	/**
	 * 获取爬取页数
	 * @return pageNum
	 */
	public int getPageNum(){
		return pageNum;
	}
	
	/**
	 * 设置线程数，超出指定范围无效
	 * @param num 线程数[1,20]
	 */
	public final SpiderApp setThreadNum(int num){
		if (num <= maxThreadNum && num >=1){
			threadNum = num;
			return this;
		}
		return this;
	}
	
	
	/**
	 * 获取线程数目
	 * @return this
	 */
	public  int getThreadNum(){
		return threadNum;
	}
	
	/**
	 * 设置每一次访问之间的间隔
	 * @param second 默认1秒
	 * @return this
	 */
	public SpiderApp setSleepTime(int second){
		this.sleepTime = second;
		return this;
	}
	

	
	/**
	 * 设置头部参数
	 */
	public SpiderApp setHanderParms(HashMap<String,String> parms){
		this.parms = parms;
		return this;
	}
	
	/**
	 * 设置错误处理函数
	 * @param errHandle
	 * @return this
	 */
	public SpiderApp setExcepHandle(ExceptionHandle errHandle){
		this.errHandle = errHandle;
		return this;
	}
	
	
	
	/**
	 * 非200的处理方法
	 * @return this
	 */
	public SpiderApp setBadRequestHandle(BadRequestHandle badhandle){
		this.badRequestHandle = badhandle;
		return this;
	}
	
	/**
	 * 设置对每个爬取下来文本处理方法
	 * @param conHandle
	 * @return this
	 */
	public SpiderApp setContentHandle(ContentHandle conHandle){
		this.contentHandle = conHandle;
		return this;
	}
	
	/**
	 * 设置从当前页面提取下一次需要访问连接的规则
	 * @param jumpHandle
	 * @return
	 */
	public SpiderApp setJumpHandle(JumpHandle jumpHandle){
		this.jumpHandle = jumpHandle;
		return this;
	}
	
	/**
	 * 设置进行页面访问时的预处理
	 * @param before
	 * @return this
	 */
	public SpiderApp setBeforeRequestHandle(BeforeReuqestHandle before){
		this.berforeReaquestHandle = before;
		return this;	
	}
	
	
	/**
	 * 页面自增，且url放入到已访问队列中
	 * @param url
	 */
	private synchronized void visted(String url){
		nowNum++;
		visited.add(url);
	}
	
	
	private boolean flag  =false;	
	/**
	 * false表示文本预处理只执行一次，true表示每次爬取都进行预处理
	 * @param flag
	 * @return this
	 */
	public SpiderApp setFalg(boolean flag){
		this.flag = flag;
		return this;
	}
	
	
	public boolean getJsonFlag(){
		return tojson;
	}
	
	/**
	 * 根据响应码处理对于流程
	 * @param code
	 * @param hr
	 * @param sInfo
	 * @param conn
	 * @throws Exception
	 */
	private void dealWithCode(int code,HttpRequest hr,SpiderInfo sInfo,HttpURLConnection conn)
		throws Exception{
		if(null!=logger){
			logger.info(sInfo.getUrl() + " code: "  + code );
		}
		String content = new String();
		//响应报文
        if(HttpURLConnection.HTTP_OK == code){
        	//200响应
        	content = hr.getBody(charset);
        	contentHandle.cHandle(sInfo,content);
        	if(pageNum >1){
        		String[]  urls = jumpHandle.jHandle(sInfo,content);
        		if (urls!=null){
        			for(String next_url:urls){
        				//添加至任务队列
        				mission.add(next_url);
        			}
        		}else{
        			throw new IllegalArgumentException("jumpHandle can't return null");
        		}
        	}
        }else if (code == HttpURLConnection.HTTP_MOVED_TEMP
    			|| code == HttpURLConnection.HTTP_MOVED_PERM
				|| code == HttpURLConnection.HTTP_SEE_OTHER){
        	//重定向
        	String newUrl = conn.getHeaderField("Location");
        	//重定向插队至第一位
        	LinkedList<String> temp = (LinkedList<String>)mission;
        	temp.add(0, newUrl);
        	mission = temp;	
        }else{
        	//非200
        	badRequestHandle.bHandle(sInfo,code);
        }

	}
	
	/**
	 * 根据url访问
	 * @param targetUrl
	 * @param conn
	 * @param second
	 */
	private void catchUrl(String targetUrl){
		String now_url = targetUrl;
		HttpURLConnection conn = null;
		SpiderInfo sInfo = SpiderInfo.createInfoBlock(now_url, pageNum,nowNum);
		try{
			if(nowNum == 1 || flag){
				berforeReaquestHandle.brHandle(sInfo);
				this.cookie = sInfo.getCookie();
			}
			if(visited.contains(now_url)){
				return;
			}
			visted(now_url);
			HttpRequest hr = new HttpRequest(now_url);
			hr.setCookie(cookie);
			conn = hr.open(parms,datas,timeout);
	        int code = conn.getResponseCode();
	        dealWithCode(code, hr, sInfo, conn);
		}catch(Exception err){
			errHandle.handle(sInfo,err);
		}
	}
	
	
	/**
	 * 
	 * @param second
	 */
	private void go(){
		String targetUrl = "";
		synchronized (this) {
			targetUrl = mission.poll();
			if(null!=targetUrl){
				catchUrl(targetUrl);
		        try {
					Thread.sleep(sleepTime*1000);
				} catch (InterruptedException e) {
					e.printStackTrace(System.err);
				}
			}
			notify();
		}

		
	}
	
	
	
	/**
	 * 设置网页编码，默认utf-8
	 * @param charset 编码名字
	 * @return this
	 */
	public SpiderApp setCharset(String  charset){
		this.charset = charset;
		return this;
	}
	


	
	/**
	 * 管理爬虫信息块的线程
	 */
	private class Manager extends Thread{
		
		private  int aliveNum = 0;
		private String spiderBegUrl;
		public Manager(int aliveNum,String spiderBegUrl){
			this.aliveNum = aliveNum;
			this.spiderBegUrl = spiderBegUrl;
			this.start();
			
		}
		
		public void dec(int number){
			aliveNum-=number;
		}
		
		
		private void spider2Json(){
			Map<String,Object> map = new HashMap<String,Object>();
			Gson gson = new Gson();
			map.put("notVisited", visited);
			map.put("mission", mission);
			map.put("threamNum", threadNum);
			map.put("pageNum", pageNum);
			map.put("nowNum", nowNum);
			map.put("beforeFlag",flag);
			map.put("tojson",tojson);
			map.put("jsonFileName",jsonFileName);
			map.put("cookie",cookie);
			map.put("timeout",timeout);
			map.put("sleepTime",sleepTime);
			map.put("charset",charset);
			map.put("logger",new Gson().toJson(logMap));
			map.put("data",new Gson().toJson(datas));
			map.put("parms",new Gson().toJson(parms));	
			String jsonStr = gson.toJson(map);
			try {
				FileWriter out = new FileWriter(jsonFileName);
				if(out != null){
					out.write(jsonStr);
					out.flush();
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace(System.err);
			}
		}
		
		@Override
		public void run(){	
			
			while(aliveNum >0){
				try {
					SpiderManager.dump(spiderBegUrl);
					if(tojson){
						spider2Json();						
					}
					Thread.sleep(2000);
				} catch (Exception e) {
					logger.error(e.getMessage());
					e.printStackTrace(System.err);
				}			
			}
			//线程退出	
			try {
				SpiderManager.delete(spiderBegUrl);
			} catch (IOException e) {
				logger.error(e.getMessage());
				e.printStackTrace(System.err);
			}
		}
	}
	
	
	//线程管理者
	private Manager manager = null;
	
	
	/**
	* 爬虫初始化
	*/
	private void init(){
		if(url != null){
			mission.add(url);
		}
		manager =  new Manager(threadNum,url);
		if(null!=logger){
			SpiderInfo.setLogger(logger);
			logger.info("Begin url: " + url);
			logger.info("PageNum: " + pageNum);
			logger.info("ThreadNum: " + threadNum);
			logger.info("Timeout: " + timeout);
			logger.info("SleepTime: " + sleepTime);
			logger.info("Spider start……");
		}
	}
	
	
	/**
	 * 根据json配置
	 * @param json
	 */
	@SuppressWarnings("unchecked")
	private void configure(JsonElement json){
		
		Type listType = new TypeToken<Map<String,Object>>() {
        }.getType();
		Map<String,Object> map = new Gson().fromJson(json, listType);
		System.out.println(map);
		Object value =  map.get("notVisited");
		if(value != null){
			Queue<String> queue = new LinkedList<>();
			for(String t:(ArrayList<String>)value){
				queue.add(t);
			}
			setVisted(queue);
		}
		value =  map.get("mission");
		if(value != null){
			Queue<String> queue = new LinkedList<>();
			for(String t:(ArrayList<String>)value){
				queue.add(t);
			}
			url = queue.poll();
			setMission(queue);
		}
		value =  map.get("threamNum");
		if(value != null){
			int number = (int)Double.parseDouble(value.toString());
			setThreadNum(number);
		}
		value =  map.get("pageNum");
		if(value != null){
			int number = (int)Double.parseDouble(value.toString());
			System.out.println(number);
			setPageNum(number);
		}
		value =  map.get("nowNum");
		if(value != null){
			int number = (int)Double.parseDouble(value.toString());
			nowNum = number;
		}
		value =  map.get("beforeFlag");
		if(value != null){
			boolean temp = Boolean.parseBoolean(value.toString());
			setFalg(temp);
		}
		value =  map.get("tojson");
		if(value != null && map.get("jsonFileName")!= null){
			boolean temp = Boolean.parseBoolean(value.toString());
			setToJSON(temp,map.get("jsonFileName").toString());
		}else if(value != null){
			boolean temp = Boolean.parseBoolean(value.toString());
			setToJSON(temp);
		}
		value = map.get("cookie");
		if(value !=null){
			this.cookie = value.toString();
		}
		value =  map.get("timeout");
		if(value != null){
			int number = (int)Double.parseDouble(value.toString());
			setTimeout(number);
		}
		value =  map.get("sleepTime");
		if(value != null){
			int number = (int)Double.parseDouble(value.toString());
			setSleepTime(number);
		}
		value = map.get("charset");
		if(value !=null){
			charset = value.toString();
		}
		value = map.get("logger");
		if(value !=null){
			JsonElement json2 = new JsonParser().parse(value.toString());
			if(!json2.isJsonNull()){
				Map<String,Object> data = new Gson().fromJson(json2, listType);
				int level = (int)Double.parseDouble(data.get("level").toString());
				String fileName = data.get("fileName").toString();
				boolean append = Boolean.parseBoolean(data.get("append").toString());
				Object configFilename = data.get("configFilename");
				if(configFilename!=null){
					setLogerConfig(level, fileName, append, configFilename.toString());
				}else{
					setLogerConfig(level, fileName, append,null);
				}
				
			}
		}
		
		value = map.get("data");
		if(value != null){
			JsonElement json2 = new JsonParser().parse(value.toString());
			if(!json2.isJsonNull()){
				Map<String,String> data = new Gson().fromJson(json2, listType);
				setDatas((HashMap<String,String>)data);
			}
		}
		
		value = map.get("parms");
		if(value != null){
			JsonElement json2 = new JsonParser().parse(value.toString());
			if(!json2.isJsonNull()){
				Map<String,String>parm = new Gson().fromJson(json2, listType);
				setHanderParms((HashMap<String,String>)parm);
			}
		}
	}
	
	/**
	 * 根据文件名载入配置
	 * @param filename
	 * @return
	 */
	public SpiderApp loadConfigure(String filename){
		String result = "";
		String line;
		try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
			while ((line = in.readLine()) != null) {
			  result += line;
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
		JsonElement json = new JsonParser().parse(result);
		configure(json);
		return this;
	}
	
	/**
	 * 启动爬虫
	 */
	public final synchronized void run() {
		init();
		if(threadNum > 1){
			//多线程爬取
			Runnable[] threads= null;
			ThreadPool pool = ThreadPool.createPool(threadNum);
			
			int urlNum = 0;
			while(!mission.isEmpty()&& nowNum<=pageNum){
				threads = new Runnable[30];
				for(int i = 0;(i<threads.length)&&(urlNum<pageNum);i++){;
					//每增加一个线程，实际就是访问多一个url
					threads[i] = new Runnable() {				
						@Override
						public void run() {
							go();
						}
					};
					urlNum++;
				}
				pool.exe(threads);
				threads = null;
				try {
					//防止mission的判空出错
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace(System.err);
				}
			}
			pool.destory();
			manager.dec(threadNum);
		}else{
			while(!mission.isEmpty()&& nowNum<=pageNum){
				go();
			}
			manager.dec(1);
		}
	}
	
}
