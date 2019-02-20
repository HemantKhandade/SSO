# SSO
A sample application to build Single Sign On Application with ApacheDS LDAP server and Java. 
This is the application that will validate the user credentials against ApacheDS LDAP Server. 
The user is validated against id. However, the precondition is that the LDAP server should be up and running. 
In the current state, the LDAP server is not set, so please set the LDAP server and use the domain as "mydomain.com" as
this is the domin that the code searches on. 
The controller receives authentication request from various applications in the same domain and for a valid user, returns 
a JWT token. The JWT token is valid for 5 mins. 
Please see the code of other two projects "ApplOne" and "ApplTwo" which will be client to this application. 
I am not sure if I can upload a docker image of LDAP server along with Data to Github so that the application can be run end to end.
