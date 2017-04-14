package com.jadyer.demo.test;

/**
 * @see ------------------------------------------------------------------------------------------------------------------------
 * @see CAS(Central Authentication Service)
 * @see 官网：https://www.apereo.org/projects/cas
 * @see 源码：https://github.com/Jasig/cas
 * @see       https://github.com/Jasig/java-cas-client
 * @see 帮助：https://wiki.jasig.org/display/CASC/CAS+Client+for+Java+3.1
 * @see       目前CAS的Java客户端官方版本是3.4.0,出于安全考虑应立即升级到该版本(2015-07-21发布的3.4.0版本)
 * @see       注意,自从3.1.11开始,maven2的groupId已经变为org.jasig.cas.client
 * @see       <dependency>
 * @see       		<groupId>org.jasig.cas.client</groupId>
 * @see       		<artifactId>cas-client-core</artifactId>
 * @see       		<version>3.4.0</version>
 * @see       </dependency>
 * @see ------------------------------------------------------------------------------------------------------------------------
 * @see 部署CAS Server
 * @see 0.这里用到的环境如下
 * @see   CentOS-6.4-x86_64-minimal
 * @see   apache-tomcat-8.0.21
 * @see   jdk-7u80-linux-x64
 * @see   cas-server-webapp-4.0.3
 * @see   cas-client-core-3.4.0
 * @see 1.可以到下面的两个网址下载cas-server-webapp-4.0.3.war,然后发布到Tomcat中
 * @see   http://central.maven.org/maven2/org/jasig/cas/cas-server-webapp/4.0.3
 * @see   http://mvnrepository.com/artifact/org.jasig.cas/cas-server-webapp/4.0.3
 * @see 2.也可以下载CAS源码,自己来构建war包（得到的war内容与cas-server-webapp-4.0.3.war是相同的）
 * @see   https://github.com/Jasig/cas/releases中下载最新版4.0.3得到cas-4.0.3.zip,并解压到桌面
 * @see   C:\Users\Jadyer\Desktop\cas-4.0.3>mvn install -DskipTests
 * @see   在构建最后,会报错下面的错误
 * @see   Could not transfer artifact net.jradius:jradius-core:pom:1.0.0 from/to coova (http://coova-dev.s3.amazonaws.com/mvn): Software caused connection abort: recv failed
 * @see   不过没关系,此时我们想要的war已经生成了,它就是\cas-4.0.3\cas-server-webapp\target\cas.war
 * @see   将cas.war部署到Tomcat,启动后访问http://127.0.0.1:8080/cas/
 * @see 3.登录cas
 * @see   CAS-4.0之前的默认验证规则是只要用户名和密码相同就认证通过
 * @see   4.0之后有所改变,其默认用户名密码为casuser/Mellon,它配置在\WEB-INF\deployerConfigContext.xml
 * @see ------------------------------------------------------------------------------------------------------------------------
 * @see 导入MyEclipse
 * @see 1.MyEclipse中新建一个Web Project,名字随意,比方说cas-server-web(新建时JavaEE版本选5.0就行)
 * @see 2.将Maven构建的cas.war解压,将其内容拷贝到上一步新建的Web Project中即可
 * @see   \WEB-INF\classes\目录中的一大推properties文件和log4j.xml拷贝到src下就可以了
 * @see 3.把这个Web Project发布到tomcat,应该看到的是一样的效果
 * @see 另外,我们会发现cas.war的\WEB-INF\classes\中并没有class文件,实际上它是在\WEB-INF\lib\cas-server-*.jar四个jar文件中的
 * @see 如果也想把这四个cas-server-*.jar的源码导入,可以像下面这么做(不过一般没这必要)
 * @see 1.cas-server-security-filter-2.0.3.jar
 * @see   它的源码地址是https://github.com/Jasig/cas-server-security-filter,实际上只有一个java文件
 * @see   也可以在这里查到http://mvnrepository.com/artifact/org.jasig.cas/cas-server-security-filter/2.0.3
 * @see 2.cas-server-core-4.0.3.jar、cas-server-support-generic-4.0.3.jar、cas-server-webapp-support-4.0.3.jar
 * @see   这三个jar的源码就在上一步下载到的cas-4.0.3.zip中,直接拷过来就行了
 * @see ------------------------------------------------------------------------------------------------------------------------
 * @see CAS支持HTTP协议的SSO
 * @see Tomcat默认是没有开启HTTPS协议的,这时访问http://127.0.0.1:8080/cas/会在页面看到下面的提示
 * @see Non-secure Connection
 * @see You are currently accessing CAS over a non-secure connection. Single Sign On WILL NOT WORK. In order to have single sign on work, you MUST log in over HTTPS.
 * @see 这段提示是硬编码在\\WEB-INF\\view\\jsp\\default\\ui\\casLoginView.jsp,注释掉的话就看不到了
 * @see 为了使客户端在HTTP协议下单点成功,可以修改以下两处配置使其不开启HTTPS验证
 * @see 1.\WEB-INF\deployerConfigContext.xml
 * @see   <bean class="org.jasig...support.HttpBasedServiceCredentialsAuthenticationHandler">添加p:requireSecure="false"
 * @see 2.\WEB-INF\spring-configuration\ticketGrantingTicketCookieGenerator.xml和\WEB-INF\spring-configuration\warnCookieGenerator.xml
 * @see   p:cookieSecure="true"改为p:cookieSecure="false"
 * @see ------------------------------------------------------------------------------------------------------------------------
 * @see CAS个性登录页
 * @see 0.cas的页面显示控制是集中在\WEB-INF\cas.properties中的cas.viewResolver.basename属性的,它的值默认是default_views
 * @see   所以cas会去classpath中寻找default_views.properties,在default_views.properties中指定了登录登出页面等
 * @see 1.修改cas.properties文件,cas.viewResolver.basename=msxf,再拷贝default_views.properties为msxf.properties
 * @see 2.修改msxf.properties文件,/WEB-INF/view/jsp/default改为/WEB-INF/view/jsp/msxf
 * @see   其中casLoginView.url表示默认登录页,casLoginGenericSuccessView.url表示默认登录成功页
 * @see 3.复制/WEB-INF/view/jsp/default/及其子目录所有文件到/WEB-INF/view/jsp/msxf/中
 * @see   接下来就可以随意修改登录页面了
 * @see   注意\\WEB-INF\\view\\jsp\\msxf\\ui\\includes\\bottom.jsp页面中引用了googleapis的一些jquery库
 * @see   可以换成百度CDN(http://cdn.code.baidu.com/)的,比如http://apps.bdimg.com/libs/jqueryui/1.10.2/jquery-ui.min.js
 * @see ------------------------------------------------------------------------------------------------------------------------
 * @see CAS登录页添加验证码
 * @see 0.这年头验证码一般用来防止帐号被暴力破解,如果我们的系统是走专线的,也就是说放在内网,那完全没必要搞验证码
 * @see 1.由于CAS使用了Spring Web Flow框架,所以我们想在表单加属性就直接找\WEB-INF\login-webflow.xml
 * @see 2.在84行<view-state id="viewLoginForm">中找到表单的两个属性,我们加一个<binding property="captcha"/>
 * @see   同样该标签中会发现model="credential"配置,所以我们就在该文件找credential对应的实体类配置
 * @see   发现是在27行设置的,其值为org.jasig.cas.authentication.UsernamePasswordCredential
 * @see   这是一个用来接收前台表单参数的JavaBean,我们这里要在表单上加一个参数captcha,所以继承它就行了
 * @see 3.创建com.msxf.sso.model.UsernamePasswordCaptchaCredential extends UsernamePasswordCredential
 * @see   再加上captcha属性,以及captcha对应的setter和getter
 * @see   再修改login-webflow.xml第27行credential对应实体类为com.msxf.sso.model.UsernamePasswordCaptchaCredential
 * @see 4.接下来添加校验验证码的流程
 * @see   继续看<view-state id="viewLoginForm">,这里我们会发现表单实际的提交等动作是由authenticationViaFormAction处理的
 * @see   authenticationViaFormAction是被配置在cas-servlet.xml中的第233行
 * @see   我们要在原有表单处理逻辑的基础上增加验证码,所以就扩展authenticationViaFormAction
 * @see   创建com.msxf.sso.authentication.AuthenticationViaCaptchaFormAction extends AuthenticationViaFormAction
 * @see   在AuthenticationViaCaptchaFormAction中增加一个validateCaptcha()方法用来校验验证码
 * @see 5.将messages.properties的一些提示文字改为中文
 * @see   required.username=必须输入帐号
 * @see   required.password=必须输入密码
 * @see   required.captcha=必须输入验证码
 * @see   error.authentication.captcha.bad=验证码不正确
 * @see   authenticationFailure.AccountNotFoundException=登录失败--帐号不正确
 * @see   authenticationFailure.FailedLoginException=登录失败--密码不正确
 * @see   authenticationFailure.UNKNOWN=未知错误
 * @see ------------------------------------------------------------------------------------------------------------------------
 * @see CAS服务端通过数据库认证用户
 * @see 实现方式有两种,一是自己写数据库获取用户名密码再认证的类,一是借助CAS-4.0.3自带的JDBC支持来实现认证,下面分别介绍
 * @see 【自己写认证类（推荐）】
 * @see 1.之前我们知道CSA-4.0.3的默认登录用户密码是配置在deployerConfigContext.xml,所以到deployerConfigContext.xml里面找
 * @see   找到<bean id="primaryAuthenticationHandler" class="org.jasig.cas.authentication.AcceptUsersAuthenticationHandler">
 * @see   我们在AcceptUsersAuthenticationHandler.java中发现CAS是把配置的用户密码读取到全局Map<String, String>中的
 * @see 2.而AcceptUsersAuthenticationHandler.java是通过继承AbstractUsernamePasswordAuthenticationHandler.java才实现的认证
 * @see   所以创建com.msxf.sso.authentication.UserAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler
 * @see   然后重写authenticateUsernamePasswordInternal()方法,在里面获取到前台页面输入的用户密码,再到数据库中校验就行了
 * @see 3.接下来创建\WEB-INF\spring-configuration\applicationContext-datasource.xml,它会在启动时被自动加载(web.xml中设定的)
 * @see   在applicationContext-datasource.xml中配置数据库连接池,连接池的用户名密码等可以配置在\WEB-INF\cas.properties
 * @see   同时增加<context:component-scan base-package="com.msxf.sso"/>使得可以在自定义类中应用Spring注解
 * @see 4.新建一个UserDaoJdbc.java类,通过它利用Spring JDBC Template访问数据库
 * @see   因为要连接数据库,所以还要把druid-1.0.14.jar和mysql-connector-java-5.1.35.jar加入到lib目录中
 * @see 5.最后记得deployerConfigContext.xml把这段Bean配置给注释掉<bean id="primaryAuthenticationHandler">
 * @see   并在我们自定义的UserAuthenticationHandler.java中使用@Component(value="primaryAuthenticationHandler")声明其为Bean
 * @see   注意其名字应该是primaryAuthenticationHandler,因为deployerConfigContext.xml的其它配置引用了primaryAuthenticationHandler
 * @see   否则你还要找到引用了primaryAuthenticationHandler的位置修改为新的Bean
 * @see 【cas-server-support-jdbc-4.0.3.jar】
 * @see 1.这一种方式就简单一些了,先引入c3p0-0.9.1.2.jar和cas-server-support-jdbc-4.0.3.jar
 * @see 2.修改deployerConfigContext.xml,注释掉<bean id="primaryAuthenticationHandler">
 * @see   并增加<bean id="dataSource"><bean id="passwordEncoder"><bean id="mssoUsersAuthenticationHandler">(下方会贴出具体代码)
 * @see   同样这里也是从cas.properties读取的数据库连接用户密码
 * @see 3.由于在认证过程中是通过<bean id="authenticationManager">引用了<bean id="primaryAuthenticationHandler">来实现的
 * @see   所以修改这里的primaryAuthenticationHandler为我们新建的mssoUsersAuthenticationHandler
 * @see 4.通过查看org.jasig.cas.adaptors.jdbc.QueryDatabaseAuthenticationHandler源码会发现
 * @see   这种方式与上面自己写认证类的方式,原理是一样的,都是直接或间接的扩展AbstractUsernamePasswordAuthenticationHandler
 * @see ------------------------------------------------------------------------------------------------------------------------
 * @see CAS服务端自定义返回的用户信息
 * @see 返回的用户信息是在deployerConfigContext.xml中的<bean id="attributeRepository">配置的
 * @see 既然想自定义返回的用户信息,那就继承org.jasig.services.persondir.support.StubPersonAttributeDao就好了
 * @see 1.创建com.msxf.sso.authentication extends StubPersonAttributeDao并复写getPerson()方法
 * @see   使用@Component(value="attributeRepository")定义它的BeanID
 * @see 2.注释deployerConfigContext.xml中的<bean id="attributeRepository">和<util:map id="attrRepoBackingMap">配置
 * @see 3.修改\WEB-INF\view\jsp\protocol\2.0\casServiceValidationSuccess.jsp(不要改3.0下面的)
 * @see 具体改动,详见下方贴出的代码
 * @see 另外,返回给客户端的相关信息是由org.jasig.services.persondir.IPersonAttributeDao接口定义的
 * @see StubPersonAttributeDao就是IPersonAttributeDao的实现之一,其它实现如SingleRowJdbcPersonAttributeDao/LdapPersonAttributeDao
 * @see 所以也可在deployerConfigContext.xml中配置<bean id="attributeRepository">的实现为SingleRowJdbcPersonAttributeDao
 * @see <bean id="attributeRepository" class="org.jasig.services.persondir.support.jdbc.SingleRowJdbcPersonAttributeDao">
 * @see 个人觉得这样不是很灵活,所以就不贴示例代码了,有情趣的可以看http://pkaq.github.io/2015/01/14/CAS/
 * @see ------------------------------------------------------------------------------------------------------------------------
 * @see CAS服务端配置HTTPS
 * @see 1.生成KeyStore
 * @see   1.1.D:\>keytool -genkey -alias XuanyuKeyStore -keyalg RSA -validity 1024 -keystore D:/keys/XuanyuKey
 * @see       -genkey   表示生成密钥
 * @see       -alias    指定别名,这里是XuanyuKeyStore
 * @see       -keyalg   指定算法,这里是RSA
 * @see       -validity 指定证书有效期,这里是1024天
 * @see       -keystore 指定生成的KeyStore文件名和路径,这里是D:/keys/XuanyuKey
 * @see   1.2.CMD输出-->输入密钥库口令：xuanyu75(以前叫做'输入keystore密码')
 * @see                 再次输入新密码：xuanyu75
 * @see                 您的名字与姓氏是什么？[Unknown]：sso.jadyer.com(单点服务器域名：要根据实际情况填写域名,否则会导致证书上的名称无效)
 * @see                 您的组织单位名称是什么？[Unknown]：http://blog.csdn.net/jadyer
 * @see                 您的组织名称是什么？[Unknown]：JavaLover_Jadyer
 * @see                 您所在的城市或区域名称是什么？[Unknown]：重庆
 * @see                 您所在的州或省份名称是什么？[Unknown]：江北区
 * @see                 该单位的两字母国家代码是什么[Unknown]：ZH
 * @see                 CN=sso.jadyer.com, OU=http://blog.csdn.net/jadyer, O=JavaLover_Jadyer, L=重庆, ST=江北区, C=ZH 正确吗？[否]：y
 * @see                 输入<XuanyuKeyStore>的主密码（如果和 keystore 密码相同，按回车）：这里按回车键
 * @see                 (这里的主密码一定要与keystore密码相同,否则启动Tomcat时就会告诉你java.io.IOException: Cannot recover key)
 * @see   1.3.接下来生成了D:/keys/XuanyuKey文件
 * @see 2.导出证书
 * @see   2.1.D:\>keytool -export -file D:/keys/Xuanyu.crt -alias XuanyuKeyStore -keystore D:/keys/XuanyuKey
 * @see       -alias需要输入生成KeyStore时用的别名,否则会提示'keytool 错误: java.lang.Exception: 别名 <XuanyuCRT> 不存在'
 * @see       输入密钥库口令应输入生成KeyStore时设置的口令
 * @see       导入成功后控制台会打印'存储在文件 <D:/keys/Xuanyu.crt> 中的证书'
 * @see   2.2.当浏览器访问单点客户端时,首次会跳到CAS服务端登录页面,这时浏览器会先询问用户,这是一个未知SSL的请求,是否继续
 * @see       如果把CRT证书导入到浏览器中,就不会看到这个询问页面了,下面是导入CRT到浏览器的步骤
 * @see        双击Xuanyu.crt-->安装证书-->下一步-->将所有的证书放入下列存储-->浏览-->受信任的根证书颁发机构-->下一步-->完成
 * @see        这时会弹出对话框'您即将从一个声称代表如下的证书颁发机构安装证书:sso.jadyer.com......您确认安装此证书吗'-->是-->导入成功-->确定
 * @see 3.导入证书到JVM
 * @see   3.1.D:\>keytool -import -keystore D:\Develop\Java\jdk1.7.0_76\jre\lib\security\cacerts -file D:/keys/Xuanyu.crt -alias XuanyuKeyStore
 * @see           输入密钥库口令:xuanyu75
 * @see           所有者: CN=sso.jadyer.com, OU=http://blog.csdn.net/jadyer, O=JavaLover_Jadyer, L=重庆, ST=江北区, C=ZH
 * @see           发布者: CN=sso.jadyer.com, OU=http://blog.csdn.net/jadyer, O=JavaLover_Jadyer, L=重庆, ST=江北区, C=ZH
 * @see           序列号: 5149ee5a
 * @see           有效期开始日期: Fri Jul 24 15:21:24 CST 2015, 截止日期: Sun May 13 15:21:24 CST 2018
 * @see           证书指纹:
 * @see                    MD5: 84:E1:92:ED:71:49:D4:D5:94:CB:AB:35:BB:01:81:0B
 * @see                    SHA1: A0:1E:BD:CE:8B:DB:F3:7E:2C:ED:7A:9A:5D:72:41:3C:1D:28:D1:2A
 * @see                    SHA256: B2:90:99:8D:29:BA:87:48:AE:3A:D8:E2:AD:8E:9E:F3:1B:95:FF:65:29:
 * @see           48:BD:EB:C4:29:14:9E:69:86:B1:D6
 * @see                    签名算法名称: SHA256withRSA
 * @see                    版本: 3
 * @see           
 * @see           扩展:
 * @see           
 * @see           #1: ObjectId: 2.5.29.14 Criticality=false
 * @see           SubjectKeyIdentifier [
 * @see           sun.security.x509.KeyIdentifier [
 * @see           0000: 9C 46 74 89 5B AA 10 52   24 4C 7B F3 49 66 CD 81  .Ft.[..R$L..If..
 * @see           0010: F4 A9 E5 22                                        ..."
 * @see           ]
 * @see           ]
 * @see           
 * @see           是否信任此证书? [否]:  y
 * @see           证书已添加到密钥库中
 * @see   注意在输入密钥口令后若提示'keytool 错误: java.io.IOException: Keystore was tampered with, or password was incorrect'
 * @see   那就换个密码,输入'changeit'就行了
 * @see 4.应用证书到Tomcat-8.0.21
 * @see   4.1.拷贝生成的D:/keys/XuanyuKey到\\%TOMCAT_HOME%\\conf\\目录中(其它目录也可以)
 * @see   4.2.server.xml
 * @see       <Connector port="8443" protocol="HTTP/1.1" SSLEnabled="true"
 * @see                  maxThreads="150" scheme="https" secure="true"
 * @see                  clientAuth="false" sslProtocol="TLS" URIEncoding="UTF-8"
 * @see                  keystoreFile="conf/XuanyuKey" keystorePass="xuanyu75"/>
 * @see   4.3.浏览器访问https://127.0.0.1:8443/blog会发现你的应用已经处于SSL安全通道中了
 * @see   4.4.但此时访问http://127.0.0.1:8080/blog会发现也能访问
 * @see       也就是说,我们虽然启用了HTTPS,但现在还可以绕开HTTPS直接HTTP访问,如此HTTPS也就起不到作用了
 * @see       修改\\%TOMCAT_HOME%\\conf\\web.xml文件,尾部<welcome-file-list>标签后加入下面的内容即可
 * @see       <security-constraint>
 * @see           <!-- Authorization setting for SSL -->
 * @see           <web-resource-collection>
 * @see               <web-resource-name>SSL_App</web-resource-name>
 * @see               <!-- 指明需要SSL的url -->
 * @see               <url-pattern>/*</url-pattern>
 * @see               <http-method>GET</http-method>
 * @see               <http-method>POST</http-method>
 * @see           </web-resource-collection>
 * @see           <user-data-constraint>
 * @see               <!-- 指明需要SSL -->
 * @see               <transport-guarantee>CONFIDENTIAL</transport-guarantee>
 * @see           </user-data-constraint>
 * @see       </security-constraint>
 * @see 你也可以申请免费的StartSSL CA证书: StartSSL(公司名：StartCom)也是一家CA机构
 * @see 它的根证书很久之前就被一些具有开源背景的浏览器支持(Firefox/Chrome/Safari等)
 * @see 申请地址：http://www.startssl.com
 * @see 申请参考：http://www.linuxidc.com/Linux/2011-11/47478.htm
 * @see ------------------------------------------------------------------------------------------------------------------------
 * @see CAS客户端配置单点登录
 * @see 这里用的是cas-client-core-3.4.0.jar(这是2015-07-21发布的)
 * @see 下载地址http://mvnrepository.com/artifact/org.jasig.cas.client/cas-client-core/3.4.0
 * @see 另外为了使客户端在HTTP协议下单点成功,可以修改以下两处配置使其不开启HTTPS验证
 * @see 1.\WEB-INF\deployerConfigContext.xml
 * @see   <bean class="org.jasig...support.HttpBasedServiceCredentialsAuthenticationHandler">添加p:requireSecure="false"
 * @see 2.\WEB-INF\spring-configuration\ticketGrantingTicketCookieGenerator.xml和\WEB-INF\spring-configuration\warnCookieGenerator.xml
 * @see   p:cookieSecure="true"改为p:cookieSecure="false"
 * @see 下面介绍两种配置方法,一种是纯web.xml配置,一种是借助Spring来配置,相关的官方文档如下所示
 * @see https://wiki.jasig.org/display/CASC/Configuring+the+Jasig+CAS+Client+for+Java+in+the+web.xml
 * @see https://wiki.jasig.org/display/CASC/Configuring+the+JA-SIG+CAS+Client+for+Java+using+Spring
 * @see 【纯web.xml】
 * @see web.xml中需配置四个顺序固定的Filter,而且出于认证考虑,最好配置在其他Filter之前,它们的先后顺序如下
 * @see AuthenticationFilter
 * @see TicketValidationFilter(或其它AbstractTicketValidationFilter实现,比如Cas20ProxyReceivingTicketValidationFilter)
 * @see HttpServletRequestWrapperFilter
 * @see AssertionThreadLocalFilter
 * @see 另外各个Filter的<init-param>优先级都比<context-param>要高,通常<context-param>用来配置公用的参数
 * @see 1.AuthenticationFilter
 * @see   用来拦截请求,判断是否需要CASServer认证,需要则跳转到CASServer登录页,否则放行请求
 * @see   有两个必须参数,一个是指定CASServer登录地址的casServerLoginUrl,另一个是指定认证成功后跳转地址的serverName或service
 * @see   service和serverName设置一个即可,二者都设置时service的优先级更高,即会以service为准
 * @see   service指的是一个确切的URL,而serverName是用来指定客户端的主机名的,格式为{protocol}:{hostName}:{port}
 * @see   指定serverName时,该Filter会把它附加上当前请求的URI及对应的查询参数来构造一个确切的URL作为认证成功后的跳转地址
 * @see   比如serverName为"http://gg.cn",当前请求的URI为"/oa",查询参数为"aa=bb",则认证成功后跳转地址为http://gg.cn/oa?aa=bb
 * @see   casServerLoginUrl--去哪登录,serverName--我是谁
 * @see 2.TicketValidationFilter
 * @see   请求通过AuthenticationFilter认证后,若请求中携带了ticket参数,则会由该类Filter对携带的ticket进行校验
 * @see   验证ticket的时候,要访问CAS服务的/serviceValidate接口,使用的url就是${casServerUrlPrefix}/serviceValidate
 * @see   所以它也有两个参数是必须指定的,casServerUrlPrefix(CASServer对应URL地址的前缀)和serverName或service
 * @see   实际上,TicketValidationFilter只是对验证ticket的这一类Filter的统称,其并不对应CASClient中的具体类型
 * @see   CASClient中有多种验证ticket的Filter,都继承自AbstractTicketValidationFilter
 * @see   常见的有Cas10TicketValidationFilter/Cas20ProxyReceivingTicketValidationFilter/Saml11TicketValidationFilter
 * @see   它们的验证逻辑都是一致的,都有AbstractTicketValidationFilter实现,只是使用的TicketValidator不一样而已
 * @see   如果要从服务器获取用户名之外的更多信息应该采用CAS20这个2.0协议的代理
 * @see 3.HttpServletRequestWrapperFilter
 * @see   用于封装每个请求的HttpServletRequest为其内部定义的CasHttpServletRequestWrapper
 * @see   它会将保存在Session或request中的Assertion对象重写HttpServletRequest的getUserPrincipal()、getRemoteUser()、isUserInRole()
 * @see   这样在我们的应用中就可以非常方便的从HttpServletRequest中获取到用户的相关信息
 * @see 4.AssertionThreadLocalFilter
 * @see   为了方便用户在应用的其它地方获取Assertion对象,其会将当前的Assertion对象存放到当前的线程变量中
 * @see   以后用户在程序的任何地方都可以从线程变量中获取当前的Assertion,而无需从Session或request中解析
 * @see   该线程变量是由AssertionHolder持有的,我们在获取当前的Assertion时也只需Assertion assertion = AssertionHolder.getAssertion()
 * @see 【借助Spring】
 * @see 与上述web.xml配置四个Filter方式不同的是,可以使用Spring的四个DelegatingFilterProxy来代理需要配置的四个Filter
 * @see 此时这四个Filter就应该配置为Spring的Bean对象,并且web.xml中的<filter-name>就应该对应SpringBean名称
 * @see 但是SingleSignOutFilter/HttpServletRequestWrapperFilter/AssertionThreadLocalFilter等Filter不含配置参数
 * @see 所以实际上只需要配置AuthenticationFilter和Cas20ProxyReceivingTicketValidationFilter两个Filter交由Spring代理就可以了
 * @see 【注意】
 * @see 1.CAS1.0提供的接口有/validate，CAS2.0提供的接口有/serviceValidate,/proxyValidate,/proxy
 * @see 2.四个Filter太多了,有时间的话考虑参考org.springframework.web.filter.CompositeFilter写一个Filter来实现
 * @see 3.web.xml的好处是可以配置匿名访问的资源,配置参数参考AuthenticationFilter中的ignoreUrlPatternMatcherStrategyClass
 * @see   起码cas-client-core-3.4.0.jar中的Spring配置还不支持ignorePattern(该参数默认正则验证,此外还有contains和equals验证)
 * @see 4.javax.net.ssl.SSLHandshakeException: java.security.cert.CertificateException: No name matching casserver found
 * @see   这是由于创建证书的域名和应用中配置的CAS服务域名不一致导致出错(说白了就是指客户端导入的CRT证书与CAS服务端的域名不同)
 * @see 【测试】
 * @see 测试时在C:\Windows\System32\drivers\etc\hosts中添加以下三个配置
 * @see 127.0.0.1 sso.jadyer.com
 * @see 127.0.0.1 boss.jadyer.com
 * @see 127.0.0.1 risk.jadyer.com
 * @see 然后拷贝三个Tomcat,分别用作sso服务器和两个sso客户端
 * @see 修改两个sso客户端的\Tomcat\conf\server.xml的以下三个端口,保证启动监听端口不重复
 * @see <Server port="8105" shutdown="SHUTDOWN">
 * @see <Connector port="8180" protocol="HTTP/1.1"......>
 * @see <Connector port="8109" protocol="AJP/1.3" redirectPort="8443" />
 * @see <Server port="8205" shutdown="SHUTDOWN">
 * @see <Connector port="8280" protocol="HTTP/1.1"......>
 * @see <Connector port="8209" protocol="AJP/1.3" redirectPort="8443" />
 * @see 最后修改两个sso客户端的\Tomcat\webapps\cas-client\WEB-INF\classes\config.properties的casClientServerName值
 * @see casClientServerName=http://boss.jadyer.com:8180
 * @see casClientServerName=http://risk.jadyer.com:8280
 * @see ------------------------------------------------------------------------------------------------------------------------
 * @see CAS客户端配置单点登出
 * @see 与单点登录相对应,通过CASServer登出所有的CASClient,登录的URL是/login,登出的URL是/logout
 * @see 这里需要配置SingleSignOutFilter和SingleSignOutHttpSessionListener
 * @see SingleSignOutFilter用来使Session失效,SingleSignOutHttpSessionListener用于在Session过期时移除其对应的映射关系
 * @see 1.要为SingleSignOutFilter配置casServerUrlPrefix参数
 * @see 2.默认的登出后会跳转到CASServer的登出页,若想跳转到其它资源,可在/logout的URL后面加上service=you want to jump url
 * @see   比如http://sso.jadyer.com:8080/cas-server-web/logout?service=http://blog.csdn.net/jadyer
 * @see   但默认servcie跳转不会生效,需要CASServer配置/WEB-INF/cas.properties中的cas.logout.followServiceRedirects=true
 * @see   另外为org.jasig.cas.client.session.SingleSignOutFilter增加service参数是没用的,因为登出后跳转到指定资源属于服务端行为
 * @see 3.禁用单点登出
 * @see   CASServer/WEB-INF/cas.properties中的slo.callbacks.disabled=true
 * @see   测试时点击登出后虽然页面跳转到了默认登出页,但再次访问CASClient资源发现并没有登出,即单点登出禁用成功
 * @see 4.测试单点登出
 * @see   测试时先登出,然后在浏览器新标签页访问CASClient资源,发现会自动重定向到单点登录页
 * @see   或者登出后,点浏览器后退按钮,发现会后退到之前的CASClient资源页,但在这个页面点击任何请求,都会自动重定向到单点登录页
 * @see ------------------------------------------------------------------------------------------------------------------------
 * @see CAS服务端RememberMe
 * @see 先介绍一下CAS-4.0.3服务端的来自cas.properties中的一些其它配置项
 * @see 1.cas.securityContext.status.allowedSubnet=127.0.0.1
 * @see   可以访问的服务端统计页面：http://sso.jadyer.com:8080/cas-server-web/status
 * @see   可以访问的服务端统计页面：http://sso.jadyer.com:8080/cas-server-web/statistics
 * @see 2.host.name=S3
 * @see   uniqueIdGenerators.xml中的各种UniqueTicketIdGenerator生成TGT/ST等ticket时会用到host.name作为ticket的后缀
 * @see   host.name通常用在集群环境下,其值对于每个节点来说都必须是唯一的,这样整个集群环境生成的各种ticket也必定是唯一的
 * @see   单机环境下就没必要修改它了
 * @see 3.cas.logout.followServiceRedirects=true
 * @see   是否允许客户端Logout后重定向到service参数指定的资源
 * @see 4.tgt.maxTimeToLiveInSeconds=28800
 * @see   指定Session的最大有效时间,即从生成到指定时间后就将超时,默认28800s,即8小时
 * @see 5.tgt.timeToKillInSeconds=7200
 * @see   指定用户操作的超时时间,即用户在多久不操作后就超时,默认7200s,即2小时
 * @see   经本人亲测：在测试tgt.timeToKillInSeconds时还要注意客户端web.xml配置的超时时间
 * @see   即只有客户端配置超时时间不大于tgt.timeToKillInSeconds时才能看见服务端设置的效果
 * @see 6.st.timeToKillInSeconds=10
 * @see   指定service ticket的有效时间,默认10s
 * @see   这也是debug追踪CAS应用认证过程中经常会失败的原因,因为追踪的时候service ticket已经过了10秒有效期了
 * @see 7.slo.callbacks.disabled=false
 * @see   是否禁用单点登出
 * @see 关于RememberMe,可参考官方文档,网址如下(下面两个网址描述的RememberMe实现都是一样的,只是第二个还有其它描述)
 * @see http://jasig.github.io/cas/development/installation/Configuring-LongTerm-Authentication.html
 * @see http://jasig.github.io/cas/4.0.x/installation/Configuring-Authentication-Components.html#long-term-authentication
 * @see RememberMe也就是平时所说的记住密码的功能,可以让用户登录成功后,关闭浏览器再重新打开浏览器访问应用时不需要再次登录
 * @see RememberMe与上面的Session超时配置tgt.timeToKillInSeconds是两回事,Session超时是针对一次会话而言,RememberMe则更广
 * @see 另外本文的CAS-4.0.3服务端源码修改,是在我的以下三篇博文基础上修改的
 * @see http://blog.csdn.net/jadyer/article/details/46875393
 * @see http://blog.csdn.net/jadyer/article/details/46914661
 * @see http://blog.csdn.net/jadyer/article/details/46916169
 * @see 具体修改步骤如下
 * @see 1.cas.properties中新增配置项rememberMeDuration=1209600
 * @see 2.ticketExpirationPolicies.xml中新增RememberMe过期策略的配置
 * @see 3.ticketGrantingTicketCookieGenerator.xml中新增属性项p:rememberMeMaxAge="${rememberMeDuration:1209600}"
 * @see 4.deployerConfigContext.xml
 * @see 5.casLoginView.jsp表单中增加rememberMe字段
 * @see 6.login-webflow.xml增加接收表单rememberMe字段的配置
 * @see 7.UsernamePasswordCaptchaCredential.java集成RememberMeUsernamePasswordCredential使得可以接收表单的rememberMe字段
 * @see ------------------------------------------------------------------------------------------------------------------------
 * @create 2015-7-14 上午11:27:16
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class CASTest {}