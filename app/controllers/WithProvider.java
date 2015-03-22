package controllers;

import securesocial.core.java.Authorization;
import services.User;

public class WithProvider implements Authorization<User> {
	public boolean isAuthorized(User user, String params[]) {
		return user.main.providerId().equals(params[0]);
	}
}