package com.example.Controller.Admin;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Entity.Admin;
import com.example.Entity.Order;
import com.example.Entity.Recharge;
import com.example.Entity.Transaction;
import com.example.Entity.User;
import com.example.Service.AdminService;
import com.example.Service.JwtUtil;
import com.example.Service.OrderService;
import com.example.Service.RechargeService;
import com.example.Service.TransactionService;
import com.example.Service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/admin")
public class UserManagementController {
	
	@Autowired
	AdminService adminService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	RechargeService rechargeService;
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	OrderService orderService;
	
	
	

	@GetMapping("/user")
	public String admin(@CookieValue(value = "auth_token", defaultValue = "") String authToken, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
		
		String username = JwtUtil.validateToken(authToken);
		if(username != null) {
			Admin loggedInAdmin = adminService.findByUsername(username);
			if(loggedInAdmin != null) {
				model.addAttribute("loggedInAdmin", loggedInAdmin);
				
				List<User> users = userService.findAll();
				model.addAttribute("users", users);
				User user = new User();
				model.addAttribute("user", user);
				
				return "Admin/user";
			}
		}else {
			redirectAttributes.addFlashAttribute("danger", "Bạn cần phải đăng nhập!");
			return "redirect:/admin/login";
		}
		
		return "Admin/admin";
	}
	
	
	@GetMapping("/user/{id}")
	public String update(@CookieValue(value = "auth_token", defaultValue = "") String authToken, Model model, @PathVariable int id, HttpServletRequest request) {
		String username = JwtUtil.validateToken(authToken);
		if(username != null) {
			Admin loggedInAdmin = adminService.findByUsername(username);
			if(loggedInAdmin != null) {
				model.addAttribute("loggedInAdmin", loggedInAdmin);
			
			Optional<User> userGet = userService.findById(id);
			
			List<Recharge> recharges = rechargeService.findByUserId(id);
			model.addAttribute("recharges", recharges);
			
			List<Transaction> transactions = transactionService.findByUserId(userGet.get().getId());
			model.addAttribute("transactions", transactions);
			
			List<Transaction> filteredTransactions = new ArrayList<>();
			for(Transaction transaction : transactions) {
				if(transaction.getRecharge() != null) {
					filteredTransactions.add(transaction);
				}
			}
			model.addAttribute("filteredTransactions", filteredTransactions);
			User user = new User();
			if (userGet.isPresent()) {
				user = userGet.get();
			}
			model.addAttribute("user", user);
			
			return "Admin/user-detail";
		}else {
			return "redirect:/admin/login";
		}
		
	}
		return "redirect:/admin/login";
	}
	
	
	
	
	@PostMapping("/user/save")
	public String save(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
		User existingUser = userService.findByUsername(user.getUsername());
		try {
			if(existingUser != null && existingUser.getId() != user.getId()) {
				 redirectAttributes.addFlashAttribute("danger", "Tên tài khoản này đã được sử dụng!");
		            return "redirect:/admin/user";
			}
			Date currentDate = new Date();
			
			
	            user.setUpdateDate(currentDate);
	        
			System.out.println(2);
			userService.save(user);
			redirectAttributes.addFlashAttribute("success", "Lưu thành công!");
			 return "redirect:/admin/user";
		}catch (Exception e) {
			 redirectAttributes.addFlashAttribute("danger", "Không thể lưu người dùng!");
	            return "redirect:/admin/user";
		}
	}
	
	@GetMapping("/user/delete/{id}")
	public String delete(@PathVariable int id, RedirectAttributes redirectAttributes) {
		try {
			List<Transaction> transactions = transactionService.findByUserId(id);
			for(Transaction transaction : transactions) {
				transactionService.delete(transaction);
			}
			
			List<Order> orders = orderService.findByUserId(id);
			for(Order order : orders) {
				orderService.delete(order);
			}
			List<Recharge> recharges = rechargeService.findByUserId(id);
			for(Recharge recharge : recharges) {
				rechargeService.delete(recharge);
			}
			
			
			userService.deleteById(id);
			redirectAttributes.addFlashAttribute("success", "Xóa thành công!");
			return "redirect:/admin/user";
		}catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Xóa thất bại!");
			return "redirect:/admin/user";
		}
		
	}
	
	@PostMapping("/user-detail/save")
	public String updateUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
		User existingUser = userService.findByUsername(user.getUsername());
		Date currentDate = new Date();
        user.setUpdateDate(currentDate);
		try {
			
			if(existingUser != null && existingUser.getId() != user.getId()) {
				redirectAttributes.addFlashAttribute("danger", "Lỗi trùng lặp tên đăng nhập!");
				return"redirect:/admin/user/" + user.getId();
			}
			
	        System.out.println(user.getUsername());
			
			userService.save(user);
			redirectAttributes.addFlashAttribute("success", "Lưu thành công!");
			return "redirect:/admin/user/" + user.getId();
		}catch (Exception e) {
			e.printStackTrace(); 
			redirectAttributes.addFlashAttribute("danger", "Lưu thất bại!");
			return "redirect:/admin/user/" + user.getId();
		}
	}
}
