package funnyspider;

import java.io.File;
//import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 爬虫管理类
 */
public class SpiderManager {

	private static String fatherDir = "temp";
	
	/**
	 * 写入文件
	 * @param BegUrl
	 * @throws IOException
	 */
	protected static synchronized void dump(String BegUrl) throws IOException{
		String name = fatherDir +File.separator+ "spider-"  
					+ BegUrl.replace("/", "-").replace(":", "-")
					+ ".temp";
		File file = new File(fatherDir);
		if(!file.exists()){
			file.mkdir();
		}
		FileWriter out = new FileWriter(name);
		out.write(SpiderInfo.dump());
		out.flush();
		out.close();
	}
	
	/**
	 * 线程正常完成删除文件
	 * @throws IOException
	 */
	protected static synchronized void delete(String BegUrl) throws IOException{
		String name = fatherDir +File.separator+ "spider-"  
					+ BegUrl.replace("/", "-").replace(":", "-")
					+ ".temp";
		new File(name).delete();
	}
	
//	/**
//	 * 在文件内容里面过滤标签
//	 * @param str
//	 * @param tag
//	 * @return
//	 * @throws Exception
//	 */
//	private static String filter(String str,String tag)
//		throws Exception{
//		String temp = "<"+tag+">";
//		int index1 = str.indexOf(temp);
//		int index2 = str.indexOf("</" + tag+ ">");
//		String res = "";
//		if(index1 == -1 || index2 ==-1){
//			throw new IllegalArgumentException("the File error!");
//		}else{
//			res = str.substring(index1+temp.length() ,index2);
//		}
//		return res;
//	}
//	
	
//	/**
//	 * 读取文件
//	 * @throws FileNotFoundException 
//	 */
//	private static synchronized void check(String path) 
//			throws Exception{
//		File f = new File(path);
//		if(!f.exists()) return ;
//		while(!f.canRead());
//		FileInputStream in = new FileInputStream(f);
//		byte[] buffer = new byte[1024];
//		int ch = in.read(buffer);
//		in.close();
//		String res = new String(buffer,0,ch);
//		System.out.print("begin_url: " + filter(res, "begin_url"));
//		System.out.print("    alive: " + filter(res, "alive"));
//		System.out.println("    schedule: " + filter(res, "nowPageNum")
//				+ "/"+filter(res, "allallPageNum"));
//	}
//	
	
//	/**
//	 * 管理程序
//	 * @param comm
//	 * c 表示查看运行情况
//	 * @throws FileNotFoundException 
//	 */
//	private static void manager(String comm) 
//			throws Exception{
//		if("c".equals(comm)){
//			File f = new File(fatherDir);
//			File[] tempList = f.listFiles();
//			for(File file:tempList){
//				if(file.isFile()){
//					check(file.getParent());
//				}
//			}	
//		}
//	}
	
	
}
