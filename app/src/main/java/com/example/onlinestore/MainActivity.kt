package com.example.onlinestore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.sax.RootElement
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.get
import com.example.onlinestore.act.EditAdsAct
import com.example.onlinestore.database.DbManager
import com.example.onlinestore.databinding.ActivityMainBinding
import com.example.onlinestore.dialoghelper.DialogConst
import com.example.onlinestore.dialoghelper.DialogHelper
import com.example.onlinestore.dialoghelper.GoogleAcConst
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
     private lateinit var tvAccaunt:TextView
     private lateinit var rootElement:ActivityMainBinding
     private val dialogHelper = DialogHelper(this)
     val mAuth = FirebaseAuth.getInstance()
     val dbManager = DbManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityMainBinding.inflate(layoutInflater)
        val view = rootElement.root
        setContentView(view)
        init()
        dbManager.readDataFromDb()
    }

    override fun onStart() {
        super.onStart()
        uiUpdate(mAuth.currentUser)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == GoogleAcConst.GOOGLE_SIGN_iN_REQUEST_CODE){
            //Log.d("MyLog","Sign in result")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if(account != null){
                    dialogHelper.accHelper.signInFirebaseWithGoogle(account.idToken!!)
                }
            }
            catch (e: ApiException){
                Log.d("MyLog", "Api error : ${e.message}")
            }
        }
        //super.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult (requestCode, resultCode, data)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.id_new_ads){
            val i = Intent(this, EditAdsAct::class.java)
            startActivity(i)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun init(){
        setSupportActionBar(rootElement.mainContent.toolbar)
        var toggle = ActionBarDrawerToggle(this, rootElement.drawerLayout, rootElement.mainContent.toolbar, R.string.open , R.string.close)
        rootElement.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        rootElement.navView.setNavigationItemSelectedListener (this)
        tvAccaunt = rootElement.navView.getHeaderView(0).findViewById(R.id.tvAccountEmail)
    }

     override fun onNavigationItemSelected(item: MenuItem): Boolean {
         when(item.itemId){
             R.id.id_my_ads ->{
                 Toast.makeText(this,"Presed id_my_ads", Toast.LENGTH_LONG).show()
             }
             R.id.id_car ->{
                 Toast.makeText(this,"Presed CARS", Toast.LENGTH_LONG).show()
             }
             R.id.id_pc ->{
                 Toast.makeText(this,"Presed PC", Toast.LENGTH_LONG).show()
             }
             R.id.id_smart ->{
                 Toast.makeText(this,"Presed SMARTPHONES", Toast.LENGTH_LONG).show()
             }
             R.id.id_dm ->{
                 Toast.makeText(this,"Presed DM", Toast.LENGTH_LONG).show()
             }
             R.id.id_sign_up ->{
                 dialogHelper.createSignDialog(DialogConst.SIGN_UP_STATE)
             }
             R.id.id_sign_in ->{
                 dialogHelper.createSignDialog(DialogConst.SIGN_IN_STATE)
             }
             R.id.id_sign_out ->{

                 uiUpdate(null)
                 mAuth.signOut()
                 dialogHelper.accHelper.signOutG()
             }
         }
         rootElement.drawerLayout.closeDrawer(GravityCompat.START);
         return true
     }

    fun uiUpdate(user: FirebaseUser?)  {
        tvAccaunt.text = if(user == null){
            resources.getString(R.string.not_reg)
        }
        else{
            user.email
        }
    }
 }