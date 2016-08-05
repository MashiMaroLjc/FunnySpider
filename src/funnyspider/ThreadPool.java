package funnyspider;

import java.util.LinkedList;
import java.util.List;


/**
 * 线程池
 */
class ThreadPool {

	//线程池最大线程数
	private int maxThreadNum;
	private static ThreadPool self;
	private WorkThread[] pool;
	private List<Runnable> taskQueue = new LinkedList<Runnable>();  
	private boolean isRun = true;
	private volatile int finishTask = 0;
	private volatile int allTask = 0;
	
	private ThreadPool(int maxThreadNum){
		this.maxThreadNum = maxThreadNum;
		pool = new WorkThread[this.maxThreadNum];
		for(int i =0;i<this.maxThreadNum;i++){
			pool[i] = new WorkThread();
			pool[i].start();
		}
	}
	
	
	/**
	 * 单例模式，创建池
	 * @param ThreadNum
	 * @return
	 */
	public static ThreadPool createPool(int ThreadNum){
		if(null==self){
			self =  new ThreadPool(ThreadNum);
		}
		return self;
	}
	
	
	
	private boolean isFinish(){
		return finishTask >= allTask;
	}
	
	/**
	 * 销毁整个线程池
	 */
	public void destory(){
		if(self!=null){
			while(!isFinish()){
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace(System.err);
				}
			}
			for(int i = 0;i<pool.length;i++){
				pool[i].stopWorker();
				pool[i] = null;
			}
			self = null;
			taskQueue.clear();
		}
	}
	
	
	/**
	 * 执行线程
	 * @param task
	 */
	public void exe(Runnable[] tasks){
		synchronized (taskQueue) {
			for(Runnable task:tasks){
				if(task != null){
					taskQueue.add(task);
					allTask ++;
				}else{
					break;
				}
			}
			taskQueue.notify();
			
		}
	}
	
	
	
	public String toString(){
		int number = Thread.currentThread().getThreadGroup().activeCount();
		return "Thread number:" + number
				+ "  Queue number(Wait Task):" + taskQueue.size()
				+ "  Finish task:" + finishTask
				+ "  Need task:" + allTask;
			
	}
	
	/**
	 * 工作线程类
	 *
	 */
	private class WorkThread extends Thread{
		private Runnable getRunnable(){
			while(isRun && taskQueue.isEmpty()){
				synchronized (taskQueue) {
					try{
						taskQueue.wait(20);
					}catch(InterruptedException err){
						err.printStackTrace(System.err);
					}
				}
			}
			synchronized (pool) {
				if(!taskQueue.isEmpty()){
					return taskQueue.remove(0);
				}else{
					return null;
				}
			}
		}
		
		@Override
		public void run(){
			Runnable r =null;
			while(isRun){
				r = getRunnable();
				if(null != r){
					r.run();
					finishTask++;
				}
				r = null;
			}
		}
		
        public void stopWorker() {  
            isRun = false;  
        }  
		
	}
	
	
}
