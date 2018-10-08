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
		
		//�ߺ� �޼��� �����������?
		UserDao dao = context.getBean("userDao", UserDao.class);
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		User user = new User("gyumee","�ڼ�ö","springno1");
		
		dao.add(user);
		assertThat(dao.getCount(), is(1));

//		System.out.println(user.getId() + " ��� ����");

		User user2 = dao.get(user.getId());
//		System.out.println(user2.getName());
//		System.out.println(user2.getPassword());
//		System.out.println(user2.getId() + " ��ȸ ����");
//		if(!user.getName().equals(user2.getName())) {
//			System.out.println("�׽�Ʈ ���� (name)");
//		}else if(!user.getPassword().equals(user2.getPassword())) {
//			System.out.println("�׽�Ʈ ���� (password)");
//		}else {
//			System.out.println("��ȸ �׽�Ʈ ����");
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
		User user1 = new User("gyumee","�ڼ�ö","springno1");
		User user2 = new User("legw700","�̱��","springno2");
		User user3 = new User("bumjin","�ڹ���","springno3");
		
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
