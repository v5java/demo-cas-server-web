package com.msxf.sso.authentication;

import java.security.GeneralSecurityException;

import javax.annotation.Resource;
import javax.security.auth.login.FailedLoginException;

import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.jasig.cas.authentication.principal.SimplePrincipal;
import org.springframework.stereotype.Component;

/**
 * 自定义的用户登录认证类
 * @create 2015-7-17 下午3:48:44
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
@Component(value="primaryAuthenticationHandler")
public class UserAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler {
	@Resource
	private UserDaoJdbc userDaoJdbc;
	
	/**
	 * 认证用户名和密码是否正确
	 * @see UsernamePasswordCredential参数包含了前台页面输入的用户信息
	 */
	@Override
	protected HandlerResult authenticateUsernamePasswordInternal(UsernamePasswordCredential transformedCredential) throws GeneralSecurityException, PreventedException {
		String username = transformedCredential.getUsername();
		String password = transformedCredential.getPassword();
		if(userDaoJdbc.verifyAccount(username, password)){
			return createHandlerResult(transformedCredential, new SimplePrincipal(username), null);
		}
		throw new FailedLoginException();
	}
}