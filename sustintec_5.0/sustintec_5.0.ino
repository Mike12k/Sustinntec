#include <SoftwareSerial.h>
#include <LiquidCrystal_I2C.h>
#include <Keypad.h>
#include <Wire.h>
#include <OneWire.h>                
#include <DallasTemperature.h>
#include "RTClib.h"
OneWire ourWire(8);                //Se establece el pin 2  como bus OneWire
 
RTC_DS1307 RTC;
//#include <DallasTemperature.h>
LiquidCrystal_I2C lcd(0x27,16,2);
SoftwareSerial BTSerial(14, 15);
DallasTemperature sensors(&ourWire); //Se declara una variable u objeto para nuestro sensor

#define ONE_WIRE_BUS 8 ////////////////////////////////////////////////////////////////////////
                      //Definir pin nuevo termometro 
const byte rowsCount = 4; 
const byte columsCount = 4;
char pulsacion,key1 = ' ';


   
#define ACTION 9 // define pin 9 as for ACTION
#define SENSOR 12 // define pint 2 for sensor
#define ACTION0 36 //Fria

int pinOut1 = 11;  //fria
int pinOut2 = 10; //caliente
int pinOut3 =6;   //bomba


//OneWire oneWire(ONE_WIRE_BUS);
//DallasTemperature sensors(&oneWire);

 
char keys[rowsCount][columsCount] = {
   { '1','2','3', 'A' },
   { '4','5','6', 'B' },
   { '7','8','9', 'C' },
   { '*','0','#', 'D' }
};
 
const byte rowPins[rowsCount] =      {36,34,32,30};//36,34,32,30
const byte columnPins[columsCount] = {28,26,24,22 };//28,26,24,22
 Keypad Teclado1 = Keypad(makeKeymap(keys), rowPins, columnPins, rowsCount, columsCount); //Creamos lo que va a ser nuestro Keypad, que llamaremos "Teclado1"

int i;
int pulsacion1;
int seg1,min1,hor1,ano1,mes1,dia1; //Hora que trae el reloj DS1307RTC
int min2,hor2; //Hora que defino yo como alarma. Solo me interesan la hora y los minutos

boolean caudalimetro = false;
boolean MensajeTepAgua = false;
boolean electrovalvula = false;
boolean RelevadorBomba = false;
boolean termometro = false;

volatile int NumPulsos;
int PinSensor = 2;
float factor_conversion=7.11;
float volumen=0;
long dt=0;
long t0=0;
int alarma=A0;
   
  const int sensor = 2; // Pin digital para el sensor de flujo YF-S201
  int litros_Hora; // Variable que almacena el caudal (L/hora)
  volatile int pulsos = 0; // Variable que almacena el número de pulsos
  unsigned long tiempoAnterior = 0; // Variable para calcular el tiempo transcurrido
  unsigned long pulsos_Acumulados = 0; // Variable que almacena el número de pulsos acumulados
  float litros; // // Variable que almacena el número de litros acumulados
 
  // Rutina de servicio de la interrupción (ISR)
  void flujo()
  {
    pulsos++; // Incrementa en una unidad el número de pulsos
  }
   

Keypad keypad = Keypad(makeKeymap(keys), rowPins, columnPins, rowsCount, columsCount);

