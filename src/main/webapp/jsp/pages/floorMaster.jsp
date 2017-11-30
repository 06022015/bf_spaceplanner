<%@ include file="../common/taglib.jsp" %>
<c:choose>
    <c:when test="${not empty storeId}">
        <script type="text/javascript">
            $(document).ready(function () {
                $('#floor_list').dataTable({
                    "paging":false,
                    "ordering":false,
                    "info":false,
                    "searching":false
                });
            });
        </script>
        <div class="header">
            <div class="title_text">
                <h1><fmt:message key="label.Floors"/></h1>
            </div>
            <security:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_SPACE_PLANNER')">
                <c:if test="${not empty store && fn:length(floors)< store.noOfFloor}">
                    <div class="tool_icon">
                        <a class="icon add" id="add_floor" title="<fmt:message key="label.Add.Floor"/>"></a>
                    </div>
                </c:if>
            </security:authorize>
        </div>
    </c:when>
    <c:otherwise>
        <script type="text/javascript">
            $(document).ready(function () {
                $('#floor_list').dataTable({
                });
            });
        </script>
        <content tag="titleText"><fmt:message key="label.Dashboard"/></content>
    </c:otherwise>
</c:choose>
<div id="floors">
    <table id="floor_list" class="display" cellspacing="0" width="100%">
        <thead>
        <tr>
            <c:if test="${ empty storeId}">
                <th><fmt:message key="label.Store.Code"/></th>
            </c:if>
            <th><fmt:message key="label.Floor.Number"/></th>
            <%--<th><fmt:message key="label.Chargeable.Area"/></th>--%>
            <th><fmt:message key="label.Carpet.Area"/></th>
            <th><fmt:message key="label.Retail.Area"/></th>
            <th><fmt:message key="label.Number.Of.Location"/></th>
            <th><fmt:message key="label.AutoCad.Area"/></th>
            <th><fmt:message key="label.Difference"/></th>
            <th><fmt:message key="label.Status"/></th>
            <c:if test="${not empty showAction && showAction eq true}">
                <th><fmt:message key="label.Action"/></th>
            </c:if>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${not empty floors}">
                <c:forEach items="${floors}" var="floor" varStatus="cnt">
                    <tr>
                        <c:if test="${empty storeId}">
                            <td>
                                <a href="<c:url value="/comm/store/form.html?storeId=${floor.store.id}&floorId=${floor.id}"/>">${floor.store.code}</a>
                            </td>
                        </c:if>
                        <td class="editable link" name="floorNumber" required="true" form="floor_form"
                            href="<c:url value="/comm/floor/report.html?storeId=${floor.store.id}&floorId=${floor.id}"/>" value="${floor.floorNumber}">
                            <a href="<c:url value="/comm/floor/report.html?storeId=${floor.store.id}&floorId=${floor.id}"/>" class="action_property">${floor.floorNumber}</a>
                        </td>
                        <%--<td class="editable" name="chargeableArea" required="true" form="floor_form" value="${floor.chargeableArea}">${floor.chargeableArea}</td>--%>
                        <td class="editable" name="carpetArea" required="true" form="floor_form" value="${floor.carpetArea}">${floor.carpetArea}</td>
                        <td class="editable" name="retailArea" form="floor_form" value="${floor.retailArea}">${floor.retailArea}</td>
                        <td>${floor.noOfLocation}</td>
                        <td>${floor.designArea}</td>
                        <c:choose>
                            <c:when test="${floor.designStatus eq 'Space_Design_Uploaded' &&  (floor.retailArea - floor.designArea) ne 0}">
                                <td class="mismatch">${floor.retailArea - floor.designArea}</td>
                            </c:when>
                            <c:otherwise>
                                <td>${floor.retailArea - floor.designArea}</td>
                            </c:otherwise>
                        </c:choose>
                        <td>${fn:replace(floor.designStatus, '_',' ')}</td>
                        <c:if test="${not empty showAction && showAction eq true}">
                            <td class="hidden" name="id" value="${floor.id}" form="floor_form">
                                <div class="table_element">
                                    <security:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_SPACE_PLANNER')">
                                        <c:if test="${pageContext.request.isUserInRole('ROLE_ADMIN') || floor.designStatus eq 'Master_Created'}">
                                            <a class="icon edit border_right action"
                                               title="<fmt:message key="label.Edit"/>"></a>
                                        </c:if>
                                        <c:choose>
                                            <c:when test="${floor.designStatus eq 'Master_Created'}">
                                                <a href="<c:url value="/comm/floor/publish.html?floorId=${floor.id}&type=Master_Published"/>"
                                                   class="icon publish border_right"
                                                   title="<fmt:message key="label.Publish.Master"/>"></a>
                                            </c:when>
                                            <c:when test="${floor.designStatus eq 'Space_Design_Uploaded'}">
                                                <a href="<c:url value="/comm/floor/publish.html?floorId=${floor.id}&type=Space_Design_Published"/>"
                                                   class="icon publish border_right"
                                                   title="<fmt:message key="label.Publish.Space.Design"/>"></a>
                                                <%-- <a href="<c:url value="/comm/floor/publish.html?floorId=${floor.id}&type=Space_Design_Rejected"/>"
                                               class="icon publish border_right"
                                               title="<fmt:message key="label.Reject.Space.Design"/>"></a>--%>
                                            </c:when>
                                            <c:when test="${floor.designStatus eq 'Brand_Master_Uploaded'}">
                                                <a href="<c:url value="/comm/floor/publish.html?floorId=${floor.id}&type=Brand_Master_Published"/>"
                                                   class="icon publish border_right"
                                                   title="<fmt:message key="label.Publish.Brand.Master"/>"></a>
                                            </c:when>
                                            <c:when test="${floor.designStatus eq 'Brand_Design_Uploaded'}">
                                                <a href="<c:url value="/comm/floor/publish.html?floorId=${floor.id}&type=Brand_Design_published"/>"
                                                   class="icon publish border_right"
                                                   title="<fmt:message key="label.Publish.Brand.Design"/>"></a>
                                                <%--<a href="<c:url value="/comm/floor/publish.html?floorId=${floor.id}&type=Brand_Design_Rejected"/>"
                                                   class="icon publish border_right"
                                                   title="<fmt:message key="label.Reject.Brand.Design"/>"></a>--%>
                                            </c:when>
                                            <c:when test="${floor.designStatus eq 'Brand_Design_published'}">
                                                <security:authorize access="hasRole('ROLE_ADMIN')">
                                                    <a href="<c:url value="/comm/floor/publish.html?floorId=${floor.id}&type=Published"/>"
                                                       class="icon publish border_right"
                                                       title="<fmt:message key="label.Publish.To.SAP"/>"></a>
                                                </security:authorize>
                                            </c:when>
                                        </c:choose>
                                    </security:authorize>
                                    <c:if test="${floor.designStatus ne 'Master_Created' && floor.designStatus ne 'Master_Published'}">
                                        <a href="<c:url value="/comm/floor/download.html?storeId=${floor.store.id}&floorId=${floor.id}&type=excel"/>"
                                           class="icon xls border_right"
                                           title="<fmt:message key="label.Download.Excel"/>"></a>
                                        <c:if test="${not empty floor.autoCADFileName}">
                                            <a href="<c:url value="/comm/floor/download.html?storeId=${floor.store.id}&floorId=${floor.id}&type=dxf"/>"
                                               class="icon dxf border_right"
                                               title="<fmt:message key="label.Download.Design"/>"></a>
                                        </c:if>
                                        <c:if test="${not empty floor.pdfFileName}">
                                            <a href="<c:url value="/comm/floor/download.html?storeId=${floor.store.id}&floorId=${floor.id}&type=pdf"/>"
                                               class="icon pdf  border_right"
                                               title="<fmt:message key="label.Download.PDF"/>"></a>
                                        </c:if>
                                    </c:if>
                                    <security:authorize access="hasRole('ROLE_ADMIN')">
                                        <%--<a href="<c:url value="/comm/floor/archive.html?storeId=${floor.store.id}&floorId=${floor.id}"/>"
                                           class="icon archive" title="<fmt:message key="label.Archive"/>">&nbsp;</a>--%>
                                        <a href="<c:url value="/comm/floor/delete.html?storeId=${floor.store.id}&floorId=${floor.id}"/>"
                                           class="icon delete" title="<fmt:message key="label.Delete"/>"></a>
                                    </security:authorize>
                                </div>
                                <div class="form_element">
                                    <a class="icon save border_right" title="<fmt:message key="label.Save"/>"></a>
                                    <a class="icon cancel" title="<fmt:message key="label.Cancel"/>"></a>
                                </div>
                            </td>
                        </c:if>
                    </tr>
                </c:forEach>
            </c:when>
        </c:choose>
        </tbody>
    </table>
</div>
<c:if test="${not empty store}">
    <div id="floor_block" style="display: none;">
        <table id="new_floor_table">
            <tr class="new_floor" noOfFloor="${store.noOfFloor}">
                <td class="editable" name="floorNumber" required="true" form="floor_form"></td>
                <%--<td class="editable" name="chargeableArea" required="true" form="floor_form"></td>--%>
                <td class="editable" name="carpetArea" required="true" form="floor_form"></td>
                <td class="editable" name="retailArea" form="floor_form"></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td>
                    <div class="form_element">
                        <a class="icon save border_right" title="<fmt:message key="label.Save"/>"></a>
                        <a class="icon cancel" title="<fmt:message key="label.Cancel"/>"></a>
                    </div>
                </td>
            </tr>
        </table>
        <form action="${pageContext.request.contextPath}/comm/floor/save.html" method="post" id="floor_form" onsubmit="disableSubmitButton();">
            <input type="hidden" name="store.id" value="${storeId}">
            <input type="submit">
        </form>
    </div>
</c:if>
