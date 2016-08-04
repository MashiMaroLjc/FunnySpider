package funnyspider.util;


/**
 * 默认的异常处理类,向标准错误流输出错误信息
 */
public class ExceptionDealWith implements ExceptionHandle{
	@Override
	public  void handle(Exception err) {
		// TODO Auto-generated method stub
		System.err.println("Cause:" + err.getCause() + "  Reason: " + err.getMessage());
		err.printStackTrace(System.err);
	}	
}
