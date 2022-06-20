package com.cookandroid.itshow_tob

import com.google.firebase.auth.FirebaseAuth

var auth: FirebaseAuth? = FirebaseAuth.getInstance()
var u_email = auth?.currentUser?.email.toString()

