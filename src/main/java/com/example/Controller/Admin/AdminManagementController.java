package com.example.Controller.Admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Entity.Admin;
import com.example.Service.AdminService;
import com.example.Service.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminManagementController {
	
	@Autowired
	AdminService adminService;

	@GetMapping("/admin")
	public String admin(@CookieValue(value = "auth_token", defaultValue = "") String authToken, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
		
		String username = JwtUtil.validateToken(authToken);
		if(username != null) {
			Admin loggedInAdmin = adminService.findByUsername(username);
			if(loggedInAdmin != null) {
				model.addAttribute("loggedInAdmin", loggedInAdmin);
				
				List<Admin> admins = adminService.findAll();
				model.addAttribute("admins", admins);
				Admin admin = new Admin();
				model.addAttribute("admin", admin);
				
				return "Admin/admin";
			}
		}else {
			return "redirect:/admin/login";
		}
		
		return "Admin/admin";
	}
	

	@GetMapping("/admin/{id}")
	public String update(@CookieValue(value = "auth_token", defaultValue = "") String authToken, Model model, @PathVariable int id, HttpServletRequest request) {
		String username = JwtUtil.validateToken(authToken);
		if(username != null) {
			Admin loggedInAdmin = adminService.findByUsername(username);
			if(loggedInAdmin != null) {
				model.addAttribute("loggedInAdmin", loggedInAdmin);
			List<Admin> admins = adminService.findAll();
			model.addAttribute("admins", admins);
			Optional<Admin> adminGet = adminService.findById(id);
			Admin admin = new Admin();
			if (adminGet.isPresent()) {
				admin = adminGet.get();
			}
			model.addAttribute("admin", admin);
			return "Admin/admin";
		}else {
			return "redirect:admin/login";
		}
		
	}
		return "redirect:admin/login";
	}

	@PostMapping("/admin/save")
	public String saveAdmin(@CookieValue(value = "auth_token", defaultValue = "") String authToken, 
	                        @ModelAttribute Admin admin, 
	                        RedirectAttributes redirectAttributes) {
	    try {
	        Admin existingAdmin = adminService.findByUsername(admin.getUsername());
	        if (existingAdmin != null && existingAdmin.getId() != admin.getId()) {
	            redirectAttributes.addFlashAttribute("danger", "Tên tài khoản này đã được sử dụng!");
	            return "redirect:/admin/admin";
	        }

	        // Nếu điều kiện trên không thỏa mãn, lưu thông tin admin
	        adminService.save(admin);
	        redirectAttributes.addFlashAttribute("success", "Lưu thành công!");
	        return "redirect:/admin/admin";
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("danger", "Lưu thất bại!");
	        return "redirect:/admin/admin";
	    }
	}


	@GetMapping("/admin/delete/{id}")
	public String deleteById(@CookieValue(value = "auth_token", defaultValue = "") String authToken, @PathVariable int id, RedirectAttributes redirectAttributes, Model model) {
		try {
			String username = JwtUtil.validateToken(authToken);
			Admin loggedInAdmin = adminService.findByUsername(username);
			if(loggedInAdmin != null && loggedInAdmin.getId() == id) {
				redirectAttributes.addFlashAttribute("danger", "Không thể xóa tài khoản đang đăng nhập!");
				return "redirect:/admin/admin";
			}
			adminService.deleteById(id);
			redirectAttributes.addFlashAttribute("success", "Xóa thành công!");
			return "redirect:/admin/admin";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Xóa thất bại!");
			return "redirect:/admin/admin";
		}
	}

}
