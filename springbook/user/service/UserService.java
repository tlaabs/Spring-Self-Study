package springbook.user.service;

import java.util.List;

import springbook.user.dao.Level;
import springbook.user.dao.User;
import springbook.user.dao.UserDao;

public class UserService{
	//테스트와 애플리케이션 코드의 숫자 중복 제거
	//static 속성은 Policy에 있는게 맞는 거 같은데, 테스트할 때 불러올 방법이 생각 안난다.
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;
	// @Autowired 대신 수정자 메서드를 통한 DI
	UserDao userDao;
	
	UserLevelUpgradePolicy levelPolicy;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public void setLevelPolicy(UserLevelUpgradePolicy levelPolicy) {
		this.levelPolicy = levelPolicy;
	}

	//Policy 적용
	public void upgradeLevels() {
		List<User> users = userDao.getAll();
		for (User user : users) {
			if(levelPolicy.canUpgradeLevel(user)) {
				levelPolicy.upgradeLevel(user);
			}
		}
	}

	public void add(User user) {
		if (user.getLevel() == null)
			user.setLevel(Level.BASIC);
		userDao.add(user);
	}

}
