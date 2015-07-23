<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
<script type="text/javascript" src="/js/jquery.zclip.min.js"></script>
<script>
	$(document).ready(function() {
		// 특수 공백문자 일반 공백문자로 치환
		// TODO replace 서버에서 할 수 있도록 
		$('a#copy-description').zclip({ path : '/js/ZeroClipboard.swf',
		copy : function() {
			var idx = $(this).data('idx');
			return ($('p#description_' + idx).text()).replace(/ /gi, " ")
		},
		afterCopy : function() {
			//alert("Complete copy to clipboard.");
		}
		});
		// The link with ID "copy-description" will copy
		// the text of the paragraph with ID "description"
	});
</script>
</head>
<body>
	<h1>Sql Log Formatter</h1>
	Total Sql : ${fn:length(formattedSqls)}
	<table border="1">
		<c:forEach var="formattedSql" items="${formattedSqls}" varStatus="status">
			<tr>
				<td align="center" height="25">parameter</td>
				<td>${formattedSql.parameters}</td>
			</tr>
			<tr>
				<td align="center">sql
					<div style="position: relative;">
						<a href="#" id="copy-description" data-idx="${status.index}">Copy</a>
					</div>
				</td>
				<td>
					<p id="description_${status.index}">${formattedSql.sql}</p>
				</td>
			</tr>
			<tr>
				<td colspan="2" height="20"></td>
			</tr>
		</c:forEach>
	</table>

</body>
</html>