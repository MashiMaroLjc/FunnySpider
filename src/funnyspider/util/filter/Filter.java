package funnyspider.util.filter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Filter {
	/**
	 * 使用正则表达式过滤
	 * @param str
	 * @param pattern
	 * @return 过滤结果组成数数组
	 */
	public static final String[] reFilter(String str,String pattern){
		Pattern pa = Pattern.compile(pattern);
		Matcher matcher = pa.matcher(str);
		ArrayList<String> result = new ArrayList<String>();
		while(matcher.find()){
			result.add( matcher.group());
		}
		return (String[]) result.toArray(new String[result.size()]);
	}
	
	
	
	/**
	 * 以newChar替换所有的html标签
	 * @param html
	 * @param newChar
	 * @return 替换后的html标签
	 */
	public static final String replaceTag(String html,String newChar){
		Pattern pattern = Pattern.compile("<.+?>", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(html);
		return matcher.replaceAll(newChar);
	}
	
	
	/**
	 * 去除html的标签
	 * @param html
	 * @return 清除后的文本
	 */
	public static final String filterTag(String html){
		return replaceTag(html, "");
	}
	
}
