function clearFilter(){
    window.location= moduleURl;
}

function showDeleteConfirmModal(link, entityName){
    entityId = link.attr("entityId")

    $("#confirmText").text("Are you sure you want to delete this " + entityName + " ID: " + entityId + " ?");
    $("#buttonYes").attr("href",link.attr("href"));
    $("#confirmModal").modal();
}
