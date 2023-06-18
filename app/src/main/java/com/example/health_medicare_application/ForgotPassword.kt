package com.example.health_medicare_application

import android.content.Context
import android.telephony.SmsManager
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.health_medicare_application.ui.theme.Activityscreen
import com.example.health_medicare_application.ui.theme.boxes
import com.example.health_medicare_application.ui.theme.fillmaxwid
import com.example.health_medicare_application.ui.theme.fnt20
import com.example.health_medicare_application.ui.theme.horzcenter
import com.example.health_medicare_application.ui.theme.purewhite
import com.example.health_medicare_application.ui.theme.purple673
import com.example.health_medicare_application.ui.theme.rcshape
import com.example.health_medicare_application.ui.theme.txtcenter
import com.example.health_medicare_application.ui.theme.vertspace
import com.google.firebase.database.DatabaseReference

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordPage(context: Context, navController: NavController,databaseReference: DatabaseReference,databaseHelper: UserDatabaseHelper)
{
    Scaffold(
        topBar = { TopBar("FORGOT PASSWORD") },
        content = {pad -> ForgotPassword(pad,context,navController,databaseReference,databaseHelper) },
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPassword(h: PaddingValues, context: Context, navController: NavController, databaseReference: DatabaseReference, databaseHelper: UserDatabaseHelper) {
    Column(
        modifier = Activityscreen,
        horizontalAlignment = horzcenter,
        verticalArrangement = vertspace
    )
    {
        val email = remember { mutableStateOf(TextFieldValue("")) }
        val usrname = remember { mutableStateOf("") }
        val mobile = remember { mutableStateOf(TextFieldValue("")) }
        val newpw = remember { mutableStateOf(TextFieldValue("")) }
        val pwvisib = remember { mutableStateOf(false) }
        val otpuser = remember { mutableStateOf(TextFieldValue("")) }
        val otpsys = remember { mutableStateOf("") }
        val txtfieldcol = TextFieldDefaults.textFieldColors(containerColor = Color.White)
        TextField(
            value = email.value,
            onValueChange = {
                email.value = it
            },
            singleLine = true,
            colors = txtfieldcol,
            label = { Text("Enter Email ID") },
            modifier = fillmaxwid.padding(top = 50.dp)
                .border(BorderStroke(2.dp, purple673)),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) }
        )
        usrname.value = email.value.text.substringBefore("@")
        TextField(
            value = mobile.value,
            onValueChange = {
                if (it.text.length <= 10)
                    mobile.value = it
            },
            colors = txtfieldcol,
            label = { Text("Enter Mobile Number") },
            modifier = boxes,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            leadingIcon = { Icon(Icons.Outlined.Phone, contentDescription = null) }
        )
        Button(
            onClick = {
                otpsys.value = otp();
                val smsManager: SmsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(
                    "+91" + mobile.value,
                    null,
                    "Password Reset OTP : " + otpsys.value + " - Health Medicare App",
                    null,
                    null
                )
                Toast.makeText(
                    context, "OTP Sent Sucessfully",
                    Toast.LENGTH_SHORT
                ).show()
            },
            modifier = fillmaxwid,
            colors = ButtonDefaults.buttonColors(purple673),
            shape = rcshape
        )
        {
            Text(
                text = "GENERATE OTP", color = purewhite, fontSize = fnt20,
                textAlign = txtcenter
            )
        }
        TextField(
            value = otpuser.value,
            onValueChange = {
                if (it.text.length <= 6)
                    otpuser.value = it
            },
            colors = txtfieldcol,
            label = { Text("Enter 6 Digit OTP") },
            modifier = boxes,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            leadingIcon = { Icon(Icons.Outlined.Key, contentDescription = null) }
        )
        TextField(
            value = newpw.value,
            onValueChange = {
                newpw.value = it
            },
            colors = txtfieldcol,
            label = { Text("Enter New Password") },
            modifier = boxes,
            visualTransformation = if (pwvisib.value) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                if (pwvisib.value) {
                    IconButton(onClick = { pwvisib.value = false }) {
                        Icon(
                            imageVector = Icons.Filled.Visibility,
                            contentDescription = "show_pw"
                        )
                    }
                } else {
                    IconButton(
                        onClick = { pwvisib.value = true }) {
                        Icon(
                            imageVector = Icons.Filled.VisibilityOff,
                            contentDescription = "hide_pw"
                        )
                    }
                }
            }
        )
        Button(
            onClick = {
                if (otpsys.value == otpuser.value.text) {
                    databaseReference.child("" + usrname.value).child("password")
                        .setValue(newpw.value.text);
                    databaseHelper.updatePassword(email.value.text, newpw.value.text);
                    Toast.makeText(
                        context, "Password Reset Successful ",
                        Toast.LENGTH_SHORT
                    ).show();
                    navController.navigate("dashboard/${email.value.text}")
                } else {
                    Toast.makeText(
                        context, "Wrong OTP",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = fillmaxwid,
            colors = ButtonDefaults.buttonColors(purple673),
            shape = rcshape
        )
        {
            Text(
                text = "RESET PASSWORD", color = purewhite, fontSize = fnt20
            )
        }
    }
}
fun otp():String
{
    val randomInteger = (1..1000000).random()
    return randomInteger.toString()
}