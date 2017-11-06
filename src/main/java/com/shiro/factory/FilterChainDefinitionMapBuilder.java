package com.shiro.factory;

import java.util.LinkedHashMap;

public class FilterChainDefinitionMapBuilder {
	
	public  LinkedHashMap<String, String> buildfilterChainDefinitionMap(){
		LinkedHashMap<String, String> map = new LinkedHashMap<>();
//		/shiro/login = anon
//		/shiro/logout = logout
//		
//		/admin.jsp = roles[admin]
//		/scott.jsp = roles[scott]
//		
//		# everything else requires authentication:
//		/** = authc
		map.put("/shiro/login", "anon");
		map.put("/shiro/logout", "logout");
		map.put("/admin.jsp", "authc,roles[admin]");
		map.put("/scott.jsp", "authc,roles[scott]");
		
		map.put("/**", "authc");
		
		
		return map;
	}
}
