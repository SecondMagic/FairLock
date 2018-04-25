package com.my.thread;

public class SynchronizedTest {
	FairLock fairLock = new FairLock();
	public static void main(String[] args){
		final SynchronizedTest synchronizedTest = new SynchronizedTest();

		Thread thread1 = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					synchronizedTest.add();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		},"thread1");
		Thread thread2 = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					synchronizedTest.add();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		},"thread2");
		Thread thread3 = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					synchronizedTest.add();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		},"thread3");
		Thread thread4 = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					synchronizedTest.add();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		},"thread4");
		
		thread1.start();
		thread2.start();
		thread3.start();
		thread4.start();
	}
	
	public void add() throws InterruptedException{
		fairLock.lock();
		//保证当临界区抛出异常时fairLock对象可以被解锁
		try{
			for(Integer i=0;i<100;i++){
				System.out.println(Thread.currentThread().getName()+":"+i);
			}
		}finally{
			fairLock.unlock();
		}
	}
}
