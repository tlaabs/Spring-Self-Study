import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//JUnit�� ����ϴ� ���ø����̼� ���ؽ�Ʈ�� ����� �����ϴ� �۾��� ��������.
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
//�׽�Ʈ���� context�� �����̳� ���¸� �����Ѵٴ� ���� �׽�Ʈ ���ؽ�Ʈ �����ӿ�ũ�� �˷���.
@DirtiesContext
public class UserDaoTest2 {
//  Ÿ�� -> ���� �̸� -> ���� �߻�
	@Autowired
	private ApplicationContext context;
	@Autowired
	private UserDao dao;
	private User user1;
	private User user2;
	private User user3;

	@Before
	public void setUp() {
		System.out.println(context);
		System.out.println(dao);
//		ApplicationContext context = new GenericXmlApplicationContext(
//				"applicationContext.xml");
		// �ߺ� �޼��� �����������?
//		dao = context.getBean("userDao", UserDao.class);

//		DataSource dataSource = new SingleConnectionDataSource(
//				"jdbc:mysql://localhost/testdb?serverTimezone=UTC&useSSL=false", "spring", "book", true);
//		dao.setDataSource(dataSource);
		
		user1 = new User("gyumee", "ㅁㄻㄹ", "springno1");
		user2 = new User("leggw700", "ㅁㄴㅇㄹ", "springno2");
		user3 = new User("bumjin", "ㅁㄴㅇㄹ", "springno3");
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
		System.out.println(context);
		System.out.println(dao);		
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