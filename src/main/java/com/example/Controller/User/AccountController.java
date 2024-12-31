package com.example.Controller.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Entity.Information;
import com.example.Entity.Transaction;
import com.example.Entity.User;
import com.example.Service.InformationService;
import com.example.Service.JwtUtil;
import com.example.Service.TransactionService;
import com.example.Service.UserService;

@Controller
public class AccountController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	TransactionService transactionService;

	@Autowired
	InformationService informationService;
	
	
	
	@GetMapping("/account")
	public String Account(@CookieValue(value = "user_auth_token", defaultValue = "") String authToken, Model model, RedirectAttributes redirectAttributes) {
		String username = JwtUtil.validateToken(authToken);
		Optional<Information> information = informationService.findById(1);
		model.addAttribute("info", information.get());
		
		if(username != null) {
			User user = userService.findByUsername(username);
			if(user != null) {
				model.addAttribute("user", user);
				
				List<Transaction> transactions = transactionService.findByUserId(user.getId());
				model.addAttribute("transactions", transactions);
			}
			
		}else {
			redirectAttributes.addFlashAttribute("success", "Bạn cần phải đăng nhập!");
			return "redirect:/login";
		}
		
		boolean isLoggedIn = username != null;
		model.addAttribute("isLoggedIn", isLoggedIn);
		return "User/account";
	}
	
	@PostMapping("/change-pass")
	public String changePass(@RequestParam("password") String password, @RequestParam("new-password") String newPassword, @RequestParam("userId") int userId, RedirectAttributes redirectAttributes) {
		Optional<User> user = userService.findById(userId);
		if(user.isPresent()) {
			if(user.get().getPassword().equals(password)) {
				user.get().setPassword(newPassword);
				Date currentDate = new Date();
				user.get().setUpdateDate(currentDate);
				userService.save(user.get());
				redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công!");
				return "redirect:/account";
			}else {
				redirectAttributes.addFlashAttribute("danger", "Mật khẩu không chính xác!");
				return "redirect:/account";
			}
		}
		redirectAttributes.addFlashAttribute("danger", "Không thể thay đổi mật khẩu!");
		return "redirect:/account";
	}
}
