while true
do
echo "Start to talk:"
#rec --encoding signed-integer --bits 16 --channels 1 --rate 16000 test.wav
rec --channels 1 --rate 16000 recording.wav rate 16k silence 1 0.1 1.5% 1 3.0 1%

curl -X POST \
--data-binary @'recording.wav' \
--header 'Content-Type: audio/l16; rate=16000;' \
'https://www.google.com/speech-api/v2/recognize?output=json&lang=en&key=KEY' \
| sed '2!d'  | jq '.result[0].alternative[0].transcript' > stt.txt


echo "You said:"
cat stt.txt
echo "print stt done"

python client.py -f "stt.txt"
done

