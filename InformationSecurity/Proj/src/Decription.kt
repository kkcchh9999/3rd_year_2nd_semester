
fun main(){

    //암호문, 키 입력받기
    print("암호문을 입력하세요 : ")
    val password = readLine()
    print("키를 입력하세요 : ")
    val key = readLine()
    var keySort = mutableListOf<Char>()

    //키의 모음 갯수 체크
    var vowelCount = 0
    val vowel = arrayOf('a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U')
    for (i in key!!.indices) {
        if (vowel.contains(key[i])) {
            vowelCount ++
        }
        //키 순서를 통해 정렬을 위함
        keySort.add(key[i])
    }

    //비즈네르 암호
    var vigenere = Array<CharArray>(26) { CharArray(26) }
    var alphabet = 'A'
    for (i in 0..25) {
        for (j in 0..25) {
            vigenere[i][j] = alphabet
            alphabet ++
            if (alphabet > 'Z') {
                alphabet = 'A'
            }
        }
        alphabet++
    }

    //비즈네르 암호 복호화
    var passwordBeforeVigenere: String = ""
    var keyCount = 0
    for (i in password!!.indices) {
        if (password[i] == ' ') {
            passwordBeforeVigenere += ' '
        } else {
            for (j in 0..25) {
                if (key[keyCount] in 'a'..'z') {
                    if (vigenere[key[keyCount].toInt()-97][j].toLowerCase() == password[i]) {
                        passwordBeforeVigenere += (j + 97).toChar()
                    }
                } else if (key[keyCount] in 'A'..'Z') {
                    if (vigenere[key[keyCount].toInt()-65][j] == password[i]) {
                        passwordBeforeVigenere += (j + 65).toChar()
                    }
                }
            }
        }
        keyCount ++
        if (keyCount == key.length) {
            keyCount = 0
        }
    }
    println("비즈네르 암호 복호화: [$passwordBeforeVigenere]")

    //암호문 배열에 세로로 넣기
    var passwordArray = Array<CharArray>(password!!.length / key.length) { CharArray(key.length) { _ -> ' ' } }
    var countRow = 0
    var countColumn = 0
    for (i in passwordBeforeVigenere.indices) {
        passwordArray[countColumn][countRow] = passwordBeforeVigenere[i]
        countColumn ++
        if (countColumn == password.length / key.length) {
            countColumn = 0
            countRow ++
        }
    }
    println("세로로 배열에 저장 : ${passwordArray.contentDeepToString()}")


    //키 순서 맵핑
    keySort.sort()
    val keyMap = mutableMapOf<Int, Char>()
    for (i in keySort.indices) {
        keyMap[i] = keySort[i]
    }

    var shiftInput = ""
    countRow = 0
    countColumn = 0
    for (i in passwordBeforeVigenere.indices) {
        for (j in key.indices) {
            if (key[countRow] == keyMap[j]) {
                shiftInput += passwordArray[countColumn][j]
            }
        }
        countRow ++
        if (countRow == key.length) {
            countRow = 0
            countColumn ++
        }
    }
    println("key 순서에 맞춰 배열에서 꺼내기 : $shiftInput")

    var input = ""
    //키의 모음 갯수만큼 카운트 and 시프트
    for (i in shiftInput.indices) {
        if (shiftInput[i] in 'a'..'z') {
            input += if (shiftInput[i] - vowelCount < 'a') {
                (shiftInput[i]-vowelCount+26)
            } else {
                (shiftInput[i]-vowelCount)
            }
        } else if (shiftInput [i] in 'A'..'Z') {
            input += if (shiftInput[i] - vowelCount < 'A') {
                (shiftInput[i]-vowelCount+26)
            } else {
                (shiftInput[i]-vowelCount)
            }
        }
    }
    println("복구된 평문 : $input")
}