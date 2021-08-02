# TCS-Inframind - Wearable based Employee Health Monitoring System

## Sustainability & Wellness

### Problem Statement 

A company XYZ has 100 employees working where it has provided each and every employee
with an android support wearable technology to monitor each and every one’s health. The
company’s major concern is the priority of the employees’ health. Now that the company has
executed the first half of their target by providing the employees with wearables. Now they
have to fulfil the second half of their target which would be creating an application to monitor
individually and to govern them as a group.

#### Technologies used

Frontend 
  * React Admin, Material UI, Android
  
Backend
  * Flask -  Prediction Models, Open API, Mail 
  * Sklearn, Crypto, Fitbit
  * Firebase – Firestore, Auth, Storage


#### Tasks & Considerations

##### Tasks - Completed
  - IoT device or Open API deployed 
  - Mobile and Web applications 
  - Disease Prediction Models
  - Stress Management
  - Chatbot and Email Automation
  
#### Considerations - Completed
  - Fitbit/Google integration 
  - Realtime Sync
  - Encryption 
  - Unique ID


#### Design

##### Mobile Application - Single User

###### Screen 1

- User Log In/ Sign Up

- UI to show the health parameters from IOT/Open API

- After getting the values
from wearable to smartphone upload the
values in your personal cloud like firebase
for every 30 seconds.

###### Screen 2 

- Option to add manual data with body text or
document like medical records and view
them whenever needed. 

- Chatbot that gives the health condition status and helps to find the medical records from
the earlier list uploaded based on file name and provides fitness queries.

- FItbit/Google fit integration

- Encryption 

- Device ID

##### Web Application (To manage multiple users health)

###### Page 1

- Now from the database that contains all
users records with their health information.
Visualize their health information based on
critical and normal values for each and every
parameter mentioned above. 

###### Page 2

- View the medical records of all the users
based the username search criteria and list
them wisely.


##### Email Automation

Email Automation that runs daily at the end of the day which collects the user’s health
information and provides their condition of their health summary for each and every user.
SMS/ Email upon major critical health warning such as reducing sugar level, asthma, etc.
