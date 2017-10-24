package com.jsoup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 通过Html解析实现批量下载
 * 
 * @author Cribbee
 * @version v1.0
 * @date 2017年10月
 */

public class HtmlJsoup {

	/**
	 * 根据网页和编码获取网页内容和源代码
	 * 
	 * @param url
	 * @param encoding
	 */
	public static String getHtmlResourceByUrl(String url, String encoding) {

		StringBuffer buffer = new StringBuffer();
		URL urlOjb = null;
		URLConnection uc = null;
		InputStreamReader in = null;
		BufferedReader reader = null;

		try {
			// 建立网络连接
			urlOjb = new URL(url);
			// 打开网络连接
			uc = urlOjb.openConnection();
			// 创建输入流
			in = new InputStreamReader(uc.getInputStream(), encoding);
			// 创建一个缓冲写入文件流
			reader = new BufferedReader(in);

			String line = null;
			while ((line = reader.readLine()) != null) {
				// 一行一行的追加
				buffer.append(line + "\r\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return buffer.toString();
	}

	// 执行测试程序代码，java程序的入口
	public static void main(String[] args) {

		// 根据网页和编码获取网页内容和源代码
		String htmlSource = getHtmlResourceByUrl("http://www.onehome.me/", "utf-8");
		// System.out.println(htmlSource);

		// 解析网页的源代码
		Document document = Jsoup.parse(htmlSource);
		// 获取所有图片地址
		Elements elements = document.getElementsByTag("img");

		for (Element element : elements) {
			String imgSrc = element.attr("src");
			if (!"".equals(imgSrc) && imgSrc.startsWith("http://")) {
				System.out.println("正在下载的图片地址：" + imgSrc);
				downImages("C://lxy_images", imgSrc);
				System.out.println("--下--载--完--毕--");
			}
			// <img src = "images/1.jpg"/>
		}

		System.out.println("lalalala");
	}

	/**
	 * 根据图片的URL下载的图片到本地的filePath
	 * 
	 * @param filePath
	 * @param imageUrl\
	 */
	public static void downImages(String filePath, String imageUrl) {

		// 截取图片的名称
		String fileName = imageUrl.substring(imageUrl.lastIndexOf("/"));

		// 创建文件的目录结构
		File files = new File(filePath);
		// 是否存在文件夹
		if (!files.exists()) {
			files.mkdirs();
		}

		try {

			URL url = new URL(imageUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			InputStream is = connection.getInputStream();
			// 创建文件
			File file = new File(filePath + fileName);
			FileOutputStream out = new FileOutputStream(file);

			int i = 0;
			while ((i = is.read()) != -1) {
				out.write(i);

			}
			is.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
