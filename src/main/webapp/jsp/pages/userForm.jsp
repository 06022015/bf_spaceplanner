<%@ include file="../common/taglib.jsp" %>
<c:choose>
    <c:when test="${not empty user && not empty user.id}">
        <content tag="titleText"><fmt:message key="label.Edit.User"/></content>
    </c:when>
    <c:otherwise>
        <content tag="titleText"><fmt:message key="label.Add.User"/></content>
    </c:otherwise>
</c:choose>
<div class="two_column_form">
    <div class="form_grid">
        <form:form commandName="user" action="${pageContext.request.contextPath}/user/save.html" method="post" id="user_form"
                   onsubmit="disableSubmitButton();">
        <form:hidden path="id"/>
        <div class="grid_row">
            <div class="grid_column">
                <label class="grid_level">First Name:</label>
                <fmt:message key="label.First.Name" var="firstName"/>
                <form:input required="required" path="firstName" type="text" id="firstName" maxlength="50"
                            placeholder='${firstName}'/>
            </div>
            <div class="grid_column">
                <label class="grid_level">Last Name:</label>
                <fmt:message key="label.Last.Name" var="lastName"/>
                <form:input required="required" path="lastName" type="text" id="lastName" maxlength="50"
                            placeholder='${lastName}'/>
            </div>
        </div>
        <div class="grid_row">

            <div class="grid_column">
                <label class="grid_level">Email:</label>
                <fmt:message key="label.Email" var="email"/>
                <c:choose>
                    <c:when test="${not empty user && not empty user.id}">
                        <form:input required="required" path="email" type="email" id="email" maxlength="50"
                                    placeholder='${email}' readonly="true"/>
                    </c:when>
                    <c:otherwise>
                        <form:input required="required" path="email" type="email" id="email" maxlength="50"
                                    placeholder='${email}'/>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="grid_column">
                <label class="grid_level">Mobile:</label>
                <fmt:message key="label.Mobile.Number" var="mobile"/>
                <form:input required="required" path="mobile" type="tel" id="mobile" maxlength="50"
                            placeholder='${mobile}'/>
            </div>

        </div>
        <c:if test="${ empty user || empty user.id}">
            <div class="grid_row">
                <div class="grid_column">
                    <label class="grid_level">Password:</label>
                    <fmt:message key="label.Password" var="password"/>
                    <form:input required="required" path="password" type="password" id="password" maxlength="24"
                                placeholder='${password}' pattern="^(?=.*\d)(?=.*[A-Za-z])[A-Za-z0-9]{6,18}$" onchange="validatePassword();"/>
                </div>
                <div class="grid_column">
                    <label class="grid_level">Confirm Password:</label>
                    <fmt:message key="label.Confirm.Password" var="confirmPassword"/>
                    <form:input required="required" path="confirmPassword" type="password" id="confirmPassword" maxlength="24"
                                placeholder='${confirmPassword}' pattern="^(?=.*\d)(?=.*[A-Za-z])[A-Za-z0-9]{6,18}$" onkeyup="validatePassword();"/>
                </div>
            </div>
        </c:if>
        <div class="grid_row">
            <div class="grid_column">
                <label class="grid_level">Role:</label>
                <form:select path="roleName" required="required">
                    <form:option value="ROLE_ADMIN" label="Admin"/>
                    <form:option value="ROLE_SPACE_PLANNER" label="Space planner"/>
                    <form:option value="ROLE_DESIGNER" label="Designer"/>
                    <form:option value="ROLE_COMMERCIAL" label="Commercial"/>
                </form:select>
            </div>
        </div>
        <div class="grid_row">
            <input type="submit" value="Save" class="button"/>
            <a href="<c:url value="/user/show.html?id=1"/>" class="button" title="<fmt:message key="label.Cancel"/>"><fmt:message key="label.Cancel"/></a>
        </div>
    </div>
    </form:form>
</div>
