<%@ include file="../common/taglib.jsp" %>
<content tag="titleText"><fmt:message key="label.Archive.Report"/></content>
<div id="sites">
    <div class="form_grid">
        <div class="grid_row">
            <div class="grid_column">
                <label class="grid_level"><fmt:message key="label.Store"/>:</label>
                <select name="storeId" onchange="loadFloor(this,'/comm/archive/view.html');" required>
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
                <select name="floorId" required onchange="loadFloorDetails(this,'/comm/archive/view.html?storeId=${storeId}');">
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
            <div class="grid_column">
                <label class="grid_level"><fmt:message key="label.Version"/>:</label>
                <select name="version" required onchange="loadFloorDetailsByVersion(this, '/comm/archive/view.html?storeId=${storeId}&floorId=${floorId}');">
                    <option value=""><fmt:message key="label.Select.Version"/></option>
                    <c:forEach begin="1" end="${maxVersion}" step="1" var="vr">
                        <c:choose>
                            <c:when test="${not empty version && version eq vr}">
                                <option value="${vr}" selected="true">${vr}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${vr}">${vr}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
            </div>
        </div>
        <c:if test="${not empty floorDetails}">
            <div class="grid_row">
                <a href="<c:url value="/comm/floor/download.html?storeId=${storeId}&floorId=${floorId}&version=${version}&type=excel"/>"
                   class="button" title="<fmt:message key="label.Download.Excel"/>"><fmt:message
                        key="label.Download.Excel"/></a>
                <a href="<c:url value="/comm/floor/download.html?storeId=${storeId}&floorId=${floorId}&version=${version}&type=dxf"/>"
                   class="button" title="<fmt:message key="label.Download.Design"/>"><fmt:message
                        key="label.Download.Design"/></a>
                <a href="<c:url value="/comm/floor/download.html?storeId=${storeId}&floorId=${floorId}&version=${version}&type=pdf"/>"
                   class="button" title="<fmt:message key="label.Download.PDF"/>"><fmt:message
                        key="label.Download.PDF"/></a>
            </div>
        </c:if>
    </div>
</div>
<div id="floor_detail">
    <c:if test="${not empty storeId  && not empty floorId && not empty version}">
        <jsp:include page="floorView.jsp"/>
    </c:if>
</div>
