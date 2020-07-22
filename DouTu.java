package com.cczhang.dt;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

/**
 * @author zhangcongcong
 * @date 2020年7月22日15:18:37
 * #description 别人有的，你也可以用  python可以写的 我Java也可以
 */
public class DouTu {

    //图片地址
    //http://www.adoutu.com/picture/list/

    public static final int MAX_PAGE = 2;
    public static int CURRENT_PAGE = 1;
    public final String FILE_DOWNLOAD_PATH = "C:\\FTP\\Java\\PC\\";
    public final String[] MATCH_CHAR = new String[]{"<img src=\"", "title=\"", "href=\"/picture/"};

    public void TuList(String netURL) throws IOException {
        URL url = null;
        URLConnection urlconn = null;
        BufferedReader br = null;
        try {
            url = new URL(netURL);//爬取的网址、这里爬取的是一个生物网站
            urlconn = url.openConnection();
            br = new BufferedReader(new InputStreamReader(
                    urlconn.getInputStream()));
            String buf = null;
            while ((buf = br.readLine()) != null && buf != "") {
                boolean match = true;
                for (int i = 0; i < MATCH_CHAR.length; i++) {
                    if (!buf.contains(MATCH_CHAR[i])) {
                        match = false;
                    }
                }
                if (match) {
                    buf = buf.trim();
                    String netPicUrl = buf.substring(buf.indexOf("src="),buf.indexOf("title=")).replace("src=","").replace("\"","");
                    downloadPic(netPicUrl,new Date().getTime()+netPicUrl.substring(netPicUrl.lastIndexOf(".",netPicUrl.length())),FILE_DOWNLOAD_PATH);
                }
            }
            System.out.println("爬取成功^_^");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void downloadPic(String netUrl, String fileName, String keepPath) throws IOException {
        // 构造URL
        URL url = new URL(netUrl);
        // 打开连接
        URLConnection con = url.openConnection();
        // 输入流
        InputStream is = con.getInputStream();
        // 1K的数据缓冲
        byte[] bs = new byte[2048];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        if(new File(keepPath).exists()){
            new File(keepPath).mkdirs();
        }
        String filename = keepPath + fileName;  //下载路径及下载图片名称
        File file = new File(filename);
        FileOutputStream os = new FileOutputStream(file, true);
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接
        os.close();
        is.close();
        System.out.println("地址:"+netUrl+"素材已保存至"+keepPath);
    }

    public static void main(String[] args) throws IOException {
        DouTu douTu = new DouTu();
        for(int i = CURRENT_PAGE ; i < MAX_PAGE ; i ++){
            douTu.TuList("http://www.adoutu.com/picture/list/"+CURRENT_PAGE);
        }
    }
}
