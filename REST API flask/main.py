from flask import Flask, request
from flask_restful import Api, Resource
import pandas as pd
import random 

import smtplib
import json

import pandas as pd
from sklearn.preprocessing import StandardScaler
from sklearn.decomposition import PCA
import numpy as np
# import pandas as pd

from sklearn.multiclass import OneVsRestClassifier
import pickle

from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler

from sklearn.neighbors import KNeighborsClassifier
from sklearn.svm import SVC
from sklearn.tree import DecisionTreeClassifier
from sklearn.ensemble import RandomForestClassifier
from sklearn.linear_model import LogisticRegression
from sklearn import metrics
from sklearn.naive_bayes import GaussianNB
from sklearn import model_selection

from flask_cors import CORS
	
# from sklearn.ensemble import BaggingClassifier
# from sklearn.ensemble import ExtraTreesClassifier
# from sklearn.ensemble import AdaBoostClassifier
# from sklearn.ensemble import GradientBoostingClassifier
# from sklearn.ensemble import VotingClassifier
# from sklearn.metrics import roc_curve, roc_auc_score

from flask_mail import Mail, Message

app = Flask(__name__)
mail= Mail(app)
# CORS(app)
api = Api(app)

app.config['MAIL_SERVER']='smtp.gmail.com'
app.config['MAIL_PORT'] = 465
app.config['MAIL_USERNAME'] = 'abhilashsajtest2@gmail.com'
app.config['MAIL_PASSWORD'] = 'tcsinframind'
app.config['MAIL_USE_TLS'] = False
app.config['MAIL_USE_SSL'] = True
mail = Mail(app)

# try:
# 	server = smtplib.SMTP('smtp.gmail.com',587)
# 	server.starttls()
# 	server.login('abhilashsajtest2@gmail.com','tcsinframind')
# except SMTPException:
# 	print("Error: unable to send email")


import base64
import hashlib
import json

from Crypto import Random
from Crypto.Cipher import AES

block_size = 16
pad = lambda s: s + (block_size - len(s) % block_size) * chr(block_size - len(s) % block_size)
unpad = lambda s: s[0:-ord(s[-1:])]
iv = Random.new().read(AES.block_size) # Random IV

def get_private_key(secret_key, salt):
    return hashlib.pbkdf2_hmac('SHA256', secret_key.encode(), salt.encode(), 65536, 32)

def encrypt_with_AES(message, secret_key, salt):
    private_key = get_private_key(secret_key, salt)
    message = pad(message)
    cipher = AES.new(private_key, AES.MODE_CBC, iv)
    cipher_bytes = base64.b64encode(iv + cipher.encrypt(message))
    return bytes.decode(cipher_bytes)

def decrypt_with_AES(encoded, secret_key, salt):
    private_key = get_private_key(secret_key, salt)
    cipher_text = base64.b64decode(encoded)
    iv = cipher_text[:AES.block_size]
    cipher = AES.new(private_key, AES.MODE_CBC, iv)
    plain_bytes = unpad(cipher.decrypt(cipher_text[block_size:]));
    return bytes.decode(plain_bytes)


class OpenAPI(Resource):

	def get(self):
		# response.headers["X-Total-Count"] = "1"
		print("OpenAPI invoked - GET")
		return {'post_meal': bool(random.getrandbits(1)),
			'blood_sugar_level': random.randint(0,400),
	        'breaths_per_minute': random.randint(10,30),
	        'is_running': bool(random.getrandbits(1)),
	        'breath_shortness_severity': random.randint(0,10),
	        'cough_frequency': random.randint(0,10),
	        'cough_severity': random.randint(0,10),
	        
	        'blood_pressure_sys': random.randint(50,250),
	        'blood_pressure_dia': random.randint(50,250),
	        'heart_rate': random.randint(60,200),
	        'cholestorol': random.randint(60,200),
	        'oxygen_saturation': random.randint(90,100),
	        'lf/hf ratio': random.uniform(1,2)
	        }


	def post(self):
		print("OpenAPI invoked - POST")
		secret_key = "yourSecretKey"
		salt = "anySaltYouCanUseOfOn"

		
		health_data = {'post_meal': bool(random.getrandbits(1)),
			'blood_sugar_level': random.randint(0,400),
	        'breaths_per_minute': random.randint(10,30),
	        'is_running': bool(random.getrandbits(1)),
	        'breath_shortness_severity': random.randint(0,10),
	        'cough_frequency': random.randint(0,10),
	        'cough_severity': random.randint(0,10),
	        
	        'blood_pressure_sys': random.randint(50,250),
	        'blood_pressure_dia': random.randint(50,250),
	        'heart_rate': random.randint(60,200),
	        'cholestorol': random.randint(60,200),
	        'oxygen_saturation': random.randint(90,100),
	        'lf/hf ratio': round(random.uniform(1,2), 2)
	        }
		plain_text = json.dumps(health_data)
		cipher = encrypt_with_AES(plain_text, secret_key, salt)
		# print("Cipher: " + cipher)
		decrypted = decrypt_with_AES(cipher, secret_key, salt)
		# print("Decrypted " + decrypted)
		
		return {"Cipher":cipher}
        

