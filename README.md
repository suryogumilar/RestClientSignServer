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

*catatan tambahan: ini berbeda dengan certificate yang nanti digunakan oleh rest-camel-springboot* 

### enabling https for springboot

lihat pada application.yaml dan isi environment untuk entri keystore yg terdapat dalam file tersebut 

#### Buat key pair untuk Rest Service

*bedakan dengan keypair untuk akses signserver di atas*

menggunakan openssl

 - **generate pair key**   
   `openssl.exe req -newkey rsa:2048 -nodes -keyout key.pem -x509 -days 3650 -out certificate.pem`
 - **Combine your key and certificate in a PKCS#12 (P12) bundle:**   
   `openssl pkcs12 -inkey key.pem -in certificate.pem -export -out certificate.p12`
 - **Periksa p12 bundle untuk melihat alias untuk entry key yg dibuat**   
   biasanya alias name yang dibuat oelh openssl berupa numeric, misal 1  
   `keytool -list -v -keystore certificate.p12`

### isi entry keystore trustore

isi environment untuk entri keystore yang telah dibuat ke dalam application.yaml. Contoh entri (untuk keystore type):
 - key-store-type : PKCS12

untuk truststore; import certificate pengakses rest service ke dalam truststore rest service.
`keytool -import -trustcacerts -noprompt -alias ca -ext san=dns:ss.gehirn.org,ip:127.0.0.1 -file certificate.pem -keystore truststore_rest_service.jks`

check:
`keytool -list -v -keystore truststore_rest_service.jks`

certificate.pem adalah certificate pengakses rest service, misal browser, postman atau apps

test koneksi melalui
`openssl s_client -state -connect <request-domain>:<request-port> -cert /path/to/client-cert -key /path/to/client-cert-key | openssl x509 -text`

#### Java environment untuk CN yang terdaftar dalam truststore

info ini diperlukan untuk mendaftarkan akses username(CN). terdapat dalam main class file `RestClientSignServerApplication`   

List CN yang terdaftar dalam truststore dipisahkan oleh karakter ':'.

java envi = `CN_LIST`   

contoh entri:
`CN_LIST=ss.gehirn.org:seele browser`

envi lainnya yang perlu diset:
 - CN_LIST *sudah dijelaskan di atas*
 - key-alias
 - key-store-passwd
 - key-store-type
 - posisi-key-store
 - posisi-trust-store
 - trust-store-passwd
 - enable-csrf
 - UNICODE_VERSION
 - WORKER_NAME
 - WSDL_LOC

enable-csrf diset ke false (disable) jika ingin service bermethode POST bisa diakses client beda site, jika opsi ini diaktifkan, CN client mungkin harus disamakan dengan hostname service rest ini.   

Untuk method GET *enable-csrf* tidak berpengaruh. Service yang didefinisikan dengan method get tetap bisa diakses walau opsi ini *true* maupun *false*.

#### contoh script

```sh
#/bin/sh
export CN_LIST='seele browser:ss.gehirn.org'
export enable_csrf=false
export key_alias=1
export key_store_passwd=*****
export key_store_type=PKCS12
export posisi_key_store=/mnt/forrest/certificate.p12
export posisi_trust_store=/mnt/forrest/truststore_rest_service.jks
export trust_store_passwd=*****
export UNICODE_VERSION=040000
export WORKER_NAME=MRTDSODSigner
export WSDL_LOC=https://ss.gehirn.org:8443/signserver/ClientWSService/ClientWS?wsdl
cd /usr/src/app/
java -Djavax.net.ssl.trustStore=/mnt/forss/cacerts_4_java_apps.jks -Djavax.net.ssl.trustStorePassword=**** -Djavax.net.ssl.keyStorePassword=**** -Djavax.net.ssl.keyStore=/mnt/forss/mrtdauth_keystore.jks -jar ./apps.jar
```

#### referensi
 - https://www.baeldung.com/spring-boot-https-self-signed-certificate
 - https://www.baeldung.com/x-509-authentication-in-spring-security
 - https://www.gitmemory.com/issue/postmanlabs/postman-app-support/9751/803756371
 - https://stackoverflow.com/questions/51363021/spring-security-x509-403-forbidden
 - https://stackoverflow.com/questions/44824382/how-to-disable-csrf-in-spring-using-application-properties/45383128
 - https://owasp.org/www-community/attacks/csrf
 - https://www.baeldung.com/executable-jar-with-maven
 - https://stackoverflow.com/questions/37698473/spring-boot-control-target-jar-file-name
 - https://stackoverflow.com/questions/33022827/spring-boot-app-not-picking-up-application-properties
 - https://github.com/spring-projects/spring-boot/issues/3477
 - https://stackoverflow.com/questions/4238944/controlling-maven-final-name-of-jar-artifact
 - https://www.studytonight.com/post/solved-caused-by-javalangclassnotfoundexception-javaxxmlwswebservicefeature-in-java-11