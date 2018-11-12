package springbook.user.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static springbook.user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.mail.MailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import springbook.user.dao.Level;
import springbook.user.dao.User;
import springbook.user.dao.UserDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserServiceTest {

	@Autowired
	ApplicationContext context;

	// Autowired는 기본적으로 타입을 이용해 빈을 찾지만 만약 타입으로 하나의 빈을 결정할 수 없는 경우에는 필드 이름을 이용한다.
	@Autowired
	UserService userService;
	@Autowired
	UserService testUserService;

//	@Autowired
//	UserServiceImpl userServiceImpl;

	@Autowired
	DataSource dataSource;

	@Autowired
	UserDao userDao;

	@Autowired
	PlatformTransactionManager transactionManager;

	@Autowired
	MailSender mailSender;

	List<User> users;

	static class TestUserServiceException extends RuntimeException {
	}

	static class TestUserServiceImpl extends UserServiceImpl {

		private String id = "madnite1";

		@Override
		protected void upgradeLevel(User user) {
			// TODO Auto-generated method stub
			if (user.getId().equals(this.id))
				throw new TestUserServiceException();
			super.upgradeLevel(user);
		}

		// 트랜잭션 read-only 테스트를 위해 강제 오버라이드
		@Override
		public List<User> getAll() {
			// TODO Auto-generated method stub
			for (User user : super.getAll()) {
				super.update(user);
			}
			return null;
		}

	}

	static class MockUserDao implements UserDao {
		private List<User> users;
		private List<User> updated = new ArrayList();

		private MockUserDao(List<User> users) {
			this.users = users;
		}

		public List<User> getUpdated() {
			return this.updated;
		}

		@Override
		public List<User> getAll() {
			// TODO Auto-generated method stub
			return this.users;
		}

		@Override
		public void update(User user) {
			// TODO Auto-generated method stub
			updated.add(user);
		}

		@Override
		public void add(User user) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException();
		}

		@Override
		public User get(String id) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException();
		}

		@Override
		public void deleteAll() {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException();
		}

	}

	@Before
	public void setUp() {
		users = Arrays.asList(
				new User("bumjin", "박범진", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0, "a@naver.com"),
				new User("joytouch", "강명성", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0, "b@naver.com"),
				new User("erwins", "신승범", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1, "c@naver.com"),
				new User("madnite1", "김상진", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD, "d@naver.com"),
				new User("green", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE, "e@naver.com"));
	}

	@Test
	public void bean() {
		assertThat(this.userService, is(notNullValue()));
	}

	@Test
	@DirtiesContext // 컨텍스트의 DI 설정을 변경하는 테스트라는 것을 알려준다.
	public void upgradeLevels() throws Exception {

		UserServiceImpl userServiceImpl = new UserServiceImpl();

		MockUserDao mockUserDao = new MockUserDao(this.users);
		userServiceImpl.setUserDao(mockUserDao);

//		CommonUserLevelUpgradePolicy policy = new CommonUserLevelUpgradePolicy();
//		policy.setUserDao(mockUserDao);
//		userServiceImpl.setLevelPolicy(policy);
		// 목 오브젝트
		MockMailSender mockMailSender = new MockMailSender();
		userServiceImpl.setMailSender(mockMailSender);

		userServiceImpl.upgradeLevels();

		// Mock 오브젝트이므로 실제 db에 적용되지않는다.
		List<User> updated = mockUserDao.getUpdated();
		assertThat(updated.size(), is(2));
		checkUserAndLevel(updated.get(0), "joytouch", Level.SILVER);
		checkUserAndLevel(updated.get(1), "madnite1", Level.GOLD);
//		checkLevelUpgraded(users.get(0), false);
//		checkLevelUpgraded(users.get(1), true);
//		checkLevelUpgraded(users.get(2), false);
//		checkLevelUpgraded(users.get(3), true);
//		checkLevelUpgraded(users.get(4), false);

		List<String> request = mockMailSender.getRequests();
		assertThat(request.size(), is(2));
		assertThat(request.get(0), is(users.get(1).getEmail()));
		assertThat(request.get(1), is(users.get(3).getEmail()));

	}

	private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
		assertThat(updated.getId(), is(expectedId));
		assertThat(updated.getLevel(), is(expectedLevel));
	}

