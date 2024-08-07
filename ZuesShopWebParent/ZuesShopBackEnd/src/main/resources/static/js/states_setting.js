var btnLoadCountriesForStates;
var dropDownCountriesForStates;
var dropDownStates;
var labelStateName;
var fieldStateName;
var btnAddState;
var btnUpdateState;
var btnDeleteState;

$(document).ready(function (){
    btnLoadCountriesForStates = $("#btnLoadCountriesForStates");
    dropDownCountriesForStates = $("#dropDownCountriesForStates");
    dropDownStates = $("#dropDownStates");
    labelStateName = $("#labelStateName");
    fieldStateName = $("#fieldStateName");
    btnAddState = $("#btnAddState");
    btnUpdateState = $('#btnUpdateState');
    btnDeleteState = $("#btnDeleteState");

    btnLoadCountriesForStates.click(function (){
        loadCountriesForStates();
    });

    dropDownCountriesForStates.on("change",function (){
        loadStateForCountry();
    });

    dropDownStates.on("change",function (){
        changeFormStateToSelectState();
        fieldStateName.on("input",function (){
            if(fieldStateName.val().length<=0)
            {
                btnUpdateState.prop("disabled", true);//disabled
            }else{
                btnUpdateState.prop("disabled", false);//enabled

            }
        })
    });


    btnAddState.click(function (){
        if(btnAddState.val() === "Add"){
            addStatus();
        }else{
            changeFormStateToNewState();
        }
    });

    btnUpdateState.click(function (){
       updateState();
    });

    btnDeleteState.click(function (){
       deleteState();
    });
});

function deleteState(){
    stateId = dropDownStates.val();
    url = contextPath + "states/delete/" + stateId;


    $.ajax({
        type: 'DELETE',
        url: url,
        beforeSend: function (xhr){
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
    }).done(function (){
        $("#dropDownStates option[value='" + stateId  + "']").remove();
        changeFormStateToNewState();
        showToastMessage("The state has been deleted");
    }).fail(function (){
        showToastMessage("ERROR: Could not connect to server or server encountered an error")
    });
}

function updateState(){
    if(!validateState()) return;

    url = contextPath + "states/save";

    stateId = dropDownStates.val();
    stateName = fieldStateName.val();

    selectedCountry = $("#dropDownCountriesForStates option:selected");
    countryId = selectedCountry.val();
    countryName = selectedCountry.text();
    jsonData = {id: stateId, name: stateName, country: {id: countryId, name: countryName}};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr){
            xhr.setRequestHeader(csrfHeaderName, csrfValue)
        },
        data: JSON.stringify(jsonData),// transform javascript to Json
        contentType: 'application/json'
    }).done(function (stateId){
        $("#dropDownStates option:selected").text(stateName);
        showToastMessage("The state has been updated");
    }).fail(function (){
        showToastMessage("ERROR: Could not connect to server or server encountered an error")

    });
}

function validateState(){
    formState = document.getElementById("formState");
    if(!formState.checkValidity()){
        formState.reportValidity();
        return false;
    }
    return true;
}

function addStatus(){
    if(!validateState()) return;

    url = contextPath + "states/save";
    stateName = fieldStateName.val().trim();

    selectedCountry = $("#dropDownCountriesForStates option:selected");
    countryId = selectedCountry.val();
    countryName = selectedCountry.text();
    jsonData = {name: stateName, country: {id: countryId, name: countryName}};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr){
            xhr.setRequestHeader(csrfHeaderName, csrfValue)
        },
        data: JSON.stringify(jsonData),// transform javascript to Json
        contentType: 'application/json'
    }).done(function (stateId){
        selectedNewlyAddedState(stateId, stateName);
        showToastMessage("The new state has been added");
    }).fail(function (){
        showToastMessage("ERROR: Could not connect to server or server encountered an error")

    });
}

function selectedNewlyAddedState(stateId, stateName){
    $("<option>").val(stateId).text(stateName).appendTo(dropDownStates);
    $("#dropDownStates option[value='" +stateId + "']").prop("selected", true);

    fieldStateName.val("").focus();
}

function changeFormStateToSelectState() {
    btnAddState.prop("value", "New");
    btnUpdateState.prop("disabled", false);
    btnDeleteState.prop("disabled", false);

    labelStateName.text("Selected State:")

    selectedState = $("#dropDownStates option:selected");

    selectedStateName = selectedState.text();
    fieldStateName.val(selectedStateName);

}

function changeFormStateToNewState(){
    btnAddState.val("Add");
    labelStateName.text("State/Province Name:");

    fieldStateName.val("").focus();
}

function loadStateForCountry(){
    selectedCountry = $("#dropDownCountriesForStates option:selected");
    countryId = selectedCountry.val();
    url = contextPath + "states/list_by_country/" + countryId;

    $.get(url,function (responseJson){
        dropDownStates.empty();

        $.each(responseJson,function (index, state){
            $("<option>").val(state.id).text(state.name).appendTo(dropDownStates);
        });
    }).done(function (){
        showToastMessage("All states have been loaded for country " + selectedCountry.text());
    }).fail(function (){
        showToastMessage("ERROR: Could not connect to server or server encountered an error")
    });
}

function loadCountriesForStates(){
    url = contextPath + "countries/list";

    $.get(url,function (responseJson){
        dropDownCountriesForStates.empty();

        $.each(responseJson,function (index, country){
            $("<option>").val(country.id).text(country.name).appendTo(dropDownCountriesForStates);
        });
    }).done(function (){
        btnLoadCountriesForStates.val("Refresh Country List");
        showToastMessage("All countries have been loaded");
    }).fail(function (){
        showToastMessage("ERROR: Could not connect to server or server encountered an error")
    });
}