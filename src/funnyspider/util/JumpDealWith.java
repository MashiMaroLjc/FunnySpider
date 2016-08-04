package funnyspider.util;
import funnyspider.SpiderInfo;
import funnyspider.util.filter.LinkFilter;


/**
 * 实现了跳转接口，直接获取全部连接
 */
public class JumpDealWith implements JumpHandle{
	@Override
	public String[] jHandle(SpiderInfo spiderInfo,String html) {
		return LinkFilter.filter(html);
	}
	
}
