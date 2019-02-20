package com.hk.sso.service;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.springframework.stereotype.Component;

@Component("vaidateUser")
public class VaidateUser {


	String url = "ldap://localhost:10389";
	public static String MGR_DN = "ou=people, o=mydomain.com";
	public static String MY_SEARCHBASE = "o=mydomain.com";
	
	public boolean validateUser(String employeeNumber, String password) {
		boolean userValid = false;
		System.out.println("Validate User :: Enter" );
		System.out.println("Validate User :: URL :: " + url);
		System.out.println("Validate User :: employeeNumber:password = " + employeeNumber + " : " + password);
		Hashtable<String,String> ctx = new Hashtable<>();
		ctx.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		ctx.put(Context.PROVIDER_URL, url);
		ctx.put(Context.SECURITY_PRINCIPAL, "employeeNumber="+employeeNumber+",ou=people,dc=mydomain,dc=com");
	
		ctx.put(Context.SECURITY_AUTHENTICATION, "simple");
		ctx.put(Context.SECURITY_CREDENTIALS, password);
		
		DirContext env = null;
		 try {
			env = new InitialDirContext(ctx);
			System.out.println("User is Valid");
			userValid = true;
		 }catch(Exception ex) {
			 ex.printStackTrace();
		 }finally {
			 try {
				env.close();
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		return userValid;
	}
	
}
