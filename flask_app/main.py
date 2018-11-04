from flask import Flask
from flask import request

app = Flask(__name__)

@app.route('/')
def hello_world():
    return 'Hello, World!'

@app.route('/create-order', methods=['POST'])
def create_order():
    print("information: ", request.get_json())
    return "completed order!\n"