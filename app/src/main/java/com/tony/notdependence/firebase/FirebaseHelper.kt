package com.tony.notdependence.firebase

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference

class FirebaseHelper(private val activity: Activity) {

    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance()}
    val database: DatabaseReference by lazy { FirebaseDatabase.getInstance("https://notdependence-default-rtdb.europe-west1.firebasedatabase.app").reference}
    val currentUid = auth.currentUser?.uid

    fun userRef(uid: String) = ref("user", uid)
    fun aimRef(uid: String) = ref("aims", uid)



    fun ref(path: String, uid: String) = database.child(path).child(uid)

    fun currentUserReference(): DatabaseReference = userRef(currentUid!!)

}