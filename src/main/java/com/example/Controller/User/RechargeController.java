package com.example.Controller.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Entity.Bank;
import com.example.Entity.Information;
import com.example.Entity.Recharge;
import com.example.Entity.Transaction;
import com.example.Entity.User;
import com.example.Entity.Value;
import com.example.Service.BankService;
import com.example.Service.InformationService;
import com.example.Service.JwtUtil;
import com.example.Service.RechargeService;
import com.example.Service.TransactionService;
import com.example.Service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class RechargeController {

	@Autowired
	RechargeService rechargeService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	InformationService informationService;
	
	@Autowired
	BankService bankService;
	
	@Autowired
	TransactionService transactionService;
	
	@GetMapping(value = "/recharge")
	public String recharge(@CookieValue(value = "user_auth_token", defaultValue = "") String authToken, Model model, RedirectAttributes redirectAttributes) {		
		
		Optional<Information> information = informationService.findById(1);
		model.addAttribute("info", information.get());
		
		Optional<Bank> bank = bankService.findById(1);
		model.addAttribute("bank", bank.get());
		
		String username = JwtUtil.validateToken(authToken);
		if(username != null) {
			User user = userService.findByUsername(username);
			if(user != null) {
				model.addAttribute("user", user);
				
				List<Recharge> recharges = rechargeService.findByUserId(user.getId());
				model.addAttribute("recharges", recharges);
				
				List<Transaction> transactions = transactionService.findByUserId(user.getId());
				List<Transaction> filteredTransactions = new ArrayList<>();
				for(Transaction transaction : transactions) {
					if(transaction.getRecharge() != null) {
						filteredTransactions.add(transaction);
					}
				}
				model.addAttribute("transactions", filteredTransactions);
				
				
			}
			
		}else {
			redirectAttributes.addFlashAttribute("success", "Bạn cần phải đăng nhập!");
			return "redirect:/login";
		}
		
		boolean isLoggedIn = username != null;
		model.addAttribute("isLoggedIn", isLoggedIn);
		return "User/recharge";
	}
	
	
	@PostMapping("/recharge/add")
	public String addRecharge(@CookieValue(value = "user_auth_token", defaultValue = "") String authToken, @RequestParam("transactionCode") String transactionCode, @RequestParam("depositAmount") int depositAmount, RedirectAttributes redirectAttributes) {
		
		String username = JwtUtil.validateToken(authToken);
		if(username != null) {
			User user = userService.findByUsername(username);
			if(user != null) {
				
				List<Recharge> recharges = rechargeService.findByUserId(user.getId());
				List<Recharge> numberWaitRecharges = new ArrayList<>();
				for(Recharge recharge : recharges) {
					if("WAIT".equals(recharge.getStatus())) {
						numberWaitRecharges.add(recharge);
					}
				}
				
				if( numberWaitRecharges.size() >= 3) {
					redirectAttributes.addFlashAttribute("danger", "Hiện tại đã có 3 đơn đang được yêu cầu, vui lòng chờ!");
					return "redirect:/recharge";
				}
				
				Recharge recharge = new Recharge();
				Date currentDate = new Date();
				
				recharge.setTransactionCode(transactionCode);
				recharge.setUser(user);
				recharge.setPrevBalance(user.getAmount());
				recharge.setDepositAmount(depositAmount);
				recharge.setAfterBalance(user.getAmount() + depositAmount);
				recharge.setStatus("WAIT");
				recharge.setDepositDate(currentDate);
				rechargeService.save(recharge);
				
				Transaction transaction = new Transaction();
				transaction.setPrevBalance(user.getAmount());
				transaction.setDepositAmount(depositAmount);
				transaction.setAfterBalance(user.getAmount() + depositAmount);
				transaction.setDepositDate(currentDate);
				transaction.setDetail("Nạp tiền");
				transaction.setRecharge(recharge);
				transaction.setUser(user);
				transactionService.save(transaction);
				redirectAttributes.addFlashAttribute("success", "Thêm yêu cầu thành công!");
			}
			
		}else {
			redirectAttributes.addFlashAttribute("success", "Bạn cần phải đăng nhập!");
			return "redirect:/login";
		}
		
		return "redirect:/recharge";
	}
	
}