//	private void checkLevel(User user, Level expectedLevel) {
//		User userUpdate = userDao.get(user.getId());
//		assertThat(userUpdate.getLevel(), is(expectedLevel));
//	}

	private void checkLevelUpgraded(User user, boolean upgraded) {
		User userUpdate = userDao.get(user.getId());
		if (upgraded) {
			assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
		} else {
			assertThat(userUpdate.getLevel(), is(user.getLevel()));
		}
	}

	@Test
	public void add() {
		userDao.deleteAll();

		User userWithLevel = users.get(4);
		User userWithoutLevel = users.get(0);
		userWithoutLevel.setLevel(null);

		userService.add(userWithLevel);
		userService.add(userWithoutLevel);

		User userWithLevelRead = userDao.get(userWithLevel.getId());
		User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

		assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
		assertThat(userWithoutLevelRead.getLevel(), is(Level.BASIC));

	}

//	모 아니면 도
	@Test
	@DirtiesContext
	public void upgradeAllOrNothing() throws Exception {
//		TestUserServiceImpl testUserService = new TestUserServiceImpl(users.get(3).getId());
//		testUserService.setUserDao(this.userDao);
//		CommonUserLevelUpgradePolicy policy = new CommonUserLevelUpgradePolicy();
//		policy.setUserDao(userDao);
//		testUserService.setLevelPolicy(policy);
//		testUserService.setDataSource(this.dataSource);
//		testUserService.setTransactionManager(transactionManager);
//		testUserService.setMailSender(mailSender);

//		UserServiceTx txUserService = new UserServiceTx();
//		txUserService.setTransactionManager(transactionManager);
//		txUserService.setUserService(testUserService);

//		TransactionHandler txHandler = new TransactionHandler();
//		txHandler.setTarget(testUserService);
//		txHandler.setTransactionManager(transactionManager);
//		txHandler.setPattern("upgradeLevels");
//		UserService txUserService = (UserService)Proxy.newProxyInstance(getClass().getClassLoader(),
//				new Class[] {UserService.class},
//				txHandler);

//		TxProxyFactoryBean txProxyFactoryBean = context.getBean("&userService", TxProxyFactoryBean.class);
//		txProxyFactoryBean.setTarget(testUserService);
//		UserService txUserService = (UserService) txProxyFactoryBean.getObject();

//		ProxyFactoryBean txProxyFactoryBean =
//				context.getBean("&userService",ProxyFactoryBean.class);
//		txProxyFactoryBean.setTarget(testUserService);
//		UserService txUserService = (UserService) txProxyFactoryBean.getObject();

		userDao.deleteAll();
		for (User user : users)
			userDao.add(user);

		try {
			this.testUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		} catch (TestUserServiceException e) {
		}

		// 업데이트가 안됬을 것이다. 예상
		checkLevelUpgraded(users.get(1), false);

	}

	@Test
	public void mockUpgradeLevels() throws Exception {
		UserServiceImpl userServiceImpl = new UserServiceImpl();

		UserDao mockUserDao = mock(UserDao.class);
		when(mockUserDao.getAll()).thenReturn(this.users);
		userServiceImpl.setUserDao(mockUserDao);

		MailSender mockMailSender = mock(MailSender.class);
		userServiceImpl.setMailSender(mockMailSender);

		userServiceImpl.upgradeLevels();

		// any : 파라미터 내용은 무시하고 호출 횟수 확인 가능
		verify(mockUserDao, times(2)).update(any(User.class));
//		verify(mockUserDao, times(2)).update((User) any(User.class));
		// users.get(1)을 파라미터로 update()가 호출된 적이 있는지를 확인해준다.
		verify(mockUserDao).update(users.get(1));
		assertThat(users.get(1).getLevel(), is(Level.SILVER));
		verify(mockUserDao).update(users.get(3));
		assertThat(users.get(3).getLevel(), is(Level.GOLD));

	}

	@Test
	public void advisorAutoProxyCreator() {
		assertThat(testUserService, is(java.lang.reflect.Proxy.class));
	}

	@Test(expected = TransientDataAccessResourceException.class)
	public void readOnlyTransactionAttribute() {
		testUserService.getAll();
	}

	@Test
	public void transactionSync() {
		DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();

		TransactionStatus txStatus = transactionManager.getTransaction(txDefinition);

		try {
			userService.deleteAll();
			userService.add(users.get(0));
			userService.add(users.get(1));
		} 
		finally {
			transactionManager.rollback(txStatus);
		}
	}
	/*
	 * 
	 * http://mspark7233.blogspot.com/2012/12/spring3-ibatis-transactional-error.html
	 * 테스트에서의 @Transactional 설정이 왜안되지?
	/**
	@Test
	@Transactional
	public void transactionSync2() {
		userService.deleteAll();
		userService.add(users.get(0));
		userService.add(users.get(1));
	}
	*/
}
