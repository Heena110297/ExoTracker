
from pyfcm import FCMNotification
import pyrebase

push_service = FCMNotification(api_key="AAAABh1UAT8:APA91bGfL2ce9SyE8rFJWo1FhjWwX-dYAY1W3w47mQc7Pcu2dPv3T00tRScW_CMbYgCaFkbr7s9wn22PhnqGD3ZtO8nra6ZhQ5MIl88W3p4DwvxD0ee9Tmlc1IIg3KfJ2tycQATfAOWP")


config = {
"apiKey": "AIzaSyAzSw93mnCOJYrcWdzQEd0pwTWWwYLpoLs",
"authDomain": "exotracker-5ab73.firebaseapp.com",
"databaseURL": "https://exotracker-5ab73.firebaseio.com",
"projectId": "exotracker-5ab73",
"storageBucket": "exotracker-5ab73.appspot.com",
"messagingSenderId": "26261848383"
};

firebase = pyrebase.initialize_app(config)
db = firebase.database()

message_title = "Order Status"
#message_body = db.child("address").get().val()

message_body = "New Order Received:"#+ str(message_body)
registration_id = db.child("message").get().val()
#registration_id2 = db.child("message1").get().val()
def stream_handler(post):
	print(post)
	if (post['data'] is 1):
		#result = push_service.notify_single_device(registration_id=registration_id2, message_title=message_title, message_body=message_body, sound="default")
		result = push_service.notify_single_device(registration_id=registration_id, message_title=message_title, message_body=message_body, sound="default")

		print (result)
my_stream = db.child("Status").stream(stream_handler, None)

