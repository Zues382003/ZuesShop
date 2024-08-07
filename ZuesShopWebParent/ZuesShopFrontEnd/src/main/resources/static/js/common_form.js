//user_form
$(document).ready(function (){

    //show animation when focus in input at user-form and account_form
    const form_email = document.querySelectorAll('.js-input-form')

    for(let item of form_email){
        item.addEventListener('input',function (){

            if(String(item.value).length>0)
            {
                item.classList.add('is-valid');
            }
            else{
                item.classList.remove('is-valid');
            }

        })

        // Kiểm tra độ dài giá trị ban đầu khi binding dữ liệu
        if(String(item.value).length>0)
        {
            item.classList.add('is-valid');
        }
        else{
            item.classList.remove('is-valid');
        }
    }
    //

    // Show Photos of user
    $('#fileImage').on("change",function (){
        if(!checkFileSize(this)){
            return;
        }
        showImageThumbnail(this);
    });

    $('#btn-Cancel').on("click",function (){
        window.location = moduleUrl;
    });
    //
});

function showImageThumbnail(fileInput){
    var file = fileInput.files[0];

    var reader = new FileReader();
    reader.onload = function (e){
        $("#thumbnail").attr("src",e.target.result);
    }

    reader.readAsDataURL(file);
}

function checkFileSize(fileInput){
    var fileSize = fileInput.files[0].size;

    if(fileSize > 1048576){
        fileInput.setCustomValidity("You must choose an image less than 1MB!")
        fileInput.reportValidity();
        return false;
    }
    else{
        fileInput.setCustomValidity("");
        return true;
    }
}

function showModalDialog(title, message){
    $("#modalTitle").text(title);
    $("#modalBody").text(message);
    $("#modalDialog").modal();
}

function showErrorModal(message){
    showModalDialog("Error",message);
}

function showWarningModal(message){
    showModalDialog("Warning",message);
}

