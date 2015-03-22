package services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.libs.F;
import securesocial.core.BasicProfile;
import securesocial.core.PasswordInfo;
import securesocial.core.services.SaveMode;
import securesocial.core.java.BaseUserService;
import securesocial.core.java.Token;
import securesocial.core.providers.UsernamePasswordProvider;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MyUserService extends BaseUserService<User> {
	private static Logger logger = LoggerFactory.getLogger("userservice");
	private HashMap<String, User> users = new HashMap<String, User>();
	private HashMap<String, Token> tokens = new HashMap<String, Token>();
	@Override
	public F.Promise<User> doSave(BasicProfile profile, SaveMode mode) {
		User result = null;
		if (mode == SaveMode.SignUp()) {
			result = new User(profile);
			users.put(profile.providerId() + profile.userId(), result);
		} else if (mode == SaveMode.LoggedIn()) {
			for (Iterator<User> it = users.values().iterator() ; it.hasNext() && result == null ; ) {
				User user = it.next();
				for ( BasicProfile p : user.identities) {
					if ( p.userId().equals(profile.userId()) && p.providerId().equals(profile.providerId())) {
						user.identities.remove(p);
						user.identities.add(profile);
						result = user;
						break;
					}
				}
			}
		} else if (mode == SaveMode.PasswordChange()) {
			for (Iterator<User> it = users.values().iterator() ; it.hasNext() && result == null ; ) {
				User user = it.next();
				for (BasicProfile p : user.identities) {
					if (p.userId().equals(profile.userId()) && p.providerId().equals(UsernamePasswordProvider.UsernamePassword())) {
						user.identities.remove(p);
						user.identities.add(profile);
						result = user;
						break;
					}
				}
			}
		} else {
			throw new RuntimeException("Unknown mode");
		}
		return F.Promise.pure(result);
	}
	@Override
	public F.Promise<User> doLink(User current, BasicProfile to) {
		User target = null;
		for ( User u: users.values() ) {
			if ( u.main.providerId().equals(current.main.providerId()) && u.main.userId().equals(current.main.userId()) ) {
				target = u;
				break;
			}
		}
		if ( target == null ) {
			// this should not happen
			throw new RuntimeException("Can't find user : " + current.main.userId());
		}
		boolean alreadyLinked = false;
		for ( BasicProfile p : target.identities) {
			if ( p.userId().equals(to.userId()) && p.providerId().equals(to.providerId())) {
				alreadyLinked = true;
				break;
			}
		}
		if (!alreadyLinked) target.identities.add(to);
		return F.Promise.pure(target);
	}
	@Override
	public F.Promise<Token> doSaveToken(Token token) {
		tokens.put(token.uuid, token);
		return F.Promise.pure(token);
	}
	@Override
	public F.Promise<BasicProfile> doFind(String providerId, String userId) {
		if(logger.isDebugEnabled()){
			logger.debug("Finding user {}", userId);
		}
		BasicProfile found = null;
		for ( User u: users.values() ) {
			for ( BasicProfile i : u.identities ) {
				if ( i.providerId().equals(providerId) && i.userId().equals(userId) ) {
					found = i;
					break;
				}
			}
		}
		return F.Promise.pure(found);
	}
	@Override
	public F.Promise<PasswordInfo> doPasswordInfoFor(User user) {
		throw new RuntimeException("doPasswordInfoFor is not implemented yet in sample app");
	}
	@Override
	public F.Promise<BasicProfile> doUpdatePasswordInfo(User user, PasswordInfo info) {
		throw new RuntimeException("doUpdatePasswordInfo is not implemented yet in sample app");
	}
	@Override
	public F.Promise<Token> doFindToken(String tokenId) {
		return F.Promise.pure(tokens.get(tokenId));
	}
	@Override
	public F.Promise<BasicProfile> doFindByEmailAndProvider(String email, String providerId) {
		BasicProfile found = null;
		for ( User u: users.values() ) {
			for ( BasicProfile i : u.identities ) {
				if ( i.providerId().equals(providerId) && i.email().isDefined() && i.email().get().equals(email) ) {
					found = i;
					break;
				}
			}
		}
		return F.Promise.pure(found);
	}
	@Override
	public F.Promise<Token> doDeleteToken(String uuid) {
		return F.Promise.pure(tokens.remove(uuid));
	}
	@Override
	public void doDeleteExpiredTokens() {
		Iterator<Map.Entry<String,Token>> iterator = tokens.entrySet().iterator();
		while ( iterator.hasNext() ) {
			Map.Entry<String, Token> entry = iterator.next();
			if ( entry.getValue().isExpired() ) {
				iterator.remove();
			}
		}
	}
}
