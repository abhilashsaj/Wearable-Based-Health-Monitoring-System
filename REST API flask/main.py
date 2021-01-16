from flask import Flask 
from flask_restful import Api, Resource
import pandas as pd
import random 

app = Flask(__name__)
api = Api(app)

class OpenAPI(Resource):
	def get(self):
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
        

api.add_resource(OpenAPI, "/")

if __name__ == "__main__":
	app.run(debug=True)


