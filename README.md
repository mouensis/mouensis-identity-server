# mouensis-identity-server
A unified identity authentication and authorization management platform
#简要
统一身份认证服务器，用于账号浏览器登录，登出。用户、角色、权限、日志等管理。关于系统的认证和授权主要利用Spring Security
的功能进行封装重构，这里重点说明一点，考虑到目前主流的风格是前后端分离的架构，本系统将采用无Session的风格进行开发。
#认证
##验证码
###概述
Spring Security 默认提供用户名和密码组合认证，但是我们在一般应用系统中都会采取验证码来防止机器恶意破解密码，增强系统的安全性。
在使用验证码进行认证之前我们需要先在服务端生成一个图片格式（或二进制字节流）的验证码给客户端显示。
###验证码生成
服务端增加一个验证码端点过滤器（<code>LoginCaptchaEndpointFilter</code>），用于获取验证码的接入点。
该过滤器注入一个验证码生成器（<code>LoginCaptchaGenerator</code>）用于生成验证码图片和验证码文本内容。
注入一个Redis接口<code>StringRedisTemplate</code>用于存储验证码文本内容。
过滤器在生成验证码之后存储Redis是会自动生成一个验证码临时Token值。一方面是用来作为存储验证码文本内容的Key值。
另一方面存储在响应的Header中，Key值为<code>Captcha-Token</code>。
###验证码使用
客户端调用<url>/captcha</url>获取验证码显示，同时存储响应Header中临时Token（<code>Captcha-Token</code>），
在后续提交账号、密码和验证码时需要将该临时Token已同样的格式存储在请求Header中。
###验证码认证
验证码的认证使用独立的过滤器<code>LoginCaptchaAuthenticationFilter</code>，读取Header中的临时Token及请求参数中的验证码文本，
通过验证码校验器<code>LoginCaptcha</code>

##用户密码验证码认证
##手机号密码验证码认证
##邮箱密码验证码认证
##手机号短信码验证码认证
