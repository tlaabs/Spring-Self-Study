import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//JUnit이 사용하는 애플리케이션 컨텍스트를 만들고 관리하는 작업을 진행해줌.
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
//테스트에서 context의 구성이나 상태를 변경한다는 것을 테스트 컨텍스트 프레임워크에 알려줌.
@DirtiesContext
public class UserDaoTest {
//  타입 -> 변수 이름 -> 예외 발생
	@Autowired
	private ApplicationContext context;
	@Autowired
	private UserDao dao;
	private User user1;
	private User user2;
	private User user3;

	@Before
	public void setUp() {
//		ApplicationContext context = new GenericXmlApplicationContext(
//				"applicationContext.xml");
		System.out.println(context);
		System.out.println(dao);
		// 중복 메서드 문제가생기면?
		
		dao = context.getBean("userDao", UserDao.class);

		DataSource dataSource = new SingleConnectionDataSource(
				"jdbc:mysql://localhost/testdb?serverTimezone=UTC&useSSL=false", "spring", "book", true);
		dao.setDataSource(dataSource);
		
		user1 = new User("gyumee", "박성철", "springno1");
		user2 = new User("leggw700", "이길원", "springno2");
		user3 = new User("bumjin", "박범진", "springno3");
	}

	@Test
	public void addAndGet() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));

		dao.add(user1);
		dao.add(user2);
		assertThat(dao.getCount(), is(2));

		User userget1 = dao.get(user1.getId());
		assertThat(userget1.getName(), is(user1.getName()));
		assertThat(userget1.getPassword(), is(user1.getPassword()));

		User userget2 = dao.get(user2.getId());
		assertThat(userget2.getName(), is(user2.getName()));
		assertThat(userget2.getPassword(), is(user2.getPassword()));
	}

//	public static void main(String args[]) {
//		JUnitCore.main("UserDaoTest");
//	}

	@Test
	public void count() throws SQLException {		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));

		dao.add(user1);
		assertThat(dao.getCount(), is(1));

		dao.add(user2);
		assertThat(dao.getCount(), is(2));

		dao.add(user3);
		assertThat(dao.getCount(), is(3));

	}

	@Test(expected = EmptyResultDataAccessException.class)
	public void getUserFailure() throws SQLException {
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));

		dao.get("unknown_id");
	}

}