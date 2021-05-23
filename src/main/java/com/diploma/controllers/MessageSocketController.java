package com.diploma.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.diploma.dao.GroupRepository;
import com.diploma.dao.MessageRepository;
import com.diploma.model.GroupModel;
import com.diploma.model.MessageModel;
import com.diploma.model.MessageModel.MessageType;

@RestController
public class MessageSocketController {
	@Autowired
	SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	GroupRepository groupRep;
	@Autowired
	MessageRepository messageRep;
	
	@MessageMapping("/{groupSerial}")
	private void sendToGroup(@DestinationVariable("groupSerial")String serial, MessageModel message) {
		
		GroupModel group = groupRep.getGroupByGroupSerialJoinMessages(serial);
		System.out.println(group);
		message.setType(MessageType.MESSAGE);
		group.addMessage(message);
		groupRep.save(group);
		
		MessageModel sendable = new MessageModel(message.getMessage(),message.getFromLogin(),message.getSentAt());
		messagingTemplate.convertAndSend("/user/"+serial,sendable);//gives error, should find a way to omit sending group 
	}
	
	@MessageMapping("/{groupSerial}/requests")
	private void sendRequestToGroup(@DestinationVariable("groupSerial")String serial, MessageModel message) {
		
		GroupModel group = groupRep.getGroupByGroupSerialJoinMessages(serial);
		message.setType(MessageType.REQUEST);
		message.setMessage("");
		group.addMessage(message);
		groupRep.save(group);
		
		MessageModel sendable = new MessageModel(message.getMessage(),message.getFromLogin(),message.getSentAt());
		messagingTemplate.convertAndSend("/user/"+serial+"/requests",sendable);//gives error, should find a way to omit sending group 
	}
}
