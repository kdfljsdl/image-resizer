Image resizer and uploader bot/

1. Implementation description.
The requirements are described in the file uploader_bot_java.compressed.pdf, which can be found in same folder with this file.
The bot uses cloudamqp.com cloud MQ provider base on RabbitMQ.
Image resizer uses java capabilities to process images along with imgscalr library.
Dropbox is used as remote cloud storage.
Proxy support is not implemented. So the computer which runs bot must have direct internet access.


2. Installation.
- put image-resizer forled to desired location, for example /opt/image-resizer
- make script file bin/bot executable


2. Configuration.
Configuration is very simple. Configuration file is ./conf/bot.properties.
2.1. Setup cloudamqp connection:
 - go to https://customer.cloudamqp.com/login. Sign up, or log in, if you already have an account there. I used my account login: oxun@list.ru, password:ejnu83fhf9f.
 - go to https://customer.cloudamqp.com/instance and create an instance.
 - on the page with the list of instances, choose desired instance and press "Details" button, the blue one.
 From that page you need to get values and set them to conf/bot.properties file, in the following way:
    Server 	->  amqp.host property
    User & Vhost -> amqp.virtualHost and amqp.username
    Password -> amqp.password
 - enter RabbitMQ management interface and create 4 queues (durability=durable, auto delete=no):
    resize
    upload
    done
    failed

2.2. Setup drop box cloud storage:
 - login to dropbox.com.
 - go to https://www.dropbox.com/developers/apps/ page. Create an app or use an existing one if have one.
 - enter app configuration page and generate access toke. Set access toke value to cloudstorage.dropBox.AccessToken property in the file bot.properties.
 - set "Allow implicit grant" to Allow


3. Run the bot.
 Bot works according to specification defined in uploader_bot_java.compressed.pdf file.
 Put files to some foled which is accessing to the bot.
 Run:
 ./bot schedule FOLDER_NAME, where FOLDED_NAME-the folder with images
 ./bot resize
 ./bot upload

 After that you shoud see uploaded images on dropbox.com in the folder defined by cloudstorage.dropBox.dropBoxFolder in conf/bot.property file.



