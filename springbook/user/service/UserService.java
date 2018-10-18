package springbook.user.service;

import java.sql.Connection;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import springbook.user.dao.Level;
import springbook.user.dao.User;
import springbook.user.dao.UserDao;

public class UserService {
	// 테스트와 애플리케이션 코드의 숫자 중복 제거
	// static 속성은 Policy에 있는게 맞는 거 같은데, 테스트할 때 불러올 방법이 생각 안난다.
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;
	// @Autowired 대신 수정자 메서드를 통한 DI
	UserDao userDao;

	private DataSource dataSource;

	UserLevelUpgradePolicy levelPolicy;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setLevelPolicy(UserLevelUpgradePolicy levelPolicy) {
		this.levelPolicy = levelPolicy;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	// Policy 적용
	public void upgradeLevels() throws Exception{
		/**
		 * try/catch/finally 블록이 서비스내에 존재하고 JDBC 작업 코드의 전형적인 문제점을 그대로 가질 수 밖에 없다.
		 * 또한 서비스내의 인스턴스 변수에 Connection을 저장해뒀다가 다른 메소드에서 사용할 수도 없다.
		 * 왜냐하면 서비스가 스프링 빈으로 선언되서 싱글톤으로 되어있기 떄문이다
		 * 멀티스레드 환경에서 공유하는 인스턴스 변수에 정보를 저장하다가는 서로 덮어쓰는 일이 발생할 수 있다.
		 */
		
		TransactionSynchronizationManager.initSynchronization();
		//DB 커넥션 생성과 동기화를 함께 해주는 유틸리티 메서드
		//트랜잭션 동기화에 사용하도록 저장소에 바인딩까지 해줌.
		Connection c = DataSourceUtils.getConnection(dataSource);
		c.setAutoCommit(false);
		
		try {
		List<User> users = userDao.getAll();
		for (User user : users) {
			if(canUpgradeLevel(user)) {
				upgradeLevel(user);
			}
		}
		c.commit();
		}catch(Exception e) {
			c.rollback();
			throw e;
		}finally {
			DataSourceUtils.releaseConnection(c, dataSource);
			TransactionSynchronizationManager.unbindResource(this.dataSource);
			TransactionSynchronizationManager.clearSynchronization();
		}

	}

	private boolean canUpgradeLevel(User user) {
		return levelPolicy.canUpgradeLevel(user);
	}

	protected void upgradeLevel(User user) {
		levelPolicy.upgradeLevel(user);
		userDao.update(user);
	}

	public void add(User user) {
		if (user.getLevel() == null)
			user.setLevel(Level.BASIC);
		userDao.add(user);
	}

}
