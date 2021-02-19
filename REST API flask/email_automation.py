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
server.login('abhilashsajtest2@gmail.com','tcsinframind')

# manual 

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

		summary = "\n\nSummary\n" + user_health['status']

		health_status = "\nHealth Status: \n" +chd+ diabetes+hypoxemia+ stress+ bronchi+asthma +summary

		message = """From: Abhilash Saj <abhilashsajtest2@gmail.com>
		To: User <%s>
		MIME-Version: 1.0
		Content-type: text/html
		Subject: Daily Health Update

		%s
		"""

		# print(message % (email, health_status))
		message = 'Subject: {}\n\n{}'.format("Daily User health report", health_status)
		server.sendmail('abhilashsajtest2@gmail.com',email, message)
		print(f'Document data: {doc.to_dict()}')
		
	else:
		print(u'No such document!')


# #Scheduling code

# from datetime import datetime, timedelta
# from threading import Timer

# x=datetime.today()
# y = x.replace(day=x.day, hour=1, minute=0, second=0, microsecond=0) + timedelta(days=1)
# delta_t=y-x

# secs=delta_t.total_seconds()

def healthSummary():
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

			message = """From: From Person <abhilashsaj@gmail.com>
			To: To Person <%s>
			MIME-Version: 1.0
			Content-type: text/html
			Subject: Daily Health Update

			%s
			"""

			print(message % (email, health_status))
			server.sendmail('abhilashsajtest@gmail.com','abhilashsaj@gmail.com', message)
			# print(f'Document data: {doc.to_dict()}')
			
		else:
			print(u'No such document!')

# t = Timer(secs, healthSummary)
# t.start()


