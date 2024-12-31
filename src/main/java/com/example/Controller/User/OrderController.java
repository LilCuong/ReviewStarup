package com.example.Controller.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Entity.Bank;
import com.example.Entity.Information;
import com.example.Entity.Order;
import com.example.Entity.OrderImage;
import com.example.Entity.Recharge;
import com.example.Entity.Transaction;
import com.example.Entity.User;
import com.example.Service.InformationService;
import com.example.Service.JwtUtil;
import com.example.Service.OrderImageService;
import com.example.Service.OrderService;
import com.example.Service.TransactionService;
import com.example.Service.UserService;

@Controller
public class OrderController {

	@Autowired
	UserService userService;
	
	@Autowired
	InformationService informationService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	OrderImageService orderImageService;
	
	@Autowired
	TransactionService transactionService;
	
	@GetMapping("/order")
	public String recharge(@CookieValue(value = "user_auth_token", defaultValue = "") String authToken, Model model, RedirectAttributes redirectAttributes) {		
		
		Optional<Information> information = informationService.findById(1);
		model.addAttribute("info", information.get());

		String username = JwtUtil.validateToken(authToken);
		if(username != null) {
			User user = userService.findByUsername(username);
			if(user != null) {
				model.addAttribute("user", user);
				
			List<Order> orders = orderService.findByUserId(user.getId());
			model.addAttribute("orders", orders);
			
			List<Transaction> transactions = transactionService.findByUserId(user.getId());
			List<Transaction> filteredTransactions = new ArrayList<>();
			for (Transaction transaction : transactions) {
			    if (transaction.getOrder() != null) {
			        filteredTransactions.add(transaction);
			    }
			}
			model.addAttribute("transactions", filteredTransactions);
			
			Map<Integer, List<OrderImage>> orderImagesMap = new HashMap<>();
			for(Order order : orders) {
				List<OrderImage> orderImages = orderImageService.findByOrderId(order.getId());
				orderImagesMap.put(order.getId(), orderImages);
			}
			model.addAttribute("orderImagesMap", orderImagesMap);
			}
			
		}else {
			redirectAttributes.addFlashAttribute("success", "Bạn cần phải đăng nhập!");
			return "redirect:/login";
		}
		
		boolean isLoggedIn = username != null;
		model.addAttribute("isLoggedIn", isLoggedIn);
		return "User/order";
	}
}
