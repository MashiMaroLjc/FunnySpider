
package funnyspider.util.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;



/**
 * Http工具，提供常用的get,post方法，并提供获取输入流，正文等方法
 */
public class HttpRequest {

	private String url;
	
	private HttpURLConnection conn;
	
	public HttpRequest(String url){
		this.url = url;
		
	}
	
	public String getUrl(){
		return url;
	}
	
	public HttpURLConnection getConnect(){
		return conn;
	}
	
	
	
	private String cookie = null;
	
	public void setCookie(String cookie){
		this.cookie = cookie;
	}
	
	
	/**
	 * 设置conn
	 * @param parms
	 * @param second
	 */
	private void setConnection(String url,HashMap<String, String> parms,int second)
			throws Exception{

		conn = (HttpURLConnection)new URL(url).openConnection();
		conn.setConnectTimeout(second * 1000);
		if (parms != null){
			for(HashMap.Entry<String, String> entry:parms.entrySet()){
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}else{
			//参数为空设置默认参数
			conn.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			conn.setRequestProperty("Accept-Language","zh-CN,zh;q=0.8");
	        conn.setRequestProperty("connection", "Keep-Alive");
	        conn.setRequestProperty("user-agent",
	                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	        conn.setRequestProperty("Charset", "utf-8");	        
		}
		if(cookie!=null){
			conn.setRequestProperty("Cookie",cookie);
		}
	}
	
	/**
	 * get方法获取链接
	 * @param parms 头部参数
	 * @param data 数据
	 * @param second 连接超时秒
	 * @return
	 * @throws Exception
	 * */
	public HttpURLConnection open(	HashMap<String, String> parms,
									HashMap<String, String> data,
									int second)
			throws Exception{
		String Data = "";
        if(data!=null){
        	for(HashMap.Entry<String, String> entry:data.entrySet()){
				Data +=  entry.getKey()+ "="+entry.getValue() + "&";
			}
        	Data = Data.substring(0, Data.length()-1);
        }
       String tempUrl = url +  "?" + URLEncoder.encode(Data,"utf-8");
		setConnection(tempUrl,parms, second);
        conn.connect();
        return conn;
	}
	
	
	/**
	 * post方法打开链接
	 * @param parms 头部参数
	 * @param data 数据
	 * @param second 连接超时秒
	 * @return
	 * @throws Exception
	 */
	public HttpURLConnection post(HashMap<String, String> parms,
			HashMap<String, String> data,int second)throws Exception{
		setConnection(url,parms, second);
		conn.setDoOutput(true);
        conn.setDoInput(true);
        PrintWriter out = new PrintWriter(conn.getOutputStream());
        // 发送请求参数
        String postData = "";
        if(data!=null){
        	for(HashMap.Entry<String, String> entry:data.entrySet()){
				postData +=  entry.getKey()+ "="+entry.getValue() + "&";
			}
        	postData = postData.substring(0, postData.length()-1);
        }
        out.print(URLEncoder.encode(postData, "utf-8"));
        // flush输出流的缓冲
        out.flush();
        
		return conn;
	}
	
	
	/**
	 * 获取输入流
	 * @return
	 * @throws Exception
	 */
	public InputStream getInputStream() throws Exception{
		if(conn != null){
			return conn.getInputStream();
		}else{
			throw new NullPointerException("you need open the connect!");
		}
	}
	
	/**
	 * 获取响应的utf-8正文
	 * @return
	 * @throws IOException
	 */
	
	public String getBody() throws IOException{
		return getBody(null);
	}
	
	/**
	 * 
	 * @param charset 字符编码
	 * @return
	 * @throws IOException
	 */
	public String getBody(String charset) throws IOException{
		if (conn == null){
			throw new NullPointerException("you need open the connect!");
		}
		String result = "";
		if(charset == null){
			charset = "utf-8";
		}
		BufferedReader in = new BufferedReader(new InputStreamReader(
				conn.getInputStream(),charset));
		String line;
		while ((line = in.readLine()) != null) {
		  result += line + "\n";
		}
		in.close();
    	return result;
	}
	
}
