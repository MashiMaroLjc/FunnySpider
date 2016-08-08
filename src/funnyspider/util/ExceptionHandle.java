package funnyspider.util;

import funnyspider.SpiderInfo;

/**
 * 定义如何处理爬取过程中出现的问题的接口
 *
 */

public interface ExceptionHandle {
	public void handle(SpiderInfo spiderInfo,Exception err);
}
