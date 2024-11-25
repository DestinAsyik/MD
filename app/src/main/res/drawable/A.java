package drawable;

<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".ui.login.LoginActivity">

    <TextView
        android:id="@+id/textView2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="40dp"
        android:text="@string/tittle"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <ImageView
        android:id="@+id/imageView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/textView2"
        app:srcCompat="@drawable/banner_login"
        tools:ignore="ContentDescription" />

    <TextView
        android:id="@+id/textView3"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/sign_In"
        app:layout_constraintStart_toStartOf="@+id/imageView"
        app:layout_constraintTop_toBottomOf="@+id/imageView" />

    <TextView
        android:id="@+id/textView4"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/deskripsi"
        app:layout_constraintStart_toStartOf="@+id/textView3"
        app:layout_constraintTop_toBottomOf="@+id/textView3" />

    <Button
        android:id="@+id/button3"
        android:layout_width="300dp"
        android:layout_height="60dp"
        android:layout_marginTop="12dp"
        android:backgroundTint="#336749"
        android:fontFamily="@font/poppins_bold"
        android:text="@string/login"
        android:textSize="16sp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/password_input" />

    <EditText
        android:id="@+id/email_input"
        android:layout_width="348dp"
        android:layout_height="52dp"
        android:layout_marginTop="20dp"
        android:background="@drawable/rounded_corner"
        android:ems="10"
        android:fontFamily="@font/inter_regular"
        android:hint="@string/email"
        android:inputType="textEmailAddress"
        android:padding="15dp"
        android:textColor="#9A9A9A"
        android:textSize="18sp"
        app:layout_constraintStart_toStartOf="@+id/textView4"
        app:layout_constraintTop_toBottomOf="@+id/textView4" />

    <EditText
        android:id="@+id/password_input"
        android:layout_width="348dp"
        android:layout_height="52dp"
        android:layout_marginTop="16dp"
        android:background="@drawable/rounded_corner"
        android:ems="10"
        android:fontFamily="@font/inter_regular"
        android:hint="@string/password"
        android:inputType="textPassword"
        android:padding="15dp"
        android:textColor="#9A9A9A"
        android:textSize="18sp"
        app:layout_constraintStart_toStartOf="@+id/email_input"
        app:layout_constraintTop_toBottomOf="@+id/email_input" />

</androidx.constraintlayout.widget.ConstraintLayout>