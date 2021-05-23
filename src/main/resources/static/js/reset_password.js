function getResetLink(){

const header=  $("meta[name='_csrf_header']").attr("content");
const token = $("meta[name='_csrf']").attr("content");

$(document).ajaxSend(function(e, xhr, options) {
        if (options.type == "POST" && options.url == "/user/forgot-password") {
            xhr.setRequestHeader(header, token);
        }
    });

$.post("/user/forgot-password",{email:$('.email').val()},function(data){
		if(data){
		$('.message').empty();
		$('.message').append("<p style='color:lightgray'>email sent sucessfully</p>");
		}else{
		$('.message').empty();
		$('.message').append("<p style='color:red' >user was not found.<br> Check email or register.</p>");
		}
});
}
