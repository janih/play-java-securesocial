package services

import securesocial.core.RuntimeEnvironment
import securesocial.core.services.UserService
import securesocial.core.providers._

import scala.collection.immutable.ListMap

/** 
 * see https://github.com/jaliss/securesocial/blob/master/module-code/app/securesocial/core/RuntimeEnvironment.scala
 */
class MyEnvironment extends RuntimeEnvironment.Default[User] {
	override val userService: UserService[User] = new MyUserService()

	override lazy val providers = ListMap(
		include(new TwitterProvider(routes, cacheService, oauth1ClientFor(TwitterProvider.Twitter))),
		include(new FacebookProvider(routes, cacheService, oauth2ClientFor(FacebookProvider.Facebook))),
		include(new GoogleProvider(routes, cacheService, oauth2ClientFor(GoogleProvider.Google)))
	)
}
