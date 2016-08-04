package funnyspider.util;

import funnyspider.SpiderInfo;


/**
 * 定义访问前进行的操作的接口
 */
public interface BeforeReuqestHandle {
	public void brHandle(SpiderInfo spiderInfo);
	
}
