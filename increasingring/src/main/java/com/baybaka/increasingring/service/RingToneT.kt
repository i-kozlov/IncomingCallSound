package com.baybaka.increasingring.service

import android.Manifest
import android.R.attr.duration
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.BaseColumns
import android.provider.ContactsContract
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.baybaka.increasingring.R


class RingToneT(val context: Context) {

    private var hasAccess = false;

    fun updatePermissionInfo(): Boolean {
        val result = Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
        hasAccess = result
        return result
    }

    fun configurePlayer(number: String): MediaPlayer? {

        val uri = if (hasAccess || updatePermissionInfo()) {
            val ringTone = getRingTone(number)
            if (ringTone == null) RingtoneManager.getDefaultUri(1) else Uri.parse(ringTone)
        } else {
            val toast = Toast.makeText(context, R.string.cannot_access_contacts, duration)
            toast.show()
            RingtoneManager.getDefaultUri(1)
        }

        return getPlayer(uri)
    }

    fun getRingTone(number: String): String? {
        val uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number))

        val contentResolver = context.contentResolver
        val contactLookup = contentResolver.query(uri, arrayOf(BaseColumns._ID, ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.Contacts.CUSTOM_RINGTONE), null, null, null)

        contactLookup?.use {
            it ->
            if (it.count > 0) {
                it.moveToNext()
//                name = it.getString(it.getColumnIndex(ContactsContract.Data.DISPLAY_NAME))
                val ringTone = it.getString(it.getColumnIndex(ContactsContract.Data.CUSTOM_RINGTONE))
                //String contactId = contactLookup.getString(contactLookup.getColumnIndex(BaseColumns._ID));
                return ringTone
            }

        }

        return null
    }

    private fun getPlayer(uri: Uri): MediaPlayer? {

        val mediaPlayer = MediaPlayer.create(context, uri)
        //case mediaPlayer creation failed (some sony devices)
        mediaPlayer?.let {
            it.setAudioStreamType(AudioManager.STREAM_MUSIC)
            it.setLooping(true)
            //                mediaPlayer.prepare();
        }

        return mediaPlayer
    }
}