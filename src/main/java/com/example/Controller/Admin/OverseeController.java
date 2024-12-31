package com.example.Controller.Admin;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Entity.Admin;
import com.example.Entity.Oversee;
import com.example.Entity.Statistic;
import com.example.Entity.StatisticHistory;
import com.example.Service.JwtUtil;
import com.example.Service.OverseeService;
import com.example.Service.StatisticHistoryService;
import com.example.Service.StatisticService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/oversee")
public class OverseeController {
	
	@Autowired
	OverseeService overseeService;
	
	@Autowired
	StatisticService statisticService;
	
	@Autowired
	StatisticHistoryService statisticHistoryService;
	
	@GetMapping("/index")
	public String index(@CookieValue(value = "oversee_auth_token", defaultValue = "") String authToken, Model model) {
		String oversee = JwtUtil.validateToken(authToken);
		
		if(oversee == null) {
			return "redirect:/oversee/oversee/login";
		}
		
		Optional<Statistic> statistic = statisticService.findById(1);
		if(statistic.isPresent()) {
			model.addAttribute("statistic", statistic.get());
		}
		
		List<StatisticHistory> statisticHistories = statisticHistoryService.findAll();
		model.addAttribute("statisticHistories", statisticHistories);
		
		int totalRevenue = statisticHistories.stream()
				.mapToInt(StatisticHistory::getRevenue).sum() + statistic.get().getRevenue();
		model.addAttribute("totalRevenue", totalRevenue);
		
		
		return "Admin/statistics";
	}

	@GetMapping("/oversee/login")
	public String oversee() {
		return "Admin/oversee";
	}
	
	
//	@PostMapping("/login")
//	public String login(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request) {
//		Oversee oversee = overseeService.findByUsername(username);
//		if(oversee != null) {
//			if(oversee.getPassword().equals(password)) {
//				HttpSession session = request.getSession();
//				session.setAttribute("loggedInOversee", oversee);
//				return "redirect:/oversee/index";
//			}
//			return "redirect:/oversee/oversee/login";
//		}
//		return "redirect:/oversee/oversee/login";
//	}
//	
//	
	
	
	@PostMapping("/login")
	public String login(@RequestParam("username") String username, @RequestParam("password") String password,  @RequestParam(value = "rememberMe", required = false) Boolean rememberMe, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		
		Oversee oversee = overseeService.findByUsername(username);
		
		if(oversee != null) {
			if(oversee.getPassword().equals(password)) {
				String token = JwtUtil.genarateToken(username, rememberMe != null && rememberMe);
				
				Cookie cookie = new Cookie("oversee_auth_token", token);
				cookie.setHttpOnly(true);
				cookie.setSecure(true);
				cookie.setPath("/");
				
				if(rememberMe != null && rememberMe) {
					cookie.setMaxAge(864000);
				}else {
					cookie.setMaxAge(-1);
				}
				
				response.addCookie(cookie);
				
				return "redirect:/oversee/index";
			}else {
				redirectAttributes.addFlashAttribute("danger", "Sai mật khẩu!");
				return "redirect:/oversee/oversee/login";
			}
		}else {
			redirectAttributes.addFlashAttribute("danger", "Không tìm thấy tài khoản!");
			return "redirect:/oversee/oversee/login";
		}
		
	}
	
	
	
	@GetMapping("/renew")
	public String renew() {
		Optional<Statistic> statistic = statisticService.findById(1);
		StatisticHistory statisticHistory = new StatisticHistory();
		Date currentDate = new Date();
		try {
			statisticHistory.setDate(currentDate);
			statisticHistory.setRevenue(statistic.get().getRevenue());
			statisticHistoryService.save(statisticHistory);
			statistic.get().setRevenue(0);
			statisticService.save(statistic.get());
		}catch (Exception e) {
			
		}
		return "redirect:/oversee/index";
		
	}
	
	
	@GetMapping("/statistic/delete/{id}")
	public String deleteById(@PathVariable int id, RedirectAttributes redirectAttributes) {
		StatisticHistory statisticHistory = statisticHistoryService.findById(id);
		try {
			statisticHistoryService.delete(statisticHistory);
			redirectAttributes.addFlashAttribute("success", "Thành công!");
		}catch (Exception e) {
			redirectAttributes.addFlashAttribute("danger", "Thất bại!");
		}
		return "redirect:/oversee/index";
	}
	

}
