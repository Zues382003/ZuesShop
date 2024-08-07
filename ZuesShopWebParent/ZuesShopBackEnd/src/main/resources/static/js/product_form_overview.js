var extraImagesCount = 0;

$(document).ready(function (){
    dropdownBrands = $("#brand")
    dropdownCategories = $("#category")
    $('#shortDescription').richText();
    $('#fullDescription').richText();
    dropdownBrands.change(function (){
        dropdownCategories.empty();
        getCategories();
    })
    getCategoriesForNewForm();
});

function getCategoriesForNewForm() {
    catIdField = $("#categoryId");
    editMode = false;

    if(catIdField.length){
        editMode = true;
    }

    if(!editMode) getCategories();
}

function getCategories() {
    brandId = dropdownBrands.val();
    url = brandModuleUrl + "/" + brandId + "/categories";

    $.get(url,function (responseJson){
        $.each(responseJson,function (index, category){
            $("<option>").val(category.id).text(category.name).appendTo(dropdownCategories);
        })
    });
}

function checkUnique(form) {
    url = urlCheckProduct;
    productId = $('#id').val();
    productName = $('#name').val();

    csrfValue = $("input[name = '_csrf']").val();
    params = {id: productId, name: productName, _csrf: csrfValue};

    $.post(url, params, function (response) {
        if (response === "OK") {
            form.submit();
        } else if (response === "Duplicate") {
            showWarningModal("There is another product having same name: " + productName);
        } else {
            showErrorModal("Unknow response form server");
        }
    }).fail(function () {
        showErrorModal("Could not connect to server");
    })

    return false;
}


