import os
import firebase_admin
from firebase_admin import credentials
from google.cloud import firestore
from firebase_admin import firestore
import smtplib
import json

cred = credentials.Certificate('key.json')
fault_app = firebase_admin.initialize_app(cred)
db = firestore.client()

server = smtplib.SMTP('smtp.gmail.com',587)
server.starttls()
server.login('abhilashsajtest@gmail.com','tcsinframind')

from datetime import datetime, timedelta
from threading import Timer

x=datetime.today()
y = x.replace(day=x.day, hour=1, minute=0, second=0, microsecond=0) + timedelta(days=1)
delta_t=y-x

secs=delta_t.total_seconds()

def hello_world():
	docs = db.collection(u'users').stream()
	for doc in docs:
		user_doc = doc.to_dict()
		uid = user_doc['uid']
		email = user_doc['email']
		print(uid,email)
		
		doc_ref = db.collection('user_health').document(uid)
		doc = doc_ref.get()

		if doc.exists:
			# print(f'Document data: {doc.to_dict()}')
			user_health = doc.to_dict()

			chd = "\nCHD: " + user_health['chd']
			diabetes = "\nDiabetes: " + user_health['diabetes']
			hypoxemia = "\nHypoxemia: " + user_health['hypoxemia']
			stress = "\nStress: " + user_health['stress']
			bronchi = "\nBronchi: " + user_health['bronchi']
			asthma = "\nAsthma: " + user_health['asthma']

			health_status = "\nHealth Status: \n" +chd+ diabetes+hypoxemia+ stress+ bronchi+asthma

			message = """From: From Person <from@fromdomain.com>
			To: To Person <to@todomain.com>
			MIME-Version: 1.0
			Content-type: text/html
			Subject: SMTP HTML e-mail test

			This is an e-mail message to be sent in HTML format

			<b>This is HTML message.</b>
			<h1>This is headline.</h1>
			"""

			print(message)
			server.sendmail('abhilashsajtest@gmail.com','abhilashsaj@gmail.com', message)
			# print(f'Document data: {doc.to_dict()}')
			
		else:
			print(u'No such document!')

t = Timer(secs, hello_world)
t.start()





    # print(doc.id)

# print(user_ids)







# for uid in user_ids:
# 	collections = db.collection('users').document(uid).collection('health_data').document('05-02-2021').collections()
# 	for collection in collections:
# 	    for doc in collection.stream():
# 	        print(f'{doc.id} => {doc.to_dict()}')
# 	        print(doc.id)



# for uid in user_ids:
# 	collections = db.collection('users').document(uid).collections()
# 	for collection in collections:
# 	    for doc in collection.stream():
# 	        print(f'{doc.id} => {doc.to_dict()}')
# 	        print(doc.id)

# cred = credentials.Certificate("key.json")
# firebase_admin.initialize_app(cred)
