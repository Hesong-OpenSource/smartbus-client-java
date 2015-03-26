/*
 * com_hesong_smartbus_client_net_JniWrapper.c
 *
 *  Created on: 2013-3-10
 *      Author: tanbro
 */

#include "com_hesong_smartbus_client_net_JniWrapper.h"

#include <stdbool.h>
#include <string.h>

#include "smartbus_netcli_interface.h"

static JavaVM *jvm = NULL;
static jobject clazz = NULL;
static jmethodID cb_connection = NULL;
static jmethodID cb_disconnect = NULL;
static jmethodID cb_globalconnect = NULL;
static jmethodID cb_recvdata = NULL;
static jmethodID cb_invokeflowret = NULL;

static void WINAPI connection_cb(void * arg, unsigned char local_clientid,
		int accesspoint_unitid, int ack) {
	JNIEnv* env = NULL;
	(*jvm)->AttachCurrentThread(jvm, (void**) (&env), NULL);
	(*env)->CallStaticVoidMethod(env, clazz, cb_connection, (jint) arg,
			(jbyte) local_clientid, (jint) accesspoint_unitid, (jint) ack);
	(*jvm)->DetachCurrentThread(jvm);
}

static void WINAPI disconnect_cb(void * arg, unsigned char local_clientid) {
	JNIEnv* env = NULL;
	(*jvm)->AttachCurrentThread(jvm, (void**) (&env), NULL);
	(*env)->CallStaticVoidMethod(env, clazz, cb_disconnect, (jint) arg,
			(jbyte) local_clientid);
	(*jvm)->DetachCurrentThread(jvm);
}

static void WINAPI globalconnect_cb(void * arg, char unitid, char clientid,
		char clienttype, char accesspoint_unit, char status, const char * add_info) {
	JNIEnv* env = NULL;
	(*jvm)->AttachCurrentThread(jvm, (void**) (&env), NULL);
	jstring txt_add_info = (*env)->NewStringUTF(env, add_info);
	(*env)->CallStaticVoidMethod(env, clazz, cb_globalconnect, (jint) arg,
			(jbyte) unitid, (jbyte) clientid, (jbyte) clienttype,
			(jbyte) status, txt_add_info);
	(*jvm)->DetachCurrentThread(jvm);
}

static void WINAPI recvdata_cb(void * arg, unsigned char local_clientid,
		SMARTBUS_PACKET_HEAD * head, void * data, int size) {
	JNIEnv* env = NULL;
	(*jvm)->AttachCurrentThread(jvm, (void**) (&env), NULL);
	jstring txt = (*env)->NewStringUTF(env, data);
	(*env)->CallStaticVoidMethod(env, clazz, cb_recvdata, (jint) arg, (jbyte) local_clientid,
			(jbyte) head->cmd, (jbyte) head->cmdtype, (jbyte) head->src_unit_id,
			(jbyte) head->src_unit_client_id,
			(jbyte) head->src_unit_client_type, (jbyte) head->dest_unit_id,
			(jbyte) head->dest_unit_client_id,
			(jbyte) head->dest_unit_client_type, txt);
	(*jvm)->DetachCurrentThread(jvm);
}

static void WINAPI invokeflow_ret_cb(void * arg, unsigned char local_clientid,
		SMARTBUS_PACKET_HEAD * head, const char * projectid, int invoke_id,
		int ret, const char * param) {
	JNIEnv* env = NULL;
	(*jvm)->AttachCurrentThread(jvm, (void**) (&env), NULL);
	jobject packinfo_cls = (*env)->FindClass(env,
			"com/hesong/smartbus/client/PackInfo");
	jmethodID packinfo_init = (*env)->GetMethodID(env, packinfo_cls, "<init>",
			"(BBBBBBBBB)V");
	jobject packinfo = (*env)->NewObject(env, packinfo_cls, packinfo_init,
			(jbyte) head->head_flag, (jbyte) head->cmd, (jbyte) head->cmdtype,
			(jbyte) head->src_unit_id, (jbyte) head->src_unit_client_id,
			(jbyte) head->src_unit_client_type, (jbyte) head->dest_unit_id,
			(jbyte) head->dest_unit_client_id,
			(jbyte) head->dest_unit_client_type);
	jstring jstr_projectid = (*env)->NewStringUTF(env, projectid);
	jstring jstr_param = (*env)->NewStringUTF(env, param);
	(*env)->CallStaticVoidMethod(env, clazz, cb_invokeflowret, (jint) arg,
			(jbyte) local_clientid, packinfo, jstr_projectid, (jint) invoke_id,
			(jint) ret, jstr_param);
	(*jvm)->DetachCurrentThread(jvm);
}

