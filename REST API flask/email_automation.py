import os
import firebase_admin
from firebase_admin import credentials
from google.cloud import firestore
from firebase_admin import firestore


cred = credentials.Certificate('key.json')
fault_app = firebase_admin.initialize_app(cred)
db = firestore.client()


docs = db.collection(u'users').stream()

user_ids = []
for doc in docs:
    # print(f'{doc.id} => {doc.to_dict()}')
    user_ids.append(doc.id)
    # print(doc.id)

print(user_ids)

for uid in user_ids:
	doc_ref = db.collection('user_health').document(uid)

	doc = doc_ref.get()
	if doc.exists:
	    print(f'Document data: {doc.to_dict()}')
	else:
	    print(u'No such document!')





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
