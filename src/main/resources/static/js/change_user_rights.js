function showChangeForm(id){
	$('.manage_'+id).remove();
	$('.user_rights_'+id).remove();
	$('.user_info_'+id).append('grant request accept rights<input type="checkbox" name="request-rights" class="request_'+id+'"><br>'
		+'grant task add/modify rights<input type="checkbox" name="task-rights" class="task_'+id+'"><br>'
		+'grant group modify rights<input type="checkbox" name="group-rights" class="group_'+id+'"><br>'
		+'<button  class="'+id+'" onclick="sendUpdateRequest('+id+')">save changes</button>');
}

function sendUpdateRequest(id){

const header=  $("meta[name='_csrf_header']").attr("content");
const token = $("meta[name='_csrf']").attr("content");

$(document).ajaxSend(function(e, xhr, options) {
        if (options.type == "POST" && options.url == "manage-rights/"+id) {
            xhr.setRequestHeader(header, token);
        }
    });

$.post("manage-rights/"+id,
		{
		requestRights:$('.request_'+id).is(":checked"),
		groupRights:$('.group_'+id).is(":checked"),
		taskRights:$('.task_'+id).is(":checked")
		},function(data){
		if(data){
			alert("User rights changed sucessfully!");
		}else{
			alert("No such user or user is creator of the group!")
		}
			$('.users_container').load(location.href +'.user_info_'+id ); // doesn`t load
			
		
});
	
}