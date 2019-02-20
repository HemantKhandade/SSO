package com.hk.sso.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.hk.sso.model.ClientDetails;
import com.hk.sso.service.GenerateAuthToken;
import com.hk.sso.service.VaidateUser;

@Controller
@Scope("session")
@RequestMapping("/sso")
@SessionAttributes({"clientDetails","client_id"})
public class SSOLoginController {
	
	HashMap<String, ClientDetails> clientAppInfo = new HashMap<>();
	
	@GetMapping(value="/redirectlogin")
	public String redirectlogin(@RequestParam(name="client_id", required=true) String clientId, 
						@RequestParam(name="return_url", required=true) String returnUrl, 
						@RequestParam(name="subject", required=true) String subject, 
						HttpServletRequest request,
						Model model) {
		System.out.println("SSOLoginController.redirectlogin Enter");
		System.out.println("SSOLoginController.redirectlogin returnUrl :: " + returnUrl);
		
		ClientDetails clientDetails = new ClientDetails();
		clientDetails.setClientId(clientId);
		clientDetails.setRedirectUrl(returnUrl);
		clientDetails.setSubject(subject);
		
		clientAppInfo.put(clientId, clientDetails);
		request.setAttribute("client_id", clientId);
		return "login";
		
	}
	@GetMapping(value="/home")
	public String home(HttpServletRequest request, Model model) {
		System.out.println("SSOLoginController.home Enter");
		ClientDetails clientDetails = new ClientDetails();
		String clientId = "ssoclient";
		clientDetails.setClientId(clientId);
		clientDetails.setRedirectUrl("http://localhost:8090/login");
		clientDetails.setSubject("SSO_USER");
		
		clientAppInfo.put(clientId, clientDetails);
		model.addAttribute("clientDetails", clientAppInfo);
		model.addAttribute("client_id", clientId);
		return "login";
		
	}
	
	@PostMapping(value="/login")
	public String login(@RequestParam(name="username", required=true) String username, 
						@RequestParam(name="password", required=true) String password,
						HttpServletRequest request,
						HttpServletResponse response,
						Model model) 
	{
		
		System.out.println("SSOLoginController.login Enter");
		System.out.println("SSOLoginController.login username :: " + username);
		System.out.println("SSOLoginController.login password :: " + password);
		
		String result = null;
		String token = null;
		GenerateAuthToken getAuthToken = null;
		
		try {
			String clientID = request.getParameter("client_id");
			System.out.println("clientId " + clientID);
			System.out.println("model clientId " + model.containsAttribute("client_id"));
			
			VaidateUser authenticateUser = new VaidateUser();
			boolean userAuthenticated = authenticateUser.validateUser(username, password);
			
			//if(username.equals("hemant") && password.equals("hkpswd"))
			if(userAuthenticated)
			{
				result = "Success";
				
				ClientDetails clientDetails = clientAppInfo.get(clientID);
				//String redirectUrl="http://localhost:8080/opus/home?securedAuthToken=4fiu8klsdhruel847klsjfkl";
				if(clientDetails != null) {
					getAuthToken = new GenerateAuthToken();
					System.out.println("client details :: " + clientDetails.getSubject());
					token = getAuthToken.createJWT(clientID, clientDetails.getSubject());
					System.out.println("clientDetails.getRedirectUrl()::  " + clientDetails.getRedirectUrl());
					System.out.println("Generated Token :: " + token);
					String redirectUrl = "http://"+clientDetails.getRedirectUrl() + "?securedAuthToken="+token;
					System.out.println("Back to :: " + redirectUrl);
			    	try {
						response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
						response. sendRedirect(redirectUrl);
						response.setHeader("Location", redirectUrl);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					System.out.println("Client Details is null");
				}
			}else {
				result = "Login Failed.Please retry";
			}
		} catch (Exception e) {
			result = "Login Failed.Please retry";
			e.printStackTrace();
		}
		
		model.addAttribute("result", result);
		return "home";
		
	}

}