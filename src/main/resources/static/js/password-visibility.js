function changeVisibility(){
	if(document.getElementById('password_field').type=='text'){
		document.getElementById('password_field').type = 'password';
	}else{
		document.getElementById('password_field').type = 'text';
	}
}