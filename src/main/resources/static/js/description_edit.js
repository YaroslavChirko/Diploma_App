function showEditForm(name,info){
$('.entity_name').empty();
$('.entity_name').append('<input class="name_edit" name="name" value='+name+'>');

$('.entity_information').empty();
$('.entity_information').append('<textarea class="information_edit" name="info" value='+info.split(" ").join("&nbsp;")+'></textarea>');

$('.edit').remove();
$('.edit_form').append('<input class="submit_button" type="submit" value="save changes">');
}