<html>
<script>
function validate(){
var username=document.form.user.value;
var password=document.form.pass.value;
if(username==""){
 alert("Enter Username!");
  return false;
}
if(password==""){
 alert("Enter Password!");
  return false;
}
return true;
}
</script>
<form name="form" method="post" action="/admin" onsubmit="javascript:return validate();">
<table>
<tr><td>Username:</td><td><input type="text" name="user"></td></tr>
<tr><td>Password:</td><td><input type="password" name="pass"></td></tr>
<tr><td></td><td><input type="submit" value="Submit"></td></tr>
</table>
</form>
<%= ((HttpSession)request.getAttribute("session")).toString() %>
		<% if (request.getAttribute("session") != null && ((HttpSession)request.getAttribute("session")).getAttribute("admin") != null ) { %>
		<button type="button">Reload</button>
		<% } else { %> 
		<img src="/static/banner_v.gif">
		<% } %>
</html>
<%String msg=request.getParameter("msg");
if(msg!=null){
    %>
<label><font color="red"><%=msg%></font></label> 
<%
}
%>