package springbook.user.service;

import springbook.user.dao.User;

//트랜잭션 테스트용
public class TestUserService extends UserServiceImpl{
	
	static class TestUserServiceException extends RuntimeException{

	}

	private String id;
	
	public TestUserService(String id) {
		// TODO Auto-generated constructor stub
		this.id = id;
	}

	@Override
	protected void upgradeLevel(User user) {
		// TODO Auto-generated method stub
		if(user.getId().equals(this.id)) throw new TestUserServiceException();
		super.upgradeLevel(user);
	}
	
}
