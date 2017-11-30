<%@ page import="com.spaceplanner.java.util.Constants" %>
<%@ include file="../common/taglib.jsp" %>
<div style="font-weight: bold;padding: 7px 0;">
    <html:messages>
        <c:choose>
            <%-- Error Messages --%>
            <c:when test="${status ne null && status.code ne '200'}">
                <div id="error"><c:out value="${status.message}" escapeXml="false"/></div>
                <ul id="error" style="text-align: left;">
                    <c:forEach items="${status.errors}" var="error">
                        <li>${error}</li>
                    </c:forEach>
                </ul>
            </c:when>
            <%-- Success Messages --%>
            <c:when test="${status ne null && status.code eq '200'}">
                <div id="success"><c:out value="${status.message}" escapeXml="false"/></div>
            </c:when>
        </c:choose>
    </html:messages>
</div>

<% if (session.getAttribute(Constants.RESPONSE_STATUS) != null) {
    session.removeAttribute(Constants.RESPONSE_STATUS);
}
%>
