package com.baybaka.increasingring.service

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.BaseColumns
import android.provider.ContactsContract
import android.support.v4.content.ContextCompat


interface IMediaPlayerProvider {
    fun getConfiguredPlayer(number: String): MediaPlayer?
}

class MediaPlayerProvider(val context: Context) : IMediaPlayerProvider {

    //todo inject checker singleton whenever it is needed?
    fun hasPermissions(): Boolean {

        val  noSecurity = Build.VERSION.SDK_INT < Build.VERSION_CODES.M
        fun accessGranted() = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED

        return noSecurity || accessGranted()
    }

    override fun getConfiguredPlayer(number: String): MediaPlayer? {
        val uri = if (!hasPermissions()) {
            //todo should be called in UI thread
//            val toast = Toast.makeText(context, R.string.cannot_access_contacts, duration)
//            toast.show()
            RingtoneManager.getDefaultUri(1)
        } else {
            val ringTone = getRingTone(number)
            ringTone?.let { Uri.parse(it) } ?: RingtoneManager.getDefaultUri(1)
        }

//        return getPlayer(uri)
        return getPlayer(RingtoneManager.getDefaultUri(1))
    }

    private fun getRingTone(number: String): String? {
        val uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number))

        val contentResolver = context.contentResolver
        val contactLookup = contentResolver.query(uri, arrayOf(BaseColumns._ID, ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.Contacts.CUSTOM_RINGTONE), null, null, null)

        return contactLookup?.use {
            it: Cursor ->
            if (it.count > 0) {
                it.moveToNext()
                val ringTone = it.getString(it.getColumnIndex(ContactsContract.Data.CUSTOM_RINGTONE))
                ringTone
            } else null

        }


    }

    private fun getPlayer(uri: Uri): MediaPlayer? = try {

        val mediaPlayer = MediaPlayer.create(context, uri)
        //case mediaPlayer creation failed (some sony devices)
        mediaPlayer?.let {
            it.setAudioStreamType(AudioManager.STREAM_MUSIC)
            it.isLooping = true
        }
        mediaPlayer
    } catch (e: RuntimeException) {
        e.printStackTrace()
        null
    }
}