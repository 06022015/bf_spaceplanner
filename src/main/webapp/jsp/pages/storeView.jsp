<%@ include file="../common/taglib.jsp" %>
<script type="text/javascript">
    $(document).ready(function() {
        $('table').dataTable( {
        } );
    } );
</script>
<content tag="titleText"><fmt:message key="label.Stores"/></content>
<div>
    <table id="site_list" class="display" cellspacing="0" width="100%">
        <thead>
        <tr>
            <th><fmt:message key="label.Sl.No"/></th>
            <th><fmt:message key="label.Code"/></th>
            <th><fmt:message key="label.Name"/></th>
            <th><fmt:message key="label.No.Of.Floor"/></th>
            <%-- <th><fmt:message key="label.Total.Area"/></th>--%>
            <%--<th><fmt:message key="label.Chargeable.Area"/></th>--%>
            <th><fmt:message key="label.Carpet.Area"/></th>
            <th><fmt:message key="label.Retail.Area"/></th>
            <th><fmt:message key="label.Apparel.GMROF"/></th>
            <th><fmt:message key="label.Life.Style.GMROF"/></th>
            <th><fmt:message key="label.Store.GMROF"/></th>
            <th><fmt:message key="label.Projected.Store.Sale"/></th>
            <th><fmt:message key="label.Action"/></th>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${not empty stores}">
                <c:forEach items="${stores}" var="store" varStatus="cnt">
                    <tr>
                        <td>${cnt.count}</td>
                        <td><a href="<c:url value="/comm/store/form.html?storeId=${store.id}"/>">${store.code}</a></td>
                        <td class="action_property">${store.name}</td>
                        <td>${store.noOfFloor}</td>
                            <%--<td>${store.totalArea}</td>--%>
                        <%--<td>${store.chargeableArea}</td>--%>
                        <td>${store.carpetArea}</td>
                        <td>${store.retailArea}</td>
                        <td>${store.apparelGMROF}</td>
                        <td>${store.lifeStyleGMROF}</td>
                        <td>${store.storeGMROF}</td>
                        <td>${store.projectedStoreSale}</td>
                        <td>
                            <div class="table_element">
                                <security:authorize access="!hasRole('ROLE_ADMIN')">
                                    <a href="<c:url value="/comm/store/download.html?storeId=${store.id}"/>" class="icon xls" title="<fmt:message key="label.Download.Excel"/>">&nbsp;</a>
                                </security:authorize>
                                <security:authorize access="hasRole('ROLE_ADMIN')">
                                    <a href="<c:url value="/comm/store/download.html?storeId=${store.id}"/>" class="icon xls border_right" title="<fmt:message key="label.Download.Excel"/>">&nbsp;</a>
                                    <a href="<c:url value="/comm/store/archive.html?storeId=${store.id}"/>" class="icon archive" title="<fmt:message key="label.Archive"/>">&nbsp;</a>
                                    <%--<a href="<c:url value="/comm/store/delete.html?storeId=${store.id}"/>" class="icon delete" title="<fmt:message key="label.Delete"/>">&nbsp;</a>--%>
                                </security:authorize>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </c:when>
        </c:choose>
        </tbody>
    </table>
</div>

