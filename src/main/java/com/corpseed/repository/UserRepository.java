package com.corpseed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);

	User findByUuidAndDeleteStatus(String useruuid,int dStatus);

	User findByEmailAndUuidNot(String email,String useruuid);

	List<User> findByDeleteStatusOrderByIdDesc(int dStatus);

	List<User> findByRoleNotAndDeleteStatusAndAccountStatus(String role,int dStatus,int accStatus);

	List<User> findByRoleNotInAndDeleteStatus(List<String> roles,int dStatus);

	User findByEmailAndRoleAndDeleteStatus(String email, String role,int dStatus);

	User findByEmailAndDisplayStatusAndDeleteStatus(String email, String status,int dStatus);

	List<User> findByDisplayStatusAndDeleteStatus(String status,int dStatus);

	List<User> findByRoleAndDeleteStatus(String role,int dStatus);

	User findByEmailAndDeleteStatus(String username, int dStatus);

	User findByIdAndDeleteStatus(long typeId, int i);

	List<User> findByRoleNotInAndDepartmentAndDeleteStatus(List<String> roles, String dept, int dStatus);

	User findByEmailAndRoleNot(String email, String role);

	List<User> findByRoleNotAndDeleteStatus(String string, int i);

    List<User> findUserByAccountStatusAndDeleteStatusAndDisplayStatusAndRoleNot(int accountStatus, int deleteStatus, String displayStatus,String role_user);

    User findBySlugAndRoleNot(String slug, String role_user);

	User findBySlugAndRoleNotAndIdNot(String slug, String role_user, long id);

    User findBySlug(String userSlug);
}
