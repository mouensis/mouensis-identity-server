package com.mouensis.server.identity.security.captcha;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 默认生成器
 *
 * @author zhuyuan
 * @date 2020/12/16 11:18
 */
public class DefaultLoginCaptchaGenerator implements LoginCaptchaGenerator {

    @Override
    public LoginCaptcha generate() {
        //创建一张图片
        BufferedImage verifyPic = new BufferedImage(120, 40, BufferedImage.TYPE_3BYTE_BGR);
        //通过图片获取画笔
        Graphics2D g = verifyPic.createGraphics();
        //准备一个字母+数字的字典
        String letters = "23456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        //规定验证码的位数
        int verifyLength = 4;
        //生成随机验证码
        String verifyCode = "";
        //循环取值
        for (int i = 0; i < verifyLength; i++) {
            verifyCode += letters.charAt((int) (Math.random() * letters.length()));
        }
        //将图片的底板由黑变白
        g.setColor(Color.white);
        g.fillRect(0, 0, 120, 40);
        //将验证码画在图片之上
        g.setFont(new Font("微软雅黑", Font.BOLD, 24));
        for (int i = 0; i < verifyLength; i++) {
            //随机产生一个角度
            double theta = Math.random() * Math.PI / 4 * ((int) (Math.random() * 2) == 0 ? 1 : -1);
            //产生偏转
            g.rotate(theta, 24 + i * 22, 20);
            //每画一个字幕之前都随机给一个颜色
            g.setColor(new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)));
            g.drawString(verifyCode.charAt(i) + "", 20 + i * 22, 26);
            //回正
            g.rotate(-theta, 24 + i * 22, 20);
        }
        //加入干扰线
        for (int i = 0; i < 5; i++) {
            //给随机颜色
            g.setColor(new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)));
            //画线
            g.drawLine((int) (Math.random() * 120), (int) (Math.random() * 40),
                    (int) (Math.random() * 120), (int) (Math.random() * 40));
        }
        //设置边框颜色
        g.setColor(Color.black);
        //给验证码一个外边框
        g.drawRect(0, 0, 118, 38);

        //验证码字节输出流
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            ImageIO.write(verifyPic, "png", stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new LoginCaptcha(verifyCode, stream.toByteArray());
    }

    public static void main(String[] args) {
        System.out.println(new DefaultLoginCaptchaGenerator().generate());
    }

}
