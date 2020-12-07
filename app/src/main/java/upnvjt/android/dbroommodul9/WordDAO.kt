package upnvjt.android.dbroommodul9

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

//pengenalan sebuah class data access object atau alamat akses objek data
@Dao
interface WordDAO {
    //Query Select pada tabel database
    @Query("SELECT * FROM word_table ORDER BY word ASC")
    fun getAlphabetizedWords(): LiveData<List<Word>>

    //Query Insert pada tabel database
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: Word)

    //Query Delete pada tabel database
    @Query("DELETE FROM word_table")
    suspend fun deleteAll()
}