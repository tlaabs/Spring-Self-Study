package springbook.user.service;

import java.util.List;

import springbook.user.dao.Level;
import springbook.user.dao.User;
import springbook.user.dao.UserDao;

public class UserService {
	// @Autowired 대신 수정자 메서드를 통한 DI
	UserDao userDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void upgradeLevels() {
		List<User> users = userDao.getAll();
		for (User user : users) {
			if(canUpgradeLevel(user)) {
				upgradeLevel(user);
			}
		}
	}

	private boolean canUpgradeLevel(User user) {
		Level currentLevel = user.getLevel();
		switch (currentLevel) {
		case BASIC:
			return (user.getLogin() >= 50);
		case SILVER:
			return (user.getRecommend() >= 30);
		case GOLD:
			return false;
		default:
			throw new IllegalArgumentException("Unknown Level: " + currentLevel);
		}
	}
	
	private void upgradeLevel(User user) {
		user.upgradeLevel();
		userDao.update(user);
	}

	public void add(User user) {
		if (user.getLevel() == null)
			user.setLevel(Level.BASIC);
		userDao.add(user);
	}

}
