import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//Application Context가 사용할 설정정보라는 표시
@Configuration
public class DaoFactory {
	
	@Bean //오브젝트 생성을 담당하는 IoC용 메소드라는 표시
	public UserDao userDao() {
		UserDao userDao = new UserDao(connectionMaker());
		return userDao;
	}
	
	@Bean
	public ConnectionMaker connectionMaker() {
		return new DConnectionMaker();
	}
}
