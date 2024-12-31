package com.example.Controller.Admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Entity.Admin;
import com.example.Entity.Recharge;
import com.example.Entity.Statistic;
import com.example.Entity.Transaction;
import com.example.Entity.User;
import com.example.Service.AdminService;
import com.example.Service.JwtUtil;
import com.example.Service.RechargeService;
import com.example.Service.StatisticService;
import com.example.Service.TransactionService;
import com.example.Service.UserService;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/admin")
public class RechargeManagementController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	RechargeService rechargeService;
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired AdminService adminService;
	
	@Autowired
	StatisticService statisticService;

	@GetMapping("/recharge")
	public String deposit(@CookieValue(value = "auth_token", defaultValue = "") String authToken,Model model) {
String username = JwtUtil.validateToken(authToken);
		
		if(username != null) {
			Admin loggedInAdmin = adminService.findByUsername(username);
			if(loggedInAdmin != null) {
				model.addAttribute("loggedInAdmin", loggedInAdmin);
			}
			
			List<Recharge> recharges = rechargeService.findAll();
			model.addAttribute("recharges", recharges);
			
			List<Transaction> transactions = transactionService.findAll();
			List<Transaction> filteredTransactions = new ArrayList<>();
			for(Transaction transaction : transactions) {
				if(transaction.getRecharge() != null) {
					filteredTransactions.add(transaction);
				}
			}
			model.addAttribute("filteredTransactions", filteredTransactions);
			
		}else {
			return "redirect:/admin/login";
		}
		
		return "Admin/recharge";
	}
	
	@PostMapping("/recharge/add")
	public String addRecharge(@RequestParam("userId") int userId, @RequestParam("depositAmount") int depositAmount,  RedirectAttributes redirectAttributes) {
		Optional<User> user = userService.findById(userId);
		try {
			
			Date currentDate = new Date();
			Recharge recharge = new Recharge();
			recharge.setDepositAmount(depositAmount);
			recharge.setStatus("DONE");
			recharge.setPrevBalance(user.get().getAmount());
			recharge.setAfterBalance(user.get().getAmount() + depositAmount);
			recharge.setDepositDate(currentDate);
			recharge.setUser(user.get());
			
			rechargeService.save(recharge);
			
			
			
			Transaction transaction = new Transaction();
			transaction.setRecharge(recharge);
			transaction.setPrevBalance(user.get().getAmount());
			transaction.setDepositAmount(depositAmount);
			transaction.setAfterBalance(user.get().getAmount() + depositAmount);
			transaction.setDepositDate(currentDate);
			transaction.setDetail("Nạp tiền");
			transaction.setUser(user.get());
			transactionService.save(transaction);
			
			user.get().setAmount(user.get().getAmount() + depositAmount);
			user.get().setDeposited(user.get().getDeposited() + depositAmount);
			userService.save(user.get());
			
			Optional<Statistic> statistic = statisticService.findById(1);
			statistic.get().setRevenue(statistic.get().getRevenue() + depositAmount);
			statisticService.save(statistic.get());
			
			redirectAttributes.addFlashAttribute("success", "Lưu thành công!");
			return "redirect:/admin/user/" + user.get().getId();
		}catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Lưu thất bại!");
			return "redirect:/admin/user/" + user.get().getId();
		}
		
	}
	
