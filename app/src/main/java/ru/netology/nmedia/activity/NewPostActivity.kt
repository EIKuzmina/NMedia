package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityNewPostBinding

class NewPostActivity : AppCompatActivity() {
    companion object {
        const val KEY_TEXT = "text"
    } // заводим отдельную константу, ключ для хранения текста
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.okSave.setOnClickListener {  //проверяем введённый текст, что он не пустой, сохранеяем в переменную "text"
            val text = binding.editSave.text.toString()
            if (text.isBlank()) {
                setResult(RESULT_CANCELED) // т.е. не успешное завершение
            } else { //успешно, создаём интент, вызываем конструктор, через "apply" настраиваем(экшены для внутреннего перемещения не нужны):
                setResult(RESULT_OK, Intent().apply { putExtra(KEY_TEXT, text)})
            }
            finish()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}

object NewPostContract : ActivityResultContract<Unit, String?>(){
    override fun createIntent(context: Context, input: Unit) =
        Intent(context, NewPostActivity::class.java)

    override fun parseResult(resultCode: Int, intent: Intent?) =
        intent?.getStringExtra(NewPostActivity.KEY_TEXT)

}