import socket
import parser
import argparse

parser = argparse.ArgumentParser()
parser.add_argument('-f', '--file', help='filename', required=True)
args = parser.parse_args()


TCP_IP = 'Enter IP'
TCP_PORT = 5006
BUFFER_SIZE = 1024

fileInput = open(args.file, "r")
MESSAGE = fileInput.read()

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((TCP_IP, TCP_PORT))
s.send(MESSAGE)
data = s.recv(BUFFER_SIZE)
s.close()

print "received data:", data
