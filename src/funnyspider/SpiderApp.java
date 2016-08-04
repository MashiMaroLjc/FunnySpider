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
	private int maxThreadNum = 10;
	
	//总共访问的次数
	private int pageNum = 1;
	private int nowNum = 1;
	
	//爬虫休眠参数
	private int sleepTime = 1;
	
	//http头部参数
	private HashMap<String,String> parms = null;
	
	//http get或post的数据
	private HashMap<String,String> datas = null;
	
	//是否为守护线程
	private boolean isDaemo = false;
	
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
	 * 设置是否为守护线程
	 * @param flag 
	 * @return this
	 */
	
	public SpiderApp setThreadDemo(boolean flag){
		isDaemo = flag;
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
	 * 
	 * @param charset
	 * @param second
	 */
	private void go(String charset,int second){
		
		String content = new String();
		HttpURLConnection conn = null;
		
		while(!mission.isEmpty() && nowNum<=pageNum){
			String now_url = mission.poll();
			//循环遍历任务队列
			SpiderInfo sInfo = SpiderInfo.createInfoBlock(now_url, pageNum);
			try{
				if(nowNum == 1 || flag){
					berforeReaquestHandle.brHandle(sInfo);
					this.cookie = sInfo.getCookie();
				}
				if(visited.contains(now_url)){
					continue;
				}
				visted(now_url);
				HttpRequest hr = new HttpRequest(now_url);
				hr.setCookie(cookie);
				conn = hr.open(parms,datas,second);
		        int code = conn.getResponseCode();
		       
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
		        Thread.sleep(sleepTime*1000);
			}catch(Exception err){
				errHandle.handle(err);
			}
			
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
		
		public void dec(){
			aliveNum--;
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
	
	
	
	
	
	/**
	 * 启动爬虫
	 * @param timeoutSecond 每次访问的timeout秒数
	 */
	public final void run(int timeoutSecond) {
		mission.add(url);	
		Manager ma =  new Manager(threadNum,url);
		if(threadNum > 1){
			//多线程爬取
			for(int i = 0;i<threadNum;i++){
				Thread t = new Thread(){
					@Override
					public void run(){
						go(charset,timeoutSecond);
						ma.dec();
					}
				};
				t.setDaemon(isDaemo);
				t.start();
			}
		}else{
			go(charset,timeoutSecond);
			ma.dec();
		}
	}
	
}
