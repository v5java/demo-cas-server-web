package com.msxf.sso.authentication;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.jasig.services.persondir.IPersonAttributes;
import org.jasig.services.persondir.support.AttributeNamedPersonImpl;
import org.jasig.services.persondir.support.StubPersonAttributeDao;
import org.springframework.stereotype.Component;

import com.msxf.sso.model.User;

/**
 * 自定义的返回给客户端相关信息
 * @create 2015-7-18 下午5:52:56
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
@Component(value="attributeRepository")
public class UserStubPersonAttributeDao extends StubPersonAttributeDao {
	@Resource
	private UserDaoJdbc userDaoJdbc;
	
	@Override
	public IPersonAttributes getPerson(String uid) {
		Map<String, List<Object>> attributes = new HashMap<String, List<Object>>();
		try {
			User user = userDaoJdbc.getByUsername(uid);
			attributes.put("userId", Collections.singletonList((Object)user.getUsercode()));
			attributes.put("username", Collections.singletonList((Object)user.getUsername()));
			attributes.put("usernamePlain", Collections.singletonList((Object)URLEncoder.encode(user.getUsernamePlain(), "UTF-8")));
			attributes.put("blogURL", Collections.singletonList((Object)"http://blog.csdn.net/jadyer"));
			attributes.put("blogger", Collections.singletonList((Object)URLEncoder.encode("玄玉", "UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new AttributeNamedPersonImpl(attributes);
	}
}