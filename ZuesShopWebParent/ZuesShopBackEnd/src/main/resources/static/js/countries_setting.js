var btnLoadCountries;
var dropDownCountries;
var btnAddCountry;
var btnUpdateCountry;
var btnDeleteCountry;
var labelCountryName;
var fieldCountryName;
var labelCountryCode;
var fieldCountryCode;

$(document).ready(function (){
    btnLoadCountries = $("#btnLoadCountries");
    dropDownCountries = $("#dropDownCountries");

    btnAddCountry = $("#btnAddCountry");
    btnUpdateCountry = $("#btnUpdateCountry");
    btnDeleteCountry = $("#btnDeleteCountry");
    labelCountryName = $("#labelCountryName");
    fieldCountryName = $("#fieldCountryName");
    labelCountryCode = $("#labelCountryCode");
    fieldCountryCode = $("#fieldCountryCode");

    btnLoadCountries.on("click",function (){
        loadCountries();
    });

    dropDownCountries.on("change", function (){
        changeFormStateToSelectCountry();
        fieldCountryName.on("input",function (){
            if(fieldCountryName.val().length > 0 )
            {
                btnUpdateCountry.prop("disabled", false);//enabled
            }else{
                btnUpdateCountry.prop("disabled", true);//disabled
            }
        })
        fieldCountryCode.on("input",function (){
            if(fieldCountryCode.val().length > 0 )
            {
                btnUpdateCountry.prop("disabled", false);
            }else{
                btnUpdateCountry.prop("disabled", true);
            }
        })
    });

    btnAddCountry.click(function (){
        if(btnAddCountry.val() === "Add"){
            addCountry();
        }else{
            changeFormStateToNewCountry();
        }
    });

    btnUpdateCountry.click(function (){
        updateCountry();
    });

    btnDeleteCountry.click(function (){
        deleteCountry();
    })
});

function deleteCountry(){
    optionValue = dropDownCountries.val();
    countryId = optionValue.split("-")[0];
    url = contextPath + "countries/delete/" + countryId;

    $.ajax({
        type: 'DELETE',
        url: url,
        beforeSend: function (xhr){
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
    }).done(function (){
        $("#dropDownCountries option[value='" + optionValue + "']").remove();
        changeFormStateToNewCountry();
        showToastMessage("The country has been deleted");

    }).fail(function (){
        showToastMessage("ERROR: Could not connect to server or server encountered an error")
    });
}

function updateCountry(){
    if(!validateFormCountry()) return;

    url = contextPath + "countries/save";
    countryName = fieldCountryName.val().trim();
    countryCode = fieldCountryCode.val().trim();

    countryId = dropDownCountries.val().split("-")[0];

    jsonData = {id: countryId, name: countryName, code: countryCode};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr){
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),// transform javascript to json
        contentType: 'application/json'
    }).done(function (countryId){
        selectedCountry = $("#dropDownCountries option:selected");
        selectedCountry.val(countryId + "-" + countryCode);
        selectedCountry.text(countryName);
        showToastMessage("The country has been updated");

    }).fail(function (){
        showToastMessage("ERROR: Could not connect to server or server encountered an error")
    });
}

function validateFormCountry(){
    formCountry = document.getElementById("formCountry");
    if(!formCountry.checkValidity()){
        formCountry.reportValidity();
        return false;
    }

    return true;
}

function addCountry(){

    if(!validateFormCountry()) return;

    url = contextPath + "countries/save";
    countryName = fieldCountryName.val().trim();
    countryCode = fieldCountryCode.val().trim();
    jsonData = {name: countryName, code: countryCode};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr){
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function (countryId){
        selectNewlyAddedCountry(countryId, countryName, countryCode);
        showToastMessage("The new country has been added");
    }).fail(function (){
        showToastMessage("ERROR: Could not connect to server or server encountered an error")
    });
}

function selectNewlyAddedCountry(countryId, countryName, countryCode){
    optionValue = countryId + "-" + countryCode;
    $("<option>").val(optionValue).text(countryName).appendTo(dropDownCountries);

    $("#dropDownCountries option[value='" + optionValue + "']").prop("selected", true);

    fieldCountryCode.val("");
    fieldCountryName.val("").focus();
}

function changeFormStateToNewCountry(){
    btnAddCountry.val("Add");
    labelCountryName.text("Country Name:");

    fieldCountryCode.val("");
    fieldCountryName.val("").focus();
}

function changeFormStateToSelectCountry(){
    btnAddCountry.prop("value","New");
    btnUpdateCountry.prop("disabled", false);
    btnDeleteCountry.prop("disabled", false);

    labelCountryName.text("Selected Country:")

    selectedCountry = $("#dropDownCountries option:selected");

    selectedCountryName = selectedCountry.text();
    fieldCountryName.val(selectedCountryName);

    selectedCountryCode = selectedCountry.val().split("-")[1];
    fieldCountryCode.val(selectedCountryCode);
}

function loadCountries() {
    url = contextPath + "countries/list";

    $.get(url,function (responseJson){
        dropDownCountries.empty();

        $.each(responseJson,function (index, country){
            optionValue = country.id + "-" + country.code;
            $("<option>").val(optionValue).text(country.name).appendTo(dropDownCountries);
        });
    }).done(function (){
        btnLoadCountries.val("Refresh Country List");
        showToastMessage("All countries have been loaded");
    }).fail(function (){
        showToastMessage("ERROR: Could not connect to server or server encountered an error")
    });
}

function showToastMessage(message){
    $("#toastMessage").text(message);
    $(".toast").toast("show");
}