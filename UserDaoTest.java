import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class UserDaoTest {

	@Test
	public void addAndGet() throws ClassNotFoundException, SQLException{
		// TODO Auto-generated method stub
//		ApplicationContext context =
//				new AnnotationConfigApplicationContext(DaoFactory.class);
		ApplicationContext context = new GenericXmlApplicationContext(
				"applicationContext.xml");
		
		//중복 메서드 문제가생기면?
		UserDao dao = context.getBean("userDao", UserDao.class);
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		User user = new User("gyumee","박성철","springno1");
		
		dao.add(user);
		assertThat(dao.getCount(), is(1));

//		System.out.println(user.getId() + " 등록 성공");

		User user2 = dao.get(user.getId());
//		System.out.println(user2.getName());
//		System.out.println(user2.getPassword());
//		System.out.println(user2.getId() + " 조회 성공");
//		if(!user.getName().equals(user2.getName())) {
//			System.out.println("테스트 실패 (name)");
//		}else if(!user.getPassword().equals(user2.getPassword())) {
//			System.out.println("테스트 실패 (password)");
//		}else {
//			System.out.println("조회 테스트 성공");
//		}
		
		assertThat(user2.getName(), is(user.getName()));
		assertThat(user2.getPassword(), is(user.getPassword()));
	}
	
//	public static void main(String args[]) {
//		JUnitCore.main("UserDaoTest");
//	}
	
	@Test
	public void count() throws SQLException{
		ApplicationContext context = new GenericXmlApplicationContext(
				"applicationContext.xml");
		
		UserDao dao = context.getBean("userDao", UserDao.class);
		User user1 = new User("gyumee","박성철","springno1");
		User user2 = new User("legw700","이길원","springno2");
		User user3 = new User("bumjin","박범진","springno3");
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.add(user1);
		assertThat(dao.getCount(), is(1));
		
		dao.add(user2);
		assertThat(dao.getCount(), is(2));
		
		dao.add(user3);
		assertThat(dao.getCount(), is(3));
		
	}

}
