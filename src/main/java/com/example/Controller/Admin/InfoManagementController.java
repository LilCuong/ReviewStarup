package com.example.Controller.Admin;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Entity.Admin;
import com.example.Entity.Email;
import com.example.Entity.Information;
import com.example.Repository.InformationRepository;
import com.example.Service.AdminService;
import com.example.Service.EmailService;
import com.example.Service.InformationService;
import com.example.Service.JwtUtil;

@Controller
@RequestMapping("/admin")
public class InfoManagementController {
	
	@Autowired
	AdminService adminService;
	
	@Autowired
	InformationService informationService;
	
	@Autowired
	EmailService emailService;

	@GetMapping("/info")
	public String contact(@CookieValue(value = "auth_token", defaultValue = "") String authToken, Model model) {
String username = JwtUtil.validateToken(authToken);
		
		if(username != null) {
			Admin loggedInAdmin = adminService.findByUsername(username);
			if(loggedInAdmin != null) {
				model.addAttribute("loggedInAdmin", loggedInAdmin);
				
				Optional<Information> information = informationService.findById(1);
				if(information.isPresent()) {
					model.addAttribute("information", information.get());
				}
				
				Email email = emailService.getEmail();
				model.addAttribute("email", email);
				
			}
		}else {
			return "redirect:/admin/login";
		}
		
		return "Admin/info";
	}
	
	@PostMapping("/info/update")
	public String updateInfo(@ModelAttribute Information info, RedirectAttributes redirectAttributes) {
		try {
			informationService.save(info);
			redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
		}catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Cập nhật thất bại!");
		}
		return "redirect:/admin/info";
	}
	
	@PostMapping("/info/email/update")
	public String updateEmail(@ModelAttribute Email email, RedirectAttributes redirectAttributes) {
		try {
			emailService.save(email);
			redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
		}catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Cập nhật thất bại!");
		}
		return "redirect:/admin/info";
	}
}
