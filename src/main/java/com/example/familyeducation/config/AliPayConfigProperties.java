package com.example.familyeducation.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "alipay", ignoreInvalidFields = true)
public class AliPayConfigProperties {

    // 「沙箱环境」应用ID - 您的APPID，收款账号既是你的APPID对应支付宝账号。获取地址；https://open.alipay.com/develop/sandbox/app
    private String app_id;
    // 「沙箱环境」商户私钥，你的PKCS8格式RSA2私钥
    private String merchant_private_key ;
//            = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDU78mHd6L2351Arj4dvUQu/0uDwJoX6UBh71+YZKWcP9nq++EkzCqRzQ60ziby1vR6smzqWgyacGvjRL4B2C/1WMB3OPS7jzW1uMWTx173gt3Wcrno8FN8xThkqpmyd+Jt5B5n35nRvlv0AYOsvA+UayMdcy9AxaiWZMUBjXfyttlMOXbiavRmIIKPvIAWdOf2MhKWZT1KfgAHsNv70lo21PB4SaK06pPa8NMSLs0wxFp7j2mpNicT61h2V06A/14owajy9PPHUrSv+j6nBCMQGHk0BOyyEm6X0j+yOiD2u7vEXpf8JAqIsd1o0lexxiMCs/AD36E1YujqKRqyzqqLAgMBAAECggEAJ40mhLgPGAXvAo2a0hFOseAPmbhTpu9XWxmISxkevcgKZSBF8HzPx2csPR8xktekb4nmuxzgzGrnTz58gIE0X0EN9tcodhIiqs9YO5vDovu20tdaes7w4vMLB3+8rq6f60N89lRNEO1nzQne+F2yEyJXUmgLtd9A8jZjdWdeiUFgnQTUsing5l6iPLklBccuVclXYS3wKg9fQWu04iWzB0k5J/Xso9vQlhfF8+lOQWoOA4sYtz2V9FTEZuSBshx1oPzZp7KaSsgGmPkhtRozNMwAk62vekDfDAMUNMFDr2RA3raXBzEt+j3zXkaNINoOOeek/Y5pN9bg81/fl26TCQKBgQDqduoiRzQs4RoDfEk0wtMz78yQWjDN3/MABuH5KxR4dpp2Dz2mocN/DEArm2Zh2xkM6+xNW2fWcKc5ziHEK6WusiEAgd5JaMfpWLhgcGEY7fB7uow/httlgnk/PYH1IkTU0FXd0VEmVbqrczLlp/WJ/A8/XLrmzEG2Ca6nTO6ZhwKBgQDofq1ngzTdQnQn5gRoWZQzEZzFwIUUH9UlS0FzrJQiH95Fz2C5OXsmhEcfuJSufWN52+lC/gg/uzRKJ5HUTfHH6XvyZKziUoxdFG3eq9GdaFm39J5RLylL5gxXoSANBytgnQXPORlRW5OKnij+w3x0Wabr4/8MAJuZPw36XO8X3QKBgQDNBVyoyrCSCegwYg61hyMdyDONioAglhKr1FjnA8erqrJYR+119v+26aR3PTTinq6iAO6pmiMLfYiM4HXgTvZa80IuamFQucINhtn64mnVXcKqvq4Cos7YTaXuKb+o9eVQXjindXBLYC2R3HMGJL9tgC5pDpIUXUmcf4zsmDYPowKBgQDRFMLq2QLcjBubme6SzSRKvOcIq/bBv1LWpBPra+MXwSMKEtIAIOJQJBs2Oq9zMA4ak5r1hTBz5+3BVk59586sopu1w7X6HpReTLX1zvzGGwa8OP97MPdpayvkTmFddfobe3qy/b13/ev0eAGUAsdUdx92+eXurElCCoOOiK73/QKBgD9CgflUaAc7SToSN7y9YE6mIf7OUWURMm7iYc5o5g3qbh9Xz2yJGUOIm3oQ3GJXsoT0tdq6uIwkK8Lx5YxQVDWdzwWR3VepAP0E/NzsC9bbOjpQtqJ0JyZv321TE7TaW8dTi0f+bQo2CVHDI0NqT+dIJMZ+hrAGAevX7BfJ3A0x";
    // 「沙箱环境」支付宝公钥
    private String alipay_public_key ;
//        ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhfE9mr+Hy3Jk7P1FMBqR7UyqcHoqXqGpD9Fd1BrWPfmwYICkwNQ2M2MDpDus3u2dKmz8IXIvzMQLHMyJMNg5aXU9b7Aqc6g07gkwQDI1vgtepZVKiH9hnPbCRPton7tQSqoeoEjfwWgkvO44s+j0dSrVL9FLvlkyAIikGXy9Hl1Ve/ACoMvCASkV76husOe5nn6F0AsamAQCsnG0o5OHAtUGiOA907Lgj339oCFBdnBAiwtNcUTv1mKrfqejO7E+xiVR8FtNS5NWxNoixr2ha4T9DXTW1+4EcufgR4IFrfRBbefAgfnn4OPdUDw6DrlQxJxm40vQnahwdivHjxFe3QIDAQAB";
    // 「沙箱环境」服务器异步通知页面路径
    private String notify_url;
    // 「沙箱环境」页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    private String return_url;
    // 「沙箱环境」
    private String gatewayUrl;
    // 签名方式
    private String sign_type = "RSA2";
    // 字符编码格式
    private String charset = "utf-8";
    // 传输格式
    private String format = "json";

}