//	@PostMapping("/recharge/update")
//	public String update(@RequestParam("status") String status, @RequestParam("id") int id, RedirectAttributes redirectAttributes) {
//		Optional<Recharge> recharge = rechargeService.findById(id);
//		try {
//			recharge.get().setStatus(status);
//			rechargeService.save(recharge.get());
//			redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
//			return "redirect:/admin/user/" + recharge.get().getUser().getId();
//		}catch (Exception e) {
//			redirectAttributes.addFlashAttribute("danger", "Cập nhật thất bại!");
//			return "redirect:/admin/user/" + recharge.get().getUser().getId();
//		}
//		
//	}
	
	@GetMapping("/recharge/delete/{id}")
	public String delete(@PathVariable int id, RedirectAttributes redirectAttributes) {
		Optional<Recharge> recharge = rechargeService.findById(id);
		Optional<User> user = userService.findById(recharge.get().getUser().getId());
		Optional<Transaction> transaction = transactionService.findByRechargeId(recharge.get().getId());
		try {
			
			transactionService.deleteById(transaction.get().getId());
			rechargeService.deleteById(id);
			
			redirectAttributes.addFlashAttribute("success", "Xóa thành công!");
			return "redirect:/admin/user/" + user.get().getId();
		}catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Xóa thất bại!");
			return "redirect:/admin/user/" + user.get().getId();
		}
	}
	
	@GetMapping("/recharge/delete-by-id/{id}")
	public String deleteById(@PathVariable int id, RedirectAttributes redirectAttributes) {
		Optional<Recharge> recharge = rechargeService.findById(id);
		
		Optional<Transaction> transaction = transactionService.findByRechargeId(recharge.get().getId());
		try {
			
			transactionService.deleteById(transaction.get().getId());
			rechargeService.deleteById(id);
			
			redirectAttributes.addFlashAttribute("success", "Xóa thành công!");
			return "redirect:/admin/recharge";
		}catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Xóa thất bại!");
			return "redirect:/admin/recharge";
		}
	}
	
	@GetMapping("/recharge/delete-all")
	public String deleteAll(RedirectAttributes redirectAttributes) {
		List<Transaction> transactions = transactionService.findAll();
		
		List<Recharge> recharges = rechargeService.findAll();
		try {
			
			for(Transaction transaction : transactions) {
				if(transaction.getRecharge() != null && !transaction.getRecharge().getStatus().equals("WAIT")) {
					transactionService.delete(transaction);
					
				}
			}
			
			for(Recharge recharge : recharges) {
				if(!recharge.getStatus().equals("WAIT")) {
					rechargeService.delete(recharge);
				}
			}
			
			redirectAttributes.addFlashAttribute("success", "Đã xóa tất cả đơn đã qua xử lý!");
		}catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Không thể xóa!");
		}
		return "redirect:/admin/recharge";
	}
	
	@GetMapping("/recharge/done/{id}")
	public String done(@PathVariable int id, RedirectAttributes redirectAttributes) {
		Optional<Recharge> recharge = rechargeService.findById(id);
		Optional<User> user = userService.findById(recharge.get().getUser().getId());
		if(recharge != null) {
			try {
				recharge.get().setStatus("DONE");
				recharge.get().setPrevBalance(user.get().getAmount());
				recharge.get().setAfterBalance(user.get().getAmount() + recharge.get().getDepositAmount());
				rechargeService.save(recharge.get());
				user.get().setAmount(user.get().getAmount() + recharge.get().getDepositAmount());
				user.get().setDeposited(user.get().getDeposited() + recharge.get().getDepositAmount());
				userService.save(user.get());
				
				Optional<Statistic> statistic = statisticService.findById(1);
				statistic.get().setRevenue(statistic.get().getRevenue() + recharge.get().getDepositAmount());
				statisticService.save(statistic.get());
				
				redirectAttributes.addFlashAttribute("success", "Lưu thành công!");
				
			}catch (Exception e) {
				redirectAttributes.addFlashAttribute("danger", "Lưu thất bại!");
			}
		}
		
		return "redirect:/admin/recharge";
	}
	
	@GetMapping("/recharge/cancel/{id}")
	public String cancel(@PathVariable int id, RedirectAttributes redirectAttributes) {
		Optional<Recharge> recharge = rechargeService.findById(id);
		Optional<User> user = userService.findById(recharge.get().getUser().getId());
		if(recharge != null) {
			try {
				recharge.get().setStatus("CANCEL");
				
				
				rechargeService.save(recharge.get());
				redirectAttributes.addFlashAttribute("success", "Lưu thành công!");
				
			}catch (Exception e) {
				redirectAttributes.addFlashAttribute("danger", "Lưu thất bại!");
			}
		}
		
		return "redirect:/admin/recharge";
	}
	

	
	
	
}
