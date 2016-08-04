package funnyspider.util;

import funnyspider.SpiderInfo;


/**
 * 对响应内容的类必须实现该接口
 */
public interface ContentHandle {
	public void cHandle(SpiderInfo spiderInfo,String respondContent);
}
