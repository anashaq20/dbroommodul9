package upnvjt.android.dbroommodul9

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

//Menganotasi sebuah kelas menjadi sebuah ruang database dengan
// tabel (entitas) dari kelas Word
@Database(entities = arrayOf(Word::class), version = 1, exportSchema = false)
public abstract class WordRoomDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDAO
    companion object {
        //Singleton mencegah banyak instance dari database
        // terbuka dalam waktu yang bersamaan
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        //inisiasi fungsi database untuk sinkronisasi database secara terus menerus
        fun getDatabase(context: Context, scope: CoroutineScope) : WordRoomDatabase {
            val tempInstance = INSTANCE
            //kegunaan if akan membuat fungsi database mengembalikan nilai
            // berupa instance jika instance sudah dibuat pada suatu waktu
            if (tempInstance != null) {
                return tempInstance
            }
            //dan fungsi dibawah akan membuat isi dari database terupdate seiring waktu
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext, WordRoomDatabase::class.java,
                    "word_database").addCallback(WordDatabaseCallback(scope)).build()
                INSTANCE= instance
                return instance
            }
        }

        private class WordDatabaseCallback(private val scope: CoroutineScope): RoomDatabase.Callback(){
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database -> scope.launch { populateDatabase(database.wordDao())
                    }
                }
            }
            suspend fun populateDatabase(wordDao : WordDAO){
                //Menghapus semua konten di dalam sini
                wordDao.deleteAll()

                //menambahkan kata contoh
                var word = Word("Hello")
                wordDao.insert(word)
                word = Word("World!")
                wordDao.insert(word)
            }

        }
    }
}
