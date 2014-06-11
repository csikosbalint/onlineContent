<html>
<% if (request.getAttribute("session") != null && ((HttpSession)request.getAttribute("session")).getAttribute("admin") != null ) { %>



<form action="/">
<button type="submit" name="forceReload">Reload list from database (new query)</button>
</form>
<!-- Use of this code assumes agreement with the Google Custom Search Terms of Service. -->
<!-- The terms of service are available at http://www.google.com/cse/docs/tos.html -->
<form name="cse" id="searchbox_demo" action="http://www.google.com/cse">
  <input type="hidden" name="cref" value="" />
  <input type="hidden" name="ie" value="utf-8" />
  <input type="hidden" name="hl" value="" />
  <input name="q" type="text" size="40" />
  <input type="submit" name="sa" value="Search" />
</form>
<script type="text/javascript" src="https://www.google.com/cse/publicurl?cx=004811520739431370780:ggegf7qshxe"></script>


<% } else { %>
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
<form name="form" method="post" action="/" onsubmit="javascript:return validate();">
<table>
<tr><td>Username:</td><td><input type="text" name="user"></td></tr>
<tr><td>Password:</td><td><input type="password" name="pass"></td></tr>
<tr><td></td><td><input type="submit" value="Submit"></td></tr>
</table>
</form>
<% } %>
</html>