<%@ include file="../common/taglib.jsp" %>
<script type="text/javascript">
    $(document).ready(function(){
        $("#confirmation_dialogue").dialog({
            autoOpen: false
        });
        $(".confirmation").click(function(){
            $("#confirmation_dialogue").dialog("open");
        });
    });
</script>
<c:choose>
    <c:when test="${not empty isRequest && isRequest}">
        <content tag="titleText"><fmt:message key="label.Change.Request"/></content>
    </c:when>
    <c:otherwise>
        <content tag="titleText"><fmt:message key="label.Upload.Enrichment"/></content>
    </c:otherwise>
</c:choose>
<div id="request_form" class="two_column_form">
    <form:form commandName="uploadForm" action="${pageContext.request.contextPath}/comm/floor/upload/save.html" method="post"
               onsubmit="disableSubmitButton();" enctype="multipart/form-data">
        <div class="form_grid">
            <div class="grid_row">
                <div class="grid_column">
                    <label class="grid_level"><fmt:message key="label.Store"/>:</label>
                    <form:select path="storeId" onchange="loadFloor(this,'/comm/floor/upload/form.html?isRequest=${isRequest}')" required="required">
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
                    <c:choose>
                        <c:when test="${not empty isRequest && isRequest}">
                            <label class="grid_level"><fmt:message key="label.Change.Request"/>:</label>
                        </c:when>
                        <c:otherwise>
                            <label class="grid_level"><fmt:message key="label.Enrichment"/>:</label>
                        </c:otherwise>
                    </c:choose>
                    <div class="type_file">
                        <input class="file_name validateExcel" id="file_name" required placeholder="<fmt:message key="label.Choose.File"/>"
                               type="text"/>
                        <div class="brows_btn_block button">
                            <span><fmt:message key="label.Browse"/> </span>
                            <form:input path="file" type="file" required="required" accept=".xls,.xlsx"/>
                        </div>
                    </div>
                </div>

            </div>
            <div class="grid_row">
                <c:choose>
                    <c:when test="${not empty isRequest && isRequest}">
                        <div class="grid_column">
                            <label class="grid_level"><fmt:message key="label.Request.Type"/>:</label>
                            <div class="radio_button_group">
                                <div class="radio_button_block">
                                    <form:radiobutton path="changeRequestType" value="CICO" required="required"/>CICO
                                </div>
                                <div class="radio_button_block">
                                    <form:radiobutton path="changeRequestType" value="BIBO" required="required"/>BIBO
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <form:hidden path="changeRequestType" value="NEW"/>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="grid_row">
                <input type="submit" value="Save" class="button"/>
                <a href="<c:url value="/comm/store.html"/>" class="button" title="<fmt:message key="label.Cancel"/>"><fmt:message key="label.Cancel"/></a>
            </div>
        </div>
    </form:form>
</div>
<c:if test="${empty isRequest || !isRequest}">
    <div id="site_floor">
        <c:if test="${not empty storeId}">
            <jsp:include page="floorMaster.jsp"/>
        </c:if>
    </div>
</c:if>
