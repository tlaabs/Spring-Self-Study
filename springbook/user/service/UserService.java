package springbook.user.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import springbook.user.dao.User;

@Transactional
public interface UserService {
	void add(User user);
	//DAO메소드와 1:1대응되는 CRUD 메소드이지만 단순 위임 이상의 로직을 가질 수 있다.(다른 트랜잭션에 참여할 가능성이 높다)
	void deleteAll();
	void update(User user);
	void upgradeLevels();
	
	@Transactional(readOnly=true)
	User get(String id);
	
	@Transactional(readOnly=true)
	List<User> getAll();
}
