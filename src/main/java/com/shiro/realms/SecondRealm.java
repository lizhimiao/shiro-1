package com.shiro.realms;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.util.ByteSource;

public class SecondRealm extends AuthenticatingRealm {

	
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
		
		//(1). principal：认证的实体信息. 常用来表示用户名username，也可以是数据表对应的实体类对象，只要唯一即可；
		// 还可以是任意 字符串或任意对象，暂时未发现对认证比对结果产生任何影响
		Object principal = this;
		
		// MD5盐值加密方式： 
		String algorithmName = "SHA1";   // 加密算法
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
		
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(principal, hashedCredentials, credentialsSalt, realmName);
		return info;
	}

}