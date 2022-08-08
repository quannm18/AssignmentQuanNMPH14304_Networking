package com.quannm18.assignmentquannmph14304_networking.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.quannm18.assignmentquannmph14304_networking.R
import com.quannm18.assignmentquannmph14304_networking.network.TodoInterface
import com.quannm18.assignmentquannmph14304_networking.view.repository.TodoRepository
import com.quannm18.assignmentquannmph14304_networking.viewmodel.MainViewModel
import com.quannm18.assignmentquannmph14304_networking.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var mViewModel: MainViewModel
    private lateinit var mTodoRepository: TodoRepository
    private lateinit var mViewModelFactory: MainViewModelFactory
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        window.statusBarColor = R.color.color_green
        window.navigationBarColor = R.color.color_green

        mTodoRepository = TodoRepository()
        mViewModelFactory = MainViewModelFactory(mTodoRepository)

        mViewModel = ViewModelProvider(this,mViewModelFactory)[MainViewModel::class.java]

        btnSignUp.setOnClickListener {
            val mFullname = edFullName.text.toString()
            val mUsername = edUsername.text.toString()
            val mPassword = edPassword.text.toString()
            val mRetypePassword = edtRetypePassword.text.toString()
            val mEmail = edEmail.text.toString()
            val isAccept = chkAccept.isChecked

            if (mFullname.isEmpty()||mUsername.isEmpty()
                ||mPassword.isEmpty()||mEmail.isEmpty()){
                Toast.makeText(this, "Vui lòng nhập đầy đủ các trường",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!mEmail.contains("@")){
                Toast.makeText(this, "Email sai định dạng",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (mPassword!=mRetypePassword){
                Toast.makeText(this, "Mật khẩu không khớp",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!isAccept){
                Toast.makeText(this, "Bạn chưa đồng ý điều khoản",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            mViewModel.signUp(mFullname,mUsername,mPassword,mEmail)
        }
        mViewModel.myInsertRepository.observe(this){
            if (it.isSuccessful){
                if (it.body()?.uppercase()?.contains("Lỗi".uppercase()) != true){
                    Toast.makeText(this,"Tạo tài khoản thành công!",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,MainActivity::class.java))
                }else{
                    Toast.makeText(this,"Lỗi đăng ký!",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}