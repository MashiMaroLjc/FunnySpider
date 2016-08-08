package funnyspider;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.DailyRollingFileAppender;

import funnyspider.util.SaveFile;
import funnyspider.util.filter.HtmlNode;
import funnyspider.util.filter.SimpleHtmlSoup;
import funnyspider.util.net.Database;




public class Test {

//    // 任务类  
//    static class Task implements Runnable {  
//        private static volatile int i = 1;  
//  
//        @Override  
//        public void run() {// 执行任务  
//            System.out.println("任务 " + i + " 完成   Name:" + Thread.currentThread().getName());  
//            i++;
//        }  
//    }  
//	
	public static void main(String[] args) throws Exception {
//		ThreadPool pool = ThreadPool.createPool(3);
//		pool.exe(new Runnable[]{
//				new Task(),
//				new Task(),
//				new Task(),
//
//		});
//		pool.exe(new Runnable[]{
//				new Task(),
//				new Task(),
//				new Task(),	
//		});
//		System.out.println(pool);
//		pool.destory();
//		System.out.println(pool)
//		String url = "http://www.meizitu.com";
//		new SpiderApp(url).setPageNum(20).
//			setContentHandle(new SaveFile("gbk")).
//			setCharset("gbk").setThreadNum(10)
//			.setLogerConfig(6).setTimeout(2)
//			.setToJSON(true,"fuck.json")
//			.run();
//		Object a = null;
//		System.out.println((SpiderApp)a);
//		new SpiderApp().loadConfigure("fuck.json").setToJSON(true,"spider.json").run();
//		try {
//			String result = "";
//			BufferedReader in = new BufferedReader(new FileReader("1.html"));
//			String line;
//			try {
//				while ((line = in.readLine()) != null) {
//				  result += line + "\n";
//				}
//				in.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			SimpleHtmlSoup soup = new SimpleHtmlSoup(result);
//			HtmlNode[] links = soup.getNode("li");
//			for(HtmlNode link:links){
//				System.out.println(link.getPropertys());
//			}
//		} catch (FileNotFoundException e) {
//
//			e.printStackTrace();
//		}

	}

}
