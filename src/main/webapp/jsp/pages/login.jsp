<%@ include file="../common/taglib.jsp" %>
<style type="text/css">
    #message{
        text-align: center;
    }
</style>
<content tag="pageTitle"><fmt:message key="label.Login"/></content>
<div class="center_form">
    <div class="form_grid">
        <form action="<c:url value='/j_spring_security_check' />" method="post">
            <div class="grid_row">
                <div class="grid_column">
                    <h2><fmt:message key="label.Login"/> </h2>
                </div>

            </div>
            <div class="grid_row">
                <div class="grid_column">
                    <input type="email" name="j_username" placeholder="Username" required/>
                </div>
            </div>
            <div class="grid_row">
                <div class="grid_column">
                    <input type="password" name="j_password" placeholder="Password" required/>
                </div>
            </div>
            <div class="grid_row">
                <div class="grid_column">
                    <a class="forgot_password" href="<c:url value='/forgot/password/form.html'/>"><fmt:message key="label.Forgot.Password"/></a>
                </div>
            </div>
            <div class="grid_row" >
                <input type="submit" class="button" value="<fmt:message key="label.Login"/>" title="<fmt:message key="label.Login"/>">
            </div>
        </form>
    </div>
</div>
