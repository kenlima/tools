<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>Sql Log Formatter</h1>
	<form name="frm" action="/sqllogformat/result" method="post">
		<table>
			<tr>
				<td><input type="submit" value="Go"></td>
			</tr>
			<tr>
				<td><textarea rows="30" cols="150" name="sqlLog"></textarea></td>
			</tr>
			<tr>
				<td><input type="submit" value="Go"></td>
			</tr>
		</table>
	</form>

</body>
</html>