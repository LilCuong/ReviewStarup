package com.example.Controller.User;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Service.InformationService;
import com.example.Service.JwtUtil;
import com.example.Service.OrderService;
import com.example.Service.TransactionService;
import com.example.Service.UserService;
import com.example.Service.ValueService;
import com.example.Entity.Information;
import com.example.Entity.Order;
import com.example.Entity.Transaction;
import com.example.Entity.User;
import com.example.Entity.Value;
import com.example.Repository.ValueRepository;

@Controller
public class HomeController {

	@Autowired
	UserService userService;
	
	@Autowired
	OrderService orderService;

	@Autowired
	ValueService valueService;
	
	@Autowired 
	TransactionService transactionService;
	
	@Autowired
	InformationService informationService;

	@GetMapping({"/index", "", "/"})
	public String Home(@CookieValue(value = "user_auth_token", defaultValue = "") String authToken, Model model) {
		Optional<Value> value = valueService.findId1();
		model.addAttribute("amount", value.get().getAmount());
		Optional<Information> information = informationService.findById(1);
		model.addAttribute("info", information.get());
		
		String username = JwtUtil.validateToken(authToken);
		if (username != null) {
			User user = userService.findByUsername(username);
			if (user != null) {
				model.addAttribute("user", user);
				
			}
		}
		else {
			
			return "redirect:/login";
		}
		boolean isLoggedIn = username != null;
		model.addAttribute("isLoggedIn", isLoggedIn);
		return "User/index";
	}

	@PostMapping("/order")
	public String order(@CookieValue(value = "user_auth_token", defaultValue = "") String authToken, @RequestParam("mapsLink") String mapsLink, @RequestParam("description") String description, @RequestParam("quantity") int quantity, @RequestParam("note") String note, RedirectAttributes redirectAttributes, Model model) {
		Optional<Value> value = valueService.findId1();
		model.addAttribute("amount", value.get().getAmount());
		
		String loggedInUser = JwtUtil.validateToken(authToken);
		User user = userService.findByUsername(loggedInUser);
		
		int orderAmount = quantity * value.get().getAmount();
		
		if(loggedInUser == null) {
			redirectAttributes.addFlashAttribute("error", "Bạn cần phải đăng nhập để tạo đơn!");
			return "redirect:/index";
		}if(user.getAmount() < orderAmount) {
			System.out.println(user.getAmount() + orderAmount);
			redirectAttributes.addFlashAttribute("danger", "Số dư tài khoản không đủ!");
			return "redirect:/index";
		}
			
			Date currentDate = new Date();
			Order order = new Order();
			order.setMapLink(mapsLink);
			order.setDescription(description);
			order.setQuantity(quantity);
			order.setUser(user);
			order.setCreationDate(currentDate);
			order.setNote(note);
			order.setStatus("Đang chờ");
			
			order.setUnitPrice(quantity * value.get().getAmount());
			orderService.save(order);
			
			
			
			
			
			
			
			
			Transaction transaction = new Transaction();
			transaction.setDetail("Đơn maps");
			transaction.setDepositAmount(quantity * value.get().getAmount());
			transaction.setDepositDate(currentDate);
			transaction.setOrder(order);
			transaction.setUser(user);
			transaction.setPrevBalance(user.getAmount());
			transaction.setAfterBalance(user.getAmount() - quantity * value.get().getAmount());
			transactionService.save(transaction);
			
			user.setAmount(user.getAmount() - quantity * value.get().getAmount());
			userService.save(user);
		
		redirectAttributes.addFlashAttribute("success", "Tạo đơn thành công!");
		return "redirect:/index";
	}
}
