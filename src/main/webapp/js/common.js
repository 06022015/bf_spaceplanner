var AREA_VALIDATION_REGEX="^[1-9]([0-9]{1,6})?(\\.[0-9]{1,2})?$";

if(!('contains' in String.prototype)) {
    String.prototype.contains = function(str, startIndex) {
        return -1 !== String.prototype.indexOf.call(this, str, startIndex);
    };
}




function validatePassword() {
    var password = document.getElementById('password');
    var confirmPassword = document.getElementById('confirmPassword');
    if (password.value != confirmPassword.value) {
        confirmPassword.setCustomValidity('Passwords Don`t Match');
    } else {
        confirmPassword.setCustomValidity('');
    }
}

function disableSubmitButton(element){
    $(element).prop("disabled", true);
}

function validateExcelFileType(element){
    var value = $(element).val();
    var ext = value.split('.')[value.split('.').length-1].toLowerCase();
    var fieldFile = document.getElementById('file');
    var fieldFileName = document.getElementById('file_name');
    if(ext=="xls" || ext=="xlsx"){
        fieldFile.setCustomValidity('');
        fieldFileName.setCustomValidity('');
    }else{
        fieldFile.setCustomValidity('Invalid File Format');
        fieldFileName.setCustomValidity('Invalid File Format');
    }
}

$(document).ready(function(){
    /*$('#error, #success').fadeOut(10000);*/
    $('.type_file input[type=text]').on('focus, keydown' ,function(){
        $(this).trigger("blur");
    });

    $('.type_file input[type=file]').change(function(){
        $(this).parent().parent().find("input[type=text]").val(this.value);
        if($(this).parent().parent().find("input[type=text]").hasClass("validateExcel")){
            validateExcelFileType(this);
        }
    });

    $("#add_floor").on("click", function () {
        var clonedRow = $("table#new_floor_table").find("tr").clone();
        showFormElements(clonedRow.find(".form_element"));
        if ($("table#floor_list").find("tbody").find("tr").find("td").hasClass("dataTables_empty")) {
            $("table#floor_list").find("tbody").find("tr").remove();
            clonedRow.appendTo($("table#floor_list").find("tbody"));
        } else {
            clonedRow.insertAfter($("table#floor_list").find("tbody").find("tr").last());
        }
        var rowLength = $("table#floor_list").find("tbody").find("tr").length;
        var maxRowLength = $(clonedRow).attr("noOfFloor");
        if (rowLength >= maxRowLength) {
            $(this).hide();
        }
        $("table#floor_list").find("tbody").find(".edit").hide();
    });

    $(document).on("click","td .action", function () {
        if ($(this).hasClass("edit")) {
            $(this).parents("tbody").find(".edit").hide();
            $("#add_floor").hide();
            showFormElements(this);
        } else if ($(this).hasClass("delete")) {
            alert("Delete");
        }
    });

    $(document).on("click","td .save", function(e){
        $("form#floor_form").find("input[type=submit]").click();
    });

    $(document).on("click","td .cancel", function(){
        var rowForm = $(this).parents("tr");
        if(rowForm.hasClass("new_floor")){
            rowForm.remove();
            var rowLength = $("table#floor_list").find("tbody").find("tr").length;
            var maxRowLength = $(rowForm).attr("noOfFloor");
            if (rowLength < maxRowLength) {
                $("#add_floor").show();
            }
        }else{
            showTableElements(this);
        }
        $("table#floor_list").find("tbody").find(".edit").show();
        $("#add_floor").show();
    });

    /*$('select').selectmenu(
     {
     change: function( event, ui ) {
     $(this).parent().find('select').trigger("change");
     }
     }
     );*/

    var url= window.location.href;
    $(".archive").click(function(e){
        e.preventDefault();
        url = $(this).attr("href");
        var  message = $("#confirmation_message_to_archive").html().trim();
        message  = message+ " '"+$(this).parents("tr").find(".action_property").html().trim()+"'";
        openConfirmationDialog(message);
    });

    $(".delete").click(function(e){
        e.preventDefault();
        url = $(this).attr("href");
        var  message = $("#confirmation_message_to_delete").html().trim();
        message  = message+ " '"+$(this).parents("tr").find(".action_property").html().trim()+"'";
        openConfirmationDialog(message);
    });

    $( "#dialog-confirm" ).dialog({
        autoOpen: false,
        resizable: false,
        modal: true,
        buttons: {
            Yes: function() {
                window.location.href= url;
                $( this ).dialog( "close" );
            },
            No: function() {
                $( this ).dialog( "close" );
            }
        }
    });

    $( "#dialog-error" ).dialog({
        autoOpen: false,
        resizable: false,
        modal: true,
        buttons: {
            Ok: function() {
                $( this ).dialog( "close" );
            }
        }
    });

    $("form#upload_design_form").submit(function (e){
        var designAutocadFile = $("#designAutocadFile").val().trim();
        var designPDFFile = $("#designPDFFile").val().trim();
        var isValid = undefined!=designAutocadFile && null!= designAutocadFile && designAutocadFile !="";
        if(!isValid)
            isValid = undefined!=designPDFFile && null!= designPDFFile && designPDFFile !="";
        if(!isValid){
            e.preventDefault();
            var  message = $("#error_message_to_design_form").html().trim();
            openAlertDialog(message);
        }

    });

});

