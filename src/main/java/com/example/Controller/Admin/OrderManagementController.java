package com.example.Controller.Admin;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Entity.Admin;
import com.example.Entity.Bank;
import com.example.Entity.Order;
import com.example.Entity.OrderImage;
import com.example.Entity.Recharge;
import com.example.Entity.Transaction;
import com.example.Entity.User;
import com.example.Service.AdminService;
import com.example.Service.JwtUtil;
import com.example.Service.OrderImageService;
import com.example.Service.OrderService;
import com.example.Service.TransactionService;
import com.example.Service.UserService;

@Controller
@RequestMapping("/admin")
public class OrderManagementController {
	
	@Autowired
	AdminService adminService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	OrderImageService orderImageService;

	@GetMapping("/order")
	public String order(@CookieValue(value = "auth_token", defaultValue = "") String authToken, Model model, RedirectAttributes redirectAttributes) {
String username = JwtUtil.validateToken(authToken);
		
		if(username != null) {
			Admin loggedInAdmin = adminService.findByUsername(username);
			if(loggedInAdmin != null) {
				model.addAttribute("loggedInAdmin", loggedInAdmin);
			}
			
			List<Order> orders = orderService.findAll();
			model.addAttribute("orders", orders);
			
			Map<Integer, List<OrderImage>> orderImagesMap = new HashMap<>();
			for(Order order : orders) {
				List<OrderImage> orderImages = orderImageService.findByOrderId(order.getId());
				orderImagesMap.put(order.getId(), orderImages);
			}
			model.addAttribute("orderImagesMap", orderImagesMap);
			
			List<Transaction> transactions = transactionService.findAll();
			List<Transaction> filteredTransactions = new ArrayList<>();
			for(Transaction transaction : transactions) {
				if(transaction.getOrder() != null) {
					filteredTransactions.add(transaction);
				}
			}
			model.addAttribute("filteredTransactions", filteredTransactions);
		}else {
			return "redirect:/admin/login";
		}
		return "Admin/order"; 
	}
	
	@GetMapping("/order/done/{id}")
	public String done(@PathVariable int id, RedirectAttributes redirectAttributes) {
		try {
			Optional<Order> order = orderService.findById(id);
			Date currentDate = new Date();
			order.get().setCompletionDate(currentDate);
			order.get().setStatus("Hoàn thành");
			orderService.save(order.get());
			redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
			return "redirect:/admin/order";
		}catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Cập nhật thất bại!");
			return "redirect:/admin/order";
		}
	}
	
	@GetMapping("/order/cancel/{id}")
	public String cancel(@PathVariable int id, RedirectAttributes redirectAttributes) {
		try {
			Optional<Order> order = orderService.findById(id);
			Date currentDate = new Date();
			order.get().setCompletionDate(currentDate);
			order.get().setStatus("Đã hủy");
			orderService.save(order.get());
			
			Optional<User> user = userService.findById(order.get().getUser().getId());
			user.get().setAmount(user.get().getAmount() + order.get().getUnitPrice());
			userService.save(user.get());
			
			Optional<Transaction> transaction = transactionService.findByOrderId(id);
			
			transaction.get().setAfterBalance(transaction.get().getAfterBalance() + transaction.get().getDepositAmount());
			transaction.get().setDepositAmount(0);
			transactionService.save(transaction.get());
			
			redirectAttributes.addFlashAttribute("success", "Hủy thành công!");
			return "redirect:/admin/order";
		}catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Hủy thất bại!");
			return "redirect:/admin/order";
		}
	}
	
	@Value("${upload.dir}")
	private String uploadDir;
	
	@PostMapping("/order/add-image")
	public String addImage(@RequestParam("orderId") int orderId, @RequestParam("file") MultipartFile[] files, RedirectAttributes redirectAttributes) {
		Optional<Order> orderOptional = orderService.findById(orderId);
		if(orderOptional.isPresent()) {
			Order order = orderOptional.get();
			
			for(MultipartFile file : files) {
				if(!file.isEmpty()) {
					try {
						String fileName = StringUtils.cleanPath(file.getOriginalFilename());
						Path copyLocation = Paths.get(uploadDir + File.separator + fileName);
						Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
						
						OrderImage orderImage = new OrderImage();
						orderImage.setOrder(order);
						orderImage.setImage("/ReviewMapsPhoto/" + fileName);
						orderImageService.save(orderImage);
					}catch (Exception e) {
						redirectAttributes.addFlashAttribute("danger", "Có lỗi xảy ra khi thêm ảnh!");
	                    return "redirect:/admin/order";
					}
				}
			}
			 redirectAttributes.addFlashAttribute("success", "Ảnh đã được thêm thành công.");
		}else {
			 redirectAttributes.addFlashAttribute("danger", "Không tìm thấy đơn hàng.");
		}
		 return "redirect:/admin/order";
	}
	
	@GetMapping("/order/delete-image/{id}")
	public String deleteOrderImage(@PathVariable int id, RedirectAttributes redirectAttributes) {
		try {
			orderImageService.deleteById(id);
			redirectAttributes.addFlashAttribute("success", "Xóa ảnh thành công!");
		}catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Xóa ảnh thất bại!");
		}
		return "redirect:/admin/order";
	}
	
	
	
	@GetMapping("/order/delete-by-id/{id}")
	public String deleteById(@PathVariable int id, RedirectAttributes redirectAttributes) {
		Optional<Order> order = orderService.findById(id);
		
		Optional<Transaction> transaction = transactionService.findByOrderId(order.get().getId());
		try {
			
			transactionService.deleteById(transaction.get().getId());
			orderService.delete(order.get());
			
			redirectAttributes.addFlashAttribute("success", "Xóa thành công!");
			return "redirect:/admin/order";
		}catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Xóa thất bại!");
			return "redirect:/admin/order";
		}
	}
	
	
	@GetMapping("/order/delete-all")
	public String deleteAll(RedirectAttributes redirectAttributes) {
		List<Transaction> transactions = transactionService.findAll();
		List<Order> orders = orderService.findAll();
		
		try {
			
			for(Transaction transaction : transactions) {
				if(transaction.getOrder() != null && !transaction.getOrder().getStatus().equals("Đang chờ")) {
					transactionService.delete(transaction);
					
				}
			}
			for(Order order : orders) {
				if(!order.getStatus().equals("Đang chờ")) {
				 orderService.delete(order);	
				}
			}
			
			
			redirectAttributes.addFlashAttribute("success", "Đã xóa tất cả đơn đã qua xử lý!");
			return "redirect:/admin/order";
		}catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Không thể xóa!");
			return "redirect:/admin/order";
		}
	}
	
}
