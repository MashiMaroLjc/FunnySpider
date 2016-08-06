package funnyspider;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import funnyspider.util.SaveFile;
import funnyspider.util.filter.HtmlNode;
import funnyspider.util.filter.SimpleHtmlSoup;




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
	public static void main(String[] args) {
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
//		System.out.println(pool);
//		String url = "http://www.meizitu.com";
//		new SpiderApp(url).setPageNum(20).
//			setContentHandle(new SaveFile("gbk")).
//			setCharset("gbk").setThreadNum(20)
//			.run(2);

		try {
			String result = "";
			BufferedReader in = new BufferedReader(new FileReader("1.html"));
			String line;
			try {
				while ((line = in.readLine()) != null) {
				  result += line + "\n";
				}
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SimpleHtmlSoup soup = new SimpleHtmlSoup(result);
			HtmlNode[] links = soup.getNode("li");
			for(HtmlNode link:links){
				System.out.println(link.getPropertys());
			}
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		
	}

}
