package com.my.thread;

public class QueueObject {
	//������������ڷ�ֹ�����źŶ�ʧ
	
	//�Ƿ���
	private boolean isNotify=false;
	public void doWait() throws InterruptedException{
		//��ֹ���⻽�ѣ���ʱ�����wait�������п��ܻ���ֲ�����notify���������⻽�ѵ����
		//�������һ��isNotify��ǣ���ȷ�������������ʱ����������wait״̬
		while(!this.isNotify){
			this.wait();
		}
		this.isNotify = false;
	}
	public void doNotify(){
		this.isNotify = true;
		this.notify();
	}
	//�Ƚ�
	public boolean equals(Object o) {
		return this == o;
	}
}
