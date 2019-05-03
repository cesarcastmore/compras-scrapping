package com.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import javax.net.ssl.SSLSocketFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

public class Connection {


	int connectTimeout = 0;
	int readTimeout = 0;
	SSLSocketFactory sslSocket;
	HostnameVerifier hostname;
	private final String boundary;

	public Connection() {
		boundary = "===" + System.currentTimeMillis() + "===";

	}

	public Response send(Request request) throws ConnectionException {

		Response response = new Response();
		javax.net.ssl.HttpsURLConnection httpCon = null;
		int status = 0;
		try {

			String url = request.getUrl();

			if (!request.getParam().isEmpty()) {

				String parametros = "?";

				Iterator it = request.getParam().entrySet().iterator();

				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();
					parametros = parametros + pair.getKey() + "=" + pair.getValue() + "&";
					it.remove();
				}
				parametros = parametros.substring(0, parametros.length() - 1);

				url = url + parametros;
			}


			url = url.trim();

			URL endURL = new URL(url);
			httpCon = (javax.net.ssl.HttpsURLConnection) endURL.openConnection();

			httpCon.setDoOutput(true);
			httpCon.setDoInput(true);

			httpCon.setDoOutput(true);
			if (request.getContentType() != null)
				httpCon.setRequestProperty("Content-Type", request.getContentType());

			if (request.getSoapAction() != null)
				httpCon.setRequestProperty("SOAPAction", request.getSoapAction());
			
			if (request.getInvocationType() != null)
				httpCon.setRequestProperty("X-Amz-Invocation-Type", request.getInvocationType());

			httpCon.setRequestMethod(request.getMethod());

			if (connectTimeout > 0)
				httpCon.setConnectTimeout(connectTimeout);
			if (readTimeout > 0)
				httpCon.setReadTimeout(readTimeout);

			
			if (request.getxAuthToken() != null) {
				httpCon.setRequestProperty("x-auth-token", request.getxAuthToken());
			}
			
			if (request.getXrefid() != null) {
				httpCon.setRequestProperty("x-refid", request.getXrefid());
			}
			
			if (request.getContentMD5() != null) {
				httpCon.setRequestProperty("content-md5",request.getContentMD5());
			}


			if (request.getAuthorization() != null) {
				httpCon.setRequestProperty("Authorization",request.getAuthorization());
			}



			if (request.getContentLength() != null) {
				httpCon.setRequestProperty("Content-Length",request.getContentLength());
			}


			if (request.getHost() != null) {
				httpCon.setRequestProperty("Host",request.getHost());
			}



			if (request.getExcept() != null) {
				httpCon.setRequestProperty("Expect",request.getExcept());
			}



			if (request.getAcceptEncoding() != null) {
				httpCon.setRequestProperty("Accept-Encoding",request.getAcceptEncoding());
			}

			if(request.getHeaders() != null){
			for (Map.Entry<String, String> entry : request.getHeaders().entrySet())
				{
					httpCon.setRequestProperty(entry.getKey(), entry.getValue());

				}
			}	



			if (request.getConnection() != null) {
				httpCon.setRequestProperty("Connection",request.getConnection());
			}


			if (request.getBody() != null) {

				OutputStreamWriter outputStream = new OutputStreamWriter(httpCon.getOutputStream(), "UTF8");
				outputStream.write(request.getBody());
				outputStream.flush();

			}

			// Preparamos el response
			response = new Response();

			status = httpCon.getResponseCode();
			response.setStatus(status);

			InputStream inputStream = null;
			try {
				inputStream = httpCon.getInputStream();
			} catch (IOException exception) {
				inputStream = httpCon.getErrorStream();
			}


			if (inputStream != null) {

				StringBuffer sb = new StringBuffer();

				BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

				String inputLine = "";

				while ((inputLine = br.readLine()) != null) {
					sb.append(inputLine);
				}

				response.setContentType(httpCon.getContentType());
				response.setBody(sb.toString());

			}



			httpCon.disconnect();
		} catch (SocketTimeoutException e) {

			
			httpCon.disconnect();
			throw new ConnectionException(e.getMessage(), status);

		} catch (IOException e) {

			httpCon.disconnect();
			throw new ConnectionException(e.getMessage(), status);

		}

