<%@ include file="../common/taglib.jsp" %>
<content tag="titleText"><fmt:message key="label.Report"/></content>
<div id="report">
    <div class="form_grid">
        <div class="grid_row">
            <div class="grid_column">
                <label class="grid_level"><fmt:message key="label.Store"/>:</label>
                <select id="storeId" name="storeId">
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
                <select id="floorId" name="floorId">
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
        </div>
        <div class="grid_row">
            <div class="grid_column">
                <label class="grid_level"><fmt:message key="label.Brand"/>:</label>
                <select id="brandId" name="brandId" brandId="${brandId}"></select>
            </div>
            <c:if test="${not empty floorId}">
                <div class="grid_column">
                    <label class="grid_level"><fmt:message key="label.Status"/>:</label>
                    <label class="grid_level">${fn:replace(designStatus, '_',' ')}</label>
                </div>
            </c:if>
        </div>
        <c:if test="${not empty storeId  || not empty floorId || not empty brandId}">
            <div class="grid_row">
                <c:if test="${designStatus ne 'Master_Created' && designStatus ne 'Master_Published'}">
                    <a href="<c:url value="/comm/download/xls.html?storeId=${storeId}&floorId=${floorId}&brandId=${brandId}&type=excel"/>"
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
                            <c:when test="${designStatus eq 'Design_Published'}">
                                <a href="<c:url value="/comm/floor/publish.html?floorId=${floorId}&type=Design_Accepted&target=report"/>"
                                   class="button"
                                   title="<fmt:message key="label.Accept.Design"/>"><fmt:message
                                        key="label.Accept.Design"/></a>

                                <a href="<c:url value="/comm/floor/publish.html?floorId=${floorId}&type=Design_Rejected&target=report"/>"
                                   class="button"
                                   title="<fmt:message key="label.Reject.Design"/>"><fmt:message
                                        key="label.Reject.Design"/></a>
                            </c:when>
                        </c:choose>
                    </security:authorize>
                    <security:authorize access="!hasRole('ROLE_SPACE_PLANNER')">
                        <c:choose>
                            <c:when test="${designStatus eq 'Design_Uploaded'}">
                                <a href="<c:url value="/comm/floor/publish.html?floorId=${floorId}&type=Design_Published&target=report"/>"
                                   class="button"
                                   title="<fmt:message key="label.Publish.Design"/>"><fmt:message
                                        key="label.Publish.Design"/></a>
                            </c:when>
                        </c:choose>
                    </security:authorize>
                    <c:if test="${designStatus eq 'Design_Accepted'}">
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
    <c:if test="${not empty storeId  || not empty floorId || not empty brandId}">
        <jsp:include page="floorView.jsp"/>
    </c:if>
</div>
<script type="text/javascript">
    $(document).ready(function() {
        handleBrandFilter('report');
    });
</script>
<style type="text/css">
    .grid_column > span{
        float: right;
    }
</style>