void setup() {
   digitalWrite(pinOut1, HIGH);
    digitalWrite(pinOut2, HIGH);
    digitalWrite(pinOut3, LOW);
    
 Wire.begin(); // Inicia el puerto I2C
RTC.begin(); // Inicia la comunicación con el RTC
  pinMode(sensor, INPUT_PULLUP); // Pin digital como entrada con conexión PULL-UP interna  
  lcd.backlight();
  //Iniciamos la pantalla
  lcd.init();
sensors.begin();
  lcd.begin(16,2);
  lcd.clear();//Limpiamos la LCD
  Serial.begin(9600);// setup Serial Monitor to display information
  BTSerial.begin(38400);
  Serial.println("ATcommand");
  pinMode(SENSOR, INPUT_PULLUP);// define pin as Input  sensor
  //pinMode(ACTION, OUTPUT);// define pin as OUTPUT for ACTION
   pinMode(11, OUTPUT);
   pinMode(10, OUTPUT);
   pinMode(6, OUTPUT);
   Serial.begin(9600);
  //Situamos el cursor
  lcd.setCursor(3, 0);
  lcd.print(F("BIENVENIDO"));//Escribimos en la primera linea
  lcd.setCursor(3,1);//Saltamos a la segunda linea
  lcd.print(F("SUSTINNTEC"));//Escribimos en la segunda linea
delay(4000);
lcd.clear();//Limpiamos la LCD
lcd.setCursor(3, 0);
  lcd.print(F("Selecciona"));//Escribimos en la primera linea
  lcd.setCursor(0,1);//Saltamos a la segunda linea
  lcd.print("2-MANUAL  3-AUTO");//Escribimos en la segunda linea
delay(4000);
//Situamos el cursor
    interrupts(); // Habilito las interrupciones
    // Interrupción INT0, llama a la ISR llamada "flujo" en cada flanco de subida en el pin digital 2
    attachInterrupt(digitalPinToInterrupt(sensor), flujo, RISING);  
    tiempoAnterior = millis(); // Guardo el tiempo que tarda el ejecutarse el setup
 
 
     }
