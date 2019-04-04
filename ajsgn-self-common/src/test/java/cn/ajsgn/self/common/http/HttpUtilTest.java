package cn.ajsgn.self.common.http;

import java.io.IOException;

public class HttpUtilTest {

	public static void main(String[] args) throws IOException {
//		HttpUtil.addCookie("JSESSIONID", "208A69040794E59C6B44B6BB611C05B5");
		String result = HttpUtil.get("http://xxx.baidu.com/");	// 需要登陆的请求
		
		HttpUtil.addRequestHeader("Cookie", "xxx=xxx; xxx1=xxx1");
		String result1 = HttpUtil.get("http://xxx.baidu.com/");	// 需要登陆的请求
		
		System.out.println(result);
		System.out.println(result1);
		System.out.println(result.equals(result1));//true
		
	}
}