function validateFloorNumber(){
    var field = document.getElementById('floorNumber');
    var storeId = $("form#floor_form").find("input[name=\'store.id\']").val().trim();
    var floorId = $("#floorNumber").parents("tr").find("input[name=id]").val();
    $.get(getContextPath() + '/comm/validate/floorNumber.html?' + new Date(), {storeId:storeId,floorId:floorId, floorNumber:field.value, ajax:true, date:new Date()}, function (response) {
        if(!response.isValid){
            field.setCustomValidity('Floor Number is already exist');
        } else {
            field.setCustomValidity('');
        }
    });
}

function validateStoreName(){
    var field = document.getElementById('storeName');
    var storeId = $("form#store_form").find("#id").val();
    $.get(getContextPath() + '/comm/validate/storeName.html?' + new Date(), {storeId: storeId, storeName:field.value, ajax:true, date:new Date()}, function (response) {
        if(!response.isValid){
            field.setCustomValidity('Store name is already exist');
        } else {
            field.setCustomValidity('');
        }
    });
}

function openConfirmationDialog(message){
    $("#dialog-confirm").find("p").find(".dialog-message").html(message);
    $( "#dialog-confirm" ).dialog('open');
}

function openAlertDialog(message){
    $("#dialog-error").find("p").find(".dialog-message").html(message);
    $( "#dialog-error" ).dialog('open');
}

function loadFloor(element,url){
    var newURL = getContextPath()+url;
    var selectedVal = $(element).val();
    if(undefined !=selectedVal && ""!= selectedVal){
        if(newURL.contains("?")){
            newURL = newURL+ "&storeId="+selectedVal;
        }else{
            newURL = newURL+ "?storeId="+selectedVal;
        }
    }
    window.location.href = newURL;
}


function loadFloorDetails(element, url){
    var newURL = getContextPath()+url;
    var selectedVal = $(element).val();
    if(undefined !=selectedVal && ""!= selectedVal){
        newURL = newURL+"&floorId="+selectedVal;
    }
    window.location.href = newURL;
}

function loadFloorDetailsByVersion(element, url){
    var newURL = getContextPath()+url;
    var selectedVal = $(element).val();
    if(undefined !=selectedVal && ""!= selectedVal){
        newURL = newURL+"&version="+selectedVal;
    }
    window.location.href = newURL;
}

function loadChild(element, url){
    var newURL = getContextPath()+url;
    var selectedVal = $(element).val();
    if(undefined !=selectedVal && ""!= selectedVal){
        newURL = newURL+"&floorId="+selectedVal;
    }
    window.location.href = newURL;
}

function getContextPath() {
    return window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
}

function getUIR(){
    return window.location.pathname.substring(window.location.pathname.indexOf("/",2));
}


function showTableElements(element){
    $(element).parents("tr").find("td").each(function(){
        if($(this).hasClass("editable")){
            var value = $(this).attr("value");
            if($(this).hasClass("link")){
                var element = document.createElement("a");
                $(element).attr("href", $(this).attr("href"));
                $(element).html(value);
                value = $(element);
            }
            $(this).html(value);
        }
    });
    $(element).parents("tr").find(".form_element").hide();
    $(element).parents("tr").find(".table_element").show();
}

function showFormElements(element){
    $(element).parents("tr").find("td").each(function(){
        if($(this).hasClass("editable")){
            var inputField = document.createElement("input");
            $(inputField).attr("type","text");
            $(inputField).attr("name",$(this).attr("name"));
            $(inputField).attr("maxlength",12);
            $(inputField).attr("value",$(this).attr("value"));
            $(inputField).attr("required",$(this).attr("required"));
            if($(this).attr("name")=="floorNumber"){
                $(inputField).attr("id","floorNumber");
                $(inputField).attr("onblur","validateFloorNumber();");
            }else{
                $(inputField).attr("pattern",AREA_VALIDATION_REGEX);
            }
            $(inputField).attr("form",$(this).attr("form"));
            $(this).html(inputField);
        }else if($(this).hasClass("hidden")){
            var inputField = document.createElement("input");
            $(inputField).attr("type","hidden");
            $(inputField).attr("name","id");
            $(inputField).attr("value",$(this).attr("value"));
            $(inputField).attr("form",$(this).attr("form"));
            $(this).find(".save").parent().append(inputField);
        }
    });
    $(element).parents("tr").find(".table_element").hide();
    $(element).parents("tr").find(".form_element").show();
}