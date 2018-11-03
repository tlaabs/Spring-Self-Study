package springbook.user.service;

import java.lang.reflect.Proxy;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import springbook.learningtest.jdk.TransactionHandler;

public class TxProxyFactoryBean implements FactoryBean<Object>{
	Object target;
	PlatformTransactionManager transactionManager;
	String pattern;
	Class<?> serviceInterface;
	
	
	
	public void setTarget(Object target) {
		this.target = target;
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public void setServiceInterface(Class<?> serviceInterface) {
		this.serviceInterface = serviceInterface;
	}

	@Override
	public Object getObject() throws Exception {
		// TODO Auto-generated method stub
		TransactionHandler txHandler = new TransactionHandler();
		txHandler.setTarget(target);
		txHandler.setTransactionManager(transactionManager);
		txHandler.setPattern(pattern);
		return Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] {serviceInterface}, txHandler);
	}

	@Override
	public Class<?> getObjectType() {
		// TODO Auto-generated method stub
		//UserService 이외에도 트랜잭션 부가기능이 필요한 오브젝트를 위한 프록시를 만들 대 재사용 가능. 설정만 바꿔주면됨.
		return serviceInterface;
	}

	@Override
	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return false;
	}
		
}
