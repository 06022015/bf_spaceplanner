<%@ include file="../common/taglib.jsp" %>
<script type="text/javascript">
    $(document).ready(function() {
        $('#user_list').dataTable( {
            /*"order": [[ 1, "asc" ]]*/
        } );
    } );
</script>
<content tag="titleText"><fmt:message key="label.Users"/></content>
<div>
    <table id="user_list" class="display" cellspacing="0" width="100%">
        <thead>
        <tr>
            <th><fmt:message key="label.Sl.No"/></th>
            <th><fmt:message key="label.Name"/></th>
            <th><fmt:message key="label.Email"/></th>
            <th><fmt:message key="label.Mobile.Number"/></th>
            <th><fmt:message key="label.Status"/></th>
            <th><fmt:message key="label.Delete"/></th>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${null ne users && fn:length(users)>0}">
                <c:forEach items="${users}" var="user" varStatus="cnt">
                    <tr>
                        <td>${cnt.count}</td>
                        <td>
                            <a class="action_property" href="<c:url value="/user/form.html?id=${user.id}"/>">${user.firstName}&nbsp; ${user.lastName}</a>
                        </td>
                        <td>${user.username}</td>
                        <td>${user.mobile}</td>
                        <td style="text-align: center;">
                            <input type="checkbox" checked="${user.enabled}">
                        </td>
                        <td>
                            <a href="<c:url value="/user/delete.html?id=${user.id}"/>" class="icon delete" title="<fmt:message key="label.Delete"/>"></a>
                        </td>
                    </tr>
                </c:forEach>
            </c:when>
        </c:choose>
        </tbody>
    </table>
</div>
