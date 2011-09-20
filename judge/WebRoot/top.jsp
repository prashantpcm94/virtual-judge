<%@ taglib prefix="s" uri="/struts-tags"%>
<table width="100%" border="0" class="banner" style="margin-bottom:30px;"><tr><td><a href="toIndex.action">Home</a></td><td><a href="problem/toListProblem.action">Problems</a></td><td><a href="problem/status.action">Status</a></td><td><a href="contest/toListContest.action">Contest</a></td><td><s:if test="#session.visitor != null">[<a id="my_username" href="user/toUpdate.action?uid=<s:property value="#session.visitor.id" />"><s:property value="#session.visitor.username" /></a>]</s:if><s:else><a id="register" href="javascript:void(0)">Register</a></s:else></td><td><s:if test="#session.visitor != null"><a id="logout" href="javascript:void(0)">Logout</a></s:if><s:else><a id="login" href="javascript:void(0)">Login</a></s:else></td></tr></table>
<s:if test="#session.visitor == null">
<div id="dialog-form-login" style="display: none" title="Login">
	<p class="validateTips"></p>
	<form>
	<fieldset>
		<label for="username">Username *</label>
		<input type="text" id="username" class="text ui-widget-content ui-corner-all" />
		<label for="password">Password *</label>
		<input type="password" id="password" class="text ui-widget-content ui-corner-all" />
	</fieldset>
	</form>
</div>
<div id="dialog-form-register" style="display: none" title="Register">
	<p class="validateTips"></p>
	<form>
	<fieldset>
		<div style="width:200px;float:left">
			<label for="username1">Username *</label>
			<input type="text" id="username1" class="text ui-widget-content ui-corner-all" />
			<label for="password1">Password *</label>
			<input type="password" id="password1" class="text ui-widget-content ui-corner-all" />
			<label for="repassword">Repeat *</label>
			<input type="password" id="repassword" class="text ui-widget-content ui-corner-all" />
			<label for="nickname">Nickname</label>
			<input type="text" id="nickname" class="text ui-widget-content ui-corner-all" />
			<label for="school">School</label>
			<input type="text" id="school" class="text ui-widget-content ui-corner-all" />
			<label for="qq">QQ</label>
			<input type="text" id="qq" class="text ui-widget-content ui-corner-all" />
			<label for="email">Email</label>
			<input type="text" id="email" class="text ui-widget-content ui-corner-all" />
			<label for="share">Share code by default</label><br />
			<s:radio id="share" name="share" list="#{'0':'No', '1':'Yes'}" value="1" theme="simple" />
		</div>
		<div style="width:200px;margin-left:20px;float:left">
			<label for="blog">Blog & Introduction</label>
			<s:textarea id="blog" rows="25" cols="30" cssClass="text ui-widget-content ui-corner-all" />
		</div>
	</fieldset>
	</form>
</div>
</s:if>
