Uri Content Provider <br>
content://com.mac.fireflies.wgt.clientscontentproviders/users

Shell to test the Deep Link <br>
adb shell am start -W -a android.intent.action.VIEW -d "example://contentprovider/users" com.williamgdev.example.contentprovider