<%@ include file="../common/taglib.jsp" %>
<content tag="titleText"><fmt:message key="label.Change.Password"/></content>
<div class="two_column_form">
    <div class="form_grid">
        <form:form commandName="changePassword" action="${pageContext.request.contextPath}/user/password/change/save.html" method="post" id="change_password_form"
                   onsubmit="disableSubmitButton();">
        <%--<form:hidden path="id"/>--%>
        <div class="grid_row">
            <div class="grid_column">
                <label class="grid_level"><fmt:message key="label.Old.Password"/>:</label>
                <fmt:message key="label.Old.Password" var="oldPassword"/>
                <form:input required="required" path="oldPassword" type="password" id="oldPassword" maxlength="24"
                            placeholder='${oldPassword}' pattern="^(?=.*\d)(?=.*[A-Za-z])[A-Za-z0-9]{6,18}$"/>
            </div>
        </div>
        <div class="grid_row">
            <div class="grid_column">
                <fmt:message key="label.New.Password" var="newPassword"/>
                <label class="grid_level">${newPassword}:</label>
                <form:input required="required" path="newPassword" type="password" id="password" maxlength="24"
                            placeholder='${newPassword}' pattern="^(?=.*\d)(?=.*[A-Za-z])[A-Za-z0-9]{6,18}$" onchange="validatePassword();"/>
            </div>
            <div class="grid_column">
                <fmt:message key="label.Confirm.Password" var="confirmPassword"/>
                <label class="grid_level">${confirmPassword}:</label>

                <form:input required="required" path="confirmPassword" type="password" id="confirmPassword" maxlength="24"
                            placeholder='${confirmPassword}' pattern="^(?=.*\d)(?=.*[A-Za-z])[A-Za-z0-9]{6,18}$" onchange="validatePassword();"/>
            </div>
        </div>
      <div class="grid_row">
            <input type="submit" value="Save" class="button"/>
            <a href="<c:url value="/comm/home.html"/>" class="button" title="<fmt:message key="label.Cancel"/>"><fmt:message key="label.Cancel"/></a>
        </div>
    </div>
    </form:form>
</div>