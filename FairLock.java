package com.my.thread;

import java.util.ArrayList;
import java.util.List;

public class FairLock {
	//�Ƿ����
	private boolean isLock = false;
	//��ǰ���������߳�
	private Thread lockThread = null;
	//������д��������̵߳�QueueObject
	private List<QueueObject> waitThread = new ArrayList<QueueObject>();
	
	public void lock() throws InterruptedException{
		QueueObject queueObject = new QueueObject();
		////��ʼ���ã���ǰ�߳�Ϊ����״̬
		boolean isNowThreadLock = true;
		synchronized(this){
			//��ʼ���ã��������̵߳�queueObject������������
			waitThread.add(queueObject);
		}
		
		//��ǰ�߳�����״̬
		while(isNowThreadLock){
			//��ǰ����δ����&���������������һ���߳��ǵ�ǰ�̣߳���queueObject�����߳��ˣ�ʱ��
			//isNowThreadLock�Ż���false(����ǰ�̲߳������ˣ����������иö�����������ȼ����)
			//��Ϊ�漰����̹߳���ĳ�Ա������������Ҫ��ͬ��
			synchronized(this){
				isNowThreadLock = isLock || waitThread.get(0) != queueObject;
				//������̲߳�Ӧ����������Ӧ�û�ȡ��
				if(!isNowThreadLock){
					isLock = true;//�����Ѽ���
					waitThread.remove(0);//������ǰ�̵߳�queueObject�Ƴ���������
					lockThread=Thread.currentThread();//��ǰ���������߳�
					return;//������ɣ����� �ٽ�����λ��lock()��unlock()֮�䣩 �Ĵ���
				}
			}	
			//������߳�Ӧ������
			//�򽫴����̵߳�queueObjectִ��wait������
			//��ʱ���߳���������ֲ�����queueObject��wait������
			try {
				//�ֲ�����������������̲߳���Ӱ�죬����ͬ��
				queueObject.doWait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//�漰�̹߳������������
				//��������쳣���򽫸��߳��׳�
				synchronized(this){
					waitThread.remove(queueObject);
				}
				throw e;
			}
		}
	}
	
	public synchronized void unlock(){
		//�жϵ�ǰ�߳��Ƿ��ǳ��������߳�
		if(lockThread != Thread.currentThread()){
			//�׳��쳣
			throw new IllegalMonitorStateException(
					        "Calling thread has not locked this lock");
		}
		//ȥ���������
		isLock = false ;
		//��ǰ����û�ж��������
		lockThread = null;
		//�������������������һ���߳�
		if(waitThread.size() > 0){
			waitThread.get(0).doNotify();
		}
	}
}
