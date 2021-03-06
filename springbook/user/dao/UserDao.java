package springbook.user.dao;
import java.util.List;

public interface UserDao {
	/**
	 * Connection 파라미터가 추가되면 더 이상 데이터 액세스 기술에 독립적일 수가 없다.(이제까지 해왔던 작업이 수포가 됨)
	 * JPA나 하이버네이트로 구현하게되면 Connection 대신 다른 오브젝트를 사용함.
	 * 테스트 코드까지 변질되었다.
	 * 
	 */
	
	void add(User user);
	User get(String id);
	List<User> getAll();
	void deleteAll();
	int getCount();
	void update(User user1);
}
