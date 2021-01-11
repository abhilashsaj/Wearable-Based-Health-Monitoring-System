# TCS-Inframind

## Sustainability & Wellness

### Problem Statement 

A company XYZ has 100 employees working where it has provided each and every employee
with an android support wearable technology to monitor each and every one’s health. The
company’s major concern is the priority of the employees’ health. Now that the company has
executed the first half of their target by providing the employees with wearables. Now they
have to fulfil the second half of their target which would be creating an application to monitor
individually and to govern them as a group.

#### TASKS

Have an IoT device or Open API deployed on server to gather the datasets for the health
impacting parameters like Body Temperature, Blood Pressure, Respiration, Glucose, Heart
Rate, Oxygen Saturation, Electro Cardiogram etc.

Any critical values in any one of the parameters is considered to be putting that particular
person is not health conscious. So, for monitoring purposes applications are needed.

Have a prediction model to find diseases from parameters so that precautions can be taken
beforehand.

#### Design

##### Mobile Application - Single User

###### Screen 1

- UI to show the health parameters value as
mentioned above. 

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

- BlockChain Security

- IMEI identification (Unique ID)

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
