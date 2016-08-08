package funnyspider.util;



import funnyspider.SpiderInfo;
import funnyspider.util.net.Download;


/**
 * 实现响应处理的接口，用于保存html文档
 */
public class SaveFile implements ContentHandle {

	
	private int nameNum = 1;
	private String charset = "utf-8";
	
	public SaveFile() {
	}
	
	public SaveFile(String charset){
		this.charset=charset;
	}
	
	@Override
	public void cHandle(SpiderInfo sinfo,String html) {
		String filename = nameNum + ".html";
		try {
			Download.saveString(html, filename,charset);
			nameNum += 1;
			sinfo.outputMessage(filename +" --------------> 下载成功",System.out);
		} catch (Exception e) {
			sinfo.outputMessage(e.getMessage(),System.err);
		}
	}

}
