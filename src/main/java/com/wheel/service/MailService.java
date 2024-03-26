package com.wheel.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wheel.controller.request.MailParam;
import com.wheel.controller.response.ProductVO;
import com.wheel.dao.MailDao;
import com.wheel.dao.dataObject.MailDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Example;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Slf4j
@Service
public class MailService {

    @Resource
    FileService fileService;

    @Resource
    MailDao mailDao;

    @Resource
    JavaMailSender javaMailSender;

    @Resource
    Mapper beanMapper;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${spring.mail.to}")
    private String to;

    private static final String sourceFrom = "from <{0}>:{1}";


    public void send(MailParam param) {
        this.add(param);
        if (CollectionUtils.isEmpty(param.getFiles())) {
            sendSimpleMail(param);
        } else {
            List<File> files = fileService.upload(param.getFiles(), true);
            try {
                sendAttachFileMail(param, files);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            } finally {
                files.forEach(File::delete);
            }
        }

    }


    /**
     * 发送简单邮件
     */
    public void sendSimpleMail(MailParam param) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setSubject(MessageFormat.format(sourceFrom, param.getEmail(), param.getSubject()));
        message.setTo(to);
        message.setSentDate(new Date());
        StringBuilder stringBuilder = new StringBuilder();
        JSON.parseArray(param.getProducts())
                .stream()
                .forEach(product -> stringBuilder.append(((JSONObject)product).getString("model"))
                        .append(":")
                        .append("http://43.135.159.250:3000/products/" + ((JSONObject)product).getString("productId"))
                        .append("\r\n"));


        message.setText(param.getMessage()
                + "\r\n----------------------------------------------------------------------------"
                + "\r\nproduct link as below:\r\n"
                + stringBuilder);
        javaMailSender.send(message);
    }

    /**
     * 发送带附件的邮件
     *
     * @throws MessagingException
     */
    public void sendAttachFileMail(MailParam param, List<File> files) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(from);
        helper.setSubject(MessageFormat.format(sourceFrom, param.getEmail(), param.getSubject()));
        helper.setTo(to);
        helper.setSentDate(new Date());
        StringBuilder stringBuilder = new StringBuilder();
        JSON.parseArray(param.getProducts())
                .stream()
                .forEach(product -> stringBuilder.append(((JSONObject)product).getString("model"))
                        .append(":")
                        .append("http://43.135.159.250:3000/products/" + ((JSONObject)product).getString("productId"))
                        .append("\r\n"));
        helper.setText(param.getMessage()
                + "\r\n----------------------------------------------------------------------------"
                + "\r\nproduct link as below:\r\n"
                + stringBuilder);

        Multipart multipart = new MimeMultipart();
        BodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(param.getMessage(), "text/html;charset=UTF-8");
        multipart.addBodyPart(bodyPart);
        // 文件路径
        files.forEach(file -> {
            BodyPart addAttachmentBodyPart = new MimeBodyPart();
            FileDataSource f = new FileDataSource(file);//获取文件路径
            try {
                addAttachmentBodyPart.setDataHandler(new DataHandler(f));
                addAttachmentBodyPart.setFileName(file.getName());
                multipart.addBodyPart(addAttachmentBodyPart);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        mimeMessage.setContent(multipart);
        javaMailSender.send(mimeMessage);

    }

    /**
     * 带图片资源的邮件
     *
     * @throws MessagingException
     */
    public void sendImgResMail() throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject("这是一封测试邮件");
        helper.setFrom("857903497@qq.com");
        helper.setTo("1559908926@qq.com");
        helper.setSentDate(new Date());
        helper.setText("<p>hello 大家好，这是一封测试邮件，这封邮件包含两种图片，分别如下</p><p>第一张图片：</p><img src='cid:p01'/><p>第二张图片：</p><img src='cid:p02'/>", true);
        helper.addInline("p01", new FileSystemResource(new File("C:\\Users\\Administrator\\Desktop\\11.jpg")));
        helper.addInline("p02", new FileSystemResource(new File("C:\\Users\\Administrator\\Desktop\\11.jpg")));
        javaMailSender.send(mimeMessage);
    }


    public MailDO add(MailParam param) {
        MailDO record = beanMapper.map(param, MailDO.class);
        record.setIsRemoved(0);
        mailDao.save(record);
        return record;
    }


    public List<MailDO> findAllByEmail(String email) {
        MailDO query = new MailDO();
        query.setIsRemoved(0);
        Example<MailDO> example = Example.of(query);
        return mailDao.findAll(example);
    }


}
