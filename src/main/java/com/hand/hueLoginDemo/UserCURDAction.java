package com.hand.hueLoginDemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

/**
 * hue 用户增删改查实现（调用hue接口）
 * @author youtakeoff
 *
 */
public class UserCURDAction {
	
	public static final String HOST = "http://10.211.55.247:8000/";
	
	public static void main(String[] args) {
		Map<String,String> maps = UserCURDAction.loginPage();
		Map<String,String> map = UserCURDAction.loginFunc(maps);
//		boolean boo = UserCURDAction.registerFunc(map);//新增用户
		boolean boo1 = UserCURDAction.updateUserFunc(map);//修改用户
//		System.out.println("新增用户:"+boo);
		System.out.println("修改用户:"+boo1);
	}
	
	/**
	 * access hue login page
	 * @return
	 */
	public static Map<String,String> loginPage(){
		String url = HOST+"accounts/login?next=/";
		Map<String,String> map = null;
		try {
			HttpGet get = new HttpGet(url);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			CloseableHttpResponse response = httpClient.execute(get);
			map = getTokenSessionMsg(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * hue login 
	 */
	public static Map<String,String> loginFunc(Map<String,String> map) {
		String url = HOST+"accounts/login/";
		Map<String,String> params = new HashMap<String,String>();
        params.put("csrfmiddlewaretoken", map.get("csrftoken"));
        params.put("username","admin");
        params.put("password", "admin");
        params.put("next", "/");
		CloseableHttpResponse response = post(url, params, map.get("sessionid"), map.get("csrftoken"));
		map = getTokenSessionMsg(response);
		return map;
	}
	
	/**
	 * hue register
	 */
	public static boolean registerFunc(Map<String,String> map) {
		String url = HOST+"useradmin/users/new";
		boolean boo = false;
        Map<String,String> params = new HashMap<String,String>();
        params.put("csrfmiddlewaretoken", map.get("csrftoken"));
        params.put("username", "tom01");
        params.put("password1", "123456admin");
        params.put("password2", "123456admin");
        params.put("ensure_home_directory", "on");
        params.put("groups", "1");
        params.put("is_active", "on");
        params.put("is_embeddable", "true");
	    //构造消息体
        CloseableHttpResponse response = post(url, params, map.get("sessionid"), map.get("csrftoken"));
        if(response!=null && response.getStatusLine().toString().contains("200")) {
        	boo = true;
        }
		return boo;
	}
	
	/**
	 * update hue user
	 */
	public static boolean updateUserFunc(Map<String,String> map) {
		String url = HOST+"useradmin/users/edit/tom01";//youtakeoff2 表示要修改的用户名
		boolean boo = false;
		Map<String,String> params = new HashMap<String,String>();
        params.put("csrfmiddlewaretoken", map.get("csrftoken"));
        params.put("username", "tom01");
        params.put("password1", "123456");
        params.put("password2", "123456");
        params.put("ensure_home_directory", "on");
        params.put("groups", "1");
        params.put("is_active", "on");
        params.put("is_embeddable", "true");
      //构造消息体
        CloseableHttpResponse response = post(url, params, map.get("sessionid"), map.get("csrftoken"));
        if(response!=null && response.getStatusLine().toString().contains("200")) {
        	boo = true;
        }
        return boo;
	}

	public static CloseableHttpResponse post(String url,Map<String,String> params,String sessionid,String csrftoken) {
		HttpPost post = null;
		CloseableHttpResponse response = null;
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			post = new HttpPost(url);
			post.setHeader("Content-Type","application/x-www-form-urlencoded");
	        post.setHeader("Connection", "Keep-Alive");
	        post.setHeader("Cookie", "sessionid="+sessionid+"; csrftoken="+csrftoken);
	        List<NameValuePair> qparams = null;
	        if(params!=null) {
	        	qparams = new ArrayList<NameValuePair>();
	        	for (Map.Entry<String, String> entry : params.entrySet()) { 
	        		qparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
	        	}
	        }
	        HttpEntity reqEntity = new UrlEncodedFormEntity(qparams,Consts.UTF_8);
	        post.setEntity(reqEntity);
	        response = httpClient.execute(post);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public static Map<String,String> getTokenSessionMsg(CloseableHttpResponse response){
		Map<String,String> map = new HashMap<String,String>();
		Header[] header = response.getAllHeaders();
		if(header!=null && header.length>0) {
			for (Header header2 : header) {
				if("Set-Cookie".equals(header2.getName())) {
					String cookie = header2.getValue();
					String[] strs = cookie.split(";");
					String[] strs2 = strs[0].split("=");
					/*
					 * csrftoken 
					 * sessionid
					 */
					map.put(strs2[0], strs2[1]);
				}
			}
		}
		return map;
	}
}
