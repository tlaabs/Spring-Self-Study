package springbook.user.dao;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

//Application Context�� ����� ����������� ǥ��
@Configuration
public class DaoFactory {
	
//	@Bean //������Ʈ ������ ����ϴ� IoC�� �޼ҵ��� ǥ��
//	public UserDao userDao() {
//		UserDao userDao = new UserDao();
//		userDao.setDataSource(dataSource());
//		return userDao;
//	}
	
//	@Bean
//	public ConnectionMaker connectionMaker() {
//		return new DConnectionMaker();
//	}
	
	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost/springbook?serverTimezone=UTC&useSSL=false");
		dataSource.setUsername("spring");
		dataSource.setPassword("book");
		
		return dataSource;
		
	}
}