		return response;

	}


	/*
	 * public Response sendSSL(Request request) throws ConnectionException {
	 * 
	 * Response response = new Response();
	 * 
	 * try{
	 * 
	 * String url=request.getUrl();
	 * 
	 * if(!request.getParam().isEmpty()){
	 * 
	 * String parametros="?";
	 * 
	 * Iterator it = request.getParam().entrySet().iterator();
	 * 
	 * while (it.hasNext()) { Map.Entry pair = (Map.Entry)it.next();
	 * parametros=parametros + pair.getKey() + "=" + pair.getValue()+"&";
	 * it.remove(); // avoids a ConcurrentModificationException }
	 * parametros=parametros.substring(0, parametros.length() -1);
	 * 
	 * url=url+parametros; }
	 * 
	 * url = url.trim();
	 * 
	 * URL endURL = new URL(url); javax.net.ssl.HttpsURLConnection httpsCon =
	 * (javax.net.ssl.HttpsURLConnection) endURL.openConnection();
	 * 
	 * if(this.sslSocket != null){ httpsCon.setSSLSocketFactory(this.sslSocket);
	 * 
	 * }
	 * 
	 * if(this.hostname != null){ httpsCon.setHostnameVerifier(this.hostname); }
	 * 
	 * 
	 * httpsCon.setDoOutput(true); httpsCon.setDoInput(true);
	 * 
	 * httpsCon.setDoOutput(true); if(request.getContentType()!= null)
	 * httpsCon.setRequestProperty( "Content-Type", request.getContentType());
	 * 
	 * if(request.getSoapAction()!= null) httpsCon.setRequestProperty(
	 * "SOAPAction", request.getSoapAction());
	 * 
	 * httpsCon.setRequestMethod(request.getMethod());
	 * 
	 * if(connectTimeout > 0) httpsCon.setConnectTimeout(connectTimeout);
	 * if(readTimeout > 0) httpsCon.setReadTimeout(readTimeout);
	 * 
	 * 
	 * 
	 * if(request.getUsuario() != null && request.getContrasenia() != null){
	 * String loginPassword = request.getUsuario() + ":" +
	 * request.getContrasenia(); String encoded =
	 * Base64Coder.encodeString(loginPassword); httpsCon.setRequestProperty
	 * ("Authorization", "Basic " + encoded);
	 * 
	 * 
	 * }
	 * 
	 * if(request.getBody() != null){
	 * 
	 * 
	 * 
	 * OutputStreamWriter out = new OutputStreamWriter(
	 * httpsCon.getOutputStream());
	 * 
	 * out.write(request.getBody()); out.flush();
	 * 
	 * }
	 * 
	 * //Preparamos el response response = new Response();
	 * 
	 * int status = httpsCon.getResponseCode(); response.setStatus(status);
	 * 
	 * 
	 * 
	 * InputStream inputStream = (InputStream) httpsCon.getInputStream();
	 * 
	 * if (inputStream != null) {
	 * 
	 * StringBuffer sb = new StringBuffer();
	 * 
	 * BufferedReader br = new BufferedReader(new
	 * InputStreamReader(inputStream));
	 * 
	 * String inputLine = "";
	 * 
	 * while ((inputLine = br.readLine()) != null) { sb.append(inputLine); }
	 * 
	 * response.setContentType(httpsCon.getContentType());
	 * response.setBody(sb.toString());
	 * 
	 * }
	 * 
	 * httpsCon.disconnect(); } catch (SocketTimeoutException e){
	 * 
	 * throw new ConnectionException(e.getMessage());
	 * 
	 * } catch (IOException e) {
	 * 
	 * // TODO Auto-generated catch block throw new
	 * ConnectionException(e.getMessage());
	 * 
	 * }
	 * 
	 * 
	 * return response;
	 * 
	 * }
	 */
	/**
	 * @return the connectTimeout
	 */
	public int getConnectTimeout() {
		return connectTimeout;
	}

	/**
	 * @param connectTimeout
	 *            the connectTimeout to set
	 */
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	/**
	 * @return the readTimeout
	 */
	public int getReadTimeout() {
		return readTimeout;
	}

	/**
	 * @param readTimeout
	 *            the readTimeout to set
	 */
	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	/**
	 * @return the sslSocket
	 */
	public SSLSocketFactory getSslSocket() {
		return sslSocket;
	}

	/**
	 * @param sslSocket
	 *            the sslSocket to set
	 */
	public void setSslSocket(SSLSocketFactory sslSocket) {
		this.sslSocket = sslSocket;
	}

	/**
	 * @return the hostname
	 */
	public HostnameVerifier getHostname() {
		return hostname;
	}

	/**
	 * @param hostname
	 *            the hostname to set
	 */
	public void setHostname(HostnameVerifier hostname) {
		this.hostname = hostname;
	}

}
