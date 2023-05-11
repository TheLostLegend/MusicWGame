package com.serwylo.beatgame.ui
//
//import com.badlogic.gdx.backends.android.AndroidApplication
//import com.badlogic.gdx.files.FileHandle
//import games.spooky.gdx.nativefilechooser.NativeFileChooser
//import games.spooky.gdx.nativefilechooser.NativeFileChooserCallback
//import games.spooky.gdx.nativefilechooser.NativeFileChooserConfiguration
//import games.spooky.gdx.nativefilechooser.NativeFileChooserUtils
//import java.io.*
//import java.net.MalformedURLException
//
//class AndroidFileChooser(application: AndroidApplication) : NativeFileChooser {
//    private val app: AndroidApplication
//
//    override fun chooseFile(
//        configuration: NativeFileChooserConfiguration ,
//        callback: NativeFileChooserCallback
//    ) {
//        NativeFileChooserUtils.checkNotNull(configuration , "configuration")
//        NativeFileChooserUtils.checkNotNull(callback , "callback")
//
//        // Create target Intent for new Activity
//        val intent=Intent()
//        intent.setAction(Intent.ACTION_GET_CONTENT)
//
//        // This one will ensure we have access to the
//        // MediaStore.MediaColumns.DISPLAY_NAME property
//        intent.addCategory(Intent.CATEGORY_OPENABLE)
//
//        // Handle MIME type filter and starting path, if any
//        var data: android.net.Uri?=null
//        var type: String?=null
//        if (configuration.directory != null) {
//            try {
//                data=
//                    android.net.Uri.parse(configuration.directory.file().toURI().toURL().toString())
//            } catch (ex: MalformedURLException) {
//                app.error(javaClass.simpleName , "Invalid starting directory" , ex)
//            }
//        }
//        if (configuration.mimeFilter != null) type=normalizeMimeType(configuration.mimeFilter)
//        if (data == null) {
//            if (type != null) {
//                intent.setType(type)
//            }
//        } else {
//            if (type == null) {
//                intent.setData(data)
//            } else {
//                intent.setDataAndType(data , type)
//            }
//        }
//
//        // Warn if name filter was provided (not supported on this platform)
//        if (configuration.nameFilter != null) app.debug(
//            javaClass.simpleName ,
//            "nameFilter property is not supported on Android"
//        )
//
//        // Register a listener to get a callback
//        // It will deregister by itself on first call
//        app.addAndroidEventListener(object : AndroidEventListener() {
//            override fun onActivityResult(requestCode: Int , resultCode: Int , data: Intent) {
//
//                // Don't interfere with other activity results
//                if (requestCode != IntentCode) return
//                when (resultCode) {
//                    Activity.RESULT_CANCELED ->                    // Action got cancelled
//                        callback.onCancellation()
//                    Activity.RESULT_OK -> try {
//                        val file: FileHandle
//
//                        // Get the Uri of the selected file
//                        val uri: android.net.Uri=data.getData()
//
//                        // Try to build file from it
//                        file=fileHandleFromUri(uri)
//
//                        // Call success callback
//                        callback.onFileChosen(file)
//                    } catch (ex: IOException) {
//                        callback.onError(ex)
//                    }
//                    else -> {}
//                }
//
//                // Self deregistration
//                app.removeAndroidEventListener(this)
//            }
//        })
//        try {
//            app.startActivityForResult(
//                Intent.createChooser(intent , configuration.title) ,
//                IntentCode
//            )
//        } catch (ex: ActivityNotFoundException) {
//            callback.onError(ex)
//        }
//    }
//
//    @Throws(IOException::class)
//    private fun fileHandleFromUri(uri: android.net.Uri): FileHandle {
//        var f: File=File(uri.toString())
//        if (!f.exists()) {
//
//            // Copy stream to temp file and return that file
//            val outputDir: File=app.getCacheDir()
//            f=File(outputDir , "~" + nameFromUri(uri))
//            var input: InputStream?=null
//            var output: OutputStream?=null
//            try {
//                input=app.getContentResolver().openInputStream(uri)
//                output=FileOutputStream(f)
//                copyStream(input , output)
//            } finally {
//                input?.close()
//                output?.close()
//            }
//        }
//        return FileHandle(f)
//    }
//
//    private fun nameFromUri(uri: android.net.Uri): String {
//        val projection: Array<String>= arrayOf<kotlin.String>(MediaStore.MediaColumns.DISPLAY_NAME)
//        val metaCursor: android.database.Cursor=
//            app.getContentResolver().query(uri , projection , null , null , null)
//        if (metaCursor != null) {
//            try {
//                if (metaCursor.moveToFirst()) {
//                    return metaCursor.getString(0)
//                }
//            } finally {
//                metaCursor.close()
//            }
//        }
//        return uri.getLastPathSegment()
//    }
//
//    companion object {
//        private const val IntentCode=19161107
//        @Throws(IOException::class)
//        private fun copyStream(`in`: InputStream? , out: OutputStream) {
//            val buffer=ByteArray(2048)
//            var n=`in`!!.read(buffer)
//            while (n >= 0) {
//                out.write(buffer , 0 , n)
//                n=`in`.read(buffer)
//            }
//        }
//        private fun normalizeMimeType(type: String): String? {
//            var type=type ?: return null
//            type=type.trim { it <= ' ' }.lowercase()
//            val semicolonIndex=type.indexOf(';')
//            if (semicolonIndex != -1) {
//                type=type.substring(0 , semicolonIndex)
//            }
//            return type
//        }
//    }
//    init {
//        NativeFileChooserUtils.checkNotNull(application , "application")
//        app=application
//    }
//}
