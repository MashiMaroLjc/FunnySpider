package funnyspider.util;

import funnyspider.SpiderInfo;


/**
 * 默认的异常处理类,向日志或标准错误流输出错误信息
 */
public class ExceptionDealWith implements ExceptionHandle{

	@Override
	public void handle(SpiderInfo spiderInfo, Exception err) {
		spiderInfo.outputMessage(err.getMessage(), System.err);
		err.printStackTrace(System.err);
	}

}
