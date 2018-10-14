package springbook.user.dao;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({UserDaoTest.class, UserDaoTest2.class})
public class AllTests {
	/* ������ applicationContext ���������� ����ϸ�
	 * ��� �׽�Ʈ Ŭ������ �ϳ��� Context�� �����Ͽ� ����Ѵ�.
	 * UserDaoTest���� ����ϴ� UserDao�� �����Ͽ� ����ϴµ�
	 * UserDaoTest���� UserDao�� ������ �޼��带 ���� �������踦 ������ �����ߴ�.
	 * ���� UserDaoTest2���� ����� UserDao�� ����ϰԵǾ� �׽�Ʈ�� ������ �߻��Ѵ�.
	 * �̸� �ذ��ϱ����� @DirtiesContext�� �߰����ָ�
	 * ���� �׽�Ʈ���� ���ο� ���ø����̼� ���ؽ�Ʈ�� ���� ����ϰ� ���ش�. 
	*/
}
