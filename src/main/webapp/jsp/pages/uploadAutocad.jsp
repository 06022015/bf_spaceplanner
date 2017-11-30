<%@ include file="../common/taglib.jsp" %>
<%--<style type="text/css">
    .grid_column{
        width: 475px;;
    }
</style>--%>
<content tag="titleText"><fmt:message key="label.Upload.AutoCad.Design"/></content>
<div id="upload_design" class="two_column_form">
    <form:form commandName="design" action="${pageContext.request.contextPath}/comm/floor/autocad/save.html" method="post" onsubmit="disableSubmitButton(this);"
               id="upload_design_form" enctype="multipart/form-data">
        <div class="form_grid">
            <div class="grid_row">
                <div class="grid_column">
                    <label class="grid_level"><fmt:message key="label.Store"/>:</label>
                    <form:select path="storeId" onchange="loadFloor(this,'/comm/floor/autocad/form.html');" required="required">
                        <form:option value=""><fmt:message key="label.Select.Store"/></form:option>
                        <c:forEach items="${stores}" var="str">
                            <c:choose>
                                <c:when test="${not empty storeId && storeId eq str.id}">
                                    <form:option value="${str.id}" selected="true">${str.code} &nbsp;${str.name}</form:option>
                                </c:when>
                                <c:otherwise>
                                    <form:option value="${str.id}">${str.code} &nbsp;${str.name}</form:option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </form:select>
                </div>
                <div class="grid_column">
                    <label class="grid_level"><fmt:message key="label.Floor"/>:</label>
                    <form:select path="floorId" required="required">
                        <form:option value=""><fmt:message key="label.Select.Floor"/></form:option>
                        <c:forEach items="${floors}" var="flr">
                            <c:choose>
                                <c:when test="${not empty floorId && floorId eq flr.id}">
                                    <form:option value="${flr.id}" selected="true">${flr.floorNumber}</form:option>
                                </c:when>
                                <c:otherwise>
                                    <form:option value="${flr.id}">${flr.floorNumber}</form:option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </form:select>
                </div>
            </div>
            <div class="grid_row">
                <div class="grid_column">
                    <label class="grid_level"><fmt:message key="label.Design.Autocad"/>:</label>
                    <div class="type_file">
                        <input class="file_name" id="designAutocadFile" placeholder="<fmt:message key="label.Choose.Autocad.File"/>" type="text" readonly="true"/>
                        <div class="brows_btn_block button">
                            <span><fmt:message key="label.Browse"/> </span>
                            <form:input path="designDXFFile" type="file" accept=".dxf"/>
                                <%--<input type="file" required accept="application/dxf, application/pdf,application/msexcel"/>--%>
                        </div>
                    </div>
                </div>
                <div class="grid_column">
                    <label class="grid_level"><fmt:message key="label.Design.PDF"/>:</label>
                    <div class="type_file">
                        <input class="file_name" id="designPDFFile"  placeholder="<fmt:message key="label.Choose.PDF.File"/>" type="text" readonly="true"/>
                        <div class="brows_btn_block button">
                            <span><fmt:message key="label.Browse"/></span>
                            <form:input path="designPDFFile" type="file" accept=".pdf"/>
                        </div>
                    </div>
                </div>
            </div>
            <div class="grid_row">
                <input type="submit" value="Save" class="button"/>
                <a href="<c:url value="/comm/store.html"/>" class="button" title="<fmt:message key="label.Cancel"/>"><fmt:message key="label.Cancel"/></a>
            </div>
        </div>
    </form:form>
</div>
<div id="site_floor">
    <c:if test="${not empty storeId}">
        <jsp:include page="floorMaster.jsp"/>
    </c:if>
</div>
<%--
<label class="grid_level">Design PDF:</label>
<div class="type_file">
    <div style="float: right; width: 70%;">
        <input type="text" readonly="true" placeholder="Choose Autocad File" id="designAutocadFile" class="file_name">
        <span style="font-size: 9px;">Select .dxf</span>
    </div>
    <div class="brows_btn_block button">
        <span>Browse.. </span>
        <input type="file" value="" accept=".dxf" name="designDXFFile" id="designDXFFile">
    </div>
</div>--%>
