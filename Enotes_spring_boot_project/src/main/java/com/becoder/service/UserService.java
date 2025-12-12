package com.becoder.service;

import com.becoder.entity.User;

public interface UserService {

	 User saveUser(User user);

	boolean existEmailCheck(String email);
	
	//added

}
