package funnyspider.util;

import funnyspider.SpiderInfo;

/**
 * 处理页面跳转时的类必须实现该接口
 */
public interface JumpHandle {
	//返回下一页的url
	public String[] jHandle(SpiderInfo spiderInfo,String respondContent);
}
