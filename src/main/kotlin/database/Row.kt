package database

import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.util.*
import kotlin.text.Charsets.UTF_16
import kotlin.text.Charsets.UTF_8


const val ID_SIZE: Int = Int.SIZE_BYTES
const val USERNAME_SIZE = 32
const val EMAIL_SIZE = 255

const val ID_OFFSET = 0
const val USERNAME_OFFSET = ID_OFFSET + ID_SIZE
const val EMAIL_OFFSET = USERNAME_OFFSET + USERNAME_SIZE


const val ROW_SIZE = ID_SIZE + USERNAME_SIZE + EMAIL_SIZE

class Row(val id: Int, val userName: String, val email: String) {


    fun serialize(bytebuffer: ByteBuffer) {
        bytebuffer.putInt(id)

        val trimmedUserName = trimByteArray(userName, USERNAME_SIZE)
        val trimmedEmail = trimByteArray(email, EMAIL_SIZE)

        bytebuffer
            .put(trimmedUserName)
            .put(trimmedEmail)
    }

    fun deserialize(byteBuffer: ByteBuffer): Row {
        val id = byteBuffer.getInt(ID_OFFSET)

        val uname = deserializeString(byteBuffer, USERNAME_OFFSET, USERNAME_SIZE)
        val em = deserializeString(byteBuffer, EMAIL_OFFSET, EMAIL_SIZE)

        return Row(id, uname, em)
    }

    private fun trimByteArray(stringToConvert: String, sizeToScale: Int): ByteArray {
        // Kotlin version of Java's String.getBytes()
        val strInByteArr: ByteArray = stringToConvert.toByteArray()
        return Arrays.copyOf(strInByteArr, sizeToScale)
    }

    private fun deserializeString(byteBuffer: ByteBuffer, pos: Int, size: Int): String {
        val stringBytes = ByteArray(size)
        byteBuffer.position(pos)
        byteBuffer.get(stringBytes)
        return String(stringBytes).replace(0.toChar().toString(), "");
    }
}

class Table(var numberOfRows: Int, var rows: MutableList<Row>) {

    val tableMaxNumberOfPages: Int = 100

    private val pageSize = 4096



    private val rowsPerPage = pageSize / ROW_SIZE
    private val tableMaxNumberOfRows = rowsPerPage * tableMaxNumberOfPages

    fun getRowSlot(rowNumber: Int): Int {
        val pageNumber: Int = rowNumber / rowsPerPage
        val rowOffset: Int = rowNumber % rowsPerPage
        val byteOffset: Int = rowOffset * ROW_SIZE
        return pageNumber + byteOffset
    }

    fun addToRow(row: Row?) {
        if (row != null) rows.add(row)
    }

}


