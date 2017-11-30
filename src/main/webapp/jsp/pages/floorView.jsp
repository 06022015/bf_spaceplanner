<%@ include file="../common/taglib.jsp" %>
<script type="text/javascript">
    $(document).ready(function () {
        $('table').dataTable({
            "order": [],
            "columnDefs": [ {
                "targets"  : 'no-sort',
                "orderable": false
            }],
            "iDisplayLength":50
        });
    });
</script>
<div>
    <table id="floor_view" class="display" cellspacing="0" width="100%">
        <thead>
            <tr>
                <th colspan="5"></th>
                <th colspan="2"><fmt:message key="label.Master.Data"/></th>
                <th colspan="1"><fmt:message key="label.Design.Data"/></th>
                <th colspan="9"></th>
            </tr>
        <tr>
            <th><fmt:message key="label.Division"/></th>
            <th><fmt:message key="label.Category"/></th>
            <th><fmt:message key="label.SIS.Details"/></th>
            <th title="<fmt:message key="label.Location"/>"><fmt:message key="label.Location.short"/></th>
            <th><fmt:message key="label.Area"/></th>
            <th><fmt:message key="label.Brand.Code"/></th>
            <th><fmt:message key="label.Brand.Name"/></th>
            <%--<th><fmt:message key="label.Brand.Code"/></th>--%>
            <th><fmt:message key="label.Brand.Name"/></th>
            <th><fmt:message key="label.MG"/></th>
            <th><fmt:message key="label.PSFPD"/></th>
            <th><fmt:message key="label.Sales"/></th>
            <th><fmt:message key="label.GMV"/></th>
            <th title="<fmt:message key="label.Agreement.Margin"/>"><fmt:message key="label.Agreement.Margin.short"/></th>
            <th title="<fmt:message key="label.Vistex.Margin"/>"><fmt:message key="label.Vistex.Margin.short"/></th>
            <th><fmt:message key="label.GMROF"/></th>
            <th><fmt:message key="label.Security.Deposit"/></th>
            <th><fmt:message key="label.SD.Amount"/></th>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${null ne floorDetails && fn:length(floorDetails)>0}">
                <c:forEach items="${floorDetails}" var="floorDetail" varStatus="cnt">
                    <tr  <c:if test="${floorDetail.requested eq true }"> class="requested"
                        title="<fmt:formatDate pattern="dd/MM/yyyy"
                                               value="${floorDetail.startDate}" />" </c:if>
                            >
                        <td>${floorDetail.categoryDivision.division}</td>
                        <td>${floorDetail.category}</td>
                        <td>${floorDetail.sisDetails}</td>
                        <td>${floorDetail.locationCode}</td>
                        <td>${floorDetail.designArea}</td>
                        <c:choose>
                            <c:when test="${floorDetail.valid eq false}">
                                <td class="mismatch">${floorDetail.brand.code}</td>
                                <td class="mismatch">${floorDetail.brand.name}</td>
                                <td class="mismatch">${floorDetail.designBrandName}</td>
                            </c:when>
                            <c:otherwise>
                                <td>${floorDetail.brand.code}</td>
                                <td>${floorDetail.brand.name}</td>
                                <td>${floorDetail.designBrandName}</td>
                            </c:otherwise>
                        </c:choose>
                        <td>${floorDetail.MG}</td>
                        <td>${floorDetail.PSFPD}</td>
                        <td>${floorDetail.sales}</td>
                        <td>${floorDetail.GMV}</td>
                        <td>${floorDetail.agreementMargin}</td>
                        <td>${floorDetail.vistexMargin}</td>
                        <td>${floorDetail.GMROF}</td>
                        <td>${floorDetail.securityDeposit}</td>
                        <td>${floorDetail.sdAmount}</td>
                    </tr>
                </c:forEach>
            </c:when>
        </c:choose>
        </tbody>
    </table>
</div>

