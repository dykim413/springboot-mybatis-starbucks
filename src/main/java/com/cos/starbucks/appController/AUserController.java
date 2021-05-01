package com.cos.starbucks.appController;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cos.starbucks.model.User;
import com.cos.starbucks.repository.UserRepository;
import com.cos.starbucks.security.MyUserDetailsService;

@RestController
@RequestMapping("/android")
public class AUserController {

    private static final Logger logger = LoggerFactory.getLogger(AUserController.class);

	private final UserRepository uRepo;

	private final BCryptPasswordEncoder passwordEncoder;

	private final MyUserDetailsService mMyUserDetailsService;

	@Autowired
	public AUserController( UserRepository uRepo, BCryptPasswordEncoder passwordEncoder, MyUserDetailsService mMyUserDetailsService) {
	    this.uRepo = uRepo;
	    this.passwordEncoder = passwordEncoder;
	    this.mMyUserDetailsService = mMyUserDetailsService;
    }
	
	@PostMapping("/joinProc")
	public String create(User user) {
		String rawPassword = user.getPassword();
		String encPassword = passwordEncoder.encode(rawPassword);
		user.setPassword(encPassword);

		try {
            uRepo.join(user);
        } catch (SQLException e) {
            System.out.println("james error code : " + e.getErrorCode());
        } catch (Exception e) {
		    if( e.getMessage().contains("Duplicate entry") ) {
		        return "exists";
            }
			return "fail";
		}

		return "success";
	}
	
	@PostMapping("/kakaoJoinProc")
	public String kCreate(User user) {

		try {
			uRepo.join(user);	
		} catch (Exception e) {
			return "join fail";
		}
		
		return "join success";
	}
	
	@PostMapping("/kakaoSubscribeCheck")
	public User KLogin(User user, HttpSession session) {
		
		user = uRepo.findByProviderAndProviderId("kakao", user.getProviderId());
		
		if(user==null) {
			//미가입
			return new User();
			
		}else {
			//가입
			UserDetails userDetail =
	        		 mMyUserDetailsService.loadUserByUsername(user.getUsername());
	         Authentication authentication = 
	                new UsernamePasswordAuthenticationToken(userDetail, userDetail.getPassword(), userDetail.getAuthorities());
	          
	          SecurityContext securityContext = SecurityContextHolder.getContext();
	          
	          securityContext.setAuthentication(authentication);
	          
	          session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
	          	          
			return user;
		}
	}
	

	
}
