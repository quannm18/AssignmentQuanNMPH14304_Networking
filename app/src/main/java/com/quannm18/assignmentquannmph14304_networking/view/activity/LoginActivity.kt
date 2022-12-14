package com.quannm18.assignmentquannmph14304_networking.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.orhanobut.hawk.Hawk
import com.quannm18.assignmentquannmph14304_networking.R
import com.quannm18.assignmentquannmph14304_networking.view.repository.TodoRepository
import com.quannm18.assignmentquannmph14304_networking.viewmodel.MainViewModel
import com.quannm18.assignmentquannmph14304_networking.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var mViewModel: MainViewModel
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        window.statusBarColor = R.color.color_green
        window.navigationBarColor = R.color.color_green

        Hawk.init(this).build()
        val mRepository = TodoRepository()
        val mFactory = MainViewModelFactory(mRepository)
        mViewModel = ViewModelProvider(this, mFactory)[MainViewModel::class.java]

        val mBundleLogin = Hawk.get<Bundle>("login")
        if (mBundleLogin!=null){
            val isRemember = mBundleLogin.getBoolean("checkRemember")
            val mUsername = mBundleLogin.getString("mUsername")
            val mPassword = mBundleLogin.getString("mPassword")
            if (isRemember&&(mUsername!=null && mPassword!=null)){
                edUsername.setText(mUsername)
                edPassword.setText(mPassword)
                chkRemember.isChecked = isRemember
            }else{
                edUsername.setText("")
                edPassword.setText("")
                chkRemember.isChecked = isRemember
            }
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
        btnLogin.setOnClickListener {
            val mUsername = edUsername.text.toString()
            val mPw = edPassword.text.toString()

            if (mUsername.isEmpty() && mPw.isEmpty()) {
                Toast.makeText(this, "Vui l??ng nh???p ????? c??c tr?????ng", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            mViewModel.getLogin(mUsername, mPw)
            var mStatusLogin = ""
            mViewModel.myRepository.observe(this) {
                if (it.isSuccessful) {
                    if (it.body()?.uppercase()
                            ?.contains("c??ng".uppercase()) == true
                    ) {
                        Toast.makeText(this, "????ng nh???p th??nh c??ng!", Toast.LENGTH_SHORT).show()

                        val mBundle = Bundle()
                        mBundle.putString("mUsername", mUsername)
                        mBundle.putString("mPassword", mPw)
                        mBundle.putBoolean("checkRemember", chkRemember.isChecked)
                        Hawk.put("login", mBundle)

                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        Toast.makeText(
                            this,
                            "T??i kho???n ho???c m???t kh???u kh??ng ????ng",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}