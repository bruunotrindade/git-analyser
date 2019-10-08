import threading
import os

def run(path):
    os.system("java -jar TM.jar " + path)

repos = ["/home/brunotrindade/Reps/angular/", "/home/brunotrindade/Reps/angular.js/", "/home/brunotrindade/Reps/atom/", "/home/brunotrindade/Reps/awesome-python/", "/home/brunotrindade/Reps/bitcoin/", "/home/brunotrindade/Reps/bootstrap/", "/home/brunotrindade/Reps/Chart.js/", "/home/brunotrindade/Reps/d3/", "/home/brunotrindade/Reps/django/", "/home/brunotrindade/Reps/elasticsearch/", "/home/brunotrindade/Reps/electron/", "/home/brunotrindade/Reps/express/", "/home/brunotrindade/Reps/flask/", "/home/brunotrindade/Reps/flutter/", "/home/brunotrindade/Reps/Font-Awesome/", "/home/brunotrindade/Reps/go/", "/home/brunotrindade/Reps/jquery/", "/home/brunotrindade/Reps/json-server/", "/home/brunotrindade/Reps/lantern/", "/home/brunotrindade/Reps/laravel/", "/home/brunotrindade/Reps/linux/", "/home/brunotrindade/Reps/lodash/", "/home/brunotrindade/Reps/meteor/", "/home/brunotrindade/Reps/moby/", "/home/brunotrindade/Reps/moment/", "/home/brunotrindade/Reps/node/", "/home/brunotrindade/Reps/oh-my-zsh/", "/home/brunotrindade/Reps/rails/", "/home/brunotrindade/Reps/react/", "/home/brunotrindade/Reps/react-native/", "/home/brunotrindade/Reps/redux/", "/home/brunotrindade/Reps/reveal.js/", "/home/brunotrindade/Reps/RxJava/", "/home/brunotrindade/Reps/socket.io/", "/home/brunotrindade/Reps/swift/", "/home/brunotrindade/Reps/tensorflow/", "/home/brunotrindade/Reps/three.js/", "/home/brunotrindade/Reps/TypeScript/", "/home/brunotrindade/Reps/vscode/", "/home/brunotrindade/Reps/vue/"]

for rep in repos:
    threading.Thread(target=run, args=(rep,)).start()

print("Fim")
