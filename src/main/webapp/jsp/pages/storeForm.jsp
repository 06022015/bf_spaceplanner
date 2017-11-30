<%@ include file="../common/taglib.jsp" %>
<script type="text/javascript">
    $(document).ready(function () {
        $('.form_grid .grid_row table').dataTable({
            "paging":   false,
            "ordering": false,
            "info":     false,
            "searching": false,
            "sDom": '<"top"i>rt<"bottom"flp><"clear">'
        });


    });
</script>
<content tag="titleText"><fmt:message key="label.Store"/></content>
<div id="store">
    <form:form commandName="store" action="${pageContext.request.contextPath}/comm/store/save.html" method="post" id="store_form"
               onsubmit="disableSubmitButton();">
        <div class="form_grid">
            <div class="grid_row">
                <div class="grid_column">
                    <label class="grid_level"><fmt:message key="label.Store"/>:</label>
                    <form:select path="id" onchange="loadFloor(this,'/comm/store/form.html');">
                        <security:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_SPACE_PLANNER')">
                        <form:option value=""><fmt:message key="label.New.Store"/></form:option>
                        </security:authorize>
                        <c:forEach items="${stores}" var="str">
                            <form:option value="${str.id}">${str.code} &nbsp;${str.name}</form:option>
                        </c:forEach>
                    </form:select>
                        <%--<select name="site" id="site_list">
                            <option value="0">Select Site</option>
                            <option value="1">2200</option>
                            <option value="2">2300</option>
                            <option value="3">2400</option>
                        </select>--%>
                </div>
            </div>
            <div class="grid_row">
                <div class="grid_column">
                    <label class="grid_level"><fmt:message key="label.Store.Code" />:</label>
                    <c:choose>
                        <c:when test="${not empty store.id}">
                            <form:input required="required" path="code" type="text" id="code" maxlength="5" readonly="true"/>
                            <%--<form:hidden path="code" maxlength="5" />--%>
                        </c:when>
                        <c:otherwise>
                            <form:input required="required" path="code" type="text" id="code" maxlength="5" />
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="grid_column">
                    <label class="grid_level"><fmt:message key="label.Store"/> &nbsp;<fmt:message key="label.Name"/>:</label>
                    <form:input required="required" path="name" type="text" id="storeName" maxlength="60" onblur="validateStoreName();"/>
                </div>
                <div class="grid_column">

                    <label class="grid_level"><fmt:message key="label.No.Of.Floor"/>:</label>
                    <form:input required="required" path="noOfFloor" type="number" id="noOfFloor" maxlength="2" min="1" max="99"/>
                </div>
            </div>
            <div class="grid_row">

                <div class="grid_column">
                    <label class="grid_level"><fmt:message key="label.Address"/>:</label>
                    <form:input required="required" path="address.street" type="text" id="street" maxlength="80"/>
                </div>

                <div class="grid_column">

                    <label class="grid_level"><fmt:message key="label.City"/>:</label>
                    <form:input required="required" path="address.city" type="text" id="city" maxlength="25"/>
                </div>

                <div class="grid_column">

                    <label class="grid_level"><fmt:message key="label.State"/>:</label>
                    <form:input required="required" path="address.state" type="text" id="state" maxlength="25"/>
                </div>
            </div>
            <div class="grid_row">
                <table class="display" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                            <%--<th><fmt:message key="label.No.Of.Floor"/></th>--%>
                        <%--<th><fmt:message key="label.Total.Area"/></th>--%>
                        <%--<th><fmt:message key="label.Chargeable.Area" /></th>--%>
                        <th><fmt:message key="label.Carpet.Area"/></th>
                        <th><fmt:message key="label.Retail.Area"/></th>
                        <th><fmt:message key="label.Apparel.GMROF"/></th>
                        <th><fmt:message key="label.Life.Style.GMROF"/></th>
                        <th><fmt:message key="label.Store.GMROF"/></th>
                        <th><fmt:message key="label.Projected.Store.Sale"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <%--<td class="form_element">
                            <form:input required="required" path="totalArea" type="text" id="totalArea" maxlength="10"/>
                        </td>--%>
                       <%-- <td class="form_element">
                            <form:input required="required" path="chargeableArea" pattern="${patternDouble}" type="text" id="chargeableArea" maxlength="10"/>
                        </td>--%>
                        <td class="form_element">
                            <form:input required="required" path="carpetArea" pattern="${patternDouble}" type="text" id="carpetArea" maxlength="10"/>
                        </td>
                        <td class="form_element">
                            <form:input  path="retailArea" pattern="${patternDouble}" type="text" id="retailArea" maxlength="10"/> <%--required="required"--%>
                        </td>
                        <td class="form_element">
                            <form:input  path="apparelGMROF" type="text" id="apparelGMROF" maxlength="10"/>
                        </td>
                        <td class="form_element">
                            <form:input  path="lifeStyleGMROF" type="text" id="lifeStyleGMROF" maxlength="10"/>
                        </td>
                        <td class="form_element">
                            <form:input  path="storeGMROF" type="text" id="storeGMROF" maxlength="10"/>
                        </td>
                        <td class="form_element">
                            <form:input  path="projectedStoreSale" type="text" id="projectedStoreSale" maxlength="10"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="grid_row">
                <security:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_SPACE_PLANNER')">
                    <input type="submit" value="<fmt:message key="label.Save"/>" class="button"/>
                </security:authorize>
                <a href="<c:url value="/comm/store.html"/>" class="button" title="<fmt:message key="label.Cancel"/>"><fmt:message key="label.Cancel"/></a>
            </div>
        </div>
    </form:form>
</div>
<div id="site_floor">
    <c:if test="${not empty store && not empty store.id}">
        <jsp:include page="floorMaster.jsp"/>
    </c:if>
</div>


