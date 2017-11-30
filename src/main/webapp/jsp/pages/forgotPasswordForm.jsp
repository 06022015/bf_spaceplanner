<%@ include file="../common/taglib.jsp" %>
<style type="text/css">
    #message{
        text-align: center;
    }
</style>
<content tag="pageTitle"><fmt:message key="label.Reset.Password"/></content>
<div class="center_form">
    <div class="form_grid">
        <form action="<c:url value='/forgot/password.html' />" method="post">
            <div class="grid_row">
                <div class="grid_column">
                    <h2><fmt:message key="label.Reset.Password"/> </h2>
                </div>
            </div>
            <div class="grid_row">
                <div class="grid_column">
                    <input type="email" name="email" placeholder="Email" required/>
                </div>
            </div>
            <div class="grid_row">
                <input type="submit" class="button" value="<fmt:message key="label.Send"/>" title="<fmt:message key="label.Send"/>">
                <a href="<c:url value="/login.html"/>" class="button" title="<fmt:message key="label.Cancel"/>"><fmt:message key="label.Cancel"/></a>
            </div>
        </form>
    </div>
</div>
