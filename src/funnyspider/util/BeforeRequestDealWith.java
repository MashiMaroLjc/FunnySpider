package funnyspider.util;
import funnyspider.SpiderInfo;

/**
 *默认访问前的操作，就是不做任何操作
 */
public class BeforeRequestDealWith implements BeforeReuqestHandle{
	@Override
	public void brHandle(SpiderInfo spiderInfo) {
	}
}
