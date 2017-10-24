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
 * ͨ��Html����ʵ����������
 * 
 * @author Cribbee
 * @version v1.0
 * @date 2017��10��
 */

public class HtmlJsoup {

	/**
	 * ������ҳ�ͱ����ȡ��ҳ���ݺ�Դ����
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
			// ������������
			urlOjb = new URL(url);
			// ����������
			uc = urlOjb.openConnection();
			// ����������
			in = new InputStreamReader(uc.getInputStream(), encoding);
			// ����һ������д���ļ���
			reader = new BufferedReader(in);

			String line = null;
			while ((line = reader.readLine()) != null) {
				// һ��һ�е�׷��
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

	// ִ�в��Գ�����룬java��������
	public static void main(String[] args) {

		// ������ҳ�ͱ����ȡ��ҳ���ݺ�Դ����
		String htmlSource = getHtmlResourceByUrl("http://www.onehome.me/", "utf-8");
		// System.out.println(htmlSource);

		// ������ҳ��Դ����
		Document document = Jsoup.parse(htmlSource);
		// ��ȡ����ͼƬ��ַ
		Elements elements = document.getElementsByTag("img");

		for (Element element : elements) {
			String imgSrc = element.attr("src");
			if (!"".equals(imgSrc) && imgSrc.startsWith("http://")) {
				System.out.println("�������ص�ͼƬ��ַ��" + imgSrc);
				downImages("C://lxy_images", imgSrc);
				System.out.println("--��--��--��--��--");
			}
			// <img src = "images/1.jpg"/>
		}

		System.out.println("lalalala");
	}

	/**
	 * ����ͼƬ��URL���ص�ͼƬ�����ص�filePath
	 * 
	 * @param filePath
	 * @param imageUrl\
	 */
	public static void downImages(String filePath, String imageUrl) {

		// ��ȡͼƬ������
		String fileName = imageUrl.substring(imageUrl.lastIndexOf("/"));

		// �����ļ���Ŀ¼�ṹ
		File files = new File(filePath);
		// �Ƿ�����ļ���
		if (!files.exists()) {
			files.mkdirs();
		}

		try {

			URL url = new URL(imageUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			InputStream is = connection.getInputStream();
			// �����ļ�
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
