<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	<form action="shiro/login" method="post">
		username：<input type="text" name="username"></input><br>
		password：<input type="password" name="password"></input><br>
		 <input type="checkbox" name="rememberMe" />记住我
		<input type="submit" value="Submit"></input>
	</form>
	
</body>
</html>