# Messaging application

Messaging app based on a Client-Server model that works with CLI arguments.




Server arguments:
1)Server token(e.g 5000)

First and second arguments of Client: localhost <insert-server-token>



Client requests: 

1) Create account: localhost <insert-server-token> 1 <insert-name>
CLI will output an authentication code that will be used later. (numbers in ascending order)

2) list all users: 2 <authentication-token>
 
3) send message: 3 <authentication-token> <username-of-receiver> <message>

4) list messages: 4 <authentication-token>

5) read message: 5 <authentication-token> <number-of-message>

6) delete message: 6 <authentication-token> <number-of-message> 


isIdentifier restrictions are applied to Username. 
Unread messages are marked with an asterisk(*)