jint JNICALL Java_com_hesong_smartbus_client_net_JniWrapper_Init(JNIEnv *env,
		jclass cls, jbyte unitid) {
	clazz = cls;
	(*env)->GetJavaVM(env, &jvm);
	cb_connection = (*env)->GetStaticMethodID(env, cls, "cb_connection",
			"(IBII)V");
	cb_disconnect = (*env)->GetStaticMethodID(env, cls, "cb_disconnect",
			"(IB)V");
	cb_recvdata = (*env)->GetStaticMethodID(env, cls, "cb_recvdata",
			"(IBBBBBBBBBLjava/lang/String;)V");
	cb_invokeflowret =
			(*env)->GetStaticMethodID(env, cls, "cb_invokeflowret",
					"(IBBBBBBBBBBLjava/lang/String;IILjava/lang/String;)V");
	cb_globalconnect = (*env)->GetStaticMethodID(env, cls, "cb_globalconnect",
			"(IBBBBLjava/lang/String;)V");
	int result = SmartBusNetCli_Init((unsigned char) unitid);
	SmartBusNetCli_SetCallBackFn(connection_cb, recvdata_cb, disconnect_cb,
			invokeflow_ret_cb, globalconnect_cb, NULL);
	return (jint) result;
}

void JNICALL Java_com_hesong_smartbus_client_net_JniWrapper_Release(JNIEnv *env,
		jclass cls) {
	clazz = NULL;
	SmartBusNetCli_Release();
}

jint JNICALL Java_com_hesong_smartbus_client_net_JniWrapper_CreateConnect(
		JNIEnv *env, jclass cls, jbyte local_clientid, jlong local_clienttype,
		jstring masterip, jshort masterport, jstring slaverip,
		jshort slaverport, jstring author_username, jstring author_pwd,
		jstring add_info) {
	const char *pc_masterip = (*env)->GetStringUTFChars(env, masterip, NULL);
	const char *pc_slaverip = (*env)->GetStringUTFChars(env, slaverip, NULL);
	const char *pc_author_username = (*env)->GetStringUTFChars(env,
			author_username, NULL);
	const char *pc_author_pwd = (*env)->GetStringUTFChars(env, author_pwd,
	NULL);
	const char *pc_add_info = (*env)->GetStringUTFChars(env, add_info, NULL);

	int result = SmartBusNetCli_CreateConnect((unsigned char) local_clientid,
			(long) local_clienttype, pc_masterip, (unsigned short) masterport,
			pc_slaverip, (unsigned short) slaverport, pc_author_username,
			pc_author_pwd, pc_add_info);

	(*env)->ReleaseStringUTFChars(env, masterip, pc_masterip);
	(*env)->ReleaseStringUTFChars(env, slaverip, pc_slaverip);
	(*env)->ReleaseStringUTFChars(env, author_username, pc_author_username);
	(*env)->ReleaseStringUTFChars(env, author_pwd, pc_author_pwd);
	(*env)->ReleaseStringUTFChars(env, add_info, pc_add_info);

	return (jint) result;
}

jint JNICALL Java_com_hesong_smartbus_client_net_JniWrapper_SendText(
		JNIEnv *env, jclass cls, jbyte local_clientid, jbyte cmd, jbyte cmdtype,
		jint dst_unitid, jint dst_clientid, jint dst_clienttype, jstring txt) {
	const char* pc_txt = (*env)->GetStringUTFChars(env, txt, NULL);
	int txt_sz = (pc_txt == NULL) ? 0 : strlen(pc_txt) + 1;
	int result = SmartBusNetCli_SendData((unsigned char) local_clientid,
			(unsigned char) cmd, (unsigned char) cmdtype, (int) dst_unitid,
			(int) dst_clientid, (int) dst_clienttype, pc_txt, txt_sz);
	(*env)->ReleaseStringUTFChars(env, txt, pc_txt);
	return (jint) result;
}

jint JNICALL Java_com_hesong_smartbus_client_net_JniWrapper_RemoteInvokeFlow(
		JNIEnv *env, jclass cls, jbyte local_clientid, jint server_unitid,
		jint ipscindex, jstring projectid, jstring flowid, jint mode,
		jint timeout, jstring in_valuelist) {
	const char* pc_projectid = (*env)->GetStringUTFChars(env, projectid, NULL);
	const char* pc_flowid = (*env)->GetStringUTFChars(env, flowid, NULL);
	const char* pc_in_valuelist = (*env)->GetStringUTFChars(env, in_valuelist,
	NULL);
	int result = SmartBusNetCli_RemoteInvokeFlow((unsigned char) local_clientid,
			(int) server_unitid, (int) ipscindex, pc_projectid, pc_flowid,
			(int) mode, (int) timeout, pc_in_valuelist);
	(*env)->ReleaseStringUTFChars(env, projectid, pc_projectid);
	(*env)->ReleaseStringUTFChars(env, flowid, pc_flowid);
	(*env)->ReleaseStringUTFChars(env, in_valuelist, pc_in_valuelist);
	return (jint) result;
}
