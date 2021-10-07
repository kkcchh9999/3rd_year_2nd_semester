import java.util.*

fun main() {

    var vowelCount = 0
    val vowel = arrayOf('a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U')

    var shiftInput: String = ""

    //평문, 키 입력받기
    print("평문을 입력하세요 : ")
    val input = readLine()
    print("키를 입력하세요 : ")
    val key = readLine()

    val arrSize = if (input!!.length % key!!.length == 0) {
        input!!.length / key!!.length
    } else {
        input!!.length / key!!.length + 1
    }
    var array = Array<CharArray>(arrSize) { CharArray(key.length){ _ -> ' ' } }

    //키의 모음 갯수 체크
    for (i in key!!.indices) {
        if (vowel.contains(key[i])) {
            vowelCount ++
        }
    }

    //키의 모음 갯수만큼 카운트
    for (i in input!!.indices) {
        if (input[i] in 'a'..'z') {
            shiftInput += if (input[i] + vowelCount > 'z') {
                (input[i]+vowelCount-26)
            } else {
                (input[i]+vowelCount)
            }
        } else if (input [i] in 'A'..'Z') {
            shiftInput += if (input[i] + vowelCount > 'Z') {
                (input[i]+vowelCount-26)
            } else {
                (input[i]+vowelCount)
            }
        }
    }
    print(shiftInput)

    var countRow = 0
    var countColumn = 0
    for (i in input!!.indices) {
        array[countRow][countColumn] = shiftInput[i]
        countColumn++
        if (countColumn >= key.length) {
            countColumn = 0
            countRow ++
        }
    }

    print(array.contentDeepToString())

}