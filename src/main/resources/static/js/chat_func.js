function connect(serial){
	
	console.log("Serial "+serial)
	let socket = new SockJS('/chat');
	stompClient = Stomp.over(socket);
	stompClient.connect({},function(frame){
	console.log(frame);
	$(document).ready(function(){
		stompClient.subscribe('/user/'+serial, function(response){
			let data = JSON.parse(response.body);
			console.log(data);
			updateChat();
		});
	});
	});

}

function updateChat(){
	$(".message_container").load(location.href + " .message");
	$("#message_data").val('');
	scrollToChatBottom();
}


function disconnect(){
	stompClient.disconnect();
}

function sendMessage(serial, login){
	$(document).ready(function(){
	stompClient.send("/group/"+serial,{},
	JSON.stringify({'message':$("#message_data").val(),
	'fromLogin':login}));
	});
}

function executeAll(serial, login){
	console.log("Serial "+serial)
	let socket = new SockJS('/chat');
	stompClient = Stomp.over(socket);
	stompClient.connect({},function(frame){
	console.log(frame);
		$(document).ready(function(){
		stompClient.send("/group/"+serial,{},
		JSON.stringify({'message':$("#message_data").val(),
		'fromLogin':login}));
		stompClient.disconnect();
		});
	});
	
}

function scrollToChatBottom(){	
	const element = document.getElementById("message_container");
	element.scrollTop = element.scrollHeight;
}