<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%-- shiro 标签：
	1). <shiro:guest></shiro:guest>：用户没有身份验证时显示标签体信息，即游客访问信息（可以在里面添加登录按钮）
	2). <shiro:user></shiro:user>：用户已经通过身份验证（包括记住我登录）后显示标签体信息
	3). <shiro:authenticated></shiro:authenticated>：用户已经通过身份验证（不包括记住我登录）后显示标签体信息
	4). <shiro:notAuthenticated></shiro:notAuthenticated>：用户未通过身份验证（包括记住我登录也算未通过）后显示标签体信息
	5). <shiro:principal></shiro:principal>：显示用户身份信息
	6). <shiro:hasRole name="admin"></shiro:hasRole>：如果当前subject有某一角色则显示标签体信息
	7). <shiro:hasAnyRoles name="admin,scott"></shiro:hasAnyRoles>：如果当前subject有任一角色则显示标签体信息
	8). <shiro:lacksRole name="admin"></shiro:lacksRole>：如果当前subject没有角色则显示标签体信息
	9). <shiro:hasPermission name="user:delete"></shiro:hasPermission>：如果当前subject有某操作权限则显示标签体信息
	10). <shiro:lacksPermission name="user:delete"></shiro:lacksPermission>：如果当前subject没有某有操作权限则显示标签体信息
	
	 --%>
	<shiro:guest>
		<button onclick="window.location.href='login.jsp'" >登录</button>
	</shiro:guest>
	
	<h4>Enter Success </h4></br></br>
	
	Welcome: <shiro:principal></shiro:principal></br></br>
	
	<a href="admin.jsp">进入 admin 角色权限界面</a></br></br>
	
	<shiro:hasRole name="scott">
		<a href="scott.jsp">进入 scott 角色权限界面</a></br></br>
	</shiro:hasRole>
	
	<a href="shiro/testShiroAnnotation">测试Shiro权限注解</a></br></br>
	
	<a href="shiro/logout">logout</a></br></br>
</body>
</html>