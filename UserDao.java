
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;

/*
 * �ۼ��� : �ɼ���
 * ������Ʈ : 2018-10-04
 * ���� : �ʳ��� DAO
 */

public class UserDao {

	// DB Ŀ�ؼǵ��� ����� ������ �� �ֵ��� �����ִ� Ŭ����.
	private DataSource dataSource;

//	private ConnectionMaker connectionMaker; // �ʱ⿡ �����ϸ� �ٲ� ���� ������,
	// �ֳ��ϸ� @Bean�� �ٿ�������� ������ �� ��ü�� �̱�����.

//	public UserDao(ConnectionMaker connectionMaker) {
//		this.connectionMaker = connectionMaker;
// �������� �˻�
//		AnnotationConfigApplicationContext context =
//				new AnnotationConfigApplicationContext(DaoFactory.class);
//		this.connectionMaker = context.getBean("connectionMaker", ConnectionMaker.class);
//	}

//	public void setConnectionMaker(ConnectionMaker connectionMaker) {
//		this.connectionMaker = connectionMaker;
//	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void add(User user) throws SQLException {
		Connection c = dataSource.getConnection();

		PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
		ps.setString(1, user.getId());
		ps.setString(2, user.getName());
		ps.setString(3, user.getPassword());

		ps.executeUpdate();

		ps.close();
		c.close();
	}

	public User get(String id) throws SQLException {
		Connection c = dataSource.getConnection();

		PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
		ps.setString(1, id);

		ResultSet rs = ps.executeQuery();
		User user = null;
		
		if (rs.next()) {
			user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
		}
		
		rs.close();
		ps.close();
		c.close();
		
		if(user == null) throw new EmptyResultDataAccessException(1);

		return user;

	}

	public void deleteAll() throws SQLException {
		Connection c = dataSource.getConnection();

		PreparedStatement ps = c.prepareStatement("delete from users");
		ps.executeUpdate();

		ps.close();
		c.close();
	}

	public int getCount() throws SQLException {
		Connection c = dataSource.getConnection();

		PreparedStatement ps = c.prepareStatement("select count(*) from users");

		ResultSet rs = ps.executeQuery();
		rs.next();
		int count = rs.getInt(1);

		rs.close();
		ps.close();
		c.close();

		return count;
	}
}
