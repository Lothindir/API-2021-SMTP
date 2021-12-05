## Introduction
The main goal of this project is to become familiar with the SMTP protocol. We were asked by our professor to develop a client application that plays pranks on a list of victims. This app will generate a number of pranks based on properties specified in a config file, a list of messages and a list of victims. Then, the app will communicate with a SMTP server in order to send the prank e-mail to the targeted victims.

As we don't want to disturb a live SMTP server, we will use a test server called MockMock. It will act as a normal server, let us look at the requests we make and check if our app works, but it won't dispatch the pranks to the real e-mail addresses.

## MockMock installation
The MockMock being used in this project is the following : [MockMock](https://github.com/HEIGVD-Course-API/MockMock)

First you need to open a terminal in the folder containing the provided DockerFile. Make sure you have docker installed and that the service is running.

```
# Check if the service is running
systemctl status docker.service

# Start the docker service in the current session
systemctl start docker.service

# To enable docker at boot
systemctl enable docker.service

```

Then, use `docker build -t mockmock .` to build an image named *mockmock*. This step might take a couple of minutes. You might need to use **sudo** for this command to work.

Once it's done, use `docker run -d  -p 25:25 -p 8282:8282 mockmock` to create a container running *mockmock* image and run it in detached mode. You can remove *-d* if you want to see it in your terminal. 
Now, go to your favorite browser and type `localhost:8282`, the MockMock web interface should appear.

When you're done using the container, kill it with `docker kill <container_id>`. You can see your container id with `docker ps -a`.

## Set up
Now that you have a MockMock server running in the background you start by configuring your app. This SMTP application contains three files : config.properties, messages.utf8, victims.utf8. In the first one you will find the following properties :
```java
smtpServerAddress=127.0.0.1
smtpServerPort=25
numberOfGroups=1
personToCC=bob.dylan@rock.ch, michael.jackson@thriller.com
```

Feel free to edit the properties. In the second file you will find two templates of message you could send. Each message starts with a subject line and ends with `----------------`. You can add as many messages as you like, the app will chose one randomly and assign it to a prank.
The last file contains the list of e-mail addresses. Be sure to have correctly formatted addresses otherwise the app won't work.


## Implémentation

## Class diagram

![]()

We decided to divide our project in four packages : config, mail, prank, 
smtp. 
The SpamiBot class is the entry point of the application. It creates an SMTP 
client with some properties from a configuration manager. Generates a list 
of pranks using the PrankManager and sends each custom Mail to the client.

### Config

This package contains the different configuration files such as config.
properties, messages.utf8 and victims.utf8. We decided to create a 
ConfigurationManager class, its main goal is to fetch the configuration from 
the files.

### Mail
In this package you will find a Group class representing a group of person 
affected by the prank attack. 

The Mail class stores all the information of the e-mail to send such as the 
sender's address, the subject, the body, the victims' address and the people 
to set as carbon copy. Every information is stored as a String.

Obviously it's a class representing a person. It stores the e-mail address 
fetched in the files.

### Prank
Here we put the Prank class. It contains all the information regarding the 
prank attack (victims, group, mail, sender). 
Its mail object will be 
transferred to the smtp client and sent to the victims.

Then there is the PrankManager, it simply generates groups from the list of 
people and creates a custom prank for each one of them.


## Smtp
The SMTP Client only task is to communicate with the SMTP server and execute 
the attack. It uses the SMTP protocol to send the information to the server 
and listens for its response.







