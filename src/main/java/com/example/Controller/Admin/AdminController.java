package com.example.Controller.Admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Entity.Admin;
import com.example.Entity.Order;
import com.example.Entity.Recharge;
import com.example.Entity.User;
import com.example.Service.AdminService;
import com.example.Service.JwtUtil;
import com.example.Service.OrderService;
import com.example.Service.RechargeService;
import com.example.Service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	AdminService adminService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	RechargeService rechargeService;
	
	@Autowired
	OrderService orderService;

	@GetMapping("/index")
	public String index(@CookieValue(value = "auth_token", defaultValue = "") String authToken, Model model, RedirectAttributes redirectAttributes) {
		String username = JwtUtil.validateToken(authToken);
		
		if(username != null) {
			Admin loggedInAdmin = adminService.findByUsername(username);
			System.out.println(loggedInAdmin.getUsername());
			if(loggedInAdmin != null) {
				model.addAttribute("loggedInAdmin", loggedInAdmin);
				
				
				List<Admin> admins = adminService.findAll();
				int numberOfAdmin = admins.size();
				model.addAttribute("numberOfAdmin", numberOfAdmin);
				
				List<User> users = userService.findAll();
				int numberOfUser = users.size();
				model.addAttribute("numberOfUser", numberOfUser);
				
				int sumAmount = users.stream()
                        .mapToInt(User::getDeposited)
                        .sum();
				model.addAttribute("sumAmount", sumAmount);
				
				List<Order> orders = orderService.findAll();
				int numberOfOrder = orders.size();
				model.addAttribute("numberOfOrder", numberOfOrder);
			}
		}else {
			return "redirect:/admin/login";
		}
		
		return "Admin/index";
	}
	
	@GetMapping("/login")
	public String login() {
		return "Admin/login";
	}
	
	@PostMapping("/login")
	public String login(@RequestParam("username") String username, @RequestParam("password") String password,  @RequestParam(value = "rememberMe", required = false) Boolean rememberMe, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		
		Admin admin = adminService.findByUsername(username);
		
		if(admin != null) {
			if(admin.getPassword().equals(password)) {
				String token = JwtUtil.genarateToken(username, rememberMe != null && rememberMe);
				
				Cookie cookie = new Cookie("auth_token", token);
				cookie.setHttpOnly(true);
				cookie.setSecure(true);
				cookie.setPath("/");
				
				if(rememberMe != null && rememberMe) {
					cookie.setMaxAge(864000);
				}else {
					cookie.setMaxAge(-1);
				}
				
				response.addCookie(cookie);
				redirectAttributes.addFlashAttribute("success", "Đăng nhập thành công!");
				return "redirect:/admin/index";
			}else {
				redirectAttributes.addFlashAttribute("danger", "Sai mật khẩu!");
				return "redirect:/admin/login";
			}
		}else {
			redirectAttributes.addFlashAttribute("danger", "Không tìm thấy tài khoản!");
			return "redirect:/admin/login";
		}
		
	}
	
	@PostMapping("/logout")
	public String logout(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		Cookie cookie = new Cookie("auth_token", null);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		
		redirectAttributes.addFlashAttribute("success", "Đăng xuất thành công!");
		return "redirect:/admin/login";
		
	}
	
}
