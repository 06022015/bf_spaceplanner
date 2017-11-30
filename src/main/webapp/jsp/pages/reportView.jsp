<%@ include file="../common/taglib.jsp" %>
<content tag="titleText"><fmt:message key="label.Report"/></content>
<div id="report">
    <div class="form_grid">
        <div class="grid_row">
            <div class="grid_column">
                <label class="grid_level"><fmt:message key="label.Store"/>:</label>
                <select name="storeId" onchange="loadFloor(this,'/comm/floor/report.html');" required>
                    <option value=""><fmt:message key="label.Select.Store"/></option>
                    <c:forEach items="${stores}" var="str">
                        <c:choose>
                            <c:when test="${not empty storeId && storeId eq str.id}">
                                <option value="${str.id}" selected="true">${str.code} &nbsp;${str.name}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${str.id}">${str.code} &nbsp;${str.name}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
            </div>
            <div class="grid_column">
                <label class="grid_level"><fmt:message key="label.Floor"/>:</label>
                <select name="floorId" required onchange="loadFloorDetails(this,'/comm/floor/report.html?storeId=${storeId}');">
                    <option value=""><fmt:message key="label.Select.Floor"/></option>
                    <c:forEach items="${floors}" var="flr">
                        <c:choose>
                            <c:when test="${not empty floorId && floorId eq flr.id}">
                                <option value="${flr.id}" selected="true">${flr.floorNumber}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${flr.id}">${flr.floorNumber}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
            </div>
            <c:if test="${not empty floorId}">
                <div class="grid_column">
                    <label class="grid_level"><fmt:message key="label.Status"/>:</label>
                    <label class="grid_level">${fn:replace(designStatus, '_',' ')}</label>
                </div>
            </c:if>
        </div>
        <c:if test="${not empty storeId  && not empty floorId}">
            <div class="grid_row">
            <c:if test="${designStatus ne 'Master_Created' && designStatus ne 'Master_Published'}">
                <a href="<c:url value="/comm/floor/download.html?storeId=${storeId}&floorId=${floorId}&type=excel"/>"
                   class="button" title="<fmt:message key="label.Download.Excel"/>"><fmt:message key="label.Download.Excel"/></a>
            </c:if>
                <security:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_SPACE_PLANNER','ROLE_DESIGNER')">
                    <security:authorize access="!hasRole('ROLE_DESIGNER')">
                        <c:choose>
                            <c:when test="${designStatus eq 'Master_Created'}">
                                <a href="<c:url value="/comm/floor/publish.html?floorId=${floorId}&type=Master_Published&target=report"/>"
                                   class="button"
                                   title="<fmt:message key="label.Publish.Master"/>"><fmt:message
                                        key="label.Publish.Master"/></a>
                            </c:when>
                            <c:when test="${designStatus eq 'Brand_Master_Uploaded'}">
                                <a href="<c:url value="/comm/floor/publish.html?floorId=${floorId}&type=Brand_Master_Published&target=report"/>"
                                   class="button"
                                   title="<fmt:message key="label.Publish.Brand.Master"/>"><fmt:message
                                        key="label.Publish.Brand.Master"/></a>
                            </c:when>
                        </c:choose>
                    </security:authorize>
                    <security:authorize access="!hasRole('ROLE_SPACE_PLANNER')">
                        <c:choose>
                            <c:when test="${designStatus eq 'Space_Design_Uploaded'}">
                                <a href="<c:url value="/comm/floor/publish.html?floorId=${floorId}&type=Space_Design_Published&target=report"/>"
                                   class="button"
                                   title="<fmt:message key="label.Publish.Space.Design"/>"><fmt:message
                                        key="label.Publish.Space.Design"/></a>
                                <%--<a href="<c:url value="/comm/floor/publish.html?floorId=${floorId}&type=Space_Design_Rejected"/>"
                              class="button"
                              title="<fmt:message key="label.Reject.Space.Design"/>"><fmt:message key="label.Reject.Space.Design"/></a>--%>
                            </c:when>
                            <c:when test="${designStatus eq 'Brand_Design_Uploaded'}">
                                <a href="<c:url value="/comm/floor/publish.html?floorId=${floorId}&type=Brand_Design_published&target=report"/>"
                                   class="button"
                                   title="<fmt:message key="label.Publish.Brand.Design"/>"><fmt:message
                                        key="label.Publish.Brand.Design"/></a>
                                <%--<a href="<c:url value="/comm/floor/publish.html?floorId=${floorId}&type=Brand_Design_Rejected"/>"
                              class="button"
                              title="<fmt:message key="label.Reject.Brand.Design"/>"><fmt:message key="label.Reject.Brand.Design"/></a>--%>
                            </c:when>
                        </c:choose>
                    </security:authorize>
                    <c:if test="${designStatus eq 'Brand_Design_published'}">
                        <security:authorize access="hasRole('ROLE_ADMIN')">
                            <a href="<c:url value="/comm/floor/publish.html?floorId=${floorId}&type=Published&target=report"/>"
                               class="button" title="<fmt:message key="label.Publish.To.SAP"/>"><fmt:message
                                    key="label.Publish.To.SAP"/></a>
                        </security:authorize>
                    </c:if>
                </security:authorize>
                    <%--<c:if test="${designStatus eq 'Enrichment_Uploaded'}">
                        <security:authorize access="hasRole('ROLE_ADMIN')">
                            <a href="<c:url value="/comm/floor/publishToSAP.html?storeId=${storeId}&floorId=${floorId}"/>" class="button" title="<fmt:message key="label.Publish.To.SAP"/>"><fmt:message key="label.Publish.To.SAP"/></a>
                        </security:authorize>
                    </c:if>--%>
            </div>
        </c:if>
    </div>
</div>
<div id="floor_detail">
    <c:if test="${not empty storeId  && not empty floorId}">
        <jsp:include page="floorView.jsp"/>
    </c:if>
</div>
