var extraImagesCount = 0;
$(document).ready(function (){
    $("input[name = 'extraImage']").each(function (index){
        extraImagesCount++;
        $(this).change(function (){

            if(!checkFileSize(this)){
                return;
            }
            showExtraImageThumbnail(this, index);
        });
    });

    $("a[name = 'linkRemoveExtraImage']").each(function (index){
        $(this).click(function (){
            removeExtraImage(index);
        })
    });
});

function showExtraImageThumbnail(fileInput, index) {
    var file = fileInput.files[0];

    fileName = file.name;

    imageNameHiddenField = $("#imageName" + index);
    if(imageNameHiddenField.length){
        imageNameHiddenField.val(fileName);
    }

    var reader = new FileReader();
    reader.onload = function (e) {
        $("#extraThumbnail" + index).attr("src", e.target.result);
    }

    reader.readAsDataURL(file);
    if (index >= extraImagesCount - 1) {
        addExtraImageSection(index + 1);
    }
}

function addExtraImageSection(index) {
    htmlAddExtraImage = `
            <div class="col border m-3 p-2" id="divExtraImage${index}"
             style="font-size: 1.6rem; margin-top: 20px">
            <div id="extraImageHeader${index}"><label class="m-2 color-font">Extra Image #${index + 1}:</label></div>
            <div class="m-2">
                <img id="extraThumbnail${index}" alt="Extra image #${index + 1} preview" class="img-fluid"
                     src="${defauleImage}">
                <input type="file"  
                        onchange="showExtraImageThumbnail(this, ${index})"
                        name="extraImage" class="mt-2" accept="image/png, image/jpeg">
            </div>
        </div>
            `
    htmlRemoveExtraImage = `
            <a class="btn fas fa-times-circle fa-2x icon-dark float-right" 
            href="javascript:removeExtraImage(${index-1})"
            style="cursor: pointer" title="Remove this image"></a>
    `


    $("#divProductImage").append(htmlAddExtraImage);
    $("#extraImageHeader" + (index-1)).append(htmlRemoveExtraImage);

    extraImagesCount++;

}

function removeExtraImage(index) {
    $("#divExtraImage" + index).remove();
}


