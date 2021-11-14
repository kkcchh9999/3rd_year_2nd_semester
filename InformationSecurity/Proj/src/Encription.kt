
fun main() {

    var shiftInput: String= ""

    //평문, 키 입력받기
    print("평문을 입력하세요 : ")
    val input = readLine()
    print("키를 입력하세요 : ")
    val key = readLine()
    val keySort = mutableListOf<Char>()
    val keyMap = mutableMapOf<Char, Int>()

    //시프트
    if (input != null && key != null) {
        var j = 0
        for (i in input.indices) {
            if (input[i] !in 'A'..'Z') {
                println("평문은 대문자만 입력 가능합니다")
                return
            }

            shiftInput += if (input[i] + (key[j].code % 26) > 'Z') {
                (input[i] + (key[j].code % 26) - 26)
            } else {
                (input[i] + (key[j].code % 26))
            }

            j ++
            if (j == key.length) {
                j = 0
            }
        }
    } else {
        println("평문, 키를 입력하세요!!!")
        return
    }

    //키 정렬
    println(shiftInput)
    for (i in key.indices) {
        keySort.add(key[i])
    }
    keySort.sort()
    println(keySort)

    //배열 생성
    val arrSize = if (input.length % key.length == 0) {
        input.length / key.length
    } else {
        input.length / key.length + 1
    }
    var array = Array(arrSize) { CharArray(key.length) }

      //배열에 저장하기
    var countRow = 0
    var countColumn = 0
    for (i in input.indices) {

        //중복 처리 HOW??????????

//        array[countRow][keyMap[key[countColumn]]!!] = shiftInput[i]
//        countColumn++
//        if (countColumn >= key.length) {
//            countColumn = 0
//            countRow ++
//        }
    }
    print("배열에 섞어서 저장 : ")
    println(array.contentDeepToString())

}