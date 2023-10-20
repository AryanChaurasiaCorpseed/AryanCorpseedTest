package com.corpseed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corpseed.entity.ForgetPassword;
import com.corpseed.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface ForgetPasswordRepository extends JpaRepository<ForgetPassword, Long> {

	ForgetPassword findByUserAndDeleteStatus(User user,int dStatus);

	ForgetPassword findByUserAndDisplayStatusAndDeleteStatus(User user, String status,int dStatus);

	List<ForgetPassword> findByPostDateBeforeAndDeleteStatus(String dateBeforeDays,int dStatus);

}
