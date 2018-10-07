import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

//Application Context가 사용할 설정정보라는 표시
@Configuration
public class DaoFactory {
	
	@Bean //오브젝트 생성을 담당하는 IoC용 메소드라는 표시
	public UserDao userDao() {
		UserDao userDao = new UserDao();
		userDao.setDataSource(dataSource());
		return userDao;
	}
	
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
