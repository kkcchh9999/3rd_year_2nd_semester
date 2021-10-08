
fun main(){

    //암호문, 키 입력받기
    print("암호문을 입력하세요 : ")
    val input = readLine()
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
}