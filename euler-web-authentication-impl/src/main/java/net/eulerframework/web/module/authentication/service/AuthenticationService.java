package net.eulerframework.web.module.authentication.service;

import javax.annotation.Resource;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.eulerframework.common.util.Assert;
import net.eulerframework.common.util.BeanTool;
import net.eulerframework.common.util.StringTool;
import net.eulerframework.web.config.WebConfig;
import net.eulerframework.web.core.base.service.impl.BaseService;
import net.eulerframework.web.core.exception.BadRequestException;
import net.eulerframework.web.core.i18n.Tag;
import net.eulerframework.web.module.authentication.dao.IUserDao;
import net.eulerframework.web.module.authentication.dao.IUserProfileDao;
import net.eulerframework.web.module.authentication.entity.IUserProfile;
import net.eulerframework.web.module.authentication.entity.User;
import net.eulerframework.web.module.authentication.exception.UserSignUpException;

@Service
@Transactional
public class AuthenticationService extends BaseService implements IAuthenticationService {

    @Resource private IUserDao userDao;
    @Resource private IUserProfileDao<IUserProfile> userProfileDao;
    @Resource private PasswordEncoder passwordEncoder;
    
    @Override
    public String signUp(User user) throws UserSignUpException {
        
        try{
            BeanTool.clearEmptyProperty(user);

            String password;
            
            try {
                Assert.isNotNull(user.getUsername(), BadRequestException.class, Tag.i18n("Username is null"));
                Assert.isNotNull(user.getEmail(), BadRequestException.class, Tag.i18n("Email is null"));
                Assert.isNotNull(user.getPassword(), BadRequestException.class, Tag.i18n("Password is null"));
                //Assert.isNotNull(user.getMobile(), BadRequestException.class, Tag.i18n("Mobile is null"));
                Assert.isTrue(user.getUsername().matches(WebConfig.getUsernameFormat()), BadRequestException.class, Tag.i18n("The username format does not meet the requirements"));
                Assert.isTrue(user.getEmail().matches(WebConfig.getEmailFormat()), BadRequestException.class, Tag.i18n("The email format does not meet the requirements"));
                password = user.getPassword().trim();
                Assert.isTrue(password.matches(WebConfig.getPasswordFormat()), BadRequestException.class, Tag.i18n("The password format does not meet the requirements"));
                Assert.isTrue(password.length() >= WebConfig.getMinPasswordLength() && password.length() <= 20, BadRequestException.class, Tag.i18n("The password length does not meet the requirements"));
            } catch (BadRequestException e) {
                throw new UserSignUpException(e.getMessage(), e);
            }
            
            User existUser = this.userDao.findUserByName(user.getUsername());
            
            if(existUser != null)
                throw new UserSignUpException(Tag.i18n("Username han been used"));  
            
            existUser = this.userDao.findUserByEmail(user.getEmail());
            
            if(existUser != null)
                throw new UserSignUpException(Tag.i18n("Email han been used"));          

            if(user.getMobile() != null) {
                existUser = this.userDao.findUserByMobile(user.getMobile());
                
                if(existUser != null)
                    throw new UserSignUpException(Tag.i18n("Mobile han been used"));
            }
            
            user.setId(null);
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setEnabled(true);
            
            user.setPassword(this.passwordEncoder.encode(password));
            
            user.setResetToken(null);
            user.setResetTokenExpireTime(null);
            
            return (String) this.userDao.save(user);
        } catch (UserSignUpException userSignUpException) {
            throw userSignUpException;
        } catch (Exception e) {
            throw new UserSignUpException(Tag.i18n("Unknown user sign up error"), e);
        }
    }

    @Override
    public <T extends IUserProfile> String signUp(User user, T userProfile) throws UserSignUpException {
        
        try{
            String userId = this.signUp(user);
            
            if(StringTool.isNull(userId))
                throw new Exception();
            
            userProfile.setUserId(userId);
            this.userProfileDao.save(userProfile);
            
            return userId;
        } catch (UserSignUpException userSignUpException) {
            throw userSignUpException;
        } catch (Exception e) {
            throw new UserSignUpException(Tag.i18n("Unknown user sign up error"), e);
        }
    }

    @Override
    public void passwdResetEmailGen(String email) {
        // TODO Auto-generated method stub

    }

    @Override
    public void passwdResetSMSGen(String email) {
        // TODO Auto-generated method stub

    }

    @Override
    public User findUser(String passwordResetToken) {
        // TODO Auto-generated method stub
        return null;
    }

}