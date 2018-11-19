#Playing with Gatling

### Motive

A mini project serving as a playground for exploring Gatling as a tool for non-functional testing



### How to run 

Firstly, we need to start up the web server. 

1a) Either run the Flask (Python) application by running the following command on the terminal: 

```
cd flask_app
pip install -r requirements.txt
FLASK_APP=main.py
flask run -p 8080
```

1b) Or run the SimpleWebServer app using Intellij


2) Then to run the Gatling simulation, use the following command in the terminal: 

```
sbt gatling:test
``` 
