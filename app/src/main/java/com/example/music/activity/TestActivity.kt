package com.example.music.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.music.R
import com.example.music.test.Author
import com.example.music.test.Book
import kotlinx.android.synthetic.main.activity_test.*
import org.litepal.LitePal


class TestActivity : AppCompatActivity() {
    val TAG = "TestActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        Log.d(TAG,"cnm")
        bt_save.setOnClickListener {
            val author1 = Author()
            author1.apply {
                name = "逃课"
                age = 30
                save()
            }

//            val author2 = Author()
//            author2.apply {
//                name = "第二个作者"
//                age = 30
//                save()
//            }


            val book1 = Book()
            book1.apply {
                name = "book1"
                price = 10
                authorList.add(author1)
                save()
            }




//            author1.list.add(book1)
//            author1.save()

//            author2.list = mlist
//            author2.save()

        }


        bt_get.setOnClickListener {
            val author = LitePal.findFirst(Author::class.java,true)
            if (author.list.isNullOrEmpty()){
                Log.d(TAG,"list为空")
            }else{

                for (i in 0 until author.list!!.size){
                    Log.d(TAG, author.list!![i].name)
                }
            }

//            val author2 = LitePal.where("name = ?","第二个作者").findFirst(Author::class.java,true)
//            if (!author2.list.isNullOrEmpty()){
//                Log.d(TAG, "第二个作者"+author2.list!![0].name)
//            }else{
//                Log.d(TAG,"list为空")
//            }
//
//            val book1 = LitePal.findFirst(Book::class.java,true)
//            if (book1!=null){
//                Log.d(TAG,book1.name)
//            }
//
//            val testList = book1.authorList
//            if (testList.isNullOrEmpty()){
//                Log.d(TAG,"对应的作者表被删除")
//            }else{
//                for (i in 0 until testList.size){
//                    Log.d(TAG,testList[i].name)
//                }
//            }
        }
        bt_jian.setOnClickListener {
            val author = LitePal.findFirst(Author::class.java,true)
//            author.list?.removeAt(0)
//            author.save()

//            val author2 = LitePal.where("name = ?","第二个作者").findFirst(Author::class.java,true)
//            author2.list?.removeAt(0)
//            author2.save()

            val book = LitePal.findFirst(Book::class.java)
            book.authorList.remove(author)
            book.save()

        }

        other.setOnClickListener {

            LitePal.deleteAll(Author::class.java)
        }


    }
}


