package springbook.user.service;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class TransactionAdvice implements MethodInterceptor{
	PlatformTransactionManager transactionManager;
	
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		// TODO Auto-generated method stub
		TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());

		try {
//			userService.upgradeLevels();
			//Method.invoke()를 이용해 타깃 오브젝트의 메소드를 호출할 때는 예외가 InvocationTragetException으로 한 번 포장되서 전달됨.
			Object ret = invocation.proceed();
			this.transactionManager.commit(status);
			return ret;
		} catch (RuntimeException e) {
			this.transactionManager.rollback(status);
			throw e; //중첩된 예외 가져오기
		}
	}
	
}
