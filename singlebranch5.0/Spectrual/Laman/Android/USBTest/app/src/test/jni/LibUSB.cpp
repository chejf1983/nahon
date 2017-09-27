//
// Created by Administrator on 2017/8/29.
//
#include "stdio.h"
#include <jni.h>
#include "libusb.h"
#include "NahonLibUSB.h"

/*
 * Class:     com_drv_nahon_usb_LibUSB
 * Method:    USBScanDevImpl
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_drv_nahon_usb_LibUSB_USBScanDevImpl
  (JNIEnv *, jclass, jint){
    return 1;
  }

//libusb_device_handle *handle = NULL;
/*
 * Class:     com_drv_nahon_usb_LibUSB
 * Method:    OpenUSBImpl
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_drv_nahon_usb_LibUSB_OpenUSBImpl
  (JNIEnv * env, jclass, jint fd){
   libusb_device **devs;
    ssize_t cnt;
    int r, i;

       r = libusb_init(NULL);
       if (r < 0)
           return r;

       cnt = libusb_get_device_list(NULL, &devs);
       if (cnt < 0)
           return (int)cnt;

       libusb_free_device_list(devs, 1);

       libusb_exit(NULL);

       return cnt;
}

/*
 * Class:     com_drv_nahon_usb_LibUSB
 * Method:    USBBulkWriteDataImpl
 * Signature: (I[BII)I
 */
JNIEXPORT jint JNICALL Java_com_drv_nahon_usb_LibUSB_USBBulkWriteDataImpl
  (JNIEnv *, jclass, jint, jbyteArray, jint, jint){
    return 3;}

/*
 * Class:     com_drv_nahon_usb_LibUSB
 * Method:    USBBulkReadDataImpl
 * Signature: (I[BII)I
 */
JNIEXPORT jint JNICALL Java_com_drv_nahon_usb_LibUSB_USBBulkReadDataImpl
  (JNIEnv *, jclass, jint, jbyteArray, jint, jint){
    return 4;
}

/*
 * Class:     com_drv_nahon_usb_LibUSB
 * Method:    USBCtrlDataImpl
 * Signature: (IIIII[BII)I
 */
JNIEXPORT jint JNICALL Java_com_drv_nahon_usb_LibUSB_USBCtrlDataImpl
  (JNIEnv *, jclass, jint, jint, jint, jint, jint, jbyteArray, jint, jint){
    return 5;}

/*
 * Class:     com_drv_nahon_usb_LibUSB
 * Method:    CloseUSBImpl
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_drv_nahon_usb_LibUSB_CloseUSBImpl
  (JNIEnv *, jclass, jint){
    return 6;}