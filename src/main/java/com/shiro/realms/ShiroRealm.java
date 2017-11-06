package com.shiro.realms;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/** Shiro 认证流程：
 * 1. 获取当前的 Subject，调用 SecurityUtils.getSubject();
 * 2. 测试当前用户是否已经被认证通过，调用 Subject#isAuthenticated();
 * 3. 如果认证未通过，把用户名和密码封装为 UsernamePasswordToken 对象，
 * 1). 创建一个表单
 * 2). 把请求提交给 SpringMvc 的 handler
 * 3). 使用 new UsernamePasswordToken() 封装用户名和密码.
 * 4. 执行登录，调用 Subject#login(token);
 * 5. 自定义 Realm 的方法，从数据库中获取对应的记录，将 info(如  AuthenticationInfo) 返回给 Shiro，
 * 1). 实际上需要继承 org.apache.shiro.realm.AuthenticatingRealm，
 * 2). 实现 doGetAuthenticationInfo(AuthenticationToken) 方法.
 * 6. 由 Shiro 完成密码的比对 
 * 			-------> shiro 会自动地根据配置将请求信息中的密码加密并和从数据库中获取的记录对比.
 * 1). 通过  AuthenticatingRealm  的 credentialsMatcher 属性进行密码的比对!
 * 2). 使用 MD5盐值加密；直接使用 HashedCredentialsMatcher 对象并设置加密算法即可.
 * 3). 使用 ByteSource.Util.bytes() 来计算盐值；盐值需要唯一，一般为随机字符串或实体属性
 * 附. 使用 new SimpleHash(algorithmName, source, salt, hashIterations) 来计算盐值加密后的密码的值.
 * 
 * 7. 角色授权
 * 1). 授权需要 继承 AuthorizingRealm 抽象类，实现其 doGetAuthorizationInfo(PrincipalCollection)方法；
 * 2). AuthorizingRealm 继承自  AuthenticatingRealm，但未实现其 doGetAuthenticationInfo(AuthenticationToken)方法，
 * 所以，想要实现 认证和授权 只需继承 AuthorizingRealm 类，并实现上面两个抽象方法即可.	
 * @author 92811
 *
 */
public class ShiroRealm extends AuthorizingRealm {

	// 认证时被调用的方法
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		token.getPrincipal();
		//1. 将 AuthenticationToken 转化为 UsernamePasswordToken 对象；
		UsernamePasswordToken userToken =  (UsernamePasswordToken) token;
		
		//2. 从 UsernamePasswordToken 中获取 username;
		String username = userToken.getUsername(); 
		
		//3. 调用数据库的方法，从数据库中查询 username 对应的用户记录；
		System.out.println("从数据库中获取用户信息：" + username +" 对应信息");
		
		//4. 若用户不存在，则抛出 UnknownAccountException 异常；
		if("unknown".equals(username)){
			throw new UnknownAccountException("用户不存在");
		}
		
		//5. 根据用户信息，抛出其它异常；
		if("monster".equals(username)){
			throw new LockedAccountException("用户被锁定");
		}
		
		//6. 根据用户信息，构建并返回 AuthenticationInfo 对象，通常使用的实现类是 SimpleAuthenticationInfo.
		
		//(1). principal：认证的实体信息. 常用来表示用户名 username，也可以是数据表对应的实体类对象，只要唯一即可；
		// 还可以是任意 字符串或任意对象，暂时未发现对认证比对结果产生任何影响
		Object principal = username;
		
		// MD5盐值加密方式： 
		String algorithmName = "MD5";   // 加密算法
		Object source = "123456";       // 密码
		Object salt = ByteSource.Util.bytes(username);  // 盐值，通过 ByteSource.Util.bytes()计算盐值
		int hashIterations = 1024;      // 加密次数
		//  credentials： 密码； 
		//(2).  hashedCredentials： MD5盐值加密后的密码；------> 应为从数据库中获取的加密密码
		Object hashedCredentials = new SimpleHash(algorithmName, source, salt, hashIterations);
		
		//(3). credentialsSalt：盐值加密. 盐值需要唯一，一般为随机字符串或实体属性
		ByteSource credentialsSalt = ByteSource.Util.bytes(username);
		
		//(4). realmName： 当前 Realm 对象的 name，调用父类的 getName()方法即可
		String realmName = getName();
		
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(principal, hashedCredentials, credentialsSalt, realmName);
		return authenticationInfo;
	}

	// 授权时被调用的方法
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		//1. 获取登录成功后的用户名
		Object principal = principals.getPrimaryPrincipal();
		
		//2. 给用户进行角色授权 (可能需要根据用户名来查询数据库中当前用户的角色权限)
		Set<String> roles = new HashSet<>();
		roles.add("admin");
		if(principal.equals("scott")){
			roles.add("scott");
		}
		
		//3. 创建并返回 AuthorizationInfo 对象
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo(roles);
		return authorizationInfo;
	}

}
