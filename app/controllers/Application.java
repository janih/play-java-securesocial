package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import securesocial.core.BasicProfile;
import securesocial.core.java.SecureSocial;
import securesocial.core.java.SecuredAction;
import services.User;

public class Application extends Controller {

	public static Result index() {
		BasicProfile user = (BasicProfile) ctx().args.get(SecureSocial.USER_KEY);
		return ok(views.html.index.render(user != null));
	}

	@SecuredAction(authorization = WithProvider.class, params = {"google", "twitter", "facebook"})
	public static Result start() {
		User user = (User) ctx().args.get(SecureSocial.USER_KEY);
		String msg = "Hello " + user.getMain().fullName().get() + " from " + user.getMain().providerId() + "!";
		return ok(views.html.start.render(msg));
	}

}
