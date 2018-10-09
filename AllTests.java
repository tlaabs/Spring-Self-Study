import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({UserDaoTest.class, UserDaoTest2.class})
public class AllTests {
	/* 동일한 applicationContext 설정파일을 사용하면
	 * 모든 테스트 클래스가 하나의 Context를 공유하여 사용한다.
	 * UserDaoTest에서 사용하는 UserDao도 공유하여 사용하는데
	 * UserDaoTest에서 UserDao의 수정자 메서드를 통해 의존관계를 강제로 변경했다.
	 * 따라서 UserDaoTest2에서 변경된 UserDao를 사용하게되어 테스트에 문제가 발생한다.
	 * 이를 해결하기위해 @DirtiesContext를 추가해주면
	 * 다음 테스트에는 새로운 애플리케이션 컨텍스트를 만들어서 사용하게 해준다. 
	*/
}
