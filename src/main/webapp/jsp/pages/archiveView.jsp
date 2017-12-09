<%@ include file="../common/taglib.jsp" %>
<content tag="titleText"><fmt:message key="label.Archive.Report"/></content>
<div id="sites">
    <div class="form_grid">
        <div class="grid_row">
            <div class="grid_column">
                <label class="grid_level"><fmt:message key="label.Store"/>:</label>
                <select id="storeId" name="storeId" status="archive">
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
                <select id="floorId" name="floorId" status="archive">
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
                <label class="grid_level"><fmt:message key="label.Brand"/>:</label>
                <select id="brandId" name="brandId" brandId="${brandId}" status="archive"></select>
            </div>
            <div class="grid_column">
                <label class="grid_level"><fmt:message key="label.Version"/>:</label>
                <select id="version" name="version" status="archive">
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
                <a href="<c:url value="/comm/download/xls.html?storeId=${storeId}&floorId=${floorId}&brandId=${brandId}&version=${version}&archive=true"/>"
                   class="button" title="<fmt:message key="label.Download.Excel"/>"><fmt:message
                        key="label.Download.Excel"/></a>
                <c:if test="${empty brandId}">
                    <a href="<c:url value="/comm/floor/download.html?storeId=${storeId}&floorId=${floorId}&version=${version}&type=dxf"/>"
                       class="button" title="<fmt:message key="label.Download.Design"/>"><fmt:message
                            key="label.Download.Design"/></a>
                    <a href="<c:url value="/comm/floor/download.html?storeId=${storeId}&floorId=${floorId}&version=${version}&type=pdf"/>"
                       class="button" title="<fmt:message key="label.Download.PDF"/>"><fmt:message
                            key="label.Download.PDF"/></a>
                </c:if>
            </div>
        </c:if>
    </div>
</div>
<div id="floor_detail">
    <c:if test="${(not empty storeId  && not empty floorId && not empty version) || not empty brandId}">
        <jsp:include page="floorView.jsp"/>
    </c:if>
</div>
<script type="text/javascript">
    $(document).ready(function() {
        handleBrandFilter('archive');
    });
</script>
<style type="text/css">
    .grid_column > span{
        float: right;
    }
</style>
