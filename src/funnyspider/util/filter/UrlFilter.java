package funnyspider.util.filter;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlFilter {
	
	/**
	 * 检查url的合法性
	 * @param url
	 * @return
	 */
	public static boolean isUrl(String url){
		  if(url == null){
	            return false;
	      }
	      String regEx = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
	      Pattern p = Pattern.compile(regEx);
	      Matcher matcher = p.matcher(url);
	      return matcher.matches();
	}
	
	/**
	 * 检查url是否含有str
	 * @param str
	 * @param url
	 * @return true为含有，反之为不含有
	 */
	public static boolean check(String str,String url) 
			throws UnsupportedEncodingException,IllegalArgumentException {
		if(!isUrl(url)){
			throw new IllegalArgumentException(url + "is not correct URL");
		}
		String str2 = URLEncoder.encode(str, "utf-8");
		String url2 = URLEncoder.encode(url,"utf-8");
		return url2.contains(str2);
	}
	
	
	/**
	 * 检查url是否为该host的子站
	 * @param hostUrl
	 * @param url
	 * @return true为是，反之为否
	public static boolean hostCheck(String hostUrl,String url)
		throws Exception{
		return check(new URL(hostUrl).getHost(),url);
	}
	
	
	/**
	 * 检查url是否为授权页面
	 * @param hostUrl
	 * @param url
	 * @return true是，反之为否
	 */
	public static boolean authoritytCheck(String hostUrl,String url)
		throws Exception{
		return check(new URL(hostUrl).getAuthority(),url);
	}
	

	
}
