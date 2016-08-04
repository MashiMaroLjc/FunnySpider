package funnyspider.util;
import funnyspider.SpiderInfo;
/**
 * 处理非正常访问,非重定向响应的方法的接口
 */
public interface BadRequestHandle {
	public void bHandle(SpiderInfo spiderInfo,int code);
}
