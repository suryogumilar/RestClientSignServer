# Rest Client for Sign Server (Primekey)

using apache camel to build Rest service connected to a sign server

### Steps to built project

- Open [Spring initializer](https://start.spring.io/)
    - choose; maven, language=Java, Spring boot version etc..
    - add dependencies : Spring Boot DevTools, Spring Web, Spring Boot Actuator (for ops), Apache camel
    - generate

## HTTPS Camel

### enabling https for CXF Lib 

untuk menjalankan, pasang argumen ssl pada JVM sehingga lib cxf bisa menggunakan keystore dan trust store untuk mengakses server backend yang menggunakan authorization client certificate.

```
-Djavax.net.ssl.trustStore=C:\certa\cacerts_4_java_apps.jks
-Djavax.net.ssl.trustStorePassword=passw0rd_cacert 

-Djavax.net.ssl.keyStorePassword=passw0rd 
-Djavax.net.ssl.keyStore=C:\tuk_ws\mrtdauth_keystore.jks
```

### enabling https for springboot

lihat pada application.yaml dan isi environment untuk entri keystore yg terdapat dalam file tersebut

#### referensi
 - https://www.baeldung.com/spring-boot-https-self-signed-certificate
