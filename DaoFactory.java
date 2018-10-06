import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//Application Context�� ����� ����������� ǥ��
@Configuration
public class DaoFactory {
	
	@Bean //������Ʈ ������ ����ϴ� IoC�� �޼ҵ��� ǥ��
	public UserDao userDao() {
		UserDao userDao = new UserDao(connectionMaker());
		return userDao;
	}
	
	@Bean
	public ConnectionMaker connectionMaker() {
		return new DConnectionMaker();
	}
}
