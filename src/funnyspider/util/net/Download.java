package funnyspider.util.net;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;





/**
 * 下载工具类，提供下载文本和二进制文件的方法
 */
public class Download {
	
	/**
	 * 下载文本
	 * @param str
	 * @param filename
	 * @throws IOException
	 */
	public static void saveString(String str,String filename,String charset)
			throws IOException{
		if(str != null){
			OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(filename),charset);
			fw.write(str);
			fw.flush();
			fw.close();
		}
		
	}
	
	
	
	/**
	 * 以字节流的形式下载
	 * @param datas
	 * @param filename
	 * @throws IOException
	 */
	public static void saveBinary(byte[] datas,String filename)
		throws IOException{
		if (null != datas){
			FileOutputStream out = new FileOutputStream(filename);
			out.write(datas);
			out.flush();
			out.close();
		}
		
		
	}
	
	
	/**
	 * 访问URL下载二进制文件
	 * @param url
	 * @param filename
	 * @param parms
	 * @param second
	 * @throws Exception
	 */
	public static void saveBinaryFromUrl(String url,String filename,HashMap<String, String> parms,int second)
		throws Exception{
		InputStream myIn = new HttpRequest(url).open(parms,null,second).getInputStream();
		byte[] buffer = new byte[1024];
		int ch = 0;
		FileOutputStream out = new FileOutputStream(filename);
		while((ch = myIn.read(buffer))!=-1){
			out.write(buffer,0,ch);
		}
		out.flush();
		out.close();
	}
	
	
	
}
