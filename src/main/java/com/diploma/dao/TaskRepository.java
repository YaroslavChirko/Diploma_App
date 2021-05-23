package com.diploma.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.diploma.model.TaskModel;

public interface TaskRepository extends CrudRepository<TaskModel, Long> {
	public List<TaskModel> getTasksByExecutorEmailAndGroupGroupSerialOrderByDeadlineAsc(String email, String groupSerial);

	public List<TaskModel> getTasksByExecutorEmailAndGroupGroupSerialAndStateOrderByDeadlineAsc(String email, String groupSerial,String state);
	
	public List<TaskModel> getTasksByGroupGroupSerialAndStateOrderByDeadlineAsc(String groupSerial,String state);
}
