package com.shiro.Service;

import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.session.Session;

public class ShiroService {
	
	/** Shiro 权限注解：
	 * 1).@RequireAuthentication： Subject 已经通过身份验证(不包括记住我登录)
	 * 2).@RequireUser： Subject 已经通过身份验证(包括记住我登录)
	 * 3).@RequireGust： Subject 未经身份验证(游客)
	 * 4).@RequireRoles(value={"scott","admin"},logical="Logical.AND")：当前 Subject 需要角色...
	 * 5).@RequirePermissions：当前 Subject 需要操作权限...
	 */
	@RequiresRoles(value={"scott"})
	public void testShiroAnnotation(){
		System.out.println("testShiroAnnotation---time：" + new Date());
		
		// shiro 提供的 session 不依赖于底层容器(如tomcat)，不管是 JavaSE 还是 JavaEE 环境都可以使用
		// 使用Shiro 的session 即便是在 Service 层也可以访问到 session 会话中的数据
		Session session = SecurityUtils.getSubject().getSession();
		System.out.println("Service SessionVal：" + session.getAttribute("key"));
	}
}
