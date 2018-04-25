package com.my.thread;

public class QueueObject {
	//该类的意义在于防止唤醒信号丢失
	
	//是否唤醒
	private boolean isNotify=false;
	public void doWait() throws InterruptedException{
		//防止意外唤醒，有时候调用wait函数后，有可能会出现不调用notify函数就意外唤醒的情况
		//所以添加一个isNotify标记，来确保出现意外情况时，继续保持wait状态
		while(!this.isNotify){
			this.wait();
		}
		this.isNotify = false;
	}
	public void doNotify(){
		this.isNotify = true;
		this.notify();
	}
	//比较
	public boolean equals(Object o) {
		return this == o;
	}
}
