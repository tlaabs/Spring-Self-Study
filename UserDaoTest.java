import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class UserDaoTest {

	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		// TODO Auto-generated method stub
		ApplicationContext context =
				new AnnotationConfigApplicationContext(DaoFactory.class);
		//�ߺ� �޼��� �����������?
		UserDao dao1 = context.getBean("userDao", UserDao.class);
		
		
//		ConnectionMaker connectionMaker = new DConnectionMaker();
//
//		UserDao dao = new DaoFactory().userDao();
//
//		User user = new User();
//		user.setId("whiteship");
//		user.setName("��⼱");
//		user.setPassword("married");
//
//		dao.add(user);
//
//		System.out.println(user.getId() + " ��� ����");
//
//		User user2 = dao.get(user.getId());
//		System.out.println(user2.getName());
//		System.out.println(user2.getPassword());
//
//		System.out.println(user2.getId() + " ��ȸ ����");
	}

}
