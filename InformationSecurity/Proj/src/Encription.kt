
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
    //a e l p p
    //a p p l e
    //1 4 5 3 2

    val keySequence: MutableList<Int> = emptyList<Int>().toMutableList()
    for (i in key.indices) {
        for (j in keySort.indices) {
            if (key[i] == keySort[j]) {
                keySequence.add(j)
                keySort[j]=' '
            }
        }
    }
    //키 순서?
    println("키 순서: $keySequence")

    var startPoint = 0
    var endPoint = key.length-1
    var tmpString = ""
    var sortInput = ""
    val keyDev5 = if (shiftInput.length % key.length == 0) {
        shiftInput.length / key.length
    } else {
        shiftInput.length / key.length + 1
    }
    for (i in 0 until keyDev5) {//전치해서 추가 키 자리수로 나누어 떨어지는 부분들에 대해서
        if (endPoint < shiftInput.length) {
            tmpString = shiftInput.substring(startPoint, endPoint+1)
            for (j in tmpString.indices) {
                for (k in keySequence.indices) {
                    if (keySequence[k] == j) {
                        sortInput += tmpString[k]
                        break
                    }
                }
            }
        } else {//마지막부분 키 남은만큼 짤라서
            tmpString = shiftInput.substring(startPoint)
            val tmpKey = key.substring(0, shiftInput.length % key.length)
            val tmpKeySort: MutableList<Char> = emptyList<Char>().toMutableList()
            for (i in tmpKey.indices) {
                tmpKeySort.add(tmpKey[i])
            }
            tmpKeySort.sort()
            //다시 정렬하고
            val tmpKeySequence: MutableList<Int> = emptyList<Int>().toMutableList()
            for (i in tmpKey.indices) {
                for (j in tmpKeySort.indices) {
                    if (tmpKey[i] == tmpKeySort[j]) {
                        tmpKeySequence.add(j)
                        tmpKeySort[j]=' '
                    }
                }
            }
            //정렬한거 순서 찾아서
            for (j in tmpString.indices) {
                for (k in tmpKeySequence.indices) {
                    if (tmpKeySequence[k] == j) {
                        sortInput += tmpString[k]
                        break
                    }
                }
            }
            //적용하기
        }
        startPoint += key.length
        endPoint += key.length
    }
    println("전치: $sortInput")

    var codeInput: String = ""
    var tmpCodeKey: String = ""
    for (i in sortInput.indices) {
        codeInput += sortInput[i].code
    }
    for (i in key.indices) {
        tmpCodeKey += key[i].code
    }
    var codeKey = ""
    startPoint = 0
    while (codeInput.length != codeKey.length) {
        codeKey += tmpCodeKey[startPoint]

        if (startPoint == tmpCodeKey.length - 1) {
            startPoint = 0
        } else {
            startPoint ++
        }
    }
    println("아스키코드: $codeInput, 길이: ${codeInput.length}")
    println("아스키코드 키: $codeKey")

    var binaryInput = ""
    var binaryKey = ""
    for (i in codeInput.indices) {
        //이진수로
        tmpString = Integer.toBinaryString(codeInput[i]-'0')
        //4자리 맞추기
        while (tmpString.length < 4) {
            tmpString = "0$tmpString"
        }
        binaryInput += tmpString
        //이진수로
        tmpCodeKey = Integer.toBinaryString(codeKey[i]-'0')
        //4자리 맞추기
        while (tmpCodeKey.length < 4) {
            tmpCodeKey = "0$tmpCodeKey"
        }
        binaryKey += tmpCodeKey
    }
    println("binaryInp: $binaryInput")
    println("binaryKey: $binaryKey")

    var xorResult = ""
    for (i in binaryInput.indices) {
        xorResult += if (binaryKey[i] == binaryInput[i]) {
            "0"
        } else {
            "1"
        }
    }
    println("xorResult: $xorResult")
    print("lastlyOut: ")
    var lastlyOut = ""
    for (i in xorResult.indices step 2) {
        lastlyOut += (xorResult[i] - '0') * 2 + (xorResult[i+1] - '0')
        print(" ")
        print((xorResult[i] - '0') * 2 + (xorResult[i+1] - '0'))
    }
    print("\n")
    println("암호문: $lastlyOut")
}