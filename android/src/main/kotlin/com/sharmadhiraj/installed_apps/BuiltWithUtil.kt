package com.sharmadhiraj.installed_apps

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import java.util.zip.ZipFile
import dalvik.system.ZipPathValidator

class BuiltWithUtil {

    companion object {

        fun getPlatform(applicationInfo: ApplicationInfo): String {
            if (Build.VERSION.SDK_INT >= 34) {
                ZipPathValidator.clearCallback()
            }

            val apkPath = applicationInfo.sourceDir
            val zipFile: ZipFile
            val entries: List<String>
            try {
                zipFile = ZipFile(apkPath)
                entries = zipFile.entries().toList().map { it.name }
            } catch (e: ZipException) {
                Log.e("ZIP_ERROR", "Error processing ZIP file", e)
                return "error_processing_zip"
            }

            return when {
                isFlutterApp(entries) -> "flutter"
                isReactNativeApp(entries) -> "react_native"
                isXamarinApp(entries) -> "xamarin"
                isIonicApp(entries) -> "ionic"
                else -> "native_or_others"
            }
        }

        private fun isFlutterApp(entries: List<String>): Boolean {
            return contains(entries, "/flutter_assets/")
        }

        private fun isReactNativeApp(entries: List<String>): Boolean {
            return contains(entries, "react_native_routes.json")
                    || contains(entries, "libs_reactnativecore_components")
                    || contains(entries, "node_modules_reactnative")
        }

        private fun isXamarinApp(entries: List<String>): Boolean {
            return contains(entries, "libaot-Xamarin")
        }

        private fun isIonicApp(entries: List<String>): Boolean {
            return contains(entries, "node_modules_ionic")
        }

        private fun contains(entries: List<String>, value: String): Boolean {
            return entries.firstOrNull { entry -> entry.contains(value) } != null
        }

        fun getAppNameFromPackage(context: Context, packageInfo: PackageInfo): String {
            return packageInfo.applicationInfo.loadLabel(context.packageManager).toString()
        }


    }

}
