<%@ include file="../common/taglib.jsp" %>
<content tag="titleText"><fmt:message key="label.Change.Request"/></content>
<div id="request_form" class="two_column_form">
    <form  action="${pageContext.request.contextPath}/comm/request/save.html" method="post" onsubmit="disableSubmitButton();" enctype="multipart/form-data">
        <div class="form_grid">
            <div class="grid_row">
                <div class="grid_column">
                    <label class="grid_level"><fmt:message key="label.Store"/>:</label>
                    <select name="storeId" required>
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
                    <select name="storeId" required>
                        <option value=""><fmt:message key="label.Select.Floor"/></option>
                        <c:forEach items="${floors}" var="floor">
                            <c:choose>
                                <c:when test="${not empty floorId && floorId eq floor.id}">
                                    <option value="${floor.id}" selected="true">${floor.floorNumber}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${floor.id}">${floor.floorNumber} &nbsp;${floor.floorNumber}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </div>

            </div>
            <div class="grid_row">
                <div class="grid_column">
                    <label class="grid_level"><fmt:message key="label.Change.Request"/>:</label>
                    <div class="type_file">
                        <input class="file_name validateExcel" id="file_name" required="required" placeholder="<fmt:message key="label.Choose.File"/>"
                               type="text" <%--readonly="true"--%>/>
                        <div class="brows_btn_block button">
                            <span><fmt:message key="label.Browse"/> </span>
                            <input name="requestFile" type="file" id="file" required="required"  <%--accept=".xls,.xlsx"--%>/>
                        </div>
                    </div>
                </div>
                <div class="grid_column">
                    <label class="grid_level"><fmt:message key="label.Request.Type"/>:</label>
                    <div class="radio_button_group">
                        <%--<div class="radio_button_block">
                            <input type="radio" name="mobile" value="0" required/>New
                        </div>--%>
                        <div class="radio_button_block">
                            <input type="radio" name="requestType" value="CICO" required/>CICO
                        </div>
                        <div class="radio_button_block">
                            <input type="radio" name="requestType" value="BIBO" required/>BIBO
                        </div>
                    </div>
                </div>
            </div>
            <div class="grid_row">
                <input type="submit" value="Save" class="button"/>
                <a href="<c:url value="/comm/store.html"/>" class="button"
                   title="<fmt:message key="label.Cancel"/>"><fmt:message key="label.Cancel"/></a>
            </div>
        </div>
    </form>
</div>