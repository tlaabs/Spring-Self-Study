package springbook.user.service;

import springbook.user.dao.User;

public interface UserService {
	void add(User user);
	void upgradeLevels();
}
