package com.codegnan.controller;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.codegnan.model.Employee;
import com.codegnan.service.EmployeeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class EmployeeController {
	@Autowired
	 private EmployeeService service;
	
	@RequestMapping("/")
	public String mainPage() {
		return "index";
	}
	
	@RequestMapping("/register")
	public String registerPage() {
		return "register";
	}
	@RequestMapping("/loginreq")
	public String loginPage() {
		return "login";
	}
	@RequestMapping("/aboutus")
	public String aboutusPage() {
		return "aboutus";
	}
	@RequestMapping("/contactus")
	public String contactusPage() {
		return "contactus";
	}
	
	@RequestMapping("/saveuser")
	public ModelAndView saveEmployee(Employee employee) {
		Employee emp=service.saveEmployee(employee);
		if(emp!=null) {
			ModelAndView mav=new ModelAndView("register");
			mav.addObject("status","Successfully Register");
			return mav;
		}
		else {
			ModelAndView mav=new ModelAndView("register");
			mav.addObject("status","Registration Failed");
			return mav;
		}
	}
	
	@RequestMapping("/validatelogin")
	public ModelAndView login(String email, String password, HttpServletRequest request) {
		HttpSession session=request.getSession();
		if(email.equals("admin@codegnan") && password.equals("admin@123")) {
			session.setAttribute("email", email);
			ModelAndView mav=new ModelAndView("admin");
			mav.addObject("status","Welcome to Admin Page");
			return mav;
		}
		else {
			Employee emp=service.findByEmailAndPassword(email,password);
			if(emp!=null) {
				session.setAttribute("email", email);
				ModelAndView mav=new ModelAndView("userprofile");
				mav.addObject("employee",emp);
				return mav;
			}
			else {
				ModelAndView mav=new ModelAndView("login");
				mav.addObject("status","Inavlid Credentials.. Try Agian");
				return mav;
			}
			
		}
	}
	
	@RequestMapping("/findAll")
	public ModelAndView viewAllEmployees() {
		List<Employee> emp = service.findAll();
		ModelAndView mav=new ModelAndView("viewemps");
		mav.addObject("emplist",emp);
		return mav;
	}
	
	@RequestMapping("/delete")
	public ModelAndView deleteById(int id) {
		service.deleteById(id);
		ModelAndView mav=new ModelAndView("redirect:/findAll");
		return mav;
	}
	
	@RequestMapping("/edit")
	public String editRequest() {
		return "editemp";
	}
	
	@RequestMapping("/edituser")
	public ModelAndView editUser(Employee employee) {
		Employee emp=service.saveEmployee(employee);
		ModelAndView mav=new ModelAndView("redirect:/findAll");
		mav.addObject("emp",emp);
		return mav;
		
		
	}
	
	@RequestMapping("/logoutreq")
	public ModelAndView logout(HttpSession session) {
		session.invalidate();
		ModelAndView mav=new ModelAndView("redirect:/loginreq");
		return mav;
	}
	
	@RequestMapping("/search")
	public String searchPage() {
		return "search";
	}
	
	@RequestMapping("/searchbyid")
	public ModelAndView searchById(int id) {
		Employee employee=service.findById(id);
		if(employee!=null) {
			ModelAndView mav=new ModelAndView("viewemp");
			mav.addObject("employee",employee);
			return mav;
		}
		else {
			ModelAndView mav=new ModelAndView("search");
			mav.addObject("status","Invalid Id");
			return mav;
		}
	}
	
}
