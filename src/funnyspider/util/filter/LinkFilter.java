package funnyspider.util.filter;

public class LinkFilter extends Filter{

	
	
	/**
	 * 在&lt;a&gt; href=""&gt;&lt;/a&gt;的格式中，提取出href的地址出来
	 * @param aTag
	 * @return  超链接地址
	 */
	public static String getLink(String aTag){
		int begin = aTag.indexOf("href=");
		int len = "href=\"".length();
		int end = aTag.indexOf("\"",begin + len);
		if(-1==end){
			end = aTag.indexOf("\'",begin + len);
		}
		if(begin != -1&&end !=-1){
			return  aTag.substring(begin + len,end);
		}else{
			return null;
		}
		
	}
	
	
	
	/**
	 * 在html中过滤出所有的a标签
	 * @param html
	 * @return a标签数组
	 */
	public static String[] getLinkTag(String html){
		String pattern = "<a[^>]*href=(\"([^\"]*)\"|\'([^\']*)\'|([^\\s>]*))[^>]*>(.*?)</a>";
		return reFilter(html, pattern);
	}
	
	/**
	 * 在html中过滤出所有的超链接地址
	 * @param html
	 * @return 超链接地址数组
	 */
	public static String[] filter(String html){
		String[] result = getLinkTag(html);
		for(int i = 0;i<result.length;i++){
			result[i] = getLink(result[i]);
		}
		return result;
	}
}
