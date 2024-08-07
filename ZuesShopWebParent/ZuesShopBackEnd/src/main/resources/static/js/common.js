$(document).ready(function (){

    //index
    $("#logoutLink").on("click",function (e){
        e.preventDefault();
        document.logoutForm.submit();

    })
    //Set Image Form Sec
    var imagePath = document.getElementById("imagePathFormSec").textContent
    document.getElementById("receiverImagePathFromSec").setAttribute("src","/ZuesShopAdmin" + imagePath);

    customizeDropDownMenu();
    customizeTabs();

})
function customizeDropDownMenu(e){
    $(".dropdown > a").click(function (){
        location.href = this.href;
    })

    $(".navbar .dropdown").hover(function (){
        $(this).find('.dropdown-menu').first().stop(true,true).delay(250).slideDown();
    },function (){
        $(this).find('.dropdown-menu').first().stop(true,true).delay(150).slideUp();
    });
}

function customizeTabs(){
    //Javascript to enable link to tab
    var url = document.location.toString();
    if(url.match('#')){
        $('.nav-tabs a[href="#' + url.split('#')[1] + '"]').tab('show');
    }

    //Change hash for page-reload
    $('.nav-tabs a').on('shown.bs.tab', function (e){
        window.location.hash = e.target.hash;
    })
}