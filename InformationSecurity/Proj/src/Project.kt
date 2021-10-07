import java.util.*
import kotlin.collections.HashMap

fun main() {

    var vowelCount = 0
    val vowel = arrayOf('a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U')
    var shiftInput: String = ""

    //비즈네르 암호
    var vigenere = Array<CharArray>(26) { CharArray(26) }
    var alphabet = 'a'
    for (i in 0..25) {
        for (j in 0..25) {
            vigenere[i][j] = alphabet
            alphabet ++
            if (alphabet > 'z') {
                alphabet = 'a'
            }
        }
        alphabet++
    }

    //평문, 키 입력받기
    print("평문을 입력하세요 : ")
    val input = readLine()
    print("키를 입력하세요 : ")
    val key = readLine()
    var keySort = mutableListOf<Char>()

    //키의 모음 갯수 체크
    for (i in key!!.indices) {
        if (vowel.contains(key[i])) {
            vowelCount ++
        }
        //키 순서를 통해 정렬을 위함
        keySort.add(key[i])
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

    //배열 생성
    val arrSize = if (input!!.length % key!!.length == 0) {
        input!!.length / key!!.length
    } else {
        input!!.length / key!!.length + 1
    }
    var array = Array<CharArray>(arrSize) { CharArray(key.length){ _ -> ' ' } }

    //키 순서 맵핑
    keySort.sort()
    val keyMap = mutableMapOf<Char, Int>()
    for (i in keySort.indices) {
        keyMap[keySort[i]] = i
    }

    //배열에 저장하기
    var countRow = 0
    var countColumn = 0
    for (i in input!!.indices) {
        array[countRow][keyMap[key[countColumn]]!!] = shiftInput[i]
        countColumn++
        if (countColumn >= key.length) {
            countColumn = 0
            countRow ++
        }
    }
    print("배열에 섞어서 저장 : ")
    println(array.contentDeepToString())

    //배열 세로로 읽어서 하나의 password 로 만들기
    var passwordBeforeVigenere = ""
    var password = ""
    for (i in key.indices) {
        for (j in 0 until arrSize) {
            passwordBeforeVigenere += array[j][i]
        }
    }
    print("비즈네르 이전 암호문 : ")
    println(passwordBeforeVigenere)

    //비즈네트 암호화
    var keyCount = 0
    for (i in passwordBeforeVigenere.indices) {
        if (passwordBeforeVigenere[i] == ' ') {
            password += ' '
        } else if (passwordBeforeVigenere[i] in 'A'..'Z') {
            password += if (key[keyCount] in 'A'..'Z') {
                vigenere[key[keyCount].toInt() - 65][passwordBeforeVigenere[i].toInt() - 65]
            } else {
                vigenere[key[keyCount].toInt() - 97][passwordBeforeVigenere[i].toInt() - 65]
            }
        } else if (passwordBeforeVigenere[i] in 'a'..'z') {
            password += if (key[keyCount] in 'A'..'Z') {
                vigenere[key[keyCount].toInt() - 65][passwordBeforeVigenere[i].toInt() - 97]
            } else {
                vigenere[key[keyCount].toInt() - 97][passwordBeforeVigenere[i].toInt() - 97]
            }
        }
        keyCount ++
        if (keyCount == key.length) {
            keyCount = 0
        }
    }

    println("완성된 암호문 : \'$password\'")
}