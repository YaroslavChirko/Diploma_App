package com.diploma.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.diploma.model.MessageModel;
import com.diploma.model.MessageModel.MessageType;


public interface MessageRepository extends CrudRepository<MessageModel, Integer> {
	
	public List<MessageModel> getMessagesByGroupGroupSerialOrderBySentAtAsc(String serial);
	public List<MessageModel> getMessagesByGroupGroupSerialAndTypeOrderBySentAtAsc(String serial, MessageType type);
	@Transactional
	public List<MessageModel> deleteMessagesByGroupGroupSerialAndTypeAndFromLogin(String serial, MessageType type,String fromLogin);
}
