$(document).ready(function (){
    $("a[name = 'linkRemoveDetail']").each(function (index){
        $(this).click(function (){
            removeDetailSectionByIndex(index);
        })
    })
})

function addNextDetailSection(){
    allDivDetails = $("[id^='divDetail']");
    divDetailsCount = allDivDetails.length;

    htmlDetailSection = `
            <div class="col-lg-5 border color-font p-4 mt-4" id="divDetail${divDetailsCount}" >
                <input type="hidden" name="detailIDs" value="0">
                <label class="m-3" style="font-size: 1.8rem">Name:</label>
                <input style="font-size: 1.6rem" type="text" class="form-control" name="detailNames" maxlength="255">
                <label class="m-3" style="font-size: 1.8rem">Value:</label>
                <input style="font-size: 1.6rem" type="text" class="form-control" name="detailValues" maxlength="255">
            </div>
    `;

    $("#divProductDetails").append(htmlDetailSection);

    previousDivDetailSection = allDivDetails.last();
    previousDivDetailId = previousDivDetailSection.attr("id");
    htmlLinkRemove = `
            <a class="btn fas fa-times-circle fa-2x mt-3 icon-dark float-right" 
            href="javascript:removeDetailSectionById('${previousDivDetailId}')"
            style="cursor: pointer; color: black" title="Remove this detail"></a>
    `

    previousDivDetailSection.append(htmlLinkRemove);

    $("input[name = 'detailNames']").last().focus();
}

function removeDetailSectionById(id){
    $("#" + id).remove();
}

function removeDetailSectionByIndex(index){
    $("#divDetail" + index).remove();
}