class EncryptionAES(Resource):
	
	def get(self):	
		secret_key = "yourSecretKey"
		salt = "anySaltYouCanUseOfOn"

		
		health_data = {'post_meal': bool(random.getrandbits(1)),
			'blood_sugar_level': random.randint(0,400),
	        'breaths_per_minute': random.randint(10,30),
	        'is_running': bool(random.getrandbits(1)),
	        'breath_shortness_severity': random.randint(0,10),
	        'cough_frequency': random.randint(0,10),
	        'cough_severity': random.randint(0,10),
	        
	        'blood_pressure_sys': random.randint(50,250),
	        'blood_pressure_dia': random.randint(50,250),
	        'heart_rate': random.randint(60,200),
	        'cholestorol': random.randint(60,200),
	        'oxygen_saturation': random.randint(90,100),
	        'lf/hf ratio': random.uniform(1,2)
	        }
		plain_text = json.dumps(health_data)
		cipher = encrypt_with_AES(plain_text, secret_key, salt)
		# print("Cipher: " + cipher)
		decrypted = decrypt_with_AES(cipher, secret_key, salt)
		# print("Decrypted " + decrypted)

		return {"Plain Text":plain_text,"Cipher":cipher,"Decrypted": decrypted}

		 



class PredictionModels(Resource):

	def get(self):
		email = (request.form['email'])
		# print(email)
		post_meal = bool(request.form['post_meal'])
		blood_sugar_level = int(request.form['blood_sugar_level'])
		breaths_per_minute = int(request.form['breaths_per_minute'])
		is_running = bool(request.form['is_running'])
		breath_shortness_severity = int(request.form['breath_shortness_severity'])
		cough_frequency = int(request.form['cough_frequency'])
		cough_severity = int(request.form['cough_severity'])
		blood_pressure_sys = int(request.form['blood_pressure_sys'])
		blood_pressure_dia = int(request.form['blood_pressure_dia'])
		heart_rate = int(request.form['heart_rate'])
		cholestorol = int(request.form['cholestorol'])
		oxygen_saturation = int(request.form['oxygen_saturation'])
		lf_hf_ratio = float(request.form['lf_hf_ratio'])


		loaded_model = pickle.load(open('diabetes_prediction_model.sav', 'rb'))
		# loaded_model.predict([100,True])
		diabetes = "No"
		if(loaded_model.predict([[blood_sugar_level,post_meal]]) == 0):
			diabetes="No"
		elif(loaded_model.predict([[blood_sugar_level,post_meal]]) == 0):
			diabetes="PreDiabetes"
		else:
			diabetes="Yes"

		loaded_model = pickle.load(open("bronchi.sav", 'rb'))

		bronchi = "No"
		if(loaded_model.predict([[breaths_per_minute, breath_shortness_severity, cough_frequency,cough_severity]]) == 0):
			bronchi="No"
		else:
			bronchi="Yes"

		loaded_model = pickle.load(open("hypoxemia.sav", 'rb'))
		hypoxemia = "No"
		if(loaded_model.predict([[int(oxygen_saturation)]]) == 0):
			hypoxemia="No"
		else:
			hypoxemia="Yes"

		loaded_model = pickle.load(open("asthma.sav", 'rb'))
		asthma = "No"
		if(loaded_model.predict([[oxygen_saturation, heart_rate,breaths_per_minute] ]) == 0):
			asthma="No"
		else:
			asthma="Yes"

		loaded_model = pickle.load(open("CHD.sav", 'rb'))
		chd = "No"
		if(loaded_model.predict([[blood_pressure_sys,blood_pressure_dia, heart_rate,cholestorol] ]) == 0):
			chd="No"
		else:
			chd="Yes"

		loaded_model = pickle.load(open("stress.sav", 'rb'))
		stress = "No"
		if(loaded_model.predict([[lf_hf_ratio, is_running] ]) == 0):
			stress="No"
		else:
			stress="Yes"

		

		return {"stress": stress,"diabetes": diabetes, "bronchi": bronchi,"hypoxemia":hypoxemia, "asthma":asthma, "chd": chd}

	def post(self):		
		print("Prediction models invoked - Auto POST")
		post_meal = True
		
		if(request.form['post_meal'] == "true"):
			post_meal = True
		else: 
			post_meal = False

		secret_key = "yourSecretKey"
		salt = "anySaltYouCanUseOfOn"

		# email_cipher = request.form['uid']
		# print(email_cipher)

		# decrypted = decrypt_with_AES(email_cipher, secret_key, salt)
		# print(decrypted)

		blood_sugar_level = int(request.form['blood_sugar_level'])
		breaths_per_minute = int(request.form['breaths_per_minute'])
		is_running= True
		if(request.form['is_running'] == "true"):
			is_running = True
		else: 
			is_running = False

		breath_shortness_severity = int(request.form['breath_shortness_severity'])
		cough_frequency = int(request.form['cough_frequency'])
		cough_severity = int(request.form['cough_severity'])
		blood_pressure_sys = int(request.form['blood_pressure_sys'])
		blood_pressure_dia = int(request.form['blood_pressure_dia'])
		heart_rate = int(request.form['heart_rate'])
		cholestorol = int(request.form['cholestorol'])
		oxygen_saturation = int(request.form['oxygen_saturation'])
		lf_hf_ratio = float(request.form['lf_hf_ratio'])

		message= ""
		loaded_model = pickle.load(open('diabetes_prediction_model.sav', 'rb'))
		# loaded_model.predict([100,True])
		diabetes = "No"
		if(loaded_model.predict([[blood_sugar_level,post_meal]]) == 0):
			diabetes="No"
		elif(loaded_model.predict([[blood_sugar_level,post_meal]]) == 0):
			message = message + "Warning! Blood Sugar Level very high\n"
			diabetes="PreDiabetes"
		else:
			message = message + "Danger! Blood Sugar Level dangerously high\n"
			diabetes="Yes"

		loaded_model = pickle.load(open("bronchi.sav", 'rb'))

		bronchi = "No"
		if(loaded_model.predict([[breaths_per_minute, breath_shortness_severity, cough_frequency,cough_severity]]) == 0):
			bronchi="No"
		else:
			message = message + "Warning! Abnormalities detected in respiration\n"
			bronchi="Yes"

		loaded_model = pickle.load(open("hypoxemia.sav", 'rb'))
		hypoxemia = "No"
		if(loaded_model.predict([[int(oxygen_saturation)]]) == 0):
			hypoxemia="No"
		else:
			message = message + "Warning! Oxygen Saturation levels running low..\n"
			hypoxemia="Yes"

		loaded_model = pickle.load(open("asthma.sav", 'rb'))
		asthma = "No"
		if(loaded_model.predict([[oxygen_saturation, heart_rate,breaths_per_minute] ]) == 0):
			asthma="No"
		else:
			message = message + "Warning! Abnormalities detected in respiration and heart_rate.. \n"
			asthma="Yes"

		loaded_model = pickle.load(open("CHD.sav", 'rb'))
		chd = "No"
		if(loaded_model.predict([[blood_pressure_sys,blood_pressure_dia, heart_rate,cholestorol] ]) == 0):
			chd="No"
		else:
			message = message + "Warning! High cholestorol and Blood Pressure detected...\n"
			chd="Yes"

		loaded_model = pickle.load(open("stress.sav", 'rb'))
		stress = "No"

		if(loaded_model.predict([[lf_hf_ratio, is_running] ]) == 0):
			stress="No"
		else:
			message = message + "Warning! Stress levels high...\n"
			stress="Yes"

		if(message == ""):
			message = "All Good!"


		print("Models run successfully for home activity")

		


		try: 
			email_id = request.form['email_id']			
			msg = Message('Warning!!!', sender = 'abhilashsajtest2@gmail.com', recipients = [email_id])
			msg.body = message
			# mail.send(msg)
			# print("mail sent successfully")


		except smtplib.SMTPException:
			print("An exception occurred email couldnt be send")

		patient_record = {"status":message,"stress": stress,"diabetes": diabetes, "bronchi": bronchi,"hypoxemia":hypoxemia, "asthma":asthma, "chd": chd}
		# print(patient_record)
		plain_text = json.dumps(patient_record)

		cipher = encrypt_with_AES(plain_text, secret_key, salt)
		# print("Cipher: " + cipher)
		decrypted = decrypt_with_AES(cipher, secret_key, salt)
		# print("Decrypted " + decrypted)
		# print(patient_record)

		return {"Cipher":cipher}

		# return {"diabetes": diabetes, "bronchi": bronchi,"hypoxemia":hypoxemia, "asthma":asthma, "chd": chd}

