package com.centrevillehs.fblaconnectv2

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlin.random.Random

class EmailService {
    companion object {
        private const val SMTP_HOST = "smtp.gmail.com"
        private const val SMTP_PORT = "587"
        private const val EMAIL = "fblacentralverify@gmail.com"
        private const val PASSWORD = "vuik iovc yfwx cson"

        fun generateVerificationCode(): String {
            return Random.nextInt(100000, 999999).toString()
        }

        suspend fun sendVerificationEmail(toEmail: String, code: String): Boolean {
            return withContext(Dispatchers.IO) {
                try {
                    val props = Properties().apply {
                        put("mail.smtp.auth", "true")
                        put("mail.smtp.starttls.enable", "true")
                        put("mail.smtp.starttls.required", "true")
                        put("mail.smtp.host", SMTP_HOST)
                        put("mail.smtp.port", SMTP_PORT)
                        put("mail.smtp.ssl.protocols", "TLSv1.2")
                        put("mail.smtp.ssl.trust", SMTP_HOST)
                    }

                    val session = Session.getInstance(props, object : Authenticator() {
                        override fun getPasswordAuthentication(): PasswordAuthentication {
                            return PasswordAuthentication(EMAIL, PASSWORD)
                        }
                    })

                    session.debug = true // Enable debug logging

                    val message = MimeMessage(session).apply {
                        setFrom(InternetAddress(EMAIL))
                        setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail))
                        subject = "FBLA Connect - Email Verification"
                        setText("""
                            Welcome to FBLA Central!
                            
                            Your verification code is: $code
                            
                            This code will expire in 10 minutes.
                            
                            If you didn't request this code, please ignore this email.
                            
                            - FBLA Central Team
                        """.trimIndent())
                    }

                    Transport.send(message)
                    Log.d("EmailService", "Email sent successfully to $toEmail")
                    true
                } catch (e: Exception) {
                    Log.e("EmailService", "Error sending email", e)
                    false
                }
            }
        }
    }
}