/*
1º Parte del proyecto del servidor web
Adaptado de  Ruiz Santos (RandomNerdTutorials.com)
*/
// Importamos las librerías
#include <ESP8266WiFi.h>
#include <ESPAsyncTCP.h>
#include <ESPAsyncWebServer.h>
#include <FS.h>
// Sustituye los datos de tu red WIFI ( el nombre y la contraseña )
const char* ssid = "INFINITUMvjzu";
const char* password = "1456987jesus";

// Puesta de LED GPIO
const int ledPin = 2;
// Para guardar el estado del LED
String ledState;

// Creamos el servidor AsyncWebServer en el puerto 80
AsyncWebServer server(80);

// Remplazamos el marcador con el estado del  LED
String processor(const String& var){
Serial.println(var);
if(var == "STATE"){
if(digitalRead(ledPin)){
ledState = "ON";
}
else{
ledState = "OFF";
}
// Imprimimos el estado del led ( por el COM activo )
Serial.print(ledState);
return ledState;
}
}
void setup(){
// Establecemos la velocidad de conexión por el puerto serie
Serial.begin(9200);
// Ponemos a  ledPin  como salida
pinMode(ledPin, OUTPUT);

// Iniciamos  SPIFFS
if(!SPIFFS.begin()){
Serial.println("ha ocurrido un error al montar SPIFFS");
return;
}

// conectamos al Wi-Fi
WiFi.begin(ssid, password);
// Mientras no se conecte, mantenemos un bucle con reintentos sucesivos
while (WiFi.status() != WL_CONNECTED) {
delay(1000);
// Esperamos un segundo
Serial.println("Connecting to WiFi..");
}

Serial.println();
Serial.println(WiFi.SSID());
Serial.print("IP address:\t");
// Imprimimos la ip que le ha dado nuestro router
Serial.println(WiFi.localIP());

// Ruta para cargar el archivo index.html
server.on("/", HTTP_GET, [](AsyncWebServerRequest *request){
request->send(SPIFFS, "/index.html", String(), false, processor);
});

// Ruta para cargar el archivo style.css
server.on("/style.css", HTTP_GET, [](AsyncWebServerRequest *request){
request->send(SPIFFS, "/style.css", "text/css");
});

// Ruta para poner el GPIO alto
server.on("/on", HTTP_GET, [](AsyncWebServerRequest *request){
digitalWrite(ledPin, HIGH);
request->send(SPIFFS, "/index.html", String(), false, processor);
});

// Ruta para poner el GPIO bajo
server.on("/off", HTTP_GET, [](AsyncWebServerRequest *request){
digitalWrite(ledPin, LOW);
request->send(SPIFFS, "/index.html", String(), false, processor);
});

// Start server
server.begin();
}

void loop(){

}
