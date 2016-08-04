package funnyspider.util;

import java.net.HttpURLConnection;
import java.util.HashMap;

import funnyspider.SpiderInfo;
import funnyspider.util.net.HttpRequest;

/**
 * 实现了预处理接口，用于登陆
 *
 */
public class Login implements BeforeReuqestHandle{

	
	
	private String loginUrl;
	
	private HashMap<String,String> data = null;
	
	private HashMap<String,String> parms = null;
	
	private int method;
	
	
	public static final int GET_METHOD = 0;
	
	
	public static final int POST_METHOD = 1;
	/**
	 * 
	 * @param url 登陆检验的url
	 * @param parms 头部参数
	 * @param data 登陆数据
	 * @param method 数据交换方式，一般使用POST_METHOD
	 */
	public Login(String url,
					HashMap<String,String> parms,
					HashMap<String,String> data,
					int method) {
		this.loginUrl = url;
		this.parms = parms;
		this.method = method;
		this.data = data;
	}
	
	@Override
	public void brHandle(SpiderInfo spiderInfo) {
		// TODO Auto-generated method stub
		try{
			HttpURLConnection conn = null;
			if(method == GET_METHOD){
				conn =  new HttpRequest(loginUrl).open(parms,data,5);
			}else if(method == POST_METHOD){
				conn =  new HttpRequest(loginUrl).post(parms,data,5);
			}else{
				throw new IllegalArgumentException("method is illegal!");
			}
			String cookie = conn.getHeaderField("Set-Cookie");
			String oldCookie = spiderInfo.getCookie();
			if(oldCookie == null){
				oldCookie = "";
			}
			if(cookie != null){
				spiderInfo.setCookie(cookie + oldCookie);
			}else{
				throw new NullPointerException("Set-Cookie is null");
			}
		}catch(Exception err){
			System.err.println(err.getMessage());
			spiderInfo.kill();
		}
	}

}
