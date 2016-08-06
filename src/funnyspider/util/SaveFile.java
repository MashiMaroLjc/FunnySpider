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
		// TODO Auto-generated constructor stub
	}
	
	public SaveFile(String charset){
		this.charset=charset;
	}
	
	@Override
	public void cHandle(SpiderInfo sinfo,String html) {
		// TODO Auto-generated method stub
		String filename = nameNum + ".html";
		try {
			Download.saveString(html, filename,charset);
			nameNum += 1;
			System.out.println(filename +" --------------> 下载成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace(System.err);
		}
	}

}
