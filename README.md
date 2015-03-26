This repository contains the source code for [http://hawaiihoops.scotthonda.cloudbees.net/](http://hawaiihoops.scotthonda.cloudbees.net/).

## What is Hawaii Hoops?
Hawaii Hoops is a social network for basketball players on Oahu. The website's mission is to make it easy for players to not only connect to one another but to find local teams, leagues, and basketball courts.


## I don't live in Hawaii...
Don't live in Hawaii and want the services provided by Hawaii Hoops in your area? No problem, Hawaii Hoops can be easily modified to fit the needs of any location or sport. Simply install Hawaii Hoops on your local machine and input local data for local basketball courts before deployment.

##Dependencies
- [JDK 6 or later](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
- [Play! Framework 2.0](http://www.playframework.com/)

## Installation
Download the [zip file](https://github.com/hawaiihoopsnetwork/HawaiiHoopsNetwork/archive/master.zip), or clone the repository  
```
$ git clone https://github.com/hawaiihoopsnetwork/HawaiiHoopsNetwork.git
Cloning into 'HawaiiHoopsNetwork'...
remote: Reusing existing pack: 774, done.
remote: Counting objects: 109, done.
remote: Compressing objects: 100% (102/102), done.
remote: Total 883 (delta 42), reused 0 (delta 0)
Receiving objects: 100% (883/883), 1.60 MiB | 215.00 KiB/s, done.
Resolving deltas: 100% (429/429), done.
Checking connectivity... done.
```  

In conf/application.conf edit the default database and mailer settings  
```
# Database configuration
# ~~~~~
# You can declare as many datasources as you want.
# By convention, the default datasource is named `default`
#
 db.default.driver=org.h2.Driver
 db.default.url="jdbc:h2:mem:play;MODE=MYSQL"
 db.default.user=user
 db.default.password=password

# Mailer:
smtp.host=smtp.gmail.com
smtp.port=587
smtp.ssl=yes
smtp.user=user
smtp.password=password
#smtp.user=${HiHoops_Email}
#smtp.password=${HiHoops_Email_Password}
```
run `$ play run`  
view the running application at [localhost:9000](localhost:9000)

##Screencasts
<a href="https://www.youtube.com/watch?v=6WZrLIXGf3o"><img height="60%" width="60%" src="https://raw.githubusercontent.com/hawaiihoopsnetwork/HawaiiHoopsNetwork/af13c95313c627a59fe94a2a246bf02587985583/public/images/HiHoopsDev.png"></a><br/>
[Developer Screencast](https://www.youtube.com/watch?v=6WZrLIXGf3o)

<a href="http://youtu.be/x4H4cRifssg"><img height="60%" width="60%" src="https://raw.githubusercontent.com/hawaiihoopsnetwork/HawaiiHoopsNetwork/master/public/images/UserScreenShot.png"></a><br/>
[Users Screencast](http://youtu.be/x4H4cRifssg)

##Comments? Issues?
Please Create a [github issue](https://github.com/hawaiihoopsnetwork/HawaiiHoopsNetwork/issues?milestone=4&state=open)
##Wiki Links
[Team Meetings](https://github.com/hawaiihoopsnetwork/HawaiiHoopsNetwork/wiki/Team-Meetings)  
[Use Cases](https://github.com/hawaiihoopsnetwork/HawaiiHoopsNetwork/wiki/Use-Cases-3.0)  
[Developer Guide](https://github.com/hawaiihoopsnetwork/HawaiiHoopsNetwork/wiki/Developer-Guide)  
[User Guide](https://github.com/hawaiihoopsnetwork/HawaiiHoopsNetwork/wiki/User-Guide)  
[Home Page](http://hawaiihoopsnetwork.github.io/HawaiiHoopsNetwork/)  
[User Evaluations](https://github.com/hawaiihoopsnetwork/HawaiiHoopsNetwork/wiki/User-Evaluations)  

