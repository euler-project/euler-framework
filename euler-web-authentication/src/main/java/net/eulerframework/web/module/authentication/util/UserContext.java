package net.eulerframework.web.module.authentication.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import net.eulerframework.cache.inMemoryCache.DefaultObjectCache;
import net.eulerframework.cache.inMemoryCache.ObjectCachePool;
import net.eulerframework.web.config.WebConfig;
import net.eulerframework.web.module.authentication.entity.User;
import net.eulerframework.web.module.authentication.service.UserService;

public class UserContext {
    private static final String oauthUserPrefix = "__OAUTH_";
    private static UserService userService;

    public static void setUserService(UserService userService) {
        UserContext.userService = userService;
    }

    private final static DefaultObjectCache<String, User> USER_CACHE = ObjectCachePool.generateDefaultObjectCache(WebConfig.getUserContextCacheLife());

    private final static DefaultObjectCache<Serializable, User> USER_CACHE_ID = ObjectCachePool.generateDefaultObjectCache(WebConfig.getUserContextCacheLife());
    
    public static List<User> getUserByNameOrCode(String nameOrCode) {
        return userService.loadUserByNameOrCodeFuzzy(nameOrCode);
    }
    
    public static List<Serializable> getUserIdByNameOrCode(String nameOrCode) {
        List<User> users = getUserByNameOrCode(nameOrCode);
        List<Serializable> result = new ArrayList<>();
        for(User user : users){
            result.add(user.getId());
        }
        return result;
    }
    
    public static User getUserById(String userId){
        User user = USER_CACHE_ID.get(userId);
        
        if(user == null) {
            user = userService.loadUser(userId);
            
            if(user == null)
                return null;
            
            USER_CACHE_ID.put(user.getId(), user);
        }
        
        return user;        
    }

    public static String getUserNameAndCodeById(String id) {
        User user = getUserById(id);
        if(user == null)
            return "UNKOWN-"+id;
        return user.getFullName()+"("+user.getUsername()+")";
    }

    public static User getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();

        if (context == null)
            return User.ANONYMOUS_USER;

        Authentication authentication = context.getAuthentication();

        if (authentication == null)
            return User.ANONYMOUS_USER;

        if (OAuth2Authentication.class.isAssignableFrom(authentication.getClass())) {
            OAuth2Authentication oauth2Authentication = (OAuth2Authentication) authentication;
            authentication = oauth2Authentication.getUserAuthentication();

            if (authentication == null) {
                String clientId = (String) oauth2Authentication.getPrincipal();
                String username = oauthUserPrefix + clientId;

                User user = USER_CACHE.get(username);
                if (user != null) {
                    return user;
                }

                user = new User();
                user.setId(username);
                user.setUsername(username);

                USER_CACHE.put(user.getUsername(), user);

                return user;
            }
        }

        Object principal = context.getAuthentication().getPrincipal();

        if (User.class.isAssignableFrom(principal.getClass())) {
            User user = (User) principal;
            USER_CACHE.put(user.getUsername(), user);
            return user;
        }

        if (UserDetails.class.isAssignableFrom(principal.getClass())) {
            UserDetails userDetails = (UserDetails) principal;
            principal = userDetails.getUsername();
        }

        if (String.class.isAssignableFrom(principal.getClass())
                && (!User.ANONYMOUS_USERNAME.equalsIgnoreCase((String) principal))) {

            String username = (String) principal;
            User user = USER_CACHE.get(username);

            if (user != null) {
                return user;
            }

            UserDetails userDetails = userService.loadUserByUsername(username);
            
            if(userDetails == null)
                return User.ANONYMOUS_USER;
            
            if (userDetails.getClass().isAssignableFrom(CredentialsContainer.class)) {
                ((CredentialsContainer) userDetails).eraseCredentials();
            }

            if (User.class.isAssignableFrom(userDetails.getClass())) {
                user = (User) userDetails;
            } else {
                user = new User();
                user.loadDataFromOtherUserDetails(userDetails);
            }

            USER_CACHE.put(user.getUsername(), user);

            return user;
        } else {
            return User.ANONYMOUS_USER;
        }
    }
    
    public static void sudo() {
        UsernamePasswordAuthenticationToken systemToken = new UsernamePasswordAuthenticationToken(
                User.ROOT_USER, null,
                User.ROOT_USER.getAuthorities());
        systemToken.setDetails(User.ROOT_USER);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(systemToken);
    }
}
