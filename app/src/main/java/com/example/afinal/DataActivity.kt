package com.example.afinal

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.MenuItem.SHOW_AS_ACTION_ALWAYS
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.afinal.databinding.ActivityDataBinding
import com.example.afinal.databinding.DialogInputBinding
import com.example.afinal.databinding.ResultItemBinding

class DataActivity : AppCompatActivity() {
    private val names = mutableListOf<String>()
    private val phones = mutableListOf<String>()
    lateinit var dbHelper: DBHelper
    lateinit var adapter: ResultAdapter
    lateinit var binding: ActivityDataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "전화번호부"

        dbHelper = DBHelper(this)
        adapter = ResultAdapter(names, phones)

        binding.resultList.layoutManager = LinearLayoutManager(this)
        binding.resultList.adapter = adapter
        binding.resultList.addItemDecoration(MyDecoration(this))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(0,0,1,"삽입")?.setShowAsAction(SHOW_AS_ACTION_ALWAYS)
        menu?.add(0,1,1,"검색")?.setShowAsAction(SHOW_AS_ACTION_ALWAYS)
        menu?.add(0,2,1,"정렬")?.setShowAsAction(SHOW_AS_ACTION_ALWAYS)
        menu?.add(0,3,1,"삭제")?.setShowAsAction(SHOW_AS_ACTION_ALWAYS)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            0->{
                val dialogBinding = DialogInputBinding.inflate(layoutInflater)
                AlertDialog.Builder(this)
                    .setTitle("삽입할 전화번호")
                    .setView(dialogBinding.root)
                    .setNegativeButton("취소",null)
                    .setPositiveButton("완료"){_,_ ->
                        val db = dbHelper.writableDatabase
                        db.execSQL("insert into USER_TB(name, phone) values(?,?)",
                            arrayOf(dialogBinding.name.text.toString(), dialogBinding.phone.text.toString()))
                        db.close()
                    }.show()
            }
            1->{
                val db = dbHelper.readableDatabase
                val cursor = db.rawQuery("select name, phone from USER_TB", null)
                names.clear()
                phones.clear()
                while (cursor.moveToNext()){
                    names.add(cursor.getString(0))
                    phones.add(cursor.getString(1))
                }
                db.close()
                adapter.notifyDataSetChanged()
            }
            2->{
                val db = dbHelper.readableDatabase
                val cursor = db.rawQuery("select name, phone from USER_TB ORDER BY name", null)
                names.clear()
                phones.clear()
                while (cursor.moveToNext()){
                    names.add(cursor.getString(0))
                    phones.add(cursor.getString(1))
                }
                db.close()
                adapter.notifyDataSetChanged()
            }
            3->{
                val db = dbHelper.writableDatabase
                dbHelper.onUpgrade(db,1,2)
                db.close()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

class ResultHolder(val binding: ResultItemBinding): RecyclerView.ViewHolder(binding.root)

class ResultAdapter(val names: MutableList<String>, val phones : MutableList<String>):
        RecyclerView.Adapter<ResultHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultHolder =
        ResultHolder(
            ResultItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )

    override fun getItemCount(): Int = names.size + 1

    override fun onBindViewHolder(holder: ResultHolder, position: Int) {
        if(position == 0){
            holder.binding.name.text = "이름"
            holder.binding.phone.text = "전화번호"
        }
        else{
            holder.binding.name.text = names[position - 1]
            holder.binding.phone.text = phones[position - 1]
            holder.binding.name.setTextColor(Color.BLACK)
            holder.binding.phone.setTextColor(Color.BLACK)
        }
    }
        }