package logInHandling;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginHandler {
	
	private static MessageDigest md;
	
	
	/**
	 * This method receives a string and returns a string as a encrypted MD5.
	 * @param pass a String to be encrypted
	 * @return a String with MD5 Encryption
	 * @since 10/9/17
	 */
	public static String cryptWithMD5(String pass){
	    try {
	        md = MessageDigest.getInstance("MD5");
	        byte[] passBytes = pass.getBytes();
	        md.reset();
	        byte[] digested = md.digest(passBytes);
	        StringBuffer sb = new StringBuffer();
	        for(int i=0;i<digested.length;i++){
	            sb.append(Integer.toHexString(0xff & digested[i]));
	        }
	        return sb.toString();
	    } catch (NoSuchAlgorithmException ex) {
	        Logger.getLogger(LoginHandler.class.getName()).log(Level.SEVERE, null, ex);
	    }
	        return null;
	   }
	}
