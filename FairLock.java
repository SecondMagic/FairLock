package com.my.thread;

import java.util.ArrayList;
import java.util.List;

public class FairLock {
	//是否加锁
	private boolean isLock = false;
	//当前持有锁的线程
	private Thread lockThread = null;
	//存放所有代表阻塞线程的QueueObject
	private List<QueueObject> waitThread = new ArrayList<QueueObject>();
	
	public void lock() throws InterruptedException{
		QueueObject queueObject = new QueueObject();
		////初始设置，当前线程为阻塞状态
		boolean isNowThreadLock = true;
		synchronized(this){
			//初始设置，将代表线程的queueObject移入阻塞队列
			waitThread.add(queueObject);
		}
		
		//当前线程阻塞状态
		while(isNowThreadLock){
			//当前对象未加锁&阻塞队列中最近的一个线程是当前线程（用queueObject代表线程了）时，
			//isNowThreadLock才会是false(代表当前线程不阻塞了，即即将持有该对象的锁，优先级最大)
			//因为涉及多个线程共享的成员变量，所以需要加同步
			synchronized(this){
				isNowThreadLock = isLock || waitThread.get(0) != queueObject;
				//如果该线程不应该阻塞，即应该获取锁
				if(!isNowThreadLock){
					isLock = true;//对象已加锁
					waitThread.remove(0);//将代表当前线程的queueObject移出阻塞队列
					lockThread=Thread.currentThread();//当前持有锁的线程
					return;//加锁完成，继续 临界区（位于lock()和unlock()之间） 的代码
				}
			}	
			//如果该线程应该阻塞
			//则将代表线程的queueObject执行wait操作，
			//此时该线程阻塞在其局部变量queueObject的wait方法上
			try {
				//局部变量，不会对其他线程产生影响，不加同步
				queueObject.doWait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//涉及线程共享变量，加锁
				//如果出现异常，则将该线程抛出
				synchronized(this){
					waitThread.remove(queueObject);
				}
				throw e;
			}
		}
	}
	
	public synchronized void unlock(){
		//判断当前线程是否是持有锁的线程
		if(lockThread != Thread.currentThread()){
			//抛出异常
			throw new IllegalMonitorStateException(
					        "Calling thread has not locked this lock");
		}
		//去除对象的锁
		isLock = false ;
		//当前对象没有对象持有锁
		lockThread = null;
		//唤醒阻塞队列中最近的一个线程
		if(waitThread.size() > 0){
			waitThread.get(0).doNotify();
		}
	}
}
