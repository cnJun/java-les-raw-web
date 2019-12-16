package com.zeroten.raweb.controller.wechat;

import com.zeroten.raweb.constant.UrlMapping;
import com.zeroten.raweb.controller.HelloServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.StringBuilderWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet(name = "WechatMessageServlet", urlPatterns = UrlMapping.WECHAT_MESSAGE_RECEIVE)
public class WechatMessageServlet extends HttpServlet {
    private static final Logger log = LogManager.getLogger(HelloServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String signature = req.getParameter("signature");
        String timestamp = req.getParameter("timestamp");
        String nonce = req.getParameter("nonce");
        String echostr = req.getParameter("echostr");

        resp.getWriter().print(echostr);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reqString = getStreamAsString(req, "UTF-8");
        log.info("receive wechat message: {}", reqString);

        resp.getWriter().print(reqString);
    }

    private String getStreamAsString(HttpServletRequest req, String charset) {
        try (InputStream is = req.getInputStream()) {
            StringBuilderWriter sw = new StringBuilderWriter();

            if (charset == null) {
                InputStreamReader in = new InputStreamReader(is);
                copy(in, sw);
            } else {
                InputStreamReader in = new InputStreamReader(is, charset);
                copy(in, sw);
            }

            return sw.toString();
        } catch (IOException e) {
        }
        return "";
    }

    private int copy(Reader input, Writer output) throws IOException {
        long count = copyLarge(input, output);
        return count > 2147483647L ? -1 : (int)count;
    }

    private long copyLarge(Reader input, Writer output) throws IOException {
        return copyLarge(input, output, new char[4096]);
    }

    private long copyLarge(Reader input, Writer output, char[] buffer) throws IOException {
        long count = 0L;

        int n;
        for(; -1 != (n = input.read(buffer)); count += (long)n) {
            output.write(buffer, 0, n);
        }

        return count;
    }

}