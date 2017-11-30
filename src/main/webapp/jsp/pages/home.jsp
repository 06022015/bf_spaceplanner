<%@ include file="../common/taglib.jsp" %>
<%--<script type="text/javascript">
    $(document).ready(function(){
        $('.brows_btn').change(function(){
            $(this).parent.parent.find(".file_name").val(this.value);
        });
        $('.type_file').onclick(function(){
             $(this).find("input[type=file]").click();
        });
    });
    /*document.getElementById("uploadBtn").onchange = function () {
        document.getElementById("uploadFile").value = this.value;
    };*/
</script>--%>

<div class="form">
    <form action="/comm/upload.html" method="post">
        <ul>
            <h2><fmt:message key="label.Upload"/> </h2>
            <li>
                <div class="type_file">
                    <input class="file_name" required placeholder="Choose File" type="text" disabled="disabled"/>
                    <div class="brows_btn_block">
                        <span>Brows</span>
                        <input type="file" required accept="application/dxf, application/pdf,application/msexcel"/>
                    </div>
                </div>
            </li>
            <li>
                <select name="test">
                    <option value="1">Mumbai</option>
                    <option value="2">Bangalore</option>
                    <option value="3">Delhi</option>
                    <option value="4">Kolkata</option>
                    <option value="5">Chennai</option>
                    <option value="6">Chandigarh</option>
                    <option value="7">Vadodra</option>
                    <option value="8">Surat</option>
                    <option value="9">Ahemdabad</option>
                    <option value="10">Varanashi</option>
                </select>
            </li>
            <li>
                <a href="<c:url value="/comm/download/dxf.html"/>"  title="<fmt:message key="label.Download"/>">Download DXF FIle</a>
            </li>
            <li>
                <input type="submit" onclick="myFunction()" value="<fmt:message key="label.Upload"/>">
                <div class="clear"></div>
            </li>
        </ul>
    </form>
</div>
