package com.example.Controller.User;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Entity.User;
import com.example.Service.EmailConfigService;
import com.example.Service.JwtUtil;
import com.example.Service.TokenService;
import com.example.Service.UserService;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class LoginController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	EmailConfigService emailConfigService;
	
	@Autowired
	TokenService tokenService;

	@GetMapping("/login")
	public String login() {
		return "User/login";
	}
	
	@GetMapping("/register")
	public String Register() {
		return "User/register";
	}
	
	@PostMapping("/login")
	public String login(@RequestParam("username") String username, @RequestParam("password") String password,  @RequestParam(value = "rememberMe", required = false) Boolean rememberMe, HttpServletResponse response, RedirectAttributes redirectAttributes)  {
		User user = userService.findByUsername(username);
		
		if(user != null) {
			if(user.getPassword().equals(password)){
String token = JwtUtil.genarateToken(username, rememberMe != null && rememberMe);
				
				Cookie cookie = new Cookie("user_auth_token", token);
				cookie.setHttpOnly(true);
				cookie.setSecure(true);
				cookie.setPath("/");
				
				if(rememberMe != null && rememberMe) {
					cookie.setMaxAge(864000);
				}else {
					cookie.setMaxAge(-1);
				}
				response.addCookie(cookie);
				
				return "redirect:/index";
			}else {
				redirectAttributes.addFlashAttribute("danger", "Mật khẩu không khớp!");
				return "redirect:/login";
			}
			
		}else {
			redirectAttributes.addFlashAttribute("danger", "Không tìm thấy tên người dùng!");
			return "redirect:/login";
		}
		
	}

	@PostMapping("/register")
	public String register(@RequestParam("username") String username, @RequestParam("email") String email, @RequestParam("password") String password, RedirectAttributes redirectAttributes) {
		User existingUsername = userService.findByUsername(username);
		User existingEmail = userService.findByEmail(email);
		
		if(existingUsername != null) {
			redirectAttributes.addFlashAttribute("danger", "Tên người dùng đã tồn tại!");
			return "redirect:/register"; 
		}
		
		if(existingEmail != null) {
			redirectAttributes.addFlashAttribute("danger", "Email đã được sử dụng cho tài khoản khác!");
			return "redirect:/register"; 
		}
		
		User user = new User();
		user.setUsername(username);
		user.setEmail(email);
		user.setAmount(0);
		user.setPassword(password);
		user.setRole("Nhà quảng cáo");
		Date currentDate = new Date();
		user.setUpdateDate(currentDate);
		userService.save(user);
		redirectAttributes.addFlashAttribute("success", "Đăng ký thành công!");
		return "redirect:/login";
	}
	

	@PostMapping("/logout")
	public  String logout(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		Cookie cookie = new Cookie("user_auth_token", null);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		
		response.addCookie(cookie);
		
		redirectAttributes.addFlashAttribute("success", "Đăng xuất thành công!");
		return "redirect:/login";
	}
	
	
	@GetMapping("/forgot-password")
	public String forgotPassword() {
		return "User/forgot-password";
	}
	
	@PostMapping("/forgot-password")
	public String forgotPassword(@RequestParam("username") String username,  RedirectAttributes redirectAttributes, Model model) {
		User user = userService.findByUsername(username);
		if(user == null) {
			redirectAttributes.addFlashAttribute("danger", "Không tìm thấy tài khoản!");
			return "redirect:/forgot-password";
		}
		
		String token = tokenService.generateSixDigitToken();
		user.setToken(token);
		userService.save(user);
		
		String email = user.getEmail();
        
        String subject = "Xác thực tài khoản";
        String text = "Mã token của bạn là: " + token;
        try {
        	emailConfigService.sendEmail(email, subject, text);
        	model.addAttribute("success", "Đã gửi mã token đến email của bạn vui lòng kiểm tra!");
        	model.addAttribute("username", user.getUsername());
        	return "User/token";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Chức năng này hiện không hoạt động!");
			return "redirect:/index";
		}
        
       
	}
	
	@PostMapping("/confirm-token")
	public String confirmToken(@RequestParam("username") String username, @RequestParam("token") String token, RedirectAttributes redirectAttributes, Model model) {
		User user = userService.findByUsername(username);
		if(user != null) {
			if(user.getToken().equals(token)) {
				model.addAttribute("success", "Xác nhận thành công!");
				model.addAttribute("username", user.getUsername());
				return "User/change-password";
			}else {
				redirectAttributes.addFlashAttribute("danger", "Mã token không chính xác!");
				return "redirect:/forgot-password";
			}
			
		}
		return "redirect:/login";

	}
	
	@PostMapping("/change-password")
	public String changePass(@RequestParam("username") String username, @RequestParam("password") String password, RedirectAttributes redirectAttributes) {
		User user = userService.findByUsername(username);
		if(username != null) {
			user.setPassword(password);
			userService.save(user);
			redirectAttributes.addFlashAttribute("success", "Cập nhật mật khẩu thành công!");
			return "redirect:/login";
		}
		return "redirect:/login";
		
	}
	
	@GetMapping("/token")
	public String token() {
		return "User/token";
	}
}
