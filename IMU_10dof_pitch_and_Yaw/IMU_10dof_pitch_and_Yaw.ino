#include "MPU9250.h"
#include "BluetoothSerial.h"

BluetoothSerial SerialBT;
MPU9250 mpu;
int i = 0;
double count = 0;
double countYaw = 0;
unsigned long pMillis = 0;
const long interval = 1000;
String recieveData = ""; 

void setup() {
    Serial.begin(115200);
    SerialBT.begin("WIoTesp32");
    Wire.begin();
//    delay(2000);

    mpu.setup(0x68);  // change to your own address

    delay(5000);

    // calibrate anytime you want to
    Serial.println("Accel Gyro calibration will start.");
    mpu.calibrateAccelGyro();
}

void loop() {
    if(SerialBT.available() > 0){
      recieveData = SerialBT.read();
      if(recieveData.equals("Recalibrate")){
          Serial.println("Accel Gyro calibration will start.");
          delay(1000);
          mpu.calibrateAccelGyro();
      }
    }
    if (mpu.update()) {
//        Serial.println(mpu.getPitch());
        print_roll_pitch_yaw();
    }
}

void print_roll_pitch_yaw() {
  if(i<990){
    count += abs(mpu.getPitch());
    countYaw += abs(mpu.getYaw());
    i++;
  }else{
    unsigned long cMillis = millis();
    if(cMillis - pMillis >= interval){
      pMillis = cMillis;
      Serial.println(millis());
      }
    count= count/990;
    Serial.println("俯仰角:"+count);
    //countYaw為翻滾軸的值
    Serial.println("翻滾角:"+countYaw);
    //countYaw小於多少為合理範圍由你測量
    if(countYaw<50){
      SerialBT.println(count);
      }
    i = 0;
  } 
}

void print_calibration() {
    Serial.println("< calibration parameters >");
    Serial.println("accel bias [g]: ");
    Serial.print(mpu.getAccBiasX() * 1000.f / (float)MPU9250::CALIB_ACCEL_SENSITIVITY);
    Serial.print(", ");
    Serial.print(mpu.getAccBiasY() * 1000.f / (float)MPU9250::CALIB_ACCEL_SENSITIVITY);
    Serial.print(", ");
    Serial.print(mpu.getAccBiasZ() * 1000.f / (float)MPU9250::CALIB_ACCEL_SENSITIVITY);
    Serial.println();
    Serial.println("gyro bias [deg/s]: ");
    Serial.print(mpu.getGyroBiasX() / (float)MPU9250::CALIB_GYRO_SENSITIVITY);
    Serial.print(", ");
    Serial.print(mpu.getGyroBiasY() / (float)MPU9250::CALIB_GYRO_SENSITIVITY);
    Serial.print(", ");
    Serial.print(mpu.getGyroBiasZ() / (float)MPU9250::CALIB_GYRO_SENSITIVITY);
    Serial.println();
    Serial.println("mag bias [mG]: ");
    Serial.print(mpu.getMagBiasX());
    Serial.print(", ");
    Serial.print(mpu.getMagBiasY());
    Serial.print(", ");
    Serial.print(mpu.getMagBiasZ());
    Serial.println();
    Serial.println("mag scale []: ");
    Serial.print(mpu.getMagScaleX());
    Serial.print(", ");
    Serial.print(mpu.getMagScaleY());
    Serial.print(", ");
    Serial.print(mpu.getMagScaleZ());
    Serial.println();
}
