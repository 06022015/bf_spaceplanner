<!DOCTYPE html>
<%@ include file="../common/taglib.jsp" %>
<head>
    <%@ include file="../common/meta.jsp" %>
    <link href="<c:url value="/style/menu.css"/>" rel='stylesheet' type='text/css'/>
    <link href="<c:url value="/style/jquery-ui.css"/>" rel='stylesheet' type='text/css'/>
    <link href="<c:url value="/style/dataTables.jqueryui.css"/>" rel='stylesheet' type='text/css'/>
    <link href="<c:url value="/style/theme.css"/>" rel='stylesheet' type='text/css'/>
    <script type="application/x-javascript" src="<c:url value="/js/jquery-2.1.4.js" />"></script>
    <script type="application/x-javascript" src="<c:url value="/js/jquery-ui.min.js" />"></script>
    <script type="application/x-javascript" src="<c:url value="/js/jquery.dataTables.min.js" />"></script>
    <script type="application/x-javascript" src="<c:url value="/js/dataTables.jqueryui.js" />"></script>
    <script type="text/javascript" src="<c:url value="/js/common.js" />"></script>
    <title><decorator:getProperty property="page.titleText"/><decorator:getProperty property="page.pageTitle"/></title>
    <decorator:head/>
</head>
<body id="doc"
        <decorator:getProperty property="body.id" writeEntireProperty="true"/>
        <decorator:getProperty
                property="body.class"
                writeEntireProperty="true"/>>
<div id="header">
    <%@ include file="../common/header.jsp" %>
</div>
<div class="main">
    <div class="content">
        <div id="message"><%@ include file="../common/messages.jsp" %></div>
        <div class="header">
            <div class="title_text">
                <h1><decorator:getProperty property="page.titleText"/></h1>
            </div>
            <div class="tool_icon">
                <decorator:getProperty property="page.titleActionURLs"/>
            </div>
        </div>
        <decorator:body/>
    </div>
</div>
<footer>
    <%--<p>Copy right @2015</p>--%>
</footer>
<div style="display: none;">
    <div id="dialog-confirm" title="Confirm">
        <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span><span class="dialog-message">&nbsp;</span></p>
    </div>

    <div id="dialog-error" title="Error">
        <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span><span class="dialog-message">&nbsp;</span></p>
    </div>

    <div id="confirmation_message_to_delete">
        <fmt:message key="message.delete"/>
    </div>
    <div id="confirmation_message_to_delete_store">
        <fmt:message key="message.delete.store"/>
    </div>
    <div id="confirmation_message_to_delete_floor">
        <fmt:message key="message.delete.floor"/>
    </div>

    <div id="confirmation_message_to_archive">
        <fmt:message key="message.archive"/>
    </div>
    <div id="confirmation_message_to_archive_store">
        <fmt:message key="message.archive.store"/>
    </div>
    <div id="confirmation_message_to_archive_floor">
        <fmt:message key="message.archive.floor"/>
    </div>
    <div id="error_message_to_design_form">
        <fmt:message key="message.please.choose.atleast.one.file"/>
    </div>
</div>
</body>
</html>