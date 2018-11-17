package springbook.user.service;

import java.util.List;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import springbook.user.dao.Level;
import springbook.user.dao.User;
import springbook.user.dao.UserDao;

public class UserServiceImpl implements UserService{
	// 테스트와 애플리케이션 코드의 숫자 중복 제거
	// static 속성은 Policy에 있는게 맞는 거 같은데, 테스트할 때 불러올 방법이 생각 안난다.
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;
	// @Autowired 대신 수정자 메서드를 통한 DI
	UserDao userDao;
//	private DataSource dataSource;
//	UserLevelUpgradePolicy levelPolicy;
	
	private MailSender mailSender;
	
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
		
//	public void setLevelPolicy(UserLevelUpgradePolicy levelPolicy) {
//		this.levelPolicy = levelPolicy;
//	}

//	public void setDataSource(DataSource dataSource) {
//		this.dataSource = dataSource;
//	}
	
	@Override
	public User get(String id) {
		// TODO Auto-generated method stub
		return userDao.get(id);
	}

	@Override
	public List<User> getAll() {
		// TODO Auto-generated method stub
		return userDao.getAll();
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		userDao.deleteAll();
	}

	@Override
	public void update(User user) {
		// TODO Auto-generated method stub
		userDao.update(user);
	}

	// Policy 적용
	public void upgradeLevels(){
		/**
		 * try/catch/finally 블록이 서비스내에 존재하고 JDBC 작업 코드의 전형적인 문제점을 그대로 가질 수 밖에 없다. 또한
		 * 서비스내의 인스턴스 변수에 Connection을 저장해뒀다가 다른 메소드에서 사용할 수도 없다. 왜냐하면 서비스가 스프링 빈으로 선언되서
		 * 싱글톤으로 되어있기 떄문이다 멀티스레드 환경에서 공유하는 인스턴스 변수에 정보를 저장하다가는 서로 덮어쓰는 일이 발생할 수 있다.
		 */

		/**
		 * JTA를 사용하면 하나 이상의 DB가 참여하는 트랙잭션을 만들 수 있다. 별로의 트랜잭션 관리자를 통해 글로벌 트랙잭션을 구성 JDBC의
		 * Connection을 이용한 로컬 트랙잭션은 하나의 DB만 종속된다.
		 */
		List<User> users = userDao.getAll();
		for (User user : users) {
			if (canUpgradeLevel(user)) {
				upgradeLevel(user);
			}
		}
	}	

	private boolean canUpgradeLevel(User user) {
		Level currentLevel = user.getLevel();
		switch (currentLevel) {
		case BASIC:
			return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
		case SILVER:
			return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
		case GOLD:
			return false;
		default:
			throw new IllegalArgumentException("Unknown Level: " + currentLevel);
		}
	}

	protected void upgradeLevel(User user) {
//		levelPolicy.upgradeLevel(user);
		user.upgradeLevel();
		userDao.update(user);
		sendUpgradeEMail(user);
	}

	public void add(User user) {
		if (user.getLevel() == null)
			user.setLevel(Level.BASIC);
		userDao.add(user);
	}

	private void sendUpgradeEMail(User user) {
//		Properties props = new Properties();
//		props.put("mail.smtp.host", "mail.ksug.org");
//		Session s = Session.getInstance(props);
//		
//		MimeMessage message = new MimeMessage(s);
//		try {
//			message.setFrom(new InternetAddress("useradmin@ksug.org"));
//			message.addRecipient(Message.RecipientType.TO, 
//					new InternetAddress(user.getEmail()));
//			message.setSubject("Ugrade 안내");
//			message.setText("사용자님의 등급이 " + user.getLevel().name() + "로 업그레이드되었습니다.");
//			
//			Transport.send(message);
//		}catch(AddressException e) {
//			throw new RuntimeException(e);
//		}catch(MessagingException e) {
//			throw new RuntimeException(e);	
//		}
//		}catch(UnsupportedEncodingException e) {
//			throw new RuntimeException(e);
//		}
		
		//JavaMail의 서비스 추상화 인터페이스 사용
		//JavaMailSender는 MailSender 인터페이스를 구현한 클래스
		//DI를 통해 아래 두줄이 없어짐.
//		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//		mailSender.setHost("mail.server.com");
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setFrom("useradmin@ksug.org");
		mailMessage.setSubject("Upgrade 안내");
		mailMessage.setText("사용자님의 등급이 " + user.getLevel().name());
		
		this.mailSender.send(mailMessage);
	}

}
