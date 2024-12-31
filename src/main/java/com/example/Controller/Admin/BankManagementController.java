package com.example.Controller.Admin;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Entity.Admin;
import com.example.Entity.Bank;
import com.example.Entity.Order;
import com.example.Service.AdminService;
import com.example.Service.BankService;
import com.example.Service.JwtUtil;

@Controller
@RequestMapping("/admin")
public class BankManagementController {

	@Autowired
	AdminService adminService;

	@Autowired
	BankService bankService;

	@GetMapping("/bank")
	public String bank(@CookieValue(value = "auth_token", defaultValue = "") String authToken, Model model,
			RedirectAttributes redirectAttributes) {
		String username = JwtUtil.validateToken(authToken);

		if (username != null) {
			Admin loggedInAdmin = adminService.findByUsername(username);
			if (loggedInAdmin != null) {
				model.addAttribute("loggedInAdmin", loggedInAdmin);
			}

			Optional<Bank> bank = bankService.findById(1);
			model.addAttribute("bank", bank.get());

		} else {
			return "redirect:/admin/login";
		}
		return "Admin/bank";
	}

	@Value("${upload.dir}")
	private String uploadDir;

	@PostMapping("/bank/update")
	public String update(@ModelAttribute Bank bank,
			 RedirectAttributes redirectAttributes) {
		try {
			
			bankService.save(bank);
			redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
			return "redirect:/admin/bank";
		}catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Cập nhật thất bại!");
			return "redirect:/admin/bank";
		}
	}

}
