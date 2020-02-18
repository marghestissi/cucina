<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
 <body>
  <form action="loginUtente">
	<table>
	  
		<tr height="50" align="center">
			<th colspan="3" valign="middle">LOGIN</th>
		</tr>

		<tr height="50" >
			<td width="20%">Username</td>
			<td width="40%"><input type="text" name="idUtente"></td>
			<td width="40%"><c:forEach items="${lista}" var="errore">
								<c:if test="${errore.campoValidato=='idUtente'}" > ${errore.descrizioneErrore}</c:if>
							</c:forEach>
		</tr>
		<tr height="50" >
			<td width="20%">Password</td>
			<td width="40%"><input type="text" name="password"></td>
			<td width="40%"><c:forEach items="${lista}" var="errore">
								<c:if test="${errore.campoValidato=='password'}" > ${errore.descrizioneErrore}</c:if>
							</c:forEach>
		</tr>
		<tr height="50">
			<th colspan="3" valign="middle"><input type="submit" value="registra" ><br></th>
		</tr>
	</table>
   </form>
  </body>
</html>

