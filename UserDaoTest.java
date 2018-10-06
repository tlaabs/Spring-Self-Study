import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class UserDaoTest {

	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		// TODO Auto-generated method stub
		ApplicationContext context =
				new AnnotationConfigApplicationContext(DaoFactory.class);
		//중복 메서드 문제가생기면?
		UserDao dao1 = context.getBean("userDao", UserDao.class);
		
		
//		ConnectionMaker connectionMaker = new DConnectionMaker();
//
//		UserDao dao = new DaoFactory().userDao();
//
//		User user = new User();
//		user.setId("whiteship");
//		user.setName("백기선");
//		user.setPassword("married");
//
//		dao.add(user);
//
//		System.out.println(user.getId() + " 등록 성공");
//
//		User user2 = dao.get(user.getId());
//		System.out.println(user2.getName());
//		System.out.println(user2.getPassword());
//
//		System.out.println(user2.getId() + " 조회 성공");
	}

}
