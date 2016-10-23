import json
import requests
import urllib
import subprocess
import argparse
import socket
import httplib
import os

parser = argparse.ArgumentParser()
parser.add_argument('-o', '--origL', help='Origin Language', required=True)
args = parser.parse_args()

TCP_IP = 'IP'
TCP_PORT = 5006
BUFFER_SIZE = 20                                            


s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((TCP_IP, TCP_PORT))
s.listen(1)

origin_language = args.origL
destination_language = "en-US, ziraRUS"

while 1:
   hold = ""
   outFile = open('transferText.txt', 'w')
   r2conn, addr = s.accept()
   print 'Connection address:', addr
   while 1:
      data = r2conn.recv(BUFFER_SIZE)
      if not data: break
      print "received data:", data
      outFile.write(data)
      hold += data
      r2conn.send("BERHASIL")  
   #r2conn.close()
   outFile.close()

#Get destination language
   TCP_IP_PHONE = 'IP OWN'
   TCP_PORT_PHONE = 5007
   BUFFER_SIZE = 1024

   sp = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
   print "Connecting to Phone..."

   error = 0

   try:
      sp.connect((TCP_IP_PHONE, TCP_PORT_PHONE))
   except socket.error, msg:
      print "Couldnt connect with the socket-server"
      error = 1

   if not error:
      print "Connected"
      sp.send("Test")
      print "Data Sent"
      destination_language = sp.recv(BUFFER_SIZE)
      sp.close()
   else:
      print("Not Connected")

#Translate language
   print "From {} to {}".format(origin_language, destination_language)

   text = hold

   args = {
	'client_id' : 'ENTER',
	'client_secret' : 'ENTER',
	'scope' : 'http://api.microsofttranslator.com',
	'grant_type' : 'client_credentials'
   }

   oauth_url = 'https://datamarket.accesscontrol.windows.net/v2/OAuth2-13'
   oauth_junk = json.loads(requests.post(oauth_url, data=urllib.urlencode(args)).content)

   translation_args = {
	'text' : text,
	'to' : destination_language[0:2],
	'from' : origin_language,
   } 

   headers = {'Authorization' : 'Bearer ' + oauth_junk['access_token']}
   translation_url = 'http://api.microsofttranslator.com/V2/Ajax.svc/Translate?'
   translation_result = requests.get(translation_url+urllib.urlencode(translation_args),headers=headers)
   translation=translation_result.text[2:-1]

   print "Translated text:"
   print translation + '\n'

   #oFile = open('translatedText.txt', 'w')
   #oFile.write(translation + '\n')
   #oFile.close()

   #TTS convertion
   language = destination_language[0:2]

   print "Convert text to speech:"
   print translation

   apiKey = "API KEY"

   params = "Hello my name is Gerry"
   headers = {"Ocp-Apim-Subscription-Key": apiKey}
 
   AccessTokenHost = "api.cognitive.microsoft.com"
   path = "/sts/v1.0/issueToken"

   print ("Connect to server to get the Access Token")
   conn = httplib.HTTPSConnection(AccessTokenHost)
   conn.request("POST", path, params, headers)
   response = conn.getresponse()
   print(response.status, response.reason)

   data = response.read()
   conn.close()

   accesstoken = data.decode("UTF-8")

   body = "<speak version='1.0' xml:lang='" + language + "'><voice xml:lang='" + language + "' xml:gender='Female' name='Microsoft Server Speech Text to Speech Voice (" + destination_language + ")'>" + translation.encode('utf-8') + "</voice></speak>"


   headers = {"Content-type": "application/ssml+xml",
                        "X-Microsoft-OutputFormat": "riff-16khz-16bit-mono-pcm",
                        "Authorization": "Bearer " + accesstoken,
                        "X-Search-AppId": "OWN,
                        "X-Search-ClientID": "1OWN",
                        "User-Agent": "TTSForPython"}

   #Connect to server to synthesize the wave
   print ("\nConnect to server to synthesize the wave")
   conn = httplib.HTTPSConnection("speech.platform.bing.com")
   conn.request("POST", "/synthesize", body, headers)
   response = conn.getresponse()
   print(response.status, response.reason)

   data = response.read()
   conn.close()

   soundFile = open('speak.wav', 'w')
   soundFile.write(data)
   soundFile.close()

   print("The synthesized wave length: %d" %(len(data)))

   os.system('aplay speak.wav')


