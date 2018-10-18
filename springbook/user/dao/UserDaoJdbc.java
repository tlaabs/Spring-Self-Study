package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/*
 * �ۼ��� : �ɼ���
 * ������Ʈ : 2018-10-04
 * ���� : �ʳ��� DAO
 */

public class UserDaoJdbc implements UserDao{

	private JdbcTemplate jdbcTemplate;

	//ResultSet의 row를 조회하여 유저정보를 User 클래스에 담아서 리턴함.
	private RowMapper<User> userMapper = new RowMapper<User>() {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			user.setLevel(Level.valueOf(rs.getInt("level")));
			user.setLogin(rs.getInt("login"));
			user.setRecommend(rs.getInt("recommend"));
			return user;
		}
	};
	
	@Override
	public void update(User user) {
		// TODO Auto-generated method stub
		//JdbcTemplate을 더 이상 활용할 수 없다. jdbcTemplate 내부에서 커넥션이 모두 끝나기때문에 서비스 영역에서 트랜잭션을 수행할 수 없다.
		this.jdbcTemplate.update("update users set name = ?, password = ?, level = ?, login = ?, " +
		"recommend = ? where id = ? ", user.getName(), user.getPassword(),
		user.getLevel().intValue(), user.getLogin(), user.getRecommend(),
		user.getId());
	}

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void add(User user){
		this.jdbcTemplate.update("insert into users(id, name, password, level, login, recommend) "
				+ "values(?,?,?,?,?,?)", user.getId(), user.getName(),
				user.getPassword(), user.getLevel().intValue(),
				user.getLogin(), user.getRecommend());
		//getLevel은 Enum 타입, DB에 저장될 수 있는 SQL 타입이 아니므로 정수형으로 변환해줘야 한다.
	}

	public User get(String id) {
		// queryForobject()는 로우 개수가 하나가 아니라면 EmptyResultDataAccessException 예외를 알아서 던짐
		return this.jdbcTemplate.queryForObject("select * from users where id = ?", new Object[] { id },
				this.userMapper);

//		Connection c = dataSource.getConnection();
//
//		PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
//		ps.setString(1, id);
//
//		ResultSet rs = ps.executeQuery();
//		User user = null;
//
//		if (rs.next()) {
//			user = new User();
//			user.setId(rs.getString("id"));
//			user.setName(rs.getString("name"));
//			user.setPassword(rs.getString("password"));
//		}
//
//		rs.close();
//		ps.close();
//		c.close();
//
//		if (user == null)
//			throw new EmptyResultDataAccessException(1);
//
//		return user;

	}

	public List<User> getAll() {
		// query()템플릿은 모든 로우를 열람하면서 로우마다 RowMapper 콜백을 호출함.
		/*
		 * query()는 결과가 없을 경우 queryForObject()처럼 예외를 던지지 않는다. 대신 크기가 0인 List<T> 오브젝트를
		 * 돌려줌
		 */
		return this.jdbcTemplate.query("select * from users order by id", this.userMapper);
	}

	public void deleteAll() {
//		this.jdbcContext.executeSql("delete from users");
		this.jdbcTemplate.update("delete from users");
	}

	public int getCount(){

		// 내장된 콜백 사용하기
		return this.jdbcTemplate.queryForInt("select count(*) from users");
//		return this.jdbcTemplate.query(new PreparedStatementCreator() {
//			
//			@Override
//			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
//				// TODO Auto-generated method stub
//				return conn.prepareStatement("select count(*) from users");
//			}
//		}, new ResultSetExtractor<Integer>() {
//			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException{
//				rs.next();
//				return rs.getInt(1);
//			}
//		});
	}
}
