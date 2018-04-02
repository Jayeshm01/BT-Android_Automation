PACKAGE COM.EXAMPLE.ANDROIDREMOTE;
IMPORT ANDROID.APP.ACTIVITY;
IMPORT ANDROID.BLUETOOTH.BLUETOOTHADAPTER;
IMPORT ANDROID.CONTENT.INTENT;
IMPORT ANDROID.OS.BUNDLE;
IMPORT ANDROID.OS.HANDLER;
IMPORT ANDROID.OS.MESSAGE;
IMPORT ANDROID.UTIL.LOG;
IMPORT ANDROID.VIEW.VIEW;

IMPORT ANDROID.VIEW.VIEW.ONCLICKLISTENER;
IMPORT ANDROID.WIDGET.BUTTON;
IMPORT ANDROID.WIDGET.IMAGEBUTTON;
IMPORT ANDROID.WIDGET.IMAGEVIEW;
IMPORT ANDROID.WIDGET.TEXTVIEW;
IMPORT ANDROID.WIDGET.TOAST;
IMPORT ANDROID.WIDGET.TOGGLEBUTTON;
PUBLIC CLASS ANDROIDREMOTEACTIVITY EXTENDS ACTIVITY IMPLEMENTS ONCLICKLISTENER {
PRIVATE TEXTVIEW LOGVIEW;
PRIVATE BUTTON CONNECT;
PRIVATE BUTTON DECONNECT;
PRIVATE BUTTON LIGHTINON , LIGHTINOFF , LIGHTINAUTO ;
PRIVATE BUTTON FANON , FANOFF , FANAUTO ;
PRIVATE BUTTON LIGHTOUTON ,LIGHTOUTOFF , LIGHTOUTAUTO ;
PRIVATE BUTTON SOUNDON , SOUNDOFF ;
PRIVATE BUTTON TEMP ;
PRIVATE BLUETOOTHADAPTER MBLUETOOTHADAPTER = NULL;
PRIVATE STRING[] LOGARRAY = NULL;
PRIVATE BTINTERFACE BT = NULL;
STATIC FINAL STRING TAG = "BTMODULE";
STATIC FINAL INT REQUEST_ENABLE_BT = 3;
//THIS HANDLER LISTENS TO MESSAGES FROM THE BLUETOOTH INTERFACE AND ADDS THEM TO THE LOG
FINAL HANDLER HANDLER = NEW HANDLER() {
PUBLIC VOID HANDLEMESSAGE(MESSAGE MSG) {
STRING DATA = MSG.GETDATA().GETSTRING("RECEIVEDDATA");
ADDTOLOG(DATA);
}
};
//THIS HANDLER IS DEDICATED TO THE STATUS OF THE BLUETOOTH CONNECTION
FINAL HANDLER HANDLERSTATUS = NEW HANDLER() {
PUBLIC VOID HANDLEMESSAGE(MESSAGE MSG) {
INT STATUS = MSG.ARG1;

IF(STATUS == BTINTERFACE.CONNECTED) {
ADDTOLOG("CONNECTED");
} ELSE IF(STATUS == BTINTERFACE.DISCONNECTED) {
ADDTOLOG("DISCONNECTED");
}
}
};
//HANDLES THE LOG VIEW MODIFICATION
//ONLY THE MOST RECENT MESSAGES ARE SHOWN
PRIVATE VOID ADDTOLOG(STRING MESSAGE){
FOR (INT I = 1; I < LOGARRAY.LENGTH; I++){
LOGARRAY[I-1] = LOGARRAY[I];
}
LOGARRAY[LOGARRAY.LENGTH - 1] = MESSAGE;
LOGVIEW.SETTEXT("");
FOR (INT I = 0; I < LOGARRAY.LENGTH; I++){
IF (LOGARRAY[I] != NULL){
LOGVIEW.APPEND(LOGARRAY[I] + "\N");
}
}
}
/** CALLED WHEN THE ACTIVITY IS FIRST CREATED. */
@OVERRIDE
PUBLIC VOID ONCREATE(BUNDLE SAVEDINSTANCESTATE) {
SUPER.ONCREATE(SAVEDINSTANCESTATE);
SETCONTENTVIEW(R.LAYOUT.ACTIVITY_ANDROID_REMOTE);
TOAST.MAKETEXT(ANDROIDREMOTEACTIVITY.THIS, "WELCOME TO ANDROID REMOTE 1.0", TOAST.LENGTH_SHORT).SHOW();
TOAST.MAKETEXT(ANDROIDREMOTEACTIVITY.THIS, "APP DESIGNED BY JAYESH MEHTA", TOAST.LENGTH_LONG).SHOW();
//FIRST, INFLATE ALL LAYOUT OBJECTS, AND SET CLICK LISTENERS
LOGVIEW = (TEXTVIEW)FINDVIEWBYID(R.ID.LOGVIEW);
//I CHOSE TO DISPLAY ONLY THE LAST 5 MESSAGES

LOGARRAY = NEW STRING[2];
CONNECT = (BUTTON)FINDVIEWBYID(R.ID.CONNECT);
CONNECT.SETONCLICKLISTENER(THIS);
DECONNECT = (BUTTON)FINDVIEWBYID(R.ID.DECONNECT);
DECONNECT.SETONCLICKLISTENER(THIS);
LIGHTINOFF =(BUTTON)FINDVIEWBYID(R.ID.LIGHTINOFF);
LIGHTINOFF.SETONCLICKLISTENER(THIS);
LIGHTINON = (BUTTON)FINDVIEWBYID(R.ID.LIGHTINON);
LIGHTINON.SETONCLICKLISTENER(THIS);
LIGHTINAUTO = (BUTTON)FINDVIEWBYID(R.ID.LIGHTINAUTO);
LIGHTINAUTO.SETONCLICKLISTENER(THIS);
FANOFF = (BUTTON)FINDVIEWBYID(R.ID.FANOFF);
FANOFF.SETONCLICKLISTENER(THIS);
FANON = (BUTTON)FINDVIEWBYID(R.ID.FANON);
FANON.SETONCLICKLISTENER(THIS);
FANAUTO = (BUTTON)FINDVIEWBYID(R.ID.FANAUTO);
FANAUTO.SETONCLICKLISTENER(THIS);
LIGHTOUTOFF =(BUTTON)FINDVIEWBYID(R.ID.LIGHTOUTOFF);
LIGHTOUTOFF.SETONCLICKLISTENER(THIS);
LIGHTOUTON = (BUTTON)FINDVIEWBYID(R.ID.LIGHTOUTON);
LIGHTOUTON.SETONCLICKLISTENER(THIS);
LIGHTOUTAUTO = (BUTTON)FINDVIEWBYID(R.ID.LIGHTOUTAUTO);
LIGHTOUTAUTO.SETONCLICKLISTENER(THIS);
SOUNDOFF = (BUTTON)FINDVIEWBYID(R.ID.SOUNDOFF);
SOUNDOFF.SETONCLICKLISTENER(THIS);
TEMP = (BUTTON)FINDVIEWBYID(R.ID.TEMP);
TEMP.SETONCLICKLISTENER(THIS);
SOUNDON = (BUTTON ) FINDVIEWBYID(R.ID.SOUNFON);
SOUNDON.SETONCLICKLISTENER(THIS);
}
//IT IS BETTER TO HANDLE BLUETOOTH CONNECTION IN ONRESUME (IE ABLE TO RESET WHEN CHANGING SCREENS)
@OVERRIDE
PUBLIC VOID ONRESUME() {

SUPER.ONRESUME();
//FIRST OF ALL, WE CHECK IF THERE IS BLUETOOTH ON THE PHONE
MBLUETOOTHADAPTER = BLUETOOTHADAPTER.GETDEFAULTADAPTER();
IF (MBLUETOOTHADAPTER == NULL) {
// DEVICE DOES NOT SUPPORT BLUETOOTH
LOG.V(TAG, "DEVICE DOES NOT SUPPORT BLUETOOTH");
}
ELSE{
//DEVICE SUPPORTS BT
IF (!MBLUETOOTHADAPTER.ISENABLED()){
//IF BLUETOOTH NOT ACTIVATED, THEN REQUEST IT
INTENT ENABLEBTINTENT = NEW INTENT(BLUETOOTHADAPTER.ACTION_REQUEST_ENABLE);
STARTACTIVITYFORRESULT(ENABLEBTINTENT, REQUEST_ENABLE_BT);
}
ELSE{
//BT ACTIVATED, THEN INITIATE THE BTINTERFACE OBJECT TO HANDLE ALL BT COMMUNICATION
BT = NEW BTINTERFACE(HANDLERSTATUS, HANDLER);
}
}
}
//CALLED ONLY IF THE BT IS NOT ALREADY ACTIVATED, IN ORDER TO ACTIVATE IT
PROTECTED VOID ONACTIVITYRESULT(INT REQUESTCODE, INT RESULTCODE, INTENT MOREDATA){
IF (REQUESTCODE == REQUEST_ENABLE_BT){
IF (RESULTCODE == ACTIVITY.RESULT_OK){
//BT ACTIVATED, THEN INITIATE THE BTINTERFACE OBJECT TO HANDLE ALL BT COMMUNICATION
BT = NEW BTINTERFACE(HANDLERSTATUS, HANDLER);
}
ELSE IF (RESULTCODE == ACTIVITY.RESULT_CANCELED)
LOG.V(TAG, "BT NOT ACTIVATED");
ELSE

LOG.V(TAG, "RESULT CODE NOT KNOWN");
}
ELSE{
LOG.V(TAG, "REQUEST CODE NOT KNOWN");
}
}
//HANDLES THE CLICKS ON VARIOUS PARTS OF THE SCREEN
//ALL BUTTONS LAUNCH A FUNCTION FROM THE BTINTERFACE OBJECT
@OVERRIDE
PUBLIC VOID ONCLICK(VIEW V) {
IF(V == CONNECT) {
ADDTOLOG("TRYING TO CONNECT");
BT.CONNECT();
}
ELSE IF(V == DECONNECT) {
ADDTOLOG("CLOSING CONNECTION");
BT.CLOSE();
}
ELSE IF(V == LIGHTINON) {
BT.SENDDATA("1");
}
ELSE IF(V == LIGHTINOFF) {
BT.SENDDATA("2");
}
ELSE IF(V == LIGHTINAUTO) {
BT.SENDDATA("0");
}
ELSE IF(V == FANON) {
BT.SENDDATA("5");
}
ELSE IF(V == FANOFF) {

BT.SENDDATA("6");
}
ELSE IF(V == FANAUTO) {
BT.SENDDATA("7");
}
ELSE IF(V == LIGHTOUTON) {
BT.SENDDATA("8");
}
ELSE IF(V == LIGHTOUTOFF) {
BT.SENDDATA("9");
}
ELSE IF(V == LIGHTOUTAUTO) {
BT.SENDDATA("A");
}
ELSE IF(V == SOUNDON) {
BT.SENDDATA("3");
}
ELSE IF(V == SOUNDOFF) {
BT.SENDDATA("4");
}
ELSE IF(V == TEMP) {
BT.SENDDATA("C");
}
}
}
