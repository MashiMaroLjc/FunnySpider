package funnyspider.util;

import funnyspider.SpiderInfo;


/**
 * 默认非正常访问,非重定向响应的方法
 */
public class BadRequestDealWith implements BadRequestHandle {
	@Override
	public void bHandle(SpiderInfo spiderInfo,int code) {
		String reason = "Unknown";
		switch (code){
		case 304:
			reason = "Source don't update";
			break;
		case 400:
			reason = "Illeagl request";
			break;
		case 401:
			reason = "Unauthorized";
			break;
		case 403:
			reason = "Forbidden";
			break;
		case 404:
			reason = "Not Found";
			break;
		case 500:
		case 501:
		case 502:
		case 503:
		case 504:
			reason = "Server error";
		}
		spiderInfo.outputMessage(spiderInfo.getUrl() + " reason:" +  reason,
				System.err);	
	}

}
