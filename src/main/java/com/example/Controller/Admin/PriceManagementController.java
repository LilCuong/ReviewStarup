package com.example.Controller.Admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Entity.Admin;
import com.example.Entity.Value;
import com.example.Service.AdminService;
import com.example.Service.JwtUtil;
import com.example.Service.ValueService;

@Controller
@RequestMapping("/admin")
public class PriceManagementController {
	
	@Autowired
	ValueService valueService;
	
	@Autowired
	AdminService adminService;

	@GetMapping("/price")
	public String price(@CookieValue(value = "auth_token", defaultValue = "") String authToken, Model model) {
		String username = JwtUtil.validateToken(authToken);
		if(username != null) {
			Admin loggedInAdmin = adminService.findByUsername(username);
			if(loggedInAdmin != null) {
				model.addAttribute("loggedInAdmin", loggedInAdmin);
				
				Optional<Value> value = valueService.findId1();
				model.addAttribute("value", value.get());
				
				return "Admin/price";
			}
		}else {
			return "redirect:/admin/login";
		}
		
		return "Admin/price";
	}
	
	@PostMapping("/price/save")
	public String save(@RequestParam("amount") int amount, RedirectAttributes redirectAttributes) {
		Optional<Value> value = valueService.findId1();
		if(value.isPresent()) {
			try {
				value.get().setAmount(amount);
				valueService.save(value.get());
				redirectAttributes.addFlashAttribute("success", "Lưu thành công!");
				return "redirect:/admin/price";
			}catch (Exception e) {
				redirectAttributes.addFlashAttribute("danger", "Không thể lưu!");
				return "redirect:/admin/price";
			}
			
		}
		redirectAttributes.addFlashAttribute("danger", "Lỗi không tìm thấy!");
		return "redirect:/admin/price";
	}
}
