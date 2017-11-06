package com.shiro.handler;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shiro.Service.ShiroService;

/** Shiro 认证流程：
 * 1. 获取当前的 Subject，调用 SecurityUtils.getSubject();
 * 2. 测试当前用户是否已经被认证通过，调用 Subject#isAuthenticated();
 * 3. 如果认证未通过，把用户名和密码封装为 UsernamePasswordToken 对象，
 * 1). 创建一个表单
 * 2). 把请求提交给 SpringMvc 的 handler
 * 3). 使用 new UsernamePasswordToken() 封装用户名和密码.
 * 4. 执行登录，调用 Subject#login(token);
 * 5. 自定义 Realm 的方法，从数据库中获取对应的记录，返回给 Shiro，
 * 1). 实际上需要继承 org.apache.shiro.realm.AuthenticatingRealm，
 * 2). 实现 doGetAuthenticationInfo(AuthenticationToken) 方法.
 * 6. 由 Shiro 完成密码的比对.
 * 1). 通过  AuthenticatingRealm  的 credentialsMatcher 属性进行密码的比对!
 * 2). 使用 MD5盐值加密；直接使用 HashedCredentialsMatcher 对象并设置加密算法即可.
 * 3). 使用 ByteSource.Util.bytes() 来计算盐值；盐值需要唯一，一般为随机字符串或实体属性
 * 4). 使用 new SimpleHash(algorithmName, source, salt, hashIterations) 来计算盐值加密后的密码的值.
 * 
 * @author 92811
 *
 */
@Controller
@RequestMapping("/shiro")
public class ShiroHandler {
	
	@Autowired
	private ShiroService shiroService; 
	@Autowired
	DefaultWebSecurityManager securityManager;
	
	@RequestMapping("/login")
	public String shiroLogin(@RequestParam("username") String username,
			@RequestParam("password") String password , boolean rememberMe){
		// 获取当前的 Subject，调用 SecurityUtils.getSubject()；
        Subject currentUser = SecurityUtils.getSubject();
        // 测试当前用户是否已经被认证，即是否已经登录. 调用 Subject#isAuthenticated()；
        if (!currentUser.isAuthenticated()) {
        	// 把用户名和密码封装为 UsernamePasswordToken 对象
            UsernamePasswordToken token = new UsernamePasswordToken(username, password,rememberMe);
            // rememberme
            try {
            	// 执行登录，调用Subject#login(token)方法；
                currentUser.login(token);
                
            }
            // ... catch more exceptions here (maybe custom ones specific to your application?
            // 所有认证异常的父类.
            catch (AuthenticationException ae) {
                System.out.println("登录失败：" + ae.getMessage());
               // return "redirect:/login.jsp";
            }
        }
          
        
        // 这里的页面必须重定向-redirect，否则无论登录成功与否，都会跳转到成功后界面 
        return "redirect:/list.jsp";    
		
	}
	
	// 测试 Shiro 权限注解
	@RequestMapping("/testShiroAnnotation")
	public String testShiroAnnotation(HttpSession session){
		shiroService.testShiroAnnotation();
		
		// 在 Service 层测试 Shiro 提供的 session
		session.setAttribute("key", "value");
		return "redirect:/list.jsp";
	}
}
