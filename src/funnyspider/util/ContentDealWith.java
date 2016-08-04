package funnyspider.util;

import funnyspider.SpiderInfo;

/**
 * 默认对文本的处理方式，其提示当前url访问成功
 */
public class ContentDealWith implements ContentHandle{
	@Override
	public void cHandle(SpiderInfo spiderInfo,String respondContent) {
		System.out.println(spiderInfo.getUrl() + "  finish！");
	}
}
