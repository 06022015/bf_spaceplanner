<%@ include file="../common/taglib.jsp" %>
<div>
    <nav>
        <ul>
            <security:authorize access="isAuthenticated()">
                <li><a href="<c:url value="/comm/home.html"/>"><fmt:message key="label.Dashboard"/></a></li>
                <li class="drop">
                    <a href="#"><fmt:message key="label.Store"/></a>
                    <div class="dropdownContain">
                        <div class="triangle"></div>
                        <ul class="dropOut">
                            <li><a href="<c:url value="/comm/store.html"/>"><fmt:message key="label.view"/> </a>
                            </li>
                            <security:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_SPACE_PLANNER')">
                                <li><a href="<c:url value="/comm/store/form.html"/>"><fmt:message
                                        key="label.Add.edit"/> </a></li>
                            </security:authorize>
                            <security:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_DESIGNER')">
                                <li><a href="<c:url value="/comm/floor/autocad/form.html"/>"><fmt:message
                                        key="label.Upload"/>&nbsp;<fmt:message key="label.Design"/></a></li>
                            </security:authorize>

                            <security:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_SPACE_PLANNER')">
                                <li><a href="<c:url value="/comm/floor/upload/form.html?isRequest=false"/>"><fmt:message
                                        key="label.Upload.Enrichment"/></a></li>
                            </security:authorize>

                        </ul>
                    </div>
                </li>
                <li><a href="<c:url value="/comm/floor/report.html"/>">Report</a></li>


                <security:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_SPACE_PLANNER')">
                    <li><a href="<c:url value="/comm/floor/upload/form.html?isRequest=true"/>">Request</a></li>
                </security:authorize>

                <security:authorize access="hasRole('ROLE_ADMIN')">
                    <li class="drop">
                        <a href="#"><fmt:message key="label.User"/> </a>

                        <div class="dropdownContain">
                            <div class="triangle"></div>
                            <ul class="dropOut">
                                <li><a href="<c:url value="/user/show.html"/>"><fmt:message key="label.view"/> </a>
                                </li>
                                <li><a href="<c:url value="/user/form.html"/>"><fmt:message
                                        key="label.Add.User"/> </a></li>
                            </ul>
                        </div>
                    </li>
                </security:authorize>
                <li class="drop">
                    <a href="<c:url value="/user/show.html"/>"><fmt:message key="label.Setting"/> </a>
                    <div class="dropdownContain">
                        <div class="triangle"></div>
                        <ul class="dropOut">
                            <li><a href="<c:url value="/user/password/change/form.html"/>"><fmt:message
                                    key="label.Change.Password"/> </a></li>
                            <li><a href="<c:url value="/j_spring_security_logout"/>"><fmt:message
                                    key="label.Logout"/></a></li>
                        </ul>
                    </div>
                </li>
                <li><a href="<c:url value="/comm/archive/view.html"/>">Archive</a></li>
            </security:authorize>
        </ul>
    </nav>
</div>

