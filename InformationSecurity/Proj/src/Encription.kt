
fun main() {

    var shiftInput: String= ""

    //평문, 키 입력받기
    print("평문을 입력하세요 : ")
    val input = readLine()
    print("키를 입력하세요 : ")
    val key = readLine()
    val keySort = mutableListOf<Char>()

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


    println("shift 결과 : $shiftInput")

    //키 정렬
    for (i in key.indices) {
        keySort.add(key[i])
    }
    keySort.sort()
    println("정렬된 키: $keySort")
    //a p p l e
    //a e l p p
    //0 4 3 1 2
    var check = 0
    val keyIntArr = arrayOfNulls<Int>(key.length)
    for (i in keySort.indices) {
        if (check != 0) {
            check --
        } else {
            for (j in key.indices) {
                if (i != keySort.size - 1) {    //사이즈 문제
                    if (keySort[i] != keySort[i + 1]) {
                        if (key[j] == keySort[i]) {
                            keyIntArr[j] = i
                            break
                        }
                    } else {
                        for (k in 1 until keySort.size - i) {
                            if (keySort[i] == keySort[i + k]) {
                                check++
                                println("Check: $check")
                            } else {
                                break
                            }
                        }

                        if (key[j] == keySort[i]) {
                            for (k in 0..check) {
                                println("Check2: $check")
                                keyIntArr[j] = i + k
                            }
                        }
                    }
                } else {
                    if (key[i] == keySort[j]) {
                        keyIntArr[i] = j
                    }
                }
            }
        }
    }
    println("키의 순서: ${keyIntArr.contentDeepToString()}")

    //배열 생성
    val arrSize = if (input.length % key.length == 0) {
        input.length / key.length
    } else {
        input.length / key.length + 1
    }
    var array = Array(arrSize) { CharArray(key.length) { _ -> ' '} }

      //배열에 저장하기
//    var countRow = 0
//    var countColumn = 0
//    for (i in input.indices) {
//        array[countRow][keyIntList[]] = shiftInput[i]
//        countColumn++
//        if (countColumn >= key.length) {
//            countColumn = 0
//            countRow ++
//        }
//    }
    print("배열에 섞어서 저장 : ")
    println(array.contentDeepToString())

}