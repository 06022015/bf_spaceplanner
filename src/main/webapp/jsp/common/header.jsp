<div class="header_content">
    <div style="width: 100%;float: left;">
        <div style="height: 70px;width: 80%;float: left;">
            <div style="width: 100%;">
                <div id="logedin_detail" style="float: right;">
                    <security:authorize access="isAuthenticated()">
                        <div class="login_detail" style="display: block;font-size: 12px;padding: 5px;color: #FFFFFF">Welcome: ${sessionScope.fullName}</div>
                    </security:authorize>
                </div>
                <div id="custom_logo">
                    <%--<marquee scrollamount="2" behavior="alternate">Space Data Base Solution</marquee>--%>
                    <a href="<c:url value="/comm/home.html"/>">Space Data Base Solution</a>

                </div>
            </div>
            <div id="nav">
                <%@ include file="menu.jsp" %>
            </div>
        </div>
        <div id="logo">&nbsp;</div>
    </div>
</div>

