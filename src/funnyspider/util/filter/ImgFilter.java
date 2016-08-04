package funnyspider.util.filter;


/**
 * 在html中过滤出img内的图片地址
 */
public class ImgFilter extends Filter{
	
	/**
	 * 从整个标签里面返回src
	 * @param imgTag
	 * @return src
	 */
	
	public static String getSrc(String imgTag){
		
		int begin = imgTag.indexOf("src=");
		int len = "src=\"".length();
		int end = imgTag.indexOf("\"",begin + len);
		if(end == -1){
			end =  imgTag.indexOf("'",begin + len);
		}
		if(begin != -1&&end !=-1){
			return  imgTag.substring(begin + len,end);
		}else{
			return null;
		}
		
	}

	
	
	/**
	 * 从html里面提出img标签
	 * @param html
	 * @return img标签数组
	 */
	public static String[] getImgTag(String html){
		String pattern = "<img\\b[^>]*\\bsrc\\b\\s*=\\s*('|\")"
				+ "?([^'\"\n\r\f>]+(\\.jpg|\\.bmp|\\.eps|\\.gif|\\.mif|\\.miff|\\.png|"
				+ "\\.tif|\\.tiff|\\.svg|\\.wmf|\\.jpe|\\.jpeg|\\.dib|\\.ico|\\.tga|"
				+ "\\.cut|\\.pic)\\b)[^>]*>";	
		return reFilter(html,pattern);
	}
	
	
	
	/**
	 * 从html里面获取到全部的imgtag里面的src
	 * @param html
	 * @return src数组
	 */
	
	public static String[] filter(String html){
		String[] imgUrls = getImgTag(html);
		for(int i = 0;i<imgUrls.length;i++){
			imgUrls[i] = getSrc(imgUrls[i]);
		}
		return  imgUrls;
	}
}