class ManualPredictionModels(Resource):

	
	def post(self):		
		print("Manual prediction models invoked")
		post_meal = True
		
		if(request.form['post_meal'] == "true"):
			post_meal = True
		else: 
			post_meal = False


		# email_cipher = request.form['uid']
		# print(email_cipher)

		# decrypted = decrypt_with_AES(email_cipher, secret_key, salt)
		# print(decrypted)

		blood_sugar_level = int(request.form['blood_sugar_level'])
		breaths_per_minute = int(request.form['breaths_per_minute'])
		is_running= True
		if(request.form['is_running'] == "true"):
			is_running = True
		else: 
			is_running = False

		breath_shortness_severity = int(request.form['breath_shortness_severity'])
		cough_frequency = int(request.form['cough_frequency'])
		cough_severity = int(request.form['cough_severity'])
		blood_pressure_sys = int(request.form['blood_pressure_sys'])
		blood_pressure_dia = int(request.form['blood_pressure_dia'])
		heart_rate = int(request.form['heart_rate'])
		cholestorol = int(request.form['cholestorol'])
		oxygen_saturation = int(request.form['oxygen_saturation'])
		lf_hf_ratio = float(request.form['lf_hf_ratio'])

		message= ""
		loaded_model = pickle.load(open('diabetes_prediction_model.sav', 'rb'))
		# loaded_model.predict([100,True])
		diabetes = "No"
		if(loaded_model.predict([[blood_sugar_level,post_meal]]) == 0):
			diabetes="No"
		elif(loaded_model.predict([[blood_sugar_level,post_meal]]) == 0):
			message = message + "Warning! Blood Sugar Level very high\n"
			diabetes="PreDiabetes"
		else:
			message = message + "Danger! Blood Sugar Level dangerously high\n"
			diabetes="Yes"

		loaded_model = pickle.load(open("bronchi.sav", 'rb'))

		bronchi = "No"
		if(loaded_model.predict([[breaths_per_minute, breath_shortness_severity, cough_frequency,cough_severity]]) == 0):
			bronchi="No"
		else:
			message = message + "Warning! Abnormalities detected in respiration\n"
			bronchi="Yes"

		loaded_model = pickle.load(open("hypoxemia.sav", 'rb'))
		hypoxemia = "No"
		if(loaded_model.predict([[int(oxygen_saturation)]]) == 0):
			hypoxemia="No"
		else:
			message = message + "Warning! Oxygen Saturation levels running low..\n"
			hypoxemia="Yes"

		loaded_model = pickle.load(open("asthma.sav", 'rb'))
		asthma = "No"
		if(loaded_model.predict([[oxygen_saturation, heart_rate,breaths_per_minute] ]) == 0):
			asthma="No"
		else:
			message = message + "Warning! Abnormalities detected in respiration and heart_rate.. \n"
			asthma="Yes"

		loaded_model = pickle.load(open("CHD.sav", 'rb'))
		chd = "No"
		if(loaded_model.predict([[blood_pressure_sys,blood_pressure_dia, heart_rate,cholestorol] ]) == 0):
			chd="No"
		else:
			message = message + "Warning! High cholestorol and Blood Pressure detected...\n"
			chd="Yes"

		loaded_model = pickle.load(open("stress.sav", 'rb'))
		stress = "No"

		if(loaded_model.predict([[lf_hf_ratio, is_running] ]) == 0):
			stress="No"
		else:
			message = message + "Warning! Stress levels high...\n"
			stress="Yes"

		if(message == ""):
			message = "All Good!"


		print("Manual models run successfully")

		


		# try: 			
		# 	# print(int(request.form['blood_sugar_level']))
		# 	email_message = 'Subject: {}\n\n{}'.format("Warning!!! ", message)
		# 	email_id = request.form['email_id']
		# 	# print("hii")
		# 	# server.sendmail('abhilashsajtest2@gmail.com',email, email_message)
		# 	# print(email_message)
		# except smtplib.SMTPException:
		# 	print("An exception occurred email couldnt be send")


		print(message)

		return {"status":message,"stress": stress,"diabetes": diabetes, "bronchi": bronchi,"hypoxemia":hypoxemia, "asthma":asthma, "chd": chd}

		# return {"diabetes": diabetes, "bronchi": bronchi,"hypoxemia":hypoxemia, "asthma":asthma, "chd": chd}




api.add_resource(OpenAPI, "/")
api.add_resource(PredictionModels, "/prediction_models")
api.add_resource(ManualPredictionModels, "/manual_prediction_models")

if __name__ == "__main__":
	# app.run(debug=True)
	app.run(host='0.0.0.0',threaded=True)


