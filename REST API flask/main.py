from flask import Flask 
from flask_restful import Api, Resource
import pandas as pd
import random 

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
# from sklearn.ensemble import BaggingClassifier
# from sklearn.ensemble import ExtraTreesClassifier
# from sklearn.ensemble import AdaBoostClassifier
# from sklearn.ensemble import GradientBoostingClassifier
# from sklearn.ensemble import VotingClassifier
# from sklearn.metrics import roc_curve, roc_auc_score

app = Flask(__name__)
api = Api(app)

class OpenAPI(Resource):
	def post(self):
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
        'oxygen_saturation': random.randint(90,100)
        }
        

class PredictionModels(Resource):
	def get(self):		
		loaded_model = pickle.load(open('diabetes_prediction_model.sav', 'rb'))
		# loaded_model.predict([100,True])
		diabetes = "No"
		if(loaded_model.predict([[100,True]]) == 0):
			diabetes="No"
		elif(loaded_model.predict([[100,True]]) == 0):
			diabetes="PreDiabetes"
		else:
			diabetes="Yes"

		loaded_model = pickle.load(open("bronchi.sav", 'rb'))

		bronchi = "No"
		if(loaded_model.predict([[22,20,8,8]]) == 0):
			bronchi="No"
		else:
			bronchi="Yes"

		loaded_model = pickle.load(open("hypoxemia.sav", 'rb'))
		hypoxemia = "No"
		if(loaded_model.predict([[22]]) == 0):
			hypoxemia="No"
		else:
			hypoxemia="Yes"

		loaded_model = pickle.load(open("asthma.sav", 'rb'))
		asthma = "No"
		if(loaded_model.predict([[92, 90,30] ]) == 0):
			asthma="No"
		else:
			asthma="Yes"

		loaded_model = pickle.load(open("CHD.sav", 'rb'))
		chd = "No"
		if(loaded_model.predict([[120,90, 90,200] ]) == 0):
			chd="No"
		else:
			chd="Yes"

		return {"diabetes": diabetes, "bronchi": bronchi,"hypoxemia":hypoxemia, "asthma":asthma, "chd": chd}


api.add_resource(OpenAPI, "/")
api.add_resource(PredictionModels, "/prediction_models")

if __name__ == "__main__":
	# app.run(debug=True)
	app.run(host="0.0.0.0", port=5000, debug=True)


