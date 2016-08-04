package funnyspider.util;

import funnyspider.SpiderInfo;
import funnyspider.util.net.Download;
import funnyspider.util.filter.ImgFilter;


/**
 * 实现响应处理的接口，用于保存图片
 */
public class SaveImage implements ContentHandle {
	private int nameNum = 1;
	@Override
	public void cHandle(SpiderInfo sinfo,String html)  {
		String[] imgs = ImgFilter.filter(html);
		int subNum = 1;
		for(String img:imgs){
			try {
				Download.saveBinaryFromUrl(img, nameNum + "-" + subNum + ".jpg", null, 5);
				subNum +=1;
				System.out.println(img+" --------------> 下载成功");
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
		nameNum += 1;
	}
}
