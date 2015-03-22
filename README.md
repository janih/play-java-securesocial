#play-java-securesocial#

An example project using [Play Framework](https://www.playframework.com/https://www.playframework.com/) and [SecureSocial](http://securesocial.ws) to authenticate with Google, Twitter and Facebook.

Versions used:
* Scala 2.11
* Play 2.3.8
* SecureSocial 2.11-master-SNAPSHOT

SecureSocial's assets from [securesocial_2.11-3.0-M3-assets.jar](http://search.maven.org/remotecontent?filepath=ws/securesocial/securesocial_2.11/3.0-M3/securesocial_2.11-3.0-M3-assets.jar) have been extracted to *public/* because there seems to be an [issue with resource packaging with Play 2.3 and SecureSocial 3.0](https://groups.google.com/d/msg/securesocial/_1umOEL_uNA/aA9uk8tTW0cJ)

##Running##

1. Add secrets to *securesocial.conf* and review providers in *MyEnvironment.scala*.
3. Run ```activator run```


