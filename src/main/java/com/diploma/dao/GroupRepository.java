package com.diploma.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.diploma.model.GroupModel;
import com.diploma.model.GroupUser;
import com.diploma.model.LanguageEnum;

public interface GroupRepository extends CrudRepository<GroupModel, Integer> {
	
	List<GroupModel> getGroupsByGroupLanguage(LanguageEnum language);
	LanguageEnum getGroupLanguageByGroupSerial(String groupSerial); 
	GroupModel getGroupByGroupSerial(String groupSerial); 
	@Query("SELECT u FROM GroupUser u WHERE u.id.groupid = :group_id")
	List<GroupUser> getGroupUsers(@Param("group_id")int id);
	
	@Query("SELECT u FROM GroupUser u WHERE u.group.groupSerial= :group_serial")
	List<GroupUser> getGroupUsers(@Param("group_serial")String serial);
	
	@Query("SELECT u.user.id FROM GroupUser u WHERE u.group.groupSerial= :group_serial")
	List<Integer> getGroupUserIds(@Param("group_serial")String serial);
	
	@Query("SELECT COUNT(u) FROM GroupUser u WHERE u.group.groupSerial= :group_serial AND u.user.email = :email")
	long isUserInGroup(@Param("group_serial")String serial,@Param("email")String email);
	
	@Query("SELECT u FROM GroupUser u WHERE u.group.groupSerial= :group_serial AND u.user.id = :id")
	GroupUser getUserFromGroup(@Param("group_serial")String serial,@Param("id")int id);
	
	@Query("SELECT u FROM GroupUser u WHERE u.group.groupSerial= :group_serial AND u.user.email = :email")
	GroupUser getUserFromGroup(@Param("group_serial")String serial,@Param("email")String email);
	
	@Query("SELECT u.hasAcceptRequestRights FROM GroupUser u WHERE u.group.groupSerial= :group_serial AND u.user.email = :email")
	boolean canUserAcceptRequests(@Param("group_serial")String serial,@Param("email")String email);
	
	@Query("SELECT u.hasTaskModifyRights FROM GroupUser u WHERE u.group.groupSerial= :group_serial AND u.user.email = :email")
	boolean canUserModifyTasks(@Param("group_serial")String serial,@Param("email")String email);
	
	@Query("SELECT u.hasGroupModifyRights FROM GroupUser u WHERE u.group.groupSerial= :group_serial AND u.user.email = :email")
	boolean canUserModifyGroup(@Param("group_serial")String serial,@Param("email")String email);
	
	@Query("SELECT g FROM GroupModel g LEFT JOIN FETCH g.messages WHERE g.groupSerial= :group_serial")
	GroupModel getGroupByGroupSerialJoinMessages(@Param("group_serial")String groupSerial); 
}
