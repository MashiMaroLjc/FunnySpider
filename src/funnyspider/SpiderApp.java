package funnyspider;



import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

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
	
	
	
	//cookie
	private String cookie = null;
	
	/**
	 * 构造方法
	 * @param url
	 */
	public SpiderApp(String url){
		this.url = url;
	}
		
	
	
	/**
	 * 设置访问时携带的数据
	 * @param datas
	 * @return this
	 */
	public SpiderApp setMethodDatas(HashMap<String,String> datas){
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
	
	
	/**
	 * 获取爬取页数
	 * @return pageNum
	 */
	public int getPageNum(){
		return pageNum;
	}
	
	/**
	 * 设置线程数，超出指定范围无效
	 * @param num 线程数[1,10]
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
	private void catchUrl(String targetUrl,int second){
		String now_url = targetUrl;
		HttpURLConnection conn = null;
		SpiderInfo sInfo = SpiderInfo.createInfoBlock(now_url, pageNum);
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
			conn = hr.open(parms,datas,second);
	        int code = conn.getResponseCode();
	        dealWithCode(code, hr, sInfo, conn);
		}catch(Exception err){
			errHandle.handle(err);
		}
	}
	
	
	/**
	 * 
	 * @param second
	 */
	private void go(int second){
		String targetUrl = "";
		synchronized (this) {
			targetUrl = mission.poll();
			if(null!=targetUrl){
				catchUrl(targetUrl,second);
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
	private  static class Manager extends Thread{
		
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
		
		@Override
		public void run(){	
			while(aliveNum >0){
				try {
					SpiderManager.dump(spiderBegUrl);
					Thread.sleep(2000);
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}			
			}
			//线程退出	
			try {
				SpiderManager.delete(spiderBegUrl);
			} catch (IOException e) {
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
		mission.add(url);	
		manager =  new Manager(threadNum,url);
	}
	
	
	
	
	/**
	 * 启动爬虫
	 * @param timeoutSecond 每次访问的timeout秒数
	 */
	public final synchronized void run(int timeoutSecond) {
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
							go(timeoutSecond);
							System.out.println(" Name: "+ Thread.currentThread().getName());
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
				go(timeoutSecond);
			}
			manager.dec(1);
		}
	}
	
}