void loop() {

if (BTSerial.available())
    Serial.write(BTSerial.read());
  if (Serial.available())
    BTSerial.write(Serial.read());
  
  {  int L =digitalRead(SENSOR);// read the sensor 
  
      if(L == 1){
    Serial.println("  === All clear");
     digitalWrite(ACTION,HIGH);
    digitalWrite(pinOut2, HIGH);
    digitalWrite(pinOut3, LOW);
  }
  else {
    digitalWrite(pinOut2, LOW);
   // digitalWrite(pinOut3, LOW);
     Serial.println(" Obstacle detected");
     digitalWrite(ACTION,LOW);// turn the relay OFF
      //delay(40000);
      }
}


// Cada segundo calculamos e imprimimos el caudal y el número de litros consumidos
   { if(millis() - tiempoAnterior > 1000)
    {
      // Realizo los cálculos
      tiempoAnterior = millis(); // Actualizo el nuevo tiempo
      pulsos_Acumulados += pulsos; // Número de pulsos acumulados
      litros_Hora = (pulsos * 60 / 7.5); // Q = frecuencia * 60/ 7.5 (L/Hora)
      litros = pulsos_Acumulados*1.0/450; // Cada 450 pulsos son un litro
      pulsos = 0; // Pongo nuevamente el número de pulsos a cero
     
      // Llamada a la función que muestra los resultados en el LCD 2004
        LCD_2004();      
        }
        
        }

sensors.requestTemperatures();   //Se envía el comando para leer la temperatura
float temp= sensors.getTempCByIndex(0); //Se obtiene la temperatura en ºC
lcd.setCursor(0, 0);
lcd.print("-> ");
lcd.print(temp);
lcd.print(" Grados");
      key1 = keypad.getKey();  
     if (key1){
     if (key1 == '2'){
     //}else{   
   
  lcd.clear();//Limpiamos la LCD
  lcd.setCursor(5, 0);
  lcd.print(F("MANUAL"));//Escribimos en la primera linea
  delay(4000);
  lcd.clear();//Limpiamos la LCD
  lcd.setCursor(2, 0);
  lcd.print(F("DUCHA LISTA!!"));//Escribimos en la primera linea
  delay(4000);
 
  }
   if (key1 == 'A'){
  lcd.clear();//Limpiamos la LCD  
  lcd.setCursor(4, 0);
  lcd.print(F("FINALIZADO"));//Escribimos en la primera linea
 delay(4000);
 void(* resetFunc) (void) = 0; // esta es la funcion
 resetFunc();  // la llamo con estovoid(* resetFunc) (void) = 0; // esta es la funcion

}if (key1 == '3'){
lcd.clear();//Limpiamos la LCD
  lcd.setCursor(3, 0);
  lcd.print(F("AUTOMATICO"));//Escribimos en la primera linea
  delay(3000);
  lcd.clear();
 if(key1 == '1'){ //Mientras el pulsador NO este pulsado...
  DateTime now = RTC.now();
  // En la anterior linea ya suponemos que el reloj ha sido SETeado en otro momento
  // y que la hora ya esta en la memoria. Esa hora esta en now().
/*
  //De aqui en adelante nos vamos a dedicar a escribir la hora en la pantalla.
  lcd.setCursor(0,0);
  lcd.print("FECHA:");
  lcd.setCursor(6,0);
    dia1=now.day();
    if(dia1<10){lcd.print("0");
                lcd.setCursor(7,0);}
  lcd.print(dia1);
  lcd.setCursor(8,0);
  lcd.print("/") ;
  lcd.setCursor(9,0);
    mes1=now.month();
    if(mes1<10){lcd.print("0");
                lcd.setCursor(10,0);}
  lcd.print(mes1);
  lcd.setCursor(11,0);
  lcd.print("/") ;
  lcd.setCursor(12,0);
    ano1=now.year();
    if(ano1<10){lcd.print("0");
                lcd.setCursor(13,0);}
  lcd.print(ano1); 
  lcd.setCursor(0,1) ;
    hor1=now.hour();
    if(hor1<10){lcd.print("0");
                lcd.setCursor(1,1);}
  lcd.print(hor1); 
  lcd.setCursor(2,1); 
  lcd.print(":") ;
  lcd.setCursor(3,1);
    min1=now.minute();
    if(min1<10){lcd.print("0");
                lcd.setCursor(4,1);}
  lcd.print(min1);
  lcd.setCursor(5,1);
  lcd.print(":") ;
  lcd.setCursor(6,1);
    seg1=now.second();
    if(seg1<10){lcd.print("0");
                lcd.setCursor(7,1);}
  lcd.print(seg1);
  delay(4000);*/
}
  else{ //Cuando se pulse el pulsador...
//Pediremos al usuario que escriba la hora en la que quiere hacer sonar la alarma.
 lcd.clear();
 lcd.setCursor(1,0);
 lcd.print(F("Configuracion"));
 delay(3000);
 lcd.clear();
 lcd.setCursor(0,0);
 lcd.print(F("Escribe la hora:"));
 delay(5000);
 //La hora, constara de cuatro numeros ( 2 para los minutos y 2 para los segundos )
 // Por ello, daremos 5 vueltas dentro de un for, esperando a que el usuario pulse una tecla.
 // En la tercera vuelta (i=2), no debemos pedir al usuario que nos de un valor, sino que escribiremos " : " que separan las horas de los minutos.
 for(i=0;i<5;i++){
  char pulsacion;
   lcd.setCursor(i,1);
   if(i==2){lcd.print(":");}
   else{
    pulsacion=Teclado1.waitForKey(); // Esperamos a que pulse un boton.
   lcd.print(pulsacion);            //Escribimos el el valor de pulsacion en el LCD.
    
    switch(pulsacion){              // Pulsacion es de tipo char, por eso hemos definido al principio de programa "pulsacion1", de tipo int,
                                    // Que tendra un valor numerico que sera el de la hora en la que el usuario ha definido la alarma.
                                    //Este valor, sera dependiendo el boton pulsado en el teclado matricial, que hemos guardado en pulsacion.
      case '1': pulsacion1=1;
      break;
      case '2': pulsacion1=2;
      break;
      case '3': pulsacion1=3;
      break;
      case '4': pulsacion1=4;
      break;
      case '5': pulsacion1=5;
      break;
      case '6': pulsacion1=6;
      break;
      case '7': pulsacion1=7;
      break;
      case '8': pulsacion1=8;
      break;
      case '9': pulsacion1=9;
      break;
      case '0': pulsacion1=0;
    }
    switch(i){                            
      case 0: hor2=pulsacion1*10;       //Sabemos que el primer valor de los minutos sera un valor en decimas
      break;
      case 1: hor2=hor2+pulsacion1;     // El segundo valor de los minutos que sera una unidades
      break;
      case 3: min2=pulsacion1*10;       //Primer valor de los segundos que sera un valor en decimas
      break;
      case 4: min2=min2+pulsacion1;     //Segundo valor de los segundos que sera en unidades
      break;} 
  
   }
   delay(2000);
 }
lcd.clear();
lcd.setCursor(0,0);
lcd.print(F("Temperatura:"));
  lcd.setCursor(0, 1);
  lcd.print(F("4.-F 5.-R 6.-C"));//Escribimos en la primera linea
  delay(4000);
  for(i=0;i<1;i++){
  char pulsacion;
   lcd.setCursor(i,1);
   if(i==1){}
   else{
    pulsacion=Teclado1.waitForKey(); // Esperamos a que pulse un boton.
   //lcd.print(pulsacion);            //Escribimos el el valor de pulsacion en el LCD.
    
    switch(pulsacion){              // Pulsacion es de tipo char, por eso hemos definido al principio de programa "pulsacion1", de tipo int,
                                    // Que tendra un valor numerico que sera el de la hora en la que el usuario ha definido la alarma.
                                    //Este valor, sera dependiendo el boton pulsado en el teclado matricial, que hemos guardado en pulsacion.
    
      case '4': 
      lcd.clear();//Limpiamos la LCD
      lcd.setCursor(6, 0);
      lcd.print(F("FRIA"));//Escribimos en la primera linea
      delay(2000);
         lcd.clear();
 lcd.setCursor(5,0);
lcd.print(F("Ducha"));     
lcd.setCursor(3,1);
lcd.print(F("Configurada"));
delay(3000);
  int L =digitalRead(SENSOR);// read the sensor 
  while(min1!=min2||hor1!=hor2){    //Mientras la hora de la alarma escrita por el usuario, y la hora real sean diferentes,
                                   //escribiremos la hora real y la de la alarma en la pantalla.
  DateTime now = RTC.now();
  lcd.clear();
  lcd.setCursor(0,0);
  lcd.print(F("Hora: "));
  lcd.setCursor(0,1);
  lcd.print(F("Ducha: "));
delay(2000);
   lcd.setCursor(6,0) ; //dibujamos la hora real
    hor1=now.hour();
    if(dia1<10){lcd.print("0");
                lcd.setCursor(7,0);}
  lcd.print(hor1); 
  lcd.setCursor(8,0); 
  lcd.print(":") ;
  lcd.setCursor(9,0);
    min1=now.minute();
    if(min1<10){lcd.print("0");
                lcd.setCursor(10,0);}
  lcd.print(min1);
  lcd.setCursor(11,0);
  lcd.print(":") ;
  lcd.setCursor(12,0);
    seg1=now.second();
    if(seg1<10){lcd.print("0");
                lcd.setCursor(13,0);}
  lcd.print(seg1);

   lcd.setCursor(6,1) ; //dibujamos la hora de la alarma
   if(hor2<10){lcd.print("0");
                lcd.setCursor(7,1);}
  lcd.print(hor2);
   
  lcd.setCursor(8,1); 
  lcd.print(":") ;
  
  lcd.setCursor(9,1);
  if(min2<10){lcd.print("0");
                lcd.setCursor(10,1);}
  lcd.print(min2);
  
  delay(6000);
 }

//En cuanto sea la hora, saltara la alarma, se encenderan los LEDs y sonara el zumbador.

 lcd.clear();
 lcd.setCursor(1,0);
 lcd.print(F("-Ducha Lista-"));
delay(4000);
      if(L == HIGH){
    Serial.println("  === All clear");
 digitalWrite(pinOut2, LOW);
 }
  else {
    digitalWrite(pinOut2, HIGH);
  }
      
      break;
      case '5': lcd.clear();//Limpiamos la LCD
  lcd.setCursor(4, 0);
  lcd.print(F("REGULAR"));//Escribimos en la primera linea
  delay(2000);
   lcd.clear();
 lcd.setCursor(5,0);
lcd.print(F("Ducha"));     
lcd.setCursor(3,1);
lcd.print(F("Configurada"));
delay(3000);

 digitalWrite(pinOut3, HIGH);
 delay(40000);
 digitalWrite(pinOut3, LOW);
   L =digitalRead(SENSOR);// read the sensor 
  while(min1!=min2||hor1!=hor2){    //Mientras la hora de la alarma escrita por el usuario, y la hora real sean diferentes,
                                   //escribiremos la hora real y la de la alarma en la pantalla.
  DateTime now = RTC.now();
  lcd.clear();
  lcd.setCursor(0,0);
  lcd.print(F("Hora: "));
  lcd.setCursor(0,1);
  lcd.print(F("Ducha: "));
delay(2000);
   lcd.setCursor(6,0) ; //dibujamos la hora real
    hor1=now.hour();
    if(dia1<10){lcd.print("0");
                lcd.setCursor(7,0);}
  lcd.print(hor1); 
  lcd.setCursor(8,0); 
  lcd.print(":") ;
  lcd.setCursor(9,0);
    min1=now.minute();
    if(min1<10){lcd.print("0");
                lcd.setCursor(10,0);}
  lcd.print(min1);
  lcd.setCursor(11,0);
  lcd.print(":") ;
  lcd.setCursor(12,0);
    seg1=now.second();
    if(seg1<10){lcd.print("0");
                lcd.setCursor(13,0);}
  lcd.print(seg1);

   lcd.setCursor(6,1) ; //dibujamos la hora de la alarma
   if(hor2<10){lcd.print("0");
                lcd.setCursor(7,1);}
  lcd.print(hor2);
   
  lcd.setCursor(8,1); 
  lcd.print(":") ;
  
  lcd.setCursor(9,1);
  if(min2<10){lcd.print("0");
                lcd.setCursor(10,1);}
  lcd.print(min2);
  
  delay(6000);
 }

//En cuanto sea la hora, saltara la alarma, se encenderan los LEDs y sonara el zumbador.

 lcd.clear();
 lcd.setCursor(1,0);
 lcd.print(F("-Ducha Lista-"));
delay(4000);
      if(L == HIGH){
    Serial.println("  === All clear");
 digitalWrite(pinOut1, LOW);
 digitalWrite(pinOut2, LOW);
 }
  else {
    digitalWrite(pinOut1, HIGH);
    digitalWrite(pinOut2, HIGH);
  }
      break;
      case '6': 
 lcd.clear();//Limpiamos la LCD
 lcd.setCursor(4, 0);
 lcd.print(F("CALIENTE"));//Escribimos en la primera linea
 //delay(2000);
    lcd.clear();
 lcd.setCursor(5,0);
lcd.print(F("Ducha"));     
lcd.setCursor(3,1);
lcd.print(F("Configurada"));
delay(3000);

 digitalWrite(pinOut3, HIGH);
 delay(40000);
 digitalWrite(pinOut3, LOW);
   L =digitalRead(SENSOR);// read the sensor 
  while(min1!=min2||hor1!=hor2){    //Mientras la hora de la alarma escrita por el usuario, y la hora real sean diferentes,
                                   //escribiremos la hora real y la de la alarma en la pantalla.
  DateTime now = RTC.now();
  lcd.clear();
  lcd.setCursor(0,0);
  lcd.print(F("Hora: "));
  lcd.setCursor(0,1);
  lcd.print(F("Ducha: "));
delay(2000);
   lcd.setCursor(6,0) ; //dibujamos la hora real
    hor1=now.hour();
    if(dia1<10){lcd.print("0");
                lcd.setCursor(7,0);}
  lcd.print(hor1); 
  lcd.setCursor(8,0); 
  lcd.print(":") ;
  lcd.setCursor(9,0);
    min1=now.minute();
    if(min1<10){lcd.print("0");
                lcd.setCursor(10,0);}
  lcd.print(min1);
  lcd.setCursor(11,0);
  lcd.print(":") ;
  lcd.setCursor(12,0);
    seg1=now.second();
    if(seg1<10){lcd.print("0");
                lcd.setCursor(13,0);}
  lcd.print(seg1);

   lcd.setCursor(6,1) ; //dibujamos la hora de la alarma
   if(hor2<10){lcd.print("0");
                lcd.setCursor(7,1);}
  lcd.print(hor2);
   
  lcd.setCursor(8,1); 
  lcd.print(":") ;
  
  lcd.setCursor(9,1);
  if(min2<10){lcd.print("0");
                lcd.setCursor(10,1);}
  lcd.print(min2);
  
  delay(6000);
 }

//En cuanto sea la hora, saltara la alarma, se encenderan los LEDs y sonara el zumbador.

 lcd.clear();
 lcd.setCursor(1,0);
 lcd.print(F("-Ducha Lista-"));
delay(4000);
      if(L == HIGH){
    Serial.println("  === All clear");
 digitalWrite(pinOut2, LOW);
 }
  else {
    digitalWrite(pinOut2, HIGH);
  }
      break;  
  }}}}}
   delay(2000); 

    /*  
   do{ 
             int L =digitalRead(SENSOR);// read the sensor 
  
      if(L == HIGH){
    Serial.println("  === All clear");
      //digitalWrite(ACTION0,LOW);
    
    digitalWrite(pinOut3, HIGH);
    delay(40000);
    
    digitalWrite(pinOut1, LOW);
    digitalWrite(pinOut2, LOW);
  }
  else {
    digitalWrite(pinOut1, HIGH);
     Serial.println(" Obstacle detected");
     digitalWrite(ACTION0,HIGH);// turn the relay OFF
      }}while(key1 == '4');
        
                      termometro = !termometro; 
                     }
                     RelevadorBomba = !RelevadorBomba;
  */

   
if (caudalimetro){ver_caudalimetro();}
//if (MensajeTepAgua){ver_EscogerTepmAgua();}
//if (electrovalvula){ver_electrovalvula();}
if (RelevadorBomba){ver_relevadorBomba();}
//if (sensorPresencia){ver_sensorPresenciaElecvalve();}
if (termometro){ver_termometro();}


  /*//Movimiento del texto en la pantalla
  digitalWrite(pinOut1, HIGH);
    digitalWrite(pinOut2, HIGH);
    digitalWrite(pinOut3, HIGH);
      for ( int i = 0; i < 1; i++ ) {
    lcd.scrollDisplayLeft();
    delay (500);
  }*/

     

}
}
void ver_caudalimetro() {}

void ver_sensorPresenciaElecvalve(){ }

void ver_relevadorBomba(){}

void ver_termometro(){
 lcd.clear();
lcd.setCursor(2, 0);
lcd.print("T= ");
sensors.requestTemperatures();   //Se envía el comando para leer la temperatura
float temp= sensors.getTempCByIndex(0); //Se obtiene la temperatura en ºC
lcd.print(temp);
//lcd.println(" C");
  
 }
 
 void LCD_2004()
  {
    // Imprimo la cabecera del sistema
    /*
    lcd.clear();
    lcd.setCursor(0,0);
    lcd.print(">CAUDALIMETRO YF-S201");        
    lcd.setCursor(0,1);    
    lcd.print("********************");  
    // Presento los valores a través del LCD 2004
    */
    lcd.clear();
   /* lcd.setCursor(0,0);
    // Imprimo el caudal en L/hora
    lcd.print("-> ");
    lcd.print(litros_Hora, DEC);
    lcd.print(" L/Hora");*/
    lcd.setCursor(0,1);
    // Imprimo el número de litros acumulados
    lcd.print("-> ");
    lcd.print(litros);    
    lcd.print(" Litros");  
  }
