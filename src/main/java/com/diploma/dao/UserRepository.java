package com.diploma.dao;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.diploma.model.GroupModel;
import com.diploma.model.GroupUser;
import com.diploma.model.UserModel;


public interface UserRepository extends CrudRepository<UserModel, Integer>{
	boolean existsUserModelByEmail(String email);
	
	boolean existsByEmailAndPasswordResetTokenValue(String email, String resetCode);
	
	boolean existsByEmailAndPass(String email,String pass);
	
	@Query("SELECT u.passwordResetToken.expiryDate FROM UserModel u WHERE u.email = :email")
	Date getPasswordResetTokenExpiryDateByEmail(@Param("email") String email);
	
	@Transactional
	@Modifying
	@Query(value="DELETE FROM password_reset_tokens t WHERE t.user_id= :id", nativeQuery=true)//change tomorrow
	void deleteTokenByUserId(@Param("id") int id);
	
	@Query("SELECT u.id FROM UserModel u WHERE u.email = :email ")
	int getUserModelIdByEmail(String email);
	
	UserModel getUserByEmailAndPass(String email, String pass);
	UserModel getUserByEmail(String email);
	
	@Query(value = "SELECT u.user_id FROM user_table_languages u where u.languages = :language", nativeQuery = true)
	List<Integer> getUserIdByLanguage(@Param("language")String lang);
	
	@Query("SELECT u FROM GroupUser u WHERE u.id.userid = :user_id")
	List<GroupUser> getUserGroups(@Param("user_id")int id);
	@Query("SELECT u.group FROM GroupUser u WHERE u.user.email = :user_email")
	List<GroupModel> getUserGroupsByEmail(@Param("user_email")String email);
}
