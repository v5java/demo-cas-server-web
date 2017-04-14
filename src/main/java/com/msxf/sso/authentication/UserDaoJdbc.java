package com.msxf.sso.authentication;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.msxf.sso.model.User;

@Repository
public class UserDaoJdbc {
	private static final String SQL_USER_VERIFY = "SELECT COUNT(*) FROM permission_operator WHERE operator_login=? AND operator_pwd=SHA1(?)";
	private static final String SQL_USER_GET = "SELECT * FROM permission_operator WHERE operator_login=?";

	private JdbcTemplate jdbcTemplate;

	@Resource
	public void setDataSource(DataSource dataSource){
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * 验证用户名和密码是否正确
	 * @create 2015-7-17 下午3:56:54
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	public boolean verifyAccount(String username, String password){
		try{
			return 1==this.jdbcTemplate.queryForObject(SQL_USER_VERIFY, new Object[]{username, password}, Integer.class);
		}catch(EmptyResultDataAccessException e){
			return false;
		}
	}
	
	/**
	 * 根据用户名获取用户信息
	 * @create 2015-7-20 上午10:40:54
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	public User getByUsername(String username){
		try{
			return (User)this.jdbcTemplate.queryForObject(SQL_USER_GET, new Object[]{username}, new UserRowMapper());
		}catch(EmptyResultDataAccessException e){
			return new User();
		}
	}
}


class UserRowMapper implements RowMapper<User> {
	@Override
	public User mapRow(ResultSet rs, int index) throws SQLException {
		User user = new User();
		user.setUsercode(rs.getString("operator_code"));
		user.setUsername(rs.getString("operator_login"));
		user.setUsernamePlain(rs.getString("operator_name"));
		return user;
	}
}