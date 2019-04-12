package com.http;

import java.util.HashMap;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class Request {

	public static String POST = "POST";
	public static String GET = "GET";
	public static String DELETE = "DELETE";
	public static String PUT = "PUT";

	String contentType;
	String body;
	String method;
	HashMap<String, String> param = new HashMap<String, String>();
	String usuario;
	String contrasenia;
	String url;
	String soapAction;
	String paramsCadena;
	List<File> files = new ArrayList<File>();
	String xAuthToken;
	String xrefid;
	String contentMD5;
	String authorization;
	String invocationType;
	Map<String, String> headers = new HashMap<String, String>();

	String host;
	String contentLength;
	String expect;
	String acceptEncoding;
	String connection;

	public Request() {
		param = new HashMap<String, String>();
		files = new ArrayList<File>();
		headers = new HashMap<String, String>();
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}




		/**
	 * @return the authorization
	 */
	public String getAuthorization() {
		return authorization;
	}

	/**
	 * @return the method
	 */
	public void setAuthorization(String authorization) {
		 this.authorization = authorization;
	}





		/**
	 * @return the authorization
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}

	/**
	 * @return the method
	 */
	public void setHeaders(Map<String, String> headers) {
		 this.headers = headers;
	}





	/**
	 * @param method
	 *            the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @return the param
	 */
	public HashMap<String, String> getParam() {
		return param;
	}

	/**
	 * @param param
	 *            the param to set
	 */
	public void addParam(String key, String value) {
		param.put(key, value);
	}

	/**
	 * @param contentType
	 *            the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body
	 *            the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario
	 *            the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the contrasenia
	 */
	public String getContrasenia() {
		return contrasenia;
	}

	/**
	 * @param contrasenia
	 *            the contrasenia to set
	 */
	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the soapAction
	 */
	public String getSoapAction() {
		return soapAction;
	}

	/**
	 * @param soapAction
	 *            the soapAction to set
	 */
	public void setSoapAction(String soapAction) {
		this.soapAction = soapAction;
	}

	public String getxAuthToken() {
		return xAuthToken;
	}

	public void setxAuthToken(String xAuthToken) {
		this.xAuthToken = xAuthToken;
	}

	public String getXrefid() {
		return xrefid;
	}

	public void setXrefid(String xrefid) {
		this.xrefid = xrefid;
	}

	public String getContentMD5() {
		return contentMD5;
	}

	public void setContentMD5(String contentMD5) {
		this.contentMD5 = contentMD5;
	}
	
	
	public String getInvocationType() {
		return invocationType;
	}

	public void setInvocationType(String invocationType) {
		this.invocationType = invocationType;
	}

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}


	public String getContentLength() {
		return this.contentLength;
	}

	public void setContentLength(String contentLength) {
		this.contentLength = contentLength;
	}



	public String getExcept() {
		return this.expect;
	}

	public void setExpect(String expect) {
		this.expect = expect;
	}


	public String getAcceptEncoding() {
		return this.acceptEncoding;
	}

	public void setAcceptEncoding(String acceptEncoding) {
		this.acceptEncoding = acceptEncoding;
	}

	public String getConnection() {
		return this.connection;
	}

	public void setConnection(String connection) {
		this.connection = connection;
	}



}