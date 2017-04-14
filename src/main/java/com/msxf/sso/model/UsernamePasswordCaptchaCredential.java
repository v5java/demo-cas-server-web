package com.msxf.sso.model;

import org.jasig.cas.authentication.RememberMeUsernamePasswordCredential;

/**
 * 自定义的接收登录验证码的实体类
 * @create 2015-7-14 下午4:28:33
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
//public class UsernamePasswordCaptchaCredential extends UsernamePasswordCredential {
public class UsernamePasswordCaptchaCredential extends RememberMeUsernamePasswordCredential {
	private static final long serialVersionUID = 8317889802836113837L;
	
	private String captcha;

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
}